package enzogt.aliminas;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Principal extends AppCompatActivity implements Instrucciones.Respuesta, Dificultad.Respuesta{

    //Se instancia la clase que se usara durante toda la sesion del juego.
    Juego juego = Juego.getInstance();
    GridLayout tablero = null;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        juego.setContext(this);

        /*Inicialización de la action bar para poner la imagen del paquete de iconos.*/
        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.actionbar_frutas); //Por defecto frutas.
    }

    /*Se pone el menú del juego.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_juego, menu);
        return true;
    }

    /*Se le asigna el comportamiento a los botones del menú.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.instrucciones:
                Instrucciones dialogoInstrucciones = new Instrucciones();
                dialogoInstrucciones.show(getFragmentManager(), "Mi diálogo");
                return true;

            case R.id.configuracion:
                Dificultad dialogoDificultad = new Dificultad();
                dialogoDificultad.show(getFragmentManager(), "Mi diálogo");
                return true;

            case R.id.iconos:
                final Intent intent = new Intent(this, Iconos.class);
                startActivityForResult(intent, 1);
                return true;

            case R.id.nuevo:
                if (tablero == null) {
                    //Antes de poner el tablero, guardo las dimensiones (los usare para calcular el tamaño de los botones.)
                    guardarDimensionesTablero();
                    setContentView(R.layout.tablero);
                    tablero = (GridLayout)findViewById(R.id.tablero);
                } else {
                    //Se elimina las casillas, por si estamos comenzando otra partidas después de jugar una.
                    tablero.removeAllViewsInLayout();
                }

                juego.nuevoJuego();
                ponerBotones();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Respuesta de los dialogos*/
    @Override
    public void onRespuesta(String origen, int respuesta) {

        //Si la respuesta viene originada del dialogo de instrucciones.
        if (origen.equals("instrucciones")) {
            //El dialogo solo es de ayuda, no interesa hacer nada con la respuesta.
            //Toast.makeText(getApplicationContext(), Integer.toString(respuesta), Toast.LENGTH_LONG).show();
        }

        //Si la respuesta viene originada del dialogo de seleccion de dificultad.
        if (origen.equals("dificultad")) {
            if (respuesta > 0) {
                juego.setDificultad(respuesta);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Integer paqueteSeleccionado = data.getIntExtra("paquete", 0);
                juego.setPackIconos(paqueteSeleccionado);
                cambiarIconoActionBar(paqueteSeleccionado);
            }
        }
    }

    /*Función que cambia el icono del Action Bar*/
    private void cambiarIconoActionBar (Integer i) {
        TypedArray arrayNombres = getResources().obtainTypedArray(R.array.actionbar_iconos);
        if (i < 0 || i > arrayNombres.length())  i = 0;
        actionBar.setLogo(arrayNombres.getResourceId(i, 0));
    }

    private void guardarDimensionesTablero () {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.root);

        int margenHorizontal = (int) getResources().getDimension(R.dimen.margen_tablero_horizontal);
        int margenVertical = (int) getResources().getDimension(R.dimen.margen_tablero_vertical);

        juego.setDimensionXtablero(layout.getWidth() - (2 * margenHorizontal));
        juego.setDimensionYtablero(layout.getHeight() - (2 * margenVertical));
    }

    private void ponerBotones () {

        //Se guardan las dimensiones que
        final int dimensionXBoton = juego.getDimensionXtablero() / juego.getColumnas();
        final int dimesionYBoton = juego.getDimensionYtablero() / juego.getFilas();

        //Se ponen las dimensiones del tablero.
        tablero.setRowCount(juego.getFilas());
        tablero.setColumnCount(juego.getColumnas());

        /**Estilo del borde del botón*/
        ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.getPaint().setColor(getResources().getColor(R.color.colorBordeBoton));
        shapedrawable.getPaint().setStrokeWidth(1f);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);

        for (int i = 0; i < juego.getColumnas() * juego.getFilas(); i++) {

            //Se añade al tablero.
            tablero.addView(botonCasillaTablero(shapedrawable, dimensionXBoton, dimesionYBoton, i), i);
        }
    }

    private ImageButton botonCasillaTablero (ShapeDrawable estiloBorde, int dimensionXBoton, int dimesionYBoton, int indiceEnTablero) {

        int fila = indiceEnTablero / juego.getFilas();
        int columna = indiceEnTablero % juego.getColumnas();

        ImageButton imageButton = new ImageButton(this);

        //Id auto-generado.
        imageButton.setId(View.generateViewId());

        //Dimensiones del boton.
        imageButton.setLayoutParams(new LinearLayout.LayoutParams(dimensionXBoton, dimesionYBoton));

        //Color de fondo.
        imageButton.setBackgroundColor(getResources().getColor(R.color.colorBotonNormal));

        //Dibujado de los bordes
        imageButton.setBackground(estiloBorde);

        //Tamaño de la bandera dentro del botón.
        imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        //Supongo que no es necesario. Pero hago incapie que de otrign el botón no tiene imagen.
        imageButton.setImageDrawable(null);

        //Se pone una tag para identificarlo mas tarde y poder realizar la comparacion tablero - matriz.
        imageButton.setTag(new InformacionCasilla(fila, columna, indiceEnTablero));

        //Evento click del boton
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton ib = (ImageButton) v;

                //Se aumentará la variable de pulsaciones.
                juego.aumentarPulsaciones();

                //Se mirara que valor contenia la celda.
                comprobarCasilla(ib);
            }
        });

        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ImageButton ib = (ImageButton) view;

                //Si no tiene bandera se pone, si la tiene se quita.
                if (ib.getDrawable() == null) {
                    //Si quedan banderas se pone.
                    if (juego.quedanBanderas()) {
                        ib.setImageDrawable(getResources().getDrawable(R.drawable.bandera_roja));
                        juego.cogerBandera();
                    //Si se han agotado, se indica.
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.sinBanderas), Toast.LENGTH_SHORT).show();
                    }
                //Siempre se permite devolver la bandera.
                } else {
                    juego.devolverBandera();
                    ib.setImageDrawable(null);
                }

                //Si se pone true parece que no hace el click (sino hace Long click y luego click).
                return true;
            }
        });

        //Se devuelve el boton estilizado y eventizado para quien lo haya llamado lo obtenga.
        return imageButton;
    }

    private void comprobarCasilla (ImageButton botonAccionado){

        //Se identifican las cordenadas del boton que ha llamado a esta funcion (lo he guardado en el TAG).
        InformacionCasilla info = (InformacionCasilla) botonAccionado.getTag();

        int fila = Integer.valueOf(info.getFila());
        int columna = Integer.valueOf(info.getColumna());

        //Se mira que contiene la casilla.
        int valorCasilla = juego.getValorCasilla(fila, columna);

        //Si la casilla que vamos a desvelar tenia bandera, se devuelve.
        if (botonAccionado.getDrawable() != null) {
            juego.devolverBandera();
        }

        //Se desvela la casilla tocada.
        desvelarCasilla(botonAccionado, valorCasilla);

        //Si es una casilla vacia, se desencadena el revelado de sus semejantes.
        if (valorCasilla == juego.getValorDefectoCasilla()) {
            revelarCasillasVecinas(fila, columna);
        }

        //Juego acabado -> caso perdedor.
        if (valorCasilla == juego.getValorDefectoMina()){

            botonAccionado.setBackgroundColor(getResources().getColor(R.color.colorBotonMinaExplotada));
            botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.explosion));
            desvelarTodasCeldas();

            /*Dialogo perdedor*/
            String titulo = getResources().getString(R.string.tituloPerdedor);
            String texto = getResources().getString(R.string.textoPerdedor)
                    .replace("{tiempo}", devolverTiempoJuego())
                    .replace("{pulsaciones}", String.valueOf(juego.getPulsaciones())
                    );
            String boton = getResources().getString(R.string.botonresultado);
            int icono = R.drawable.perdedor;

            DialogFragment newFragment = ResultadoJuego.newInstance(titulo, texto, boton, icono);
            newFragment.show(getFragmentManager(), "dialog");
        }

        //Juego acabado -> caso ganador.
        if (juego.todasCasillasDescubiertas()){

            desvelarTodasCeldas();

            /*Dialogo perdedor*/
            String titulo = getResources().getString(R.string.tituloGanador);
            String texto = getResources().getString(R.string.textoGanador)
                    .replace("{tiempo}", devolverTiempoJuego())
                    .replace("{pulsaciones}", String.valueOf(juego.getPulsaciones())
                    );
            String boton = getResources().getString(R.string.botonresultado);
            int icono = R.drawable.ganador;

            DialogFragment newFragment = ResultadoJuego.newInstance(titulo, texto, boton, icono);
            newFragment.show(getFragmentManager(), "dialog");
        }

        //Toast.makeText(getApplicationContext(), String.valueOf(fila) + " | " + String.valueOf(columna), Toast.LENGTH_SHORT).show();
    }

    private String devolverTiempoJuego () {

        long lapso = System.currentTimeMillis() - juego.getTiempoAlInicioJuego();

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(lapso),
                TimeUnit.MILLISECONDS.toMinutes(lapso) -  TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(lapso)),
                TimeUnit.MILLISECONDS.toSeconds(lapso) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lapso)));
    }

    private void desvelarCasilla(ImageButton botonAccionado, int valorCasilla) {

        if (botonAccionado.isEnabled()) {
            juego.setCasillasDescubiertas();

            switch (valorCasilla) {

                case -1:
                    botonAccionado.setBackgroundColor(getResources().getColor(R.color.colorBotonSinNada));
                    botonAccionado.setImageDrawable(null); //Por si tenia una bandera.
                    break;
                case 0:
                    casoMina(botonAccionado);
                    break;
                case 1:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_uno));
                    break;
                case 2:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_dos));
                    break;
                case 3:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_tres));
                    break;
                case 4:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_cuatro));
                    break;
                case 5:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_cinco));
                    break;
                case 6:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_seis));
                    break;
                case 7:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_siete));
                    break;
                case 8:
                    botonAccionado.setImageDrawable(getResources().getDrawable(R.drawable.numero_ocho));
                    break;
            }

            //Los botones numerados tendran un fondo mas claro y algo de padding (para no tener que ponerlo 8 veces en los case).
            if (valorCasilla > 0) {
                botonAccionado.setBackgroundColor(getResources().getColor(R.color.colorBotonNumerado));
                botonAccionado.setPadding(15, 15, 15, 15);
            }

            //Se deshabilita.
            botonAccionado.setEnabled(false);
        }
    }

    private void desvelarTodasCeldas () {

        ImageButton ib;
        int botonesRecorridos = 0;

        for (int filas = 0; filas < juego.getFilas(); filas++){

            for (int columnas = 0; columnas < juego.getColumnas(); columnas++){

                ib = (ImageButton) tablero.getChildAt(botonesRecorridos);

                //Si el boton aún no se habia desvelado. Se revela.
                if(ib.isEnabled()){
                    desvelarCasilla(ib, juego.getValorCasilla(filas, columnas));
                    ib.setEnabled(false);
                }

                botonesRecorridos++;
            }
        }
    }

    private void casoMina (ImageButton ib){
        ib.setEnabled(false);
        ib.setBackgroundColor(getResources().getColor(R.color.colorBotonMina));
        ib.setImageDrawable(getResources().getDrawable(juego.devolverUnIcono()));

        switch (juego.getColumnas()){
            case 8:
                ib.setPadding(15, 15, 15, 15);
                break;
            case 10:
                ib.setPadding(10, 10, 10, 10);
                break;
            case 12:
                ib.setPadding(5, 5, 5, 5);
                break;
        }
    }

    private void revelarCasillasVecinas (int fila, int columna) {

        int filaAbsoluta;
        int columnaAbsoluta;
        int valorCasilla;

        //Se miran los vecinos.
        for (int filaRelativa = -1; filaRelativa <= 1; filaRelativa++) {

            for (int columnaRelativa = -1; columnaRelativa <= 1; columnaRelativa++) {

                filaAbsoluta = fila + filaRelativa;
                columnaAbsoluta = columna + columnaRelativa;

                //Se tiene en cuanta que la casilla no este fuera del tablero.
                if ((filaAbsoluta >= 0 && filaAbsoluta < juego.getFilas()) && (columnaAbsoluta >= 0 && columnaAbsoluta < juego.getColumnas())) {

                    //Se obtiene el valor de la casilla.
                    valorCasilla = juego.getValorCasilla(filaAbsoluta, columnaAbsoluta);

                    //Si es cualquier cosa menos una mina se mirara de destaparla.
                    if (valorCasilla == juego.getValorDefectoCasilla() || valorCasilla > 0) {

                        ImageButton casilla = (ImageButton) tablero.getChildAt((filaAbsoluta * juego.getColumnas()) + columnaAbsoluta);

                        //Si la casilla no habia sido descubierta con anterioridad se destapa.
                        if (casilla.isEnabled())
                            comprobarCasilla(casilla);
                    }
                }
            }
        }
    }

    /*Metodo que permite revelar la casilla del tablero a partir de la coordenada x e y.*/
    private void revelarCasilla (int fila, int columna){
        desvelarCasilla((ImageButton) tablero.getChildAt((fila * tablero.getColumnCount()) + columna), juego.getValorCasilla(fila, columna));
    }
}

