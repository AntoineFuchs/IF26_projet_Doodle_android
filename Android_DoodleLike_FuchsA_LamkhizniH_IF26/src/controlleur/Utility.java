package controlleur;


import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsonParser.AsyncGetRDV;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;
import modele.DateForm;
import modele.HeureForm;
import modele.RdvForm;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.DateBDD;
import dao.HeureBDD;
import dao.RdvBDD;
import activity.LoginActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;



// Methodes récurrentes
public class Utility implements IApiAccessResponse{
	
	private static RdvBDD rdvBdd;
	private static HeureBDD heureBdd;
	private static DateBDD dateBdd;
	
	
	
	private static Pattern pattern;
	private static Matcher matcher;
	//Email Pattern
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	public static boolean validate(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public  static final  void isConnected(Activity act){ // si l'utilisateur n'as plus de connection internet
		ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		boolean reponse = (netInfo != null &&  netInfo.isConnectedOrConnecting());

		if(! reponse ){
			Toast.makeText(act, "Pas de connection",
					Toast.LENGTH_LONG).show();
			backToLogin(act);}

	}

	public static final  void backToLogin(Activity act){

		Intent intent = new Intent(act.getApplicationContext(), LoginActivity.class);
		act.startActivityForResult(intent, 0);
		act.finish();
	}

	
	
	
	 // synchronisation bdd LOcal- Bdd EnLigne
	public  static final  void getRDVServeur(String utilisateurMail, Activity activity,AsyncGetRDV dFEm, RdvForm rdvForm, DateForm dateForm, HeureForm heureForm,JSONObject reponseJson, String token){ 

	

		rdvBdd  = new RdvBDD(activity.getApplicationContext());
		dateBdd  = new DateBDD(activity.getApplicationContext());
		heureBdd = new HeureBDD(activity.getApplicationContext());


		try {

			

			reponseJson = dFEm.execute(ApplicationConstants.APP_SERVER_URL,token,utilisateurMail).get();

			if( reponseJson.has("erreur") ){
				Utility.backToLogin(activity);
			}
			JSONArray message = reponseJson.getJSONArray("reponse");


			rdvBdd.open();
			dateBdd.open();
			heureBdd.open();

			Long idNewRdv = null;
			Long idNewDate = null;
			String idRdv = ""; // param de control
			String newIdRdv;   // ********

			JSONObject jsonObjectRdv;

			String checkDate = "";
			int checkMajDateChoisie = -1;

			String[] dateDecomp;
			String[] dateHeureDecomp;

			for(int i = 0 ; i< message.length() ; i++){


				jsonObjectRdv = message.getJSONObject(i);
				newIdRdv = jsonObjectRdv.getString("iD_event");

				Log.i("", "      $$$$                Frag 1 maSynchro BDD  Sujet   : "+jsonObjectRdv.getString("sujet")+" de : "+jsonObjectRdv.getString("ID"));


				// RDV
				if(idRdv.matches("")  && !rdvBdd.existFrom(jsonObjectRdv.getString("ID"),jsonObjectRdv.getString("sujet")) ) { // On créé le 1er nouvelle evenemnt et le rdv n'existe pas deja localement

					idRdv = newIdRdv;
					checkMajDateChoisie = 2; // pas besoin de mettre a jour la datechoisie du rdv

					rdvForm = new RdvForm(jsonObjectRdv.getString("ID"),jsonObjectRdv.getString("sujet"),jsonObjectRdv.getString("description"),jsonObjectRdv.getString("contacts"),jsonObjectRdv.getString("dateChoisie"));
					idNewRdv = rdvBdd.insert(rdvForm);
					Log.i("", "Frag 1 maSynchro BDD First  idNewRdv   : "+idNewRdv);


				}//1er passage	

				else if(! idRdv.matches(newIdRdv) && !rdvBdd.existFrom(jsonObjectRdv.getString("ID"),jsonObjectRdv.getString("sujet")) ){ // on arrive sur un  nouvel événement

					idRdv = newIdRdv;
					checkMajDateChoisie = 2;

					rdvForm = new RdvForm(jsonObjectRdv.getString("ID"),jsonObjectRdv.getString("sujet"),jsonObjectRdv.getString("description"),jsonObjectRdv.getString("contacts"),jsonObjectRdv.getString("dateChoisie"));
					idNewRdv = rdvBdd.insert(rdvForm);
					Log.i("", "Frag 1 maSynchro BDD   idNewRdv  : "+idNewRdv);

				}

				else if(! idRdv.matches(newIdRdv) && rdvBdd.existFrom(jsonObjectRdv.getString("ID"),jsonObjectRdv.getString("sujet")) ){// on arrive sur un nouveau rdv deja dans la bdd local
					checkMajDateChoisie = 0;

				}

				if( checkMajDateChoisie == 0){ // mettre a jour la datechoisie du rdv pour un rdv deja dans la bdd locale

					Log.i("", "Frag 1 maSynchro BDD  preMajInviteRdv  : "+jsonObjectRdv.getString("sujet")+" et ID : "+jsonObjectRdv.getString("ID"));

					rdvBdd.updateDate(jsonObjectRdv.getString("ID"),jsonObjectRdv.getString("sujet"),jsonObjectRdv.getString("dateChoisie"));
					checkMajDateChoisie = 1;
					Log.i("", "Frag 1 maSynchro BDD  MajInviteRdv  : "+jsonObjectRdv.getString("sujet"));

				}

				if(checkMajDateChoisie >= 2 ){ //il s'agit d'un nouveau rendez vous venant d'etre creer par un autre utilisateur , on ajoute les dates et heures

					// DATE
					if(! checkDate.matches(jsonObjectRdv.getString("date")) || checkMajDateChoisie == 2){ // on creer une nouvelle date   (ou si mm date mais rdv diférent)
						checkMajDateChoisie = 3;
						checkDate = jsonObjectRdv.getString("date");
						dateDecomp = decomposeDate(jsonObjectRdv.getString("date"));
						dateForm = new DateForm(idNewRdv, Integer.valueOf( dateDecomp[0]), (Integer.valueOf(dateDecomp[1])-1), Integer.valueOf(dateDecomp[2]) );

						idNewDate = dateBdd.insert(dateForm);
						Log.i("", "Frag 1 maSynchro BDD   new date id : "+idNewDate);

					} 

					//HEURE   (chaque row corespond a une heure)

					dateHeureDecomp = decomposeDate(jsonObjectRdv.getString("valeur"));
					heureForm = new HeureForm(Long.parseLong( jsonObjectRdv.getString("id_horaire") ), idNewDate, dateHeureDecomp[3], dateHeureDecomp[4], jsonObjectRdv.getString("invitesinscrit") );
					Long idInfo = heureBdd.insertAvecContact(heureForm);

					Log.i("", "Frag 1 maSynchro BDD   id new Info : "+idInfo);

				}

				Log.i("", "Frag 1 maSynchro BDD   check : "+checkDate+" et checkMaj : "+checkMajDateChoisie);

			}
			Log.i("", "Frag 1 maSynchro BDD   fin ");
			rdvBdd.close();
			heureBdd.close();
			dateBdd.close();



		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	static final String[] decomposeDate(String dateSql){
		String date[] = new String[5];

		date[0] = dateSql.substring(0, 4) ;
		date[1] = dateSql.substring(5, 7) ;
		date[2] = dateSql.substring(8, 10) ;

		if(dateSql.length() >10){
			date[3] = dateSql.substring(11, 13) ;
			date[4] = dateSql.substring(14, 16) ;
		}else {date[3] = "vide "; date[4]=" vide";}
		for(int i = 0 ; i < 5; i++){
			Log.i("", "Frag 1 maSynchro BDD   decomp date : "+date[i]);
		}
		return date;
	}



	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}
}
