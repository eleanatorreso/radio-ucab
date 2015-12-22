package info.androidhive.radioucab.Model;

public class Comentario {

    private String comentario;
    private int finalidad;
    private boolean inapropiado;
    private boolean sancionado;
    private Long idTweet;
    private int idPrograma;

    public Comentario(String comentario, int finalidad) {
        this.comentario = comentario;
        this.finalidad = finalidad;
        this.inapropiado = false;
        this.sancionado = false;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(int finalidad) {
        this.finalidad = finalidad;
    }

    public boolean isInapropiado() {
        return inapropiado;
    }

    public void setInapropiado(boolean inapropiado) {
        this.inapropiado = inapropiado;
    }

    public boolean isSancionado() {
        return sancionado;
    }

    public void setSancionado(boolean sancionado) {
        this.sancionado = sancionado;
    }

    public Long getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(Long idTweet) {
        this.idTweet = idTweet;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }
}
