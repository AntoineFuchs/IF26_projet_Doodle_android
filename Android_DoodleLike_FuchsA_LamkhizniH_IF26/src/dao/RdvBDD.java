package dao;

import java.util.ArrayList;
import java.util.List;

import modele.ApplicationConstants;
import modele.RdvForm;
import bdd.MaBaseSQLite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RdvBDD {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = ApplicationConstants.DBNAME;

	private static final String TABLE_RDV = "table_rdv";
	private static final String COL_ID_RDV = "ID_Rdv";
	private static final int NUM_COL_ID_RDV = 0;
	private static final String COL_AUTEUR = "Auteur";
	private static final int NUM_COL_AUTEUR= 1;
	private static final String COL_CONTACTS = "Contacts";
	private static final int NUM_COL_CONTACTS = 2;
	private static final String COL_TITRE = "Titre";
	private static final int NUM_COL_TITRE = 3;
	private static final String COL_DESCRITPION = "Description";
	private static final int NUM_COL_DESCRITPION= 4;
	private static final String COL_NEWRDV = "NewRdv";
	private static final int NUM_COL_NEWRDV= 5;


	private String[] allColumns = { COL_ID_RDV, COL_AUTEUR,COL_CONTACTS,COL_TITRE,COL_DESCRITPION,COL_NEWRDV };

	private SQLiteDatabase bdd;

	private MaBaseSQLite maBaseSQLite;

	public RdvBDD(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
		if (!bdd.isReadOnly()) {
			bdd.execSQL("PRAGMA foreign_keys = ON;");
		}
	}

	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}

	public SQLiteDatabase getBDD(){
		return bdd;
	}

	public long insert(RdvForm info){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_AUTEUR, info.getCreateur());
		values.put(COL_CONTACTS, info.getContacts());
		values.put(COL_TITRE, info.getTitre());
		values.put(COL_DESCRITPION, info.getDescription());
		values.put(COL_NEWRDV, info.getNewRDV());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_RDV, null, values);
	}



	public int updateChoix(Long id, String choix){
		ContentValues values = new ContentValues();
		values.put(COL_NEWRDV, choix);
		return bdd.update(TABLE_RDV, values, COL_ID_RDV + " = " +id, null);
	}
	
	public int updateDate(String auteur,String sujet, String choix){
		
		Log.i("", "RdvBDD  updateDAte"+auteur);
		
		ContentValues values = new ContentValues();
		values.put(COL_NEWRDV, choix);
		return bdd.update(TABLE_RDV, values, COL_AUTEUR + " LIKE \"" +auteur+"\" AND "+COL_TITRE + " LIKE \""+sujet+"\";", null);
	}
	
	public boolean exist(String sujet){

		int count = -1;
		Cursor c = null; 
		String query = "SELECT COUNT(*) FROM " + TABLE_RDV + " WHERE " + COL_TITRE + " = ?";
		c = bdd.rawQuery(query, new String[] {sujet});
		if (c.moveToFirst()) {
			count = c.getInt(0);
		}
		if(count>0)
			return true;
		else return false;

	}
	public boolean existFrom(String auteur,String sujet){

		int count = -1;
		Cursor c = null; 
		String query = "SELECT COUNT(*) FROM " + TABLE_RDV + " WHERE " + COL_TITRE + " = ? AND "+COL_AUTEUR+" = ?";
		c = bdd.rawQuery(query, new String[] {sujet,auteur});
		
		if (c.moveToFirst()) {
			count = c.getInt(0);
		}
		Log.i("", "rdvBdd existFrom count : "+count);

		if(count>0)
			return true;
		else return false;

	}
	
	

	public int removeWithID(Long id){
		//Suppression de la BDD grâce à l'ID
		return bdd.delete(TABLE_RDV, COL_ID_RDV + " = \"" +id+ "\"", null);
	}

	public RdvForm getWithID(Long id){
		//Récupère dans un Cursor les valeur correspondant, contenu dans la BDD
		Cursor c = bdd.query(TABLE_RDV, allColumns, COL_ID_RDV + " = \"" + id +"\"", null, null, null, null);
		return cursorToRDV(c);
	}

	public List<RdvForm> getAll(String email) {
		List<RdvForm> comments = new ArrayList<RdvForm>();
		
		email = "\"%"+email+"%\"";
		
		Cursor cursor = bdd.query(TABLE_RDV,
				allColumns, COL_CONTACTS+" LIKE "+email, null, null, null, null);

		Log.i("", "nbr de rdv : "+cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RdvForm comment = cursorToRDVRec(cursor);
			comments.add(comment);
			cursor.moveToNext();
		}
		// assurez-vous de la fermeture du curseur
		cursor.close();
		return comments;
	}
	//Cette méthode permet de convertir un cursor en un objet
	private RdvForm cursorToRDV(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;

		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un objet
		RdvForm info = new RdvForm();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		info.setId(c.getLong(NUM_COL_ID_RDV));
		info.setCreateur(c.getString(NUM_COL_AUTEUR));
		info.setContacts(c.getString(NUM_COL_CONTACTS));
		info.setTitre(c.getString(NUM_COL_TITRE));
		info.setDescription(c.getString(NUM_COL_DESCRITPION));
		info.setNewRDV(c.getString(NUM_COL_NEWRDV));

		c.close();
		//On retourne l'objet
		return info;
	}
	private RdvForm cursorToRDVRec(Cursor c){
		if (c.getCount() == 0)
			return null;

		RdvForm info = new RdvForm();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		info.setId(c.getLong(NUM_COL_ID_RDV));
		info.setCreateur(c.getString(NUM_COL_AUTEUR));
		info.setContacts(c.getString(NUM_COL_CONTACTS));
		info.setTitre(c.getString(NUM_COL_TITRE));
		info.setDescription(c.getString(NUM_COL_DESCRITPION));
		info.setNewRDV(c.getString(NUM_COL_NEWRDV));

		return info;
	}
}