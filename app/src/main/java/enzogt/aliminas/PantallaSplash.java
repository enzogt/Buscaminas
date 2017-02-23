package enzogt.aliminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class PantallaSplash extends Activity{

    private static final int SPLASH_SCREEN_DELAY = 3000;
    private String [] mensajes;
    private TextView lblCargando;
    private int mensajesMostrados = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se oculta el action bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Se pone el layouut.
        setContentView(R.layout.splash);

        mensajes = getResources().getStringArray(R.array.mensajes_splash);
        lblCargando = (TextView) findViewById(R.id.lblCargando);

        //Tarea que lanza la actividad siguiente.
        TimerTask tareaSiguienteActividad = new TimerTask() {
            @Override
            public void run() {

                // Se iniciará la pantalla del juego tras el tiempo marcado en el timer.
                Intent mainIntent = new Intent().setClass(PantallaSplash.this, Principal.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        //Tarea que va cambiando el mensaje
        TimerTask tareaCambioMensajes = new TimerTask() {

            @Override
            public void run() {

                /* Este codigo da fallo. El temporizador crea otro hilo de proceso y solo el original puede modificar un control UI.
                if(mensajesMostrados < mensajes.length)
                    PantallaSplash.this.lblCargando.setText(mensajes[mensajesMostrados++]);
                */

                //Con esto se puede.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mensajesMostrados < mensajes.length)
                            lblCargando.setText(mensajes[mensajesMostrados++]);
                    }
                });
            }
        };

        // Espera el tiempo marcado y lanza la nueva actividad.
        Timer lanzarSiguienteActividad = new Timer();
        lanzarSiguienteActividad.schedule(tareaSiguienteActividad, SPLASH_SCREEN_DELAY);

        //Cada X tiempo (tercer parametro) va cambiando el mensaje de carga.
        Timer cambiarMensajes = new Timer();
        cambiarMensajes.schedule(tareaCambioMensajes, 0, 1000);

        //Para que el Splash no sea solo estetico. aprovechare para listar los iconos drawables.
        Juego juego = Juego.getInstance();
        juego.setContext(this);
        juego.listarIconosPaqueteSeleccionado();
    }
}
