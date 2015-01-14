package modele;

public class ContactForm {
	long id;
	private String nom, prenom, eMail;
	
	public ContactForm(){
		eMail= null;
		nom = null;
		prenom = null;
	}
	public ContactForm(String mail){
		eMail= mail;
		nom = "";
		prenom = "";
	}

	public ContactForm(String mail,String nm,String preno){
		eMail= mail;
		nom = nm;
		prenom = preno;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prénom) {
		this.prenom = prénom;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	
}
