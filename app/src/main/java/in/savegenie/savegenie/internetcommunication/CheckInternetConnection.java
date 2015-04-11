package in.savegenie.savegenie.internetcommunication;

public class CheckInternetConnection 
{
	private static CheckInternetConnection cic;

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */

	/*private CheckInternetConnection() {
		
	}

	public static CheckInternetConnection getAppHttpClientInstance() {
		if (cic == null) {
			cic = new CheckInternetConnection();
		}
		return cic;
	}

	public boolean isConnected()
	{
		ConnectivityManager cm =
		        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        return true;
		    }
		    return false;
		
	}*/
}
