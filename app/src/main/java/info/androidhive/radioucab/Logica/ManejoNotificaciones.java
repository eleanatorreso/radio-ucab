package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 07/06/2015.
 */
public class ManejoNotificaciones extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = (String) getIntent().getExtras().get("do_action");
        if (action != null) {
            if (action.equals("play")) {
                // for example play a music
            } else if (action.equals("close")) {
                // close current notification
            }
        }

        finish();
    }
}
