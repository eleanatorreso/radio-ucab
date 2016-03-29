package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import info.androidhive.radioucab.Controlador.ConcursoFragment;
import info.androidhive.radioucab.Controlador.ConfiguracionFragment;
import info.androidhive.radioucab.Controlador.EditarPerfilFragment;
import info.androidhive.radioucab.Controlador.EditarTweetComentarioFragment;
import info.androidhive.radioucab.Controlador.EditarTweetDedicatoriaFragment;
import info.androidhive.radioucab.Controlador.EditarTweetPremiacionFragment;
import info.androidhive.radioucab.Controlador.EditarTweetProgramaFragment;
import info.androidhive.radioucab.Controlador.EditarTweetSolicitarFragment;
import info.androidhive.radioucab.Controlador.EnviarTweet;
import info.androidhive.radioucab.Controlador.EventoFragment;
import info.androidhive.radioucab.Controlador.HomeFragment;
import info.androidhive.radioucab.Controlador.InicioSesionTwitterFragment;
import info.androidhive.radioucab.Controlador.MainActivity;
import info.androidhive.radioucab.Controlador.MisConcursosFragment;
import info.androidhive.radioucab.Controlador.MisNoticiasFragment;
import info.androidhive.radioucab.Controlador.MisProgramasFragment;
import info.androidhive.radioucab.Controlador.NoticiaDetalleFragment;
import info.androidhive.radioucab.Controlador.NoticiaFragment;
import info.androidhive.radioucab.Controlador.ParrillaFragment;
import info.androidhive.radioucab.Controlador.PerfilFragment;
import info.androidhive.radioucab.Controlador.ProgramaDetalleFragment;
import info.androidhive.radioucab.Controlador.ProgramaFragment;
import info.androidhive.radioucab.Controlador.RegistroUsuarioFragment;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

public class ManejoActivity {

    private static ManejoActivity instancia;
    private static Activity activityPrincipal;
    private Toolbar toolbar;
    private ImageView imagen_perfil;
    private TextView boton_ingresar;
    private Usuario usuario_actual;
    private ImageView botonMenu;
    private final PerfilLogica perfilLogica = new PerfilLogica();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ImageButton boton_interaccion;
    private static String fragmentoActual;
    private ImageView boton_play;
    private ImageView boton_stop;
    private ImageView boton_pause;
    private ImageView ondas_on;
    private ImageView ondas_off;
    private ProgressBar progressStreaming;
    private ImageView icono_interaccion;
    private static int streaming = 4;
    private Tracker mTracker;
    private static MainActivity main;
    private static boolean almacenarProcesoActual;

    public static ManejoActivity getInstancia() {
        if (instancia == null) {
            instancia = new ManejoActivity();
        }
        return instancia;
    }

    public static MainActivity getInstanciaMain() {
        if (main == null) {
            main = new MainActivity();
        }
        return main;
    }

    public String getFragmentoActual() {
        return fragmentoActual;
    }

    public void setFragmentoActual(String fragmentoActual) {
        this.fragmentoActual = fragmentoActual;
    }

    public Activity getActivityPrincipal() {
        return activityPrincipal;
    }

    public static boolean isAlmacenarProcesoActual() {
        return almacenarProcesoActual;
    }

    public static void setAlmacenarProcesoActual(boolean almacenarProcesoActual) {
        ManejoActivity.almacenarProcesoActual = almacenarProcesoActual;
    }

