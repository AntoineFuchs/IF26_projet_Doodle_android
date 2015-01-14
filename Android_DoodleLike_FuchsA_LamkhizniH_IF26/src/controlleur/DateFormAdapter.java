package controlleur;

import java.util.Arrays;
import java.util.List;
import modele.DateForm;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.TextView;
import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;


// Utiliser dans Fragment 2  pour géré les dates choisies
@SuppressLint({ "ViewHolder", "InflateParams" })
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
		t.setText(mois.get(Integer.valueOf(message.get(position).getMonth())));
    	 t = (TextView) v.findViewById(R.id.yearSelected);
		t.setText(message.get(position).getYear());
		 
		GridLayout  gridLayout = (GridLayout )v.findViewById(R.id.dateLinLayout);
		TextView myTxtView ;

		// on Modifie Dynamiquement param du gridLayout
		DisplayMetrics metrics = inflater.getContext().getResources().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int nbrcol = (int)(screenWidth/50)-1;
		gridLayout.setColumnCount(nbrcol);
		gridLayout.setRowCount(1);

		Log.i("", "DataFormAdapter nbr COlONE  : "+nbrcol);
		GridLayout.LayoutParams first;
		for(int i = 0,x=0,y=0; i<message.get(position).getHeuresForm().size();i++,y++){
			if( y==nbrcol){x++; y =0;gridLayout.setRowCount((int)(x+1)); 		Log.i("", "DataFormAdapter nbr Ligne  : "+(int)(x+1));}
			
			first = new GridLayout.LayoutParams(GridLayout.spec(x),GridLayout.spec(y));
			first.width = 50;
			first.height = 25;
			
			myTxtView = new TextView(v.getContext());
			myTxtView.setText(message.get(position).getHeureForm(i).getHeure()+"h"+message.get(position).getHeureForm(i).getMinute());
		    myTxtView.setGravity(Gravity.CENTER);
		    gridLayout.addView(myTxtView,first);
		 }

        return v;
    }

}