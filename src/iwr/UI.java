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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Hlavní program
 * 
 * @author Tomáš
 * 
 */
public class UI implements Runnable {


	/**
	 * Hlavní okno
	 */
	private JFrame mainFrame;

	/**
	 * Panel s hráči
	 */
	private JPanel players;
	/**
	 * Výběr hráčů
	 */
	private JList playersChooser;
	/**
	 * Informace o hráči
	 */
	private PlayerPane playerInfo;
	/**
	 * O kolik se má ve hře posouvat
	 */
	private JTextField jumpCount;
	/**
	 * Má se dodržovat viditelnost?
	 */
	private JCheckBox obeyVisibilityRules;
	/**
	 * Zobrazení počtu akcí a jejich času
	 */
	private JLabel moves;
	/**
	 * Seznam akcí
	 */
	private JList moveList;

	/**
	 * Zobrazení mapy
	 */
	private MapPane mapPane;

	/**
	 * Zaznamenaná hra
	 */
	private Game game;
	/**
	 * Aktivní hráč
	 */
	private Player activePlayer;

	/**
	 * Aktuální herní čas
	 */
	private int time;
	
	/**
	 * Aktuální skutečný čas
	 */
	private Calendar calendar = new GregorianCalendar();
	/**
	 * Délka hry
	 */
	private int totalTime;

	/**
	 * Soubor, ze kterého se má po spuštění načíst hra
	 */
	private File load = null;

	/**
	 * Počet řádek, viditelných v seznamu akcí
	 */
	private int visibleRows = 5;

	/**
	 * Výběr způsobu pohybu ve hře
	 */
	private JComboBox what;

	/**
	 * Panel s ovládáním posunu ve hře
	 */
	private JPanel controls;

	/**
	 * Seskupení ovládání a statusbaru
	 */
	private JPanel bottom;

	/**
	 * Zobrazení tahů a času
	 */
	private JPanel statusbar;

	/**
	 * Hlavní menu programu
	 */
	private JMenuBar menu;

	/**
	 * Tlačítko posunu zpět
	 */
	private JButton prev;

	/**
	 * Tlačítko posunu vpřed
	 */
	private JButton next;
	
	/**
	 * Tlačítko skoku na konec protekce
	 */
	private JButton toEndOfProt;

	/**
	 * 
	 * @param args Parametry předané programu
	 */
	public UI(String[] args) {
		if (args.length > 0) {
			load = new File(args[0]);
		}
	}

	/**
	 * Překreslí okno
	 */
	public void repaint() {
		if (game == null)
			return;
		//setGame(game); // nejspíš nepotřebné
		mapPane.setTime(time);
		totalTime = game.length;
		moves.setText(NodeUtil.date.format(calendar.getTime()) + " ~ " + time + "/" + totalTime/*
											 * +((time>0)?(":"+game.events.get(time
											 * -1)):"")
											 */);
		if (time > 0) { 
			moveList.setSelectedIndex(time-1);
			moveList.ensureIndexIsVisible(time-1);
		} else {
			moveList.setSelectedIndices(new int[0]);
		}
		mainFrame.repaint();
	}

