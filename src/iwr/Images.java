package iwr;

import java.awt.MediaTracker;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class Images {
	protected static Map<String, ImageIcon> map = new HashMap<String, ImageIcon>();
	final public static String F_NORMAL = "f_1.png"; 
	final public static String F_SWAMP = "f_2.png"; 
	final public static String F_ROCK= "f_3.png"; 
	final public static String F_VOID = "f_4.png";
	final public static String F_INVISIBLE = "inv.gif";
	
	final public static String P_OWN = "my.png";
	final public static String P_ALLY = "aly.png";
	final public static String P_ENEMY = "ene.png";
	
	final public static String T_LOUKA = "ut_1.gif";
	final public static String T_LES = "ut_2.gif";
	
	final public static String A_ARMY = "a.gif";
	final public static String A_FRIENDARMY = "fa.gif";

	final public static String O_HQ = "hq.gif";
	final public static String O_FLAG = "flag.gif";
	
	
	static {
		
		/* lazy nacitani obrazku
		put(F_NORMAL);
		put(F_SWAMP);
		put(F_ROCK);
		put(F_VOID);
		put(F_INVISIBLE);

		put(P_OWN);
		put(P_ALLY);
		put(P_ENEMY);
		
		put(T_LES);
		put(T_LOUKA);
		
		put(A_ARMY);
		put(A_FRIENDARMY);
		
		put(O_HQ);
		put(O_FLAG);
		*/
	}
	
	protected static ImageIcon create(String s) {
		ImageIcon result = new ImageIcon(s);
		if (result == null || result.getImageLoadStatus() != MediaTracker.COMPLETE) {
			result = getImage(s);
		}
		return result;
	}
	
	public static ImageIcon get(String s) {
		ImageIcon result = map.get(s);
		if (result == null) {
			result = create(s);
			map.put(s, result);
		} 
		return result;
	}
	public static void put(String s, ImageIcon i) {
		map.put(s, i);
	}
	public static void put(String s) {
		put(s, s);
	}
	public static void put(String s, String p) {
		put(s, create(p));
	}
	private static ImageIcon getImage(String pathName)
	{
		URL url = Images.class.getResource(pathName);
		if (url == null) return null;
		return new ImageIcon(url);
	}
}
