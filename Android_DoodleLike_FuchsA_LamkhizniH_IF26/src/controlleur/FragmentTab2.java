package controlleur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import jsonParser.AsyncAddDate;
import jsonParser.AsyncAddHeure;
import jsonParser.AsyncAddRDV;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;
import modele.DateForm;
import modele.HeureForm;
import modele.InviteForm;
import modele.RdvForm;
import org.apache.http.HttpResponse;
import activity.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;
import dao.ContactsBDD;
import dao.DateBDD;
import dao.HeureBDD;
import dao.RdvBDD;

public class FragmentTab2 extends Fragment implements IApiAccessResponse   {
	private View view;
	private FragmentTab2 delegate;

	private Button boutonAjoutDate;
	private Button boutonAjoutInvite;
	private Button boutonAjoutRdv;

	private List<DateForm> listDatesPicker;
	private DateFormAdapter dateFormAdapter;
	private DatePickerFragment datePickerFragment;

	private InviteFormAdapter inviteFormAdapter;
	private List<InviteForm> listInvitesPicker;
	private InvitePickerFragment invitePickerFragment;


	private RdvBDD rdvBdd;
	private RdvForm infoEvent;
	private DateBDD dateBdd;
	private DateForm messageDate ;
	private HeureBDD heureBdd;

	private String reponseRequeteRdv = null;
	private String reponseRequeteDate = null ;
	private String reponseRequeteHeure = null;

	private String utilisateurMail;
	private String token;

	private Activity activity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		view = inflater.inflate(R.layout.fragmenttab2, container, false);
		interfaceBuilder(view);
		activity = this.getActivity();
		SharedPreferences prefs = new ObscuredSharedPreferences( view.getContext(), this.getActivity().getSharedPreferences("userDoodleLikedetails", Context.MODE_PRIVATE) );
		token = prefs.getString("token", "erreur email Frag2");
		utilisateurMail = prefs.getString("userMail", "erreur email Frag2");
		
