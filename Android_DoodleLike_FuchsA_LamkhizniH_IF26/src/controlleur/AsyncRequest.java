package controlleur;

import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;

import jsonParser.AsyncAddDate;
import jsonParser.AsyncAddHeure;
import jsonParser.AsyncAddRDV;
import jsonParser.AsyncDeleteContactsRDV;
import jsonParser.AsyncUpateRdvDate;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;
import android.app.Activity;
import android.util.Log;

public class AsyncRequest  implements IApiAccessResponse {

	
	final static String asyncAddRdv(String sujet,String descr, String invites, AsyncAddRDV dFT,String utilisateurMail, String token,Activity activiy){
		
		String reponse=null;
		try {
			reponse = dFT.execute(ApplicationConstants.APP_SERVER_URL,utilisateurMail,sujet,descr,invites,token).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.i("", "Frag 2 recu RDV OK :   "+reponse);
		if( reponse.contains("session expirée") ){
			Utility.backToLogin(activiy);
		}

		return reponse;
	}

	final static String asyncAddDate(String reponseRequeteRdv,String date, AsyncAddDate dFT,String utilisateurMail, String token,Activity activiy){
		
		String reponse =null;
		try {
			reponse = dFT.execute(ApplicationConstants.APP_SERVER_URL,reponseRequeteRdv,date,token).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( reponse.contains("session expirée") ){
			Utility.backToLogin(activiy);
		}

		Log.i("", "Frag 2 recu Date OK :   "+reponse);

		return reponse;
	}

	final static String asyncAddHeure(String reponseRequeteDate,String heure,  AsyncAddHeure dFT,String utilisateurMail, String token,Activity activiy){  // a utiliser

		
		String reponse =null;
		try {
			reponse = dFT.execute(ApplicationConstants.APP_SERVER_URL,reponseRequeteDate,heure,token).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( reponse.contains("session expirée") ){
			Utility.backToLogin(activiy);
		}


		Log.i("", "Frag 2 recu Heure OK :   "+reponse);		
		return reponse;
	}
	
	final static void asyncDeleteContactsRDV(String createur,String sujet,AsyncDeleteContactsRDV dFT,String utilisateurMail, String token,Activity activiy){
		
		String reponse =null;
		String mail = "- "+utilisateurMail;
		try {
			reponse = dFT.execute(ApplicationConstants.APP_SERVER_URL,token,createur,sujet,mail).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( reponse.contains("session expirée") ){
			Utility.backToLogin(activiy);

		}

	}

	
	final static  String asyncUpdateOnlineRDV(String createur,String sujet,String choix, AsyncUpateRdvDate dFT, String token,Activity activiy){
		
        String reponse = null;
		try {
			reponse = dFT.execute(ApplicationConstants.APP_SERVER_URL,token,createur,sujet,choix).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("", "DateChooser Date final to serveur reponse "+reponse);
		if( reponse.contains("session expirée") )
			Utility.backToLogin(activiy);

		return reponse;
	}
	
	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}
}
