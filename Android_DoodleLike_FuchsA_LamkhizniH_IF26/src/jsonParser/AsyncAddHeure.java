package jsonParser;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncAddHeure extends AsyncTask<String, Integer, String> {
    public IApiAccessResponse delegate=null;

	protected String doInBackground(String... urls) {
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair( "token",urls[3] ) );                     
		params.add( new BasicNameValuePair( "id_date",urls[1] ) );
		params.add( new BasicNameValuePair( "valeur",urls[2] ) );

		try{
			URI uri = new URI(  urls[0]+"addHoraire.php" + "?" + URLEncodedUtils.format(params, "utf-8"));
			HttpUriRequest request = new HttpGet(uri);
			request.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			try {
				response = httpclient.execute(request);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.i("","AsynAddHoraire Bug reponse ",e);
			}
		}catch(Exception e){Log.i("","AsynAddHoraire bug  url ",e);}

		
		String responseString = "-1";


		try {
			responseString = EntityUtils.toString(response.getEntity());
		} catch (ParseException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		



		return responseString;
	}
	protected void onProgressUpdate(Integer... progress) {
	}
	protected void onPostExecute(HttpResponse result) {
		delegate.postResult(result);
	}

}