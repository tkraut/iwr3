package iwr;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;

/**
 * Typ hráče
 * 
 * @author Tomáš
 * 
 */
public class Type {
	final static public String LOUKA = "louka"; //$NON-NLS-1$
	final static public String LES = "les"; //$NON-NLS-1$
	int id;
	String name;
	ImageIcon icon;

	public Type(Node typeNode) {
		for (Node n = typeNode.getFirstChild(); n != null; n = n
				.getNextSibling()) {
			if (n.getNodeName().equals("id")) { //$NON-NLS-1$
				id = Integer.parseInt(n.getFirstChild().getNodeValue());
			} else if (n.getNodeName().equals("name")) { //$NON-NLS-1$
				name = n.getFirstChild().getNodeValue();
			} else if (n.getNodeName().equals("picture")) { //$NON-NLS-1$
				icon = Images.get(NodeUtil.getString(n));
			}
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
