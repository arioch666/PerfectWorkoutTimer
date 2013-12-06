package com.perfectworkout588;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.app.DialogFragment;

public class TimerEditTimeDialogFragment extends DialogFragment {
	int _increment;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		_increment = getArguments().getInt("increment");

		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_edit_time_title);
        
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_time, null);
        builder.setView(view);
        
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   EditTimeDialogListener activity = (EditTimeDialogListener) getActivity();
                	   activity.OnEditTimeComplete(_increment);
                  }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        
//        String[] displayValues = new String[60];
//        for(int i = 1; i< displayValues.length; i++)
//        	displayValues[i-1] = Integer.toString(i);
//
//        NumberPicker np = (NumberPicker) getActivity().findViewById(R.id.numberPickerIncrement);
//        np.setMaxValue(60);
//        np.setMinValue(0);
//        np.setWrapSelectorWheel(false);
//        np.setDisplayedValues(displayValues);
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
	public interface EditTimeDialogListener
	{
		public void OnEditTimeComplete(int increment);
	}

}
