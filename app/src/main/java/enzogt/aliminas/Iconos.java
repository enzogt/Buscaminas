/*Origen: http://envyandroid.com/creating-listdialog-with-images-and-text/*/
package enzogt.aliminas;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Iconos extends ListActivity {

    public static String RESULTADO_ICONOS = "paquete"; //Es la KEY de la respuesta.
    public String[] arrayNombres, arrayIdentificadores;
    private TypedArray iconos;
    private List<Categorias> categoriasList;
    Juego juego = Juego.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populatecategoriasList();
        ArrayAdapter<Categorias> adapter = new ListadoIconosAdaptador(this, categoriasList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Categorias c = categoriasList.get(position);

                //Valor de la seleccion que se va a devolver.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULTADO_ICONOS, position);
                setResult(RESULT_OK, returnIntent);

                iconos.recycle(); //recycle images
                finish();
            }
        });
    }

    private void populatecategoriasList() {
        categoriasList = new ArrayList<Categorias>();

        arrayNombres = getResources().getStringArray(R.array.listado_iconos_nombre);
        arrayIdentificadores = getResources().getStringArray(R.array.listado_iconos_identificadores);
        iconos = getResources().obtainTypedArray(R.array.listado_iconos_iconos);


        for(int i = 0; i < arrayIdentificadores.length; i++){
            categoriasList.add(new Categorias(arrayNombres[i], arrayIdentificadores[i], iconos.getDrawable(i)));
        }
    }

    public class Categorias {
        private String nombre;
        private String identificador;
        private Drawable icono;

        public Categorias(String nombre, String identificador, Drawable icono){
            this.nombre = nombre;
            this.identificador = identificador;
            this.icono = icono;
        }
        public String getNombre() {
            return nombre;
        }
        public Drawable getIcono() {
            return icono;
        }
        public String getIdentificador() {
            return identificador;
        }
    }

}