package info.androidhive.radioucab.Logica;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by Administrator on 11/10/2015.
 */
public class ManejoSesionTwitter {

    private TwitterSession session;
    private TwitterAuthToken authToken;

    public TwitterSession getSession () {
        if (session == null)
            session = Twitter.getSessionManager().getActiveSession();
        return session;
    }

    public TwitterAuthToken getAuthToken () {
        if (authToken == null)
            authToken = getSession().getAuthToken();
        return authToken;
    }
}
