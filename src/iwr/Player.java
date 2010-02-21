package iwr;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

/**
 * Hráč ve hře
 * 
 * @author Tomáš Kraut
 * 
 */
public class Player {
	/**
	 * Prostředky hráče
	 * @author Tomáš Kraut
	 *
	 */
	class Resources {
		/**
		 * Vytvoří prostředky pro nového hráče podle pravidel hry
		 * @param mode Pravidla hry
		 */
		public Resources(GameMode mode) {
			movesHistory = new TimeMap<Integer>(mode.initialMoves);
			nectarHistory = new TimeMap<Integer>(mode.initialNectar);
			hqHistory = new TimeMap<Field>(null);
			receivedNeqHistory = new TimeMap<Integer>(0);
			destroyedHistory = new TimeMap<Integer>(0);
			fieldsHistory = new TimeMap<Set<Field>>(new HashSet<Field>());
		}
		/**
		 * Průběh volnýh tahů
		 */
		TimeLine<Integer> movesHistory;
		/**
		 * Průběh volného nektaru
		 */
		TimeLine<Integer> nectarHistory;
		/**
		 * Průběh umístění velení
		 */
		TimeLine<Field> hqHistory;
		/**
		 * Průběh počtu přijatého nektaru
		 */
		TimeLine<Integer> receivedNeqHistory;
		/**
		 * Průběh počtu vyřazených protihráčů
		 */
		TimeLine<Integer> destroyedHistory;
		/**
		 * Průběh vlastnictví polí
		 */
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

	/***
	 * Hráčovy prostředky
	 */
	protected Resources resources;

	/**
	 * Čas jeho vyřazení
	 */
	protected int death = 0;

	/**
	 * Umístění velení
	 * @param hq Pole, kam se umisťuje
	 * @param time Pořadí akce ve světě
	 */
	public void setHq(Field hq, int time) {
		resources.hqHistory.changeLoadAt(hq, time);
		addFieldAt(hq, time);
	}

	/**
	 * Odstranění velení
	 * @param time Herní čas
	 */
	public void removeHq(int time) {
		resources.hqHistory.changeLoadAt(null, time);
		removeAllFieldsAt(time);
	}

	/**
	 * Získá umístění velení hráče v čase
	 * @param time Herní čas
	 * @return Pole, kde má hráč velení, případně null, pokud je vyřazen nebo velní odstranil před začátkem světa
	 */
	public Field getHq(int time) {
		return resources.hqHistory.loadAt(time);
	}

	/**
	 * Vytvoří hráče z XML uzlu
	 * @param playerNode XML uzel &lt;player&gt;
	 * @param playerTypes Seznam typů hráčů
	 * @param mode Pravidla hry
	 */
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

	/**
	 * Zaznamená provedení přepočtu a přidá příslušné prostředky
	 * @param nectar Přidaný nektar
	 * @param moves Přidané tahy
	 * @param time Herní čas přepočtu
	 */
	public void recountAt(int nectar, int moves, int time) {
		addMovesAt(moves, time);
		addNectarAt(nectar, time);
	}

	/**
	 * Přidání nektar v daném herním čase
	 * @param nectar Přidaný nektar
	 * @param time Herní čas
	 */
	public void addNectarAt(int nectar, int time) {
		resources.nectarHistory.changeLoadAt(resources.nectarHistory
				.loadAt(time)
				+ nectar, time);
	}

	/**
	 * Odebere nektar v daném herním čase
	 * @param nectar Odebraný nektar
	 * @param time Herní čas
	 */
	public void removeNectarAt(int nectar, int time) {
		addNectarAt(-nectar, time);
	}
	
	/**
	 * Přidá tahy v daném herním čase
	 * @param moves Přidané tahy
	 * @param time Herní čas 
	 */
	public void addMovesAt(int moves, int time) {
		resources.movesHistory.changeLoadAt(resources.movesHistory.loadAt(time)
				+ moves, time);
	}

	/**
	 * Odebere tahy v daném herním čase
	 * @param moves Odebrané tahy
	 * @param time Herní čas 
	 */
	public void removeMovesAt(int moves, int time) {
		addMovesAt(-moves, time);
	}

