package iwr;

import org.w3c.dom.Node;

public class Unit {
	int id;
	String name;
	String description;
	int attack;
	int defense;
	int cost;
	int turnsPerAttack;
	int turnsPerMove;
	int lookoutBonus;

	public Unit(Node unitNode) {
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

	public int getId() {
		return id;
	}


	@Override
	public String toString() {
		return name;
	}
}
