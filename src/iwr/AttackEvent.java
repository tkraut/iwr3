package iwr;

import java.util.Map;

import org.w3c.dom.Node;

public class AttackEvent extends Event {
	//<!-- time, pos1, pos2, attcount, atttype, defcount, deftype, result, survive -->
	//<!ELEMENT att (t, p1, p2, ca, ta, cd, td, r, s)>
	
	Field src, dest;
	int attCount, defCount, survived;
	Unit attType, defType;
	iwr.Map map;
	boolean result, hqDown = false;
	Player formerOwner, attacker;
	public AttackEvent(Node attNode, Map<Integer, Unit> units, iwr.Map map, int t) {
		this.map = map;
		time = t;
		for (Node child = attNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			String name = child.getNodeName();
			if (name.equals("t")) {
				timestamp = NodeUtil.getDate(child);
			} else if (name.equals("p1")) {
				src = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("p2")) {
				dest = map.fieldAt(NodeUtil.getString(child));
			} else if (name.equals("ca")) {
				attCount = NodeUtil.getInt(child);
			} else if (name.equals("ta")) {
				attType = units.get(NodeUtil.getInt(child));
			} else if (name.equals("cd")) {
				defCount = NodeUtil.getInt(child);
			} else if (name.equals("td")) {
				defType = units.get(NodeUtil.getMaybeInt(child));
			} else if (name.equals("r")) {
				result = NodeUtil.getBool(child);
			} else if (name.equals("s")) {
				survived = NodeUtil.getInt(child);
			}
		}
	}
	
	@Override
	void apply() {
		formerOwner = dest.ownerAt(time-1);
		attacker = src.ownerAt(time-1);
		double distance = dest.distanceFrom(src);  // TODO
		if (result) {
			if (formerOwner != null) {
				formerOwner.removeFieldAt(dest, time);
				if (formerOwner.getHq(time-1) == dest) { //bylo dobyto veleni
					hqDown = true;
					formerOwner.killed(time);
					for(Field f:map.fields) {
						if (f.ownerAt(time-1)==formerOwner) { //smaze vsechna pole puvodniho majitele
							f.changeOwnerAt(null, time); 
							f.changeArmyAt(null, time);
						}
					}
					attacker.frag(time);
				}
			}
			
			dest.changeOwnerAt(attacker, time);
			attacker.addFieldAt(dest, time);
			
			dest.changeArmyAt(new Army(attType, survived), time);
		} else {
			if (dest.armyAt(time) != null) {
				dest.changeArmyAt(new Army(defType, survived), time);
			}
		}
		src.removeArmyAt(attCount, time);
		src.ownerAt(time).removeMovesAt(costOfAction(src.armyAt(time-1).unit.turnsPerAttack, distance), time);
	}

	@Override
	public String toString() {
		String pl;
		String maybeNot = "";
		String rest = ", z nichž přežilo " + survived;
		String defense;
		String hqd = "";
		if (formerOwner != null) {
			pl = "hráči " + formerOwner;
		} else {
			pl = "volné";
		}
		if (result) {
			if (hqDown) {
				hqd = ", a tím ho vyřadil ze světa";
			}
		} else {
			maybeNot = "ne";
		}
		if (defCount != 0) {
			defense = "bránilo " + defCount + " jednotek typu " + defType + (result?"":rest);
		} else {
			defense = "nebylo bráněné";
		}
		return "Hráč " + src.ownerAt(time) + " "
			+ maybeNot + "dobyl " + pl + " pole " + dest
			+ ", které " + defense 
			+ hqd
			+ ". Útok byl veden " + attCount + " jednotkami typu " + attType + (result?rest:"") + ".";
		
	}
	
}
