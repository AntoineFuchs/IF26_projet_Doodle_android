package controlleur;


import java.util.List;

import modele.InviteForm;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//Utiliser dans Fragment 2  pour gérer la liste des invités

public class InviteFormAdapter extends ArrayAdapter<InviteForm>{
    View vue;
	private LayoutInflater inflater;
    List<InviteForm> messages;
    
    public InviteFormAdapter(Activity activity, List<InviteForm> messages1){
    	super(activity, R.layout.invite_form_row,messages1);
    	vue = null;
        inflater = activity.getWindow().getLayoutInflater();
        messages = messages1;
    }


	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	View v = inflater.inflate(R.layout.invite_form_row, null);
    	
    	TextView t = (TextView) v.findViewById(R.id.nomInvite);
    	t.setHeight(35);
		t.setText(messages.get(position).getNomInvite());


        return v;
    }

}