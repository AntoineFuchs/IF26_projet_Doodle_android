package activity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import jsonParser.AsyncIsEmailUnVerif;
import jsonParser.AsyncRegister;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;
import modele.ContactForm;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import bdd.MaBaseSQLite;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import controlleur.ObscuredSharedPreferences;
import controlleur.Utility;
import dao.ContactsBDD;

public class RegisterActivity extends Activity implements IApiAccessResponse  {
/*
	ProgressDialog prgDialog;
	RequestParams params = new RequestParams();
	GoogleCloudMessaging gcmObj;
	String regId = "";

	//private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	AsyncTask<Void, Void, String> createRegIdTask;*/
	Context applicationContext;

	public static final String REG_ID = "regId";
	public static final String EMAIL_ID = "eMailId";
	EditText emailET;
	EditText passwordET;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		Utility.isConnected(this);
	
		applicationContext = getApplicationContext();
		emailET = (EditText) findViewById(R.id.emailReg);
		passwordET = (EditText) findViewById(R.id.passwordReg);
/*
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);
*/
		SharedPreferences prefs = getSharedPreferences("UserDetails",
				Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");

		if (!TextUtils.isEmpty(registrationId)) {
			Intent i = new Intent(applicationContext, LoginActivity.class);
			i.putExtra("regId", registrationId);
			startActivity(i);
			finish();
		}

	}

