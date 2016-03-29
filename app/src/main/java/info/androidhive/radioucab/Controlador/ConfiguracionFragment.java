package info.androidhive.radioucab.Controlador;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import info.androidhive.radioucab.Conexiones.conexionPUTAPI;
import info.androidhive.radioucab.Logica.ConfiguracionLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.ManejoUsuarioActual;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Model.Concurso;
import info.androidhive.radioucab.Model.Configuracion;
import info.androidhive.radioucab.R;

public class ConfiguracionFragment extends Fragment implements RespuestaAsyncTask {

    final private ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Switch configuracionUno;
    private Switch configuracionDos;
    private Button botonGuardar;
    final private ConfiguracionLogica configuracionLogica = new ConfiguracionLogica();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private ManejoUsuarioActual manejoUsuarioActual = ManejoUsuarioActual.getInstancia();
    private int valorNotificacion = 0;
    private int valorStreaming = 0;
    private static boolean almacenandoConfiguracion = false;

    public ConfiguracionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manejoActivity.editarActivity(7, false, "Configuracion", "Configuración");
        configuracionUno = (Switch) getActivity().findViewById(R.id.campo_switch_configuracion_uno);
        configuracionDos = (Switch) getActivity().findViewById(R.id.campo_switch_configuracion_dos);
        botonGuardar = (Button) getActivity().findViewById(R.id.boton_actualizar_configuracion);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                almacenarConfiguraciones();
            }
        });
        cargarConfiguraciones();
    }

    public void cargarConfiguraciones() {
        List<Configuracion> configuracionList = Configuracion.listAll(Configuracion.class);
        if (configuracionList != null && configuracionList.size() > 0) {
            if (configuracionList.get(0).getRecibirNotificaciones() == 1)
                configuracionUno.setChecked(true);
            else
                configuracionUno.setChecked(false);
            if (configuracionList.get(0).getUsoHighStreaming() == 1)
                configuracionDos.setChecked(true);
            else
                configuracionDos.setChecked(false);
        }
    }

    public void almacenarConfiguraciones() {
        if (!almacenandoConfiguracion) {
            almacenandoConfiguracion = true;
            if (configuracionUno.isChecked()) {
                valorNotificacion = 1;
            }
            if (configuracionDos.isChecked()) {
                valorStreaming = 1;
            }
            actualizarMisNoticias(valorNotificacion);
        }
        else {
            manejoToast.crearToast(getActivity(), getActivity().getResources().getString(R.string.campo_toast_almacenamiento_en_proceso));
        }
    }

    public void actualizarMisNoticias(int valorPushNotification) {
        manejoProgressDialog.iniciarProgressDialog(getActivity().getResources().getString(R.string.campo_dialogo_almacenar_configuraciones), getActivity());
        final conexionPUTAPI conexion = new conexionPUTAPI();
        conexion.contexto = getActivity();
        conexion.delegate = this;
        JSONObject objeto = new JSONObject();
        try {
            objeto.put("valorPushNotification", valorPushNotification);
            objeto.put("guid", manejoUsuarioActual.getGuid());
        } catch (JSONException e) {
            e.printStackTrace();
            almacenandoConfiguracion = false;
        }
        conexion.objeto = objeto;
        conexion.execute("Api/Usuario/PutConfiguracion");
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {

    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {
        if (codigo == 204) {
            configuracionLogica.almacenarConfiguracion(valorNotificacion, valorStreaming);
            manejoToast.crearToast(getActivity(), getActivity().getResources().getString(R.string.campo_toast_almacenar_configuraciones));
        }
        else {
            manejoToast.crearToast(getActivity(), getActivity().getResources().getString(R.string.campo_toast_error_almacenar_configuraciones));
        }
        almacenandoConfiguracion = false;
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(String respuesta) {

    }

    @Override
    public void procesoNoExitoso() {
        almacenandoConfiguracion = false;
        manejoProgressDialog.cancelarProgressDialog();
        manejoToast.crearToast(getActivity(), getActivity().getResources().getString(R.string.campo_toast_error_almacenar_configuraciones));
    }
}
