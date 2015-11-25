package info.androidhive.radioucab.Controlador;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
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
    private Toast toast;
    private static Date ultimaActWS;
    private static final ManejoFecha tiempoActual = new ManejoFecha();

    public static final String[] descriptions = new String[]{
            "Descripcion 1",
            "Descripcion 2", "Descripcion 3",
            "Descripcion 4"};

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
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrescarContenido();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.amarillo_radio_ucab, R.color.azul_radio_ucab);
        listaNoticias = (ListView) getActivity().findViewById(android.R.id.list);
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
        listaNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Noticia item = (Noticia) parent.getItemAtPosition(position);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("atras");
                DetalleNoticiaFragment detalleNoticiaFragment = new DetalleNoticiaFragment();
                detalleNoticiaFragment.noticia = Noticia.findById(Noticia.class, item.getId());
                ft.replace(((ViewGroup) getView().getParent()).getId(), detalleNoticiaFragment);
                ft.commit();
            }
        });
        List<Noticia> noticiasAlmacenadas = Noticia.listAll(Noticia.class);
        preCargarNoticias(noticiasAlmacenadas);
        if (flag == 0) {
            comprobarUltimaActualizacion();
            flag = 1;
        }
    }
/*
    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.deta, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
*/
    public void comprobarUltimaActualizacion() {
        conexionObjeto = new conexionGETAPIJSONObject();
        conexionObjeto.contexto = getActivity();
        conexionObjeto.delegate = this;
        conexionObjeto.execute("Api/Noticia/ultimaModificacionNoticia");
    }

    public void cargarNoticias() {
        conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.mensaje = "Cargando las noticias...";
        conexion.delegate = this;
        conexion.execute("Api/Noticia/GetNoticia");
    }

    public void preCargarNoticias(List<Noticia> noticias) {
        try {
            adapter = new AdaptadorNoticia(getActivity(), R.layout.adapter_noticia, noticias);
            listaNoticias.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("Noticias: precargando", e.getMessage());
        }
    }

    public void almacenarUltimaAct() {
        try {
            ManejoFecha tiempoActual = new ManejoFecha();
            Actualizacion ultimaActualizacion = new Actualizacion();
            List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
            if (listaActualizaciones != null && listaActualizaciones.size() > 0) {
                ultimaActualizacion = listaActualizaciones.get(0);
                Date evento = ultimaActualizacion.getActEvento();
                Date parrilla = ultimaActualizacion.getActPrograma();
                Actualizacion.deleteAll(Actualizacion.class);
                Actualizacion nuevaActualizacion = new Actualizacion(evento, ultimaActWS, parrilla);
                nuevaActualizacion.save();
            } else {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date fechaFalsa = format.parse("01/01/1900 12:00:00");
                Actualizacion nuevaActualizacion = new Actualizacion(fechaFalsa, ultimaActWS, fechaFalsa);
                nuevaActualizacion.save();
            }
        } catch (Exception e) {
            Log.e("Noticias: ult.act", e.getMessage());
        }
    }

    public void procesarResultados(JSONArray resultado) {
        almacenarUltimaAct();
        Noticia noticiaNueva = new Noticia();
        try {
            Noticia.deleteAll(Noticia.class);
            for (int i = 0; i < resultado.length(); i++) {
                JSONObject objeto = resultado.getJSONObject(i);
                noticiaNueva = new Noticia(objeto.getInt("id"), objeto.getString("titular"), objeto.getString("texto_noticia"), objeto.getString("link"), objeto.getString("fuente"));
                noticiaNueva.save();
            }
            adapter = new AdaptadorNoticia(getActivity(), R.layout.adapter_noticia, Noticia.listAll(Noticia.class));
            listaNoticias.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("Noticias:procesando", e.getMessage());
        }
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
                if (ultimaActualizacion.getActNoticia().equals(ultimaActWS) == true) {
                    List<Noticia> a =Noticia.listAll(Noticia.class);
                    mensaje = "Noticias Actualizadas";
                    toast = Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    cargarNoticias();
                }
            } else {
                cargarNoticias();
            }
        } catch (Exception e) {
            Log.e("Noticias: ultima act", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        procesarResultados(resultados);
        try {
            toast = Toast.makeText(getActivity(), "Noticias nuevas", Toast.LENGTH_LONG);
            toast.show();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: toast", e.getMessage());
        }
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        ultimaActualizacion(resultado);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void procesoNoExitoso() {
        try {
            toast = Toast.makeText(getActivity(), "Error al actualizar las noticias, intentelo más tarde", Toast.LENGTH_LONG);
            toast.show();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: toast", e.getMessage());
        }
    }
}
