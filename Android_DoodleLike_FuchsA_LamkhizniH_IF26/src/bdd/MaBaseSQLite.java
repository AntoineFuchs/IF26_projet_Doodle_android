package bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
 
public class MaBaseSQLite extends SQLiteOpenHelper {
 
	private static final String TABLE_CONTACTS = "table_contacts";
	private static final String COL_ID_CONTACT = "ID_contact";
	private static final String COL_EMAIL = "EMail";
	private static final String COL_NOM = "Nom";
	private static final String COL_PRENOM = "Prenom";

 
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_CONTACTS + " ("
	+ COL_ID_CONTACT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_EMAIL + " TEXT NOT NULL, "
	+ COL_NOM + " TEXT NOT NULL, "+ COL_PRENOM + " TEXT NOT NULL);";
 
	public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//on cr�� la table � partir de la requ�te �crite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut fait ce qu'on veut ici moi j'ai d�cid� de supprimer la table et de la recr�er
		//comme �a lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_CONTACTS + ";");
		onCreate(db);
	}
 
}