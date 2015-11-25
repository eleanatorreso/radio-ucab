package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
import info.androidhive.radioucab.Controlador.Adaptor.AdaptadorPrograma;
import info.androidhive.radioucab.Logica.ActualizacionLogica;
import info.androidhive.radioucab.Logica.ProgramaLogica;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Model.Actualizacion;
import info.androidhive.radioucab.Logica.ManejoFecha;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class ProgramaFragment extends Fragment implements RespuestaAsyncTask {

    private RecyclerView recyclerView;
    private AdaptadorPrograma adaptadorProgramas;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private conexionGETAPIJSONObject conexionObjeto;
    private conexionGETAPIJSONArray conexionArray;
    private static int flag = 0;
    private static Date ultimaActWS;
    private View rootView;
    private static final ManejoFecha tiempoActual = new ManejoFecha();
    private Toast toast;
    private final ProgramaLogica programaLogica = new ProgramaLogica();
    private final ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private static int pagina = 1;

    public ProgramaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_programa, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Programas: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            if (rootView != null) {
                super.onCreate(savedInstanceState);
                recyclerView = (RecyclerView) rootView.findViewById(R.id.lista_recycler_programa);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adaptadorProgramas);
                swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_programas_swipe_refresh_layout);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refrescarContenido();
                    }
                });
                swipeRefreshLayout.setColorSchemeResources(R.color.amarillo_radio_ucab, R.color.azul_radio_ucab);
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
                List<Programa> programasAlmacenados = Programa.listAll(Programa.class);
                cambioAdaptador(programasAlmacenados);
                if (flag == 0) {
                    comprobarUltimaActualizacion();
                    flag = 1;
                }
            }
        } catch (Exception e) {
            Log.e("Programa: onactivity", e.getMessage());
        }
    }

    public void cargarProgramas() {
        conexionArray = new conexionGETAPIJSONArray();
        conexionArray.contexto = getActivity();
        conexionArray.mensaje = "Cargando los programas...";
        conexionArray.delegate = this;
        conexionArray.execute("Api/Programa/GetPrograma?pagina=" + pagina);
    }

    public void comprobarUltimaActualizacion() {
        conexionObjeto = new conexionGETAPIJSONObject();
        conexionObjeto.contexto = getActivity();
        conexionObjeto.delegate = this;
        conexionObjeto.tipo = 1;
        conexionObjeto.execute("Api/Programa/ultimaModificacionPrograma");
    }


    public List<Programa> procesarResultados(JSONArray resultadoConsulta) {
        actualizacionLogica.almacenarUltimaActualizacion(3, ultimaActWS);
        Programa.deleteAll(Programa.class);
        List<Programa> programas = new ArrayList<Programa>();
        try {
            programas = programaLogica.procesarResultados(resultadoConsulta);
        } catch (Exception excep) {
            Log.e("Programa:procesar resul", excep.getMessage());
        }
        return programas;
    }

    private void cambioAdaptador(List<Programa> programas) {
        adaptadorProgramas = new AdaptadorPrograma(programas, this);
        recyclerView.setAdapter(adaptadorProgramas);
    }

    public void refrescarContenido() {
        comprobarUltimaActualizacion();
    }

    public void ultimaActualizacion(JSONObject resultado) {
        String mensaje = "";
        try {
            List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
            ultimaActWS = tiempoActual.convertirString(resultado.getString("fecha_actualizacion"));
            if (listaActualizaciones != null && listaActualizaciones.size() > 0) {
                Actualizacion ultimaActualizacion = listaActualizaciones.get(0);
                if (ultimaActualizacion.getActPrograma().equals(ultimaActWS) == true) {
                    mensaje = "Programas Actualizados";
                    toast = Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    cargarProgramas();
                }
            } else {
                cargarProgramas();
                ;
            }
        } catch (Exception e) {
            Log.e("Programas: ultima act", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultadoConsulta) {
        List<Programa> resultadosProgramas = procesarResultados(resultadoConsulta);
        cambioAdaptador(resultadosProgramas);
        try {
            toast = Toast.makeText(getActivity(), "Programas nuevos", Toast.LENGTH_LONG);
            toast.show();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Programas: toast", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        ultimaActualizacion(resultado);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void procesoExitoso(String resultado) {

    }

    @Override
    public void procesoNoExitoso() {
        try {
            toast = Toast.makeText(getActivity(), "Error al actualizar los programas, intentelo mas tarde", Toast.LENGTH_LONG);
            toast.show();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: toast", e.getMessage());
        }
    }
}

