package iwr;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coords {
	int x, y; 
	
	public Coords() {
		x = y = 0;
	}
	
	public Coords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coords(int both) {
		x = y = both;
	}
	
	public Coords(Coords old) {
		x = old.x;
		y = old.y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ";" + y + ")";
	}
	
	/**
	 * Prevede souradnice ze stringu do dvojice intu
	 * @param sCoords vstupni retezec ve tvaru napr. "A5" (pismenna souradnice 1 ci 2 znakova)
	 */
	public Coords (String sCoords) {
		Matcher match = Pattern.compile("([A-Z]+)(\\d+)", Pattern.CASE_INSENSITIVE).matcher(sCoords);
		match.matches();
		MatchResult result = match.toMatchResult();
		String sx = result.group(1);
		String sy = result.group(2);
		if (sx.length() == 1) {
			this.x = Character.getNumericValue(sx.charAt(0)) - 10;
		} else { //delsi, nez 2 neresim
			this.x = (Character.getNumericValue(sx.charAt(0)) - 9) * 26 + Character.getNumericValue(sx.charAt(1)) - 10;
		}
		/*char[] ca = sx.toCharArray();
		int base, first;
		if (ca.length > 1) {
			first = 1;
			base =  27*pow(26,ca.length-2)*(Character.getNumericValue(ca[0])- 9 );
		} else {
			first = 0;
			base = 0;
		}
		String nsx = "";
		for (int i = first; i < ca.length; ++i) {
			nsx += Character.forDigit((Character.getNumericValue(ca[i])- 10 ), 26); //vrati znak o 10 mensi (kvuli vynechani 0-9)
		}
		this.x = base+Integer.parseInt(nsx, 26);
		*/
		this.y = Integer.parseInt(sy);
	}

	public static int pow(int a, int b) {
		int res = 1;
		while (b-- > 0) {
			res *= a;
		}
		return res;
	}
	
}
