package iwr;

import org.w3c.dom.Node;

/**
 * Přepočet.
 * @author Tomáš Kraut
 *
 */
public class RecountEvent extends Event {
	/**
	 * Hra, které se přepočet týká
	 */
	Game game;

	/**
	 * Vytvoření události z XML uzlu
	 * @param rctNode XML uzel
	 * @param game Hra
	 * @param time Pořadí akce
	 */
	public RecountEvent(Node rctNode, Game game, int time) {
		this.time = time;
		this.game = game;
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
