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

    public HomeFragment() {
    }


    public void prueba() {
        Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
        manejoActivity.getActivityPrincipal().startService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(1, true, "Home");
        try {

            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("tweetsRadioUCAB")
                    .includeRetweets(true)
                    .maxItemsPerRequest(20)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);
            setListAdapter(adapter);

            final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_layout_home);
            swipeLayout.setColorSchemeResources(R.color.amarillo_ucab, R.color.azul_radio_ucab);
            timeLine = (ListView) getActivity().findViewById(android.R.id.list);
            timeLine.setOnScrollListener(new AbsListView.OnScrollListener() {
                private int currentScrollState;
                private int currentFirstVisibleItem;
                private int currentVisibleItemCount;
                private boolean isLoading = true;
                private int previousTotal = 0;
                private int pageCount = 0;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    this.currentFirstVisibleItem = firstVisibleItem;
                    this.currentVisibleItemCount = visibleItemCount;
                    boolean enable = false;
                    if (timeLine != null && timeLine.getChildCount() > 0) {
                        // check if the first item of the list is visible
                        boolean firstItemVisible = timeLine.getFirstVisiblePosition() == 0;
                        // check if the top of the first item is visible
                        boolean topOfFirstItemVisible = timeLine.getChildAt(0).getTop() == 0;
                        // enabling or disabling the refresh layout
                        enable = firstItemVisible && topOfFirstItemVisible;
                    }
                    swipeLayout.setEnabled(enable);
                }

            });
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                        @Override
                        public void success(Result<TimelineResult<Tweet>> result) {
                            swipeLayout.setRefreshing(false);
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            // Toast or some other action
                            swipeLayout.setRefreshing(false);
                            Toast toast = Toast.makeText(getActivity(), "No es posible procesar su solicitud, intente más tarde", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
            });
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
