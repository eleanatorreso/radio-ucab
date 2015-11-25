package info.androidhive.radioucab.Controlador;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import info.androidhive.radioucab.Logica.FabricLogica;
import info.androidhive.radioucab.Logica.ServicioRadio;
import info.androidhive.radioucab.Controlador.Adaptor.AdaptorNavDrawerList;
import info.androidhive.radioucab.R;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabric = fabric.getInstance();
        fabric.context = this;
        fabric.initFabric();
//	COMENTA ESTO MIENTRAS XQ CREO QUE EL GET TITLE ES PARA AGARARR EL TITULO DEL ACTIONBAR
//		mTitle = mDrawerTitle = getTitle();

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
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1), true, "22"));
        // Perfil
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        // Configuracion
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1), true, "50+"));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new AdaptorNavDrawerList(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        playbackServiceIntent = new Intent(this, ServicioRadio.class);

        ImageView boton = (ImageView) findViewById(R.id.icono_radio_ucab);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start abre a la izquierda, end a la derecha
                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        TextView loginBtn = (TextView) findViewById(R.id.botonIngresar);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new InicioSesionTwitter();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

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

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mensaje = intent.getStringExtra(ServicioRadio.mensaje);
                if (mensaje.equals("Play")) {
                    boton_pause.setVisibility(View.INVISIBLE);
                    boton_play.setVisibility(View.INVISIBLE);
                    boton_stop.setVisibility(View.VISIBLE);
                } else if (mensaje.equals("Pausar")) {
                    boton_pause.setVisibility(View.VISIBLE);
                    boton_play.setVisibility(View.INVISIBLE);
                    boton_stop.setVisibility(View.INVISIBLE);
                } else if (mensaje.equals("Pausar/Stop")) {
                    boton_pause.setVisibility(View.INVISIBLE);
                    boton_play.setVisibility(View.INVISIBLE);
                    boton_stop.setVisibility(View.VISIBLE);
                } else if (mensaje.equals("Stop")) {
                    boton_pause.setVisibility(View.INVISIBLE);
                    boton_play.setVisibility(View.VISIBLE);
                    boton_stop.setVisibility(View.INVISIBLE);
                }
            }
        };
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
        /*
        if (streaming == false) {
			playbackServiceIntent.setAction(ServicioRadio.ACTION_PLAY);
			this.startService(playbackServiceIntent);
			streaming = true;
		}

		else {
			this.stopService(playbackServiceIntent);
			streaming = false;
		}*/
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
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
        return super.onOptionsItemSelected(item);
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
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

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

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

}
