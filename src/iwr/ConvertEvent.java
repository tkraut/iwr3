package iwr;

import java.util.Date;
import java.util.TreeMap;

import org.w3c.dom.Node;

public class ConvertEvent extends Event {

	Date timestamp;
	int time;
	Field field;
	FieldType type;
	
	public ConvertEvent(Node cfdNode, Map map, TreeMap<Integer, FieldType> fieldTypes, int t) {
		time = t;
		for (Node child = cfdNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) {
				field = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("nt")) {
				type = fieldTypes.get(NodeUtil.getInt(child));
			}
		}
	}

	@Override
	public void apply() {
		field.ownerAt(time).removeMovesAt(1, time);
		field.convertToAt(type, time);
	}
	@Override
	public String toString() {
		return "Hráč " + field.ownerAt(time)+ "změnil typ pole " + field + " z " + field.typeAt(time-1) + " na " + type;
	}
}
