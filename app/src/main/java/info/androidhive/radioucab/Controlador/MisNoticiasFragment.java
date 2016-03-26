package info.androidhive.radioucab.Controlador;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.List;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONObject;
import info.androidhive.radioucab.Conexiones.conexionPUTAPI;
import info.androidhive.radioucab.Controlador.Adaptor.AdaptadorMisNoticias;
import info.androidhive.radioucab.Logica.ActualizacionLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoFecha;
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.ManejoUsuarioActual;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Logica.TagLogica;
import info.androidhive.radioucab.Model.Actualizacion;
import info.androidhive.radioucab.Model.Tag;
import info.androidhive.radioucab.R;

public class MisNoticiasFragment extends Fragment implements RespuestaAsyncTask {

    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private ListView listaNoticias;
    private AdaptadorMisNoticias adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static int flag = 0;
    private ImageView iconoSinTag;
    private TextView textoSinTag;
    private TextView encabezadoSinNoticia;
    private TextView descripcionSinNoticia;
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private conexionGETAPIJSONArray conexion;
    private conexionGETAPIJSONObject conexionObjeto;
    private final ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private static Date ultimaActWS;
    private final TagLogica tagLogica = new TagLogica();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private static final ManejoFecha tiempoActual = new ManejoFecha();
    private ManejoUsuarioActual manejoUsuarioActual = ManejoUsuarioActual.getInstancia();
    private Button botonActualizar;

