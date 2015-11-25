package info.androidhive.radioucab.Logica;


import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

public class FabricLogica {
    private static FabricLogica INSTANCE = null;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
 //  private static final String TWITTER_KEY = "98mLEbze7rMOPJx4a3OJQplQ8";
 //   private static final String TWITTER_SECRET = "XFn7RtWMzrEXP1TPgq4UUh2d11Q4dm5LDIu6snTG9Q6eNLF9b9";
    private static final String TWITTER_KEY = "to0vrfB5DrrYJHiBsumo2hK7b";
    private static final String TWITTER_SECRET = "KruDehvluC9Y6uQEmyQPoUu7elrbrmb7DZ44mQGQWkN3B8r5Hd";
    public static Context context;

    // Private constructor suppresses
    private FabricLogica(){}

    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciacion multiple
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FabricLogica();
        }
    }

    public static FabricLogica getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    public static void initFabric () {
        getInstance();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    public static void initFabricTweets () {
        getInstance();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new TwitterCore(authConfig), new TweetComposer());
    }
}
