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

    private Context context;
    private int iconoNoticia = 1;

    public AdaptadorNoticia(Context context, int resourceId,
                            List<Noticia> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView titulo;
        TextView descripcion;
    }

    private int seleccionarIconoGeneral(){
        int resultado = 0;
        switch (iconoNoticia){
            case 1:
                resultado = R.drawable.ic_noticias;
                iconoNoticia++;
                break;
            case 2:
                resultado = R.drawable.ic_noticia2;
                iconoNoticia++;
                break;
            case 3:
                resultado = R.drawable.ic_noticia3;
                iconoNoticia++;
                break;
            case 4:
                resultado = R.drawable.ic_noticia4;
                iconoNoticia++;
                break;
            case 5:
                resultado = R.drawable.ic_noticia5;
                iconoNoticia++;
                break;
            case 6:
                resultado = R.drawable.ic_noticia6;
                iconoNoticia = 1;
                break;
        }
        return resultado;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Noticia noticia = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_noticia, null);
            holder = new ViewHolder();
            holder.descripcion = (TextView) convertView.findViewById(R.id.campo_descripcion_noticia);
            holder.titulo = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.descripcion.setText(noticia.getTexto_noticia().substring(0, 140) + ".....");
        holder.titulo.setText(noticia.getTitular());
        switch (noticia.getTipo()) {
            //externa
            case 0:
                holder.imageView.setImageResource(seleccionarIconoGeneral());
                break;
            //Radio UCAB
            case 1:
                holder.imageView.setImageResource(R.drawable.ic_launcher);
                break;
            //UCAB
            case 2:
                holder.imageView.setImageResource(R.drawable.ic_logo_ucab);
                break;
        }
        return convertView;
    }
}
