package com.example.controledeconvitesrecebidos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UtilsGUI {
    public static void avisoErro(Context contexto,int idTexto, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle(R.string.aviso);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(idTexto);

        builder.setNeutralButton("OK", listener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void confirmaAcao(Context contexto, String mensagem, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle(R.string.confirmacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(mensagem);
        builder.setPositiveButton("Sim", listener);
        builder.setNegativeButton("Não", listener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
