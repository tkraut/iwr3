package iwr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

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
		return new Dimension(width()+3*fWidth, height()+3*fHeight);
	}

}