	/**
	 * Zaznamená vyřazení hráče v daném čase
	 * @param time Herní čas
	 */
	public void killed(int time) {
		removeHq(time);
		death = time;
		resources.movesHistory.changeLoadAt(0, time);
		resources.nectarHistory.changeLoadAt(0, time);
		removeAllFieldsAt(time);
	}

	/**
	 * Vrátí pořadí akce před vyřazením hráče
	 * @return Herní čas tah před vyřazením 
	 */
	public int beforeDeath() {
		return death - 1;
	}

	/**
	 * Zaznamená přijetí nektaru z obchodu 
	 * @param nectar Přijatý nektar
	 * @param time Herní čas
	 */
	public void acceptNectarAt(int nectar, int time) { // jen info o prijeti,
														// neprida nektar
		resources.receivedNeqHistory.changeLoadAt(resources.receivedNeqHistory
				.loadAt(time)
				+ nectar, time);

	}

	
	/**
	 * Zjistí množství nektaru v čase
	 * @param time Herní čas
	 * @return Množství nektaru
	 */
	public int nectarAt(int time) {
		return resources.nectarHistory.loadAt(time);
	}

	/**
	 * Zjistí počet tahů v čase
	 * @param time Herní čas
	 * @return Počet tahů
	 */
	public int movesAt(int time) {
		return resources.movesHistory.loadAt(time);
	}

	/**
	 * Zjistí množství přijatého nektaru v obchodu v čase
	 * @param time Herní čas
	 * @return Přijatý nektar od začátku do teď
	 */
	public int receivedAt(int time) {
		return resources.receivedNeqHistory.loadAt(time);
	}

	/**
	 * Zjistí počet dosud vyřazených protivníků
	 * @param time Herní čas
	 * @return Počet vyřazených protivníků
	 */
	public int fragsAt(int time) {
		return resources.destroyedHistory.loadAt(time);
	}

	/**
	 * Zaznamená vyřazení protivníka
	 * @param time Herní čas
	 */
	public void frag(int time) {
		resources.destroyedHistory.changeLoadAt(resources.destroyedHistory
				.loadAt(time) + 1, time);
	}

	/**
	 * Zjistí množinu polí hráče v čase
	 * @param time Herní čas
	 * @return Mnořina polí
	 */
	public Set<Field> fieldsAt(int time) {
		return resources.fieldsHistory.loadAt(time);
	}

	/**
	 * Odebere hráči všechna pole
	 * @param time Herní čas
	 */
	public void removeAllFieldsAt(int time) {
		resources.fieldsHistory.changeLoadAt(new HashSet<Field>(), time);
	}

	/**
	 * Přidá hráči jedno pole, pokud už jej má, nic se neděje
	 * @param field Přidané pole
	 * @param time Herní čas
	 */
	public void addFieldAt(Field field, int time) {
		Set<Field> expanded = new HashSet<Field>(resources.fieldsHistory
				.loadAt(time - 1));
		if (expanded.add(field)) {
			resources.fieldsHistory.changeLoadAt(expanded, time);
		}
	}

	/**
	 * Odebere hráči jedno pole, pokud jej nemá, nic se neděje
	 * @param field Odebrané pole
	 * @param time Herní čas
	 */
	public void removeFieldAt(Field field, int time) {
		Set<Field> shrinked = new HashSet<Field>(resources.fieldsHistory
				.loadAt(time - 1));
		if (shrinked.remove(field)) {
			resources.fieldsHistory.changeLoadAt(shrinked, time);
		}
	}

	/**
	 * Zjistí množinu polí, která hráč vidí bez pomoci spoluhráčů 
	 * @param time Herní čas
	 * @return Množina viditelných polí
	 */
	public Set<Field> visibleFieldsAt(int time) {

		Set<Field> fields = fieldsAt(time);
		Set<Field> visible = new HashSet<Field>(fields);
		for (Field f : fields) {
			visible.addAll(f.visibleFieldsAt(time));
		}
		return visible;
	}

}
