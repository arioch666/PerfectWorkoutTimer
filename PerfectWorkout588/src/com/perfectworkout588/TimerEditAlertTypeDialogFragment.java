package com.perfectworkout588;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;

public class TimerEditAlertTypeDialogFragment extends DialogFragment {
    public int _alertType = 2; // default to melody & vibration
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		_alertType = getArguments().getInt("alertType");
		
		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_edit_alert_type_title);
       
        builder.setSingleChoiceItems(R.array.alert_type_array, _alertType, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	_alertType = which;
            }
        });
        
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   EditAlertTypeDialogListener activity = (EditAlertTypeDialogListener) getActivity();
                	   activity.OnEditAlertTypeComplete(_alertType);
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
	
	public interface EditAlertTypeDialogListener
	{
		public void OnEditAlertTypeComplete(int alertType);
	}
	
}
