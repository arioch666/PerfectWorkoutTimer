package com.perfectworkout588;

import android.os.Bundle;
import android.app.Activity;
//import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.app.DialogFragment;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
//			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void imageButtonAlertType_OnClick(View view) {
		TimerEditAlertTypeDialogFragment dialog = new TimerEditAlertTypeDialogFragment();
		dialog.show(getFragmentManager(), "AlertType");
		TextView textViewMinutes = (TextView) findViewById(R.id.textViewSettingsAlertTypeCurrentValue);
		switch (dialog.AlertType) {
		default:
		case 0:
			textViewMinutes.setText(R.string.melody);
			break;
		case 1:
			textViewMinutes.setText(R.string.vibration);
			break;
		case 2:
			textViewMinutes.setText(R.string.melodyAndVibration);
			break;
		}

		textViewMinutes.setText(R.string.melody);
	}

	public void imageButtonAlertTone_OnClick(View view) {
		DialogFragment dialog = new TimerEditAlertToneDialogFragment();
		dialog.show(getFragmentManager(), "AlertTone");
	}

	public void imageButtonTimerTime_OnClick(View view) {
		// SeekBar seekBarMinutes = (SeekBar) findViewById(R.id.seekBarMinutes);
		// TextView textViewMinutes =
		// (TextView)findViewById(R.id.TextViewMinutes);

		// SeekBar seekBarSeconds = (SeekBar) findViewById(R.id.SeekBarSeconds);
		// TextView textViewSecons =
		// (TextView)findViewById(R.id.textViewSeconds);

		DialogFragment dialog = new TimerEditTimeDialogFragment();
		dialog.show(getFragmentManager(), "timerTime");
	}

}
