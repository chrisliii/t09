package ca.mcgill.ecse321.rideshare9.entity.helper;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class AdvQuery {
	private String stop; 
	private String startLocation;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTimeX; 
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTimeY;
	private String vColor;
	private String vModel;
	// true: price, false: time
	private boolean sortByPrice; 
	
	public boolean isSortByPrice() {
		return sortByPrice;
	}
	public void setSortByPrice(boolean sortByPrice) {
		this.sortByPrice = sortByPrice;
	}
	public String getvColor() {
		return vColor;
	}
	public void setvColor(String vColor) {
		this.vColor = vColor;
	}
	public String getvModel() {
		return vModel;
	}
	public void setvModel(String vModel) {
		this.vModel = vModel;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public String getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}
	public Date getStartTimeX() {
		return startTimeX;
	}
	public void setStartTimeX(Date startTimeX) {
		this.startTimeX = startTimeX;
	}
	public Date getStartTimeY() {
		return startTimeY;
	}
	public void setStartTimeY(Date startTimeY) {
		this.startTimeY = startTimeY;
	}
	
}
