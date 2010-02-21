package iwr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
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
 * 
 * @author Tomáš Kraut
 * 
 */
public class Game {
	
	/**
	 * Mapování čas -> herní čas
	 */
	NavigableMap<Date, Integer> timeOfEvents;
	/**
	 * Seznam událostí
	 */
	ArrayList<Event> events;
	/**
	 * Seznam typů polí
	 */
	Map<Integer, FieldType> fieldTypes;
	/**
	 * Výška mapy
	 */
	int height;
	/**
	 * Seznam vyřazení ze světa
	 */
	private NavigableMap<Integer, Event> kills;
	/**
	 * Počet událostí ve světě
	 */
	int length;
	/**
	 * Mapa světa
	 */
	iwr.Map map;
	/**
	 * Mód hry
	 */
	GameMode mode;
	/**
	 * Seznam hráčů
	 */
	private Map<Integer, Player> players;
	/**
	 * Seznam typů hráčů
	 */
	Map<Integer, Type> playerTypes;
	/**
	 * Poslední akce pod protekcí
	 */
	int protection = -1;
	/**
	 * Čas konce protekce
	 */
	Date protectionEnds;
	/**
	 * Číslo akce spuštění světa
	 */
	int start = -1;
	/**
	 * Čas spuštění světa
	 */
	Date startTime;
	/**
	 * Seznam typů jednotek
	 */
	Map<Integer, Unit> unitTypes;
	/**
	 * Šířka mapy
	 */
	int width;
	/**
	 * Vytvoří nový svět načtením ze souboru
	 * @param record Soubor, ze kterého má být načítáno
	 */
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
			JOptionPane
					.showMessageDialog(
							null,
							Messages.getString("Game.errInRec") + e.getMessage(), "", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			// e.printStackTrace();
		} catch (IOException e) {
			JOptionPane
					.showMessageDialog(
							null,
							Messages.getString("Game.errDuringLoading") + e.getMessage(), "", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			// e.printStackTrace();
		}
	}
	/**
	 * Vytvoří nový svět načtením z XML uzlu
	 * @param gameNode XML uzel
	 */
	public Game(Node gameNode) {
		fromNode(gameNode);
	}
	/**
	 * Načte svět z XML uzlu
	 * @param gameNode XML uzel
	 */
	public void fromNode(Node gameNode) {
		Node mapNode = null, playersNode = null, unitsNode = null, fieldtypesNode = null;
		Node worldNode = null, wmodNode = null, eventsNode = null, utypesNode = null;
		for (Node child = gameNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("map")) { //$NON-NLS-1$
				mapNode = child;
			} else if (name.equals("players")) { //$NON-NLS-1$
				playersNode = child;
			} else if (name.equals("units")) { //$NON-NLS-1$
				unitsNode = child;
			} else if (name.equals("fieldtypes")) { //$NON-NLS-1$
				fieldtypesNode = child;
			} else if (name.equals("world")) { //$NON-NLS-1$
				worldNode = child;
			} else if (name.equals("wmod")) { //$NON-NLS-1$
				wmodNode = child;
			} else if (name.equals("events")) { //$NON-NLS-1$
				eventsNode = child;
			} else if (name.equals("utypes")) { //$NON-NLS-1$
				utypesNode = child;
			}
		}

		playerTypes = new TreeMap<Integer, Type>();
		for (Node type = utypesNode.getFirstChild(); type != null; type = type
				.getNextSibling()) {
			if (type.getNodeName().equals("usertype")) { //$NON-NLS-1$
				Type newType = new Type(type);
				playerTypes.put(newType.id, newType);
			}
		}

		unitTypes = new TreeMap<Integer, Unit>();
		for (Node unit = unitsNode.getFirstChild(); unit != null; unit = unit
				.getNextSibling()) {
			Unit newUnit = new Unit(unit);
			if (unit.getNodeName().equals("unit"))unitTypes.put(newUnit.getId(), newUnit); //$NON-NLS-1$
		}

		fieldTypes = new TreeMap<Integer, FieldType>();
		for (Node type = fieldtypesNode.getFirstChild(); type != null; type = type
				.getNextSibling()) {
			if (!type.getNodeName().equals("fieldtype"))continue; //$NON-NLS-1$
			FieldType ft = new FieldType(type);
			fieldTypes.put(ft.getId(), ft);
		}

		mode = new GameMode(wmodNode);

		players = new TreeMap<Integer, Player>();
		for (Node player = playersNode.getFirstChild(); player != null; player = player
				.getNextSibling()) {
			if (player.getNodeName().equals("player")) { //$NON-NLS-1$
				Player p = new Player(player, playerTypes, mode);
				players.put(p.id, p);
			}
		}

		for (Node world = worldNode.getFirstChild(); world != null; world = world
				.getNextSibling()) {
			String wName = world.getNodeName();
			if (wName.equals("height")) { //$NON-NLS-1$
				height = NodeUtil.getInt(world);
			} else if (wName.equals("width")) { //$NON-NLS-1$
				width = NodeUtil.getInt(world);
			}
		}
		map = new iwr.Map(mapNode, width, height, fieldTypes, unitTypes);

		events = new ArrayList<Event>();
		timeOfEvents = new TreeMap<Date, Integer>();
		timeOfEvents.put(new Date(0), 0);
		for (Node event = eventsNode.getFirstChild(); event != null; event = event
				.getNextSibling()) {
			if (event.getNodeType() == Node.ELEMENT_NODE) {
				Event newEvent = Event.parseNode(event, this);
				if (newEvent != null) {
					try {
						newEvent.apply();
						events.add(newEvent);
						timeOfEvents.put(newEvent.timestamp, newEvent.time);
						++length;
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, Messages
								.getString("Game.wrongRecAt") + length); //$NON-NLS-1$
					}
				}
			}
		}
		assert protection > 0 : Messages.getString("Game.protectionNotSet"); //$NON-NLS-1$

	}

	/**
	 * Getter pro kills
	 * @return Seznam vyřazení ze světa
	 */
	public NavigableMap<Integer, Event> getKills() {
		return kills;
	}

	/**
	 * Getter pro players
	 * @return Seznam hráčů
	 */
	public Map<Integer, Player> getPlayers() {
		return players;
	}
	
	/**
	 * Provedení přepočtu
	 * @param time Pořadí akce
	 */
	public void recount(int time) {
		Map<Player, Integer> production = new HashMap<Player, Integer>();
		for (Player p : players.values()) {
			production.put(p, 0);
		}
		for (Field f : map.fields) {
			Player owner = f.ownerAt(time);
			if (owner != null) {
				production.put(owner, production.get(owner)
						+ f.typeAt(time).produce);
			}
		}
		for (Player p : players.values()) {
			p.recountAt(production.get(p), mode.speed, time);
		}

	}

}
