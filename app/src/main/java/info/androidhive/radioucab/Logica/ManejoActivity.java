package info.androidhive.radioucab.Logica;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import info.androidhive.radioucab.Controlador.ConfiguracionFragment;
import info.androidhive.radioucab.Controlador.EditarPerfilFragment;
import info.androidhive.radioucab.Controlador.EditarTweetComentarioFragment;
import info.androidhive.radioucab.Controlador.EditarTweetDedicatoriaFragment;
import info.androidhive.radioucab.Controlador.EditarTweetSolicitarFragment;
import info.androidhive.radioucab.Controlador.EnviarTweet;
import info.androidhive.radioucab.Controlador.EventoFragment;
import info.androidhive.radioucab.Controlador.HomeFragment;
import info.androidhive.radioucab.Controlador.InicioSesionTwitterFragment;
import info.androidhive.radioucab.Controlador.NavDrawerItem;
import info.androidhive.radioucab.Controlador.NoticiaDetalleFragment;
import info.androidhive.radioucab.Controlador.NoticiaFragment;
import info.androidhive.radioucab.Controlador.ParrillaFragment;
import info.androidhive.radioucab.Controlador.PerfilFragment;
import info.androidhive.radioucab.Controlador.ProgramaDetalleFragment;
import info.androidhive.radioucab.Controlador.ProgramaFragment;
import info.androidhive.radioucab.Controlador.RegistroUsuarioFragment;
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
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String fragmentoActual;

    public static ManejoActivity getInstancia() {
        if (instancia == null) {
            instancia = new ManejoActivity();
        }
        return instancia;
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

    public void setActivityPrincipal(Activity activityPrincipal) {
        this.activityPrincipal = activityPrincipal;
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
                color = activityPrincipal.getResources().getColor(R.color.azul_radio_ucab);
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
        toolbar.setBackgroundColor(color);
    }

    public void editarActivity (int seccion, boolean mostrarBotonInteraccion, String fragmentoActual) {
        cambiarDeColor(seccion);
        cambiarIconoMenu();
        if (mostrarBotonInteraccion == true) {
            boton_interaccion.setVisibility(View.VISIBLE);
        }
        else {
            boton_interaccion.setVisibility(View.INVISIBLE);
        }
        if (fragmentoActual != null)
            this.fragmentoActual = fragmentoActual;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
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
            default:
                break;
        }
        return fragment;
    }

    public int getPosicion (String nombre_fragment) {
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
        }
        return 0;
    }

    public Fragment cambiarFragment(String nombre_fragment) {
        int posicion = getPosicion(nombre_fragment);
        Fragment fragmento = getFragment(posicion);
        FragmentManager fragmentManager = getActivityPrincipal().getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragmento).commit();
        if (posicion < 20) {// update selected item and title, then close the drawer
            mDrawerList.setItemChecked(posicion, true);
            mDrawerList.setSelection(posicion);
            getActivityPrincipal().setTitle(navMenuTitles[posicion]);
        }
        return fragmento;
    }

}
