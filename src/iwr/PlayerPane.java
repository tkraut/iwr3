package iwr;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3857819570317137989L;

	protected Player player = null;

	protected int time;

	protected JLabel neq, moves, hq, received, killed;

	private JButton kill;

	private UI main;

	@Override
	protected void paintComponent(Graphics g) {
		if (player != null) {
			neq.setText(String.valueOf(player.nectarAt(time)));
			moves.setText(String.valueOf(player.movesAt(time)));
			received.setText(String.valueOf(player.receivedAt(time)));
			killed.setText(String.valueOf(player.fragsAt(time)));
			Field f = player.getHq(time);
			if (f == null) {
				hq.setText(Messages.getString("PlayerPane.-")); //$NON-NLS-1$
			} else {
				hq.setText(player.getHq(time).toString());
			}
			kill.setEnabled(player.death != 0);
		} else {
			kill.setEnabled(false);
		}
		super.paintComponent(g);

	}

	public PlayerPane(final UI ui) {
		main = ui;
		setLayout(new GridLayout(0, 2));
		add(new JLabel(Messages.getString("PlayerPane.nectar"))); //$NON-NLS-1$
		neq = new JLabel();
		add(neq);
		add(new JLabel(Messages.getString("PlayerPane.moves"))); //$NON-NLS-1$
		moves = new JLabel();
		add(moves);
		add(new JLabel(Messages.getString("PlayerPane.hq"))); //$NON-NLS-1$
		hq = new JLabel();
		add(hq);
		add(new JLabel(Messages.getString("PlayerPane.received"))); //$NON-NLS-1$
		received = new JLabel();
		add(received);
		add(new JLabel(Messages.getString("PlayerPane.frags"))); //$NON-NLS-1$
		killed = new JLabel();
		add(killed);
		add(new JLabel(Messages.getString("PlayerPane.toDeath"))); //$NON-NLS-1$
		kill = new JButton(Messages.getString("PlayerPane.>>")); //$NON-NLS-1$
		kill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (player != null && player.death != 0) {
					main.setTime(player.beforeDeath());
					main.repaint();
				}
			}
		});
		add(kill);
	}

	public void setPlayer(Player p) {
		player = p;
		if (player == null) {
			main.obeyVisibilityRules.setEnabled(false);
		} else {
			main.obeyVisibilityRules.setEnabled(true);
		}
	}

	public void setTime(int t) {
		time = t;
	}

	/*
	 * @Override public Dimension getPreferredSize() { if (player != null) {
	 * return new Dimension(super.getPreferredSize().width,
	 * neq.getPreferredSize().height*6); } return super.getPreferredSize(); }
	 */

}
