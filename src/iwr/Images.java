package iwr;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class Images {
	protected static Map<String, ImageIcon> map = new HashMap<String, ImageIcon>();
	final public static String F_NORMAL = "f_normální"; 
	final public static String F_SWAMP = "f_bažina"; 
	final public static String F_ROCK= "f_skála"; 
	final public static String F_VOID = "f_Nedobytné";
	final public static String F_INVISIBLE = "inv";
	
	final public static String P_OWN = "my";
	final public static String P_ALLY = "aly";
	final public static String P_ENEMY = "ene";
	
	final public static String T_LOUKA = "ut_1";
	final public static String T_LES = "ut_2";
	
	final public static String A_ARMY = "a";
	final public static String A_FRIENDARMY = "fa";

	final public static String O_HQ = "hq";
	final public static String O_FLAG = "flag";
	
	final public static String PREFIX = "img/";
	final public static String SUFFIX = ".png";
	final public static String GIF = ".gif";
	
	static {
		put(F_NORMAL);
		put(F_SWAMP);
		put(F_ROCK);
		put(F_VOID);
		putGif(F_INVISIBLE);

		put(P_OWN);
		put(P_ALLY);
		put(P_ENEMY);
		
		putGif(T_LES);
		putGif(T_LOUKA);
		
		putGif(A_ARMY);
		putGif(A_FRIENDARMY);
		
		putGif(O_HQ);
		putGif(O_FLAG);
		
		
	}
	public static ImageIcon get(String s) {
		return map.get(s);
	}
	public static void put(String s, ImageIcon i) {
		map.put(s, i);
	}
	public static void put(String s) {
		put(s, s);
	}
	public static void putGif(String s) {
		putGif(s, s);
	}
	public static void put(String s, String p) {
		put(s, p, SUFFIX);
	}
	public static void putGif(String s, String p) {
		put(s, p, GIF);
	}
	public static void put(String s, String p, String suf) {
		put(s, new ImageIcon(PREFIX+p+suf));
	}
}
