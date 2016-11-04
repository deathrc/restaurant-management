package com.example.restaurant.po;

public class StaffItem {
	private String imageUrl;  
    private String eid;
    private String name;
    private String sex;
    private int age;
    private float point;
    
    public StaffItem(String imageUrl, String eid,String name,String sex,int age,float point ) {  
        this.imageUrl = imageUrl;  
        this.eid=eid;
        this.name=name;
        this.sex=sex;
        this.age=age;
        this.point=point;
    }  
    
	public String getImageUrl() {
		return imageUrl;
	}
	public String getEid() {
		return eid;
	}
	public String getName() {
		return name;
	}
	public String getSex() {
		return sex;
	}
	public int getAge() {
		return age;
	}
	public float getPoint() {
		return point;
	}
    
}
