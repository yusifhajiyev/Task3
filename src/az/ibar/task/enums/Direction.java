package az.ibar.task.enums;

public enum Direction {

	LEFT_TO_RIGHT(1), UP_TO_DOWN(2);

	private int direction;

	private Direction(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
