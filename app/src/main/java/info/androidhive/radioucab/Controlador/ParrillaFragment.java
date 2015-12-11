package info.androidhive.radioucab.Controlador;

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
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.RespuestaAsyncTask;
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
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cambio el color del toolbar superior
        manejoActivity.cambiarDeColor(2);
        manejoActivity.cambiarIconoMenu();
        listaHora = (ListView) rootView.findViewById(R.id.lista_hora);
        listaPrograma = (ListView) rootView.findViewById(R.id.lista_programa);
        cargarParrilla();
    }

    public static Date GetUTCdatetimeAsDate()
    {
        //note: doesn't check for null
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
        conexion = new conexionGETAPIJSONArray();
        conexion.contexto = getActivity();
        conexion.mensaje = "Cargando la programación de hoy...";
        conexion.delegate = this;
        TimeZone zonaHoraria = TimeZone.getDefault();
        conexion.execute("Api/Programa/GetContenido?GMT=" + zonaHoraria.getDisplayName(false, TimeZone.SHORT));
    }

    public void mostrarResultados(JSONArray resultados){
        horas = new ArrayList<String>();
        programas = new ArrayList<String>();
        for (int i = 0; i < resultados.length(); i++) {
            try {
                JSONObject objeto = resultados.getJSONObject(i);
                horas.add(objeto.getString("hora_inicio") + " - " + objeto.getString("hora_fin"));
                programas.add(objeto.getString("nombre"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapterHora = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,horas );
        listaHora.setAdapter(adapterHora);
        ArrayAdapter<String> adapterPrograma = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,programas);
        listaPrograma.setAdapter(adapterPrograma);
    }

    @Override
    public void procesoExitoso(JSONArray resultados) {
        mostrarResultados(resultados);
    }

    @Override
    public void procesoExitoso(JSONObject resultado) {

    }

    @Override
    public void procesoExitoso(int codigo) {

    }

    @Override
    public void procesoNoExitoso() {

    }
}
