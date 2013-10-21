package com.perfectworkout588;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private int time;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button;
		time = 10;
		
		tv = (TextView)findViewById(R.id.tvTimer);
		tv.setText(Integer.toString(time));
		
		button = (Button)findViewById(R.id.btnStart);
		button.setOnClickListener(startTimer);
		
		button = (Button)findViewById(R.id.btnReset);
		button.setOnClickListener(resetTimer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	View.OnClickListener startTimer = new OnClickListener(){
		public void onClick(View v){
			new CountDownTimer((time * 1000), 100) {
				int seconds = time;
				
			     public void onTick(long millisUntilFinished) {
			    	 if (Math.round((float)millisUntilFinished / 1000.0f) != seconds)
			             tv.setText(Integer.toString(--seconds));
			     }

			     public void onFinish() {
			       
			     }
			  }.start();

			
			tv.setText(Integer.toString(time));
		}
	};
	
	View.OnClickListener resetTimer = new OnClickListener(){
		public void onClick(View v){

			tv.setText(Integer.toString(time));
		}
	};
}
