package iwr;

import org.w3c.dom.Node;

//<!ELEMENT mov (t, p1, p2, c)>
public class MoveEvent extends Event {
	Field src, dest;
	int count;
	public MoveEvent(Node movNode, Map map, int t) {
		time = t;
		for (Node child = movNode.getFirstChild(); child != null; child = child.getNextSibling()) {
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
		dest.ownerAt(time).removeMovesAt(costOfAction(sa.unit.turnsPerMove, distance ), count);
		dest.addArmyAt(new Army(sa.unit, count), time);
		src.removeArmyAt(count, time);
	}
	
	@Override
	public String toString() {
		return "Hráč " + src.ownerAt(time) + " přesunul " + count + " jednotek typu " + src.armyAt(time-1).unit + " z pole " + src + " na pole " + dest;
	}

}
