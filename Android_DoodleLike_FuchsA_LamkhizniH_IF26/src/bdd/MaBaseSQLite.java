package bdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class MaBaseSQLite extends SQLiteOpenHelper {

	private static final String TABLE_CONTACTS = "table_contacts";
	private static final String COL_ID_CONTACT = "ID_contact";
	private static final String COL_EMAIL = "EMail";
	private static final String COL_NOM = "Nom";
	private static final String COL_PRENOM = "Prenom";

	private static final String TABLE_RDV = "table_rdv";
	private static final String COL_ID_RDV = "ID_Rdv";
	private static final String COL_AUTEUR = "Auteur";
	private static final String COL_CONTACTS = "Contacts";
	private static final String COL_TITRE = "Titre";
	private static final String COL_DESCRITPION = "Description";
	private static final String COL_NEWRDV = "NewRdv";

	private static final String TABLE_DATE = "table_date";
	private static final String COL_ID_DATE = "ID_date";
	private static final String COL_ID_EVNMT = "ID_Ev";
	private static final String COL_ANNEE = "Annee";
	private static final String COL_MOIS = "Mois";
	private static final String COL_JOUR = "Jour";

	private static final String TABLE_HORAIRE = "table_horaire";
	private static final String COL_ID_HORAIRE = "ID_Horaire";
	private static final String COL_ID_ADATE = "ID_Adate";
	private static final String COL_HEURE = "Heure";
	private static final String COL_MINUTE = "Minute";
	private static final String COL_CONTACT_OK = "Contact_Ok";


	private static final String CREATE_CONTACT = "CREATE TABLE " + TABLE_CONTACTS + " ("
			+ COL_ID_CONTACT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_EMAIL + " TEXT UNIQUE NOT NULL, "
			+ COL_NOM + " TEXT NOT NULL, "+ COL_PRENOM + " TEXT NOT NULL);";

	private static final String CREATE_RDV = "CREATE TABLE " + TABLE_RDV + " ("
			+ COL_ID_RDV + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_AUTEUR + " TEXT NOT NULL, "
			+ COL_CONTACTS + " TEXT NOT NULL, "+ COL_TITRE + " TEXT NOT NULL, "+ COL_DESCRITPION + " TEXT NOT NULL, "+ COL_NEWRDV + " TEXT NOT NULL);";

	private static final String CREATE_DATE = "CREATE TABLE " + TABLE_DATE + " ("
			+ COL_ID_DATE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ID_EVNMT + " INTEGER NOT NULL, "
			+ COL_ANNEE + " TEXT NOT NULL, "+ COL_MOIS + " TEXT NOT NULL, "+ COL_JOUR + " TEXT NOT NULL, FOREIGN KEY("+ COL_ID_EVNMT +") REFERENCES table_rdv("+ COL_ID_RDV +") ON DELETE CASCADE);";


	private static final String CREATE_HORAIRE = "CREATE TABLE " + TABLE_HORAIRE + " ("
			+ COL_ID_HORAIRE + " INTEGER PRIMARY KEY , " + COL_ID_ADATE + " INTEGER NOT NULL, "
			+ COL_HEURE + " TEXT NOT NULL, "+ COL_MINUTE + " TEXT NOT NULL, "+ COL_CONTACT_OK + " TEXT NOT NULL, FOREIGN KEY("+ COL_ID_ADATE +") REFERENCES table_date("+ COL_ID_DATE +") ON DELETE CASCADE);";

	public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if( isTableExists(db ,TABLE_CONTACTS) ) 
			db.execSQL("DROP TABLE " + TABLE_CONTACTS + ";");
		if( isTableExists(db ,TABLE_RDV) ) 

			db.execSQL("DROP TABLE " + TABLE_RDV + ";");
		if( isTableExists(db ,TABLE_DATE) ) 
			db.execSQL("DROP TABLE " + TABLE_DATE + ";");
		if( isTableExists(db ,TABLE_HORAIRE) ) 
			db.execSQL("DROP TABLE " + TABLE_HORAIRE + ";");
		
		db.execSQL(CREATE_CONTACT);
		db.execSQL(CREATE_RDV);
		db.execSQL(CREATE_DATE);
		db.execSQL(CREATE_HORAIRE);

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme ça lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_CONTACTS + ";");
		db.execSQL("DROP TABLE " + TABLE_RDV + ";");
		db.execSQL("DROP TABLE " + TABLE_DATE + ";");
		db.execSQL("DROP TABLE " + TABLE_HORAIRE + ";");


		onCreate(db);
	}

	public boolean isTableExists(SQLiteDatabase db,String tableName) {


		Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
		if(cursor!=null) {
			if(cursor.getCount()>0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		cursor.close();

		return false;
	}

}