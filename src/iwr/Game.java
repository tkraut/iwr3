package iwr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JOptionPane;
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
		Node mapNode = null, playersNode = null, unitsNode = null, fieldtypesNode = null;
		Node worldNode = null, wmodNode = null, eventsNode = null, utypesNode = null;
		for (Node child = gameNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("map")) {
				mapNode = child;
			} else if (name.equals("players")) {
				playersNode = child;
			} else if (name.equals("units")) {
				unitsNode = child;
			} else if (name.equals("fieldtypes")) {
				fieldtypesNode = child;
			} else if (name.equals("world")) {
				worldNode = child;
			} else if (name.equals("wmod")) {
				wmodNode = child;
			} else if (name.equals("events")) {
				eventsNode = child;
			} else if (name.equals("utypes")){
				utypesNode = child;
			}
		}
		
		playerTypes = new TreeMap<Integer, Type>();
		for (Node type = utypesNode.getFirstChild(); type != null; type = type.getNextSibling()) {
			if (type.getNodeName().equals("usertype")) {
				Type newType = new Type(type);
				playerTypes.put(newType.id, newType);
			}
		}

		unitTypes = new TreeMap<Integer, Unit>();
		for (Node unit = unitsNode.getFirstChild(); unit != null; unit = unit.getNextSibling()) {
			Unit newUnit = new Unit(unit);
			if (unit.getNodeName().equals("unit")) unitTypes.put(newUnit.getId(), newUnit);
		}

		fieldTypes = new TreeMap<Integer, FieldType>();
		for (Node type = fieldtypesNode.getFirstChild(); type != null; type = type.getNextSibling()) {
			if (!type.getNodeName().equals("fieldtype")) continue;
			FieldType ft = new FieldType(type);
			fieldTypes.put(ft.getId(), ft);
		}
		
		mode = new GameMode(wmodNode);

		players = new TreeMap<Integer, Player>();
		for (Node player = playersNode.getFirstChild(); player != null; player = player.getNextSibling()) {
			if (player.getNodeName().equals("player")) {
				Player p = new Player(player, playerTypes, mode);
				players.put(p.id, p);
			}
		}

		for (Node world = worldNode.getFirstChild(); world != null; world = world.getNextSibling()) {
			String wName = world.getNodeName();
			if (wName.equals("height")) {
				height = NodeUtil.getInt(world);
			} else if (wName.equals("width")) {
				width = NodeUtil.getInt(world);
			}
		}
		map = new iwr.Map(mapNode, width, height, fieldTypes, unitTypes); 

		events = new ArrayList<Event>();
		for (Node event = eventsNode.getFirstChild(); event != null; event = event.getNextSibling()) {
			if (event.getNodeType() == Node.ELEMENT_NODE) {
				Event newEvent = Event.parseNode(event, this);
				if (newEvent != null) {
					newEvent.apply();
					events.add(newEvent);
					++length;
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
			e.printStackTrace();
		} catch (SAXException e) {
			JOptionPane.showMessageDialog(null,"Chyba v záznamu: "+e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Chyba při načítání záznamu: "+e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
		}
	}

	public void recount(int time) {
		Map<Player, Integer> production = new HashMap<Player, Integer>();
		for (Player p:players.values()) {
			production.put(p, 0);
		}
		for (Field f:map.fields) {
			Player owner = f.ownerAt(time);
			if (owner != null) {
				production.put(owner, production.get(owner) + f.typeAt(time).produce);
			}
		}
		for(Player p:players.values()) {
			p.recountAt(production.get(p), mode.speed, time);
		}
		
	}
	
	
	
}
