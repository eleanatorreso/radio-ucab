package info.androidhive.radioucab.Controlador;

import com.google.android.gms.analytics.Tracker;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import info.androidhive.radioucab.Logica.FabricLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.PerfilLogica;
import info.androidhive.radioucab.Logica.ServicioRadio;
import info.androidhive.radioucab.Controlador.Adaptor.AdaptorNavDrawerList;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private AdaptorNavDrawerList adapter;
    private static boolean streaming = false;
    private MediaPlayer player;
    private Intent playbackServiceIntent;
    private BroadcastReceiver receiver;
    private ImageView boton_play;
    private ImageView boton_stop;
    private ImageView boton_pause;
    private CharSequence mTitle;
    private FabricLogica fabric;
    private TwitterLoginButton loginButton;
    private boolean menuAbierto;
    private ImageView imagen_perfil;
    private TextView boton_ingresar;
    private ImageView boton_menu_opciones;
    private final PerfilLogica perfilLogica = new PerfilLogica();
    private Usuario usuario_actual;
    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private Menu menu;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton fab_interaccion;
    private ImageView icono_interaccion;
    private ImageView icono_informacion;
    private Tracker mTracker;
    private ImageView boton;
    private Toolbar toolbar;
    // private ManejoConcurso manejoConcurso = new ManejoConcurso();

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(ServicioRadio.result)
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void cargarUsuarioToolbar() {
        if (Usuario.listAll(Usuario.class).isEmpty() == false) {
            usuario_actual = Usuario.listAll(Usuario.class).get(0);
            boton_ingresar.setVisibility(View.INVISIBLE);
            imagen_perfil.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(this.getString(R.string.ruta_archivos_radio_ucab) + "picBig." +
                    usuario_actual.getFormato_imagen());
            perfilLogica.setContexto(this);
            imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap, 0));
        } else {
            boton_ingresar.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
        }
    }

    public void crearDialogoSiYNo(String titulo, String mensaje, String botonPositivo, String botonNegativo) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setPositiveButton(botonPositivo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            cerrarSesion();
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton(botonNegativo,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            Dialog alerta = builder.create();
            alerta.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alerta.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarSesion() {
        //cerrar sesion en Twitter
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        Usuario.deleteAll(Usuario.class);
        boton_ingresar.setVisibility(View.VISIBLE);
        imagen_perfil.setVisibility(View.INVISIBLE);
        invalidateOptionsMenu();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
        try {
            PerfilFragment perfilFragment = (PerfilFragment) fragment;
            if (perfilFragment != null && perfilFragment.isVisible()) {
                // Call a method in the ArticleFragment to update its content
                perfilFragment.actualizarPerfil();
            }
        } catch (Exception ex) {
            Log.e("Activity", ex.getMessage());
        }
    }

    public void abrirDialogoInteraccion() {
        //manejoConcurso.contexto = this;
        //manejoConcurso.comprobarConcursosActuales();
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        final AlertDialog alerta_dialogo;
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogo_interaccion, null);
        dialogo.setView(dialogView);
        dialogo.setCancelable(true);
        alerta_dialogo = dialogo.create(); //returns an AlertDialog from a Builder.
        alerta_dialogo.show();
        LinearLayout dedicar_cancion = (LinearLayout) dialogView.findViewById(R.id.icono_dedicar_cancion);
        dedicar_cancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("TweetDedicatoria", true);
                alerta_dialogo.dismiss();
            }
        });
        LinearLayout solicitar_cancion = (LinearLayout) dialogView.findViewById(R.id.icono_solicitar_canción);
        solicitar_cancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("TweetSolicitud", true);
                alerta_dialogo.dismiss();
            }
        });

        LinearLayout comentario = (LinearLayout) dialogView.findViewById(R.id.icono_comentario);
        comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("TweetComentario", true);
                alerta_dialogo.dismiss();
            }
        });

        LinearLayout programa = (LinearLayout) dialogView.findViewById(R.id.icono_programa);
        programa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("TweetPrograma", true);
                alerta_dialogo.dismiss();
            }
        });

        LinearLayout concurso = (LinearLayout) dialogView.findViewById(R.id.icono_concurso);
        concurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejoActivity.cambiarFragment("ConcursoFragment", true);
                alerta_dialogo.dismiss();
            }
        });

        Button boton_cerrar = (Button) dialogView.findViewById(R.id.boton_cerrar_interaccion);
        boton_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta_dialogo.dismiss();
            }
        });
    }

    public void abrirDialogoInformacion() {
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        final AlertDialog alerta_dialogo;
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogo_informacion, null);
        dialogo.setView(dialogView);
        dialogo.setCancelable(true);
        alerta_dialogo = dialogo.create(); //returns an AlertDialog from a Builder.
        alerta_dialogo.show();
    }

    public void mostrarBackToolbar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void ocultarOpcionToolbar(){
        if (toolbar != null)
            toolbar.setNavigationIcon(null);
    }

    public void mostrarCloseToolbar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        manejoActivity.setActivityPrincipal(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabric = fabric.getInstance();
        fabric.context = this;
        fabric.initFabric();

        //cambio el color del toolbar superior
        manejoActivity.setActivityPrincipal(this);
        manejoActivity.editarActivity(1, true, "Home");
        //COMENTA ESTO MIENTRAS XQ CREO QUE EL GET TITLE ES PARA AGARARR EL TITULO DEL ACTIONBAR
        //mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Parrilla
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Noticias
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Eventos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Programas
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1), true, "22"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Perfil
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        // Configuracion
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        // setting the nav drawer list adapter
        adapter = new AdaptorNavDrawerList(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);


        playbackServiceIntent = new Intent(this, ServicioRadio.class);

        boton = (ImageView) findViewById(R.id.icono_menu);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start abre a la izquierda, end a la derecha
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    menuAbierto = false;
                    boton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    boton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                boton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                boton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        fab_interaccion = (ImageButton) findViewById(R.id.boton_interaccion);
        if (manejoActivity.currentVersionL()) {
            //fab_interaccion.setVisibility(View.VISIBLE);
            fab_interaccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirDialogoInteraccion();
                }
            });
        } else {
            icono_interaccion = (ImageView) findViewById(R.id.imagen_interaccion);
            icono_interaccion.setVisibility(View.VISIBLE);
            icono_interaccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirDialogoInteraccion();
                }
            });
        }

        icono_informacion = (ImageView) findViewById(R.id.imagen_informacion);
        icono_informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoInformacion();
            }
        });

        boton_ingresar = (TextView) findViewById(R.id.boton_ingresar);

        boton_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new InicioSesionTwitterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.accion_ver_perfil:
                        manejoActivity.cambiarFragment("Perfil", false);
                        break;
                    case R.id.accion_cerrar_sesion:
                        crearDialogoSiYNo(getString(R.string.dialogo_asunto_cerrar_sesion)
                                , getString(R.string.dialogo_contenido_cerrar_sesion)
                                , getString(R.string.dialogo_mensaje_Si)
                                , getString(R.string.dialogo_mensaje_No));
                        break;
                }
                return true;
            }
        });

        imagen_perfil = (ImageView) findViewById(R.id.imagen_usuario);
        cargarUsuarioToolbar();
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        boton_play = (ImageView) findViewById(R.id.icon_play);
        boton_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playStreaming(1);
            }
        });

        boton_stop = (ImageView) findViewById(R.id.icon_stop);
        boton_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playStreaming(2);
            }
        });

        boton_pause = (ImageView) findViewById(R.id.icon_pause);
        boton_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playStreaming(3);
            }
        });
        manejoActivity.comprobarControlesReproductor();

    }

    /**
     * Play Musica *
     */
    public void playStreaming(int action) {
        switch (action) {
            //Play
            case 1:
                playbackServiceIntent.setAction(ServicioRadio.ACTION_PLAY);
                this.startService(playbackServiceIntent);
                streaming = true;
                break;
            //Stop
            case 2:
                this.stopService(playbackServiceIntent);
                streaming = false;
                break;
            //Pause
            case 3:
                playbackServiceIntent.setAction(ServicioRadio.ACTION_PAUSE);
                this.startService(playbackServiceIntent);
                streaming = true;
                break;
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // toggle nav drawer on selecting action bar app icon/title
    /*	if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
		}*/
        // Handle action bar actions click
        /*
        switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}*/


        if (mDrawerToggle.isDrawerIndicatorEnabled() && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
    //return super.onOptionsItemSelected(item);


    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        this.menu = menu;
        if (Usuario.listAll(Usuario.class).isEmpty() == true) {
            MenuItem itemCerrarSesion = menu.findItem(R.id.accion_cerrar_sesion);
            itemCerrarSesion.setVisible(false);
            MenuItem itemPerfil = menu.findItem(R.id.accion_ver_perfil);
            itemPerfil.setVisible(false);
        } else {
            MenuItem itemCerrarSesion = menu.findItem(R.id.accion_cerrar_sesion);
            itemCerrarSesion.setVisible(true);
            MenuItem itemPerfil = menu.findItem(R.id.accion_ver_perfil);
            itemPerfil.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
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

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
//		getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        //	mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    protected void setNavIcon() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            boton.setVisibility(View.VISIBLE);
            ocultarOpcionToolbar();
        } else {
            crearDialogoSiYNo("Confirme su salida", "¿Está seguro que desea salir de la aplicación?", "Si", "No");
            super.onBackPressed();
        }
    }
}