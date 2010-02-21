package iwr;

import java.util.Date;

import org.w3c.dom.Node;

/**
 * Pravidla hry
 * 
 * @author Tomáš Kraut
 */
public class GameMode {
	/**
	 * Načtení z XML uzlu
	 * @param gmNode XML uzel &lt;wmod&gt;
	 */
	public GameMode(Node gmNode) {
		// TODO
		for (Node child = gmNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("id")) { //$NON-NLS-1$
				id = NodeUtil.getInt(child);
			} else if (name.equals("name")) { //$NON-NLS-1$
				name = NodeUtil.getString(child);
			} else if (name.equals("peace")) { //$NON-NLS-1$
				protection = NodeUtil.getTime(child);
			} else if (name.equals("type")) { //$NON-NLS-1$
				type = NodeUtil.getString(child);
			} else if (name.equals("timelimit")) { //$NON-NLS-1$
				timelimit = NodeUtil.getTime(child); // neimplementovano
			} else if (name.equals("mapvisafter")) { //$NON-NLS-1$
				mapDisclose = NodeUtil.getDate(child); // neimplementovano
			} else if (name.equals("speed")) { //$NON-NLS-1$
				speed = NodeUtil.getInt(child);
			} else if (name.equals("prodef")) { //$NON-NLS-1$
				effectivity = NodeUtil.getDouble(child);
			} else if (name.equals("startturns")) { //$NON-NLS-1$
				initialMoves = NodeUtil.getInt(child);
			} else if (name.equals("startnectar")) { //$NON-NLS-1$
				initialNectar = NodeUtil.getInt(child);
			} else if (name.equals("rating")) { //$NON-NLS-1$
				rating = NodeUtil.getInt(child);
			} else if (name.equals("jumping")) { //$NON-NLS-1$
				canJump = NodeUtil.getBool(child);
			} else if (name.equals("flags")) { //$NON-NLS-1$
				hasFlags = NodeUtil.getBool(child);
			} else if (name.equals("unitspeedbonus")) { //$NON-NLS-1$
				unitSpeedBonus = NodeUtil.getInt(child);
			} else if (name.equals("friendarmy")) { //$NON-NLS-1$
				hasFriendArmy = NodeUtil.getBool(child);
			}
		}

	}
	/**
	 * identifikator
	 */
	public int id;
	/**
	 * Jméno modu světa
	 */
	public String name;
	/**
	 * Čas protekce
	 */
	public Date protection;
	/**
	 * Typ hry
	 */
	public String type;
	/**
	 * Časový limit (neimplementováno ve hře)
	 */
	public Date timelimit;
	/**
	 * Čas do odkrytí mapy (neimplementováno ve hře)
	 */
	public Date mapDisclose;
	/**
	 * Rychlost světa (počet tahů za přepočet)
	 */
	public int speed;
	/**
	 * Efektivita produkce nektaru 
	 */
	public double effectivity;
	/**
	 * Počet tahů do začátku
	 */
	public int initialMoves;
	/**
	 * Množství nektaru do začátku
	 */
	public int initialNectar;
	/**
	 * Hodnocení světa 
	 */
	public int rating;
	/**
	 * Je povoleno skákat
	 */
	public boolean canJump;
	/**
	 * Ve světě jsou vlajky
	 */
	public boolean hasFlags;
	/**
	 * neimplementováno ve hře
	 */
	public int unitSpeedBonus;
	/**
	 * Povolena armáda na políčku spoluhráče
	 */
	public boolean hasFriendArmy;
	/**
	 * Na svět se vztahují globální události
	 */
	public boolean applyGlobalEvents;

}
