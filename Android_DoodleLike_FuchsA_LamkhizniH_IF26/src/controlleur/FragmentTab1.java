package controlleur;

import java.util.GregorianCalendar;
import java.util.List;
import jsonParser.AsyncDeleteContactsRDV;
import jsonParser.AsyncGetRDV;
import jsonParser.IApiAccessResponse;
import modele.DateForm;
import modele.HeureForm;
import modele.RdvForm;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import activity.EventActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import dao.RdvBDD;

public class FragmentTab1 extends ListFragment  implements IApiAccessResponse {

	private EventListAdapter eventListAdapter;
	List<RdvForm> listInfoEvent;
	String utilisateurMail;
	JSONObject reponseJson;

	private RdvBDD rdvBdd;
	RdvForm rdvForm;
	DateForm dateForm;
	HeureForm heureForm;

	private String token;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.fragmenttab1, container, false);
		
		Activity activity = this.getActivity();
		Utility.isConnected(activity);

		SharedPreferences prefs = new ObscuredSharedPreferences( view.getContext(), this.getActivity().getSharedPreferences("userDoodleLikedetails", Context.MODE_PRIVATE) );
		token = prefs.getString("token", "erreur email Frag2");
		utilisateurMail = prefs.getString("userMail", "erreur email Frag2");

		AsyncGetRDV dFEm = new AsyncGetRDV();
		dFEm.delegate = this;
		Utility.getRDVServeur(utilisateurMail,this.getActivity(),dFEm,rdvForm, dateForm, heureForm, reponseJson,token);

		interfaceBuilder(view);
	

		return view;
	}



	public void interfaceBuilder(View view) {
		// Fait le JSonarray des contacts

		RdvBDD rdvBdd  = new RdvBDD(view.getContext());
		rdvBdd.open();
		listInfoEvent =	rdvBdd.getAll(utilisateurMail);
		rdvBdd.close();

		eventListAdapter =new EventListAdapter(this.getActivity(),listInfoEvent);
		setListAdapter(eventListAdapter);


	}

	public void onListItemClick(ListView l, View v, final int position, long id) {
		final Activity activity = this.getActivity(); 
		final RdvForm rdvChoisie = listInfoEvent.get(position);
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Log.i("", "Frag 1 DateFixe : "+listInfoEvent.get(position).getNewRDV());
		if( listInfoEvent.get(position).getNewRDV().matches("") || listInfoEvent.get(position).getNewRDV().matches("0000-00-00 00:00:00") ){
			Intent IntentEvent = new Intent(this.getActivity(), EventActivity.class);
			IntentEvent.putExtra("idRdv", listInfoEvent.get(position).getId());
			IntentEvent.putExtra("createurRdv", listInfoEvent.get(position).getCreateur());
			IntentEvent.putExtra("sujetRdv", listInfoEvent.get(position).getTitre());
			IntentEvent.putExtra("descrptRdv", listInfoEvent.get(position).getDescription());

			startActivityForResult(IntentEvent, 0);
			activity.finish();
		}
		else{ // on demande si l'utilisateur veut l'ajouter à son agenda et on le suprime des BDD


			if( listInfoEvent.get(position).getNewRDV().contains("1111") ){
				new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Suppression")
				.setMessage("Rendez-vous annulé.")
				.setPositiveButton("OK",null)
				.show();
			} 
			else{
				new AlertDialog.Builder(this.getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Suppression")
				.setMessage("Enregistrer dans l'agenda ?")
				.setPositiveButton("Oui", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						toCalendar(rdvChoisie);  
					}
				})
				.setNegativeButton("Non", null)
				.show();

			}
			// supression partout :

			deleteRdv( position);


		}
	}
	



	private void toCalendar(RdvForm rdvForm){
		String[] date = Utility.decomposeDate( rdvForm.getNewRDV() );
		int[] dates = new int[5];
		for(int i = 0; i <date.length ; i++) dates[i] = Integer.valueOf(date[i]);
		Intent calIntent = new Intent(Intent.ACTION_INSERT); 
		calIntent.setType("vnd.android.cursor.item/event");    
		calIntent.putExtra(Events.TITLE, rdvForm.getTitre()); 
		calIntent.putExtra(Events.DESCRIPTION, rdvForm.getDescription()); 

		GregorianCalendar calDate = new GregorianCalendar(dates[0], dates[1], dates[2], dates[3], dates[4]);
		calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false); 
		calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, 
				calDate.getTimeInMillis()); 


		startActivity(calIntent);
	}

	public void deleteRdv(int position){
		rdvBdd.open();
		rdvBdd.removeWithID(listInfoEvent.get(position).getId());
		rdvBdd.close();

		AsyncDeleteContactsRDV ADL= new AsyncDeleteContactsRDV();
		ADL.delegate = this;
		AsyncRequest.asyncDeleteContactsRDV(listInfoEvent.get(position).getCreateur(),listInfoEvent.get(position).getTitre(),
				ADL,utilisateurMail,token,this.getActivity()); // supprime l'utilisateur de la liste des invités
		listInfoEvent.remove(listInfoEvent.get(position));
		eventListAdapter.notifyDataSetChanged();
	}

	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub

	}

}