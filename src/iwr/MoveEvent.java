package iwr;

import org.w3c.dom.Node;

/**
 * Událost přesunu armády.
 * @author Tomáš Kraut
 *
 */
public class MoveEvent extends Event {
	/**
	 * Pole, ze kterého se armáda přesouvá
	 */
	Field src;
	/**
	 * Pole, na které se armáda přesouvá
	 */
	Field dest;
	/**
	 * Počet jednotek, který se přesouvá
	 */
	int count;

	/**
	 * Vytvoření události z XML uzlu
	 * @param movNode XML uzel
	 * @param map Mapa
	 * @param t Pořadí akce
	 */
	public MoveEvent(Node movNode, Map map, int t) {
		time = t;
		for (Node child = movNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p1")) {
				src = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("p2")) {
				dest = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("c")) {
				count = NodeUtil.getInt(child);
			}
		}

	}

	@Override
	void apply() {
		Army sa = src.armyAt(time);
		double distance = dest.distanceFrom(src);
		dest.ownerAt(time).removeMovesAt(
				costOfAction(sa.getUnit().getTurnsPerMove(), distance), count);
		dest.addArmyAt(new Army(sa.getUnit(), count), time);
		src.removeArmyAt(count, time);
	}

	@Override
	public String toString() {
		return super.toString()
				+ Messages.getString("MoveEvent.Player") + src.ownerAt(time) + Messages.getString("MoveEvent.moved") + count + Messages.getString("MoveEvent.unitsOfType") + src.armyAt(time - 1).getUnit() + Messages.getString("MoveEvent.from") + src + Messages.getString("MoveEvent.to") + dest; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

}
