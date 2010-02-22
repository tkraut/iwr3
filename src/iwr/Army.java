package iwr;

/**
 * Třída, reprezentující armádu. Immutable.
 * @author Tomáš Kraut
 *
 */
public class Army {

	/**
	 * Typ jednotky
	 */
	private final UnitType unit;
	/**
	 * Počet jednotek
	 */
	private final int count;

	/**
	 * Vytvoří novou armádu ze zadaného typu a počtu
	 * @param unit Typ jednotek
	 * @param count Počet jednotek
	 */
	public Army(UnitType unit, int count) {
		this.unit = unit;
		this.count = count;
	}

	/**
	 * Vytvoří novou armádu přidáním stejných jednotek
	 * @param count Počet přidaných jednotek
	 * @return Nová armáda
	 */
	Army add(int count) {
		return new Army(getUnit(), this.count + count);
	}

	/**
	 * Vytvoří novou armádu přidáním jiné. Pokud nesouhlasí typy jednotek, vrátí null.
	 * @param army Přidaná armáda
	 * @return Nová armáda nebo null
	 */
	Army add(Army army) {
		if (army == null)
			return this;
		if (getUnit() != army.getUnit())
			return null;
		return new Army(getUnit(), count + army.count);

	}

	/**
	 * Vytvoří novou armádu odebráním počtu jednotek. Pokud nezbude žádná, vrací null
	 * @param c Počet odebraných jednotek
	 * @return Zmenšená armáda nebo null
	 */
	Army remove(int c) {
		if ((count - c) <= 0)
			return null;
		return new Army(getUnit(), count - c);
	}

	@Override
	public String toString() {
		return count + Messages.getString("Army.unitsOfType") + getUnit(); //$NON-NLS-1$
	}

	/**
	 * Zjistí nákupní cenu armády.
	 * @return Cena armády
	 */
	public int cost() {
		return count * getUnit().cost;
	}

	/**
	 * Getter pro typ
	 * @return Typ jednotek
	 */
	public UnitType getUnit() {
		return unit;
	}

	/**
	 * Getter pro počet
	 * @return Počet jednotek
	 */
	public int getCount() {
		return count;
	}
}
