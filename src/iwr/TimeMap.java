package iwr;

import java.util.NavigableMap;
import java.util.TreeMap;


/**
 * Implementace časové řady pomocí TreeMap
 * @author Tomáš Kraut
 *
 * @param <L> Náklad
 * 
 * @see TreeMap
 */
public class TimeMap<L> implements TimeLine<L>{
	/**
	 * Mapa k ukládání nákladu
	 */
	private NavigableMap<Integer, L> map;

	/**
	 * Vytvoří mapu a vložení iniciální náklad v čase 0
	 * @param initial
	 */
	public TimeMap(L initial) {
		map = new TreeMap<Integer, L>();
		map.put(0, initial);
	}

	@Override
	public void changeLoadAt(L newLoad, int time) {
		map.put(time, newLoad);
	}

	@Override
	public L loadAt(int time) {
		return map.floorEntry(time).getValue();
	}

}
