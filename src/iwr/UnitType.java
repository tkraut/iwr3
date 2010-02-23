package iwr;

import org.w3c.dom.Node;

/**
 * Typ jednotky
 * @author Tomáš Kraut
 *
 */
public class UnitType {
	/**
	 * ID typu jednotky
	 */
	private int id;
	/**
	 * Jméno jednotky
	 */
	private String name;
	/**
	 * Popis jednotky
	 */
	private String description;
	/**
	 * Útočná síla jednotky 
	 */
	private int attack;
	/**
	 * Obranná síla jednotky
	 */
	private int defense;
	/**
	 * Cena jednotky
	 */
	private int cost;
	/**
	 * Potřebný počet tahů na útok jednotky
	 */
	private int turnsPerAttack;
	/**
	 * Potřebný počet tahů na přesun jednotky
	 */
	private int turnsPerMove;
	/**
	 * Lookout bonus - o kolik dál jednotka vidí, než je standardní výhled z políčka
	 */
	private int lookoutBonus;

	/**
	 * Vytvoří typ z XML uzlu
	 * @param unitNode XML uzel &lt;unit&gt;
	 */
	public UnitType(Node unitNode) {
		for (Node child = unitNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child.getNodeName().equals("id")) {
				id = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("name")) {
				name = NodeUtil.getString(child); 
			} else if (child.getNodeName().equals("desc")) {
				description = child.getFirstChild().getTextContent();
			} else if (child.getNodeName().equals("att")) {
				attack = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("def")) {
				defense = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("cost")) {
				cost = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("turnsatt")) {
				turnsPerAttack = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("turnsmove")) {
				turnsPerMove = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("lobonus")) {
				lookoutBonus = NodeUtil.getInt(child);
			}
		}

	}

	
	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}



	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * @return the attack
	 */
	public int getAttack() {
		return attack;
	}


	/**
	 * @return the defense
	 */
	public int getDefense() {
		return defense;
	}


	/**
	 * @return the turnsPerAttack
	 */
	public int getTurnsPerAttack() {
		return turnsPerAttack;
	}


	/**
	 * @return the turnsPerMove
	 */
	public int getTurnsPerMove() {
		return turnsPerMove;
	}


	/**
	 * @return the lookoutBonus
	 */
	public int getLookoutBonus() {
		return lookoutBonus;
	}


	@Override
	public String toString() {
		return name;
	}
}
