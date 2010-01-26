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
	
	public Field(Node fieldNode, TreeMap<Integer, FieldType> fieldTypes) {
		int initTypeId = Integer.parseInt(fieldNode.getFirstChild().getTextContent());
		armyHistory = new TimeMap<Army>(null);
		ownerHistory = new TimeMap<Player>(null);
		typeHistory = new TimeMap<FieldType>(fieldTypes.get(initTypeId));
	}
	
	TimeLine<FieldType> typeHistory;
	TimeLine<Player> ownerHistory;
	TimeLine<Army> armyHistory;
	
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
		if (army != null && army.count != 0) list.add(new ImageIcon("img/a.gif"));
		if (player == ownerAt(time)) {
			//list.add
		}
		return list;
	}

}
