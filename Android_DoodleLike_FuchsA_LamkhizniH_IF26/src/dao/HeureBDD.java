package dao;

import java.util.ArrayList;
import java.util.List;

import modele.ApplicationConstants;
import modele.HeureForm;
import bdd.MaBaseSQLite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
 
public class HeureBDD {
 
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = ApplicationConstants.DBNAME;
 
	private static final String TABLE_HORAIRE = "table_horaire";
	private static final String COL_ID_HORAIRE= "ID_Horaire";
	private static final int NUM_COL_ID_HORAIRE = 0;
	private static final String COL_ID_ADATE = "ID_Adate";
	private static final int NUM_COL_ID_ADATE = 1;
	private static final String COL_HEURE = "Heure";
	private static final int NUM_COL_HEURE= 2;
	private static final String COL_MINUTE = "Minute";
	private static final int NUM_COL_MINUTE = 3;
	private static final String COL_CONTACT_OK = "Contact_Ok"; //"- ctct1- ctct2- ..."
	private static final int NUM_COL_CONTACT_OK = 4;


	  private String[] allColumns = { COL_ID_HORAIRE, COL_ID_ADATE,COL_HEURE,COL_MINUTE,COL_CONTACT_OK };
 
	private SQLiteDatabase bdd;
 
	private MaBaseSQLite maBaseSQLite;
 
	public HeureBDD(Context context){
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
 
	public long insert(Long id,Long idDate,String heure, String minute){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_ID_HORAIRE, id);
		values.put(COL_ID_ADATE, idDate);
		values.put(COL_HEURE, heure);
		values.put(COL_MINUTE,  minute);
		values.put(COL_CONTACT_OK, "");
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_HORAIRE, null, values);
	}
	
	public long insertAvecContact(HeureForm heureForm){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_ID_HORAIRE, heureForm.getId()); Log.i("", "Heure BDD    dateID "+ heureForm.getIdDate() );
		values.put(COL_ID_ADATE, heureForm.getIdDate());
		values.put(COL_HEURE, heureForm.getHeure());
		values.put(COL_MINUTE,  heureForm.getMinute());
		values.put(COL_CONTACT_OK, heureForm.getInvite());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_HORAIRE, null, values);
	}
 
 
	public int updateChoix(Long id, String listInvite){ // met a jours les contact inscrit a cette heure
		ContentValues values = new ContentValues();
		values.put(COL_CONTACT_OK, listInvite);
		return bdd.update(TABLE_HORAIRE, values, COL_ID_HORAIRE + " = " +id, null);
	}
 
	public int removeWithID(Long id){
		//Suppression de la BDD grâce à l'ID
		return bdd.delete(TABLE_HORAIRE, COL_ID_HORAIRE + " = \"" +id+ "\"", null);
	}

 
	public HeureForm getWithID(Long id){
		//Récupère dans un Cursor les valeur correspondant, contenu dans la BDD
		Cursor c = bdd.query(TABLE_HORAIRE, new String[] {COL_ID_HORAIRE, COL_ID_ADATE, COL_HEURE,COL_MINUTE,COL_CONTACT_OK }, COL_ID_HORAIRE + " = \"" + id +"\"", null, null, null, null);
		return cursorToRDV(c);
	}
 
	  public List<HeureForm> getAllFromDate(Long idDate) {
		    List<HeureForm> comments = new ArrayList<HeureForm>();

		    Cursor cursor = bdd.query(TABLE_HORAIRE, new String[] {COL_ID_HORAIRE, COL_ID_ADATE, COL_HEURE,COL_MINUTE,COL_CONTACT_OK }, COL_ID_ADATE + " = \"" + idDate +"\"", null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	HeureForm comment = cursorToRDVRec(cursor);
		      comments.add(comment);
		      cursor.moveToNext();
		    }
		    // assurez-vous de la fermeture du curseur
		    cursor.close();
		    return comments;
		  }
	  
	  public List<HeureForm> getAll() {
		    List<HeureForm> comments = new ArrayList<HeureForm>();

		    Cursor cursor = bdd.query(TABLE_HORAIRE,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	HeureForm comment = cursorToRDVRec(cursor);
		      comments.add(comment);
		      cursor.moveToNext();
		    }
		    // assurez-vous de la fermeture du curseur
		    cursor.close();
		    return comments;
		  }
	  
	//Cette méthode permet de convertir un cursor en un objet
	private HeureForm cursorToRDV(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un objet
		HeureForm info = new HeureForm();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		info.setIdDate(c.getLong(NUM_COL_ID_ADATE));
		info.setId(c.getLong(NUM_COL_ID_HORAIRE));
		info.setHeure(c.getString(NUM_COL_HEURE));
		info.setMinute(c.getString(NUM_COL_MINUTE));
		info.setInviteChoix(c.getString(NUM_COL_CONTACT_OK));

		c.close();
		//On retourne l'objet
		return info;
	}
	private HeureForm cursorToRDVRec(Cursor c){
		if (c.getCount() == 0)
			return null;

		HeureForm info = new HeureForm();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		info.setIdDate(c.getLong(NUM_COL_ID_ADATE));
		info.setId(c.getLong(NUM_COL_ID_HORAIRE));
		info.setHeure(c.getString(NUM_COL_HEURE));
		info.setMinute(c.getString(NUM_COL_MINUTE));
		info.setInviteChoix(c.getString(NUM_COL_CONTACT_OK));

 
		return info;
	}
}