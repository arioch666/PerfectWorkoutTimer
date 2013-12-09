package com.perfectworkout588;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;

public class TimerEditAlertToneDialogFragment extends DialogFragment {
    int _alertTone;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		_alertTone = getArguments().getInt("alertTone");

		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_edit_alert_type_title);
       
        builder.setSingleChoiceItems(R.array.alert_tone_array, _alertTone - 1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // The 'which' argument contains the index position
            // of the selected item
            	_alertTone = which + 1;
            }
        });
        
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   EditAlertToneDialogListener activity = (EditAlertToneDialogListener) getActivity();
                	   activity.OnEditAlertToneComplete(_alertTone);
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
	
	public interface EditAlertToneDialogListener
	{
		public void OnEditAlertToneComplete(int alertType);
	}
}
