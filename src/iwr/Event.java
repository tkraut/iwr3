package iwr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

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
			game.startTime = newEvent.timestamp;
			if (game.mode.protection != null) {
				Calendar cal =  new GregorianCalendar();
				cal.setTime(game.startTime);
				cal.add(Calendar.SECOND, (int) (game.mode.protection.getTime()/1000));
				game.protectionEnds = cal.getTime();
			} else {
				game.protectionEnds = game.startTime;
			}
			
		} else if (eventName.equals("trn")) {
			newEvent = new TradeEvent(node, game.players, time);
		} else if (eventName.equals("trb")) {
			newEvent = new TradeBuyEvent(node, game.players, game.unitTypes, time);
		} else if (eventName.equals("rct") && game.start != -1 && game.start < time &&
				!node.isEqualNode(node.getPreviousSibling().getPreviousSibling())) { //TODO zrusit az v zaznamech nebudou duplicitni prepocty (nebo nerusit??)
			newEvent = new RecountEvent(node, game, time);
		} else {
			//u duplicitnich a zbytecnych prepoctu a chybnych zaznamu neresime timestampy
			return null;
		}
		if (newEvent.timestamp == null) {
			JOptionPane.showMessageDialog(null, "Nenačten timestamp" + newEvent);
		}
		if (game.protection == -1 && game.protectionEnds != null && newEvent.timestamp.after(game.protectionEnds)) {
			game.protection = newEvent.time;
		}
		return newEvent;
	}
	
	public static int costOfAction(int basicCost, double distance) {
		return (int) (basicCost*distance);
	}
	@Override
	public String toString() {
		return new SimpleDateFormat("dd. MM. HH:mm:ss → ").format(timestamp);
	}
	
}
