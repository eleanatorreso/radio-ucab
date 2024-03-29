package info.androidhive.radioucab.Conexiones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.R;

public class conexionGETAPIJSONObject extends AsyncTask<String, String, JSONObject> {
    public static Context contexto;
    public RespuestaAsyncTask delegate = null;
    public int tipo = 0;

    @Override
    protected JSONObject doInBackground(String... params) {
        String response;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 8000);
            HttpConnectionParams.setSoTimeout(httpParameters, 8000);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(contexto.getResources().getString(R.string.ip_web_service) + params[0]);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code != 200) {
                return null;
            }
            response = EntityUtils.toString(httpEntity);
            return new JSONObject(response);
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
            if (ex != null && ex.getMessage()!=null)
                Log.i("Conexion: ", ex.getMessage());
        }
    }

    @Override
    protected void onPostExecute(JSONObject resultado) {
        super.onPostExecute(resultado);
        if (resultado != null) {
            delegate.procesoExitoso(resultado);
        } else {
            delegate.procesoNoExitoso();
        }
    }
}
