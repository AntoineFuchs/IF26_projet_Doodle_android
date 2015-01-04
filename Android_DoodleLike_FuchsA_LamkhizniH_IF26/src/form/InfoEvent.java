package form;

public class InfoEvent {
	private int id;
	private String titre,description,createur, contacts, newRDV;
	
	
	public InfoEvent(){
		titre = "";
		description = "";
		createur = "";
		contacts = "";
		newRDV = "";
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
