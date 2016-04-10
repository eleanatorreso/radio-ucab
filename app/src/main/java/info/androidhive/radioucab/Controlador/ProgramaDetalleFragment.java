package info.androidhive.radioucab.Controlador;

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
import android.widget.LinearLayout.LayoutParams;

import java.lang.reflect.GenericDeclaration;
import java.util.List;

import info.androidhive.radioucab.Logica.HorarioProgramaLogica;
import info.androidhive.radioucab.Logica.LocutorProgramaLogica;
import info.androidhive.radioucab.Logica.ManejoActivity;
import info.androidhive.radioucab.Logica.ManejoToast;
import info.androidhive.radioucab.Logica.ManejoUsuarioActual;
import info.androidhive.radioucab.Logica.ProgramaLogica;
import info.androidhive.radioucab.Model.HorarioPrograma;
import info.androidhive.radioucab.Model.Locutor;
import info.androidhive.radioucab.Model.Programa;
import info.androidhive.radioucab.R;

public class ProgramaDetalleFragment extends Fragment {

    public Programa programa;
    private TextView titulo;
    private TextView descripcion;
    private TextView encabezadoLocutores;
    private TextView encabezadoHorarios;
    private TextView tipoPrograma;
    private ImageView imagenProgramaFavorito;
    private int previoTextView = 0, idElementos = 0;
    private RelativeLayout layout;
    private final LocutorProgramaLogica logicaLocutor = new LocutorProgramaLogica();
    private int idFacebook;
    private int idTwitter;
    private final ManejoActivity manejoActivity = ManejoActivity.getInstancia();
    private final ManejoUsuarioActual manejoUsuarioActual = ManejoUsuarioActual.getInstancia();
    private final ManejoToast manejoToast = ManejoToast.getInstancia();
    private final HorarioProgramaLogica horarioProgramaLogica = new HorarioProgramaLogica();
    private RelativeLayout.LayoutParams paramHorario;
    private final ProgramaLogica programaLogica = new ProgramaLogica();

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
            manejoActivity.editarActivity(5, true, "ProgramaDetalle", "Detalle del programa",false);
            manejoActivity.mostrarBackToolbar();
            crearHijos();
        } catch (Exception e) {
            Log.i("detallePrograma", e.getMessage());
        }
    }

    public String siguienteIdElemento() {
        idElementos = idElementos + 1;
        return idElementos + "";
    }

    public void setImagenFacebook(final String idUsuarioFacebook, int idDerecha, final String nombreFacebook) {
        try {
            ImageView imagenFacebook = new ImageView(getActivity());
            String uri = "@drawable/ic_facebook";
            int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            imagenFacebook.setImageDrawable(res);
            imagenFacebook.setId(Integer.parseInt(siguienteIdElemento()));
            RelativeLayout.LayoutParams paramIconoFacebook = new RelativeLayout.LayoutParams(40,40);
            paramIconoFacebook.addRule(RelativeLayout.RIGHT_OF, idDerecha);
            paramIconoFacebook.addRule(RelativeLayout.BELOW, previoTextView);
            //paramIconoFacebook.addRule(RelativeLayout.ALIGN_PARENT_END);
            paramIconoFacebook.setMargins(0, 10, 20, 0);
            layout.addView(imagenFacebook, paramIconoFacebook);
            idFacebook = imagenFacebook.getId();
            imagenFacebook.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent facebookIntent = intentAbrirFacebook(v.getContext(), idUsuarioFacebook, nombreFacebook);
                    startActivity(facebookIntent);
                }
            });
        } catch (Exception e) {
            int x = 1;
        }
    }

    public void setImagenTwitter(final String idUsuarioTwitter) {
        ImageView imagenTwitter = new ImageView(getActivity());
        String uri = "@drawable/ic_twitter";
        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        imagenTwitter.setImageDrawable(res);
        imagenTwitter.setId(Integer.parseInt(siguienteIdElemento()));
        RelativeLayout.LayoutParams paramIconoTwitter = new RelativeLayout.LayoutParams(40,40);
        paramIconoTwitter.addRule(RelativeLayout.RIGHT_OF, idFacebook);
        paramIconoTwitter.addRule(RelativeLayout.BELOW, previoTextView);
        paramIconoTwitter.setMargins(0, 10, 20, 0);
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
        String uri = "@drawable/ic_instagram";
        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        imagenInstagram.setImageDrawable(res);
        imagenInstagram.setId(Integer.parseInt(siguienteIdElemento()));
        RelativeLayout.LayoutParams paramIconoInstagram = new RelativeLayout.LayoutParams(40,40);
        paramIconoInstagram.addRule(RelativeLayout.RIGHT_OF, idTwitter);
        paramIconoInstagram.addRule(RelativeLayout.BELOW, previoTextView);
        paramIconoInstagram.setMargins(0, 10, 20, 0);
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
        List <Locutor> locutores = logicaLocutor.getLocutoresPrograma(programa.getId());
        if (locutores != null) {
            TextView nombreLocutor;
            RelativeLayout.LayoutParams paramLocutor;
            for (Locutor locutor : locutores) {
                nombreLocutor = new TextView(getActivity());
                nombreLocutor.setText(locutor.getNombreCompleto());
                nombreLocutor.setTextColor(getResources().getColor(R.color.negro));
                nombreLocutor.setTextSize(14);
                nombreLocutor.setId(Integer.parseInt(siguienteIdElemento()));
                paramLocutor = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                paramLocutor.addRule(RelativeLayout.BELOW, previoTextView);
                paramLocutor.setMargins(20, 10, 20, 10);
                layout.addView(nombreLocutor, paramLocutor);
                setImagenFacebook(locutor.getUsuarioFacebook(), nombreLocutor.getId(), locutor.getUsuarioNombreFacebook());
                setImagenTwitter(locutor.getUsuarioTwitter());
                setImagenInstagram(locutor.getUsuarioInstagram());
                previoTextView = nombreLocutor.getId();
            }
        }
    }

    private String getDiaSemana(int diaSemana) {
        switch (diaSemana) {
            case 1:
                return getActivity().getString(R.string.dia_semana_1);
            case 2:
                return getActivity().getString(R.string.dia_semana_2);
            case 3:
                return getActivity().getString(R.string.dia_semana_3);
            case 4:
                return getActivity().getString(R.string.dia_semana_4);
            case 5:
                return getActivity().getString(R.string.dia_semana_5);
            case 6:
                return getActivity().getString(R.string.dia_semana_6);
            case 7:
                return getActivity().getString(R.string.dia_semana_7);
        }
        return "";
    }

    public void cargarHorarios() {
        List<HorarioPrograma> horarios = horarioProgramaLogica.getHorarioPrograma(programa.getId());
        if (horarios != null) {
            TextView horario = new TextView(getActivity());
            previoTextView = 0;
            String texto;
            for (HorarioPrograma horarioPrograma : horarios) {
                texto = getDiaSemana(horarioPrograma.getDia_semana()) + ": " + horarioPrograma.getHorario_inicio() + "-" + horarioPrograma.getHorario_fin();
                horario.setText(texto);
                horario.setId(Integer.parseInt(siguienteIdElemento()));
                horario.setTextSize(14);
                horario.setTextColor(getResources().getColor(R.color.negro));
                paramHorario = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                //paramHorario.addRule(RelativeLayout.CENTER_HORIZONTAL);
                if (previoTextView == 0) {
                    paramHorario.addRule(RelativeLayout.BELOW, encabezadoHorarios.getId());
                    horario.setPadding(20, 10, 10, 0);
                } else {
                    paramHorario.addRule(RelativeLayout.BELOW, previoTextView);
                    horario.setPadding(20, 5, 10, 0);
                }
                //paramHorario.setMargins(20, 5, 10,0);
                previoTextView = horario.getId();
                layout.addView(horario, paramHorario);
                horario = new TextView(getActivity());
            }
        }
    }

    public void comprobarColoImagenFavorito(){
        if (!programaLogica.comprobarProgramaFavorito(programa.getTitulo())) {
            imagenProgramaFavorito.setColorFilter(getActivity().getResources().getColor(R.color.list_divider));
        }
        else {
            imagenProgramaFavorito.setColorFilter(getActivity().getResources().getColor(R.color.amarillo_ucab));
        }
    }

    public void crearHijos() {
        imagenProgramaFavorito = (ImageView) getActivity().findViewById(R.id.imagen_estrella_detalle);
        comprobarColoImagenFavorito();
        imagenProgramaFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                almacenarProgramaFavorito();
            }
        });
        titulo = (TextView) getActivity().findViewById(R.id.texto_titulo_programa);
        titulo.setText(programa.getTitulo());
        descripcion = (TextView) getActivity().findViewById(R.id.texto_descripcion_programa);
        descripcion.setText(programa.getDescripcion());
        encabezadoHorarios = (TextView) getActivity().findViewById(R.id.encabezado_horarios_programa);
        encabezadoLocutores = (TextView) getActivity().findViewById(R.id.encabezado_locutores_programa);
        tipoPrograma = (TextView) getActivity().findViewById(R.id.texto_tipo_programa);

        cargarHorarios();
        switch (programa.getTipo()) {
            case 0:
                tipoPrograma.setText("Contenido");
                break;
            case 1:
                tipoPrograma.setText("Programa");
                encabezadoLocutores.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams paramTextoLocutores = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                paramTextoLocutores.addRule(RelativeLayout.BELOW, previoTextView);
                paramTextoLocutores.setMargins(0, 20, 0, 20);
                paramTextoLocutores.addRule(RelativeLayout.CENTER_HORIZONTAL);
                encabezadoLocutores.setLayoutParams(paramTextoLocutores);
                //layout.addView(encabezadoLocutores, paramTextoLocutores);
                previoTextView = encabezadoLocutores.getId();
                cargarLocutores();
                break;
        }
    }

    public void almacenarProgramaFavorito(){
        if (!programaLogica.isAlmacenamientoProgramaFavorito()) {
            if (manejoUsuarioActual.usuarioConectado()) {
                programaLogica.almacenarProgramaFavorito(programa.getTitulo(), getActivity(), this);
            } else {
                manejoToast.crearToast(getActivity(), getActivity().getResources().getString(R.string.toast_error_debe_estar_logeado));
            }
        }
        else {
            manejoToast.crearToast(getActivity(), getActivity().getResources().getString(R.string.campo_toast_almacenamiento_en_proceso));
        }
    }

    public static Intent intentAbrirFacebook(Context context, String idFacebook, String nombreFacebook) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/" + idFacebook)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/" + nombreFacebook)); //catches and opens a url to the desired page
        }
    }

}


