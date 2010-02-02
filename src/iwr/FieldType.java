package iwr;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;


/**
 * Typ políčka
 * @author Tomáš
 *
 */
public class FieldType {
	int id;
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	protected ImageIcon img;
	
	public ImageIcon getImg() {
		return img;
	}
	
	String name;
	int armyvisible;
	/**
	 * produkce za přepočet
	 */
	int produce;
	/**
	 * jak daleko jednotka vidi
	 */
	int lookout;
	/**
	 * bonus k útoku
	 */
	int attbonus;
	/**
	 * bonus k obraně
	 */
	int defbonus;
	/**
	 * pole je dostupné
	 */
	boolean reachability;
	
	public FieldType(Node ftNode) {
		for (Node child = ftNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeName().equals("id")) {
				id = Integer.parseInt(child.getFirstChild().getTextContent());
				img = Images.get("f_"+id);
			} else if (child.getNodeName().equals("name")) {
				name = child.getFirstChild().getTextContent();
				//img = new ImageIcon("img/f_"+name+".png");
			} else if (child.getNodeName().equals("armyvisible")) {
				armyvisible = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("produce")) {
				produce = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("lookout")) {
				lookout = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("attbonus")) {
				attbonus = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("defbonus")) {
				defbonus = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("reachability")) {
				reachability = child.getFirstChild().getTextContent().equals("1");
			}
		}
	}
}
