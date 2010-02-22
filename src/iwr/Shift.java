package iwr;

/**
 * Typ posunu ve hře
 * @author Tomáš Kraut
 *
 */
public enum Shift {
	MOVE(Messages.getString("Shift.moves")), KILL(Messages.getString("Shift.kills")), HOUR("hodiny"), MINUTE("minuty"), SECOND("sekundy"); //$NON-NLS-1$ //$NON-NLS-2$
	
	/**
	 * Vytvoření - s nastavením řetězce
	 */
	private Shift(String value) {
		this.value = value;
	}
	
	/**
	 * Řetězec pro výpisy 
	 */
	private final String value;


	@Override
	public String toString() {
		return value;
	}

}
