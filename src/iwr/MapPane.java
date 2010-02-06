package iwr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -83643453752262620L;
	private static final int fHeight = 30;
	private static final int fWidth = 30;
	private Map map;
	private Player player;
	private int time;
	protected Field activeField, selectedField;

	private int width() {
		return fWidth * map.width;
	}
	
	private int height() {
		return fHeight * map.height;
	}
	
	public void setMap(Map m) {
		map = m;
	}
	
	public void setPlayer(Player p) {
		player = p;
	}
	
	public void setTime(int t) {
		time = t;
	}
	
	protected void paintCell(Graphics2D g, int i, int j) {
		Field f = map.fieldAt(i, j);
		for (ImageIcon img:f.imageForAt(player, time)) {
			g.drawImage(img.getImage(), i*fWidth, j*fHeight, img.getImageObserver());
		}
	}
	
	protected void setActiveField(Point coords) {
		if (map == null) return;
		int x = coords.x/fWidth;
		int y = coords.y/fHeight;
		if (x >= map.width || y >= map.height) {
			activeField = null;
			setToolTipText("");
		} else {
			activeField = map.fieldAt(x, y);
			String text = activeField.coords + ", " + activeField.typeAt(time);
			if (activeField.ownerAt(time) != null) {
				text += ", " + activeField.ownerAt(time);
				if (activeField.ownerAt(time).getHq(time) == activeField) {
					text += ", velení";
				}
			}
			if (activeField.armyAt(time) != null) {
				text += ", " + activeField.armyAt(time);
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
			for (int i = 0; i < map.width; ++i) {
				for (int j = 0; j < map.height; ++j) {
					paintCell(gr, i, j);
				}
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
