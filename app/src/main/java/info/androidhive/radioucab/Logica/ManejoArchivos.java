package info.androidhive.radioucab.Logica;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ManejoArchivos extends AsyncTask<String, Void, Integer> {

    public RespuestaArchivoAsyncTask delegate = null;
    //0 foto grande, 1 foto pequeña
    public int tipo_foto;

    @Override
    protected Integer doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            InputStream input = url.openStream();
            try {
                File storagePath = Environment.getExternalStorageDirectory();
                File file = new File("/sdcard/.RadioUCAB/");
                file.mkdirs();
                File outputFile = new File(file,params[1] + "." + params[2]);

                OutputStream output = new FileOutputStream(outputFile);
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                } finally {
                    output.close();
                }
            } finally {
                input.close();
            }

        } catch (Exception ex) {
            if (ex != null && ex.getMessage()!=null)
                Log.i("Almacenamiento: ", ex.getMessage());
            return 0;
        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        delegate.resultadoProceso(result, tipo_foto);
    }
}
