package iwr;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Třída pro převod písmenně-číselných souřadnic
 * @author Tomáš Kraut
 *
 */
public class Coords {
	/**
	 * X souřadnice (horizonvální) 
	 */
	private int x;
	/**
	 * Y souřadnice (vertikální)
	 */
	private int y;

	/**
	 * Vytvoří souřadnice (0,0)/A0
	 */
	public Coords() {
		x = y = 0;
	}

	/**
	 * Vytvoří souřadnice (X,Y)
	 * @param x X
	 * @param y Y
	 */
	public Coords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Vytvoří souřadnice (X,X)
	 * @param both X (==Y)
	 */
	public Coords(int both) {
		x = y = both;
	}

	/**
	 * Zkopíruje jiné souřadnice
	 * @param old Kopírované souřadnice
	 */
	public Coords(Coords old) {
		x = old.x;
		y = old.y;
	}

	/**
	 * Getter pro X
	 * @return X
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Setter pro X
	 * @param x X
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Getter pro Y
	 * @return Y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter pro Y
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ";" + y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Prevede souradnice ze stringu do dvojice intu
	 * 
	 * @param sCoords
	 *            vstupni retezec ve tvaru napr. "<b>A5</b>" (pismenna souradnice 1 ci
	 *            2 znakova)
	 */
	public Coords(String sCoords) {
		Matcher match = Pattern.compile(
				"([A-Z]+)(\\d+)", Pattern.CASE_INSENSITIVE).matcher(sCoords); //$NON-NLS-1$
		match.matches();
		MatchResult result = match.toMatchResult();
		String sx = result.group(1);
		String sy = result.group(2);
		if (sx.length() == 1) {
			this.x = Character.getNumericValue(sx.charAt(0)) - 10;
		} else { // delsi, nez 2 neresim
			this.x = (Character.getNumericValue(sx.charAt(0)) - 9) * 26
					+ Character.getNumericValue(sx.charAt(1)) - 10;
		}
		/*
		 * char[] ca = sx.toCharArray(); int base, first; if (ca.length > 1) {
		 * first = 1; base =
		 * 27*pow(26,ca.length-2)*(Character.getNumericValue(ca[0])- 9 ); } else
		 * { first = 0; base = 0; } String nsx = ""; for (int i = first; i <
		 * ca.length; ++i) { nsx +=
		 * Character.forDigit((Character.getNumericValue(ca[i])- 10 ), 26);
		 * //vrati znak o 10 mensi (kvuli vynechani 0-9) } this.x =
		 * base+Integer.parseInt(nsx, 26);
		 */
		this.y = Integer.parseInt(sy);
	}
	
	/**
	 * Pomocna funkce pro celočíselné umocňování
	 * @param x Základ
	 * @param a Exponent
	 * @return X^A
	 */
	public static int pow(int x, int a) {
		int res = 1;
		while (a-- > 0) {
			res *= x;
		}
		return res;
	}

}
