package az.ibar.task.model;

import acm.graphics.GImage;
import acm.program.GraphicsProgram;
import az.ibar.task.enums.Direction;
import az.ibar.task.model.Cross;
import az.ibar.task.model.Road;

public class Traffic extends GraphicsProgram {

	private static final long serialVersionUID = 6981324926682754492L;
	private static final int APPLICATION_HEIGHT = 600;
	private static final int APPLICATION_WIDTH = 900;
	
	private Road verticalRoad = null;
	private Road horizontalRoad = null;

	public void initTraffic() {

		Cross cross = Cross.getInstance();

		verticalRoad = new Road(cross, Direction.UP_TO_DOWN, new GImage("vertical-road.jpg"),
				getWidth(), APPLICATION_HEIGHT / 4, (APPLICATION_WIDTH - APPLICATION_HEIGHT / 3) / 2, 0);

		horizontalRoad = new Road(cross, Direction.LEFT_TO_RIGHT, new GImage("horizontal-road.jpg"),
				getWidth(), APPLICATION_HEIGHT / 4, 0, APPLICATION_HEIGHT / 3);

		cross.setX(verticalRoad.getX());
		cross.setY(horizontalRoad.getY());
		cross.setWidth(verticalRoad.getWidth());
		cross.setHeight(horizontalRoad.getHeight());
		cross.setTraffic(this);
		
		add(verticalRoad, verticalRoad.getX(), verticalRoad.getY());
		add(horizontalRoad, horizontalRoad.getX(), horizontalRoad.getY());

		Thread t1 = new Thread(horizontalRoad);
		Thread t2 = new Thread(verticalRoad);
		t1.start();
		t2.start();
	}

	public void run() {
		initTraffic();
	}

	public Road getVerticalRoad() {
		return verticalRoad;
	}

	public void setVerticalRoad(Road verticalRoad) {
		this.verticalRoad = verticalRoad;
	}

	public Road getHorizontalRoad() {
		return horizontalRoad;
	}

	public void setHorizontalRoad(Road horizontalRoad) {
		this.horizontalRoad = horizontalRoad;
	}
}
