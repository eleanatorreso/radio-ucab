package info.androidhive.radioucab.Controlador.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import java.util.List;

import info.androidhive.radioucab.Logica.ManejoUsuarioActual;
import info.androidhive.radioucab.Model.Tag;
import info.androidhive.radioucab.R;


public class AdaptadorMisNoticias extends ArrayAdapter<Tag> {

    private Context context;
    private ViewHolder holder = null;
    private ManejoUsuarioActual manejoUsuarioActual = ManejoUsuarioActual.getInstancia();

    public AdaptadorMisNoticias(Context context, int resourceId,
                                List<Tag> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        CheckBox checkBoxTipoNoticia;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Tag tag = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_mis_noticias, null);
            holder = new ViewHolder();
            holder.checkBoxTipoNoticia = (CheckBox) convertView.findViewById(R.id.checkbox_tipo_noticia);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.checkBoxTipoNoticia.setText(tag.getNombre_tag());
        if (tag.getUsuario_tag() == 1) {
            holder.checkBoxTipoNoticia.setChecked(true);
        }
        else {
            holder.checkBoxTipoNoticia.setChecked(false);
        }
        holder.checkBoxTipoNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CheckBox checkSeleccionado = (CheckBox) v;
                int seleccion = 0;
                if (checkSeleccionado.isChecked()) {
                    seleccion = 1;
                }
                manejoUsuarioActual.setValorLista(checkSeleccionado.getText().toString(), seleccion);
            }
        });
        return convertView;
    }

}
