package form;

import java.util.ArrayList;
import java.util.List;

public class DateForm {
	String year,day;
	int month;
	
	List<String> hours,minutes;
	List<Integer> nbrParticipant;

	public DateForm(int y,int m,int d){
		year = String.valueOf(y); month = m; day = String.valueOf(d); hours = new ArrayList() ;minutes = new ArrayList()  ; nbrParticipant = new ArrayList(); 
	}
	
	public int getnbrParticipantSize() {
		return nbrParticipant.size();
	}
	public int getnbrParticipant(int pos) {
		return nbrParticipant.get(pos);
	}
	public void addnbrParticipant(int val) {
		this.nbrParticipant.add(val);
	}
	
	public int getHoursSize() {
		return hours.size();
	}
	public String getHours(int pos) {
		return hours.get(pos);
	}
	public void addHours(int val) {
		this.hours.add(String.valueOf(val));
	}
	
	public int getMinutesSize() {
		return minutes.size();
	}
	public String getMinutes(int pos) {
		return minutes.get(pos);
	}
	public void addMinutes(int val) {
		this.minutes.add(String.valueOf(val));
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
}
