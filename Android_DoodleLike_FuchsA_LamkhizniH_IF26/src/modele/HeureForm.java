package modele;

public class HeureForm {
	Long id,idDate;
	int nbrInvite;
	String heure, minute, invite;
	public HeureForm(){
		heure="";minute="";invite="";
	}
	public HeureForm(HeureForm val){
		this.setId(val.getId()); 
		this.setIdDate(val.getIdDate()); 

		this.setHeure(val.getHeure()); 
		this.setMinute(val.getMinute()); 
		this.setInviteChoix(val.getInvite()); 

	}
	
	public HeureForm(String h, String min){
		heure=h;minute=min;invite="";
	}
	
	public HeureForm(String h, String min,String invit){
		heure=h;minute=min;invite=invit;
	}
	
	public HeureForm(Long id1, Long id2, String h, String min,String invit){
		id = id1; idDate = id2; heure=h ;minute=min ;invite=invit;
	}
	
	
	public String getSqlHeure(){
		return ""+heure+":"+minute+":00";
	}
	
	public String getInvite() {
		return invite;
	}
	public void changeInscription(String user){
		if(isRegister(user))
			removeInvite(user);
		else
			addInvite(user);
	}
	public String getTtailleInvite() {
		String[] parts = invite.split("- ");
		return Integer.toString( parts.length-1);
	}
	public boolean isRegister(String user){
		if(this.invite.contains("- "+user))
			return true;
		else return false;
	}
	
	public void addInvite(String user) {
		this.invite += "- "+user;
	}
	public void removeInvite(String user){
		 this.invite = invite.replace("- "+user, "");
	}
	
	public String[] getArrayIvite(){
		String[] parts = invite.split("- ");
		return parts;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdDate() {
		return idDate;
	}
	public void setIdDate(Long idDate) {
		this.idDate = idDate;
	}
	public int getNbrInvite() {
		return nbrInvite;
	}
	public void setNbrInvite(int nbrInvite) {
		this.nbrInvite = nbrInvite;
	}
	public String getHeure() {
		return heure;
	}
	public void setHeure(String heure) {
		this.heure = heure;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}

	public void setInviteChoix(String inviteChoix) {
		this.invite = inviteChoix;
	}

	
}
