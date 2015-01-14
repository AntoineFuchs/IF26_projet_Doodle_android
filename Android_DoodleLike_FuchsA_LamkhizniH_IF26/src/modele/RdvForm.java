package modele;

public class RdvForm {
	private long id;
	private String createur, titre,description,contacts, newRDV;
	
	
	public RdvForm(){
		titre = "";
		description = "";
		contacts = "";
		newRDV = "";
	}
	
	public RdvForm(String auteur, String ttr,String dscrpt, String ctct){
		createur = auteur;
		titre = ttr;
		description = dscrpt;
		contacts = ctct;
		newRDV = "";
	}
	
	public RdvForm(String auteur, String ttr,String dscrpt, String ctct,String date){
		createur = auteur;
		titre = ttr;
		description = dscrpt;
		contacts = ctct;
		newRDV = date;
	}
	


	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getNewRDV() {
		return newRDV;
	}

	public void setNewRDV(String newRDV) {
		this.newRDV = newRDV;
	}



	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateur() {
		return createur;
	}

	public void setCreateur(String createur) {
		this.createur = createur;
	}
	
	
}
