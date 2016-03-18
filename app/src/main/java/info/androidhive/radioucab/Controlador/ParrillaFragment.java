package info.androidhive.radioucab.Controlador;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import info.androidhive.radioucab.Conexiones.conexionGETAPIJSONArray;
import info.androidhive.radioucab.Logica.ActualizacionLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoProgressDialog;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.ParillaLogica;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class ParrillaFragment extends Fragment implements RespuestaAsyncTask {

    private View rootView;
    private ListView listaHora;
    private ListView listaPrograma;
    private conexionGETAPIJSONArray conexion;
    private List<String> horas;
    private List<String> programas;
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ParillaLogica parrillaLogica = new ParillaLogica();
    private ActualizacionLogica actualizacionLogica = new ActualizacionLogica();
    private ManejoProgressDialog manejoProgressDialog = ManejoProgressDialog.getInstancia();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();

    public ParrillaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_parrilla, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Parrilla: onCreateView", e.getMessage());
        }
        manejoActivity.registrarPantallaAnalytics("Parrilla");
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambio el color del toolbar superior
        manejoActivity.editarActivity(2, true, "Parrilla");
        listaHora = (ListView) rootView.findViewById(R.id.lista_hora);
        listaPrograma = (ListView) rootView.findViewById(R.id.lista_programa);
        if (!parrillaLogica.comprobarActualizacionParrilla()) {
            cargarParrilla();
            actualizacionLogica.almacenarUltimaActualizacion(4, new Date());
        }
    }

    public static Date GetUTCdatetimeAsDate()
    {
        return StringDateToDate(GetUTCdatetimeAsString());
    }

    public static String GetUTCdatetimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }

    public static Date StringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return dateToReturn;
    }

    public void cargarParrilla() {
        manejoProgressDialog.iniciarProgressDialog("Cargando parrilla de programación...", getActivity());
        conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.delegate = this;
        TimeZone zonaHoraria = TimeZone.getDefault();
        conexion.execute("Api/Programa/GetContenido?GMT=" + zonaHoraria.getDisplayName(false, TimeZone.SHORT));
    }

    public void mostrarResultados(JSONArray resultados){
        horas = new ArrayList<String>();
        programas = new ArrayList<String>();
        List<Programa> parrilaDelDia = new ArrayList<Programa>();
        for (int i = 0; i < resultados.length(); i++) {
            try {
                JSONObject objeto = resultados.getJSONObject(i);
                horas.add(objeto.getString("hora_inicio") + " - " + objeto.getString("hora_fin"));
                programas.add(objeto.getString("nombre"));
                parrilaDelDia.add(new Programa(objeto.getString("nombre")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        parrillaLogica.setParrillaDelDia(parrilaDelDia);
        ArrayAdapter<String> adapterHora = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,horas );
        listaHora.setAdapter(adapterHora);
        ArrayAdapter<String> adapterPrograma = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,programas);
        listaPrograma.setAdapter(adapterPrograma);
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        mostrarResultados(resultados);
        manejoProgressDialog.cancelarProgressDialog();
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo, int tipo) {

    }

    @Override
    public void procesoNoExitoso() {
        try {
            manejoProgressDialog.cancelarProgressDialog();
            manejoToast.crearToast(getActivity(), "Error al actualizar la parrilla de programación, intentelo más tarde");
        } catch (Exception e) {
            Log.e("Noticias: noexit", e.getMessage());
        }
    }
}
