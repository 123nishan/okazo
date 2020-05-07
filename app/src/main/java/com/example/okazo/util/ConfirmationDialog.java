package com.example.okazo.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmationDialog extends AppCompatDialogFragment {

    private orderConfirmationListener listener;
    private String message;

    public ConfirmationDialog(String message){
        this.message=message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.OnYesClicked();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.OnNoClicked();
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public interface orderConfirmationListener{
        void OnYesClicked();
        void OnNoClicked();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(orderConfirmationListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+
                    "must implement listener");
        }
    }

}


