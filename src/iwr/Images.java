package iwr;

import java.awt.MediaTracker;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * Pomocná třída pro snazší načítání obrázků
 * @author Tomáš Kraut
 *
 */
public class Images {
	/**
	 * Statické úložiště pro obrázky
	 */
	protected static Map<String, ImageIcon> map = new HashMap<String, ImageIcon>();
	
	final public static String F_NORMAL = Messages.getString("Images.F_NORMAL"); //$NON-NLS-1$
	final public static String F_SWAMP = Messages.getString("Images.F_SWAMP"); //$NON-NLS-1$
	final public static String F_ROCK = Messages.getString("Images.F_ROCK"); //$NON-NLS-1$
	final public static String F_VOID = Messages.getString("Images.F_VOID"); //$NON-NLS-1$
	final public static String F_INVISIBLE = Messages
			.getString("Images.F_INVISIBLE"); //$NON-NLS-1$

	final public static String P_OWN = Messages.getString("Images.P_OWN"); //$NON-NLS-1$
	final public static String P_ALLY = Messages.getString("Images.P_ALLY"); //$NON-NLS-1$
	final public static String P_ENEMY = Messages.getString("Images.P_ENEMY"); //$NON-NLS-1$

	final public static String T_LOUKA = Messages.getString("Images.T_LOUKA"); //$NON-NLS-1$
	final public static String T_LES = Messages.getString("Images.T_LES"); //$NON-NLS-1$

	final public static String A_ARMY = Messages.getString("Images.A_ARMY"); //$NON-NLS-1$
	final public static String A_FRIENDARMY = Messages
			.getString("Images.A_FRIENDARMY"); //$NON-NLS-1$

	final public static String O_HQ = Messages.getString("Images.O_HQ"); //$NON-NLS-1$
	final public static String O_FLAG = Messages.getString("Images.O_FLAG"); //$NON-NLS-1$
	final public static String I_YELLOW = Messages
			.getString("Images.I_FAVICON"); //$NON-NLS-1$


	/**
	 * Vytvoří obrázek buď načtením ze souboru nebo z resource
	 * @param s Cesta k souboru nebo k resource
	 * @return Načtený obrázek nebo null, pokud načtení selhalo
	 */
	private static ImageIcon create(String s) {
		ImageIcon result = new ImageIcon(s);
		if (result == null
				|| result.getImageLoadStatus() != MediaTracker.COMPLETE) {
			result = getImage(s);
		}
		return result;
	}

	/**
	 * Získání obrázku asociovaného s daným řetězcem, pokud neexistuje, pokusí se ho vytvořit.
	 * @param s Řetězec - identifikátor obrázku
	 * @return Uložený nebo vytvořený obrázek
	 */
	public static ImageIcon get(String s) {
		ImageIcon result = map.get(s);
		if (result == null) {
			result = create(s);
			map.put(s, result);
		}
		return result;
	}

	/**
	 * Asociuje řetězec s obrázkem
	 * @param s Klíčový řetězec
	 * @param i Obrázek
	 */
	public static void put(String s, ImageIcon i) {
		map.put(s, i);
	}

	/**
	 * Načte obrázek z daného umístění a asociuje ho s cestou k umístění
	 * @param s Umístění obrázku
	 */
	public static void put(String s) {
		put(s, s);
	}

	/**
	 * Načte obrázek z daného umístění a asociuje ho s řetězcem
	 * @param s Klíčový řetězec
	 * @param p Umístění obrázku
	 */
	public static void put(String s, String p) {
		put(s, create(p));
	}

	/**
	 * Pokusí se načíst obrázek z resource
	 * @param pathName Identifikátor resource
	 * @return Obrázek nebo null, pokud resource neexistuje, nebo nelze načíst
	 */
	private static ImageIcon getImage(String pathName) {
		URL url = Images.class.getResource(pathName);
		if (url == null)
			return null;
		return new ImageIcon(url);
	}
}
