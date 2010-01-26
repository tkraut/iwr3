package iwr;

import org.w3c.dom.Node;

public class Unit {
	int id;
	public Unit(Node unitNode) {
		for (Node child = unitNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeName().equals("id")) {
				id = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("name")) {
				name = child.getFirstChild().getTextContent();
			} else if (child.getNodeName().equals("desc")) {
				description = child.getFirstChild().getTextContent();
			} else if (child.getNodeName().equals("att")) {
				attack = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("def")) {
				defense = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("cost")) {
				cost = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("turnsatt")) {
				turnsPerAttack = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("turnsmove")) {
				turnsPerMove = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("lobonus")) {
				lookoutBonus = Integer.parseInt(child.getFirstChild().getTextContent());
			}
		}
		
	}
	public int getId() {
		return id;
	}
	String name;
	String description;
	int attack;
	int defense;
	int cost;
	int turnsPerAttack;
	int turnsPerMove;
	int lookoutBonus;
}
