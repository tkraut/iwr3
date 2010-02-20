package iwr;

import org.w3c.dom.Node;

public class WorldStartEvent extends Event {
	
	Game game;
	public WorldStartEvent(Node wsNode, Game g, int t) {
		time = t;
		game = g;
		for (Node child = wsNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			}
		}
	}

	@Override
	void apply() {
		game.start = time;
	}
	
	@Override
	public String toString() {
		return super.toString() + "Svět spuštěn";
	}

}
