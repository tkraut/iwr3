/**
 * 
 */
package iwr;

/**
 * Hlavní třída programu
 * @author Tomáš Kraut
 * 
 */
public class IWR {

	/**
	 * @param args Pokud je první parametr zadán, pokusí se načíst hru ze souboru na dané adrese
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new UI(args));
	}

}
