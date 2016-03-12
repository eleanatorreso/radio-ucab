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
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import info.androidhive.radioucab.Controlador.Adaptor.AdaptadorEvento;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
import info.androidhive.radioucab.Logica.ActualizacionLogica;
import info.androidhive.radioucab.Logica.EventoLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Model.Actualizacion;
import info.androidhive.radioucab.Model.Evento;
import info.androidhive.radioucab.R;
import info.androidhive.radioucab.Controlador.ViewHolder.EventoHijoViewHolder;
import info.androidhive.radioucab.Controlador.ViewHolder.EventoPadreViewHolder;
import info.androidhive.radioucab.Logica.ManejoFecha;


public class EventoFragment extends Fragment implements RespuestaAsyncTask {

    private RecyclerView recyclerView;
    private AdaptadorEvento adaptadorEvento;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private conexionGETAPIJSONObject conexionObjeto;
    private static int flag = 0;
    private static Date ultimaActWS;
    private View rootView;
    private static final ManejoFecha tiempoActual = new ManejoFecha();
    private Toast toast;
    private static int pagina = 1;
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private final EventoLogica eventoLogica = new EventoLogica();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_eventos, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Eventos: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            if (rootView != null) {
                super.onCreate(savedInstanceState);
                //cambio el color del toolbar superior
                manejoActivity.editarActivity(4, true, "Evento");
                recyclerView = (RecyclerView) rootView.findViewById(R.id.lista_recycler_evento);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adaptadorEvento);
                swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_eventos_swipe_refresh_layout);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refrescarContenido();
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
                List<Evento> eventosAlmacenados = Evento.listAll(Evento.class);
                Collections.sort(eventosAlmacenados);
                ArrayList<ParentObject> eventos = generarEventos(eventosAlmacenados);
                cambioAdaptador(eventos);
                if (flag == 0) {
                    comprobarUltimaActualizacion();
                    flag = 1;
                }
            }
        } catch (Exception e) {
            Log.e("Evento: onactivity", e.getMessage());
        }
    }

    public void cargarEventos() {
        conexionGETAPIJSONArray conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.mensaje = "Cargando los eventos...";
        conexion.delegate = this;
        conexion.execute("Api/Evento/GetEvento?pagina=" + pagina);
    }

    public void comprobarUltimaActualizacion() {
        conexionObjeto = new conexionGETAPIJSONObject();
        conexionObjeto.contexto = getActivity();
        conexionObjeto.delegate = this;
        conexionObjeto.tipo = 1;
        conexionObjeto.execute("Api/Evento/ultimaModificacionEvento");
    }

    public List<Evento> procesarResultados(JSONArray resultadoConsulta) {
        actualizacionLogica.almacenarUltimaActualizacion(1, ultimaActWS);
        List<Evento> eventos = eventoLogica.procesarResultados(resultadoConsulta);
        return eventos;
    }

    public ArrayList<ParentObject> generarEventos(List<Evento> resultadosEventos) {
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        for (Evento evento : resultadosEventos) {
            ArrayList<Object> listaHijosEvento = new ArrayList<>();
            listaHijosEvento.add(new EventoHijoViewHolder(evento.getDireccion(), evento.getHorario(), evento.getDescripcion()));
            EventoPadreViewHolder objeto = new EventoPadreViewHolder(listaHijosEvento, evento.getTitulo());
            parentObjects.add(objeto);
        }
        return parentObjects;
    }

    private void cambioAdaptador (ArrayList<ParentObject> eventos) {
        adaptadorEvento = new AdaptadorEvento(getActivity(), eventos);
        adaptadorEvento.setCustomParentAnimationViewId(R.id.list_item_expand_arrow);
        adaptadorEvento.setParentClickableViewAnimationDefaultDuration();
        adaptadorEvento.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adaptadorEvento);
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
                if (ultimaActualizacion.getActEvento().equals(ultimaActWS) == true) {
                    mensaje = "Eventos Actualizados";
                    toast = Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    cargarEventos();
                }
            } else {
                cargarEventos();
            }
        } catch (Exception e) {
            Log.e("Eventos: ultima act", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultadoConsulta) {
        List<Evento> resultadosEventos = procesarResultados(resultadoConsulta);
        ArrayList<ParentObject> eventos = generarEventos(resultadosEventos);
        cambioAdaptador(eventos);
        try {
            toast = Toast.makeText(getActivity(), "Eventos nuevos", Toast.LENGTH_LONG);
            toast.show();
            swipeRefreshLayout.setRefreshing(false);
        }
        catch (Exception e){
            Log.e("Eventos: toast", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        ultimaActualizacion(resultado);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {

    }

    @Override
    public void procesoNoExitoso() {
        try {
            toast = Toast.makeText(getActivity(), "Error al actualizar los eventos, intentelo más tarde", Toast.LENGTH_LONG);
            toast.show();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: toast", e.getMessage());
        }
    }
}
