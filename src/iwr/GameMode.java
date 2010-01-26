/**
 * 
 */
package iwr;

import java.text.SimpleDateFormat;
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
			if (name.equals("id")) {
				id = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (name.equals("name")) {
				name = child.getFirstChild().getTextContent();
			} else if (name.equals("peace")) {
				try {
					protection = new SimpleDateFormat().parse(child.getFirstChild().getTextContent());
				} catch (Exception e) {
					protection = null;
				}
			} else if (name.equals("type")) {
				type = NodeUtil.getString(child);
			} else if (name.equals("timelimit")) {
				timelimit = NodeUtil.getDate(child);
			} else if (name.equals("mapvisafter")) {
				mapDisclose = NodeUtil.getDate(child);
			} else if (name.equals("speed")) {
				speed = NodeUtil.getInt(child);
			} else if (name.equals("prodef")) {
				effectivity = NodeUtil.getDouble(child);
			} else if (name.equals("startturns")) {
				initialMoves = NodeUtil.getInt(child);
			} else if (name.equals("startnectar")) {
				initialNectar = NodeUtil.getInt(child);
			} else if (name.equals("rating")) {
				rating = NodeUtil.getInt(child);
			} else if (name.equals("jumping")) {
				canJump = NodeUtil.getBool(child);
			} else if (name.equals("flags")) {
				hasFlags = NodeUtil.getBool(child);
			} else if (name.equals("unitspeedbonus")) {
				speedbonus = NodeUtil.getInt(child);
			} else if (name.equals("friendarmy")) {
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
