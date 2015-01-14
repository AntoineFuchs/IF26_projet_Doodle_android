package listviews;

import java.util.Arrays;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;
import form.DateForm;


public class DateFormAdapter extends ArrayAdapter<DateForm>{
	private LayoutInflater inflater;
    List<DateForm> message;
    List<String> mois = Arrays.asList("Janvier", "Février", "Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre");
    
    public DateFormAdapter(Activity activity, List<DateForm> messages1){
    	super(activity, R.layout.date_form_row,messages1);
        inflater = activity.getWindow().getLayoutInflater();
        message = messages1;
    }


	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	View v = inflater.inflate(R.layout.date_form_row, null);
    	
    	TextView t = (TextView) v.findViewById(R.id.daySelected);
		t.setText(message.get(position).getDay());
    	t = (TextView) v.findViewById(R.id.monthSelected);
		t.setText(mois.get(message.get(position).getMonth()));
    	 t = (TextView) v.findViewById(R.id.yearSelected);
		t.setText(message.get(position).getYear());
		 
		LinearLayout  linearLayout = (LinearLayout )v.findViewById(R.id.dateLinLayout);
		TextView myTxtView ;
		for(int i = 0; i<message.get(position).getHoursSize();i++){
			Log.i("r",message.get(position).getHours(i)+" \n");
		}
		for(int i = 0; i<message.get(position).getHoursSize();i++){
			myTxtView = new TextView(v.getContext());
			myTxtView.setText(message.get(position).getHours(i)+"h"+message.get(position).getMinutes(i));
		    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    llp.setMargins(6, 0, 0, 0);
		    myTxtView.setLayoutParams(llp);
		    linearLayout.addView(myTxtView);
		 }

        return v;
    }

}