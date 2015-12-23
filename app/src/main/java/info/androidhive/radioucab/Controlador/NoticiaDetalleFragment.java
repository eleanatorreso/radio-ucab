package info.androidhive.radioucab.Controlador;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Model.Noticia;
import info.androidhive.radioucab.R;

public class NoticiaDetalleFragment extends Fragment {

    public Noticia noticia;
    private TextView titulo;
    private TextView texto_noticia;
    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    public NoticiaDetalleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_noticia, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manejoActivity.editarActivity(3, true, null);
        titulo = (TextView) getView().findViewById(R.id.titulo_noticia);
        titulo.setText(noticia.getTitular());
        texto_noticia = (TextView) getView().findViewById(R.id.texto_noticia);
        texto_noticia.setText(noticia.getTexto_noticia());
    }
}
