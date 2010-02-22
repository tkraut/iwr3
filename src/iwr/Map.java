package iwr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;

/**
 * Mapa světa
 * @author Tomáš Kraut
 *
 */
public class Map {
	/**
	 * Šířka světa
	 */
	int width;
	/**
	 * Výška světa
	 */
	int height;
	/**
	 * Seznam polí světa
	 */
	List<Field> fields;

	/**
	 * Načtení z XML uzlu &lt;map&gt;
	 * 
	 * @param mapNode XML uzel
	 * @param width Šířka
	 * @param height Výška
	 * @param unitTypes Typy jednotek
	 * @param fieldTypes Typy polí
	 */
	public Map(Node mapNode, int width, int height,
			java.util.Map<Integer, FieldType> fieldTypes,
			java.util.Map<Integer, UnitType> unitTypes) {
		this.width = width;
		this.height = height;
		fields = new ArrayList<Field>();
		int i = 0;
		for (Node fieldNode = mapNode.getFirstChild(); fieldNode != null; fieldNode = fieldNode
				.getNextSibling()) {
			if (fieldNode.getNodeName().equals("f")) {
				int x = getX(i);
				int y = getY(i++);
				fields.add(new Field(fieldNode, fieldTypes, unitTypes, this, x,
						y));
			}
		}
	}
	/**
	 * Získá pole na zadaných souřadnicích
	 * @param c Souřadnice
	 * @return Získané pole
	 */
	public Field fieldAt(Coords c) {
		return fieldAt(c.getX(), c.getY());
	}

	/**
	 * Získá pole na zadaných souřadnicích
	 * @param x X souřadnice
	 * @param y Y souřadnice
	 * @return Získané pole
	 */
	public Field fieldAt(int x, int y) {
		return fields.get(y * width + x);
	}

	/**
	 * Prevede souradnice na odpovidajici retezec
	 * 
	 * @param x
	 *            souradnice sirky (max 701)
	 * @param y
	 *            vyskova souradnice
	 * @return retezec reprezentujici dane souradnice
	 */
	public static String getSCoords(int x, int y) {
		String sx = ""; //$NON-NLS-1$
		if (x > 25) {
			sx += (char) ('A' + x / 26 - 1);
		}
		sx += (char) ('A' + x % 26);
		return sx + y;
	}

	/**
	 * Získá X souřadnici k pořadí pole
	 * @param ord Pořadí pole
	 * @return Jeho X souřadnice
	 */
	public int getX(int ord) {
		return ord % width;
	}

	/**
	 * Získá Y souřadnici k pořadí pole
	 * @param ord Pořadí pole
	 * @return Jeho Y souřadnice
	 */
	public int getY(int ord) {
		return ord / width;
	}

	/**
	 * Získá písmenně-číselnou souřadnici k pořadí pole
	 * @param ord Pořadí pole
	 * @return Řetězec, určující pole
	 */
	public String getSCoords(int ord) {
		return getSCoords(getX(ord), getY(ord));
	}

	/**
	 * Získá pole, určené písmenně-číselnými souřadnicemi
	 * @param sCoords SOuřadnice
	 * @return Získané pole
	 */
	public Field fieldAt(String sCoords) {
		return fieldAt(new Coords(sCoords));
	}

	/**
	 * Získá množinu všech polí
	 * @return Množina polí hry
	 */
	public Set<Field> exportFieldsAsSet() {
		return new HashSet<Field>(fields);
	}

	/**
	 * Získá množinu polí která jsou blízko k zadanému umístění 
	 * @param center Umístění
	 * @param size Maximání vzdálenost od umístění
	 * @return Množina blízkých polí
	 */
	public Set<Field> squareOfFields(Coords center, int size) {
		Set<Field> fields = new HashSet<Field>();
		for (int i = Math.max(0, center.getX() - size); i <= Math.min(width - 1,
				center.getX() + size); ++i) {
			for (int j = Math.max(0, center.getY() - size); j <= Math.min(
					height - 1, center.getY() + size); ++j) {
				fields.add(fieldAt(i, j));
			}
		}
		return fields;
	}

	/**
	 * Získá množinu polí která jsou blízko k zadanému poli 
	 * @param center Pole
	 * @param size Maximání vzdálenost od pole
	 * @return Množina blízkých polí
	 */

	public Set<Field> squareOfFields(Field center, int size) {
		return squareOfFields(center.getCoords(), size);
	}

}
