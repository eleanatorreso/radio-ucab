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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.ManejoToast;
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
    private static int pagina = 1;
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private final EventoLogica eventoLogica = new EventoLogica();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private TextView textoSinEventos;
    private ImageView imagenSinEventos;
    private TextView textoEncabezado;

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

        if (rootView != null) {
            super.onCreate(savedInstanceState);
            //cambio el color del toolbar superior
            manejoActivity.editarActivity(4, true, "Evento", "Evento");
            recyclerView = (RecyclerView) rootView.findViewById(R.id.lista_recycler_evento);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adaptadorEvento);
            imagenSinEventos = (ImageView) getActivity().findViewById(R.id.imagen_sin_eventos);
            imagenSinEventos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comprobarUltimaActualizacion();
                }
            });
            textoSinEventos = (TextView) getActivity().findViewById(R.id.texto_sin_eventos);
            textoEncabezado = (TextView) getActivity().findViewById(R.id.titulo_modulo_eventos);
            manejoScroll();
            List<Evento> eventosAlmacenados = Evento.listAll(Evento.class);
            Collections.sort(eventosAlmacenados);
            ArrayList<ParentObject> eventos = generarEventos(eventosAlmacenados);
            cambioAdaptador(eventos);
            if (flag == 0) {
                comprobarUltimaActualizacion();
                flag = 1;
            }
        }

    }

    public void manejoScroll() {
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
    }


    public void cargarEventos() {
        manejoProgressDialog.iniciarProgressDialog("Cargando los próximos eventos...", getActivity());
        conexionGETAPIJSONArray conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.delegate = this;
        conexion.execute("Api/Evento/GetEvento?pagina=" + pagina);
    }

    public void comprobarUltimaActualizacion() {
        manejoProgressDialog.iniciarProgressDialog("Comprobando si hay actualizaciones disponibles...", getActivity());
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

    private void comprobarEventos(){
        final List<Evento> listaEventos = Evento.listAll(Evento.class);
        if (listaEventos != null && listaEventos.size() > 0){
            textoEncabezado.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            imagenSinEventos.setVisibility(View.GONE);
            textoSinEventos.setVisibility(View.GONE);
        }
        else {
            textoEncabezado.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            imagenSinEventos.setVisibility(View.VISIBLE);
            textoSinEventos.setVisibility(View.VISIBLE);
        }
    }

    private void cambioAdaptador(ArrayList<ParentObject> eventos) {
        adaptadorEvento = new AdaptadorEvento(getActivity(), eventos);
        adaptadorEvento.setCustomParentAnimationViewId(R.id.list_item_expand_arrow);
        adaptadorEvento.setParentClickableViewAnimationDefaultDuration();
        adaptadorEvento.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adaptadorEvento);
        comprobarEventos();
    }

    public void refrescarContenido() {
        comprobarUltimaActualizacion();
    }

    public void ultimaActualizacion(JSONObject resultado) {
        try {
            final List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
            final List<Evento> listaEventos = Evento.listAll(Evento.class);
            ultimaActWS = tiempoActual.convertirString(resultado.getString("fecha_actualizacion"));
            if (listaActualizaciones != null && listaActualizaciones.size() > 0) {
                Actualizacion ultimaActualizacion = listaActualizaciones.get(0);
                if (ultimaActualizacion.getActEvento().equals(ultimaActWS) && listaEventos != null
                        && listaEventos.size() > 0) {
                    manejoToast.crearToast(getActivity(), "Eventos actualizados");
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
            manejoToast.crearToast(getActivity(), "Eventos nuevas");
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Eventos: toast", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        ultimaActualizacion(resultado);
        swipeRefreshLayout.setRefreshing(false);
        manejoProgressDialog.cancelarProgressDialog();
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
            manejoToast.crearToast(getActivity(), "Error al actualizar los eventos, intentelo más tarde");
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: toast", e.getMessage());
        }
    }
}
