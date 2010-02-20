/**
 * 
 */
package iwr;

import java.util.Date;

import org.w3c.dom.Node;

/**
 * Pravidla hry
 * @author Tomáš
 */
public class GameMode {
	public GameMode(Node gmNode) {
		// TODO
		for (Node child = gmNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName(); 
			if (name.equals("id")) { //$NON-NLS-1$
				id = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (name.equals("name")) { //$NON-NLS-1$
				name = child.getFirstChild().getTextContent();
			} else if (name.equals("peace")) { //$NON-NLS-1$
				protection = NodeUtil.getTime(child);
			} else if (name.equals("type")) { //$NON-NLS-1$
				type = NodeUtil.getString(child);
			} else if (name.equals("timelimit")) { //$NON-NLS-1$
				timelimit = NodeUtil.getTime(child); //neimplementovano
			} else if (name.equals("mapvisafter")) { //$NON-NLS-1$
				mapDisclose = NodeUtil.getDate(child); //neimplementovano
			} else if (name.equals("speed")) { //$NON-NLS-1$
				speed = NodeUtil.getInt(child);
			} else if (name.equals("prodef")) { //$NON-NLS-1$
				effectivity = NodeUtil.getDouble(child);
			} else if (name.equals("startturns")) { //$NON-NLS-1$
				initialMoves = NodeUtil.getInt(child);
			} else if (name.equals("startnectar")) { //$NON-NLS-1$
				initialNectar = NodeUtil.getInt(child);
			} else if (name.equals("rating")) { //$NON-NLS-1$
				rating = NodeUtil.getInt(child);
			} else if (name.equals("jumping")) { //$NON-NLS-1$
				canJump = NodeUtil.getBool(child);
			} else if (name.equals("flags")) { //$NON-NLS-1$
				hasFlags = NodeUtil.getBool(child);
			} else if (name.equals("unitspeedbonus")) { //$NON-NLS-1$
				speedbonus = NodeUtil.getInt(child);
			} else if (name.equals("friendarmy")) { //$NON-NLS-1$
				hasFriendArmy = NodeUtil.getBool(child);
			}
		}

	}
	public int id;
	public String name;
	public Date protection;
	public String type;
	public Date timelimit;
	public Date mapDisclose;
	public int speed;
	public double effectivity;
	public int initialMoves;
	public int initialNectar;
	public int rating;
	public boolean canJump;
	public boolean hasFlags;
	public int speedbonus;
	public boolean hasFriendArmy;
	public boolean applyGlobalEvents;
	
	
}
