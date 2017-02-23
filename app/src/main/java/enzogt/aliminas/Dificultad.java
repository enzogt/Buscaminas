package enzogt.aliminas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class Dificultad extends DialogFragment {

    Instrucciones.Respuesta respuesta;
    Integer seleccion = -1;
    Juego juego = Juego.getInstance();

    @Override
    public Dialog onCreateDialog (Bundle savedInstance){

        CharSequence[] opciones = {
                getResources().getString(R.string.dialogo_dificultad_nivelBajo),
                getResources().getString(R.string.dialogo_dificultad_nivelMedio),
                getResources().getString(R.string.dialogo_dificultad_nivelAlto)
        };

        if (juego.getDificultad() > 0)
            seleccion = juego.getDificultad() - 1;

        AlertDialog.Builder constructor = new AlertDialog.Builder(getActivity());
        constructor.setTitle(getResources().getString(R.string.dialogo_dificultad_titulo));

        constructor.setSingleChoiceItems(opciones, seleccion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Es el valor que ocupa la opcion en base 0. Le sumo para que empiece desde 1.
                seleccion = which + 1;
            }
        });

        constructor.setPositiveButton(getResources().getString(R.string.dialogo_dificultad_botonAfirmativo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                respuesta.onRespuesta("dificultad", seleccion);
            }
        });

        constructor.setNegativeButton(getResources().getString(R.string.dialogo_dificultad_botonNegativo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                respuesta.onRespuesta("dificultad", -1);
            }
        });

        return constructor.create();
    }

    public interface Respuesta {
        public void onRespuesta(String s, int i);
    }

    @Override
    public void onAttach (Activity activity){
        super.onAttach(activity);
        respuesta = (Instrucciones.Respuesta) activity;
    }

}