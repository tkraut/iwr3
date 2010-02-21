package iwr;

import java.util.NavigableMap;
import java.util.TreeMap;

public class TimeMap<L> implements TimeLine<L>{
	NavigableMap<Integer, L> map;

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
