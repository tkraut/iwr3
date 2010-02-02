package iwr;

import org.w3c.dom.Node;

public class RecountEvent extends Event {

	Game game;
	
	public RecountEvent(Node node, Game g, int t) {
		time = t;
		game = g;
	}

	@Override
	void apply() {
		game.recount(time);

	}
	
	@Override
	public String toString() {
		return "Proběhl přepočet";
	}

}
