package info.androidhive.radioucab.Controlador;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.widget.ProgressBar;
import info.androidhive.radioucab.R;

public class VeloApp extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3200;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velo_app);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_velo);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.azul_radio_ucab), android.graphics.PorterDuff.Mode.MULTIPLY);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(VeloApp.this, MainActivity.class);
                VeloApp.this.startActivity(mainIntent);
                VeloApp.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