	// When Register Me button is clicked
	public void RegisterUser(View view) {
		String emailID = emailET.getText().toString();
		String passwordID = passwordET.getText().toString();

		if( TextUtils.isEmpty(emailID) || !Utility.validate(emailID)){
			Toast.makeText(this, "Email invalide !",
					Toast.LENGTH_LONG).show();
			return;
		}
		String reponseIsEmail = null;
		AsyncIsEmailUnVerif dFEm = new AsyncIsEmailUnVerif();
		dFEm.delegate = this;
		try {
			reponseIsEmail = dFEm.execute(ApplicationConstants.APP_SERVER_URL,emailID).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("", "Register reponse "+reponseIsEmail);

		if ( passwordID.length() >5 && reponseIsEmail.matches("0")) {
			// Check if Google Play Service is installed in Device
			// Play services is needed to handle GCM stuffs

			// GCM

			/*if (checkPlayServices()) {

                // Register Device in GCM Server
                registerInBackground(emailID);
            }*/
			// Inscription Serveur

			AsyncRegister dFT = new AsyncRegister();
			dFT.delegate = this;
			try {
				dFT.execute(ApplicationConstants.APP_SERVER_URL,emailET.getText().toString(),passwordET.getText().toString()).get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			createLocalBdd(emailID); // initialise la BDD sqlite 
			Utility.backToLogin(this);
		}
		else if(passwordID.length() <=5){
			Toast.makeText(applicationContext, "Au moins 6 caractères pour le mot de passe",
					Toast.LENGTH_LONG).show();
		}
		else if(! reponseIsEmail.matches("0")){
			Toast.makeText(applicationContext, "Email déja pris",
					Toast.LENGTH_LONG).show();
		}
		// When Email is invalid
		else {
			Toast.makeText(applicationContext, "Email invalide",
					Toast.LENGTH_LONG).show();
		}
	}


	public void createLocalBdd(String email){
		MaBaseSQLite maBaseSQLite = new MaBaseSQLite(this.getApplicationContext(),ApplicationConstants.DBNAME , null, 1);  
		SQLiteDatabase bdd = maBaseSQLite.getWritableDatabase();
		maBaseSQLite.onCreate(bdd);


		ContactForm message = new ContactForm(email);
		ContactsBDD contactBdd  = new ContactsBDD(this.getApplicationContext());
		contactBdd.open();
		contactBdd.insert(message);
		Log.i("","User : "+contactBdd.getUtilisateur().geteMail());
		contactBdd.close();

		final SharedPreferences prefs = new ObscuredSharedPreferences( 
				this, this.getSharedPreferences("userDoodleLikedetails", Context.MODE_PRIVATE) );

		// eg.    
		prefs.edit().putString("userMail",email).commit();
		Log.i("", "Register  info : "+prefs.getString("userMail", "Pas de resultat"));
		Toast.makeText(
				applicationContext,
				"Inscription réussis",
				Toast.LENGTH_LONG).show();
	}

	
	
	
	
	// Pour les notification : pas fonctionnel
	
/*	

	// AsyncTask to register Device in GCM Server
	private void registerInBackground(final String emailID) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcmObj == null) {
						gcmObj = GoogleCloudMessaging
								.getInstance(applicationContext);
					}
					regId = gcmObj
							.register(ApplicationConstants.GOOGLE_PROJ_ID);
					msg = "Registration ID :" + regId;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				if (!TextUtils.isEmpty(regId)) {
					storeRegIdinSharedPref(applicationContext, regId, emailID);
					Toast.makeText(
							applicationContext,
							"Registered with GCM Server successfully.\n\n"
									+ msg, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							applicationContext,
							"Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
									+ msg, Toast.LENGTH_LONG).show();
				}
			}
		}.execute(null, null, null);
	}

	// Store RegId and Email entered by User in SharedPref
	private void storeRegIdinSharedPref(Context context, String regId,
			String emailID) {
		SharedPreferences prefs = getSharedPreferences("UserDetails",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putString(EMAIL_ID, emailID);
		editor.commit();
		storeRegIdinServer(regId, emailID);

	}

	// Share RegID and Email ID with GCM Server Application (Php)
	private void storeRegIdinServer(String regId2, String emailID) {
		prgDialog.show();
		params.put("emailId", emailID);
		params.put("regId", regId);
		System.out.println("Email id = " + emailID + " Reg Id = " + regId);
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(ApplicationConstants.APP_SERVER_URL, params,
				new TextHttpResponseHandler() {
			// When the response returned by REST has Http
			// response code '200'
			@Override
			public void onSuccess(int val,Header[] headers,String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				if (prgDialog != null) {
					prgDialog.dismiss();
				}
				Toast.makeText(applicationContext,
						"Reg Id shared successfully with Web App ",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(applicationContext,
						Activity.class);
				i.putExtra("regId", regId);
				startActivity(i);
				finish();
			}

			// When the response returned by REST has Http
			@Override
			public void onFailure(int statusCode,Header[] headers,
					String content, Throwable error) {
				// Hide Progress Dialog
				prgDialog.hide();
				if (prgDialog != null) {
					prgDialog.dismiss();
				}
				// When Http response code is '404'
				if (statusCode == 404) {
					Toast.makeText(applicationContext,
							"Requested resource not found",
							Toast.LENGTH_LONG).show();
				}
				// When Http response code is '500'
				else if (statusCode == 500) {
					Toast.makeText(applicationContext,
							"Something went wrong at server end",
							Toast.LENGTH_LONG).show();
				}
				// When Http response code other than 404, 500
				else {
					Toast.makeText(
							applicationContext,
							"Unexpected Error occcured! [Most common Error: Device might "
									+ "not be connected to Internet or remote server is not up and running], check for other errors as well",
									Toast.LENGTH_LONG).show();
				}
			}


		});
	}

	// Check if Google Playservices is installed in Device or not
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// When Play services not found in device
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				// Show Error dialog to install Play services
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(
						applicationContext,
						"This device doesn't support Play services, App will not work normally",
						Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		} else {
			Toast.makeText(
					applicationContext,
					"This device supports Play services, App will work normally",
					Toast.LENGTH_LONG).show();
		}
		return true;
	}

	// When Application is resumed, check for Play services support to make sure
	// app will be running normally
	@Override
	protected void onResume() {
		super.onResume();
		// checkPlayServices();
	}
	
	*/
	
	
	

	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub

	}
}