		delegate=this;
		return view;
	}


	private void interfaceBuilder(final View view) {


		// Ajout de Date

		listDatesPicker = new ArrayList<DateForm>();
		dateFormAdapter = new DateFormAdapter(this.getActivity(),listDatesPicker);

		boutonAjoutDate = (Button)view.findViewById(R.id.btnChangeDate);
		boutonAjoutDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listDatesPicker.size() == 0 )
				{	
					Log.i("", "Dans le click");
					datePickerFragment = new DatePickerFragment( getActivity(), (ListView)view.findViewById(R.id.listView_dates),listDatesPicker, dateFormAdapter);
				}
				Log.i("", "Dans le dsqdclick");

				datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
				listDatesPicker = datePickerFragment.getListDatesPicker();
				dateFormAdapter = datePickerFragment.getDateFormAdapter();
			}
		});


		// Ajout d'invité
		listInvitesPicker = new ArrayList<InviteForm>();

		inviteFormAdapter = new InviteFormAdapter(this.getActivity(),listInvitesPicker);


		boutonAjoutInvite = (Button)view.findViewById(R.id.btnInviter);
		boutonAjoutInvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listInvitesPicker.size() == 0 )
				{	
					invitePickerFragment = new InvitePickerFragment(getActivity(), (ListView)view.findViewById(R.id.listView_invites),listInvitesPicker,inviteFormAdapter, new ContactsBDD(v.getContext()));
				}
				invitePickerFragment.show(getActivity().getSupportFragmentManager(), "invitePicker");
				//if(! invitePickerFragment.isAdded()) invitePickerFragment.show(getSupportFragmentManager(), "invitePicker");
				listInvitesPicker = invitePickerFragment.getListInvitesPicker();
				inviteFormAdapter = invitePickerFragment.getInviteFormAdapter();
			}
		});




		// Ajout d'un nouveau rendez vous.

		boutonAjoutRdv = (Button)view.findViewById(R.id.validerBouton);
		boutonAjoutRdv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utility.isConnected(activity);

		        
				
				
				rdvBdd  = new RdvBDD(v.getContext());

				EditText edTxT = (EditText)view.findViewById(R.id.EditTextName); String sujet = edTxT.getText().toString(); 
				edTxT = (EditText)view.findViewById(R.id.EditTextFeedbackBody); String descr = edTxT.getText().toString(); 
				String invites = "- "+utilisateurMail;
				for(InviteForm item : listInvitesPicker){
					invites+= "- "+item.getNomInvite();
				}
				listInvitesPicker.clear();		
				inviteFormAdapter.notifyDataSetChanged();

				rdvBdd.open();
				boolean existSujet = rdvBdd.exist(sujet);
				rdvBdd.close();

				//Log.i("",Pattern.matches("[a-zA-Z]+",sujet)+" Frag 2  bool sujet : "+existSujet);
				if(!existSujet){  // ne marcher pas car les chiffre etait refusé : corrigé


					dateBdd  = new DateBDD(v.getContext());
					messageDate = new DateForm(); ;
					heureBdd = new HeureBDD(v.getContext());

					// On recupere les valeurs :

					//Log.i("", "Sujet : "+sujet.toString());Log.i("", "Descrpt : "+descr.toString());Log.i("", "Liste invités : "+invites.toString());

					// On remplie RdvBBD
					infoEvent = new RdvForm(utilisateurMail,sujet,descr,invites);

					//RdvBDD en ligne

					AsyncAddRDV dFR = new AsyncAddRDV();
					dFR.delegate = delegate;
					reponseRequeteRdv = AsyncRequest.asyncAddRdv( sujet, descr,invites,  dFR,utilisateurMail,token,activity);

					Log.i("", "Frag 2 recu RDV OK :   "+reponseRequeteRdv);
					if( reponseRequeteRdv.contains("session expirée") ){
						Utility.backToLogin(activity);
					}



					//BDD locale

					rdvBdd.open();
					messageDate.setId_rdv(rdvBdd.insert(infoEvent));
					rdvBdd.close();

					dateBdd.open();
					heureBdd.open();


					//2009-07-02 01:02:03 

					// On à recupéré l'ID du RDV et on remplie DateBBD et HeureDBB
					for(DateForm dateForm:listDatesPicker){

						//Log.i("", "getYear : "+dateForm.getYear());Log.i("", "getMonth : "+dateForm.getMonth());Log.i("", "getDay : "+dateForm.getDay());


						messageDate.setYear( dateForm.getYear());
						messageDate.setMonth(dateForm.getMonth());
						messageDate.setDay(dateForm.getDay());

						dateForm.setId( dateBdd.insert(messageDate));

						//PlaningBDD en ligne

						AsyncAddDate dFD = new AsyncAddDate();
							dFD.delegate = delegate;
						reponseRequeteDate = AsyncRequest.asyncAddDate(reponseRequeteRdv, messageDate.getSqlDate(), dFD,utilisateurMail,token,activity);


						// On rajoute les heure : 

						for( HeureForm heureI : dateForm.getHeuresForm()){


							//HeureBDD en ligne
							AsyncAddHeure dFH = new AsyncAddHeure();
							dFH.delegate = delegate;
							
							String heure  = messageDate.getSqlDate()+" "+heureI.getSqlHeure();
							reponseRequeteHeure = AsyncRequest.asyncAddHeure(reponseRequeteDate, heure,  dFH,utilisateurMail,token,activity);

							// HeureBDD local avec l'id de celle créé sur le serveur
							long idInfo = heureBdd.insert(Long.parseLong(reponseRequeteHeure),dateForm.getId(), heureI.getHeure(), heureI.getMinute() );

							Log.i("", dateForm.getId()+" Fragment 2 addRdv hours : "+  heureI.getHeure()); Log.i("", "getMinute : "+ heureI.getMinute()); Log.i("", "Date id : "+dateForm.getId()+" , id nouvel horaire : "+idInfo);

						}
					}


					heureBdd.close();
					dateBdd.close();
					listDatesPicker.clear();
					dateFormAdapter.notifyDataSetChanged();

					reload();

				}
				else{
					edTxT = (EditText)view.findViewById(R.id.EditTextName); edTxT.setText("");
					edTxT.setError( "Titre invalide" );
				}
			}
		});


	}



	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub

	}


	

	private void reload(){
		Intent intent = new Intent(this.getActivity(), MainActivity.class);
		this.getActivity().startActivityForResult(intent, 0);
		this.getActivity().finish();
	}





}