package az.ibar.task.enums;

public enum Flag {
	
	EMPTY(0), HORIZONTAL(1), VERTICAL(2);
	
	private int flag;
	
	private Flag(int flag) {
		this.flag = flag;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
