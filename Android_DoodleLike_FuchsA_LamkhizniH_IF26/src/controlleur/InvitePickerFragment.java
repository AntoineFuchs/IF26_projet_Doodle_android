package controlleur;

import java.util.ArrayList;
import java.util.List;

import listviews.InviteFormAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import bdd.ContactsBDD;
import form.ContactForm;
import form.InviteForm;

// dans la creation de RDV fait apparaitre la liste des contact pour choisir caux que l'on invite


public class InvitePickerFragment  extends DialogFragment {
	ArrayList mSelectedItems;
	List<InviteForm> listInvitesPicker ;
	InviteFormAdapter inviteFormAdapter;
	 List<String> listItem, items;
	 boolean boolItem;
	ContactsBDD contactBdd;
	String[] array ;
	public InvitePickerFragment(List<InviteForm> list, InviteFormAdapter adapt,ContactsBDD ctct) {

		listInvitesPicker=list;
		inviteFormAdapter = adapt;
		contactBdd  = ctct;
		listItem = new ArrayList<String>();

	}
	public void  removeListString(String value){

		Log.i("","invité removed : "+listItem.remove(value));
		
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		contactBdd.open();
		List<ContactForm> listContactPicker = contactBdd.getAllComments();
		 contactBdd.close();
		items= new ArrayList<String>();

		for(int i  = 0 ; i<listContactPicker.size(); i++){
			boolItem = true;
			for(String item : listItem){
				Log.i("", i+" items deja la : "+item);
				if( listContactPicker.get(i).geteMail().equals(item)){
					boolItem = false;
				}

			}
			if(boolItem) 		
				items.add( listContactPicker.get(i).geteMail() );

		}
		
		 array = items.toArray(new String[items.size()]);

		
	    mSelectedItems = new ArrayList();  // Les items choisis
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("choix")

	    	
	    			.setMultiChoiceItems(array, null,
	                      new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   if (isChecked) {
	                       mSelectedItems.add(which);
	                   } else if (mSelectedItems.contains(which)) {
	                       mSelectedItems.remove(Integer.valueOf(which));
	                   }
	               }
	           })
	           .setPositiveButton("ok", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   for(int i = 0 ; i<mSelectedItems.size();i++){
	            		   listInvitesPicker.add(new InviteForm(array[(int) mSelectedItems.get(i)]));
	            		   listItem.add(array[(int) mSelectedItems.get(i)]);
	               		}
	               
	            	   inviteFormAdapter.notifyDataSetChanged();  // bouton Ok : mis a jours de la liste des invités en ajoutant ceux choisis
	            	   dialog.dismiss();
	               }
	           })
	           .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   dialog.dismiss();
	               }
	           });

	    return builder.create();
	}
}
