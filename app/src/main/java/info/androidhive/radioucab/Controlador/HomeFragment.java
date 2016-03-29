package info.androidhive.radioucab.Controlador;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.List;

import info.androidhive.radioucab.Controlador.GCM.RegistrationIntentService;
import info.androidhive.radioucab.Logica.AnalyticsApplication;
import info.androidhive.radioucab.Logica.HashtagLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoEnvioTweet;
import info.androidhive.radioucab.Model.Hashtag;
import info.androidhive.radioucab.R;
import io.fabric.sdk.android.Fabric;

public class HomeFragment extends ListFragment {

    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private static final String TWITTER_KEY = "to0vrfB5DrrYJHiBsumo2hK7b";
    private static final String TWITTER_SECRET = "KruDehvluC9Y6uQEmyQPoUu7elrbrmb7DZ44mQGQWkN3B8r5Hd";
    private ListView timeLine;
    private View rootView;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_layout);
        try {

            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName(getActivity().getResources().getString(R.string.cuenta_twitter))
                    .includeRetweets(true)
                    .maxItemsPerRequest(20)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);
            setListAdapter(adapter);
            swipeLayout.setColorSchemeResources(R.color.amarillo_ucab, R.color.azul_radio_ucab);
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeLayout.setRefreshing(true);
                    adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                        @Override
                        public void success(Result<TimelineResult<Tweet>> result) {
                            swipeLayout.setRefreshing(false);
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            // Toast or some other action
                            swipeLayout.setRefreshing(false);
                        }
                    });
                }
            });

        } catch (Exception e) {
            int x = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            super.onCreate(savedInstanceState);
            manejoActivity.editarActivity(1, true, "Home","Home");
            manejoActivity.ocultarBackToolbar();
        }
    }

}
