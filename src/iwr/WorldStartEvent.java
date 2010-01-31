package iwr;

import org.w3c.dom.Node;

public class WorldStartEvent extends Event {
	
	Game game;
	public WorldStartEvent(Node node, Game g, int t) {
		time = t;
		game = g;
	}

	@Override
	void apply() {
		game.start = time;
	}
	
	@Override
	public String toString() {
		return "Svět spuštěn";
	}

}
