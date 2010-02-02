package iwr;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;


/**
 * Pole hracího plánu
 * @author Tomáš
 *
 */
public class Field {
	
	public Field(Node fieldNode, TreeMap<Integer, FieldType> fieldTypes, String c) {
		int initTypeId = Integer.parseInt(fieldNode.getFirstChild().getTextContent());
		armyHistory = new TimeMap<Army>(null);
		ownerHistory = new TimeMap<Player>(null);
		typeHistory = new TimeMap<FieldType>(fieldTypes.get(initTypeId));
		coords = c;
	}
	
	TimeLine<FieldType> typeHistory;
	TimeLine<Player> ownerHistory;
	TimeLine<Army> armyHistory;
	String coords;
	
	/**
	 * Zaznamená změnu typu pole
	 * @param newType nový typ pole
	 * @param time čas, kdy změna nastala
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

	public void addArmyAt(int count, int time) { //assuming there is army
		armyHistory.changeLoadAt(armyAt(time).add(count), time);
	}

	public void removeArmyAt(int count, int time) { //assuming there is army
		if (armyAt(time) == null) return;
		armyHistory.changeLoadAt(armyAt(time).remove(count), time);
	}
	
	
	/**
	 * Vrátí typ pole v čase time
	 * @param time čas
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
	
	public List<ImageIcon> imageForAt(Player player, int time) {
		ArrayList<ImageIcon> list = new ArrayList<ImageIcon>(); 
		list.add(typeAt(time).getImg());
		Army army = armyAt(time); 
		Player owner = ownerAt(time);
		if (owner != null) {
			if (player == owner) {
				list.add(Images.get(Images.P_OWN));
				if (player.getHq(time) == this) list.add(Images.get(Images.O_HQ));
				if (army != null && army.count != 0) list.add(Images.get(Images.A_ARMY));
			} else {
				if (owner.type.name.equals(Type.LOUKA)) {
					list.add(Images.get(Images.T_LOUKA));
				} else if (owner.type.name.equals(Type.LES)) {
					list.add(Images.get(Images.T_LES));
				}
				if (player != null) {
					if (player.type == owner.type) {
						list.add(Images.get(Images.P_ALLY));
						if (army != null && army.count != 0) list.add(Images.get(Images.A_ARMY));
					} else {
						list.add(Images.get(Images.P_ENEMY));
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public String toString() {
		return coords;
	}

}
