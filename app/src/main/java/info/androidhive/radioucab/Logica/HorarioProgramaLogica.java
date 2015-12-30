package info.androidhive.radioucab.Logica;

import java.util.ArrayList;
import java.util.List;
import info.androidhive.radioucab.Model.HorarioPrograma;


/**
 * Created by Eleana Torres on 30/12/2015.
 */
public class HorarioProgramaLogica {

    public List<HorarioPrograma> getHorarioPrograma (Long idPrograma) {
        List <HorarioPrograma> resultado = HorarioPrograma.find(HorarioPrograma.class, "programa = ?", String.valueOf(idPrograma));
        return resultado;
    }
}
