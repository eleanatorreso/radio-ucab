package info.androidhive.radioucab.Logica;

import android.content.Context;
import android.widget.Toast;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
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
            objeto.put("usuario_twitter",usuario.getUsuarioTwitter());
            objeto.put("mail",usuario.getCorreo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
    }

    public void almacenarUsuario(){
        TwitterSession session = sesionTwitter.getSession();
        TwitterAuthToken authToken = sesionTwitter.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        ManejoString cambioUrl = new ManejoString();
        String url = cambioUrl.getURLImagen(usuario.getImagenPerfilURL());
        ManejoArchivos almacenarFotoGrande = new ManejoArchivos();
        almacenarFotoGrande.execute(url, "picBig");
        ManejoArchivos almacenarFotoNormal = new ManejoArchivos();
        almacenarFotoNormal.execute(usuario.getImagenPerfilURL(), "picNormal");
        resetearUsuarioTelefono();
        //public Usuario(int myId, String nombre, String apellido, String correoUCAB, String usuarioTwitter, String tokenAccess, String token, String secret) {
       // Usuario usuario = new Usuario(0,"")
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
    public void procesoNoExitoso() {

    }
}
