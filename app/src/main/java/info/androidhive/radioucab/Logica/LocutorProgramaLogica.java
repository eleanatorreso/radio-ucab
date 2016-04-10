package info.androidhive.radioucab.Logica;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.LocutorPrograma;
import info.androidhive.radioucab.Model.Programa;

public class LocutorProgramaLogica {

    public void procesarLocutoresPrograma (List<Locutor> locutores, Programa programa) {
        LocutorPrograma nuevoObjeto = new LocutorPrograma();
        LocutorPrograma.deleteAll(LocutorPrograma.class);
        for (int i = 0; i < locutores.size(); i++) {
            nuevoObjeto = new LocutorPrograma(programa, locutores.get(i));
            nuevoObjeto.save();
        }
    }

    public List<Locutor> getLocutoresPrograma (Long idPrograma) {
        List <LocutorPrograma> resultado = LocutorPrograma.find(LocutorPrograma.class, "programa = ?", String.valueOf(idPrograma));
        List <Locutor> locutores = new ArrayList<>();
        for (LocutorPrograma locutorPrograma : resultado) {
            locutores.add(locutorPrograma.getLocutor());
        }
        return locutores;
    }
}

