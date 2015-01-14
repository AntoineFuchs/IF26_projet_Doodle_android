package activity;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jsonParser.AsyncLogin;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;
import controlleur.ObscuredSharedPreferences;
import controlleur.Utility;

@SuppressLint("NewApi")

public class LoginActivity extends Activity implements View.OnClickListener, IApiAccessResponse{
	

	private String userMail;
	private File database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		setEmail();
		
		// pour le developement : 
		//this.deleteDatabase(ApplicationConstants.DBNAME);

		
	
		
		database=getApplicationContext().getDatabasePath(ApplicationConstants.DBNAME);

		if ( database.exists()) {
			EditText email = (EditText)findViewById(R.id.editTextEmail);
			email.setInputType(EditorInfo.TYPE_NULL);
			findViewById(R.id.validerBouton).setOnClickListener(this);

		    findViewById(R.id.butonRegister).setVisibility(View.GONE);
		} else {
		    findViewById(R.id.blockInfo).setVisibility(View.GONE);
		    findViewById(R.id.pwd_block).setVisibility(View.GONE);
		    findViewById(R.id.validerBouton).setVisibility(View.GONE);
			findViewById(R.id.butonRegister).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), RegisterActivity.class);
					
					startActivityForResult(intent, 0);
					finish();
				}
			});
		}
		
		
		// Pour le dev
		/*
		//reset BDD
		MaBaseSQLite maBaseSQLite = new MaBaseSQLite(this.getApplicationContext(),ApplicationConstants.DBNAME , null, 1);  // changer le nom
		SQLiteDatabase bdd = maBaseSQLite.getWritableDatabase();
		maBaseSQLite.reset(bdd);
		
		
		// En attendant register
		ContactForm message = new ContactForm("h");
		ContactsBDD contactBdd  = new ContactsBDD(this.getApplicationContext());
		contactBdd.open();
		contactBdd.insert(message);
		Log.i("","User : "+contactBdd.getUtilisateur().geteMail());
		contactBdd.close();
		*/
		

		

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		
		
		JSONObject reponseJson = null;
		AsyncLogin dFT = new AsyncLogin();
		dFT.delegate = this;

		EditText email = (EditText)findViewById(R.id.editTextEmail);
		String emailTxt = email.getText().toString();
		EditText mdp = (EditText)findViewById(R.id.editTextPassword);
		String pswdTxt = mdp.getText().toString();		
			
		if( TextUtils.isEmpty(emailTxt) || !Utility.validate(emailTxt)){
       	 Toast.makeText(this, "Email invalide !",
                    Toast.LENGTH_LONG).show();
       	 	return;
		}
			try {
				
				reponseJson = dFT.execute(ApplicationConstants.APP_SERVER_URL,emailTxt,pswdTxt).get();
			
			} catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			try {
				if(reponseJson.get("error").toString().compareTo("false")==0){

					sotckMdp(reponseJson.get("token").toString(), emailTxt);

					Intent intent = new Intent(this, MainActivity.class);
					startActivityForResult(intent, 0);
					finish();
				}
				
			else{
					Context context = getApplicationContext();
					CharSequence text = "Logs incorrect";
					int duration = Toast.LENGTH_LONG;
					
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					try {
					    Thread.sleep(3000);                 //1000 milliseconds is one second.
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
			}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	}

	// Entre l'mail de l'utilisateur rentre lors de son inscritption
	private void setEmail (){
		final SharedPreferences prefs = new ObscuredSharedPreferences( this, this.getSharedPreferences("userDoodleLikedetails", Context.MODE_PRIVATE) );
		EditText email = (EditText)findViewById(R.id.editTextEmail);
		userMail = prefs.getString("userMail", "Pas de resultat");
		email.setText(userMail);
		
		
	}

	// On stock le token et le nom de l'utilisateur dans les sharedPreference et on le crypte 
	private void sotckMdp(String token, String user){
		final SharedPreferences prefs = new ObscuredSharedPreferences( this, this.getSharedPreferences("userDoodleLikedetails", Context.MODE_PRIVATE) );

			// eg.    
			prefs.edit().putString("token",token).commit();
			
			
			Log.i("", "Login info : "+prefs.getString("userMail", "Pas de resultat"));
	}
	
	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}
	
	
}