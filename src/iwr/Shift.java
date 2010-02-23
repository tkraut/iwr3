package iwr;

/**
 * Typ posunu ve hře
 * @author Tomáš Kraut
 *
 */
public enum Shift {
	/**
	 * Posun po tazích
	 */
	MOVE(Messages.getString("Shift.moves")),//$NON-NLS-1$
	/**
	 * Posun po vyřazeních (vždy o 1)
	 */
	KILL(Messages.getString("Shift.kills")),//$NON-NLS-1$
	/**
	 * Posun po hodinách
	 */
	HOUR(Messages.getString("Shift.hours")), //$NON-NLS-1$
	/**
	 * Posun po minutách
	 */
	MINUTE(Messages.getString("Shift.minutes")), //$NON-NLS-1$
	/**
	 * Posun po sekundách
	 */
	SECOND(Messages.getString("Shift.seconds"));   //$NON-NLS-1$
	
	/**
	 * Vytvoření - s nastavením řetězce
	 * @param value Příslušný řetězec
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
