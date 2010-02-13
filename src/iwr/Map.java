package iwr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.w3c.dom.Node;

public class Map {
	int width;
	int height;
	List<Field> fields;
	/**
	 * Načtení z XML uzlu <map>
	 * @param mapNode
	 * @param unitTypes 
	 */
	public Map(Node mapNode, int w, int h, TreeMap<Integer, FieldType> fieldTypes, java.util.Map<Integer, Unit> unitTypes) {
		width = w;
		height = h;
		fields = new ArrayList<Field>();
		int i=0;
		for (Node fieldNode = mapNode.getFirstChild(); fieldNode != null; fieldNode = fieldNode.getNextSibling()) {
			if (fieldNode.getNodeName().equals("f")) {
				int x = getX(i);
				int y = getY(i++);
				fields.add(new Field(fieldNode, fieldTypes, unitTypes, this, x, y));
			}
		}
	}
	
	public Field fieldAt(Coords c) {
		return fieldAt(c.getX(), c.getY());
	}
	
	public Field fieldAt(int x, int y) {
		return fields.get(y*width+x);
	}
	
	/**
	 * Prevede souradnice na odpovidajici retezec
	 * @param x souradnice sirky (max 701)
	 * @param y vyskova souradnice
	 * @return retezec reprezentujici dane souradnice
	 */
	public static String getSCoords(int x, int y) {
		String sx = "";
		if (x>25) {
			sx += (char) ('A'+x/26-1);
		}
		sx += (char) ('A'+x%26);
		return sx + y;
	}
	
	public int getX(int ord) {
		return ord%width;
	}
	
	public int getY(int ord) {
		return ord/width;
	}
	
	public String getSCoords(int ord) {
		return getSCoords(getX(ord), getY(ord));
	}
	
	
	public Field fieldAt(String sCoords) {
		return fieldAt(new Coords(sCoords));
	}
	
	public Set<Field> exportFieldsAsSet() {
		return new HashSet<Field>(fields);
	}
	
	public Set<Field> squareOfFields(Coords center, int size) {
		Set<Field> fields = new HashSet<Field>();
		for (int i = Math.max(0, center.x - size); i <= Math.min(width-1, center.x + size); ++i) {
			for (int j = Math.max(0, center.y - size); j <= Math.min(height-1, center.y + size); ++j) {
				fields.add(fieldAt(i, j));
			}
		}
		return fields;
	}
	
	public Set<Field> squareOfFields(Field center, int size) {
		return squareOfFields(center.getCoords(), size);
	}
	
}
