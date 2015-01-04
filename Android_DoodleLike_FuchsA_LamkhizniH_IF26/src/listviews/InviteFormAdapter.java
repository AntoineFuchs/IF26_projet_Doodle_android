package listviews;


import java.util.List;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import form.InviteForm;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InviteFormAdapter extends ArrayAdapter<InviteForm>{
    View vue;
	private LayoutInflater inflater;
    List<InviteForm> message;
    
    public InviteFormAdapter(Activity activity, List<InviteForm> messages1){
    	super(activity, R.layout.invite_form_row,messages1);
    	vue = null;
        inflater = activity.getWindow().getLayoutInflater();
        message = messages1;
    }


	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	View v = inflater.inflate(R.layout.invite_form_row, null);
    	
    	TextView t = (TextView) v.findViewById(R.id.nomInvite);
		t.setText(message.get(position).getNomInvite());


        return v;
    }

}