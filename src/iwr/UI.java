package iwr;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Hlavní program
 * @author Tomáš
 *
 */
public class UI implements Runnable {

	JFrame mainFrame;
	
	JPanel players;
	JList playersChooser;
	PlayerPane playerInfo;
	JTextField jumpCount;
	JCheckBox obeyVisibilityRules;
	JLabel moves;
	JList moveList;
	
	MapPane mapPane;
	
	Game game;
	Player activePlayer;
	
	int time;
	int totalTime;
	
	File load = null;

	private int visibleRows = 5;

	private JComboBox what;
	
	public UI(String[] args) {
		if (args.length > 0) {
			load = new File(args[0]);
		}
	}


	public void repaint()
	{
		if (game == null) return;
		setGame(game); //TODO efektivita
		mapPane.setTime(time);
		totalTime = game.length;
		moves.setText(time+"/"+totalTime/*+((time>0)?(":"+game.events.get(time-1)):"")*/); 
		moveList.setSelectedIndex(time-1);
		moveList.ensureIndexIsVisible(time-1);
		mainFrame.repaint();
	}
	
	
	public void createAndShowUI()
	{
		mainFrame = new JFrame(Messages.getString("UI.IWReview")); //$NON-NLS-1$
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setIconImage(Images.get(Images.I_YELLOW).getImage());
		JPanel pane = new JPanel(new BorderLayout());
		
		pane.add(/*new JScrollPane(*/getField()/*)*/, BorderLayout.CENTER);
		pane.add(getControls(), BorderLayout.SOUTH);
		
		moveList = new JList();
		moveList.setLayoutOrientation(JList.VERTICAL);
		moveList.setVisibleRowCount(visibleRows );
		moveList.setAutoscrolls(true);
		moveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		moveList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setTime(moveList.getSelectedIndex()+1);
				repaint();
			}
		});
		pane.add(new JScrollPane(moveList), BorderLayout.NORTH);
		
		createPlayers();
		pane.add(players, BorderLayout.WEST);
		
		mainFrame.add(pane);
		mainFrame.setJMenuBar(getMenu());
		mainFrame.pack();
		//mainFrame.setSize(800, 600);
		mainFrame.setVisible(true);
		if (load != null) {
			loadGame(load);
			load = null;
		}
		mainFrame.setTransferHandler(new TransferHandler() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8957222391827348684L;
			@Override
			public boolean canImport(TransferSupport support) {
				if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) return true;
				return super.canImport(support);
			}
			@SuppressWarnings("unchecked")
			@Override
			public boolean importData(TransferSupport support) {
				DataFlavor df = DataFlavor.javaFileListFlavor;
				Transferable t = support.getTransferable();
				File f;
				if (t.isDataFlavorSupported(df)) {
					try {
						f = ((List<File>) t.getTransferData(df)).get(0);
						loadGame(f);
						return true;
					} catch (UnsupportedFlavorException e) {
						return false;
					} catch (IOException e) {
						return false;
					}
				}
				return false;
			}
		});

	}
	
	private void createPlayers() {
		players = new JPanel();
		players.setLayout(new BorderLayout());
		
		playersChooser = new JList();
		playersChooser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		players.add(playersChooser, BorderLayout.NORTH);
		
		
		playersChooser.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				activePlayer = (Player) playersChooser.getSelectedValue();
				mapPane.setPlayer(activePlayer);
				playerInfo.setPlayer(activePlayer);
				repaint();				
			}
		});
		
		playerInfo = new PlayerPane(this);

		players.add(playerInfo, BorderLayout.SOUTH);
		//players.add(playerInfo);
	}
	
	public void setPlayers(Map<Integer, Player> map) {
		playersChooser.removeAll();
		if (map == null) return;
		playersChooser.setListData(map.values().toArray());
	}
	
	private JPanel getControls() {
		JPanel pane = new JPanel(new GridLayout(1, 0));
		
		//pane.add(new JButton("<<"));
		JButton prev = new JButton(Messages.getString("UI.Back")); //$NON-NLS-1$
		prev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				prev();
			}
		});
		pane.add(prev);
		jumpCount = new JTextField("1"); 
		pane.add(jumpCount);
		
		what = new JComboBox();
		what.addItem(Shift.MOVE);
