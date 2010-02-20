package iwr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;

/**
 * Pole hracího plánu
 * 
 * @author Tomáš
 * 
 */
public class Field {

	public Field(Node fieldNode, java.util.Map<Integer, FieldType> fieldTypes,
			java.util.Map<Integer, Unit> unitTypes, Map map, int x_c, int y_c) {
		Node typeNode = null, armyCountNode = null, armyTypeNode = null;
		for (Node child = fieldNode.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				typeNode = child;
			} else if (name.equals("at")) {
				armyTypeNode = child;
			} else if (name.equals("ac")) {
				armyCountNode = child;
			} else if (name.equals("flag")) {
				// Do something with flags
			}
		}
		if (typeNode == null) { // zpetna kompatibilita
			typeNode = fieldNode;
		}
		int initTypeId = NodeUtil.getInt(typeNode);
		Army army = null;
		if (armyCountNode != null && armyTypeNode != null) {
			int armyCount = NodeUtil.getInt(armyCountNode);
			Unit armyType = unitTypes.get(NodeUtil.getInt(armyTypeNode));
			army = new Army(armyType, armyCount);
		}
		armyHistory = new TimeMap<Army>(army);
		ownerHistory = new TimeMap<Player>(null);
		typeHistory = new TimeMap<FieldType>(fieldTypes.get(initTypeId));
		x = x_c;
		y = y_c;
		this.map = map;
	}

	TimeLine<FieldType> typeHistory;
	TimeLine<Player> ownerHistory;
	TimeLine<Army> armyHistory;
	int x, y;
	Map map;

	/**
	 * Zaznamená změnu typu pole
	 * 
	 * @param newType
	 *            nový typ pole
	 * @param time
	 *            čas, kdy změna nastala
	 */
	public void convertToAt(FieldType newType, int time) {
		typeHistory.changeLoadAt(newType, time);
	}

	public void changeOwnerAt(Player newOwner, int time) {
		ownerHistory.changeLoadAt(newOwner, time);
	}

	public void changeArmyAt(Army newArmy, int time) {
		armyHistory.changeLoadAt(newArmy, time);
	}

	public void addArmyAt(Army newArmy, int time) {
		armyHistory.changeLoadAt(newArmy.add(armyAt(time)), time);
	}

	public void addArmyAt(int count, int time) { // assuming there is army
		armyHistory.changeLoadAt(armyAt(time).add(count), time);
	}

	public void removeArmyAt(int count, int time) { // assuming there is army
		if (armyAt(time) == null)
			return;
		armyHistory.changeLoadAt(armyAt(time).remove(count), time);
	}

	/**
	 * Vrátí typ pole v čase time
	 * 
	 * @param time
	 *            čas
	 * @return FieldType typ pole v čase
	 */
	public FieldType typeAt(int time) {
		return typeHistory.loadAt(time);
	}

	public Player ownerAt(int time) {
		return ownerHistory.loadAt(time);
	}

	public Army armyAt(int time) {
		return armyHistory.loadAt(time);
	}

	public ImageIcon imageAt(int time) {
		return typeAt(time).getImg();
	}

	public List<ImageIcon> imageForAt(Player player, int time,
			boolean obeyVisibilityRules, Set<Field> visibility) {
		ArrayList<ImageIcon> list = new ArrayList<ImageIcon>();
		boolean visible = !obeyVisibilityRules || visibility.contains(this);
		if (!visible && typeAt(time).reachability) {
			list.add(Images.get(Images.F_INVISIBLE));
			return list;
		}
		list.add(typeAt(time).getImg());
		Army army = armyAt(time);
		Player owner = ownerAt(time);
		if (owner != null) {
			if (player == owner) {
				list.add(Images.get(Images.P_OWN));
				if (player.getHq(time) == this)
					list.add(Images.get(Images.O_HQ));
				if (army != null && army.count != 0)
					list.add(Images.get(Images.A_ARMY));
			} else {
				if (owner.type.icon != null) {
					list.add(owner.type.icon);
				} else if (owner.type.name.equals(Type.LOUKA)) { // zpetna
																	// kompatibilita
																	// se
																	// starymi
																	// zaznamy
					list.add(Images.get(Images.T_LOUKA));
				} else if (owner.type.name.equals(Type.LES)) {
					list.add(Images.get(Images.T_LES));
				}
				if (player != null) {
					if (player.type == owner.type) {
						list.add(Images.get(Images.P_ALLY));
						if (army != null && army.count != 0)
							list.add(Images.get(Images.A_ARMY));
					} else {
						list.add(Images.get(Images.P_ENEMY));
					}
				}
			}
		}
		if (!obeyVisibilityRules && army != null && army.count != 0)
			list.add(Images.get(Images.A_ARMY)); // ukazujeme armadu vsude
		return list;
	}

	public String getSCoords() {
		return Map.getSCoords(x, y);
	}

	@Override
	public String toString() {
		return getSCoords();
	}

	public double distanceFrom(Field other) {
		int dx = x - other.x;
		int dy = y - other.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public boolean visibleFromAt(Field other, int time) {
		return other.canSeeAt(this, time);
	}

	public int visibilityAt(int time) {
		int visibility = typeAt(time).lookout;
		Army army = armyAt(time);
		if (army != null) {
			visibility += army.unit.lookoutBonus;
		}
		return visibility;
	}

	public boolean canSeeAt(Field other, int time) {// prima viditelnost
		return visibilityAt(time) >= distanceFrom(other);
	}

	public Set<Field> visibleFieldsAt(int time) {
		return map.squareOfFields(this, visibilityAt(time));
	}

	public Coords getCoords() {
		return new Coords(x, y);
	}

}
