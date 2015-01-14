package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import org.apache.http.HttpResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

@SuppressLint("NewApi")

public class LoginActivity extends Activity implements View.OnClickListener, IApiAccessResponse{
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById(R.id.validerBouton).setOnClickListener(this);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/*@Override
	public void onClick(View v) {
		JSONObject reponseJson = null;
		AsyncToken dFT = new AsyncToken();
		dFT.delegate = this;

		EditText email = (EditText)findViewById(R.id.editTextEmail);
		String emailTxt = email.getText().toString();
		EditText mdp = (EditText)findViewById(R.id.password);
		String pswdTxt = mdp.getText().toString();
		
		String url = "http://train.sandbox.eutech-ssii.com/messenger/login.php";
		
			
			try {
				
				reponseJson = dFT.execute(url,emailTxt,pswdTxt).get();
			
			} catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}



			try {
				if(reponseJson.get("error").toString().compareTo("false")==0){

					Intent IntentContacts = new Intent(this, ListeContact_Activity.class);
					IntentContacts.putExtra("reponseJson", reponseJson.toString() );
					startActivityForResult(IntentContacts, 0);
					finish();
				}
				
			else{
					Context context = getApplicationContext();
					CharSequence text = "Login incorrect";
					int duration = Toast.LENGTH_LONG;
					
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				
			}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	}
	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}*/
	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}
	public void valider (){
		
		
		EditText edtTxt = (EditText)findViewById(R.id.editTextEmail);
		if( ! isValidEmail(edtTxt.getText().toString()))
			edtTxt.setError( "exemple@exemple.exemple" );
		

	}
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	@Override
	public void onClick(View v) {
		Intent IntentContacts = new Intent(this, MainActivity.class);
		startActivityForResult(IntentContacts, 0);
		finish();
		
	}

}