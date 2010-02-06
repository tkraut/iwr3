package iwr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

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
	
	@Override
	protected void paintComponent(Graphics g) {
		if (player != null) {
			neq.setText(String.valueOf(player.nectarAt(time)));
			moves.setText(String.valueOf(player.movesAt(time)));
			received.setText(String.valueOf(player.receivedAt(time)));
			killed.setText(String.valueOf(player.fragsAt(time)));
			Field f = player.getHq(time);
			if (f == null) {
				hq.setText("-");
			} else {
				hq.setText(player.getHq(time).toString());
			}
		}
		super.paintComponent(g);
		
	}
	
	public PlayerPane() {
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Nektar: "));
		neq = new JLabel();
		add(neq);
		add(new JLabel("Tahy: "));
		moves = new JLabel();
		add(moves);
		add(new JLabel("Velení: "));
		hq = new JLabel();
		add(hq);
		add(new JLabel("Obdržel nektaru: "));
		received = new JLabel();
		add(received);
		add(new JLabel("Vyřadil protivníků: "));
		killed = new JLabel();
		add(killed);
	}
	
	public void setPlayer(Player p) {
		player = p;
	}

	public void setTime(int t) {
		time = t;
	}
	
	/*@Override
	public Dimension getPreferredSize() {
		if (player != null) {
			return new Dimension(super.getPreferredSize().width, neq.getPreferredSize().height*6);
		}
		return super.getPreferredSize();
	}*/

	
}
