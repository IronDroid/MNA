package com.gomedia.mna;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogoGalery {

    public static Dialog messageDialog(Context context, int layoutResID, String title) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(layoutResID);
        dialog.setTitle(title);
        return dialog;
    }

    public static AlertDialog messageAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        return builder.create();
    }

    public static AlertDialog messageBoxfinish(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((Activity) context).finish();
            }
        });
        return builder.create();
    }

    public static AlertDialog messageBoxfinish(final Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton(
                "Aceptar",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((Activity) context).finish();
            }
        });
        return builder.create();
    }

    public static AlertDialog messageBoxfinishCancel(final Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((Activity) context).finish();
            }
        });
        builder.setNegativeButton("Cancelar", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog messageBoxOk(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog messageBoxText(Context context, String text, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton(
                text,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog messageBoxText(Context context, String text, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false).setPositiveButton(
                text,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        return alert;
    }

    public AlertDialog name(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("�Estas seguro que desea salir del sistema?").setCancelable(
                false).setPositiveButton(
                "Aceptar",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
}
