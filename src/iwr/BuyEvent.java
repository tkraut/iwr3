package iwr;

import java.util.Map;

import org.w3c.dom.Node;

/**
 * Nákup. Událost světa.
 * @author Tomáš Kraut
 *
 */
public class BuyEvent extends Event {
	
	/**
	 * Pole, na které bylo nakupováno
	 */
	Field field;
	/**
	 * Majitel pole
	 */
	Player player;
	/**
	 * Nakupovaná armáda
	 */
	Army army;

	/**
	 * Vytvoření události z XML uzlu
	 * @param buyNode XML uzel
	 * @param map Mapa
	 * @param players Seznam hráčů
	 * @param unitTypes Seznam typů jednotek
	 * @param t Pořadí události
	 */
	public BuyEvent(Node buyNode, iwr.Map map, Map<Integer, Player> players,
			java.util.Map<Integer, Unit> unitTypes, int t) {
		time = t;
		Unit unit = null;
		int count = 0;
		for (Node child = buyNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) { //$NON-NLS-1$
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) { //$NON-NLS-1$
				field = map.fieldAt(NodeUtil.getString(child));
				player = field.ownerAt(t);
			} else if (name.equals("ut")) { //$NON-NLS-1$
				unit = unitTypes.get(NodeUtil.getInt(child));
			} else if (name.equals("c")) { //$NON-NLS-1$
				count = NodeUtil.getInt(child);
			}
		}
		army = new Army(unit, count);
	}

	@Override
	void apply() {
		player.removeMovesAt(1, time);
		player.removeNectarAt(army.cost(), time);
		field.addArmyAt(army, time);
	}

	@Override
	public String toString() {
		return super.toString()
				+ Messages.getString("BuyEvent.Player") + player + Messages.getString("BuyEvent.boughtTo") + field + " " + army.getCount() + Messages.getString("BuyEvent.unitsOfType") + army.getUnit(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
