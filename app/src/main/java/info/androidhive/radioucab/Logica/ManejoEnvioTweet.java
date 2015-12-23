package info.androidhive.radioucab.Logica;

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
    public String fragmentoActual;
    public Comentario comentario;
    private final Usuario usuario = Usuario.listAll(Usuario.class).get(0);
    private final ManejoDialogs manejoDialogs = new ManejoDialogs();
    private final ManejoString manejoString = new ManejoString();
    private Toast toast;
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public ManejoEnvioTweet (Context contexto, Comentario comentario) {
        this.contexto = contexto;
        this.comentario = comentario;
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
                toast = Toast.makeText(contexto, "Su comentario ha sido procesado con exito", Toast.LENGTH_LONG);
                toast.show();
                manejoActivity.cambiarFragment(manejoActivity.getFragmentoActual());
            }

            @Override
            public void failure(TwitterException e) {
                respuesta[0] = false;
            }
        });
        return respuesta[0];
    }
/*
    //tipo 0
    public void verificarTweet() {
        conexionPUTAPI conexion = new conexionPUTAPI();
        conexion.contexto = contexto;
        conexion.mensaje = "Comprobando comentario...";
        conexion.delegate = this;
        conexion.tipo = 0;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("guid", usuario.getGuid());
            objeto.put("token_twitter", usuario.getToken_twitter());
            objeto.put("token_secret_twitter", usuario.getToken_secret_twitter());
            objeto.put("comentario", comentario.getComentario());
            objeto.put("finalidad", comentario.getFinalidad());
            objeto.put("idPrograma", comentario.getIdPrograma());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Comentario/Postcomentario");
    }*/

    //tipo 0
    public void verificarTweet() {
        conexionPOSTAPIString conexion = new conexionPOSTAPIString();
        conexion.contexto = contexto;
        conexion.mensaje = "Comprobando comentario...";
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Comentario/Postcomentario");
    }

    //tipo 1
    private void enviarIdTweet() {
        conexionPUTAPI conexion = new conexionPUTAPI();
        conexion.contexto = contexto;
        conexion.mensaje = "Publicando comentario...";
        conexion.delegate = this;
        conexion.tipo = 1;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("guid", usuario.getGuid());
            objeto.put("token_twitter", usuario.getToken_twitter());
            objeto.put("token_secret_twitter", usuario.getToken_secret_twitter());
            objeto.put("comentario", comentario.getComentario());
            objeto.put("finalidad", comentario.getFinalidad());
            objeto.put("idTwitter", comentario.getIdTweet());
            objeto.put("inapropiado", comentario.isInapropiado());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Comentario/almacenarIdTwitter");
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
        //respuesta sobre chequeo de comentario
    }

    @Override
    public void procesoNoExitoso() {
        toast = Toast.makeText(contexto, "No es posible procesar su solicitud, intente más tarde", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void procesoExitoso(String resultado) {
        if (manejoString.verificarEspacioNull(resultado)) {
            resultado = resultado.replace("\"", "");
            String[] split = resultado.split(":");
            int codigo = Integer.parseInt(split[0]);
            String hashtag = split[1];
            comentario.setComentario(comentario.getComentario().trim() + " #" + hashtag);
            if (codigo == 1) {
                //exitoso
                crearDialogoSiYNo(contexto.getString(R.string.dialogo_asunto_interaccion_exitoso), comentario.getComentario(),
                        contexto.getString(R.string.dialogo_mensaje_Si),
                        contexto.getString(R.string.dialogo_mensaje_No));

            } else if (codigo == 2) {
                //no puede comentar mas de 3 inapropiados
                usuario.setSancionado(true);
                usuario.setComentarios_inapropiados(3);
                usuario.save();
            } else if (codigo == 3) {
                //comentario inapropiado
                usuario.setComentarios_inapropiados(usuario.getComentarios_inapropiados() + 1);
                usuario.save();
            } else if (codigo == 4) {
                //comentario inapropiado, ud ha sido bloqueado
                usuario.setSancionado(true);
                usuario.setComentarios_inapropiados(3);
                usuario.save();
            } else if (codigo == 0) {
                //error intente de nuevo
            }
        }
    }

    @Override
    public void procesoNoExitosoString() {

    }
}
