package info.androidhive.radioucab.Logica;

import android.os.AsyncTask;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ManejoArchivos extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            InputStream input = url.openStream();
            try {
                //The sdcard directory e.g. '/sdcard' can be used directly, or
                //more safely abstracted with getExternalStorageDirectory()
                File storagePath = Environment.getExternalStorageDirectory();

                // create a File object for the parent directory
                File file = new File("/sdcard/.RadioUCAB/");
                // have the object build the directory structure, if needed.
                file.mkdirs();
                // create a File object for the output file
                File outputFile = new File(file,params[1] + ".jpeg");

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

        } catch (Exception e) {
            int x = 2;
            return 0;
        }
        return 1;
    }

    protected void onPostExecute(int result) {

    }
}
