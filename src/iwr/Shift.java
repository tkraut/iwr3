package iwr;

public enum Shift {
	MOVE(Messages.getString("Shift.moves")),KILL(Messages.getString("Shift.kills")); //$NON-NLS-1$ //$NON-NLS-2$
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


