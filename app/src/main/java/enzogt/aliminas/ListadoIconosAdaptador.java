/*Origen: http://envyandroid.com/creating-listdialog-with-images-and-text/*/

package enzogt.aliminas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListadoIconosAdaptador extends ArrayAdapter<Iconos.Categorias> {

    private final List<Iconos.Categorias> list;
    private final Activity context;

    static class ViewHolder {
        protected ImageView iconoCategoria;
        protected TextView nombreCategoria;
    }

    public ListadoIconosAdaptador(Activity context, List<Iconos.Categorias> list) {
        super(context, R.layout.item_seleccion_icono, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.item_seleccion_icono, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.nombreCategoria = (TextView) view.findViewById(R.id.nombre);
            viewHolder.iconoCategoria = (ImageView) view.findViewById(R.id.icono);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.nombreCategoria.setText(list.get(position).getNombre());
        holder.iconoCategoria.setImageDrawable(list.get(position).getIcono());
        return view;
    }
}