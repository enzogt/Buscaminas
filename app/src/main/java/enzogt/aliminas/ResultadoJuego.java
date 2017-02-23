package enzogt.aliminas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ResultadoJuego extends DialogFragment {

    public static ResultadoJuego newInstance(String T, String C, String B, int I) {
        ResultadoJuego frag = new ResultadoJuego();
        Bundle args = new Bundle();

        args.putString("titulo", T);
        args.putString("cuerpo", C);
        args.putString("boton", B);
        args.putInt("icono", I);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())

                .setIcon(getArguments().getInt("icono"))

                .setTitle(getArguments().getString("titulo"))

                .setMessage(getArguments().getString("cuerpo"))

                .setPositiveButton(getArguments().getString("boton"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((FragmentAlertDialog)getActivity()).doPositiveClick();
                            }
                        }
                )

                .create();
    }
}