/* *
* Código finalmente no utilizado.
* */

/*
    Menú contextual de dificultad del juego -> En el libro pone que Radio por lo que no se usará

    //Forma de poner menu contextual a la activity.
    //registerForContextMenu((View) findViewById(R.id.activity_principal));


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_dificultad, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.nivelBajo:
                Toast.makeText(getApplicationContext(), "Bajo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nivelMedio:
                Toast.makeText(getApplicationContext(), "Medio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nivelAlto:
                Toast.makeText(getApplicationContext(), "Alto", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
*/

/*
    private Button botonTablero (ShapeDrawable estiloBorde, int dimensionXBoton, int dimesionYBoton) {

        Button btnTemporal = new Button(this);

        //btnTemporal.setText("B" + ((i + 1 < 9) ? "0" + (i + 1) : i + 1));
        btnTemporal.setId(View.generateViewId());

        //Dimensiones del boton.
        btnTemporal.setLayoutParams(new LinearLayout.LayoutParams(dimensionXBoton, dimesionYBoton));

        //Color de fondo.
        btnTemporal.setBackgroundColor(getResources().getColor(R.color.colorBotonNormal));

        //Dibujado de los bordes
        btnTemporal.setBackground(estiloBorde);

        //Evento click del boton
        btnTemporal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tmp = (Button) v;
                //tmp.setVisibility(View.GONE);
                tmp.setBackgroundColor(getResources().getColor(R.color.colorBordeSinNada));
                tmp.setText("");

                //Opcional. Realmente da igual.
                //tmp.setOnClickListener(null);
                //Se quita el evento long click. Una vez se destapa la casilla no hay vuelta atrás.
                //tmp.setOnLongClickListener(null);

                //Este hace lo mismo que los 2 metodos anteriores y además le quita los estilos de boton.
                tmp.setEnabled(false);
            }
        });

        btnTemporal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Button tmp = (Button) view;
                tmp.setBackgroundColor(Color.CYAN);

                //Si se pone true parece que no hace el click (sino hace Long click y luego click).
                return true;
            }
        });

        return btnTemporal;
    }
*/

/* Otro algoritmo de desencadenado de casillas, ¿más eficiente que el mío?
* *Basado en: http://www.genbetadev.com/java-j2ee/crea-tu-propio-buscaminas.
    private void desencadenadoVacias (int fila, int columna) {

        for (int f2 = Math.max(0, fila - 1); f2 < Math.min(juego.getFilas(), fila + 2); f2++){
            for (int c2 = Math.max(0, columna - 1); c2 < Math.min(juego.getColumnas(), columna + 2); c2++){

                ImageButton casilla = (ImageButton) tablero.getChildAt((f2 * juego.getColumnas()) + c2);

                if (casilla.isEnabled())
                    comprobarCasilla(casilla);
            }
        }
    }
 */