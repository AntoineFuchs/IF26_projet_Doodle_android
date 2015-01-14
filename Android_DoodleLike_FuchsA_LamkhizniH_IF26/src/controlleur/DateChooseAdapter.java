package controlleur;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;

import jsonParser.AsyncUpateRdvDate;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;
import modele.DateForm;
import activity.MainActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import dao.RdvBDD;

//Utiliser dans EventActivity pour s'inscrire a une date

@SuppressLint("InflateParams")
public class DateChooseAdapter extends ArrayAdapter<DateForm> implements IApiAccessResponse {
	private Activity activity; 
	private LayoutInflater inflater;
	private List<DateForm> listDate;
	private List<String> mois = Arrays.asList("Janvier", "Février", "Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre");
	
	private Long idRdv;
	private  Boolean isCreateurRdv;
	private String utilisateurMail;
	private String createurRDV;
	private String sujetRDV;
	private String token;
	IApiAccessResponse delegate;
	public DateChooseAdapter(Activity activity, List<DateForm> messages1, Long idRdv, Boolean isCreateurRdv, String utilisateurMail,String createurRDV,String sujetRDV,String descrptRDV){
		super(activity, R.layout.date_x_hour_row,messages1);
		this.activity = activity;
		inflater = activity.getWindow().getLayoutInflater();
		listDate = messages1;
		this.idRdv =idRdv;
		this.isCreateurRdv = isCreateurRdv;
		this.utilisateurMail = utilisateurMail;
		this.createurRDV =createurRDV;
		this.sujetRDV = sujetRDV;
		final SharedPreferences prefs = new ObscuredSharedPreferences( 
				inflater.getContext(), activity.getSharedPreferences("userDoodleLikedetails", Context.MODE_PRIVATE) );

		token = prefs.getString("token", "erreur email Frag2");
		delegate =this;
	}


	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		
		View v = inflater.inflate(R.layout.date_x_hour_row, null);

		TextView t = (TextView) v.findViewById(R.id.textDate);
		t.setText(listDate.get(position).getDay()+" . " +mois.get(Integer.valueOf(listDate.get(position).getMonth()))+ " . " +listDate.get(position).getYear());

		LinearLayout  linearLayout = (LinearLayout )v.findViewById(R.id.hourLinLayout);


		for(int i = 0; i<listDate.get(position).getHeuresForm().size();i++){
			final int posHeure=i;
			final TextView myTxtView = new TextView(v.getContext());
			final String infoHoraire = listDate.get(position).getHeureForm(i).getHeure()+"h"+ listDate.get(position).getHeureForm(i).getMinute()+" ,   participant(s) : " ;

			// On affiche les utilisateur inscrit à tel horaire

			myTxtView.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(final View v) {
							
							// Affiche les invité inscr
							String[] getArrayIvite = listDate.get(position).getHeureForm(posHeure).getArrayIvite();

							AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext());
							builderSingle.setIcon(R.drawable.ic_launcher);
							builderSingle.setTitle("Préfére cet horaire : ");

							final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
									v.getContext(),
									android.R.layout.select_dialog_singlechoice);

							for(int x= 1; x < getArrayIvite.length ;x++){
								arrayAdapter.add(getArrayIvite[x]);
							}	           

							builderSingle.setNegativeButton("sortir",
									new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});


							if(isCreateurRdv){
								// on enregistre la date final choisis par créateur au format :   AAAA-MM-JJ HH:MM:SS
								builderSingle.setPositiveButton("choisir cette date",
										new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										String choix ="";

										choix+=""+listDate.get(position).getYear();
										int mois = Integer.valueOf(listDate.get(position).getMonth()) ;mois++;
										choix+="-"+Integer.toString(mois);
										choix+="-"+listDate.get(position).getDay();
										choix+=" "+listDate.get(position).getHeureForm(posHeure).getHeure();
										choix+=":"+listDate.get(position).getHeureForm(posHeure).getMinute();
										choix+=":00";
										Log.i("", "DateChooseAdapter   choix final : "+ choix);

										RdvBDD rdvBdd  = new RdvBDD(activity.getApplicationContext());
										rdvBdd.open();
										rdvBdd.updateChoix(idRdv, choix);
										rdvBdd.close();
										
										AsyncUpateRdvDate dFEm = new AsyncUpateRdvDate();
								        dFEm.delegate = delegate;
								        String reponseRequete = AsyncRequest.asyncUpdateOnlineRDV(createurRDV,sujetRDV,choix,dFEm,token,activity);

										if(reponseRequete != null && reponseRequete.contains("session expirée") ){
											Utility.backToLogin(activity);
										}
										
										Intent intent = new Intent(activity, MainActivity.class);
										activity.startActivityForResult(intent, 0);
										activity.finish();

									}
								});
							}

							builderSingle.setAdapter(arrayAdapter,null );
							builderSingle.show();



						}
					});


			// On gere l'inscription/desinscription de l'utilisateur à une heure donnée

			myTxtView.setOnLongClickListener(
					new OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {
							listDate.get(position).getHeureForm(posHeure).changeInscription(utilisateurMail);

							changeTextView(myTxtView, infoHoraire, listDate.get(position).getHeureForm(posHeure).isRegister(utilisateurMail), listDate.get(position).getHeureForm(posHeure).getTtailleInvite());

							return true;}
					});

			changeTextView(myTxtView, infoHoraire,listDate.get(position).getHeureForm(i).isRegister(utilisateurMail), listDate.get(position).getHeureForm(i).getTtailleInvite());


			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			llp.setMargins(6, 6, 0, 6);
			myTxtView.setLayoutParams(llp);
			linearLayout.addView(myTxtView);
		}

		return v;
	}

	private void changeTextView(TextView myTxtView, String infoHoraire, boolean inscrit, String nbrInvite ){
		Log.i("", "DateCjhoose  nbrInvite  : "+nbrInvite);
		if(inscrit){
			myTxtView.setBackgroundColor(Color.parseColor("#b2d5e2"));
			myTxtView.setText(infoHoraire+""+nbrInvite+" \n (Maintenir pour désinscription)");
		}else{
			myTxtView.setBackgroundColor(Color.parseColor("#f3f1ca"));
			myTxtView.setText(infoHoraire+" \n (Maintenir pour inscription)");
		}
	}



	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}

}