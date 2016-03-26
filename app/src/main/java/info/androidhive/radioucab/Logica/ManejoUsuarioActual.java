package info.androidhive.radioucab.Logica;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Model.Tag;
import info.androidhive.radioucab.Model.Usuario;

public class ManejoUsuarioActual {

    private static ManejoUsuarioActual instancia;
    private static List<Tag> misPreferenciasNoticias;
    private static boolean cambioListaPreferenciasNoticias = false;

    public static ManejoUsuarioActual getInstancia() {
        if (instancia == null) {
            instancia = new ManejoUsuarioActual();
        }
        return instancia;
    }

    public static List<Tag> getMisPreferenciasNoticias() {
        if (misPreferenciasNoticias == null) {
            misPreferenciasNoticias = new ArrayList<Tag>();
        }
        return misPreferenciasNoticias;
    }

    public static void setMisPreferenciasNoticias(List<Tag> misPreferenciasNoticias) {
        ManejoUsuarioActual.misPreferenciasNoticias = misPreferenciasNoticias;
    }

    public static boolean isCambioListaPreferenciasNoticias() {
        return cambioListaPreferenciasNoticias;
    }

    public static void setCambioListaPreferenciasNoticias(boolean cambioListaPreferenciasNoticias) {
        ManejoUsuarioActual.cambioListaPreferenciasNoticias = cambioListaPreferenciasNoticias;
    }

    public String getNombreUsuario() {
        final List<Usuario> usuarioActual = Usuario.listAll(Usuario.class);
        if (usuarioActual.size() > 0) {
            return usuarioActual.get(0).getUsuario_twitter();
        }
        return "";
    }

    public String getGuid() {
        final List<Usuario> usuarioActual = Usuario.listAll(Usuario.class);
        if (usuarioActual.size() > 0) {
            return usuarioActual.get(0).getGuid();
        }
        return "";
    }

    private boolean comprobarTag(String nombre, int valorSeleccionado) {
        for (Tag tag : getMisPreferenciasNoticias()) {
            if (tag.getNombre_tag().equals(nombre)) {
                tag.setUsuario_tag(valorSeleccionado);
                return true;
            }
        }
        return false;
    }

    public void setValorLista(String nombre, int valorSeleccionado) {
        cambioListaPreferenciasNoticias = true;
        if (!comprobarTag(nombre, valorSeleccionado)) {
            final List<Tag> tagAlmacenados = Tag.find(Tag.class, "nombreTag = ?", nombre);
            if (tagAlmacenados != null && tagAlmacenados.size() > 0) {
                final Tag nuevoTag = tagAlmacenados.get(0);
                nuevoTag.setUsuario_tag(valorSeleccionado);
                getMisPreferenciasNoticias().add(nuevoTag);
            }
        }
    }
}
