package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import info.androidhive.radioucab.Conexiones.conexionPOSTAPIString;
import info.androidhive.radioucab.Conexiones.conexionPUTAPI;
import info.androidhive.radioucab.Controlador.GCM.RegistrationIntentService;
import info.androidhive.radioucab.Controlador.RegistroUsuarioFragment;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

public class UsuarioLogica implements RespuestaAsyncTask, RespuestaArchivoAsyncTask, RespuestaStringAsyncTask {

    private File rutaRadioUCAB;
    private final ManejoSesionTwitter sesionTwitter = new ManejoSesionTwitter();
    public Context contexto;
    public Usuario usuario;
    private ManejoString cambioUrl = new ManejoString();
    private String url;
    private boolean actualizar, usuarioNuevo;
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private final ProgramaLogica programaLogica = new ProgramaLogica();
    private final TagLogica tagLogica = new TagLogica();
    private final ConcursoLogica concursoLogica = new ConcursoLogica();
    private final ConfiguracionLogica configuracionLogica = new ConfiguracionLogica();

    public UsuarioLogica() {
    }

    public void iniciarSesion(JSONArray resultados, User usuarioResultado){
        final JSONObject resultado;
        try {
            resultado = resultados.getJSONObject(0);
            Usuario usuarioBD = new Usuario(resultado.getString("nombre"), resultado.getString("apellido"), resultado.getString("correo")
                    , resultado.getString("usuario_twitter"), resultado.getString("token_twitter"), resultado.getString("token_secret_twitter")
                    , resultado.getString("guid"), resultado.getString("imagen_normal"),  resultado.getString("imagen_grande")
                    , resultado.getInt("comentarios_inapropiados"), resultado.getBoolean("sancionado"));
            programaLogica.almacenarProgramasFavoritos(resultado.getJSONArray("programas_favoritos"));
            tagLogica.actualizarTags(resultado.getJSONArray("tags_actualizados"), resultado.getJSONArray("tags_usuario"));
            concursoLogica.almacenarMisConcursos(resultado.getJSONArray("concursos_participados"));
            configuracionLogica.almacenarConfiguracionBD(resultado.getInt("configuracion"));
            Usuario usuarioApp = new Usuario(usuarioResultado.screenName, sesionTwitter.getAuthToken().token
                    , sesionTwitter.getAuthToken().secret, usuarioResultado.profileImageUrl);
            manejoProgressDialog.iniciarProgressDialog(contexto.getString(R.string.dialogo_mensaje_iniciando_sesion),(Activity) contexto);
            comprobarUsuario(usuarioApp, usuarioBD);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void registrarUsuario(User usuarioResultado){
        Usuario nuevoUsuario = new Usuario(usuarioResultado.name, usuarioResultado.email, usuarioResultado.screenName,
                sesionTwitter.getAuthToken().token, sesionTwitter.getAuthToken().secret, usuarioResultado.profileImageUrl);
        RegistroUsuarioFragment registroUsuarioFragment = (RegistroUsuarioFragment) manejoActivity.cambiarFragment("Registro",true, true);
        registroUsuarioFragment.usuario = nuevoUsuario;
        manejoProgressDialog.cancelarProgressDialog();
    }

    public void crearUsuarioAPI() {
        conexionPOSTAPIString conexion = new conexionPOSTAPIString();
        conexion.contexto = contexto;
        conexion.delegate = (RespuestaStringAsyncTask) this;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("nombre", usuario.getNombre());
            objeto.put("apellido", usuario.getApellido());
            objeto.put("usuario_twitter", usuario.getUsuario_twitter());
            objeto.put("token_twitter", usuario.getToken_twitter());
            objeto.put("token_secret_twitter", usuario.getToken_secret_twitter());
            objeto.put("correo", usuario.getCorreo());
            objeto.put("imagen_grande", usuario.getImagen_grande());
            objeto.put("imagen_normal", usuario.getImagen_normal());
            objeto.put("tipo", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Usuario/Postusuario");
    }

    public void actualizarUsuarioAPI() {
        conexionPUTAPI conexion = new conexionPUTAPI();
        conexion.contexto = contexto;
        conexion.delegate = this;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("guid", usuario.getGuid());
            objeto.put("nombre", usuario.getNombre());
            objeto.put("apellido", usuario.getApellido());
            objeto.put("usuario_twitter", usuario.getUsuario_twitter());
            objeto.put("token_twitter", usuario.getToken_twitter());
            objeto.put("token_secret_twitter", usuario.getToken_secret_twitter());
            objeto.put("correo", usuario.getCorreo());
            objeto.put("imagen_grande", usuario.getImagen_grande());
            objeto.put("imagen_normal", usuario.getImagen_normal());
            objeto.put("tipo", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Usuario/Putusuario");
    }

    public void resetearUsuarioTelefono() {
        rutaRadioUCAB = new File(contexto.getString(R.string.ruta_archivos_radio_ucab));
        borrarCarpetaRecursivo(rutaRadioUCAB);
    }

    private void borrarCarpetaRecursivo(File fileOrDirectory) {
        try {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    borrarCarpetaRecursivo(child);
            fileOrDirectory.delete();
        } catch (Exception ex) {
            Log.e("Carpeta", ex.getMessage());
        }
    }

    public void manejoImagenes() {
        resetearUsuarioTelefono();
        cambioUrl = new ManejoString();
        url = cambioUrl.getURLImagen(usuario.getImagen_normal());
        ManejoArchivos almacenarFotoGrande = new ManejoArchivos();
        almacenarFotoGrande.delegate = (RespuestaArchivoAsyncTask) this;
        almacenarFotoGrande.tipo_foto = 0;
        almacenarFotoGrande.execute(url, "picBig", cambioUrl.getFormatoImagen());
    }

    public void almacenarUsuarioContinuacion() {
        if (actualizar && usuarioNuevo) {
            crearUsuarioAPI();
        } else if (actualizar && !usuarioNuevo) {
            actualizarUsuarioAPI();
        } else {
            usuario.save();
            manejoActivity.registrarIdTelefono();
            manejoActivity.cambiarFragment("Perfil",false, false);
            manejoProgressDialog.cancelarProgressDialog();
        }
    }

    public void almacenarUsuario(boolean actualizar, boolean usuarioNuevo) {
        this.actualizar = actualizar;
        this.usuarioNuevo = usuarioNuevo;
        manejoImagenes();
    }

    public void comprobarUsuario(Usuario usuarioApp, Usuario usuarioBD) {
        //debo actualizar los token de session en la base de datos, aunque creo que esto no es valido, siempre mantiene los mismos
        //tokens
        actualizar = false;
        if (!usuarioApp.getToken_twitter().equals(usuarioBD.getToken_twitter())) {
            usuarioBD.setToken_twitter(usuarioApp.getToken_twitter());
            actualizar = true;
        }
        if (!usuarioApp.getToken_secret_twitter().equals(usuarioBD.getToken_secret_twitter())) {
            usuarioBD.setToken_secret_twitter(usuarioApp.getToken_secret_twitter());
            actualizar = true;
        }
        if (!usuarioApp.getImagen_normal().equals(usuarioBD.getImagen_normal())) {
            usuarioBD.setImagen_normal(usuarioApp.getImagen_normal());
            actualizar = true;
        }
        usuario = usuarioBD;
        almacenarUsuario(actualizar, false);
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        //me regresa el usuario almacenado
        JSONObject resultado = null;
        try {
            manejoProgressDialog.cancelarProgressDialog();
            resultado = resultados.getJSONObject(0);
            usuario.setGuid(resultado.getString("guid").replace("\"", ""));
            usuario.save();
            manejoActivity.registrarIdTelefono();
            manejoActivity.cambiarFragment("Perfil",false, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {
        if (codigo == 204) {
            usuario.save();
            manejoActivity.cambiarFragment("Perfil", false, false);
        }
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void resultadoProceso(int respuesta, int tipo) {
        if (tipo == 0) {
            ManejoArchivos almacenarFotoNormal = new ManejoArchivos();
            almacenarFotoNormal.delegate = this;
            almacenarFotoNormal.tipo_foto = 1;
            almacenarFotoNormal.execute(usuario.getImagen_normal(), "picNormal", cambioUrl.getFormatoImagen());
        } else {
            usuario.setImagen_grande(url);
            usuario.setFormato_imagen(cambioUrl.getFormatoImagen());
            almacenarUsuarioContinuacion();
        }
    }

    @Override
    public void procesoNoExitoso() {
        manejoToast.crearToast((Activity) contexto, contexto.getResources().getString(R.string.toast_error_general));
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(String resultado) {
        //me regresa el guid del usuario almacenado
        usuario.setGuid(resultado.replace("\"", ""));
        usuario.save();
        manejoActivity.cambiarFragment("Perfil", false, false);
        manejoProgressDialog.cancelarProgressDialog();
        manejoActivity.registrarIdTelefono();
    }

    @Override
    public void procesoNoExitosoString() {
        manejoToast.crearToast((Activity) contexto, contexto.getResources().getString(R.string.toast_error_general));
        manejoProgressDialog.cancelarProgressDialog();
    }
}
