package info.androidhive.radioucab.Conexiones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.R;

public class conexionGETAPIString extends AsyncTask<String, String, String> {
    ProgressDialog noticiaProgressDialog;
    public static Context contexto;
    public String mensaje = "";
    public RespuestaAsyncTask delegate = null;
    public int tipo = 0;

    @Override
    protected String doInBackground(String... params) {
        String response;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 8000);
            HttpConnectionParams.setSoTimeout(httpParameters, 8000);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(contexto.getResources().getString(R.string.ip_web_service) + params[0]);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            return response;
        }
        catch (Exception ex) {
            ex.printStackTrace();
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
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        if (noticiaProgressDialog != null)
            noticiaProgressDialog.dismiss();
        if (resultado != null) {
            try {
                delegate.procesoExitoso(resultado);
            } catch (Exception e) {
                e.printStackTrace();
                delegate.procesoNoExitoso();
            }
        } else {
            delegate.procesoNoExitoso();
        }
    }
}
