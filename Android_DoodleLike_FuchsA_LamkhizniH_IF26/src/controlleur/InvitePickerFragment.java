package controlleur;

import java.util.ArrayList;
import java.util.List;
import modele.ContactForm;
import modele.InviteForm;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import dao.ContactsBDD;

// dans la creation de RDV fait apparaitre la liste des contact pour choisir caux que l'on invite

//Utiliser dans Fragment 2 pour inviter des contact  On affiche que les contact qui ,e sont pas deja invité

public class InvitePickerFragment  extends DialogFragment {  // Permet de selctionner les invités du rdv
	FragmentActivity fragmentActivity;

	ArrayList mSelectedItems;
	ListView myListInvites;

	List<InviteForm> listInvitesPicker ;
	InviteFormAdapter inviteFormAdapter;
	List<String> listItem, items;
	boolean boolItem;
	ContactsBDD contactBdd;
	String[] array ;
	InvitePickerFragment invitePickerFragment;

	public InvitePickerFragment(FragmentActivity fActv,ListView viewPagerAdapte,List<InviteForm> list, InviteFormAdapter adapt,ContactsBDD ctct) {
		fragmentActivity = fActv;
		myListInvites = viewPagerAdapte;
		listInvitesPicker=list;
		inviteFormAdapter = adapt;
		contactBdd  = ctct;
		listItem = new ArrayList<String>();
		invitePickerFragment =this;
		
		build();
	}
	public void  removeListString(String value){

		Log.i("","InvitePickerFragment invité removed : "+listItem.remove(value));

	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		contactBdd.open();
		List<ContactForm> listContactPicker = contactBdd.getAllComments(1);
		contactBdd.close();
		items= new ArrayList<String>();

		for(int i  = 1 ; i<listContactPicker.size(); i++){
			boolItem = true;
			for(String item : listItem){
				//Log.i("", i+" items deja la : "+item);
				if( listContactPicker.get(i).geteMail().equals(item)){
					boolItem = false;
				}

			}
			if(boolItem) 		// on ajoute  dans le choix que les contact pas deja invité
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
				for(InviteForm iv :listInvitesPicker){
					Log.i("", "InvitePickerFragment invité : "+iv.getNomInvite());
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

	


	private void build(){

		myListInvites.setAdapter(inviteFormAdapter);
		myListInvites.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 95));
		myListInvites.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				invitePickerFragment.removeListString(listInvitesPicker.get(position).getNomInvite());

				inviteFormAdapter.remove(listInvitesPicker.get(position));
				inviteFormAdapter.notifyDataSetChanged();
				return false;
			}

		});

	}
	public List<InviteForm> getListInvitesPicker() {
		return listInvitesPicker;
	}
	public void setListInvitesPicker(List<InviteForm> listInvitesPicker) {
		this.listInvitesPicker = listInvitesPicker;
	}
	public InviteFormAdapter getInviteFormAdapter() {
		return inviteFormAdapter;
	}
	public void setInviteFormAdapter(InviteFormAdapter inviteFormAdapter) {
		this.inviteFormAdapter = inviteFormAdapter;
	}


}
