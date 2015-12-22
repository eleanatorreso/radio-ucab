package info.androidhive.radioucab.Controlador;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import info.androidhive.radioucab.Logica.FabricLogica;
import info.androidhive.radioucab.Logica.ManejoSesionTwitter;
import info.androidhive.radioucab.R;
import io.fabric.sdk.android.Fabric;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnviarTweet extends Fragment {

    private FabricLogica fabric;
    private final ManejoSesionTwitter sesionTwitter = new ManejoSesionTwitter();
    public String tweet;

    public EnviarTweet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enviar_tweet, container, false);
    }

    public void prueba() {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.update("hola prueba 2", null, null, null, null, null, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                int x = 1;
            }

            @Override
            public void failure(TwitterException e) {
                int x = 1;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Fabric.isInitialized()) {
            fabric = fabric.getInstance();
            fabric.initFabric();
        }
        prueba();
        /*TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                .text(tweet);
        builder.show();
        final Intent intent = new TweetComposer.Builder(getActivity())
                .text(tweet)
                .createIntent();
        startActivityForResult(intent, 1);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {

        }
    }
}
