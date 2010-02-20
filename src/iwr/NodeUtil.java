package iwr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Node;

public class NodeUtil {
	static DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), time = new SimpleDateFormat("HH:mm:ss");
	static int getInt(Node node) {
		return Integer.parseInt(node.getFirstChild().getTextContent());
	}
	
	static int getMaybeInt(Node node) {
		try {
			return Integer.parseInt(node.getFirstChild().getTextContent());
		} catch (Exception e) {
			return 0;
		}
	}
	
	static boolean getBool(Node node) {
		return node.getFirstChild().getTextContent().equals("1");
	}
	
	static String getString(Node node) {
		return node.getFirstChild().getTextContent();
	}
	
	static Date getDate(Node node) {
		try {
			return date.parse(node.getFirstChild().getTextContent());
		} catch (Exception e) {
			return null;
		}
	}
	
	static Date getTime(Node node) {
		try {
			return time.parse(node.getFirstChild().getTextContent());
			 
		} catch (Exception e) {
			return null;
		}
	}
	static public double getDouble(Node node) {
		return Double.parseDouble(node.getFirstChild().getTextContent());
	}
	
}
