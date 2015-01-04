package form;

public class ContactForm {
	int id;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ContactForm(String mail,String preno){
		eMail= mail;
		prenom = preno;
	}
	public ContactForm(String mail,String nm,String preno){
		eMail= mail;
		nom = nm;
		prenom = preno;
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
