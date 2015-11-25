package info.androidhive.radioucab.Logica;

import java.util.Arrays;

public class ManejoString {

    private String formatoSplit (String [] url) {
        String nuevoUrl = "";
       nuevoUrl = url[0];
        for (int i=1; i < url.length; i++){
            nuevoUrl += "/" + url [i];
        }
        return nuevoUrl;
    }

    public String getURLImagen (String urlOriginal) {
        String [] url = urlOriginal.split("/");
        String [] ultimoSegmento = url[5].split("_");
        String [] tipoArchivo = ultimoSegmento[1].split(".");
        url[5] = ultimoSegmento[0] + ".jpeg";
        return formatoSplit(url);
    }
}
