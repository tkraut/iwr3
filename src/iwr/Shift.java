package iwr;

public enum Shift {
	MOVE("tahy"),KILL("vyřazení");
	Shift(String value) {
		this.value = value;
	}
	private final String value;
	public String value() { return value; }
	@Override
	public String toString() {
		return value;
	}
	
}


