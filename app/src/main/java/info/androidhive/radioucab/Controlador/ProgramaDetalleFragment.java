package info.androidhive.radioucab.Controlador;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import java.util.List;

import info.androidhive.radioucab.Logica.LocutorProgramaLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class ProgramaDetalleFragment extends Fragment {

    public Programa programa;
    private TextView titulo;
    private TextView descripcion;
    private TextView textoLocutores;
    private RelativeLayout.LayoutParams params;
    private int previoTextView = 0, idElementos = 0;
    private RelativeLayout layout;
    private final LocutorProgramaLogica logicaLocutor = new LocutorProgramaLogica();
    private int idFacebook;
    private int idTwitter;
    private int idLocutores;
    private ManejoActivity manejoActivity = ManejoActivity.getInstancia();

    private LinearLayout contentView;


    public ProgramaDetalleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            return inflater.inflate(R.layout.fragment_detalle_programa, container, false);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            layout = (RelativeLayout) getView().findViewById(R.id.layout_detalle_programa);
            manejoActivity.editarActivity(5, true, "ProgramaDetalle");
            crearHijos();
        } catch (Exception e) {
            Log.i("detallePrograma", e.getMessage());
        }
    }

    public String siguienteIdElemento() {
        idElementos = idElementos + 1;
        return idElementos + "";
    }

    public void setImagenFacebook(final String idUsuarioFacebook, int idDerecha) {
        try {
            ImageView imagenFacebook = new ImageView(getActivity());
            String uri = "@drawable/ic_facebook_icon";
            int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            imagenFacebook.setImageDrawable(res);
            imagenFacebook.setId(Integer.parseInt(siguienteIdElemento()));
            RelativeLayout.LayoutParams paramIconoFacebook = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            paramIconoFacebook.addRule(RelativeLayout.RIGHT_OF, idDerecha);
            paramIconoFacebook.addRule(RelativeLayout.BELOW, previoTextView);
            layout.addView(imagenFacebook, paramIconoFacebook);
            idFacebook = imagenFacebook.getId();
            imagenFacebook.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent facebookIntent = intentAbrirFacebook(v.getContext(), idUsuarioFacebook);
                    startActivity(facebookIntent);
                }
            });
        } catch (Exception e) {
            int x = 1;
        }
    }

    public void setImagenTwitter(final String idUsuarioTwitter) {
        ImageView imagenTwitter = new ImageView(getActivity());
        String uri = "@drawable/ic_twitter_icon";
        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        imagenTwitter.setImageDrawable(res);
        imagenTwitter.setId(Integer.parseInt(siguienteIdElemento()));
        RelativeLayout.LayoutParams paramIconoTwitter = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramIconoTwitter.addRule(RelativeLayout.RIGHT_OF, idFacebook);
        paramIconoTwitter.addRule(RelativeLayout.BELOW, previoTextView);
        layout.addView(imagenTwitter, paramIconoTwitter);
        idTwitter = imagenTwitter.getId();
        imagenTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + idUsuarioTwitter)));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + idUsuarioTwitter)));
                }
            }
        });
    }

    public void setImagenInstagram(final String idUsuarioInstagram) {
        ImageView imagenInstagram = new ImageView(getActivity());
        String uri = "@drawable/ic_instagram_icon";
        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        imagenInstagram.setImageDrawable(res);
        imagenInstagram.setId(Integer.parseInt(siguienteIdElemento()));
        RelativeLayout.LayoutParams paramIconoInstagram = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramIconoInstagram.addRule(RelativeLayout.RIGHT_OF, idTwitter);
        paramIconoInstagram.addRule(RelativeLayout.BELOW, previoTextView);
        layout.addView(imagenInstagram, paramIconoInstagram);
        imagenInstagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String scheme = "http://instagram.com/_u/" + idUsuarioInstagram;
                String path = "https://instagram.com/" + idUsuarioInstagram;
                String nomPackageInfo = "com.instagram.android";
                Intent intentIns = new Intent();
                try {
                    v.getContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                    intentIns = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
                } catch (Exception e) {
                    intentIns = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                }
                startActivity(intentIns);
            }
        });
    }

    public void cargarLocutores() {
        List<Locutor> locutores = logicaLocutor.getLocutoresPrograma(programa.getId());
        if (locutores != null) {
            TextView nombreLocutor;
            RelativeLayout.LayoutParams paramLocutor;
            for (Locutor locutor : locutores) {
                nombreLocutor = new TextView(getActivity());
                nombreLocutor.setText(locutor.getNombreCompleto());
                nombreLocutor.setId(Integer.parseInt(siguienteIdElemento()));
                paramLocutor = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                paramLocutor.addRule(RelativeLayout.BELOW, previoTextView);
                layout.addView(nombreLocutor, paramLocutor);
                setImagenFacebook(locutor.getUsuarioFacebook(), nombreLocutor.getId());
                setImagenTwitter(locutor.getUsuarioTwitter());
                setImagenInstagram(locutor.getUsuarioInstagram());
                previoTextView = nombreLocutor.getId();
            }
        }
    }

    public void cargarHorarios() {
        List<HorarioPrograma> horarios = programa.getHorarios();
        if (horarios != null) {
            TextView horario = new TextView(getActivity());
            RelativeLayout.LayoutParams paramHorario;
            previoTextView = 0;
            for (HorarioPrograma horarioPrograma : horarios) {
                horario.setText(horarioPrograma.getHorario());
                horario.setId(Integer.parseInt(siguienteIdElemento()));
                paramHorario = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (previoTextView == 0) {
                    paramHorario.addRule(RelativeLayout.BELOW, descripcion.getId());
                } else {
                    paramHorario.addRule(RelativeLayout.BELOW, previoTextView);
                }
                previoTextView = horario.getId();
                layout.addView(horario, paramHorario);
                horario = new TextView(getActivity());
            }
        }
    }

    public void crearHijos() {
        titulo = new TextView(getActivity());
        titulo.setText(programa.getTitulo());
        descripcion = new TextView(getActivity());
        descripcion.setText(programa.getDescripcion());
        textoLocutores = new TextView(getActivity());

        titulo.setId(Integer.parseInt(siguienteIdElemento()));
        descripcion.setId(Integer.parseInt(siguienteIdElemento()));
        textoLocutores.setId(Integer.parseInt(siguienteIdElemento()));

        RelativeLayout.LayoutParams paramTitulo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paramDescripcion = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramDescripcion.addRule(RelativeLayout.BELOW, titulo.getId());

        layout.addView(titulo, paramTitulo);
        layout.addView(descripcion, paramDescripcion);

        cargarHorarios();

        textoLocutores.setText("Comunicate con los locutores:");
        RelativeLayout.LayoutParams paramTextoLocutores = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramTextoLocutores.addRule(RelativeLayout.BELOW, previoTextView);
        layout.addView(textoLocutores, paramTextoLocutores);
        previoTextView = textoLocutores.getId();

        cargarLocutores();
    }

    public static Intent intentAbrirFacebook(Context context, String idFacebook) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/" + idFacebook)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/sentiapps")); //catches and opens a url to the desired page
        }
    }


}


