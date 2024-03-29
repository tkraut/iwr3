package iwr;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;

/**
 * Typ hráče
 * 
 * @author Tomáš
 * 
 */
public class PlayerType {
	/**
	 * Typ louka, pro zpětnou kompatibilitu
	 * @deprecated
	 */
	final static public String LOUKA = "louka"; //$NON-NLS-1$
	/**
	 * Typ les, pro zpětnou kompatibilitu
	 * @deprecated
	 */
	final static public String LES = "les"; //$NON-NLS-1$
	/**
	 * ID typu
	 */
	int id;
	
	/**
	 * Název typu
	 */
	String name;
	/**
	 * Ikonka typu hráče
	 */
	ImageIcon icon;

	/**
	 * Vytvoří typ hráče z XML uzlu
	 * @param typeNode XML uzel &lt;playertype&gt;
	 */
	public PlayerType(Node typeNode) {
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
