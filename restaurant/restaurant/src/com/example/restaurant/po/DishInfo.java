package com.example.restaurant.po;

public class DishInfo {

	String dishId;
	String name;
	String dishPicture;
	Double price;
	String description;
	int sales;
	String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	int make_time;
	int surplus;
	public String getDishId() {
		return dishId;
	}
	public void setDishId(String dishId) {
		this.dishId = dishId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDishPicture() {
		return dishPicture;
	}
	public void setDishPicture(String dishPicture) {
		this.dishPicture = dishPicture;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
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
	public int getMake_time() {
		return make_time;
	}
	public void setMake_time(int make_time) {
		this.make_time = make_time;
	}
	public int getSurplus() {
		return surplus;
	}
	public void setSurplus(int surplus) {
		this.surplus = surplus;
	}
	
}
