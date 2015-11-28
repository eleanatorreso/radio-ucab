package info.androidhive.radioucab.Controlador;

import info.androidhive.radioucab.Logica.PerfilLogica;
import info.androidhive.radioucab.Logica.p;
import info.androidhive.radioucab.Model.Usuario;
import info.androidhive.radioucab.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;

import java.util.List;

public class PerfilFragment extends Fragment {

    private View rootView;
    private ImageView imagen_perfil;
    private TextView nombre_usuario;
    private TextView usuario_twitter;
    private final PerfilLogica perfilLogica = new PerfilLogica();
    private Usuario usuario;
    private RelativeLayout layout;

    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
            return rootView;
        } catch (Exception e) {
            Log.e("Perfil: onCreateView", e.getMessage());
        }
        return null;
    }

    public void cargarDatosUsuario() {
        imagen_perfil = (ImageView) getActivity().findViewById(R.id.imagen_perfil);
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/.RadioUCAB/picBig.jpeg");
        imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap,0));
        nombre_usuario = (TextView) getActivity().findViewById(R.id.texto_nombre_usuario);
        nombre_usuario.setText(usuario.getNombre() + " " + usuario.getApellido());
        usuario_twitter = (TextView) getActivity().findViewById(R.id.texto_usuario_twitter);
        usuario_twitter.setText(usuario.getUsuario_twitter());
    }

    public void temp() {
        imagen_perfil = (ImageView) getActivity().findViewById(R.id.imagen_perfil);
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/.RadioUCAB/picBig.jpeg");
        imagen_perfil.setImageBitmap(perfilLogica.convertirImagenCirculo(bitmap,0));
       /* Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.ic_imagen_perfil);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        p j = new p(getActivity());
        Canvas c = new Canvas(mutableBitmap);
        j.image = mutableBitmap;
        j.draw(c);
        layout = (RelativeLayout)getActivity().findViewById(R.id.layout_perfil);
        layout.addView(j);*/
       // imagen_perfil.setImageBitmap(getRoundedCornerBitmap(bitmap, 100));
    }

    public void crear () {
        Usuario usuario = new Usuario();
        usuario.setNombre("Eleana");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack("atras");
        CreacionUsuario a = new CreacionUsuario();
        a.usuario = usuario;
        ft.replace(((ViewGroup) getView().getParent()).getId(), a);
        ft.commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (rootView != null) {
            super.onCreate(savedInstanceState);
            perfilLogica.setContexto(this.getActivity());
            crear();
            /*
            if (!Usuario.listAll(Usuario.class).isEmpty()) {
                usuario = Usuario.listAll(Usuario.class).get(0);
                cargarDatosUsuario();
            }
            else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack("atras");
                InicioSesionTwitter inicioSesionTwitterFragment = new InicioSesionTwitter();
                ft.replace(((ViewGroup) getView().getParent()).getId(), inicioSesionTwitterFragment);
                ft.commit();
            }*/
        }
    }

}

