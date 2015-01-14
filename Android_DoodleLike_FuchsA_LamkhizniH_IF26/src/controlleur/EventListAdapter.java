package controlleur;

import java.util.List;

import modele.RdvForm;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


// Utilisé dans Frag 1 pour géré la liste des rdv
public class EventListAdapter extends ArrayAdapter<RdvForm>{
    private LayoutInflater inflater;
    List<RdvForm> messages;
    public EventListAdapter(Activity activity, List<RdvForm> messages1){
    	super(activity, R.layout.rdv_row, messages1);
        inflater = activity.getWindow().getLayoutInflater();
        messages = messages1;
    }

    public EventListAdapter(Context fragmentTab1, List<RdvForm> messages1) {
    	super(fragmentTab1, R.layout.rdv_row, messages1);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	View v = inflater.inflate(R.layout.rdv_row, null);
    	
       /*	TextView t = (TextView) v.findViewById(R.id.idRdv);
    	t.setText(""+messages.get(position).getId());*/
    	boolean checkDate = messages.get(position).getNewRDV().matches("0000-00-00 00:00:00") ;
        if( ! messages.get(position).getNewRDV().matches("") && ! checkDate ) v.setBackgroundColor(Color.parseColor("#f3f1ca"));
    	TextView t = (TextView) v.findViewById(R.id.sujetList);
		t.setText(messages.get(position).getTitre());
    	t = (TextView) v.findViewById(R.id.descriptionList);
		if( ! messages.get(position).getNewRDV().matches("") && !checkDate)
			t.setText("Fixé le : "+messages.get(position).getNewRDV()+" .\n "+messages.get(position).getDescription());
		else
			t.setText(messages.get(position).getDescription());
    	t = (TextView) v.findViewById(R.id.auteurList);
 		t.setText(messages.get(position).getCreateur());

        return v;
    }

}