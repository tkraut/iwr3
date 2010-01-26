package iwr;

import java.util.ArrayList;

public class TimeArray<L> implements TimeLine<L>{
	class Container {
		public L load;
		public int time;
		public Container(L l, int t) {
			load = l;
			time = t;
		}
	}
	
	ArrayList<Container> list;
	public TimeArray(L initial) {
		list = new ArrayList<Container>();
		list.add(new Container(initial, 0));
	}
	public void changeLoadAt(L newLoad, int time) {
		list.add(new Container(newLoad, time));
	}

	public L loadAt(int time) {
		int i = 0;
		while (i < list.size() && list.get(i).time <= time) ++i; //dokud jsou nejake zmeny, kontroluje, jestli nenastaly prilis pozde
		if (i>0) return list.get(i-1).load;
		return list.get(0).load;
	}

}
