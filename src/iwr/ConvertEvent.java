package iwr;

import java.util.Date;
import java.util.TreeMap;

import org.w3c.dom.Node;

public class ConvertEvent implements Event {

	Date timestamp;
	int time;
	Field field;
	FieldType type;
	
	public ConvertEvent(Node eventNode, Map map, TreeMap<Integer, FieldType> fieldTypes, int t) {
		time = t;
		for (Node child = eventNode.getFirstChild(); child != null; child = child.getNextSibling()) {
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
		field.convertToAt(type, time);
	}

}
