package iwr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;
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
	SortedMap<Integer, Player> players;
	SortedMap<Integer, Player> getPlayers() {
		return players;
	}
	ArrayList<Event> events;
	TreeMap<Integer, FieldType> fieldTypes;
	TreeMap<Integer, Type> playerTypes;
	TreeMap<Integer, Unit> unitTypes;
	iwr.Map map;
	int length, start = -1;
	int width, height;
	
	public void fromNode(Node gameNode){
		for (Node child = gameNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			//TODO: zarucit poradi
			String name = child.getNodeName();
			if (name.equals("map")) {
				map = new iwr.Map(child, width, height, fieldTypes); 
			} else if (name.equals("players")) {
				players = new TreeMap<Integer, Player>();
				for (Node player = child.getFirstChild(); player != null; player = player.getNextSibling()) {
					if (player.getNodeName().equals("player")) {
						Player p = new Player(player, playerTypes, mode);
						players.put(p.id, p);
					}
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
				for (Node world = child.getFirstChild(); world != null; world = world.getNextSibling()) {
					String wName = world.getNodeName();
					if (wName.equals("height")) {
						height = NodeUtil.getInt(world);
					} else if (wName.equals("width")) {
						width = NodeUtil.getInt(world);
					}
				}
			} else if (name.equals("wmod")) {
				mode = new GameMode(child);
			} else if (name.equals("events")) {
				events = new ArrayList<Event>();
				for (Node event = child.getFirstChild(); event != null; event = event.getNextSibling()) {
					if (event.getNodeType() == Node.ELEMENT_NODE) {
						Event newEvent = Event.parseNode(event, this);
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
					if (type.getNodeName().equals("usertype")) {
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
