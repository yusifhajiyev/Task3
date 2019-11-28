package az.ibar.task.model;

import az.ibar.task.enums.Direction;
import az.ibar.task.enums.Flag;
import az.ibar.task.model.Traffic;

public class Cross {
	private double x;
	private double y;
	private double width;
	private double height;
	private Flag flag = Flag.EMPTY;
	private static Cross cross = new Cross();
	private Traffic traffic;

	private Cross() {
		
	}
	
	public static Cross getInstance() {
		return cross;
	}
	
	public void refreshRoad(Direction directionEnum) {
		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			traffic.remove(traffic.getHorizontalRoad());
			traffic.add(traffic.getHorizontalRoad(), traffic.getHorizontalRoad().getX(), traffic.getHorizontalRoad().getY());
		} else {
			traffic.remove(traffic.getVerticalRoad());
			traffic.add(traffic.getVerticalRoad(), traffic.getVerticalRoad().getX(), traffic.getVerticalRoad().getY());
		}
	}

	public Flag getFlag() {
		return flag;
	}

	public synchronized boolean setFlag(Flag flag) {
		if (flag != Flag.EMPTY) {
			if (this.flag == Flag.EMPTY) {
				this.flag = flag;

				return true;
			} else {
				return false;
			}
		} else {
			this.flag = flag;
			
			return true;
		}
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Traffic getTraffic() {
		return traffic;
	}

	public void setTraffic(Traffic traffic) {
		this.traffic = traffic;
	}
}
