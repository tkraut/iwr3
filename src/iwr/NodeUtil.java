package iwr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Node;

/**
 * Pomocná třída pro načítání z XML uzlů
 * @author Tomáš Kraut
 *
 */
public class NodeUtil {
	/**
	 * Parser pro datum
	 */
	public static final DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * Parser pro čas
	 */
	public static final DateFormat time = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Načte integer z prvního potomka uzlu
	 * @param node XML uzel
	 * @return načtený integer
	 */
	static int getInt(Node node) {
		return Integer.parseInt(node.getFirstChild().getTextContent());
	}

	/**
	 * Načte integer z prvního potomka uzlu nebo 0 pokud nelze načíst
	 * @param node XML uzel
	 * @return načtený integer
	 */
	static int getMaybeInt(Node node) {
		try {
			return Integer.parseInt(node.getFirstChild().getTextContent());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Načte booleovskou hodnotu z uzlu
	 * @param node XML uzel
	 * @return true, pokud uzel obsahuje "1", jinak false
	 */
	static boolean getBool(Node node) {
		return node.getFirstChild().getTextContent().equals("1");
	}

	/**
	 * Načte řetězec z uzlu
	 * @param node XML uzel
	 * @return načtený řetězec
	 */
	static String getString(Node node) {
		return node.getFirstChild().getTextContent();
	}

	/**
	 * Načte datum a čas z uzlu
	 * @param node XML uzel
	 * @return načtené datum nebo null v případě chyby
	 */
	static Date getDate(Node node) {
		try {
			return date.parse(node.getFirstChild().getTextContent());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Načte čas z uzlu
	 * @param node XML uzel
	 * @return načtený čas nebo null v případě chyby
	 */
	static Date getTime(Node node) {
		try {
			return time.parse(node.getFirstChild().getTextContent());

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * načte double z uzlu
	 * @param node XML uzel
	 * @return načtený double
	 */
	static public double getDouble(Node node) {
		return Double.parseDouble(node.getFirstChild().getTextContent());
	}

}
