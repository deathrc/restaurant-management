package com.example.restaurant.po;

public class Dish {
	private String dishid;
	private String name;
	private double price;
	private String description;
	private int sales;
	private String type;
	private String make_time;
	private int surplus;
	private String dishpicture;

	public String getDishid() {
		return dishid;
	}

	public void setDishid(String dishid) {
		this.dishid = dishid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMake_time() {
		return make_time;
	}

	public void setMake_time(String make_time) {
		this.make_time = make_time;
	}

	public int getSurplus() {
		return surplus;
	}

	public void setSurplus(int surplus) {
		this.surplus = surplus;
	}

	public String getDishpicture() {
		return dishpicture;
	}

	public void setDishpicture(String dishpicture) {
		this.dishpicture = dishpicture;
	}

}
