package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

public class DateForm {
	String year,month,day;
	DateForm(int y,int m,int d){
		year = String.valueOf(y); month = String.valueOf(m); day = String.valueOf(d);
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
}
