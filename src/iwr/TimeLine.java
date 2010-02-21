package iwr;

/**
 * 
 * @author Tomáš Kraut
 *
 * @param <L> 
 */
public interface TimeLine<L> {
	void changeLoadAt(L newLoad, int time);
	L loadAt(int time);
}
