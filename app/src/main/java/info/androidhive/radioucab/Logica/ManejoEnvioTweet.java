package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.radioucab.Conexiones.conexionPOSTAPIString;
import info.androidhive.radioucab.Conexiones.conexionPUTAPI;
import info.androidhive.radioucab.Model.Comentario;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

public class ManejoEnvioTweet implements RespuestaAsyncTask, RespuestaStringAsyncTask {
    public Context contexto;
    public Comentario comentario;
    private final Usuario usuario = Usuario.listAll(Usuario.class).get(0);
    private final ManejoString manejoString = new ManejoString();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private final ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private ManejoDialogs manejoDialogs;

    public ManejoEnvioTweet (Context contexto, Comentario comentario) {
        this.contexto = contexto;
        this.comentario = comentario;
    }

    public void clasificarInteraccion(){
        switch (comentario.getFinalidad()){
            //concurso
            case 1:
                manejoActivity.enviarInteraccion("Concurso");
                break;
            //dedicatoria
            case 2:
                manejoActivity.enviarInteraccion("Dedicatoria");
                break;
            //solicitud
            case 3:
                manejoActivity.enviarInteraccion("Solicitud");
                break;
            //comentario
            case 4:
                manejoActivity.enviarInteraccion("Comentario");
                break;
            //sugerencia
            case 5:
                manejoActivity.enviarInteraccion("Sugerencia");
                break;
            //queja
            case 6:
                manejoActivity.enviarInteraccion("Queja");
                break;
            //programa
            case 7:
                manejoActivity.enviarInteraccion("Programa");
                break;
        }
    }

    private boolean twitearComentario() {
        final boolean [] respuesta = new boolean[1];
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.update(comentario.getComentario(), null, null, null, null, null, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                respuesta[0] = true;
                comentario.setIdTweet(result.data.getId());
                enviarIdTweet();
                manejoActivity.cambiarFragment(manejoActivity.getFragmentoActual(), false, false);
                clasificarInteraccion();
            }

            @Override
            public void failure(TwitterException e) {
                respuesta[0] = false;
            }
        });
        return respuesta[0];
    }

    //tipo 0
    public void verificarTweet() {
        manejoProgressDialog.iniciarProgressDialog("Comprobando comentario...",(Activity)contexto);
        conexionPOSTAPIString conexion = new conexionPOSTAPIString();
        conexion.contexto = contexto;
        conexion.delegate = this;
        conexion.tipo = 0;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("guid", usuario.getGuid());
            objeto.put("token_twitter", usuario.getToken_twitter());
            objeto.put("token_secret_twitter", usuario.getToken_secret_twitter());
            objeto.put("comentario", comentario.getComentario());
            objeto.put("finalidad", comentario.getFinalidad());
            objeto.put("inapropiado", comentario.isInapropiado());
            objeto.put("idPrograma", comentario.getIdPrograma());
            objeto.put("idConcurso", comentario.getIdConcurso());
            objeto.put("artista", comentario.getArtista());
            objeto.put("cancion", comentario.getCancion());
            objeto.put("receptor_dedicatoria", comentario.getReceptor_dedicatoria());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Comentario/Postcomentario");
    }

    //tipo 1
    private void enviarIdTweet() {
        manejoProgressDialog.iniciarProgressDialog("Publicando comentario...",(Activity)contexto);
        conexionPUTAPI conexion = new conexionPUTAPI();
        conexion.contexto = contexto;
        conexion.delegate = this;
        conexion.tipo = 1;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("idTwitter", comentario.getIdTweet());
            objeto.put("guid", usuario.getGuid());
            objeto.put("token_twitter", usuario.getToken_twitter());
            objeto.put("token_secret_twitter", usuario.getToken_secret_twitter());
            objeto.put("comentario", comentario.getComentario());
            objeto.put("finalidad", comentario.getFinalidad());
            objeto.put("inapropiado", comentario.isInapropiado());
            objeto.put("idPrograma", comentario.getIdPrograma());
            objeto.put("idConcurso", comentario.getIdConcurso());
            objeto.put("artista", comentario.getArtista());
            objeto.put("cancion", comentario.getCancion());
            objeto.put("receptor_dedicatoria", comentario.getReceptor_dedicatoria());
            objeto.put("idLocutor", comentario.getIdLocutor());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Comentario/almacenarIdTwitter");
    }

    public boolean comprobarTamañoTweet(String tweet) {
        if (tweet.length() < 141) {
            return true;
        }
        return false;
    }

    private void crearDialogoSiYNo(String titulo, String mensaje, String botonPositivo, String botonNegativo) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setPositiveButton(botonPositivo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            twitearComentario();
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton(botonNegativo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            Dialog alerta = builder.create();
            alerta.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alerta.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {

    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {
        manejoProgressDialog.cancelarProgressDialog();
        //respuesta sobre chequeo de comentario
        /*
        if (tipo == 1){
            if (codigo == 204){
                manejoToast.crearToast((Activity)contexto, contexto.getString(R.string.toast_comentario_exitosos));
            }
            else {
                manejoToast.crearToast((Activity) contexto, contexto.getString(R.string.toast_error_general));
            }
        }*/
    }

    @Override
    public void procesoNoExitoso() {
        manejoToast.crearToast((Activity) contexto, contexto.getString(R.string.toast_error_general));
    }

    @Override
    public void procesoExitoso(String resultado) {
        if (manejoString.verificarEspacioNull(resultado)) {
            resultado = resultado.replace("\"", "");
            String[] split = resultado.split(":");
            int codigo = Integer.parseInt(split[0]);
            String hashtag = split[1];
            comentario.setComentario(comentario.getComentario().trim() + " #" + hashtag);
            if (comentario.getIdLocutor() != 0) {
                comentario.setComentario(comentario.getComentario().trim() + " @" + comentario.getTwitterLocutor());
            }
            if (codigo == 1) {
                //exitoso
                if (comprobarTamañoTweet(comentario.getComentario())) {
                    crearDialogoSiYNo(contexto.getString(R.string.dialogo_asunto_interaccion_exitoso), comentario.getComentario(),
                            contexto.getString(R.string.dialogo_mensaje_Si),
                            contexto.getString(R.string.dialogo_mensaje_No));
                }
                else {
                    manejoToast.crearToast((Activity) contexto, contexto.getString(R.string.toast_error_tamano_excedido));
                }
            } else if (codigo == 2) {
                //no puede comentar está sancionado
                usuario.setSancionado(true);
                usuario.save();
                manejoDialogs = new ManejoDialogs(contexto.getString(R.string.dialogo_asunto_error),
                        contexto.getString(R.string.dialogo_contenido_interaccion_no_exitoso_sancionado),
                        contexto.getString(R.string.dialogo_mensaje_Ok),contexto);
                manejoDialogs.crearDialogo(true, false);

            } else if (codigo == 3) {
                //comentario inapropiado
                usuario.setComentarios_inapropiados(usuario.getComentarios_inapropiados() + 1);
                usuario.save();
                manejoDialogs = new ManejoDialogs(contexto.getString(R.string.dialogo_asunto_error),
                        contexto.getString(R.string.dialogo_contenido_interaccion_no_exitoso_comentario_inapropiado),
                        contexto.getString(R.string.dialogo_mensaje_Ok),contexto);
                manejoDialogs.crearDialogo(true, false);
            } else if (codigo == 0) {
                //error intente de nuevo
                manejoToast.crearToast((Activity) contexto, contexto.getString(R.string.toast_error_general));
            }
        }
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoNoExitosoString() {
        manejoProgressDialog.cancelarProgressDialog();
    }
}
