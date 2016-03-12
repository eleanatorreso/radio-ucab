package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import info.androidhive.radioucab.R;

public class AnalyticsApplication extends Application {
    private Tracker mTracker;

    public static AnalyticsApplication instancia;
    public static Activity contexto;

    public static AnalyticsApplication getInstancia(Activity activity) {
        if (instancia == null) {
            contexto = activity;
            instancia = new AnalyticsApplication();
        }
        return instancia;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(contexto);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}