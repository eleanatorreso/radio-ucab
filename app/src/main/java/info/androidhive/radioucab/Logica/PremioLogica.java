package info.androidhive.radioucab.Logica;

import java.util.List;

import info.androidhive.radioucab.Model.Premio;


/**
 * Created by Eleana Torres on 30/12/2015.
 */
public class PremioLogica {

    public List<Premio> getPremioConcurso (Long idConcurso) {
        List <Premio> resultado = Premio.find(Premio.class, "concurso = ?", String.valueOf(idConcurso));
        return resultado;
    }
}
