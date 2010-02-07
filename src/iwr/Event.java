package iwr;

import java.util.Date;

import org.w3c.dom.Node;



/**
 * Událost hry
 * @author Tomáš
 *
 */
abstract public class Event {
	Date timestamp;
	int time;

	abstract void apply();
	public static Event parseNode(Node node, Game game) {
		int time = game.length+1;
		Event newEvent = null;
		String eventName = node.getNodeName();
		if (eventName.equals("cfd")) {
			newEvent = new ConvertEvent(node, game.map, game.fieldTypes, time);
		} else if (eventName.equals("chq")) {
			newEvent = new CreateHQEvent(node, game.map, game.players, time);
		} else if (eventName.equals("rhq")) {
			newEvent = new RemoveHQEvent(node, game.map, game.players, time);
		} else if (eventName.equals("buy")) {
			newEvent = new BuyEvent(node, game.map, game.players, game.unitTypes, time);
		} else if (eventName.equals("mov")) {
			newEvent = new MoveEvent(node, game.map, time);
		} else if (eventName.equals("ret")) {
			newEvent = new RetreatEvent(node, game.map, time);
		} else if (eventName.equals("att")) {
			newEvent = new AttackEvent(node, game.unitTypes, game.map, time);
		} else if (eventName.equals("ws")) {
			newEvent = new WorldStartEvent(node, game, time);
		} else if (eventName.equals("trn")) {
			newEvent = new TradeEvent(node, game.players, time);
		} else if (eventName.equals("trb")) {
			newEvent = new TradeBuyEvent(node, game.players, game.unitTypes, time);
		} else if (eventName.equals("rct") && game.start != -1 && game.start < time &&
				!node.isEqualNode(node.getPreviousSibling().getPreviousSibling())) { //TODO zrusit az v zaznamech nebudou duplicitni prepocty (nebo nerusit??)
			newEvent = new RecountEvent(node, game, time);
		}
		return newEvent;
	}
	
	public static int costOfAction(int basicCost, double distance) {
		return (int) (basicCost*distance);
	}
	
}
