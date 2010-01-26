package iwr;

public interface TimeLine<L> {
	void changeLoadAt(L newLoad, int time);
	L loadAt(int time);
}
