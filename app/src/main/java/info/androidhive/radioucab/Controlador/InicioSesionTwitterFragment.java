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

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
import info.androidhive.radioucab.Logica.FabricLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoSesionTwitter;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Logica.UsuarioLogica;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;
import io.fabric.sdk.android.Fabric;

public class InicioSesionTwitterFragment extends Fragment implements RespuestaAsyncTask {

    private TwitterLoginButton loginButton;
    private View rootView;
    private FabricLogica fabric;
    private Toast toast;
    private User usuarioResultado;
    private final UsuarioLogica usuarioLogica = new UsuarioLogica();
    private final ManejoSesionTwitter sesionTwitter = new ManejoSesionTwitter();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public InicioSesionTwitterFragment() {
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
/*
    public void comprobarUsuarioAPI(String usuarioTwitter) {
        conexionGETAPIJSONObject conexion = new conexionGETAPIJSONObject();
        conexion.contexto = getActivity();
        conexion.mensaje = "Comprobando datos...";
        conexion.delegate = this;
        conexion.execute("Api/Usuario/Getusuario?usuarioTwitter=" + usuarioTwitter);
    }*/

    public void comprobarUsuarioAPI(String usuarioTwitter) {
        conexionGETAPIJSONArray conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.mensaje = "Comprobando datos...";
        conexion.delegate = this;
        conexion.execute("Api/Usuario/Getusuario?usuarioTwitter=" + usuarioTwitter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (Usuario.listAll(Usuario.class).isEmpty()) {
                loginButton = (TwitterLoginButton) getActivity().findViewById(R.id.twitter_login_button);
                manejoActivity.editarActivity(6, false);
                if (!Fabric.isInitialized()) {
                    fabric = fabric.getInstance();
                    fabric.context = getActivity();
                    fabric.initFabric();
                }
                usuarioLogica.contexto = getActivity();
                loginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(final Result<TwitterSession> result) {
                        TwitterSession session = result.data;
                        sesionTwitter.getAuthToken();
                        Twitter.getApiClient(session).getAccountService()
                                .verifyCredentials(true, false, new Callback<User>() {
                                    @Override
                                    public void failure(TwitterException e) {
                                    }

                                    @Override
                                    public void success(Result<User> userResult) {
                                        usuarioResultado = userResult.data;
                                        try {
                                            comprobarUsuarioAPI(usuarioResultado.screenName);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                    @Override
                    public void failure(com.twitter.sdk.android.core.TwitterException e) {
                        Log.d("TwitterKit", "Login with Twitter failure", e);
                    }
                });
            }
            else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("atras");
                PerfilFragment perfilFragment = new PerfilFragment();
                ft.replace(((ViewGroup) getView().getParent()).getId(), perfilFragment);
                ft.commit();
            }
        } catch (Exception e) {
            Log.e("IniSesTwit:onActiv", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        if (resultados != null && resultados.length() > 0) {
            JSONObject resultado = null;
            try {
                resultado = resultados.getJSONObject(0);
                Usuario usuarioBD = new Usuario(resultado.getString("nombre"), resultado.getString("apellido"), resultado.getString("correo")
                        , resultado.getString("usuario_twitter"), resultado.getString("token_twitter"), resultado.getString("token_secret_twitter")
                        , resultado.getString("guid"), resultado.getString("imagen_normal"),  resultado.getString("imagen_grande"));
                Usuario usuarioApp = new Usuario(usuarioResultado.screenName, sesionTwitter.getAuthToken().token
                        , sesionTwitter.getAuthToken().secret, usuarioResultado.profileImageUrl);
                usuarioLogica.comprobarUsuario(usuarioApp, usuarioBD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //cargo el perfil
        } else {
            Usuario nuevoUsuario = new Usuario(usuarioResultado.name, usuarioResultado.email, usuarioResultado.screenName,
                    sesionTwitter.getAuthToken().token, sesionTwitter.getAuthToken().secret, usuarioResultado.profileImageUrl);
            RegistroUsuarioFragment registroUsuarioFragment = (RegistroUsuarioFragment) manejoActivity.cambiarFragment("Registro");
            registroUsuarioFragment.usuario = nuevoUsuario;
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo) {

    }

    @Override
    public void procesoNoExitoso() {
        toast = Toast.makeText(getActivity(), "No es posible procesar su solicitud, intente más tarde", Toast.LENGTH_LONG);
        toast.show();
    }
}
