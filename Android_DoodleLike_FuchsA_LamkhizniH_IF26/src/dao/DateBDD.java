package dao;

import java.util.ArrayList;
import java.util.List;

import modele.ApplicationConstants;
import modele.DateForm;
import bdd.MaBaseSQLite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
public class DateBDD {
 
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = ApplicationConstants.DBNAME;
 
	private static final String TABLE_DATE = "table_date";
	private static final String COL_ID_DATE = "ID_date";
	private static final int NUM_COL_ID_DATE = 0;
	private static final String COL_ID_EVNMT = "ID_Ev";
	private static final int NUM_COL_ID_EVNMT= 1;
	private static final String COL_ANNEE = "Annee";
	private static final int NUM_COL_ANNEE = 2;
	private static final String COL_MOIS = "Mois";
	private static final int NUM_COL_MOIS = 3;
	private static final String COL_JOUR = "Jour";
	private static final int NUM_COL_JOUR= 4;


	  private String[] allColumns = { COL_ID_DATE, COL_ID_EVNMT,COL_ANNEE,COL_MOIS,COL_JOUR };
 
	private SQLiteDatabase bdd;
 
	private MaBaseSQLite maBaseSQLite;
 
	public DateBDD(Context context){
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
 
	public long insert(DateForm info){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_ID_EVNMT, info.getId_rdv());
		values.put(COL_ANNEE, info.getYear());
		values.put(COL_MOIS, info.getMonth());
		values.put(COL_JOUR, info.getDay());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_DATE, null, values);
	}
 

	public int removeWithID(Long id){
		//Suppression de la BDD grâce à l'ID
		return bdd.delete(TABLE_DATE, COL_ID_DATE + " = \"" +id+ "\"", null);
	}
	
	
	// Retourne l'id le plus élevé pour pouvoir créé le suivant manuellement (synchro plus facile ac la bbd du serveur)
	public int getMaxId(){
		int result = -1;

		/*
		Cursor mCursor = bdd.rawQuery("SELECT MAX(ID_date) FROM table_date;",null);
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            result = mCursor.getInt(mCursor.getColumnIndex("ID_date"));
          }*/
		return result;
	}
 
	public DateForm getWithID(Long id){
		//Récupère dans un Cursor les valeur correspondant, contenu dans la BDD
		Cursor c = bdd.query(TABLE_DATE, new String[] {COL_ID_DATE, COL_ID_EVNMT, COL_ANNEE,COL_MOIS,COL_JOUR}, COL_ID_DATE + " = \"" + id +"\"", null, null, null, null);
		return cursorToRDV(c);
	}
 
	  public List<DateForm> getAll() {
		    List<DateForm> comments = new ArrayList<DateForm>();

		    Cursor cursor = bdd.query(TABLE_DATE,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	DateForm comment = cursorToRDVRec(cursor);
		      comments.add(comment);
		      cursor.moveToNext();
		    }
		    // assurez-vous de la fermeture du curseur
		    cursor.close();
		    return comments;
		  }
	  
	  public List<DateForm> getAllFromRdv(Long RdvId) {
		    List<DateForm> comments = new ArrayList<DateForm>();

		    Cursor cursor = bdd.query(TABLE_DATE, new String[] {COL_ID_DATE, COL_ID_EVNMT, COL_ANNEE,COL_MOIS,COL_JOUR}, COL_ID_EVNMT + " = \"" + RdvId +"\"", null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	DateForm comment = cursorToRDVRec(cursor);
		      comments.add(comment);
		      cursor.moveToNext();
		    }
		    // assurez-vous de la fermeture du curseur
		    cursor.close();
		    return comments;
		  }
	  
	//Cette méthode permet de convertir un cursor en un objet
	private DateForm cursorToRDV(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un objet
		DateForm info = new DateForm();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		info.setId(c.getLong(NUM_COL_ID_DATE));
		info.setId_rdv(c.getLong(NUM_COL_ID_EVNMT));
		info.setYear(c.getString(NUM_COL_ANNEE));
		info.setMonth(c.getString(NUM_COL_MOIS));
		info.setDay(c.getString(NUM_COL_JOUR));

		c.close();
		//On retourne l'objet
		return info;
	}
	private DateForm cursorToRDVRec(Cursor c){
		if (c.getCount() == 0)
			return null;

		DateForm info = new DateForm();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		info.setId(c.getLong(NUM_COL_ID_DATE));
		info.setId_rdv(c.getLong(NUM_COL_ID_EVNMT));
		info.setYear(c.getString(NUM_COL_ANNEE));
		info.setMonth(c.getString(NUM_COL_MOIS));
		info.setDay(c.getString(NUM_COL_JOUR));

 
		return info;
	}
}