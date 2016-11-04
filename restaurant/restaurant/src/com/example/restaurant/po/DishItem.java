package com.example.restaurant.po;

public class DishItem {
	private String imageUrl;  
    private String dishid;
    private String name;
    private int sales;
    private float price;
    public DishItem(String imageUrl, String dishid,String name,int sales,float price ) {  
        this.imageUrl = imageUrl;  
        this.dishid=dishid;
        this.name=name;
        this.sales=sales;
        this.price=price;
        
    }
	public String getImageUrl() {
		return imageUrl;
	}
	public String getDishid() {
		return dishid;
	}
	public String getName() {
		return name;
	}
	public int getSales() {
		return sales;
	}
	public float getPrice() {
		return price;
	}  
    
}
