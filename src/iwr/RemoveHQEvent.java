package iwr;

import org.w3c.dom.Node;
/**
 * Událost odstranění velení.
 * @author Tomáš Kraut
 *
 */
public class RemoveHQEvent extends Event {

	/**
	 * Pole, na kterém se ruší velení
	 */
	Field field;
	/**
	 * Hráč, rušící velení
	 */
	Player player;

	/**
	 * Vytvoření události z XML uzlu
	 * @param chqNode XML uzel
	 * @param map Mapa
	 * @param players Seznam hráčů
	 * @param time Pořadí akce
	 */
	public RemoveHQEvent(Node chqNode, Map map,
			java.util.Map<Integer, Player> players, int time) {
		this.time = time;
		for (Node child = chqNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) {
				try {
					field = map.fieldAt(NodeUtil.getString(child));
				} catch (NullPointerException e) {
					field = player.getHq(time);
				}
			} else if (name.equals("pl")) {
				player = players.get(NodeUtil.getInt(child));
			}
		}

	}

	@Override
	void apply() {
		field.changeOwnerAt(null, time);
		player.removeHq(time);
	}

	@Override
	public String toString() {
		return super.toString()
				+ Messages.getString("RemoveHQEvent.Player") + player + Messages.getString("RemoveHQEvent.removedHQFrom") + field; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
