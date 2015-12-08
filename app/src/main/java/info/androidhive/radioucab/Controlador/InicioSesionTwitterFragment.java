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
        conexionGETAPIJSONObject conexion = new conexionGETAPIJSONObject();
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
/*

    @Override
    public void procesoExitoso(JSONArray resultados) {
        if (resultados != null && resultados.length() > 0) {
            //esto es temporal
            JSONObject resultado = null;
            try {
                resultado = resultados.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            toast = Toast.makeText(getActivity(), "Este usuario ya existe", Toast.LENGTH_LONG);
            toast.show();
            try {
                Usuario usuarioBD = new Usuario(resultado.getString("nombre"), resultado.getString("apellido"), resultado.getString("correo")
                        , resultado.getString("usuario_twitter"), resultado.getString("token_twitter"), resultado.getString("token_secret_twitter")
                        , resultado.getString("guid"), resultado.getString("imagen_normal"),  resultado.getString("imagen_grande"));
                Usuario usuarioApp = new Usuario(usuarioResultado.screenName, sesionTwitter.getAuthToken().token
                        , sesionTwitter.getAuthToken().secret, usuarioResultado.profileImageUrl);
                usuarioLogica.comprobarUsuario(usuarioApp, usuarioBD);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("atras");
                PerfilFragment perfilFragment = new PerfilFragment();
                ft.replace(((ViewGroup) getView().getParent()).getId(), perfilFragment);
                ft.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //cargo el perfil
        } else {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(usuarioResultado.name);
            nuevoUsuario.setCorreo(usuarioResultado.email);
            nuevoUsuario.setUsuario_twitter(usuarioResultado.screenName);
            nuevoUsuario.setImagenNormal(usuarioResultado.profileImageUrl);
            nuevoUsuario.setToken_secret_twitter(sesionTwitter.getAuthToken().secret);
            nuevoUsuario.setToken_twitter(sesionTwitter.getAuthToken().token);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack("atras");
            RegistroUsuarioFragment registroUsuarioFragment = new RegistroUsuarioFragment();
            registroUsuarioFragment.usuario = nuevoUsuario;
            ft.replace(((ViewGroup) getView().getParent()).getId(), registroUsuarioFragment);
            ft.commit();
        }
    }
*/
    @Override
    public void procesoExitoso(JSONArray resultados) {
        int x = 1;
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        if (resultado != null) {
            //esto es temporal
            toast = Toast.makeText(getActivity(), "Este usuario ya existe", Toast.LENGTH_LONG);
            toast.show();
            try {
                Usuario usuarioBD = new Usuario(resultado.getString("nombre"), resultado.getString("apellido"), resultado.getString("correo")
                        , resultado.getString("usuario_twitter"), resultado.getString("token_twitter"), resultado.getString("token_secret_twitter")
                        , resultado.getString("guid"), resultado.getString("imagen_normal"),  resultado.getString("imagen_grande"));
                Usuario usuarioApp = new Usuario(usuarioResultado.screenName, sesionTwitter.getAuthToken().token
                        , sesionTwitter.getAuthToken().secret, usuarioResultado.profileImageUrl);
                usuarioLogica.comprobarUsuario(usuarioApp, usuarioBD);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("atras");
                PerfilFragment perfilFragment = new PerfilFragment();
                ft.replace(((ViewGroup) getView().getParent()).getId(), perfilFragment);
                ft.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //cargo el perfil
        } else {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(usuarioResultado.name);
            nuevoUsuario.setCorreo(usuarioResultado.email);
            nuevoUsuario.setUsuario_twitter(usuarioResultado.screenName);
            nuevoUsuario.setImagenNormal(usuarioResultado.profileImageUrl);
            nuevoUsuario.setToken_secret_twitter(sesionTwitter.getAuthToken().secret);
            nuevoUsuario.setToken_twitter(sesionTwitter.getAuthToken().token);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack("atras");
            RegistroUsuarioFragment registroUsuarioFragment = new RegistroUsuarioFragment();
            registroUsuarioFragment.usuario = nuevoUsuario;
            ft.replace(((ViewGroup) getView().getParent()).getId(), registroUsuarioFragment);
            ft.commit();
        }
    }

    @Override
    public void procesoExitoso(String resultado) {

    }

    @Override
    public void procesoNoExitoso() {
        toast = Toast.makeText(getActivity(), "No es posible procesar su solicitud, intente más tarde", Toast.LENGTH_LONG);
        toast.show();
    }
}
