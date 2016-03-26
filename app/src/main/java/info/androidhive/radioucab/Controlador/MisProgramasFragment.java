package info.androidhive.radioucab.Controlador;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ProgramaLogica;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.Model.ProgramaFavorito;
import info.androidhive.radioucab.R;

public class MisProgramasFragment extends Fragment {
    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private ListView listaMisProgramas;
    private TextView textoEncabezadoMisProgramas;
    private ImageView imagenSinMisProgramas;
    private TextView textoSinMisProgramas;
    private final ProgramaLogica programaLogica = new ProgramaLogica();

    public MisProgramasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_programas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        manejoActivity.mostrarBackToolbar();
        manejoActivity.registrarPantallaAnalytics("Mis programas favoritos");
        listaMisProgramas = (ListView) getActivity().findViewById(R.id.lista_mis_programas);
        listaMisProgramas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                programaLogica.ingresarDetallePrograma(listaMisProgramas.getItemAtPosition(position).toString());
            }
        });
        textoEncabezadoMisProgramas = (TextView) getActivity().findViewById(R.id.campo_encabezado_modulo_mis_programas);
        imagenSinMisProgramas = (ImageView) getActivity().findViewById(R.id.imagen_sin_mis_programas);
        textoSinMisProgramas = (TextView) getActivity().findViewById(R.id.texto_sin__mis_programas);
        cargarMisProgramas();
    }

    public void cargarMisProgramas() {
        List<ProgramaFavorito> misProgramas = ProgramaFavorito.listAll(ProgramaFavorito.class);
        List<String> programas = new ArrayList<String>();
        for (ProgramaFavorito programa : misProgramas) {
            programas.add(programa.getNombrePrograma());
        }
        ArrayAdapter<String> adapterPrograma = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, programas);
        listaMisProgramas.setAdapter(adapterPrograma);
        comprobarCantidadProgramas();
    }

    public void comprobarCantidadProgramas() {
        if (listaMisProgramas.getCount() > 0) {
            listaMisProgramas.setVisibility(View.VISIBLE);
            textoEncabezadoMisProgramas.setVisibility(View.VISIBLE);
            imagenSinMisProgramas.setVisibility(View.GONE);
            textoSinMisProgramas.setVisibility(View.GONE);
        } else {
            listaMisProgramas.setVisibility(View.GONE);
            textoEncabezadoMisProgramas.setVisibility(View.GONE);
            imagenSinMisProgramas.setVisibility(View.VISIBLE);
            textoSinMisProgramas.setVisibility(View.VISIBLE);
        }
    }
}
