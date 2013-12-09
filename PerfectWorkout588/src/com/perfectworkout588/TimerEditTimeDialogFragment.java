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
		builder.setTitle(R.string.dialog_edit_time_increments_title);
		builder.setTitle(R.string.dialog_edit_time_increments_message);

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_set_time, null);
		builder.setView(view);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPickerIncrement);
						_increment = np.getValue();
						EditTimeDialogListener activity = (EditTimeDialogListener) getActivity();
						activity.OnEditTimeComplete(_increment);
					}
				}).setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});

		// Create the AlertDialog object and return it
		AlertDialog alertDialog = builder.create();

		String[] displayValues = new String[]{"5","10","15","20","25","30","35","40","45","50","55","60"};
		int displayValueIndex;
		for (displayValueIndex = 0; displayValueIndex < displayValues.length; displayValueIndex++)
		{ 
			if(_increment == Integer.parseInt(displayValues[displayValueIndex])) break;
		}
		
		NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPickerIncrement);
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(displayValues);
		np.setMaxValue(displayValues.length - 1);
		np.setMinValue(0);
		np.setValue(displayValueIndex);
		return alertDialog;
	}

	public interface EditTimeDialogListener {
		public void OnEditTimeComplete(int increment);
	}

}
