package controlleur;

//http://developer.android.com/training/articles/security-gms-provider.html

// Mise a jour du security provider de android 

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

// Met a  jour le security provider d'android si necessaire
public class SyncAdapter extends AbstractThreadedSyncAdapter {


	  public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		// TODO Auto-generated constructor stub
	}

	// This is called each time a sync is attempted; this is okay, since the
	  // overhead is negligible if the security provider is up-to-date.
	  @Override
	  public void onPerformSync(Account account, Bundle extras, String authority,
	      ContentProviderClient provider, SyncResult syncResult) {
	    try {
	      ProviderInstaller.installIfNeeded(getContext());
	    } catch (GooglePlayServicesRepairableException e) {

	      // Indicates that Google Play services is out of date, disabled, etc.

	      // Prompt the user to install/update/enable Google Play services.
	      GooglePlayServicesUtil.showErrorNotification(
	          e.getConnectionStatusCode(), getContext());

	      // Notify the SyncManager that a soft error occurred.
	      syncResult.stats.numIoExceptions++;
	      return;

	    } catch (GooglePlayServicesNotAvailableException e) {
	      // Indicates a non-recoverable error; the ProviderInstaller is not able
	      // to install an up-to-date Provider.

	      // Notify the SyncManager that a hard error occurred.
	      syncResult.stats.numAuthExceptions++;
	      return;
	    }

	    // If this is reached, you know that the provider was already up-to-date,
	    // or was successfully updated.
	  }
	}