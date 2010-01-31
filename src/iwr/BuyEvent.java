package iwr;

import java.util.SortedMap;

import org.w3c.dom.Node;

public class BuyEvent extends Event {

	Field field;
	Player player;
	Army army;
	
	public BuyEvent(Node buyNode, Map map, SortedMap<Integer, Player> players,
			java.util.Map<Integer, Unit> unitTypes, int t) {
		time = t;
		Unit unit = null;
		int count = 0;
		for (Node child = buyNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) {
				field = map.fieldAt(NodeUtil.getString(child));
				player = field.ownerAt(t);
			} else if (name.equals("ut")) {
				unit = unitTypes.get(NodeUtil.getInt(child));
			} else if (name.equals("c")) {
				count = NodeUtil.getInt(child);
			}
		}
		army = new Army(unit, count);
	}

	@Override
	void apply() {
		field.addArmyAt(army, time);

	}
	
	@Override
	public String toString() {
		return "Hráč "+ player + "nakoupil na pole" + field + " " + army.count + "jednotek typu " + army.unit; 
	}
}
