package az.ibar.task.model;

import java.util.LinkedList;

import acm.graphics.GCompound;
import acm.graphics.GImage;
import az.ibar.task.enums.Direction;
import az.ibar.task.enums.Flag;

public class Road extends GCompound implements Runnable {
	
	private static final long serialVersionUID = -1414291580681710383L;

	private static final double MOVE_STEP = 0.0001;

	private double width;
	private double height;
	private GImage image;
	private double x;
	private double y;
	private Direction directionEnum;
	private boolean flagRemoved = false;
	private boolean roadRefreshed = false;
	private Cross cross;
	private LinkedList<Car> cars = new LinkedList<>();

	public Road(Cross cross, Direction directionEnum, GImage image, double width, double height, double x,
			double y) {
		
		this.cross = cross;
		this.image = image;
		this.directionEnum = directionEnum;
		this.x = x;
		this.y = y;

		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			this.image.setSize(width, height);
			this.width = width;
			this.height = height;
		} else {
			this.image.setSize(height, width);
			this.width = height;
			this.height = width;
		}

		add(image);
	}

	private boolean isCarCrossing(Car car) {
		
		if (car == null) {
			return false;
		}

		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			if (car.getX() + car.getWidth() > cross.getX() && car.getX() < cross.getX() + cross.getWidth()) {
				return true;
			}
		} else {
			if (car.getY() + car.getHeight() > cross.getY() && car.getY() < cross.getY() + cross.getHeight()) {
				return true;
			}
		}

		return false;
	}

	private boolean isCarCrossed(Car car) {
		if (car == null) {
			return false;
		}

		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			if (car.getX() >= cross.getX() + cross.getWidth()) {
				return true;
			}
		} else {
			if (car.getY() >= cross.getY() + cross.getHeight()) {
				return true;
			}
		}

		return false;
	}

	private boolean isCrossEmpty() {
		for (Car car : cars) {
			if (isCarCrossing(car)) {
				return false;
			}
		}

		return true;
	}

	private void stopRoad() {
		synchronized (cross) {
			try {
				roadRefreshed = false;
				
				cross.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			if (roadMustStop()) {
				runCrossedFirstCar();
				stopRoad();
			}

			if (!isCrossEmpty()) {
				if (!roadRefreshed) {
					refreshRoad();
				}
			} else {
				if (isCarCrossed(getFirstCar())) {
					if (!flagRemoved) {
						removeFlag();
					}
				}
			}

			moveCars();

			if (carCanBeDeleted(getFirstCar())) {
				removeCar(getFirstCar());
			}

			if (canNewCarBeAdded()) {
				addNewCar();
			}

		}
	}

	private void removeFlag() {
		synchronized (cross) {
			cross.notifyAll();

			cross.setFlag(Flag.EMPTY);

			flagRemoved = true;
		}
	}

	private void moveCars() {
		if (cars.size() > 0) {

			boolean canCross = false;

			for (Car car : cars) {
				if (carWillCrossOnNextStep(car)) {
					if (this.directionEnum == Direction.UP_TO_DOWN) {
						canCross = cross.setFlag(Flag.VERTICAL);
					} else {
						canCross = cross.setFlag(Flag.HORIZONTAL);
					}

					if (!canCross) {
						runCrossedFirstCar();
						stopRoad();
					} else {
						flagRemoved = false;
					}
				}
				
				moveCar(car);
			}
		}
	}
	
	private void moveCar(Car car) {
		if (directionEnum == Direction.UP_TO_DOWN) {
			car.setCarY(car.getY() + getMoveStep(car));
			car.move(0, getMoveStep(car));
		} else {
			car.setCarX(car.getX() + getMoveStep(car));
			car.move(getMoveStep(car), 0);
		}
	}

	private double getMoveStep(Car car) {
		if (carMayStopOnNextStep(car)) {
			return getSpecificMoveStep(car);
		} else {
			return MOVE_STEP;
		}
	}

	private boolean carMayStopOnNextStep(Car car) {
		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			if (car.getX() + car.getWidth() < cross.getX() && car.getX() + car.getWidth() + MOVE_STEP > cross.getX()) {
				return true;
			}
		} else {
			if (car.getY() + car.getHeight() < cross.getY()
					&& car.getY() + car.getHeight() + MOVE_STEP > cross.getY()) {
				return true;
			}
		}

		return false;
	}

	private boolean carWillCrossOnNextStep(Car car) {
		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			if (car.getX() + car.getWidth() == cross.getX()) {
				return true;
			}
		} else {
			if (car.getY() + car.getHeight() == cross.getY()) {
				return true;
			}
		}

		return false;
	}

	private double getSpecificMoveStep(Car car) {
		double specificStep = 0;

		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			specificStep = cross.getX() - (car.getX() + car.getWidth());
		} else {
			specificStep = cross.getY() - (car.getY() + car.getHeight());
		}

		return specificStep;
	}

	private void addNewCar() {
		GImage image = null;
		double carX = 0;
		double carY = 0;
		int carWidth = 0;
		int carHeight = 0;

		Car car = new Car();
		car.setDirectionEnum(directionEnum);

		if (directionEnum == Direction.UP_TO_DOWN) {
			image = new GImage("vertical-car.png");
			carX = width / 5;
			carWidth = 30;
			carHeight = 60;
		} else {
			image = new GImage("horizontal-car.png");
			carY = height / 5;
			carWidth = 60;
			carHeight = 30;
		}

		car.setCarX(carX);
		car.setCarY(carY);
		car.setWidth(carWidth);
		car.setHeight(carHeight);
		car.setImage(image);

		cars.addLast(car);

		add(car, carX, carY);
	}

	private void removeCar(Car car) {
		remove(car);
		cars.remove(car);
	}

	private Car getFirstCar() {
		if (cars.size() == 0) {
			return null;
		}

		return cars.getFirst();
	}

	private boolean roadMustStop() {
		if (this.directionEnum == Direction.UP_TO_DOWN) {
			if (cross.getFlag() == Flag.HORIZONTAL) {
				if (getNotCrossedFirstCar() != null
						&& getNotCrossedFirstCar().getY() + getNotCrossedFirstCar().getHeight() >= cross.getY()) {
					return true;
				}
			}
		} else {
			if (cross.getFlag() == Flag.VERTICAL) {
				if (getNotCrossedFirstCar() != null
						&& getNotCrossedFirstCar().getX() + getNotCrossedFirstCar().getWidth() >= cross.getX()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean carCanBeDeleted(Car car) {
		if (car == null) {
			return false;
		}

		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			if (car.getX() + car.getWidth() > width) {
				return true;
			}
		} else {
			if (car.getY() + car.getHeight() > height) {
				return true;
			}
		}

		return false;
	}

	private Car getLastCar() {
		if (cars.size() == 0) {
			return null;
		}

		return cars.getLast();
	}

	private Car getNotCrossedFirstCar() {
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);

			if (!isCarCrossed(car) && !isCarCrossing(car)) {
				return car;
			}
		}

		return null;
	}
	
	private void runCrossedFirstCar() {
		if (isCarCrossed(getFirstCar())) {
			
			Car car = getFirstCar();
			cars.remove(getFirstCar());
			
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						moveCar(car);
						
						if (carCanBeDeleted(car)) {
							remove(car);
							Thread.currentThread().interrupt();
							break;
						}
						
					}
				}
			});
			thread.start();
		}
	}

	private void refreshRoad() {
		cross.refreshRoad(directionEnum);
		
		roadRefreshed = true;
	}

	private boolean canNewCarBeAdded() {
		if (cars.size() == 0) {
			return true;
		}

		if (directionEnum == Direction.LEFT_TO_RIGHT) {
			if (getLastCar().getX() > getLastCar().getWidth() * 4) {
				return true;
			}
		} else {
			if (getLastCar().getY() > getLastCar().getHeight() * 4) {
				return true;
			}
		}

		return false;
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

	public Direction getDirectionEnum() {
		return directionEnum;
	}

	public void setDirectionEnum(Direction directionEnum) {
		this.directionEnum = directionEnum;
	}
}