	/**
	 * Vytvoření okna a podoken 
	 */
	public void createAndShowUI() {
		mainFrame = new JFrame(Messages.getString("UI.IWReview")); //$NON-NLS-1$
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setIconImage(Images.get(Images.I_YELLOW).getImage());
		JPanel pane = new JPanel(new BorderLayout());

		createField();
		pane.add(/* new JScrollPane( */mapPane/* ) */, BorderLayout.CENTER);
		
		createBottom();
		pane.add(bottom, BorderLayout.SOUTH);

		moveList = new JList();
		moveList.setLayoutOrientation(JList.VERTICAL);
		moveList.setVisibleRowCount(visibleRows);
		moveList.setAutoscrolls(true);
		moveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		moveList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				setTime(moveList.getSelectedIndex() + 1);
				repaint();
			}
		});
		pane.add(new JScrollPane(moveList), BorderLayout.NORTH);

		createPlayers();
		pane.add(players, BorderLayout.WEST);

		mainFrame.add(pane);
		createMenu();
		mainFrame.setJMenuBar(menu);
		mainFrame.pack();
		// mainFrame.setSize(800, 600);
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
				if (support
						.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
					return true;
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
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);
	}

	/**
	 * Vytvoření panelu pro výběr hráčů a zobrazení jejich informací
	 */
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
		// players.add(playerInfo);
	}

	/**
	 * Nastaví seznam hráčů
	 * @param map Seznam hráčů
	 */
	public void setPlayers(Map<Integer, Player> map) {
		playersChooser.removeAll();
		if (map == null)
			return;
		playersChooser.setListData(map.values().toArray());
	}

	/**
	 * Vytvoření spodního panelu (statusbar + ovládání)
	 */
	private void createBottom() {
		bottom = new JPanel(new GridLayout(0,1));
		createStatusbar();
		bottom.add(statusbar);
		createControls();
		bottom.add(controls);
	}

	/**
	 * Vytvoření statusbaru - zobrazení času a tahů
	 */
	private void createStatusbar () {
		statusbar = new JPanel();
		moves = new JLabel();
		statusbar.add(moves);

	}

	/**
	 * Vytvoření panelu pro ovládání
	 */
	private void createControls() {
		controls = new JPanel(new GridLayout(1, 0));

		prev = new JButton(Messages.getString("UI.Back"));
		prev.setEnabled(false);
		prev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				prev(Integer.parseInt(jumpCount.getText()));
			}
		});
		controls.add(prev);
		jumpCount = new JTextField("1");
		controls.add(jumpCount);

		what = new JComboBox();
		for (Shift item: Shift.values()) {
			what.addItem(item);
		}
		
		controls.add(what);

		toEndOfProt = new JButton(Messages.getString("UI.EndPeace"));
		toEndOfProt.setEnabled(false);
		toEndOfProt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game != null) {
					setTime(game.protection);
					repaint();
				}

			}
		});
		controls.add(toEndOfProt);

		obeyVisibilityRules = new JCheckBox(Messages.getString("UI.FOW")); //$NON-NLS-1$
		obeyVisibilityRules.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				mapPane
						.setObeyVisibilityRules(arg0.getStateChange() == ItemEvent.SELECTED);
				repaint();
			}
		});
		obeyVisibilityRules.setEnabled(false);
		controls.add(obeyVisibilityRules);
		next = new JButton(Messages.getString("UI.Fwd"));
		next.setEnabled(false);
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				next(Integer.parseInt(jumpCount.getText()));
			}
		});
		controls.add(next);
		controls.setEnabled(false);
		// pane.add(new JButton(">>"));

	}

	/**
	 * Posune se ve hře dopředu
	 * @param amount Počet akcí, o které se posouvá
	 */
	protected void next(int amount) {
		switch ((Shift) what.getSelectedItem()) {
		case MOVE:
			setTime(Math.min(
				time + amount,
				totalTime
			));
			break;
		case KILL:
			Integer newtime = AttackEvent.kills.higherKey(time + 1);
			if (newtime != null) {
				setTime(newtime - 1);
			}
			what.setSelectedItem(Shift.MOVE);
			break;
		case HOUR:
			calendar.add(Calendar.HOUR, amount);
			jumpToTimestamp();
			break;
		case MINUTE:
			calendar.add(Calendar.MINUTE, amount);
			jumpToTimestamp();
			break;
		case SECOND:
			calendar.add(Calendar.SECOND, amount);
			jumpToTimestamp();
			break;
		}
		repaint();
	}

	/**
	 * Posune se ve hře dozadu
	 * @param amount počet akcí, o které se posouvá
	 */
	protected void prev(int amount) {
		switch ((Shift) what.getSelectedItem()) {
		case MOVE:
			setTime(Math.max(
					time - amount,
					0
			));
			break;
		case KILL:
			Integer newtime = AttackEvent.kills.floorKey(time);
			if (newtime != null) {
				setTime(newtime - 1);
			}
			what.setSelectedItem(Shift.MOVE);
			break;
		case HOUR:
			calendar.add(Calendar.HOUR, -amount);
			jumpToTimestamp();
			break;
		case MINUTE:
			calendar.add(Calendar.MINUTE, -amount);
			jumpToTimestamp();
			break;
		case SECOND:
			calendar.add(Calendar.SECOND, -amount);
			jumpToTimestamp();
			break;

		}
		repaint();
	}

	/**
	 * Načte hru ze souboru
	 * @param file Soubor se záznamem
	 */
	protected void loadGame(File file) {
		try {
			game = new Game(file);
			setTime(1);
			setGame(game);
			moveList.setListData(game.events.toArray());
			setPlayers(game.getPlayers());
			mainFrame.pack();
			controls.setEnabled(true);
			next.setEnabled(true);
			prev.setEnabled(true);
			toEndOfProt.setEnabled(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Nepodařilo se načíst hru", null, JOptionPane.ERROR_MESSAGE);
		}
		repaint();
	}

	/**
	 * Vytvoří menu
	 */
	private void createMenu() {
		menu = new JMenuBar();
		JMenu file = new JMenu(Messages.getString("UI.File")); //$NON-NLS-1$
		JMenuItem open = new JMenuItem(Messages.getString("UI.Open")); //$NON-NLS-1$
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileFilter(new FileNameExtensionFilter(Messages
						.getString("UI.IWRRecords"), "iwr", "xml")); //$NON-NLS-1$  
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					loadGame(chooser.getSelectedFile());
				}
			}
		});

//		JMenu game = new JMenu(Messages.getString("UI.Game")); //$NON-NLS-1$
//		JMenuItem refresh = new JMenuItem(Messages.getString("UI.Refresh")); //$NON-NLS-1$
//		refresh.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				repaint();
//			}
//		});
//		JMenu help = new JMenu(Messages.getString("UI.Help")); //$NON-NLS-1$

		menu.add(file);
		file.add(open);
//		menu.add(game);
//		game.add(refresh);
//		menu.add(help);
	}

	/**
	 * Nastaví herní čas
	 * @param time Herní čas
	 */
	protected void setTime(int time) {
		this.time = time;
		try {
			calendar.setTime(game.events.get(time-1).timestamp);
		} catch (ArrayIndexOutOfBoundsException e) {
			calendar.setTime(game.timeOfEvents.firstKey());
		}
		mapPane.setTime(time);
		playerInfo.setTime(time);
	}
	
	/**
	 * Skočí na tah, určený aktuálním nastaveným časem
	 */
	private void jumpToTimestamp() {
		this.time = game.timeOfEvents.floorEntry(calendar.getTime()).getValue();
		mapPane.setTime(time);
		playerInfo.setTime(time);
	}
	
	/**
	 * Nastaví skutečný čas
	 * @param timestamp Čas
	 */
	protected void setTimestamp(Date timestamp) {
		this.calendar.setTime(timestamp);
		jumpToTimestamp();
	}

	/**
	 * Nastaví aktuální hru
	 * @param game Hra
	 */
	private void setGame(Game game) {
		mapPane.setGame(game);
	}

	/**
	 * Vytvoří pole pro zobrazení mapy
	 */
	private void createField() {
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
	}

	/**
	 * @return the obeyVisibilityRules
	 */
	public JCheckBox getObeyVisibilityRules() {
		return obeyVisibilityRules;
	}

	@Override
	public void run() {
		createAndShowUI();

	}

}
