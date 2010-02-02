package iwr;

public class Army {

	Unit unit;
	int count;
	public Army(Unit u, int c) {
		unit = u;
		count = c;
	}
	Army add(int c) {
		return new Army(unit, count+c);
	}
	Army add(Army army) {
		if (army == null) return this;
		if (unit != army.unit) return null;
		return new Army(unit, count+army.count);
		
	}
	Army remove(int c) {
		if ((count - c) <= 0) return null; 
		return new Army(unit, count-c);
	}
	
	@Override
	public String toString() {
		return count + " jednotek typu " + unit;
	}
	public int cost() {
		return count * unit.cost;
	}
}
