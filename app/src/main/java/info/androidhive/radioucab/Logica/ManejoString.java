package info.androidhive.radioucab.Logica;

import java.util.Arrays;

public class ManejoString {

    private String formatoImagen;

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
        String [] tipoArchivo = ultimoSegmento[1].split("\\.");
        formatoImagen = tipoArchivo[1];
        url[5] = ultimoSegmento[0] + "." + tipoArchivo[1];
        return formatoSplit(url);
    }

    public String getFormatoImagen() {
        return formatoImagen;
    }

    public void setFormatoImagen(String formatoImagen) {
        this.formatoImagen = formatoImagen;
    }
}
