package info.androidhive.radioucab.Logica;

import android.content.Context;
import android.widget.Toast;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionPOSTAPIJSONArray;
import info.androidhive.radioucab.Model.Usuario;

public class UsuarioLogica implements RespuestaAsyncTask {

    private final File rutaRadioUCAB = new File("/sdcard/.RadioUCAB/");
    private final ManejoSesionTwitter sesionTwitter = new ManejoSesionTwitter();
    public Context contexto;
    private Toast toast;
    public Usuario usuario;

    public void crearUsuarioAPI() {
        conexionPOSTAPIJSONArray conexion = new conexionPOSTAPIJSONArray();
        conexion.contexto = contexto;
        conexion.mensaje = "Enviando los datos...";
        conexion.delegate = this;
        conexion.execute("Api/Usuario/Postusuario");
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("nombre",usuario.getNombre());
            objeto.put("apellido",usuario.getApellido());
            objeto.put("usuario_twitter",usuario.getUsuario_twitter());
            objeto.put("token_twitter",usuario.getToken_twitter());
            objeto.put("token_secret_twitter",usuario.getToken_secret_twitter());
            objeto.put("correo",usuario.getCorreo());
            objeto.put("imagen",usuario.getImagen());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
    }

    public void manejoImagenes () {
        ManejoString cambioUrl = new ManejoString();
        String url = cambioUrl.getURLImagen(usuario.getImagen());
        ManejoArchivos almacenarFotoGrande = new ManejoArchivos();
        almacenarFotoGrande.execute(url, "picBig");
        ManejoArchivos almacenarFotoNormal = new ManejoArchivos();
        almacenarFotoNormal.execute(usuario.getImagen(), "picNormal");
        resetearUsuarioTelefono();
    }

    public void almacenarUsuario(boolean actualizar) {
        manejoImagenes ();
        Usuario.deleteAll(Usuario.class);
        usuario.save();
        if (actualizar)
            crearUsuarioAPI();
    }

    public void comprobarUsuario (Usuario usuarioApp, Usuario usuarioBD) {
        //debo actualizar los token de session en la base de datos, aunque creo que esto no es valido, siempre mantiene los mismos
        //tokens
        boolean actualizar = false;
        if (!usuarioApp.getToken_twitter().equals(usuarioBD.getToken_twitter())) {
            usuarioBD.setToken_twitter(usuarioApp.getToken_twitter());
            actualizar = true;
        }
        if (!usuarioApp.getToken_secret_twitter().equals(usuarioBD.getToken_secret_twitter())) {
            usuarioBD.setToken_secret_twitter(usuarioApp.getToken_secret_twitter());
            actualizar = true;
        }
        if (!usuarioApp.getImagen().equals(usuarioBD.getImagen())){
            usuarioBD.setImagen(usuarioApp.getImagen());
            actualizar = true;
        }
        usuario = usuarioBD;
        almacenarUsuario(actualizar);
    }

    public void resetearUsuarioTelefono() {
        Usuario.deleteAll(Usuario.class);
        borrarCarpetaRecursivo(rutaRadioUCAB);
    }

    private void borrarCarpetaRecursivo(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                borrarCarpetaRecursivo(child);
        fileOrDirectory.delete();
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {

    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(String resultado) {

    }

    @Override
    public void procesoNoExitoso() {

    }
}
