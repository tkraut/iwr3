package iwr;

import org.w3c.dom.Node;

/**
 * Typ hráče
 * @author Tomáš
 *
 */
public class Type {
	int id;
	String name;
	public Type(Node typeNode) {
		for (Node n = typeNode.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeName().equals("id")) {
				id = Integer.parseInt(n.getFirstChild().getNodeValue());
			} else if (n.getNodeName().equals("name")) {
				name = n.getFirstChild().getNodeValue();
			}
		}
	}
	@Override
	public String toString() {
		return name;
	}
}
