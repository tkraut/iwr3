package iwr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel pro zobrazení aktuálního stavu mapy
 * @author Tomáš Kraut
 *
 */
public class MapPane extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -83643453752262620L;
	/**
	 * Výška jednoho políčka
	 */
	private static final int fHeight = 30;
	/**
	 * Šířka jednoho políčka
	 */
	private static final int fWidth = 30;
	/**
	 * Odkaz na hru
	 */
	private Game game;
	/**
	 * Odkaz na mapu
	 */
	private Map map;
	/**
	 * Akticní hráč
	 */
	private Player player;
	/**
	 * Herní čas
	 */
	private int time;
	/**
	 * Aktuální pole
	 */
	private Field activeField;
	/**
	 * Vybrané pole (zatím nepoužito)
	 */
	@SuppressWarnings("unused")
	private Field selectedField;
	/**
	 * Mají se ukazovat pouze viditelná pole?
	 */
	private boolean obeyVisibilityRules;
	/**
	 * Viditelná pole
	 */
	private Set<Field> visibility;

	/**
	 * @return Šířka celé mapy
	 */
	private int width() {
		return fWidth * map.width;
	}

	/**
	 * @return Výška celé mapy
	 */
	private int height() {
		return fHeight * map.height;
	}

	/**
	 * Nastaví hru, která se má vykreslovat (a spolu s ní její příslušnou mapu)
	 * @param game Hra
	 */
	public void setGame(Game game) {
		this.game = game;
		this.map = game.map;
	}

	/**
	 * Nastaví aktivního hráče
	 * @param player Hráč
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/** Nastaví aktuální herní čas
	 * @param time Herní čas
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Nastaví, zda je třeba dodržovat viditelnost
	 * @param obeyVisibilityRules Je třeba dodržovat
	 */
	public void setObeyVisibilityRules(boolean obeyVisibilityRules) {
		this.obeyVisibilityRules = obeyVisibilityRules;
	}

	/**
	 * Zjistí, jestli je třeba dodržovat viditelnost
	 * @return true, pokud je potřeba dodržovat viditelnost a je nastaven aktivní hráč
	 */
	public boolean obeysVisibilityRules() {
		return obeyVisibilityRules && player != null;
	}

	/**
	 * Vykreslí jedno pole mapy
	 * @param g 
	 * @param i X souřadnice pole
	 * @param j Y souřadnice pole
	 */
	protected void paintCell(Graphics2D g, int i, int j) {
		Field f = map.fieldAt(i, j);
		for (ImageIcon img : f.imageForAt(player, time, obeysVisibilityRules(),
				visibility)) {
			g.drawImage(img.getImage(), i * fWidth, j * fHeight, img
					.getImageObserver());
		}
	}

	/**
	 * Nastaví aktivní pole
	 * @param coords Souřadnice pole
	 */
	protected void setActiveField(Point coords) {
		if (map == null)
			return;
		int x = coords.x / fWidth;
		int y = coords.y / fHeight;
		if (x >= map.width || y >= map.height) {
			activeField = null;
			setToolTipText(""); //$NON-NLS-1$
		} else {
			activeField = map.fieldAt(x, y);
			String text = activeField.getSCoords()
					+ ", " + activeField.typeAt(time); //$NON-NLS-1$
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
		if (map == null) {
			clearMap(gr);
		} else {
			if (obeysVisibilityRules())
				countVisibility();
			for (int i = 0; i < map.width; ++i) {
				for (int j = 0; j < map.height; ++j) {
					paintCell(gr, i, j);
				}
			}
		}
	}

	/**
	 * Spočte viditelná pole pro aktivního hráče
	 */
	private void countVisibility() {
		visibility = new HashSet<Field>();
		for (Player p : game.getPlayers().values()) {
			if (p.type == player.type) {
				visibility.addAll(p.visibleFieldsAt(time));
			}
		}

	}

	/**
	 * Smaže prostor pro mapu
	 * @param g
	 */
	protected void clearMap(Graphics2D g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		if (map == null)
			return super.getPreferredSize();
		return new Dimension(width() + 5 * fWidth, height() + 5 * fHeight);
	}

}
