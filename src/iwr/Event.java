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
	/**
	 * Čas akce
	 */
	Date timestamp;
	/**
	 * Pořadí akce
	 */
	int time;

	/**
	 * Zaznamenání změn ve hře, které tato akce vyvolala
	 */
	abstract void apply();

	/**
	 * Továrna na události, vytvářející je z XML uzlů
	 * @param node ZML uzel
	 * @param game Příslušná hra
	 * @return Událost, obsažená v příslušném uzlu
	 */
	public static Event parseNode(Node node, Game game) {
		int time = game.length + 1;
		Event newEvent = null;
		String eventName = node.getNodeName();
		if (eventName.equals("cfd")) { //$NON-NLS-1$
			newEvent = new ConvertEvent(node, game.map, game.fieldTypes, time);
		} else if (eventName.equals("chq")) { //$NON-NLS-1$
			newEvent = new CreateHQEvent(node, game.map, game.getPlayers(), time);
		} else if (eventName.equals("rhq")) { //$NON-NLS-1$
			newEvent = new RemoveHQEvent(node, game.map, game.getPlayers(), time);
		} else if (eventName.equals("buy")) { //$NON-NLS-1$
			newEvent = new BuyEvent(node, game.map, game.getPlayers(),
					game.unitTypes, time);
		} else if (eventName.equals("mov")) { //$NON-NLS-1$
			newEvent = new MoveEvent(node, game.map, time);
		} else if (eventName.equals("ret")) { //$NON-NLS-1$
			newEvent = new RetreatEvent(node, game.map, time);
		} else if (eventName.equals("att")) { //$NON-NLS-1$
			newEvent = new AttackEvent(node, game.unitTypes, game.map, time);
		} else if (eventName.equals("ws")) { //$NON-NLS-1$
			newEvent = new WorldStartEvent(node, game, time);
			game.startTime = newEvent.timestamp;
			if (game.mode.protection != null) {
				Calendar cal = new GregorianCalendar();
				cal.setTime(game.startTime);
				cal.add(Calendar.SECOND,
						(int) (game.mode.protection.getTime() / 1000));
				game.protectionEnds = cal.getTime();
			} else {
				game.protectionEnds = game.startTime;
			}

		} else if (eventName.equals("trn")) { //$NON-NLS-1$
			newEvent = new TradeEvent(node, game.getPlayers(), time);
		} else if (eventName.equals("trb")) { //$NON-NLS-1$
			newEvent = new TradeBuyEvent(node, game.getPlayers(), game.unitTypes,
					time);
		} else if (eventName.equals("rct") && game.start != -1 && game.start < time && //$NON-NLS-1$
				!node.isEqualNode(node.getPreviousSibling()
						.getPreviousSibling())) { // TODO zrusit az v zaznamech
													// nebudou duplicitni
													// prepocty (nebo nerusit??)
			newEvent = new RecountEvent(node, game, time);
		} else {
			// u duplicitnich a zbytecnych prepoctu a chybnych zaznamu neresime
			// timestampy
			return null;
		}
		if (newEvent.timestamp == null) {
			JOptionPane.showMessageDialog(null, Messages
					.getString("Event.timestampNotLoaded") + newEvent); //$NON-NLS-1$
		}
		if (game.protection == -1 && game.protectionEnds != null
				&& newEvent.timestamp.after(game.protectionEnds)) {
			game.protection = newEvent.time;
		}
		return newEvent;
	}
	/**
	 * Počítá počet tahů, který akce, závisející na vzdálenosti, stojí
	 * @param basicCost Základní cena akce
	 * @param distance Vzdálenost polí
	 * @return Počet tahů, potřebných na akci 
	 */
	public static int costOfAction(int basicCost, double distance) {
		return (int) (basicCost * distance);
	}

	@Override
	public String toString() {
		return new SimpleDateFormat(Messages
				.getString("Event.PrefixDateFormat")).format(timestamp); //$NON-NLS-1$
	}

}
