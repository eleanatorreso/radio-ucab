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
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Logica.RespuestaStringAsyncTask;
import info.androidhive.radioucab.R;

public class conexionPOSTAPIString extends AsyncTask<String, String, String> {
    public static Context contexto;
    //public String mensaje = "";
    public RespuestaStringAsyncTask delegate = null;
    public int tipo = 0;
    public JSONObject objeto;

    @Override
    protected String doInBackground(String... params) {
        String response;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 8000);
            HttpConnectionParams.setSoTimeout(httpParameters, 8000);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(contexto.getResources().getString(R.string.ip_web_service) + params[0]);
            StringEntity entity = new StringEntity(objeto.toString(), HTTP.UTF_8);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            return response;
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
        } catch (Exception ex) {
            int x = 2;
        }
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        if (resultado != null) {
            try {
                delegate.procesoExitoso(resultado);
            } catch (Exception ex) {
                if (ex != null && ex.getMessage()!=null)
                    Log.i("Conexion: ", ex.getMessage());
                delegate.procesoNoExitosoString();
            }
        } else {
            delegate.procesoNoExitosoString();
        }
    }
}
