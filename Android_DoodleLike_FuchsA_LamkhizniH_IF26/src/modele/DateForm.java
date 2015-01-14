package modele;

import java.util.ArrayList;
import java.util.List;

public class DateForm {
	long id, id_rdv;
	String year,month,day;
	String invite;
	List<HeureForm> heureForm;

	
	public DateForm(int y,int m,int d){
		
		year = String.valueOf(y); month = String.valueOf(m); day = String.valueOf(d); heureForm =new ArrayList<HeureForm>();
	}
	public DateForm(long id2, int y,int m,int d){
		id_rdv = id2; year = String.valueOf(y); month = String.valueOf(m); day = String.valueOf(d);  heureForm =new ArrayList<HeureForm>();
	}
	public DateForm(){
		year = null; month = null; day = null;  heureForm =new ArrayList<HeureForm>();
	}
	
	
	public String getSqlDate(){
		return year+"-"+(Integer.parseInt(month)+1)+"-"+day;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long l) {
		this.id = l;
	}
	public long getId_rdv() {
		return id_rdv;
	}
	public void setId_rdv(long id_rdv) {
		this.id_rdv = id_rdv;
	}
	
	public void addHeureForm(String heure, String min){
		heureForm.add(new HeureForm(heure,min));
	}
	public void addHeureForm(HeureForm val){
		heureForm.add(val);
	}
	public void addHeureForm(String heure, String min, String partic){
		heureForm.add(new HeureForm(heure,min,partic));
	}
	public HeureForm getHeureForm(int pos) {
		return heureForm.get(pos);
	}
	public void setHeureForm(int pos,HeureForm heureForm) {
		this.heureForm.set(pos, heureForm) ;
	}
	
	public List<HeureForm> getHeuresForm() {
		return heureForm;
	}
	public void setHeuresForm(List<HeureForm> heureForm) {
		this.heureForm = heureForm;
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
