package info.androidhive.radioucab.Controlador;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisProgramasFragment extends Fragment {
    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public MisProgramasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manejoActivity.mostrarBackToolbar();
        return inflater.inflate(R.layout.fragment_mis_programas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView1 = (ListView) getActivity().findViewById(R.id.listView1);

        String[] items = { "Programa 1", "Programa 2", "Top 40" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        listView1.setAdapter(adapter);
    }
}