    public MisNoticiasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_noticias, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manejoActivity.mostrarBackToolbar();
        manejoActivity.registrarPantallaAnalytics("Mis noticias");
        listaNoticias = (ListView) getActivity().findViewById(R.id.lista_mis_noticias);
        iconoSinTag = (ImageView) getActivity().findViewById(R.id.imagen_sin_mis_noticias);
        iconoSinTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarUltimaActualizacion();
            }
        });
        textoSinTag = (TextView) getActivity().findViewById(R.id.texto_sin_mis_noticias);
        descripcionSinNoticia = (TextView) getActivity().findViewById(R.id.campo_descripcion_mis_noticias);
        encabezadoSinNoticia = (TextView) getActivity().findViewById(R.id.campo_encabezado_modulo_mis_noticias);
        botonActualizar = (Button) getActivity().findViewById(R.id.boton_actualizar_mis_noticias);
        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarMisNoticias();
            }
        });
        preCargarTags();
        if (flag == 0) {
            comprobarUltimaActualizacion();
            flag = 1;
        }
        manejoLista();
    }

    public void manejoLista() {
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_mis_noticias_swipe_refresh_layout);
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
        conexionObjeto.execute("Api/Tag/ultimaModificacionTag");
    }

    public void cargarNoticias() {
        manejoProgressDialog.iniciarProgressDialog("Cargando preferencias de noticia...", getActivity());
        conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.delegate = this;
        conexion.execute("Api/Tag/GetTags?guid=" + manejoUsuarioActual.getGuid());
    }

    public void actualizarMisNoticias() {
        if (manejoUsuarioActual.isCambioListaPreferenciasNoticias()) {
            manejoProgressDialog.iniciarProgressDialog("Almacenando las preferencias de noticia...", getActivity());
            final conexionPUTAPI conexion = new conexionPUTAPI();
            conexion.contexto = getActivity();
            conexion.delegate = this;
            conexion.objeto = convertirListaAJson(manejoUsuarioActual.getMisPreferenciasNoticias());
            conexion.execute("Api/Tag/PutTag");
        }
    }

    public JSONObject convertirListaAJson(List<Tag> list) {
        final JSONObject jResult = new JSONObject();
        final JSONArray jArray = new JSONArray();
        try {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jGroup = new JSONObject();
                jGroup.put("myId", list.get(i).getMyId());
                jGroup.put("nombre_tag", list.get(i).getNombre_tag());
                jGroup.put("usuario_tag", list.get(i).getUsuario_tag());
                jArray.put(jGroup);
            }
            jResult.put("tagsActualizados", jArray);
            jResult.put("guid", manejoUsuarioActual.getGuid());
            return jResult;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void comprobarCantidadTags() {
        if (listaNoticias.getCount() > 0) {
            iconoSinTag.setVisibility(View.GONE);
            textoSinTag.setVisibility(View.GONE);
            descripcionSinNoticia.setVisibility(View.VISIBLE);
            listaNoticias.setVisibility(View.VISIBLE);
            encabezadoSinNoticia.setVisibility(View.VISIBLE);
            botonActualizar.setVisibility(View.VISIBLE);
        } else {
            iconoSinTag.setVisibility(View.VISIBLE);
            textoSinTag.setVisibility(View.VISIBLE);
            descripcionSinNoticia.setVisibility(View.GONE);
            listaNoticias.setVisibility(View.GONE);
            encabezadoSinNoticia.setVisibility(View.GONE);
            botonActualizar.setVisibility(View.GONE);
        }
    }

    public void preCargarTags() {
        final List<Tag> misNoticiasAlmacenadas = Tag.listAll(Tag.class);
        if (misNoticiasAlmacenadas!= null && misNoticiasAlmacenadas.size() > 0) {
            try {
                adapter = new AdaptadorMisNoticias(getActivity(), R.layout.adapter_noticia, misNoticiasAlmacenadas);
                listaNoticias.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("Tags: precargando", e.getMessage());
            }
        }
        comprobarCantidadTags();
    }

    public void procesarResultados(JSONArray resultados) {
        actualizacionLogica.almacenarUltimaActualizacion(5, ultimaActWS);
        try {
            final JSONObject resultado = resultados.getJSONObject(0);
            tagLogica.actualizarTags(resultado.getJSONArray("tags_actualizados"), resultado.getJSONArray("tags_usuario"));
            adapter = new AdaptadorMisNoticias(getActivity(), R.layout.adapter_mis_noticias, Tag.listAll(Tag.class));
            listaNoticias.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ultimaActualizacion(JSONObject resultado) {
        try {
            final List<Actualizacion> listaActualizaciones = Actualizacion.listAll(Actualizacion.class);
            final List<Tag> listaTag = Tag.listAll(Tag.class);
            ultimaActWS = tiempoActual.convertirString(resultado.getString("fecha_actualizacion"));
            if (listaActualizaciones != null && listaActualizaciones.size() > 0) {
                Actualizacion ultimaActualizacion = listaActualizaciones.get(0);
                if (ultimaActualizacion.getActTag().equals(ultimaActWS) == true && listaTag != null && listaTag.size() > 0) {
                    manejoToast.crearToast(getActivity(), "Preferencias de noticias actualizadas");
                } else {
                    cargarNoticias();
                }
            } else {
                cargarNoticias();
            }
        } catch (Exception e) {
            Log.e("Tag: ultima act", e.getMessage());
        }
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        procesarResultados(resultados);
        try {
            manejoToast.crearToast(getActivity(), "Etiquetas nuevas");
            swipeRefreshLayout.setRefreshing(false);
            manejoProgressDialog.cancelarProgressDialog();
        } catch (Exception e) {
            Log.e("Tag: toast", e.getMessage());
        }
        comprobarCantidadTags();
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {
        ultimaActualizacion(resultado);
        swipeRefreshLayout.setRefreshing(false);
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {
        if (codigo == 204) {
            manejoProgressDialog.cancelarProgressDialog();
            manejoToast.crearToast(getActivity(), "Preferencias actualizadas");
            tagLogica.actualizarTagModificados();
            manejoActivity.cambiarFragment("Perfil",false);
        } else {
            manejoProgressDialog.cancelarProgressDialog();
            manejoToast.crearToast(getActivity(), "Error al almacenar las preferencias, intentelo más tarde");
        }
    }

    @Override
    public void procesoExitoso(String respuesta) {

    }

    @Override
    public void procesoNoExitoso() {
        try {
            manejoProgressDialog.cancelarProgressDialog();
            manejoToast.crearToast(getActivity(), "Error al actualizar las preferecias, intentelo más tarde");
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            Log.e("Noticias: noexit", e.getMessage());
        }
        comprobarCantidadTags();
    }

}
