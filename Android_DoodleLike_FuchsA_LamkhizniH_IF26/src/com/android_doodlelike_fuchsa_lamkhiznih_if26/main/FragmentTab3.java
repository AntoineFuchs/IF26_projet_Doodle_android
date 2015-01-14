package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import java.util.List;
import listviews.ContactFormAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import bdd.ContactsBDD;
import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;
import form.ContactForm;

 
public class FragmentTab3 extends Fragment {
	View view;
	ListView myListContact;
	ContactFormAdapter contactFormAdapter;
	List<ContactForm> listContactPicker;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fragmenttab3.xml
		view = inflater.inflate(R.layout.fragmenttab3, container, false);


		
		
		interfaceBuilder(view);
		return view;
	}
	
	public void interfaceBuilder(View view) {
		// Fait le JSonarray des contacts
			ContactsBDD contactBdd  = new ContactsBDD(view.getContext());
			
			//ContactForm message = new ContactForm("trt","rze","erz");
			contactBdd.open();
			//livreBdd.insertLivre(message);
			
			 listContactPicker = contactBdd.getAllComments();
				myListContact=(ListView)view.findViewById(R.id.listContacts);

			 contactFormAdapter = new ContactFormAdapter(this.getActivity(),listContactPicker);
			 myListContact.setAdapter(contactFormAdapter);
			
			 contactBdd.close();
			 
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
	}
	

}