package iwr;

import org.w3c.dom.Node;

public class CreateHQEvent extends Event {

	int time;
	Field field;
	Player player;

	public CreateHQEvent(Node chqNode, Map map,
			java.util.Map<Integer, Player> players, int t) {
		time = t;
		for (Node child = chqNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) {
				field = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("pl")) {
				player = players.get(NodeUtil.getInt(child));
			}
		}

	}

	@Override
	void apply() {
		field.changeOwnerAt(player, time);
		player.setHq(field, time);
	}

	@Override
	public String toString() {
		return super.toString()
				+ Messages.getString("CreateHQEvent.Player") + player + Messages.getString("CreateHQEvent.settledHQOn") + field; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
