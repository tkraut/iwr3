package iwr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class NodeUtil {

	static int getInt(Node node) {
		return Integer.parseInt(node.getFirstChild().getTextContent());
	}
	
	static boolean getBool(Node node) {
		return node.getFirstChild().getTextContent().equals("1");
	}
	
	static String getString(Node node) {
		return node.getFirstChild().getTextContent();
	}
	
	static Date getDate(Node node) {
		try {
			return new SimpleDateFormat().parse(node.getFirstChild().getTextContent());
		} catch (Exception e) {
			return null;
		}
	}
	static public double getDouble(Node node) {
		return Double.parseDouble(node.getFirstChild().getTextContent());
	}
	
}
