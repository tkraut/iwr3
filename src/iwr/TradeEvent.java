package iwr;

import java.util.SortedMap;

import org.w3c.dom.Node;

public class TradeEvent extends Event {
	
	Player donor, acceptor;
	int count;
	
	public TradeEvent(Node trnNode, SortedMap<Integer, Player> players, int t) {
		time = t;
		for (Node child = trnNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("pl1")) {
				donor = players.get(NodeUtil.getInt(child));
			} else if (name.equals("pl2")) {
				acceptor = players.get(NodeUtil.getInt(child));
			} else if (name.equals("c")) {
				count = NodeUtil.getMaybeInt(child); //TODO odstranit maybe, az budou opraveny zaznamy (nebo nechat??)
			}
		}
	}

	@Override
	void apply() {
		donor.removeNectarAt(count, time);
		acceptor.addNectarAt(count, time);
		acceptor.acceptNectarAt(count, time);
	}

	@Override
	public String toString() {
		return "Hráč " + donor + " poslal hráči " + acceptor + " " + count + "ml nektaru";
	}
	
}
