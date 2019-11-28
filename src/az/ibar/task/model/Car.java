package az.ibar.task.model;

import acm.graphics.*;
import az.ibar.task.enums.Direction;

public class Car extends GCompound {
	
	private static final long serialVersionUID = -2052643104592916503L;
	
	private double carX;
	private double carY;
	private int width;
	private int height;
	private Direction directionEnum;
	private GImage image;
	
	public double getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public double getCarX() {
		return carX;
	}
	public void setCarX(double x) {
		this.carX = x;
	}
	public double getCarY() {
		return carY;
	}
	public void setCarY(double y) {
		this.carY = y;
	}
	public GImage getImage() {
		return image;
	}
	public void setImage(GImage image) {
		this.image = image;
		image.setSize(width, height);
		
		add(image);
	}
	public Direction getDirectionEnum() {
		return directionEnum;
	}
	public void setDirectionEnum(Direction directionEnum) {
		this.directionEnum = directionEnum;
	}
}
