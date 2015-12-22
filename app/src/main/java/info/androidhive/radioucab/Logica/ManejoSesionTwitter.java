package info.androidhive.radioucab.Logica;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

public class ManejoSesionTwitter {

    private TwitterSession session;
    private TwitterAuthToken authToken;
    private FabricLogica fabric;

    public TwitterSession getSession() {
        if (session == null) {
            fabric = fabric.getInstance();
            fabric.initFabric();
            session = Twitter.getSessionManager().getActiveSession();
        }
            return session;
        }

    public TwitterAuthToken getAuthToken() {
        if (authToken == null)
            authToken = getSession().getAuthToken();
        return authToken;
    }

}

