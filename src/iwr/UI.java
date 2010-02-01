package iwr;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.SortedMap;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Hlavní program
 * @author Tomáš
 *
 */
public class UI implements Runnable {

	JFrame mainFrame;
	
	JPanel players;
	JComboBox playersChooser;
	JTextField jumpCount;
	
	JLabel moves;
	
	MapPane mapPane;
	
	Game game;
	Player activePlayer;
	
	int time;
	int totalTime;
	
	public void repaint()
	{
		if (game == null) return;
		setMap(game.map); //TODO efektivita
		mapPane.setTime(time);
		setPlayers(game.getPlayers());
		totalTime = game.length;
		moves.setText(time+"/"+totalTime+((time>0)?(":"+game.events.get(time-1)):""));
		mainFrame.repaint();
	}
	
	
	public void createAndShowUI()
	{
		mainFrame = new JFrame("IWReview");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pane = new JPanel(new BorderLayout());
		
		pane.add(new JScrollPane(getField()), BorderLayout.CENTER);
		pane.add(getControls(), BorderLayout.SOUTH);
		
		moves = new JLabel();
		pane.add(moves, BorderLayout.NORTH);
		
		createPlayers();
		pane.add(players, BorderLayout.WEST);
		
		mainFrame.add(pane);
		mainFrame.setJMenuBar(getMenu());
		mainFrame.pack();
		//mainFrame.setSize(800, 600);
		mainFrame.setVisible(true);
	}
	
	private void createPlayers() {
		players = new JPanel();
		
		playersChooser = new JComboBox();
		
		players.add(playersChooser);
		
		playersChooser.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				activePlayer = ((Player)arg0.getItem());
				
			}
		});
		
	}
	
	public void setPlayers(SortedMap<Integer, Player> map) {
		playersChooser.removeAllItems();
		if (map == null) return;
		for (Player player:map.values()) {
			playersChooser.addItem(player);
		}
	}
	
	private JPanel getControls() {
		JPanel pane = new JPanel(new GridLayout(1, 0));
		
		pane.add(new JButton("<<"));
		JButton prev = new JButton("<");
		prev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				prev();
			}
		});
		pane.add(prev);
		jumpCount = new JTextField("1");
		pane.add(jumpCount);
		
		JComboBox cb = new JComboBox();
		cb.addItem("tahů");
/*		cb.addItem("s");
		cb.addItem("min");
		cb.addItem("h");
		cb.addItem("dní");
		cb.addItem("vyřazení");*/
		pane.add(cb);
		JButton next = new JButton(">");
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});
		pane.add(next);
		pane.add(new JButton(">>"));
		
		return pane;
	}
	
	protected void next() {
		time = time+Integer.parseInt(jumpCount.getText());
		if (time > totalTime) time = totalTime;
		repaint();
	}
	
	protected void prev() {
		time = time-Integer.parseInt(jumpCount.getText());
		if (time < 0) time = 0;
		repaint();
	}


	private JMenuBar getMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("Soubor");
		JMenuItem open = new JMenuItem("Otevřít");
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileFilter(new FileNameExtensionFilter("IWR záznamy", "iwr", "xml"));
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
						game = new Game(chooser.getSelectedFile());
						setTime(0);
						setMap(game.map);
						mainFrame.pack();
						repaint();
			        } else {
			            System.out.println("Open command cancelled by user.");
			        }
			}
		});
		
		JMenu game = new JMenu("Hra");
		JMenuItem refresh = new JMenuItem("Obnovit");
		refresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		JMenu help = new JMenu("Nápověda");
		
		
		bar.add(file);
		file.add(open);
		bar.add(game);
		game.add(refresh);
		bar.add(help);
		
		return bar;
		
	}
	
	protected void setTime(int t) {
		time = t;
	}


	private void setMap(Map m) {
		mapPane.setMap(m);	
	}
	
	private MapPane getField() {
		return (mapPane = new MapPane());
	}
	
	@Override
	public void run() {
		createAndShowUI();
		
	}

}
