package listviews;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;
import form.ContactForm;

public class ContactFormAdapter extends ArrayAdapter<ContactForm>{
	private LayoutInflater inflater;
    List<ContactForm> message;
    public ContactFormAdapter(Activity activity, List<ContactForm> messages1){
    	super(activity, R.layout.contact_row, messages1);
        inflater = activity.getWindow().getLayoutInflater();
        message = messages1;
    }
    
	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	View v = inflater.inflate(R.layout.contact_row, null);
    	
    	TextView t = (TextView) v.findViewById(R.id.mailContact);
		t.setText(message.get(position).geteMail());
    	t = (TextView) v.findViewById(R.id.prenomContact);
		t.setText(message.get(position).getPrenom());
    	 t = (TextView) v.findViewById(R.id.nomContact);
		t.setText(message.get(position).getNom());

        return v;
    }
}
