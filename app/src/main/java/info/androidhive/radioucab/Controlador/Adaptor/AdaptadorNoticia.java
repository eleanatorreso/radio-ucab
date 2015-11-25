package info.androidhive.radioucab.Controlador.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import info.androidhive.radioucab.Model.Noticia;
import info.androidhive.radioucab.R;


public class AdaptadorNoticia extends ArrayAdapter<Noticia> {

    Context context;

    public AdaptadorNoticia(Context context, int resourceId,
                            List<Noticia> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public static final Integer[] images = {R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_parrilla,
            R.drawable.ic_home, R.drawable.ic_perfil, R.drawable.ic_launcher, R.drawable.ic_home,
            R.drawable.ic_perfil, R.drawable.ic_launcher};

    private class ViewHolder {
        ImageView imageView;
        TextView titulo;
        TextView descripcion;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Noticia noticia = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_noticia, null);
            holder = new ViewHolder();
            holder.descripcion = (TextView) convertView.findViewById(R.id.desc);
            holder.titulo = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.descripcion.setText(noticia.getTexto_noticia().substring(0, 76) + "......");
        holder.titulo.setText(noticia.getTitular());
        holder.imageView.setImageResource(images[position]);

        return convertView;
    }
}
