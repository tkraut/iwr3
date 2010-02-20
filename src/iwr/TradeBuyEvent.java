package iwr;

import org.w3c.dom.Node;

public class TradeBuyEvent extends Event {

	Player donor, acceptor;
	Unit unit;
	int nectar, count;

	public TradeBuyEvent(Node trbNode, java.util.Map<Integer, Player> players,
			java.util.Map<Integer, Unit> unitTypes, int t) {
		time = t;
		for (Node child = trbNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) { //$NON-NLS-1$
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("pl1")) { //$NON-NLS-1$
				donor = players.get(NodeUtil.getInt(child));
			} else if (name.equals("pl2")) { //$NON-NLS-1$
				acceptor = players.get(NodeUtil.getInt(child));
			} else if (name.equals("cn")) { //$NON-NLS-1$
				nectar = NodeUtil.getInt(child);
			} else if (name.equals("cu")) { //$NON-NLS-1$
				count = NodeUtil.getInt(child);
			} else if (name.equals("ut")) { //$NON-NLS-1$
				unit = unitTypes.get(NodeUtil.getInt(child));
			}
		}

	}

	@Override
	void apply() {
		donor.removeNectarAt(nectar, time);
		donor.removeMovesAt(1, time);
		acceptor.acceptNectarAt(nectar, time);
		acceptor.getHq(time).addArmyAt(new Army(unit, count), time);
	}

	@Override
	public String toString() {
		return super.toString()
				+ Messages.getString("TradeBuyEvent.Player") + donor + Messages.getString("TradeBuyEvent.sendTo") + acceptor + " " + nectar + Messages.getString("TradeBuyEvent.mlOfNectar") + count + Messages.getString("TradeBuyEvent.unitsOfType") + unit + Messages.getString("TradeBuyEvent.onTheirHQ"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}

}
