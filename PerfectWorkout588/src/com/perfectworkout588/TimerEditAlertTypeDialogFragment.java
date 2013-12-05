package com.perfectworkout588;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;

public class TimerEditAlertTypeDialogFragment extends DialogFragment {
    public int AlertType = 2; // default to melody & vibration
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_edit_alert_type_title);
       
        builder.setSingleChoiceItems(R.array.alert_type_array, AlertType, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	AlertType = which;
            }
        });
        
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   // save changes
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
	static public enum AlertTypeEnum
    {
    	melody,
    	vibration,
    	melodyAndVibration,
    }
}
