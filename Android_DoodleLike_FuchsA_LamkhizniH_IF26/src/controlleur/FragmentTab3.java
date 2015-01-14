package controlleur;

import java.util.List;
import java.util.concurrent.ExecutionException;

import jsonParser.AsyncIsEmail;
import jsonParser.IApiAccessResponse;
import modele.ApplicationConstants;
import modele.ContactForm;
import org.apache.http.HttpResponse;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import dao.ContactsBDD;


public class FragmentTab3 extends Fragment implements IApiAccessResponse  {
	View view;
	ListView myListContact;
	ContactFormAdapter contactFormAdapter;
	List<ContactForm> listContactPicker;
	Button boutonAjout;
	private String token;
	private Activity activity;
	ContactsBDD contactBdd ;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fragmenttab3.xml
		view = inflater.inflate(R.layout.fragmenttab3, container, false);
		
		final SharedPreferences prefs = new ObscuredSharedPreferences(view.getContext(), view.getContext().getSharedPreferences("userDoodleLikedetails", view.getContext().MODE_PRIVATE) );
		token = prefs.getString("token", "Pas de resultat");
			
		activity = this.getActivity();	
		interfaceBuilder(view);
		return view;
	}



	private void interfaceBuilder(final View view) {
		
		// On recupère la liste des contacts
		
		contactBdd  = new ContactsBDD(view.getContext());
		contactBdd.open();
		listContactPicker = contactBdd.getAllComments(0);
		contactBdd.close();
		contactFormAdapter = new ContactFormAdapter(this.getActivity(),listContactPicker);

		//On les met dans la Listview
		myListContact=(ListView)view.findViewById(R.id.listContacts);
		myListContact.setAdapter(contactFormAdapter);
		
		myListContact.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long arg3) {


				ContactsBDD contactBdd  = new ContactsBDD(view.getContext());
				contactBdd.open();
				contactBdd.removeWithEmail(listContactPicker.get(position).geteMail().toString());
				contactBdd.close();

				contactFormAdapter.remove(listContactPicker.get(position));
				contactFormAdapter.notifyDataSetChanged();

				return false;
			}

		});
		
		
		// Listener sur le bouton d'ajout
		
		boutonAjout =(Button)view.findViewById(R.id.addContactBouton);
		boutonAjout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Utility.isConnected(activity);

				
				EditText edTxT = (EditText)view.findViewById(R.id.newContactEmail);
				String emailValue = edTxT.getText().toString();
				String reponseIsEmail =checkEmailExist(emailValue); // vérifie que le contact est bien enregistrer en ligne
				
		        if( reponseIsEmail.contains("session expirée") ){
					Utility.backToLogin(activity);
		        }

				contactBdd.open();
				boolean contactExist = contactBdd.exist(emailValue);
				contactBdd.close();
				
				if( TextUtils.isEmpty(emailValue) || !Utility.validate(emailValue))
	        	 Toast.makeText(view.getContext(), "Email invalide !",
	                     Toast.LENGTH_LONG).show();

				else if( reponseIsEmail.matches("0")){
		        	 Toast.makeText(view.getContext(), "Utilisateur inexistant.",
		                     Toast.LENGTH_LONG).show();
				}
				else if(contactExist){
					Toast.makeText(view.getContext(), "Contact déja ajouté",
		                     Toast.LENGTH_LONG).show();
				}
				else{
					ContactForm message = new ContactForm(edTxT.getText().toString());

					listContactPicker.add(message);
					contactFormAdapter.notifyDataSetChanged();

					contactBdd.open();
					contactBdd.insert(message);
					contactBdd.close();
					edTxT.setText("");

				}
				for( ContactForm test : listContactPicker){
					Log.i("", "Apres Add contact : "+test);
				}
			}
		});
		
	}

	public String checkEmailExist(String emailValue){
        AsyncIsEmail dFEm = new AsyncIsEmail();
        dFEm.delegate = this;
        String reponseIsEmail = null;
		try {
			reponseIsEmail = dFEm.execute(ApplicationConstants.APP_SERVER_URL,token,emailValue).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("", "Frag3 isEmail reponse "+reponseIsEmail);
		
		return reponseIsEmail;
	}
	
	

   
	@Override
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}
	
	
}