/*		cb.addItem("s");
		cb.addItem("min");
		cb.addItem("h");
		cb.addItem("dní");*/
		what.addItem(Shift.KILL);
		pane.add(what);
		moves = new JLabel();
		pane.add(moves, BorderLayout.EAST);
		
		JButton toEndOfProt = new JButton(Messages.getString("UI.EndPeace")); //$NON-NLS-1$
		toEndOfProt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setTime(game.protection);
				repaint();
				
			}
		});
		pane.add(toEndOfProt);
		
		obeyVisibilityRules = new JCheckBox(Messages.getString("UI.FOW")); //$NON-NLS-1$
		obeyVisibilityRules.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				mapPane.setObeyVisibilityRules(arg0.getStateChange() == ItemEvent.SELECTED);
				repaint();
			}
		});
		obeyVisibilityRules.setEnabled(false);
		pane.add(obeyVisibilityRules);
		JButton next = new JButton(Messages.getString("UI.Fwd")); //$NON-NLS-1$
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});
		pane.add(next);
		//pane.add(new JButton(">>"));
		
		return pane;
	}
	
	protected void next() {
		switch ((Shift)what.getSelectedItem()) {
		case MOVE:
			setTime(time+Integer.parseInt(jumpCount.getText()));
			if (time > totalTime) setTime(totalTime);
			break;
		case KILL:
			Integer newtime = AttackEvent.kills.higherKey(time+1); 
			if (newtime != null) {
				setTime(newtime-1);
			}
			what.setSelectedItem(Shift.MOVE);
			break;
		}
		repaint();
	}
	
	protected void prev() {
		switch ((Shift)what.getSelectedItem()) {
		case MOVE:
			setTime(time-Integer.parseInt(jumpCount.getText()));
			if (time < 0) setTime(0);
			break;
		case KILL:
			Integer newtime = AttackEvent.kills.floorKey(time); 
			if (newtime != null) {
				setTime(newtime-1);
			}
			what.setSelectedItem(Shift.MOVE);
			break;
		}
		repaint();
	}

	protected void loadGame(File file) {
		game = new Game(file);
		setTime(0);
		setGame(game);
		moveList.setListData(game.events.toArray());
		setPlayers(game.getPlayers());
		mainFrame.pack();
		repaint();
	}
	
	private JMenuBar getMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu(Messages.getString("UI.File")); //$NON-NLS-1$
		JMenuItem open = new JMenuItem(Messages.getString("UI.Open")); //$NON-NLS-1$
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("."); 
				chooser.setFileFilter(new FileNameExtensionFilter(Messages.getString("UI.IWRRecords"), "iwr", "xml")); //$NON-NLS-1$  
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
						loadGame(chooser.getSelectedFile());
			        }
			}
		});
		
		JMenu game = new JMenu(Messages.getString("UI.Game")); //$NON-NLS-1$
		JMenuItem refresh = new JMenuItem(Messages.getString("UI.Refresh")); //$NON-NLS-1$
		refresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		JMenu help = new JMenu(Messages.getString("UI.Help")); //$NON-NLS-1$
		
		
		bar.add(file);
		file.add(open);
		bar.add(game);
		game.add(refresh);
		bar.add(help);
		
		return bar;
		
	}
	
	protected void setTime(int t) {
		time = t;
		mapPane.setTime(time);
		playerInfo.setTime(time);
	}


	private void setGame(Game g) {
		mapPane.setGame(g);	
	}
	
	private MapPane getField() {
		mapPane = new MapPane();
		mapPane.setToolTipText(Messages.getString("UI.Map")); //$NON-NLS-1$
		
		mapPane.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mapPane.setActiveField(e.getPoint());
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				
			}
		});
		mapPane.setObeyVisibilityRules(false);
		return mapPane;
	}
	
	@Override
	public void run() {
		createAndShowUI();
		
	}

}
