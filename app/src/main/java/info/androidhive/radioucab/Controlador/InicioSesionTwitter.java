package info.androidhive.radioucab.Controlador;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
import info.androidhive.radioucab.Logica.FabricLogica;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Logica.UsuarioLogica;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;
import io.fabric.sdk.android.Fabric;

public class InicioSesionTwitter extends Fragment implements RespuestaAsyncTask {

    private TwitterLoginButton loginButton;
    private View rootView;
    private FabricLogica fabric;
    private Toast toast;

    public InicioSesionTwitter() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_inicio_sesion_twitter, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("IniSesTwit:onCreateV", e.getMessage());
        }
        return null;
    }

    public void comprobarUsuarioAPI(Usuario usuarioTwitter) {
        conexionGETAPIJSONObject conexion = new conexionGETAPIJSONObject();
        conexion.contexto = getActivity();
        conexion.mensaje = "Enviando los datos...";
        conexion.delegate = this;
        conexion.execute("Api/Usuario/Getusuario?usuarioTwitter=" + usuarioTwitter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            if (rootView != null) {
                super.onCreate(savedInstanceState);
                loginButton = (TwitterLoginButton) getActivity().findViewById(R.id.twitter_login_button);
                if (!Fabric.isInitialized()) {
                    fabric = fabric.getInstance();
                    fabric.context = getActivity();
                    fabric.initFabric();
                }
                loginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(final Result<TwitterSession> result) {
                        // The TwitterSession is also available through:
                        // Twitter.getInstance().core.getSessionManager().getActiveSession()
                        TwitterSession session = result.data;
                        Log.d("twittercommunity", "user's profile url is "
                                + session.getUserId());
                        // TODO: Remove toast and use the TwitterSession's userID
                        // with your app's user model
                        Twitter.getApiClient(session).getAccountService()
                                .verifyCredentials(true, false, new Callback<User>() {

                                    @Override
                                    public void failure(TwitterException e) {

                                    }

                                    @Override
                                    public void success(Result<User> userResult) {
                                        User usuarioResultado = userResult.data;
                                        try {
                                            Usuario nuevoUsuario = new Usuario();
                                            nuevoUsuario.setNombre(usuarioResultado.name);
                                            nuevoUsuario.setCorreo(usuarioResultado.email);
                                            nuevoUsuario.setUsuarioTwitter(result.data.getUserName());
                                            nuevoUsuario.setImagenPerfilURL(usuarioResultado.profileImageUrl);
                                            FragmentManager fm = getFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
                                            ft.addToBackStack("atras");
                                            CreacionUsuario b = new CreacionUsuario();
                                            b.usuario = nuevoUsuario;
                                            ft.replace(((ViewGroup) getView().getParent()).getId(), b);
                                            ft.commit();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void failure(com.twitter.sdk.android.core.TwitterException e) {
                        Log.d("TwitterKit", "Login with Twitter failure", e);
                    }
                });
            }
        } catch (Exception e) {
            int x = 1;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {

    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        try {
            if (resultado.getString("").equals("Si")) {
                //esto es temporal
                toast = Toast.makeText(getActivity(), "Este usuario ya existe", Toast.LENGTH_LONG);
                toast.show();

            }
            else {
                //debo ir a la ventana de edicion
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void procesoNoExitoso() {
        toast = Toast.makeText(getActivity(), "No es posible procesar su solicitud, intente más tarde", Toast.LENGTH_LONG);
        toast.show();
    }
}
