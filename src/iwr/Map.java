package iwr;

import java.util.ArrayList;
import java.util.List;
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
			if (fieldNode.getNodeName().equals("f"))
				fields.add(new Field(fieldNode, fieldTypes, unitTypes, getCoords(i++)));
		}
	}
	public Field fieldAt(int x, int y) {
		return fields.get(y*width+x);
	}
	
	public String getCoords(int x, int y) {
		return Character.toString((char) ('A'+x))+""+y;
	}
	
	public String getCoords(int ord) {
		return getCoords(ord%width, ord/width);
	}
	public Field fieldAt(String coords) {
		int x, y;
		y = Integer.parseInt(coords.substring(1));
		x = Character.getNumericValue(coords.charAt(0))-Character.getNumericValue('a');
		return fieldAt(x, y);
	}
	
}
