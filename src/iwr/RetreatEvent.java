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
			if (name.equals("t")) { //$NON-NLS-1$
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p")) { //$NON-NLS-1$
				field = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("c")) { //$NON-NLS-1$
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
		return super.toString() + Messages.getString("RetreatEvent.Player") + field.ownerAt(time) + Messages.getString("RetreatEvent.retreated") + count + Messages.getString("RetreatEvent.unitsOfType") + field.armyAt(time-1).unit + Messages.getString("RetreatEvent.from") + field;  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}


}
