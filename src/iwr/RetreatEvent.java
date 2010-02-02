package iwr;

import org.w3c.dom.Node;

public class RetreatEvent extends Event {
	//!ELEMENT ret (t, p, c)>
	Field field;
	int count;
	public RetreatEvent(Node retNode, Map map, int t) {
		time = t;
		for (Node child = retNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) {
				field = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("c")) {
				count = NodeUtil.getInt(child);
			}
		}

	}
	
	@Override
	void apply() {
		try {
			field.ownerAt(time).addNectarAt(field.armyAt(time).cost()/2, time);
		} catch (Exception e) {
			//Do nothing
		}
		field.removeArmyAt(count, time);
	}
	
	@Override
	public String toString() {
		return "Hráč " + field.ownerAt(time) + " propustil " + count + " jednotek typu " + field.armyAt(time-1).unit + " z pole " + field; 
	}


}
