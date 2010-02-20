package iwr;

import org.w3c.dom.Node;

public class RecountEvent extends Event {

	Game game;

	public RecountEvent(Node rctNode, Game g, int t) {
		time = t;
		game = g;
		for (Node child = rctNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			}
		}

	}

	@Override
	void apply() {
		game.recount(time);

	}

	@Override
	public String toString() {
		return super.toString()
				+ Messages.getString("RecountEvent.RecountDone"); //$NON-NLS-1$
	}

}
