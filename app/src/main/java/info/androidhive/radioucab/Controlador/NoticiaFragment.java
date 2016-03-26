package info.androidhive.radioucab.Controlador;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
import info.androidhive.radioucab.Logica.ActualizacionLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.NoticiaLogica;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Controlador.Adaptor.AdaptadorNoticia;
import info.androidhive.radioucab.Model.Actualizacion;
import info.androidhive.radioucab.Model.Noticia;
import info.androidhive.radioucab.Logica.ManejoFecha;
import info.androidhive.radioucab.R;


public class NoticiaFragment extends ListFragment implements RespuestaAsyncTask {
    private ListView listaNoticias;
    private AdaptadorNoticia adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private conexionGETAPIJSONArray conexion;
    private conexionGETAPIJSONObject conexionObjeto;
    private static int flag = 0;
    private static Date ultimaActWS;
    private static final ManejoFecha tiempoActual = new ManejoFecha();
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private final ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private final NoticiaLogica noticiaLogica = new NoticiaLogica();
    private static int pagina = 1;
    private ImageView iconoSinNoticias;
    private TextView textoSinNoticia;
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private View footer;

    public NoticiaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View rootView = inflater.inflate(R.layout.fragment_noticias, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Noticias: onCreateView", e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(3, true, "Noticia", "Noticia");
        iconoSinNoticias = (ImageView) getActivity().findViewById(R.id.imagen_sin_noticias);
        iconoSinNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarUltimaActualizacion();
            }
        });
        textoSinNoticia = (TextView) getActivity().findViewById(R.id.texto_sin_noticias);
        listaNoticias = (ListView) getActivity().findViewById(android.R.id.list);
        listaNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Noticia item = (Noticia) parent.getItemAtPosition(position);
                NoticiaDetalleFragment noticiaDetalleFragment = (NoticiaDetalleFragment) manejoActivity.cambiarFragment("NoticiaDetalle", true);
                noticiaDetalleFragment.noticia = Noticia.findById(Noticia.class, item.getId());
            }
        });
        // Add footer view
        footer = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progressbar_footer, null, false);
        listaNoticias.addFooterView(footer);
        preCargarNoticias();
        if (flag == 0) {
            comprobarUltimaActualizacion();
            flag = 1;
        }
        manejoLista();
    }

    public void manejoLista(){
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                comprobarUltimaActualizacion();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.amarillo_ucab, R.color.azul_radio_ucab);
        listaNoticias.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int currentVisibleItemCount;
            private boolean isLoading = true;
            private int previousTotal = 0;
            private int pageCount = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                int threshold = 1;
                int count = listaNoticias.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listaNoticias.getLastVisiblePosition() >= count - threshold && pageCount < 2) {
                        Log.i("noticia", "loading more data");
                    } else {
                        Log.e("hide footer", "footer hide");
                        listaNoticias.removeFooterView(footer);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                boolean enable = false;
                if (listaNoticias != null && listaNoticias.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listaNoticias.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listaNoticias.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }

        });
    }

    public void comprobarUltimaActualizacion() {
        manejoProgressDialog.iniciarProgressDialog("Comprobando si hay actualizaciones disponibles...", getActivity());
        conexionObjeto = new conexionGETAPIJSONObject();
        conexionObjeto.contexto = getActivity();
        conexionObjeto.delegate = this;
        conexionObjeto.execute("Api/Noticia/ultimaModificacionNoticia");
    }

    public void cargarNoticias() {
        manejoProgressDialog.iniciarProgressDialog("Cargando noticias...", getActivity());
        conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.delegate = this;
        conexion.execute("Api/Noticia/GetNoticia?pagina=" + pagina);
    }

    public void comprobarCantidadNoticias(){
        if (listaNoticias.getCount() > 0) {
            iconoSinNoticias.setVisibility(View.GONE);
            textoSinNoticia.setVisibility(View.GONE);
            listaNoticias.setVisibility(View.VISIBLE);
        }
        else {
            iconoSinNoticias.setVisibility(View.VISIBLE);
            textoSinNoticia.setVisibility(View.VISIBLE);
            listaNoticias.setVisibility(View.INVISIBLE);
        }
    }

    public void preCargarNoticias() {
        final List<Noticia> noticiasAlmacenadas = Noticia.listAll(Noticia.class);
        if (noticiasAlmacenadas.size() > 0) {
            try {
                adapter = new AdaptadorNoticia(getActivity(), R.layout.adapter_noticia, noticiasAlmacenadas);
                listaNoticias.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("Noticias: precargando", e.getMessage());
            }
            comprobarCantidadNoticias();
        }
    }

    public void procesarResultados(JSONArray resultados) {
        actualizacionLogica.almacenarUltimaActualizacion(2, ultimaActWS);
        try {
            Noticia.deleteAll(Noticia.class);
            noticiaLogica.procesarResultados(resultados);
            adapter = new AdaptadorNoticia(getActivity(), R.layout.adapter_noticia, Noticia.listAll(Noticia.class));
            listaNoticias.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("Noticias:procesando", e.getMessage());
        }
    }

    public void ultimaActualizacion(JSONObject resultado) {
        try {
            final List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
            final List<Noticia> listaNoticias = Noticia.listAll(Noticia.class);
            ultimaActWS = tiempoActual.convertirString(resultado.getString("fecha_actualizacion"));
            if (listaActualizaciones != null && listaActualizaciones.size() > 0) {
                Actualizacion ultimaActualizacion = listaActualizaciones.get(0);
                if (ultimaActualizacion.getActNoticia().equals(ultimaActWS) && listaNoticias != null &&
                        listaNoticias.size() > 0) {
                    manejoToast.crearToast(getActivity(), "Noticias actualizadas");
                } else {
                    cargarNoticias();
                }
            } else {
                cargarNoticias();
            }
        } catch (Exception e) {
            Log.e("Noticias: ultima act", e.getMessage());
        }
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        procesarResultados(resultados);
        try {
            manejoToast.crearToast(getActivity(), "Noticias nuevas");
            swipeRefreshLayout.setRefreshing(false);
            manejoProgressDialog.cancelarProgressDialog();
        } catch (Exception e) {
            Log.e("Noticias: toast", e.getMessage());
        }
        comprobarCantidadNoticias();
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
            manejoToast.crearToast(getActivity(), "Error al actualizar las noticias, intentelo más tarde");
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: noexit", e.getMessage());
        }
        comprobarCantidadNoticias();
    }
}
