package po;

import java.util.ArrayList;

public class Dish {
	private ArrayList<String> dishid;
	private ArrayList<String> name;
	private ArrayList<String> dishpicture;
	private ArrayList<Integer> price;
	private ArrayList<String> description;
	private ArrayList<Integer> sales;
	private ArrayList<String> type;
	private ArrayList<String> make_time;
	private ArrayList<Integer> surplus;

	public Dish() {

	}

	public Dish(ArrayList<String> dishid, ArrayList<String> name, ArrayList<String> dishpicture,
			ArrayList<Integer> price, ArrayList<String> description, ArrayList<Integer> sales, ArrayList<String> type,
			ArrayList<String> make_time, ArrayList<Integer> surplus) {
		super();
		this.dishid = dishid;
		this.name = name;
		this.dishpicture = dishpicture;
		this.price = price;
		this.description = description;
		this.sales = sales;
		this.type = type;
		this.make_time = make_time;
		this.surplus = surplus;
	}

	public ArrayList<String> getDishid() {
		return dishid;
	}

	public void setDishid(ArrayList<String> dishid) {
		this.dishid = dishid;
	}

	public ArrayList<String> getName() {
		return name;
	}

	public void setName(ArrayList<String> name) {
		this.name = name;
	}

	public ArrayList<String> getDishpicture() {
		return dishpicture;
	}

	public void setDishpicture(ArrayList<String> dishpicture) {
		this.dishpicture = dishpicture;
	}

	public ArrayList<Integer> getPrice() {
		return price;
	}

	public void setPrice(ArrayList<Integer> price) {
		this.price = price;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}

	public ArrayList<Integer> getSales() {
		return sales;
	}

	public void setSales(ArrayList<Integer> sales) {
		this.sales = sales;
	}

	public ArrayList<String> getType() {
		return type;
	}

	public void setType(ArrayList<String> type) {
		this.type = type;
	}

	public ArrayList<String> getMake_time() {
		return make_time;
	}

	public void setMake_time(ArrayList<String> make_time) {
		this.make_time = make_time;
	}

	public ArrayList<Integer> getSurplus() {
		return surplus;
	}

	public void setSurplus(ArrayList<Integer> surplus) {
		this.surplus = surplus;
	}
}
