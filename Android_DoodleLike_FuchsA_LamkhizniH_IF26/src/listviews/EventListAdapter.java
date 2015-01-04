package listviews;

import java.util.List;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import form.InfoEvent;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventListAdapter extends ArrayAdapter<InfoEvent>{
    private LayoutInflater inflater;
    List<InfoEvent> messages;
    public EventListAdapter(Activity activity, List<InfoEvent> messages1){
    	super(activity, R.layout.list_meet_layout, messages1);
        inflater = activity.getWindow().getLayoutInflater();
        messages = messages1;
    }

    public EventListAdapter(Context fragmentTab1, List<InfoEvent> messages1) {
    	super(fragmentTab1, R.layout.list_meet_layout, messages1);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	View v = inflater.inflate(R.layout.list_meet_layout, null);
    	
    	TextView t = (TextView) v.findViewById(R.id.sujetList);
		//t.setText("Sujet");
    	t = (TextView) v.findViewById(R.id.descriptionList);
		//t.setText("Description");
    	 t = (TextView) v.findViewById(R.id.auteurList);
		//t.setText("?? date ??");

        return v;
    }

}