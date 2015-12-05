package info.androidhive.radioucab.Conexiones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.R;

public class conexionPOSTAPIJSONArray extends AsyncTask<String, String, JSONArray> {
    ProgressDialog noticiaProgressDialog;
    public static Context contexto;
    public String mensaje = "";
    public RespuestaAsyncTask delegate = null;
    public int tipo = 0;
    public JSONObject objeto;

    @Override
    protected JSONArray doInBackground(String... params) {
        String response;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 8000);
            HttpConnectionParams.setSoTimeout(httpParameters, 8000);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(contexto.getResources().getString(R.string.ip_web_service) + params[0]);
            httpPost.setHeader("content-type", "application/json");
            StringEntity entity = new StringEntity(objeto.toString());
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            return new JSONArray(response);
        }
        catch (Exception ex) {
            if (ex != null && ex.getMessage()!=null)
                Log.i("Conexion: ", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        try {
            super.onPreExecute();
            if (!mensaje.isEmpty()) {
                noticiaProgressDialog = new ProgressDialog(contexto);
                noticiaProgressDialog.setMessage(mensaje);
                noticiaProgressDialog.setIndeterminate(true);
                noticiaProgressDialog.setCancelable(true);
                noticiaProgressDialog.show();
            }
        } catch (Exception ex) {
            int x = 2;
        }
    }

    @Override
    protected void onPostExecute(JSONArray resultados) {
        super.onPostExecute(resultados);
        if (noticiaProgressDialog != null)
            noticiaProgressDialog.dismiss();
        if (resultados != null) {
            try {
                delegate.procesoExitoso(resultados);
            } catch (Exception ex) {
                if (ex != null && ex.getMessage()!=null)
                    Log.i("Conexion: ", ex.getMessage());
                delegate.procesoNoExitoso();
            }
        } else {
            delegate.procesoNoExitoso();
        }
    }
}
