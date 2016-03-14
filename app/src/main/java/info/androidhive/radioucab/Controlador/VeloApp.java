package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.R;

public class VeloApp extends Activity {

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velo_app);
        ImageView imagen = (ImageView) findViewById(R.id.icono_principal);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VeloApp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
