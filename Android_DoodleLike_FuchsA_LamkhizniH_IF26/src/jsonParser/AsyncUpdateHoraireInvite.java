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

import android.os.AsyncTask;
import android.util.Log;

public class AsyncUpdateHoraireInvite extends AsyncTask<String, Integer, String> {
    public IApiAccessResponse delegate=null;
    
	protected String doInBackground(String... urls) {
		
		Log.i("", "AsyncUpdateHoraireInvite  " );

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair( "token",urls[1] ) );                    
		params.add( new BasicNameValuePair( "Id_horaire",urls[2] ) );
		params.add( new BasicNameValuePair( "invitesinscrit",urls[3] ) );


		try{
			URI uri = new URI(  urls[0]+"updateinscrit.php" + "?" + URLEncodedUtils.format(params, "utf-8"));
			HttpUriRequest request = new HttpGet(uri);
			request.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			try {
				response = httpclient.execute(request);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.i("","AsyncUpdateHoraireInvite Bug reponse ",e);
			}
		}catch(Exception e){Log.i("","AsyncUpdateHoraireInvite bug  url ",e);}

		
		String responseString = "ok";
		Log.i("", "AsyncUpdateHoraireInvite rep "+response);

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
