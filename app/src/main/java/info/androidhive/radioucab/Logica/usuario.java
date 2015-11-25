package info.androidhive.radioucab.Logica;

/**
 * Created by Eleana Torres on 17/11/2015.
 */
public class usuario {
    private static usuario ourInstance = new usuario();

    public static usuario getInstance() {
        return ourInstance;
    }

    private usuario() {
    }
}
