package iwr;

import org.w3c.dom.Node;

/**
 * Spuštění světa. (Po jeho naplnění)
 * @author Tomáš Kraut
 *
 */
public class WorldStartEvent extends Event {
	/**
	 * Hra
	 */
	Game game;

	/**
	 * Vytvoření události z XML uzlu
	 * @param wsNode XML uzel
	 * @param game Příslušná hra
	 * @param time Pořadí události
	 */
	public WorldStartEvent(Node wsNode, Game game, int time) {
		this.time = time;
		this.game = game;
		for (Node child = wsNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
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
		return super.toString()
				+ Messages.getString("WorldStartEvent.WorldStarted"); //$NON-NLS-1$
	}

}
