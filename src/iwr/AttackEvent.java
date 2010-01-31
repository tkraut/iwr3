package iwr;

import java.util.Map;

import org.w3c.dom.Node;

public class AttackEvent extends Event {
	//<!-- time, pos1, pos2, attcount, atttype, defcount, deftype, result, survive -->
	//<!ELEMENT att (t, p1, p2, ca, ta, cd, td, r, s)>
	
	Field src, dest;
	int attCount, defCount, survived;
	Unit attType, defType;
	boolean result;
	public AttackEvent(Node attNode, Map<Integer, Unit> units, iwr.Map map, int t) {
		time = t;
		for (Node child = attNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p1")) {
				src = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("p2")) {
				dest = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("ca")) {
				attCount = NodeUtil.getInt(child);
			} else if (name.equals("ta")) {
				attType = units.get(NodeUtil.getInt(child));
			} else if (name.equals("cd")) {
				defCount = NodeUtil.getInt(child);
			} else if (name.equals("td")) {
				defType = units.get(NodeUtil.getMaybeInt(child));
			} else if (name.equals("r")) {
				result = NodeUtil.getBool(child);
			} else if (name.equals("s")) {
				survived = NodeUtil.getInt(child);
			}
		}
	}
	
	@Override
	void apply() {
		
		if (result) {
			dest.changeOwnerAt(src.ownerAt(time), time);
			dest.changeArmyAt(new Army(attType, survived), time);
		} else {
			dest.changeArmyAt(new Army(defType, survived), time);
		}
		src.removeArmyAt(attCount, time);
	}

	@Override
	public String toString() {
		//TODO
		if (result) {
			return "Hráč " + src.ownerAt(time) + " dobyl hráči " + dest.ownerAt(time-1) + " pole " + dest; 
		} else {
			return "Hráč " + src.ownerAt(time) + " nedobyl hráči " + dest.ownerAt(time-1) + " pole " + dest;
		}
	}
	
}
