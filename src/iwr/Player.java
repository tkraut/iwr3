package iwr;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

/**
 * Hráč ve hře
 * 
 * @author Tomáš
 * 
 */
public class Player {
	class Resources {
		public Resources(GameMode mode) {
			movesHistory = new TimeMap<Integer>(mode.initialMoves);
			nectarHistory = new TimeMap<Integer>(mode.initialNectar);
			hqHistory = new TimeMap<Field>(null);
			receivedNeqHistory = new TimeMap<Integer>(0);
			destroyedHistory = new TimeMap<Integer>(0);
			fieldsHistory = new TimeMap<Set<Field>>(new HashSet<Field>());
		}

		TimeLine<Integer> movesHistory;
		TimeLine<Integer> nectarHistory;
		TimeLine<Field> hqHistory;
		TimeLine<Integer> receivedNeqHistory;
		TimeLine<Integer> destroyedHistory;
		TimeLine<Set<Field>> fieldsHistory;
	}

	/**
	 * Jmeno hrace <nick>
	 */
	protected String nick;
	/**
	 * ID hrace <id>
	 */
	protected int id;
	/**
	 * Typ <type>
	 */
	protected Type type;
	/**
	 * Aliance hrace <ali> Prozatim nevyuzite
	 */
	protected Aliance aliance;
	/**
	 * Datum registrace hrace <regdate>
	 */
	protected Date regdate;

	protected Resources resources;

	protected int death = 0;

	public void setHq(Field hq, int time) {
		resources.hqHistory.changeLoadAt(hq, time);
		addFieldAt(hq, time);
	}

	public void removeHq(int time) {
		resources.hqHistory.changeLoadAt(null, time);
		removeAllFieldsAt(time);
	}

	public Field getHq(int time) {
		return resources.hqHistory.loadAt(time);
	}

	public Player(Node playerNode, Map<Integer, Type> playerTypes, GameMode mode) {
		resources = new Resources(mode);
		for (Node child = playerNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child.getNodeName().equals("id")) {
				id = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("nick")) {
				nick = child.getFirstChild().getTextContent();
			} else if (child.getNodeName().equals("type")) {
				type = playerTypes.get(Integer.parseInt(child.getFirstChild()
						.getTextContent()));
			} else if (child.getNodeName().equals("ali")) {
				// ali =
				// Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("regdate")) {
				// regdate = new Date(child.getFirstChild().getTextContent());
			}
		}
		// <player><id>66</id><nick>asu</nick><type>2</type><ali></ali><regdate>2005-03-06
		// 19:27:08</regdate></player>

	}

	@Override
	public String toString() {
		return nick + " (" + type + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void recountAt(int nectar, int moves, int time) {
		addMovesAt(moves, time);
		addNectarAt(nectar, time);
	}

	public void addNectarAt(int nectar, int time) {
		resources.nectarHistory.changeLoadAt(resources.nectarHistory
				.loadAt(time)
				+ nectar, time);
	}

	public void removeNectarAt(int nectar, int time) {
		addNectarAt(-nectar, time);
	}

	public void addMovesAt(int moves, int time) {
		resources.movesHistory.changeLoadAt(resources.movesHistory.loadAt(time)
				+ moves, time);
	}

	public void removeMovesAt(int moves, int time) {
		addMovesAt(-moves, time);
	}

	public void killed(int time) {
		removeHq(time);
		death = time;
		resources.movesHistory.changeLoadAt(0, time);
		resources.nectarHistory.changeLoadAt(0, time);
		removeAllFieldsAt(time);
	}

	public int beforeDeath() {
		return death - 1;
	}

	public void acceptNectarAt(int nectar, int time) { // jen info o prijeti,
														// neprida nektar
		resources.receivedNeqHistory.changeLoadAt(resources.receivedNeqHistory
				.loadAt(time)
				+ nectar, time);

	}

	public int nectarAt(int time) {
		return resources.nectarHistory.loadAt(time);
	}

	public int movesAt(int time) {
		return resources.movesHistory.loadAt(time);
	}

	public int receivedAt(int time) {
		return resources.receivedNeqHistory.loadAt(time);
	}

	public int fragsAt(int time) {
		return resources.destroyedHistory.loadAt(time);
	}

	public void frag(int time) {
		resources.destroyedHistory.changeLoadAt(resources.destroyedHistory
				.loadAt(time) + 1, time);
	}

	public Set<Field> fieldsAt(int time) {
		return resources.fieldsHistory.loadAt(time);
	}

	public void removeAllFieldsAt(int time) {
		resources.fieldsHistory.changeLoadAt(new HashSet<Field>(), time);
	}

	public void addFieldAt(Field field, int time) {
		Set<Field> expanded = new HashSet<Field>(resources.fieldsHistory
				.loadAt(time - 1));
		if (expanded.add(field)) {
			resources.fieldsHistory.changeLoadAt(expanded, time);
		}
	}

	public void removeFieldAt(Field field, int time) {
		Set<Field> shrinked = new HashSet<Field>(resources.fieldsHistory
				.loadAt(time - 1));
		if (shrinked.remove(field)) {
			resources.fieldsHistory.changeLoadAt(shrinked, time);
		}
	}

	public Set<Field> visibleFieldsAt(int time) {

		Set<Field> fields = fieldsAt(time);
		Set<Field> visible = new HashSet<Field>(fields);
		for (Field f : fields) {
			visible.addAll(f.visibleFieldsAt(time));
		}
		return visible;
	}

}
