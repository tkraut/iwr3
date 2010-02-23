package iwr;

/**
 * Třída, implementující TimeLine poskytuje možnost zjištění stavu nějakého nákladu v určitém čase.
 * Vždy by měla poskytovat stav po změně s časem menším nebo rovným, než je zjišťovaný..
 * @author Tomáš Kraut
 *
 * @param <L> Náklad - jev, proměnná, či stav, který se v čase mění
 */
public interface TimeLine<L> {
	/**
	 * Zaznamenání změny nákladu
	 * @param newLoad Změněný náklad
	 * @param time Čas, kdy změna nastala
	 */
	void changeLoadAt(L newLoad, int time);
	/**
	 * Zjištění stavu nákladu v čase
	 * @param time Čas
	 * @return Náklad v čase
	 */
	L loadAt(int time);
}
