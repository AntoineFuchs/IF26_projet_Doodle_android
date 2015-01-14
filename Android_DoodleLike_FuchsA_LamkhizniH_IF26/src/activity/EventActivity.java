package activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jsonParser.AsynDeleteRdv;
import jsonParser.AsyncDeleteContactsRDV;
import jsonParser.AsyncGetInviteHoraire;
import jsonParser.AsyncUpateRdvDate;
import jsonParser.AsyncUpdateHoraireInvite;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;
import modele.DateForm;
import modele.HeureForm;
import modele.RdvForm;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import controlleur.DateChooseAdapter;
import controlleur.ObscuredSharedPreferences;
import controlleur.Utility;
import dao.DateBDD;
import dao.HeureBDD;
import dao.RdvBDD;


//  Lorsque l'on choisie un RDV dans le menu principale.
//  Permet de choisir les dates qui nous interessent

public class EventActivity extends Activity implements IApiAccessResponse  {

	List<DateForm> listDatesChoisie;
	private ListView myListDates;
	private DateChooseAdapter dateChooseAdapter;
	private Button boutonTerminer;
	private String utilisateurMail;

	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);


		final SharedPreferences prefs = new ObscuredSharedPreferences( 
				this, this.getSharedPreferences("userDoodleLikedetails", Context.MODE_PRIVATE) );

		token = prefs.getString("token", "erreur token EvntActiv");
		utilisateurMail =  prefs.getString("userMail", "erreur email EvntActiv");

		final long idRDV =(long) getIntent().getExtras().get("idRdv");
		final String sujetRDV =(String) getIntent().getExtras().get("sujetRdv");
		String descrptRDV =(String) getIntent().getExtras().get("descrptRdv");
		final String createurRDV = (String) getIntent().getExtras().get("createurRdv");
		boolean isCreateurRdv = utilisateurMail.matches(createurRDV);
		Log.i("", "id ; "+idRDV);



		// Info RDV

		RdvBDD rdvBdd  = new RdvBDD(this.getApplicationContext());
		rdvBdd.open();
		RdvForm rdv = rdvBdd.getWithID(idRDV);
		rdvBdd.close();

		Log.i("", "EventActivity invité : "+rdv.getContacts());


		TextView t = (TextView) this.findViewById(R.id.textSujet);
		t.setText(rdv.getTitre());
		t = (TextView) this.findViewById(R.id.textAuteur);
		
		if(isCreateurRdv){ // si l'utilisateur à créé ce rendez vous il peut l'annuler
			
			t.setText("Créateur : Vous \n ( Maintenir pour tout supprimer )");
			t.setOnLongClickListener(
					new OnLongClickListener() {                                             // On supprime le RDV
						@Override
						public boolean onLongClick(final View v) {

							new AlertDialog.Builder(v.getContext())
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle("Suppression : ")
							.setMessage("Ete vous sur de vouloir supprimer ce \"rendez-vous\"?")
							.setPositiveButton("Valider", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which) {
									/*
					        	 On vérifie qu'il ya ya bien supression en cascade rdv > date > Heure

					    		HeureBDD heureBDD = new HeureBDD(v.getContext());
					    		heureBDD.open();
					    		List<HeureForm> list = heureBDD.getAll();
					    		heureBDD.close();
					    		for(HeureForm heureF: list){
					    			Log.i("", "EventActiv on choose date, id heure before :"+heureF.getId());
					    		}
									 */
									
									updateOnlineRDV( createurRDV,  sujetRDV, "1111-11-11 11:11:11");
									 asyncDeleteContactsRDV(createurRDV,  sujetRDV);
									RdvBDD rdvBDD = new RdvBDD(v.getContext());
									rdvBDD.open();
									rdvBDD.removeWithID(idRDV);
									rdvBDD.close();

									/*
					    		 On vérifie qu'il ya ya bien supression en cascade rdv > date > Heure
					    		 heureBDD = new HeureBDD(v.getContext());
					    		heureBDD.open();
					    		list = heureBDD.getAll();
					    		heureBDD.close();
					    		for(HeureForm heureF: list){
					    			Log.i("", "EventActiv on choose date, id heure after :"+heureF.getId());
					    		}
									 */
									sortir();
									finish();    
								}

							})
							.setNegativeButton("Annuler", null)
							.show();
							return false;
						}
					});
		}
		else 	
			t.setText("Créateur "+rdv.getCreateur());


		t = (TextView) this.findViewById(R.id.EditTextInfo);
		t.setText(rdv.getDescription());



		// Liste des Dates
		listDatesChoisie = new ArrayList<DateForm>();

		DateBDD dateBdd  = new DateBDD(this.getApplicationContext());
		HeureBDD heureBDD = new HeureBDD(this.getApplicationContext());

		dateBdd.open();
		heureBDD.open();

		List<DateForm> listeDate = dateBdd.getAllFromRdv(idRDV);
		List<HeureForm> listeHeure;
		String info;
		for(DateForm date : listeDate){

			listeHeure = heureBDD.getAllFromDate(date.getId());
			for(HeureForm heure : listeHeure){
				info = this.getServeurInvitePourDate(heure.getId()) ;
				Log.i("", "EventActiv : id date : "+heure.getId()+" et "+info);

				//heure.setInviteChoix(info);

				date.addHeureForm(heure);
			}
			listDatesChoisie.add(date);
		}

		heureBDD.close();
		dateBdd.close();

		dateChooseAdapter = new DateChooseAdapter(this,listDatesChoisie,idRDV,isCreateurRdv,utilisateurMail,createurRDV,sujetRDV,descrptRDV);
		myListDates=(ListView)this.findViewById(R.id.listView_dates);
		myListDates.setAdapter(dateChooseAdapter);


		// Sortir de l'activité et enregistrer les changements de choix

		boutonTerminer = (Button)this.findViewById(R.id.terminerButon);
		boutonTerminer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {

				HeureBDD heureBDD = new HeureBDD(v.getContext());
				heureBDD.open();

				List<HeureForm> listeHeure;
				for(DateForm date : listDatesChoisie){
					listeHeure = date.getHeuresForm();
					for(HeureForm heure : listeHeure){
						Log.i("", "EventActiv : Update Horaire id date : "+heure.getId()+" et invite : "+heure.getInvite());
						//MAJ BDD serveur
						majServeurInviteHoraire(heure.getId().toString(), heure.getInvite());


						//MAJ BDD Local
						heureBDD.updateChoix(heure.getId(), heure.getInvite());


					}
				}
				heureBDD.close();

				sortir();
			}
		});
	}

	private void sortir(){

		Utility.backToLogin(this);

	}

	private String getServeurInvitePourDate(Long id){
		Utility.isConnected(this);


		String reponseInvite = null;
		AsyncGetInviteHoraire dFEm = new AsyncGetInviteHoraire();
		dFEm.delegate = this;
		try {
			reponseInvite = dFEm.execute(ApplicationConstants.APP_SERVER_URL,token,id.toString()).get();
			if( reponseInvite.contains("session expirée") ){
				Utility.backToLogin(this);
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("", "EventActiv  getServeurinvite reponse "+reponseInvite);

		return reponseInvite;
	}

	private String updateOnlineRDV(String createur,String sujet,String choix){
		AsyncUpateRdvDate dFEm = new AsyncUpateRdvDate();
        dFEm.delegate = this;
        String reponse = null;
		try {
			reponse = dFEm.execute(ApplicationConstants.APP_SERVER_URL,token,createur,sujet,choix).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("", "DateChooser Date final to serveur reponse "+reponse);
		
		return reponse;
	}
	private String majServeurInviteHoraire(String id,String value){
		String reponse = null;

		Utility.isConnected(this);

		Log.i("", id+" EventActiv  Updtateserveurinvite Test value "+value);

		AsyncUpdateHoraireInvite dFEm = new AsyncUpdateHoraireInvite();
		dFEm.delegate = this;

		try {

			reponse = dFEm.execute(ApplicationConstants.APP_SERVER_URL,token,id,value).get();
			if( reponse.contains("session expirée") ){
				Utility.backToLogin(this);
			}

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("", "EvenActiv porbleme maj");
		}
		Log.i("", "EventActiv  Updtateserveurinvite reponse "+reponse);
		return reponse;
	}
	
	private void asyncDeleteContactsRDV(String createur,String sujet){
		
		Utility.isConnected(this);

		AsyncDeleteContactsRDV ADL= new AsyncDeleteContactsRDV();
		ADL.delegate = this;
		String reponse =null;
		String mail = "- "+utilisateurMail;
		try {
			reponse = ADL.execute(ApplicationConstants.APP_SERVER_URL,token,createur,sujet,mail).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( reponse.contains("session expirée") ){
			Utility.backToLogin(this);
		}

	}
	




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub

	}
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
