package iwr;

import java.util.Date;

import org.w3c.dom.Node;

public class RemoveHQEvent extends Event {

	Date timestamp;
	int time;
	Field field;
	Player player;
	
	public RemoveHQEvent(Node chqNode, Map map, java.util.Map<Integer, Player> players, int t) {
		time = t;
		for (Node child = chqNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) {
				try {
					field = map.fieldAt(NodeUtil.getString(child));
				} catch (NullPointerException e) {
					field = player.getHq(t);
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
		return "Hráč " + player + "odstranil velení z pozice " + field;
	}

}
