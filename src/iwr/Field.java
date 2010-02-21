package iwr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import org.w3c.dom.Node;

/**
 * Políčko hracího plánu
 * 
 * @author Tomáš Kraut
 * 
 */
public class Field {
	/**
	 * Změny armády v průběhu hry
	 */
	TimeLine<Army> armyHistory;
	/**
	 * Mapa
	 */
	Map map;
	/**
	 * Změny vlastníka v průběhu hry
	 */
	TimeLine<Player> ownerHistory;
	/**
	 * Změny geologie v průběhu hry
	 */
	TimeLine<FieldType> typeHistory;
	/**
	 * X souřadnice
	 */
	int x; 
	/**
	 * Y souřadnice
	 */
	int y;
	/**
	 * Vytvoření z XML uzlu
	 * @param fieldNode XML uzel
	 * @param fieldTypes Seznam typů polí
	 * @param unitTypes Seznam typů jednotek
	 * @param map Mapa
	 * @param x X souřadnice
	 * @param y Y souřadnice
	 */
	public Field(Node fieldNode, java.util.Map<Integer, FieldType> fieldTypes,
			java.util.Map<Integer, Unit> unitTypes, Map map, int x, int y) {
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
		this.x = x;
		this.y = y;
		this.map = map;
	}
	/**
	 * Přidá v čase T další armádu k té na políčku (předpokládá, že přidává stejnou nebo původně žádná nebyla)
	 * @param newArmy Přidaná armáda
	 * @param time Čas přidání
	 */
	public void addArmyAt(Army newArmy, int time) {
		armyHistory.changeLoadAt(newArmy.add(armyAt(time)), time);
	}
	/**
	 * Přidá v čase T daný počet jednotek na pole (předpokládá, že tam už armáda je)
	 * @param count Počet přidaných jednotek
	 * @param time Čas přidání
	 */
	public void addArmyAt(int count, int time) { // assuming there is army
		armyHistory.changeLoadAt(armyAt(time).add(count), time);
	}
	/**
	 * Vrátí armádu na poli v čase T
	 * @param time
	 * @return Armáda, přítomná na poli
	 */
	public Army armyAt(int time) {
		return armyHistory.loadAt(time);
	}
	
	/**
	 * Zjistí, zda je v daném čase přímo vidět na jiné pole
	 * @param other Pole, na které chceme vidět
	 * @param time Čas ve hře
	 * @return true, pokud je na vybrané pole přímá viditelnost, jinak false
	 */
	public boolean canSeeAt(Field other, int time) {// prima viditelnost
		return visibilityAt(time) >= distanceFrom(other);
	}

	/**
	 * Zaznamená změnu typu pole
	 * 
	 * @param newType
	 *            Nový typ pole
	 * @param time
	 *            Čas, kdy změna nastala
	 */
	public void convertToAt(FieldType newType, int time) {
		typeHistory.changeLoadAt(newType, time);
	}

	/**
	 * Spočítá vzdálenost od daného pole
	 * @param other Pole, ke kterému počítáme vzdálenost
	 * @return Vzdálenost (nezaokrouhlená)
	 */
	public double distanceFrom(Field other) {
		int dx = x - other.x;
		int dy = y - other.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Získání souřadnic pole
	 * @return Souřadnice tohoto pole
	 */
	public Coords getCoords() {
		return new Coords(x, y);
	}

	/**
	 * Získání písmenně-číselných souřardnic pole
	 * @return Řetězec, reprezentující toto pole ve hře
	 */
	public String getSCoords() {
		return Map.getSCoords(x, y);
	}

	/**
	 * Zaznamená změnu armády na poliu
	 * @param newArmy Změněná armáda na poli
	 * @param time Čas změny
	 */
	public void changeArmyAt(Army newArmy, int time) {
		armyHistory.changeLoadAt(newArmy, time);
	}

	/**
	 * Zaznamená změnu vlastníka pole
	 * @param newOwner Nový vlastník 
	 * @param time Čas změny
	 */
	public void changeOwnerAt(Player newOwner, int time) {
		ownerHistory.changeLoadAt(newOwner, time);
	}

	/**
	 * Získání obrázku typu v čase T
	 * @param time Čas
	 * @return Obrázek typu
	 */
	public ImageIcon imageAt(int time) {
		return typeAt(time).getImg();
	}

	/**
	 * Získání všech obrázků pro toto pole a určného hráče a čas 
	 * @param player Vybraný hráč
	 * @param time Čas
	 * @param obeyVisibilityRules Určení, zda bereme v potaz viditelnost polí(armády, velení...)
	 * @param visibility Pole, na která určený hráč vidí
	 * @return Seznam obrázků, které se mají vykreslit
	 */
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
				if (army != null && army.getCount() != 0)
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
						if (army != null && army.getCount() != 0)
							list.add(Images.get(Images.A_ARMY));
					} else {
						list.add(Images.get(Images.P_ENEMY));
					}
				}
			}
		}
		if (!obeyVisibilityRules && army != null && army.getCount() != 0)
			list.add(Images.get(Images.A_ARMY)); // ukazujeme armadu vsude
		return list;
	}

	/**
	 * Získání vlastníka pole v čase
	 * @param time Čas
	 * @return Vlastník pole
	 */
	public Player ownerAt(int time) {
		return ownerHistory.loadAt(time);
	}

	/**
	 * Odebrání daného počtu jednotek v čase (předpoklad, že tam je nějaká armáda)
	 * @param count Počet jednotek
	 * @param time Čas
	 */
	public void removeArmyAt(int count, int time) { // assuming there is army
		if (armyAt(time) == null)
			return;
		armyHistory.changeLoadAt(armyAt(time).remove(count), time);
	}

	@Override
	public String toString() {
		return getSCoords();
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

	/**
	 * Získání vzdálenosti, na kterou je z tohoto polev čase T vidět 
	 * @param time Čas
	 * @return Vzdálenost, kam lze dohlédnout
	 */
	public int visibilityAt(int time) {
		int visibility = typeAt(time).lookout;
		Army army = armyAt(time);
		if (army != null) {
			visibility += army.getUnit().lookoutBonus;
		}
		return visibility;
	}

	/**
	 * Získání polí, na která je v čase T odsud vidět
	 * @param time Čas
	 * @return Množina viditelných polí
	 */
	public Set<Field> visibleFieldsAt(int time) {
		return map.squareOfFields(this, visibilityAt(time));
	}

	/**
	 * Zjištění, zda je toto pole viditelné v čase T přímo z daného pole
	 * @param other Dané pole
	 * @param time Čas
	 * @return true, pokud je sem vidět, jinak false
	 */
	public boolean visibleFromAt(Field other, int time) {
		return other.canSeeAt(this, time);
	}

}
