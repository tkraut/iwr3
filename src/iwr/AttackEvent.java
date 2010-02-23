package iwr;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.w3c.dom.Node;

/**
 * Útok, Událost světa.
 * 
 * @author Tomáš Kraut
 * 
 */
public class AttackEvent extends Event {
	// <!-- time, pos1, pos2, attcount, atttype, defcount, deftype, result,
	// survive -->
	// <!ELEMENT att (t, p1, p2, ca, ta, cd, td, r, s)>
	/**
	 * Pole, ze kterého byl veden útok.
	 */
	Field src;
	/**
	 * Pole, na které byl veden útok
	 */
	Field dest;
	/**
	 * Počet útočících jednotek
	 */
	int attCount;
	/**
	 * Počet bránících jednotek
	 */
	int defCount;
	/**
	 * Počet přeživších jednotek
	 */
	int survived;
	/**
	 * Typ útočících jednotek
	 */
	UnitType attType;
	/**
	 * Typ bránících jednotek
	 */
	UnitType defType;
	/**
	 * Mapa, na které se akce odehrává
	 */
	iwr.Map map;
	/**
	 * Útok byl úspěšný
	 */
	boolean result;
	/**
	 * Bylo dobyto velení
	 */
	boolean hqDown = false;
	/**
	 * Obránce
	 */
	Player defender;
	/**
	 * Útočník
	 */
	Player attacker;
	
	/**
	 * Seznam útoků, které vedly k vyřazení hráče
	 */
	static NavigableMap<Integer, AttackEvent> kills = new TreeMap<Integer, AttackEvent>();

	/**
	 * Vytvoření události z XML uzlu
	 * @param attNode XML uzel
	 * @param units Typy jednotek
	 * @param map Mapa, kde se hra odehrává
	 * @param t Pořadí události
	 */
	public AttackEvent(Node attNode, Map<Integer, UnitType> units, iwr.Map map,
			int t) {
		this.map = map;
		time = t;
		for (Node child = attNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p1")) {
				src = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("p2")) {
				dest = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("ca")) {
				attCount = NodeUtil.getInt(child);
			} else if (name.equals("ta")) {
				attType = units.get(NodeUtil.getInt(child));
			} else if (name.equals("cd")) {
				defCount = NodeUtil.getInt(child);
			} else if (name.equals("td")) {
				defType = units.get(NodeUtil.getMaybeInt(child));
			} else if (name.equals("r")) {
				result = NodeUtil.getBool(child);
			} else if (name.equals("s")) {
				survived = NodeUtil.getInt(child);
			}
		}
	}

	@Override
	void apply() {
		defender = dest.ownerAt(time - 1);
		attacker = src.ownerAt(time - 1);
		double distance = dest.distanceFrom(src); // TODO
		if (result) {
			if (defender != null) {
				defender.removeFieldAt(dest, time);
				if (defender.getHq(time - 1) == dest) { // bylo dobyto veleni
					hqDown = true;
					defender.killed(time);
					for (Field f : map.fields) {
						if (f.ownerAt(time - 1) == defender) { // smaze vsechna
							// pole
							// puvodniho
							// majitele
							f.changeOwnerAt(null, time);
							f.changeArmyAt(null, time);
						}
					}
					attacker.frag(time);
					kills.put(time, this);
				}
			}

			dest.changeOwnerAt(attacker, time);
			attacker.addFieldAt(dest, time);

			dest.changeArmyAt(new Army(attType, survived), time);
		} else {
			if (dest.armyAt(time) != null) {
				dest.changeArmyAt(new Army(defType, survived), time);
			}
		}
		src.removeArmyAt(attCount, time);
		src.ownerAt(time)
				.removeMovesAt(
						costOfAction(src.armyAt(time - 1).getUnit().getTurnsPerAttack(),
								distance), time);
	}

	@Override
	public String toString() {
		String pl;
		String maybeNot = ""; //$NON-NLS-1$
		String rest = Messages.getString("AttackEvent.fromWhichSurvived") + survived; //$NON-NLS-1$
		String defense;
		String hqd = ""; //$NON-NLS-1$
		if (defender != null) {
			pl = Messages.getString("AttackEvent.defender.DAT") + defender; //$NON-NLS-1$
		} else {
			pl = Messages.getString("AttackEvent.freeField"); //$NON-NLS-1$
		}
		if (result) {
			if (hqDown) {
				hqd = Messages.getString("AttackEvent.andScores"); //$NON-NLS-1$
			}
		} else {
			maybeNot = Messages.getString("AttackEvent.not"); //$NON-NLS-1$
		}
		if (defCount != 0) {
			defense = Messages.getString("AttackEvent.defended") + defCount + Messages.getString("AttackEvent.unitsOfType") + defType + (result ? "" : rest); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			defense = Messages.getString("AttackEvent.wasntDefended"); //$NON-NLS-1$
		}
		return super.toString()
				+ Messages.getString("AttackEvent.Player") + src.ownerAt(time) + " " //$NON-NLS-1$ //$NON-NLS-2$
				+ maybeNot
				+ Messages.getString("AttackEvent.conquered") + pl + Messages.getString("AttackEvent.field") + dest //$NON-NLS-1$ //$NON-NLS-2$
				+ Messages.getString("AttackEvent.which") + defense //$NON-NLS-1$
				+ hqd
				+ Messages.getString("AttackEvent.attackWasDoneFrom") + src + " " + attCount + Messages.getString("AttackEvent.unitsOfType.INS") + attType + (result ? rest : "") + "."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	}

}
