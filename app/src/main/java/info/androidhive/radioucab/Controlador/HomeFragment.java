package info.androidhive.radioucab.Controlador;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import info.androidhive.radioucab.R;

public class HomeFragment extends ListFragment {
	
	public HomeFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    try {
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("LTorresOrtiz")
                .includeRetweets(true)
                .maxItemsPerRequest(20)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);
        setListAdapter(adapter);
    }
    catch (Exception e) {
        int x = 0;
    }
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
         
        return rootView;
    }
}
