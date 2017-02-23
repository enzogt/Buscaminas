package enzogt.aliminas;

import android.content.Context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class Juego {
    private static Juego ourInstance = new Juego();
    private Context context;

    /*Propiedades del juego*/

    //Array donde se guardaran las diferentes imagenes. Corresponden a la "minas".
    private ArrayList<Integer> [] identificadoresPaqueteIconosSeleccionados;

    //Dificultad, pre-inicializada al nivel mas bajo(valores posibles 1-2-3).
    private Integer dificultad = 1;

    //Número de minas del tablero.
    private Integer minas = 5;

    //Banderas de la que se ponen al hacer long click.
    private Integer banderasDisponibles = minas;

    //Iconos, inicializada a "frutas".
    private Integer packIconos = 0;

    //Tamaño del tablero
    private Integer filas = 8;
    private Integer columnas = 8;

    //Dimensiones de las casillas.
    private Integer dimensionXtablero;
    private Integer dimensionYtablero;

    //Es la representacion lógica del tablero
    private int [][] matrizTablero;

    //Posibles valores de las casillas: vacio -1 | mina: 0 | proxima: 1-8
    private final int valorDefectoCasilla = -1;
    private final int valorDefectoMina = 0;

    private int casillasDescubiertas = 0;
    private int pulsaciones = 0;

    private long tiempoAlInicioJuego;



    /*Singleton*/
    public static Juego getInstance() {
        return ourInstance;
    }
    /*Constructor*/
    private Juego() {}



    /*Getters, setters y otros metodos*/
    public void setContext(Context context) {this.context = context;}

    public Integer getDificultad() {
        return dificultad;
    }
    public void setDificultad(Integer dificultad) {
        this.dificultad = dificultad;

        switch (dificultad){

            case 2:
                filas = 10;
                columnas = 10;
                minas = 10;
                break;

            case 3:
                filas = 12;
                columnas = 12;
                minas = 25;
                break;

            default:
                filas = 8;
                columnas = 8;
                minas = 5;
                break;
        }

        establecerTablero();
    }

    public Integer getPackIconos() {return packIconos;}
    public void setPackIconos(Integer packIconos) {this.packIconos = packIconos;}

    public Integer getFilas() {return filas;}
    public Integer getColumnas() {return columnas;}

    public Integer getDimensionXtablero() {return dimensionXtablero;}
    public void setDimensionXtablero(Integer dimensionXtablero) {this.dimensionXtablero = dimensionXtablero;}

    public Integer getDimensionYtablero() {return dimensionYtablero;}
    public void setDimensionYtablero(Integer dimensionYtablero) {this.dimensionYtablero = dimensionYtablero;}

    public void cogerBandera() {this.banderasDisponibles--;}
    public void devolverBandera() {this.banderasDisponibles++;}
    public boolean quedanBanderas() {return this.banderasDisponibles > 0;}

    public int getValorDefectoCasilla() {return valorDefectoCasilla;}
    public int getValorDefectoMina() {return valorDefectoMina;}

    public int getValorCasilla (int fila, int columna) {
        return matrizTablero[fila][columna];
    }

    public void setCasillasDescubiertas () {
        this.casillasDescubiertas++;
    }

    public boolean todasCasillasDescubiertas (){ return  (casillasDescubiertas == ((filas * columnas) - minas)); }

    public int getPulsaciones() {return pulsaciones;}
    public void aumentarPulsaciones() {this.pulsaciones++;}

    public long getTiempoAlInicioJuego() {return tiempoAlInicioJuego;}

    /*Metodos del juego*/
    private void establecerSizeTablero(){

        switch (dificultad){
            //Dificultad media.
            case 2:
                filas = 10;
                columnas = 10;
                break;

            //Dificultad alta.
            case 3:
                filas = 12;
                columnas = 12;
                break;

            //Dificultad baja.
            default:
                filas = 8;
                columnas = 8;
                break;
        }
    }

    private void establecerTablero () {
        matrizTablero = new int[filas][columnas];
    }

    private void inicializarTablero (){

        for (int fila = 0; fila < filas; fila++){

            for (int columna = 0; columna < columnas; columna++){

                matrizTablero[fila][columna] = valorDefectoCasilla;
            }
        }

        //imprimirTableroConsola();
    }

    private void plantarMinasYAsignarProximidad () {

        Random random = new Random();

        int minasRepartir = minas;

        while (minasRepartir > 0) {
            //Posiciones aleatorias de la nueva mina.
            int x = random.nextInt(filas);
            int y = random.nextInt(columnas);

            if (matrizTablero[y][x] != valorDefectoMina) {

                matrizTablero[y][x] = valorDefectoMina;
                ponerProximidad(y, x);

                minasRepartir--;
            }
        }

        imprimirTableroConsola();
    }

    private void ponerProximidad (int filaMina, int columnaMina) {

        int filaAbsoluta;
        int columnaAbsoluta;

        for (int filaRelativa = -1; filaRelativa <= 1; filaRelativa++){

            for (int columnaRelativa = -1; columnaRelativa <= 1; columnaRelativa++){

                //La casilla donde esta la mina no se toca.
                if ( !(filaRelativa == 0 && columnaRelativa == 0) ){

                    filaAbsoluta = filaMina + filaRelativa;
                    columnaAbsoluta = columnaMina + columnaRelativa;

                    //Se tiene en cuanta que la casilla no este fuera del tablero.
                    if ( (filaAbsoluta >= 0 && filaAbsoluta < filas) && (columnaAbsoluta >= 0 && columnaAbsoluta < columnas) ) {

                        if (matrizTablero[filaAbsoluta][columnaAbsoluta] != valorDefectoMina) {

                            //Si es una casilla que antiguamente era casilla vacia
                            if (matrizTablero[filaAbsoluta][columnaAbsoluta] < 1) {
                                matrizTablero[filaAbsoluta][columnaAbsoluta] = 1;
                            //Si la casilla ya tenia un valor por ser proxima a otra mina.
                            } else {
                                matrizTablero[filaAbsoluta][columnaAbsoluta] += 1;
                            }
                        }
                    }
                }
            }
        }
    }

    public void nuevoJuego (){

        //Inicializaciones.
        banderasDisponibles = minas;
        casillasDescubiertas = 0;
        pulsaciones = 0;
        //listarIconosPaqueteSeleccionado(); -> Ahora se hace en el Splash

        //Configuracion del nuevo juego.
        establecerSizeTablero();
        establecerTablero();
        inicializarTablero();
        plantarMinasYAsignarProximidad();

        tiempoAlInicioJuego = System.currentTimeMillis();
    }

    private void imprimirTableroConsola() {

        String celda;

        System.out.println("\n-----------------------------------------");

        for (int fila = 0; fila < filas; fila++){

            System.out.print("| ");

            for (int columna = 0; columna < columnas; columna++){
                celda = String.valueOf(matrizTablero[fila][columna]);
                System.out.print( ((celda.length() == 1) ? " " + celda : celda) + " | ");
            }
            System.out.println();
        }

        System.out.println("-----------------------------------------\n");
    }

    public void listarIconosPaqueteSeleccionado () {

        //Se va a mirar el XML donde he guardado la informacion de los paquetes.
        String arrayPrefijos [] = context.getResources().getStringArray(R.array.prefijos_paquetes);

        //Donde se iran guardado los ID de los iconos encontrdos.
        identificadoresPaqueteIconosSeleccionados =  new ArrayList[arrayPrefijos.length];

        for(int x = 0; x < identificadoresPaqueteIconosSeleccionados.length; x++){
            identificadoresPaqueteIconosSeleccionados[x] = new ArrayList<>();
        }

        //Variables del sistema de listado de Drawable.
        final R.drawable drawableResources = new R.drawable();
        final Class<R.drawable> c = R.drawable.class;
        final Field[] fields = c.getDeclaredFields();

        for (int i = 90, max = fields.length; i < max; i++) {
            try {
                for (int paquete = 0, totalPaquetes = arrayPrefijos.length; paquete < totalPaquetes; paquete++){

                    String paquetillo = arrayPrefijos[paquete];
                    String cadenilla = fields[i].toString();

                    if (fields[i].toString().contains(arrayPrefijos[paquete])) {

                        int demonios = fields[i].getInt(drawableResources);

                        identificadoresPaqueteIconosSeleccionados[paquete].add(fields[i].getInt(drawableResources));
                    }

                }
            } catch (Exception e) {
                continue;
            }
        }

    }

    public int devolverUnIcono () {

        Random random = new Random();
        return identificadoresPaqueteIconosSeleccionados[packIconos].get(random.nextInt(identificadoresPaqueteIconosSeleccionados[packIconos].size()));
    }

}
