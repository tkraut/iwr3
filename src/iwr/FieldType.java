package iwr;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;

/**
 * Typ políčka
 * 
 * @author Tomáš
 * 
 */
public class FieldType {
	/**
	 * zatím neimplementováno ve hře, v záznamech vždy 0
	 */
	int armyvisible;
	/**
	 * bonus k útoku
	 */
	int attbonus;

	/**
	 * bonus k obraně
	 */
	int defbonus;

	/**
	 * Identifikátor typu
	 */
	private int id;

	/**
	 * Obrázek typu
	 */
	protected ImageIcon img = null;

	/**
	 * jak daleko jednotka na tomto poli vidí
	 */
	int lookout;
	/**
	 * Název typu pole
	 */
	String name;
	/**
	 * produkce za přepočet
	 */
	int produce;
	/**
	 * pole je dostupné
	 */
	boolean reachability;
	/**
	 * Vytvoření z XML uzlu
	 * @param ftNode XML uzel
	 */
	public FieldType(Node ftNode) {
		for (Node child = ftNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child.getNodeName().equals("id")) {
				id = NodeUtil.getInt(child);
				if (img == null)
					img = Images.get("f_" + id);
			} else if (child.getNodeName().equals("name")) {
				name = NodeUtil.getString(child);
			} else if (child.getNodeName().equals("picture")) {
				img = Images.get(NodeUtil.getString(child));
			} else if (child.getNodeName().equals("armyvisible")) {
				armyvisible = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("produce")) {
				produce = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("lookout")) {
				lookout = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("attbonus")) {
				attbonus = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("defbonus")) {
				defbonus = NodeUtil.getInt(child);
			} else if (child.getNodeName().equals("reachability")) {
				reachability = NodeUtil.getBool(child);
			}
		}
	}
	/**
	 * Getter pro id
	 * @return id
	 */
	public int getId() {
		return id;
	}
	/**
	 * Getter pro img
	 * @return img
	 */
	public ImageIcon getImg() {
		return img;
	}

	@Override
	public String toString() {
		return name;
	}

}
