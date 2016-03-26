package info.androidhive.radioucab.Controlador;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
import info.androidhive.radioucab.Controlador.Adaptor.AdaptadorMisConcursos;
import info.androidhive.radioucab.Logica.ConcursoLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.ManejoUsuarioActual;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Model.MiConcurso;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

public class MisConcursosFragment extends Fragment implements RespuestaAsyncTask {

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private AdaptadorMisConcursos adaptadorMisConcursos;
    private RecyclerView recyclerView;
    private View rootView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private ManejoUsuarioActual manejoUsuarioActual = ManejoUsuarioActual.getInstancia();
    private static int flag = 0;
    private ConcursoLogica concursoLogica = new ConcursoLogica();
    private ImageView imagenRefrescarSinConcursos;
    private TextView textoSinConcursos;

    public MisConcursosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_mis_concursos, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Programas: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            super.onCreate(savedInstanceState);
            manejoActivity.mostrarBackToolbar();
            manejoActivity.registrarPantallaAnalytics("Mis concursos");
            recyclerView = (RecyclerView) getActivity().findViewById(R.id.lista_recycler_mis_concursos);
            layoutManager = new LinearLayoutManager(getActivity());
            imagenRefrescarSinConcursos = (ImageView) getActivity().findViewById(R.id.imagen_sin_mis_concursos);
            imagenRefrescarSinConcursos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comprobarActualizaciones();
                }
            });
            textoSinConcursos = (TextView) getActivity().findViewById(R.id.texto_sin_mis_concursos);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adaptadorMisConcursos);
            manejoSwipeRefresh();
            cargarMisConcursosAlmacenados();
            if (flag == 0) {
                comprobarActualizaciones();
                flag = 1;
            }
        }
    }

    public void manejoSwipeRefresh(){
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_mis_concursos_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                comprobarActualizaciones();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.amarillo_ucab, R.color.azul_radio_ucab);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int currentVisibleItemCount;
            private boolean isLoading;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentVisibleItemCount = layoutManager.getChildCount();
                currentVisibleItemCount = layoutManager.getItemCount();
                currentFirstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                boolean enable = false;
                if (recyclerView != null && recyclerView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = layoutManager.findFirstVisibleItemPosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = recyclerView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);

                if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToEnd();
                } else if (dy < 0) {
                    onScrolledUp();
                } else if (dy > 0) {
                    onScrolledDown();
                }
            }


            public void onScrolledUp() {
            }

            public void onScrolledDown() {
            }

            public void onScrolledToEnd() {
                Log.v("Final", "EEEEH");
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public void cargarMisConcursosAlmacenados() {
        List<MiConcurso> misConcursos = MiConcurso.listAll(MiConcurso.class);
        cambioAdaptador(misConcursos);
    }

    private void cambioAdaptador(List<MiConcurso> concursos) {
        adaptadorMisConcursos = new AdaptadorMisConcursos(concursos, this);
        recyclerView.setAdapter(adaptadorMisConcursos);
        if (concursos != null && concursos.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            imagenRefrescarSinConcursos.setVisibility(View.GONE);
            textoSinConcursos.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            imagenRefrescarSinConcursos.setVisibility(View.VISIBLE);
            textoSinConcursos.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    public void comprobarActualizaciones() {
        manejoProgressDialog.iniciarProgressDialog("Comprobando si hay actualizaciones disponibles...", getActivity());
        conexionGETAPIJSONArray conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.delegate = this;
        conexion.execute("Api/Usuario/GetMisConcursos?usuarioTwitter=" + manejoUsuarioActual.getNombreUsuario());
    }

    public List<MiConcurso> procesarResultados(JSONArray resultadoConsulta) {
        List<MiConcurso> misConcursos = new ArrayList<MiConcurso>();
        try {
            misConcursos = concursoLogica.almacenarMisConcursos(resultadoConsulta);
        } catch (Exception excep) {
            Log.e("MisConcursos:procesa.", excep.getMessage());
        }
        return misConcursos;
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        if (resultados != null && resultados.length() > 0) {
            JSONObject arrayConcursosParticipados = null;
            try {
                arrayConcursosParticipados = resultados.getJSONObject(0);
                List<MiConcurso> misConcursos = procesarResultados(arrayConcursosParticipados.getJSONArray("concursos_participados"));
                cambioAdaptador(misConcursos);
                manejoProgressDialog.cancelarProgressDialog();
                manejoToast.crearToast(getActivity(), "Mis concursos están actualizados");
                swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {

    }

    @Override
    public void procesoExitoso(String respuesta) {

    }

    @Override
    public void procesoNoExitoso() {
        try {
            manejoProgressDialog.cancelarProgressDialog();
            manejoToast.crearToast(getActivity(), "Error al actualizar sus concursos, intentelo más tarde");
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: toast", e.getMessage());
        }
    }
}
