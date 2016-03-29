package info.androidhive.radioucab.Model;

public class Comentario {

    private String comentario;
    private int finalidad;
    private boolean inapropiado;
    private boolean sancionado;
    private Long idTweet;
    private int idPrograma;
    private int idConcurso;
    private String cancion;
    private String artista;
    private String receptor_dedicatoria;
    private String twitterLocutor;
    private int idLocutor;

    public Comentario(String comentario, int finalidad) {
        this.comentario = comentario;
        this.finalidad = finalidad;
        this.inapropiado = false;
        this.sancionado = false;
    }

    public Comentario(String comentario, int finalidad, String artista, String cancion) {
        this.comentario = comentario;
        this.finalidad = finalidad;
        this.artista = artista;
        this.cancion = cancion;
        this.inapropiado = false;
        this.sancionado = false;
    }

    public Comentario(String comentario, int finalidad, String artista, String cancion, String receptor_dedicatoria) {
        this.comentario = comentario;
        this.finalidad = finalidad;
        this.artista = artista;
        this.cancion = cancion;
        this.receptor_dedicatoria = receptor_dedicatoria;
    }

    public String getTwitterLocutor() {
        return twitterLocutor;
    }

    public void setTwitterLocutor(String twitterLocutor) {
        this.twitterLocutor = twitterLocutor;
    }

    public int getIdLocutor() {
        return idLocutor;
    }

    public void setIdLocutor(int idLocutor) {
        this.idLocutor = idLocutor;
    }

    public String getReceptor_dedicatoria() {
        return receptor_dedicatoria;
    }

    public void setReceptor_dedicatoria(String receptor_dedicatoria) {
        this.receptor_dedicatoria = receptor_dedicatoria;
    }

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getIdConcurso() {
        return idConcurso;
    }

    public void setIdConcurso(int idConcurso) {
        this.idConcurso = idConcurso;
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
