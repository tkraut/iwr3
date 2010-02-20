package iwr;

import org.w3c.dom.Node;

public class ConvertEvent extends Event {

	int time;
	Field field;
	FieldType type;
	
	public ConvertEvent(Node cfdNode, Map map, java.util.Map<Integer, FieldType> fieldTypes, int t) {
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
		return super.toString() + Messages.getString("ConvertEvent.Player") + field.ownerAt(time)+ Messages.getString("ConvertEvent.changedTypeOfField") + field + Messages.getString("ConvertEvent.from") + field.typeAt(time-1) + Messages.getString("ConvertEvent.to") + type; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
