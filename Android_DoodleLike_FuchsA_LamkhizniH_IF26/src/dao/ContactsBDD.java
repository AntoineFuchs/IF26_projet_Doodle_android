package dao;

import java.util.ArrayList;
import java.util.List;

import modele.ApplicationConstants;
import modele.ContactForm;
import bdd.MaBaseSQLite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ContactsBDD {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = ApplicationConstants.DBNAME;

	private static final String TABLE_CONTACTS = "table_contacts";
	private static final String COL_ID_CONTACT = "ID_contact";
	private static final int NUM_COL_ID_CONTACT = 0;
	private static final String COL_EMAIL = "EMail";
	private static final int NUM_COL_EMAIL = 1;
	private static final String COL_NOM = "Nom";
	private static final int NUM_COL_NOM = 2;
	private static final String COL_PRENOM = "Prenom";
	private static final int NUM_COL_PRENOM = 3;
	private String[] allColumns = { COL_ID_CONTACT, COL_EMAIL,COL_NOM,COL_PRENOM };

	private SQLiteDatabase bdd;

	private MaBaseSQLite maBaseSQLite;

	public ContactsBDD(Context context){
		//On cr�er la BDD et sa table
		maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en �criture
		bdd = maBaseSQLite.getWritableDatabase();
	}

	public void close(){
		//on ferme l'acc�s � la BDD
		bdd.close();
	}

	public SQLiteDatabase getBDD(){
		return bdd;
	}

	public long insert(ContactForm info){
		//Cr�ation d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associ� � une cl� (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_EMAIL, info.geteMail());
		values.put(COL_NOM, info.getNom());
		values.put(COL_PRENOM, info.getPrenom());
		//on ins�re l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_CONTACTS, null, values);
	}

	public int update(int id, ContactForm info){
		//La mise � jour d'un objet dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple pr�ciser quelle objet on doit mettre � jour gr�ce � l'ID
		ContentValues values = new ContentValues();
		values.put(COL_EMAIL, info.geteMail());
		values.put(COL_NOM, info.getNom());
		values.put(COL_PRENOM, info.getPrenom());
		return bdd.update(TABLE_CONTACTS, values, COL_ID_CONTACT + " = " +id, null);
	}

	public int removeWithEmail(String email){
		//Suppression d'un objet de la BDD gr�ce � l'ID
		return bdd.delete(TABLE_CONTACTS, COL_EMAIL + " LIKE \"" +email+ "\"", null);
	}

	public ContactForm getWithEmail(String titre){
		//R�cup�re dans un Cursor les valeur correspondant � un objet contenu dans la BDD (ici on s�lectionne l'objet gr�ce � son titre)
		Cursor c = bdd.query(TABLE_CONTACTS, new String[] {COL_ID_CONTACT, COL_EMAIL, COL_NOM,COL_PRENOM}, COL_EMAIL + " LIKE \"" + titre +"\"", null, null, null, null);
		return cursorToContact(c);
	}

	public boolean exist(String email){		
		int reponse = -1;
		Cursor c = null; 
		String query = "SELECT COUNT(*) FROM " 
				+ TABLE_CONTACTS + " WHERE " + COL_EMAIL + " = ?";
		c = bdd.rawQuery(query, new String[] {email});
		if (c.moveToFirst()) {
			reponse = c.getInt(0);
		}
		return reponse > 0;

	}

	public int getMinId(){
		int result = -1;

		Cursor c = bdd.query(TABLE_CONTACTS, new String[] { "min(" + COL_ID_CONTACT + ")" }, null, null,
				null, null, null);  
		c.moveToFirst();  //ADD THIS!
		result = c.getInt(0);
		return result;
	}
	public ContactForm getUtilisateur(){
		//R�cup�re dans un Cursor les valeur correspondant � un objet contenu dans la BDD (ici on s�lectionne l'objet gr�ce � son titre)
		Cursor c = bdd.query(TABLE_CONTACTS, new String[] {COL_ID_CONTACT, COL_EMAIL, COL_NOM,COL_PRENOM}, COL_ID_CONTACT + " = "+getMinId()+"", null, null, null, null);
		return cursorToContact(c);
	}

	//    Pour obtenir les contacts � afficher
	public List<ContactForm> getAllComments(int val) { 
		List<ContactForm> comments = new ArrayList<ContactForm>();

		Cursor cursor = bdd.query(TABLE_CONTACTS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) { 
			ContactForm comment = cursorToContactRec(cursor);
			if(val != 0) { comments.add(comment);}// On affiche pas le mail de l'utilisateur
			else  val++;
			cursor.moveToNext();
		}
		// assurez-vous de la fermeture du curseur
		cursor.close();
		return comments;
	}
	//Cette m�thode permet de convertir un cursor en un objet
	private ContactForm cursorToContact(Cursor c){
		//si aucun �l�ment n'a �t� retourn� dans la requ�te, on renvoie null
		if (c.getCount() == 0)
			return null;

		//Sinon on se place sur le premier �l�ment
		c.moveToFirst();
		//On cr�� un objet
		ContactForm info = new ContactForm();
		//on lui affecte toutes les infos gr�ce aux infos contenues dans le Cursor
		info.setId(c.getLong(NUM_COL_ID_CONTACT));
		info.seteMail(c.getString(NUM_COL_EMAIL));
		info.setNom(c.getString(NUM_COL_NOM));
		info.setPrenom(c.getString(NUM_COL_PRENOM));
		c.close();
		//On retourne l'objet
		return info;
	}
	private ContactForm cursorToContactRec(Cursor c){
		if (c.getCount() == 0)
			return null;

		ContactForm info = new ContactForm();
		//on lui affecte toutes les infos gr�ce aux infos contenues dans le Cursor
		info.setId(c.getLong(NUM_COL_ID_CONTACT));
		info.seteMail(c.getString(NUM_COL_EMAIL));
		info.setNom(c.getString(NUM_COL_NOM));
		info.setPrenom(c.getString(NUM_COL_PRENOM));

		//On retourne l'objet
		return info;
	}
}