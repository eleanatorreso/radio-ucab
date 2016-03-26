package info.androidhive.radioucab.Conexiones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import info.androidhive.radioucab.R;

public class conexionPUTAPI extends AsyncTask<String, String, Integer> {
    ProgressDialog noticiaProgressDialog;
    public static Context contexto;
    public RespuestaAsyncTask delegate = null;
    public int tipo = 0;
    public JSONObject objeto;

    @Override
    protected Integer doInBackground(String... params) {
        String response;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 8000);
            HttpConnectionParams.setSoTimeout(httpParameters, 8000);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPut httpPut = new HttpPut(contexto.getResources().getString(R.string.ip_web_service) + params[0]);
            httpPut.setHeader("content-type", "application/json");
            StringEntity entity = new StringEntity(objeto.toString(),"UTF-8");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            httpPut.setEntity(entity);
            //httpPut.addHeader("Accept", "application/json");
            //httpPut.addHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPut);
            int code = httpResponse.getStatusLine().getStatusCode();
            String message = httpResponse.getStatusLine().getReasonPhrase();
            return code;
        }
        catch (Exception ex) {
            if (ex != null && ex.getMessage()!=null)
                Log.i("Conexion: ", ex.getMessage());
        }
        return 0;
    }

    @Override
    protected void onPreExecute() {
        try {
            super.onPreExecute();
        } catch (Exception ex) {
            Log.i("Conexion: ", ex.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Integer codigo) {
        super.onPostExecute(codigo);
        if (noticiaProgressDialog != null)
            noticiaProgressDialog.dismiss();
        if (codigo != 0) {
            try {
                delegate.procesoExitoso(codigo, tipo);
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