    public void setActivityPrincipal(Activity activityPrincipal) {
        this.activityPrincipal = activityPrincipal;
        this.main = (MainActivity) activityPrincipal;
        toolbar = (Toolbar) activityPrincipal.findViewById(R.id.toolbar);
        perfilLogica.setContexto(activityPrincipal);
        boton_ingresar = (TextView) activityPrincipal.findViewById(R.id.boton_ingresar);
        imagen_perfil = (ImageView) activityPrincipal.findViewById(R.id.imagen_usuario);
        boton_interaccion = (ImageButton) activityPrincipal.findViewById(R.id.boton_interaccion);
        navMenuTitles = activityPrincipal.getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = activityPrincipal.getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) activityPrincipal.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) activityPrincipal.findViewById(R.id.list_slidermenu);
        boton_play = (ImageView) activityPrincipal.findViewById(R.id.icon_play);
        boton_stop = (ImageView) activityPrincipal.findViewById(R.id.icon_stop);
        boton_pause = (ImageView) activityPrincipal.findViewById(R.id.icon_pause);
        progressStreaming = (ProgressBar) activityPrincipal.findViewById(R.id.progressBar_streaming);
        ondas_off = (ImageView) activityPrincipal.findViewById(R.id.imagen_ondas_off);
        ondas_on = (ImageView) activityPrincipal.findViewById(R.id.imagen_ondas_on);
        icono_interaccion = (ImageView) activityPrincipal.findViewById(R.id.imagen_interaccion);
    }

    public void cambiarToolbar() {
        if (Usuario.listAll(Usuario.class) != null) {
            usuario_actual = Usuario.listAll(Usuario.class).get(0);
            boton_ingresar.setVisibility(View.INVISIBLE);
            imagen_perfil.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(activityPrincipal.getString(R.string.ruta_archivos_radio_ucab) + "picBig." +
                    usuario_actual.getFormato_imagen());
            imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap, 0));
            activityPrincipal.invalidateOptionsMenu();
        }
    }

    private void cambiarIconoMenu() {
        botonMenu = (ImageView) getActivityPrincipal().findViewById(R.id.icono_menu);
        botonMenu.setImageDrawable(getActivityPrincipal().getResources().getDrawable(R.drawable.ic_menu));
    }

    private void cambiarDeColor(int seccion) {
        int color = 0;
        switch (seccion) {
            //Home
            case 1:
                color = activityPrincipal.getResources().getColor(R.color.azul_radio_ucab);
                break;
            //que esta pasando hoy
            case 2:
                color = activityPrincipal.getResources().getColor(R.color.azul_radio_ucab);
                break;
            //noticias
            case 3:
                color = activityPrincipal.getResources().getColor(R.color.amarillo_ucab);
                break;
            //eventos
            case 4:
                color = activityPrincipal.getResources().getColor(R.color.naranja);
                break;
            //programas
            case 5:
                color = activityPrincipal.getResources().getColor(R.color.verde_ucab);
                break;
            //perfil
            case 6:
                color = activityPrincipal.getResources().getColor(R.color.azul_ucab);
                break;
            //configuracion
            case 7:
                color = activityPrincipal.getResources().getColor(R.color.gris_claro);
                break;

        }
        if (toolbar != null)
            toolbar.setBackgroundColor(color);
    }

    public void editarActivity(int seccion, boolean mostrarBotonInteraccion, String fragmentoActual, String nombreFragmento) {
        cambiarDeColor(seccion);
        cambiarIconoMenu();
        if (mostrarBotonInteraccion == true) {
            if (currentVersionL()) {
                boton_interaccion.setVisibility(View.VISIBLE);
            } else {
                icono_interaccion.setVisibility(View.VISIBLE);
            }
        } else {
            boton_interaccion.setVisibility(View.INVISIBLE);
            icono_interaccion.setVisibility(View.INVISIBLE);
        }
        if (fragmentoActual != null) {
            this.fragmentoActual = fragmentoActual;
        }
        registrarPantallaAnalytics(nombreFragmento);
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = (Fragment) new HomeFragment();
                break;
            case 1:
                fragment = new ParrillaFragment();
                break;
            case 2:
                fragment = new NoticiaFragment();
                break;
            case 3:
                fragment = new EventoFragment();
                break;
            case 4:
                fragment = new ProgramaFragment();
                break;
            case 5:
                fragment = new PerfilFragment();
                break;
            case 6:
                fragment = new ConfiguracionFragment();
                break;
            case 20:
                fragment = new RegistroUsuarioFragment();
                break;
            case 21:
                fragment = new InicioSesionTwitterFragment();
                break;
            case 22:
                fragment = new EditarPerfilFragment();
                break;
            case 23:
                fragment = new EnviarTweet();
                break;
            case 24:
                fragment = new EditarTweetComentarioFragment();
                break;
            case 25:
                fragment = new NoticiaDetalleFragment();
                break;
            case 26:
                fragment = new ProgramaDetalleFragment();
                break;
            case 27:
                fragment = new EditarTweetSolicitarFragment();
                break;
            case 28:
                fragment = new EditarTweetDedicatoriaFragment();
                break;
            case 29:
                fragment = new EditarTweetProgramaFragment();
                break;
            case 30:
                fragment = new EditarTweetPremiacionFragment();
                break;
            case 31:
                fragment = new ConcursoFragment();
                break;
            case 32:
                fragment = new MisConcursosFragment();
                break;
            case 33:
                fragment = new MisNoticiasFragment();
                break;
            case 34:
                fragment = new MisProgramasFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    public int getPosicion(String nombre_fragment) {
        switch (nombre_fragment) {
            case "Home":
                return 0;
            case "Parrilla":
                return 1;
            case "Noticia":
                return 2;
            case "Evento":
                return 3;
            case "Programa":
                return 4;
            case "Perfil":
                return 5;
            case "Configuracion":
                return 6;
            case "Registro":
                return 20;
            case "Inicio":
                return 21;
            case "EditarPerfil":
                return 22;
            case "EnviarTweet":
                return 23;
            case "TweetComentario":
                return 24;
            case "NoticiaDetalle":
                return 25;
            case "ProgramaDetalle":
                return 26;
            case "TweetSolicitud":
                return 27;
            case "TweetDedicatoria":
                return 28;
            case "TweetPrograma":
                return 29;
            case "TweetConcurso":
                return 30;
            case "ConcursoFragment":
                return 31;
            case "MisConcursosFragment":
                return 32;
            case "MisNoticiasFragment":
                return 33;
            case "MisProgramasFragment":
                return 34;
        }
        return 0;
    }

    public void mostrarBackToolbar() {
        botonMenu.setVisibility(View.INVISIBLE);
        main.mostrarBackToolbar();
    }

    public void mostrarCloseToolbar() {
        botonMenu.setVisibility(View.INVISIBLE);
        main.mostrarCloseToolbar();
    }

    public void ocultarBackToolbar() {
        if (botonMenu != null) {
            botonMenu.setVisibility(View.VISIBLE);
            main.ocultarOpcionToolbar();
        }
    }

    public void almacenarProcesoActual(boolean almacenarProcesoActual) {
        this.almacenarProcesoActual = almacenarProcesoActual;
    }

    public void mostrarNombreInformacion(String nombrePrograma){
        if (main.textoProgramaSonando != null) {
            main.textoProgramaSonando.setText(nombrePrograma);
            main.progressBarInformacion.setVisibility(View.GONE);
        }
    }

    public Fragment cambiarFragment(String nombre_fragment, boolean addToBackStack, boolean almacenarProcesoActual) {
        final int posicion = getPosicion(nombre_fragment);
        final Fragment fragmento = getFragment(posicion);
        final FragmentManager fragmentManager = getInstanciaMain().getFragmentManager();
        if (posicion < 20) {
            mDrawerList.setItemChecked(posicion, true);
            mDrawerList.setSelection(posicion);
            getActivityPrincipal().setTitle(navMenuTitles[posicion]);
        }
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragmento);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        almacenarProcesoActual(almacenarProcesoActual);
        return fragmento;
    }

    public void comprobarControlesReproductor() {
        switch (streaming) {
            //cargando
            case 0:
                progressStreaming.setVisibility(View.VISIBLE);
                boton_play.setVisibility(View.INVISIBLE);
                ondas_on.setVisibility(View.INVISIBLE);
                ondas_off.setVisibility(View.VISIBLE);
                break;
            //play
            case 1:
                ondas_on.setVisibility(View.VISIBLE);
                ondas_off.setVisibility(View.INVISIBLE);
                boton_pause.setVisibility(View.INVISIBLE);
                boton_play.setVisibility(View.INVISIBLE);
                boton_stop.setVisibility(View.VISIBLE);
                break;
            //pausa
            case 2:
                progressStreaming.setVisibility(View.INVISIBLE);
                ondas_on.setVisibility(View.INVISIBLE);
                ondas_off.setVisibility(View.VISIBLE);
                boton_pause.setVisibility(View.INVISIBLE);
                boton_play.setVisibility(View.VISIBLE);
                boton_stop.setVisibility(View.INVISIBLE);
                break;
            //pausar/stop
            case 3:
                ondas_on.setVisibility(View.VISIBLE);
                ondas_off.setVisibility(View.INVISIBLE);
                boton_pause.setVisibility(View.INVISIBLE);
                boton_play.setVisibility(View.INVISIBLE);
                boton_stop.setVisibility(View.VISIBLE);
                break;
            //stop
            case 4:
                ondas_off.setVisibility(View.VISIBLE);
                ondas_on.setVisibility(View.INVISIBLE);
                boton_pause.setVisibility(View.INVISIBLE);
                boton_play.setVisibility(View.VISIBLE);
                boton_stop.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void progressBarStreaming(boolean flag) {
        if (flag) {
            progressStreaming.setVisibility(View.VISIBLE);
            boton_play.setVisibility(View.INVISIBLE);
            progressStreaming.getIndeterminateDrawable().setColorFilter(activityPrincipal.getResources().getColor(R.color.blanco), PorterDuff.Mode.SRC_IN);
            streaming = 0;
        } else {
            progressStreaming.setVisibility(View.GONE);
        }
    }

    //metodo creado para actualizar el activity cada vez que la notificacion de la musica sea abierta
    public void cambioReproductor(String mensaje) {
        if (mensaje.equals("Play")) {
            ondas_on.setVisibility(View.VISIBLE);
            ondas_off.setVisibility(View.INVISIBLE);
            boton_pause.setVisibility(View.INVISIBLE);
            boton_play.setVisibility(View.INVISIBLE);
            boton_stop.setVisibility(View.VISIBLE);
            streaming = 1;
            registrarPantallaAnalytics("Reproduccion del streaming");
        } else if (mensaje.equals("Pausar")) {
            ondas_on.setVisibility(View.INVISIBLE);
            ondas_off.setVisibility(View.VISIBLE);
            boton_pause.setVisibility(View.INVISIBLE);
            boton_play.setVisibility(View.VISIBLE);
            boton_stop.setVisibility(View.INVISIBLE);
            streaming = 2;
        } else if (mensaje.equals("Pausar/Stop")) {
            ondas_on.setVisibility(View.VISIBLE);
            ondas_off.setVisibility(View.INVISIBLE);
            boton_pause.setVisibility(View.INVISIBLE);
            boton_play.setVisibility(View.INVISIBLE);
            boton_stop.setVisibility(View.VISIBLE);
            streaming = 3;
        } else if (mensaje.equals("Stop")) {
            ondas_off.setVisibility(View.VISIBLE);
            ondas_on.setVisibility(View.INVISIBLE);
            boton_pause.setVisibility(View.INVISIBLE);
            boton_play.setVisibility(View.VISIBLE);
            boton_stop.setVisibility(View.INVISIBLE);
            streaming = 4;
        }
    }

    public boolean currentVersionL() {
        final int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= 21) {
            return true;
        }
        return false;
    }

    public void manejoAnalytics() {
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = AnalyticsApplication.getInstancia(activityPrincipal);
        mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());
    }

    public void registrarPantallaAnalytics(String name) {
        if (mTracker == null) {
            manejoAnalytics();
        }
        Log.i("Pantalla enviada", "Nombre de la pantalla: " + name);
        mTracker.setScreenName("Pantalla:" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().build());
        GoogleAnalytics.getInstance(activityPrincipal.getBaseContext()).dispatchLocalHits();
    }

    public void enviarInteraccion(String tipo) {
        if (mTracker == null) {
            manejoAnalytics();
        }
        mTracker.send(new HitBuilders.SocialBuilder()
                .setNetwork("Twitter")
                .setAction("Interaccion")
                .setTarget(tipo)
                .build());
    }

}
