package iwr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -83643453752262620L;
	private static final int fHeight = 30;
	private static final int fWidth = 30;
	private Game game;
	private Map map;
	private Player player;
	private int time;
	protected Field activeField, selectedField;
	private boolean obeyVisibilityRules;
	private Set<Field> visibility;

	private int width() {
		return fWidth * map.width;
	}
	
	private int height() {
		return fHeight * map.height;
	}
	
	public void setGame(Game g) {
		game = g;
		map = g.map;
	}
	
	public void setPlayer(Player p) {
		player = p;
	}
	
	public void setTime(int t) {
		time = t;
	}
	
	public void setObeyVisibilityRules(boolean obeyVisibilityRules) {
		this.obeyVisibilityRules = obeyVisibilityRules;
	}

	public boolean obeysVisibilityRules() {
		return obeyVisibilityRules && player != null;
	}

	protected void paintCell(Graphics2D g, int i, int j) {
		Field f = map.fieldAt(i, j);
		for (ImageIcon img:f.imageForAt(player, time, obeysVisibilityRules(), visibility)) {
			g.drawImage(img.getImage(), i*fWidth, j*fHeight, img.getImageObserver());
		}
	}
	
	protected void setActiveField(Point coords) {
		if (map == null) return;
		int x = coords.x/fWidth;
		int y = coords.y/fHeight;
		if (x >= map.width || y >= map.height) {
			activeField = null;
			setToolTipText(""); //$NON-NLS-1$
		} else {
			activeField = map.fieldAt(x, y);
			String text = activeField.getSCoords() + ", " + activeField.typeAt(time); //$NON-NLS-1$
			if (activeField.ownerAt(time) != null) {
				text += ", " + activeField.ownerAt(time); //$NON-NLS-1$
				if (activeField.ownerAt(time).getHq(time) == activeField) {
					text += ", " + Messages.getString("MapPane.hq"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			if (activeField.armyAt(time) != null) {
				text += ", " + activeField.armyAt(time); //$NON-NLS-1$
			}
			setToolTipText(text);
		}
	}
	

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		if (map==null) {
			clearMap(gr);
		} else {
			if (obeysVisibilityRules()) countVisibility();
			for (int i = 0; i < map.width; ++i) {
				for (int j = 0; j < map.height; ++j) {
					paintCell(gr, i, j);
				}
			}
		}
	}
	
	private void countVisibility() {
		visibility = new HashSet<Field>();
		for (Player p:game.players.values()) {
			if (p.type == player.type) {
				visibility.addAll(p.visibleFieldsAt(time));
			}
		}
		
	}

	protected void clearMap(Graphics2D g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (map == null) return super.getPreferredSize();
		return new Dimension(width()+5*fWidth, height()+5*fHeight);
	}
	
	

}
