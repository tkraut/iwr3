package iwr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * Zaznamenaná hra
 * @author Tomáš
 *
 */
public class Game {

	GameMode mode;
	List<Player> players;
	List<Player> getPlayers() {
		return players;
	}
	ArrayList<Event> events;
	TreeMap<Integer, FieldType> fieldTypes;
	TreeMap<Integer, Type> playerTypes;
	TreeMap<Integer, Unit> unitTypes;
	Map map;
	int length;
	
	public void fromNode(Node gameNode){
		for (Node child = gameNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("map")) {
				map = new Map(child, 8,8, fieldTypes); // TODO nevycucavat cisla z prstu
			} else if (name.equals("players")) {
				players = new ArrayList<Player>();
				for (Node player = child.getFirstChild(); player != null; player = player.getNextSibling()) {
					if (player.getNodeName().equals("player")) players.add(new Player(player, playerTypes));
				}
			} else if (name.equals("units")) {
				unitTypes = new TreeMap<Integer, Unit>();
				for (Node unit = child.getFirstChild(); unit != null; unit = unit.getNextSibling()) {
					Unit newUnit = new Unit(unit);
					if (unit.getNodeName().equals("unit")) unitTypes.put(newUnit.getId(), newUnit);
				}
			} else if (name.equals("fieldtypes")) {
				fieldTypes = new TreeMap<Integer, FieldType>();
				for (Node type = child.getFirstChild(); type != null; type = type.getNextSibling()) {
					if (!type.getNodeName().equals("fieldtype")) continue;
					FieldType ft = new FieldType(type);
					fieldTypes.put(ft.getId(), ft);
				}
			} else if (name.equals("world")) {
				
			} else if (name.equals("wmod")) {
				mode = new GameMode(child);
			} else if (name.equals("events")) {
				events = new ArrayList<Event>();
				int now = 0;
				for (Node event = child.getFirstChild(); event != null; event = event.getNextSibling()) {
					if (event.getNodeType() == Node.ELEMENT_NODE) {
						Event newEvent = null;
						String eventName = event.getNodeName();
						if (eventName.equals("cfd")) {
							newEvent = new ConvertEvent(event, map, fieldTypes, now++);
						}
						
						
						
						if (newEvent != null) {
							newEvent.apply();
							events.add(newEvent);
							++length;
						}
					}
				}
			} else if (name.equals("utypes")){
				playerTypes = new TreeMap<Integer, Type>();
				for (Node type = child.getFirstChild(); type != null; type = type.getNextSibling()) {
					if (type.getNodeName().equals("utype")) {
						Type newType = new Type(type);
						playerTypes.put(newType.id, newType);
					}
				}
			}
		}
		
	}
	
	public Game(Node gameNode) {
		fromNode(gameNode);
	}
	
	public Game(File record) {
		DocumentBuilder builder;
		Document doc;
		Node game;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.parse(record);
			game = doc.getFirstChild();
			fromNode(game);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
