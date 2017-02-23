package enzogt.aliminas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class Instrucciones extends DialogFragment {

    Respuesta respuesta;

    @Override
    public Dialog onCreateDialog (Bundle savedInstance){

        AlertDialog.Builder constructor = new AlertDialog.Builder(getActivity());
        constructor.setTitle(getResources().getString(R.string.dialogo_titulo));
        constructor.setMessage(getResources().getString(R.string.dialogo_texto));

        constructor.setPositiveButton(getResources().getString(R.string.dialogo_boton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                respuesta.onRespuesta("instrucciones", 1);
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
        respuesta = (Respuesta) activity;
    }
}
