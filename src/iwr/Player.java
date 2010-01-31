package iwr;

import java.util.Date;
import java.util.TreeMap;

import org.w3c.dom.Node;


/**
 * Hráč ve hře
 * @author Tomáš
 *
 */
public class Player {
	class Resources {
		public Resources(GameMode mode) {
			movesHistory = new TimeMap<Integer>(mode.initialMoves);
			nectarHistory = new TimeMap<Integer>(mode.initialNectar);
			hqHistory = new TimeMap<Field>(null);
			receivedNeqHistory= new TimeMap<Integer>(0);
			destroyedHistory = new TimeMap<Integer>(0);
		}
		TimeLine<Integer> movesHistory;
		TimeLine<Integer> nectarHistory;
		TimeLine<Field> hqHistory;
		TimeLine<Integer> receivedNeqHistory;
		TimeLine<Integer> destroyedHistory;
	}
	
	
	/**
	 * Jmeno hrace <nick>
	 */
	protected String nick;
	/**
	 * ID hrace <id>
	 */
	protected int id;
	/**
	 * Typ <type>
	 */
	protected Type type;
	/**
	 * Aliance hrace <ali>
	 * Prozatim nevyuzite
	 */
	protected Aliance aliance;
	/**
	 * Datum registrace hrace <regdate>
	 */
	protected Date regdate;
	
	protected Resources resources;
	
	public void setHq(Field hq, int time) {
		resources.hqHistory.changeLoadAt(hq, time);
	}
	
	public Field getHq(int time) {
		return resources.hqHistory.loadAt(time);
	}
	
	public Player(Node playerNode, TreeMap<Integer, Type> playerTypes, GameMode mode) {
		resources = new Resources(mode);
		for (Node child = playerNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeName().equals("id")) {
				id = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("nick")) {
				nick = child.getFirstChild().getTextContent();
			} else if (child.getNodeName().equals("type")) {
				type = playerTypes.get(Integer.parseInt(child.getFirstChild().getTextContent()));
			} else if (child.getNodeName().equals("ali")) {
				//ali = Integer.parseInt(child.getFirstChild().getTextContent());
			} else if (child.getNodeName().equals("regdate")) {
				//regdate = new Date(child.getFirstChild().getTextContent());
			}
		}
		//<player><id>66</id><nick>asu</nick><type>2</type><ali></ali><regdate>2005-03-06 19:27:08</regdate></player>
		
	}
	
	public PlayerResources resourcesAt(int time) {
		//TODO
		return null;
	}
	
	@Override
	public String toString() {
		return nick+" ("+type+")";
	}
	
}
