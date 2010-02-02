package iwr;


import org.w3c.dom.Node;

public class TradeBuyEvent extends Event {

	Player donor, acceptor;
	Unit unit;
	int nectar, count;
	
	public TradeBuyEvent(Node trbNode, java.util.Map<Integer, Player> players, java.util.Map<Integer, Unit> unitTypes, int t) {
		time = t;
		for (Node child = trbNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("pl1")) {
				donor = players.get(NodeUtil.getInt(child));
			} else if (name.equals("pl2")) {
				acceptor = players.get(NodeUtil.getInt(child));
			} else if (name.equals("cn")) {
				nectar = NodeUtil.getInt(child);
			} else if (name.equals("cu")) {
				count = NodeUtil.getInt(child);
			} else if (name.equals("ut")) {
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
		return "Hráč " + donor + " poslal hráči " + acceptor + " " + count + "ml nektaru. Ty byly přeměněny na " + count + " jednotek typu " + unit + "na jeho velení";
	}

}
