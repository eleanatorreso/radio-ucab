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
import info.androidhive.radioucab.R;

public class UsuarioLogica implements RespuestaAsyncTask {

    private File rutaRadioUCAB;
    private final ManejoSesionTwitter sesionTwitter = new ManejoSesionTwitter();
    public Context contexto;
    private Toast toast;
    public Usuario usuario;

    public UsuarioLogica(){
    }

    public void crearUsuarioAPI() {
        conexionPOSTAPIJSONArray conexion = new conexionPOSTAPIJSONArray();
        conexion.contexto = contexto;
        conexion.mensaje = "Enviando los datos...";
        conexion.delegate = this;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("nombre",usuario.getNombre());
            objeto.put("apellido",usuario.getApellido());
            objeto.put("usuario_twitter",usuario.getUsuario_twitter());
            objeto.put("token_twitter",usuario.getToken_twitter());
            objeto.put("token_secret_twitter",usuario.getToken_secret_twitter());
            objeto.put("correo",usuario.getCorreo());
            objeto.put("imagen",usuario.getImagenGrande());
            objeto.put("tipo",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Usuario/Postusuario");
    }

    public void resetearUsuarioTelefono() {
        rutaRadioUCAB = new File(contexto.getString(R.string.ruta_archivos_radio_ucab));
        Usuario.deleteAll(Usuario.class);
        borrarCarpetaRecursivo(rutaRadioUCAB);
    }

    private void borrarCarpetaRecursivo(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                borrarCarpetaRecursivo(child);
        fileOrDirectory.delete();
    }

    public void manejoImagenes () {
        resetearUsuarioTelefono();
        ManejoString cambioUrl = new ManejoString();
        String url = cambioUrl.getURLImagen(usuario.getImagenNormal());
        ManejoArchivos almacenarFotoGrande = new ManejoArchivos();
        almacenarFotoGrande.execute(url, "picBig", cambioUrl.getFormatoImagen());
        ManejoArchivos almacenarFotoNormal = new ManejoArchivos();
        almacenarFotoNormal.execute(usuario.getImagenNormal(), "picNormal", cambioUrl.getFormatoImagen());
        usuario.setImagenGrande(url);
        usuario.setFormatoImagen(cambioUrl.getFormatoImagen());
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
        if (!usuarioApp.getImagenNormal().equals(usuarioBD.getImagenNormal())){
            usuarioBD.setImagenGrande(usuarioApp.getImagenNormal());
            actualizar = true;
        }
        usuario = usuarioBD;
        almacenarUsuario(actualizar);
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
