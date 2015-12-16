package info.androidhive.radioucab.Controlador;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.Toast;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;
import info.androidhive.radioucab.Controlador.GCM.RegistrationIntentService;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.R;

public class HomeFragment extends ListFragment {

    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public HomeFragment() {
    }


    public void prueba() {
        Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
        manejoActivity.getActivityPrincipal().startService(intent);
    }
/*
    public void cargarBotonInteraccion () {
        ImageButton imageButton = (ImageButton) getActivity().findViewById(R.id.boton_interaccion);
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // Or read size directly from the view's width/height
                int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
                outline.setOval(0, 0, size, size);
            }
        };
        imageButton.setOutlineProvider(viewOutlineProvider);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Juju",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(1, true);
        try {
            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("LTorresOrtiz")
                    .includeRetweets(true)
                    .maxItemsPerRequest(20)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);
            setListAdapter(adapter);
        } catch (Exception e) {
            int x = 0;
        }
        //prueba();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }

}
