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

	private int time; //in seconds
	private TextView mintv, sectv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button;
		time = 70;
		
		mintv = (TextView)findViewById(R.id.tvMin);
		mintv.setText(Integer.toString(time/60));
		
		sectv = (TextView)findViewById(R.id.tvSec);
		sectv.setText(Integer.toString(time%60));
		
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
				int mins, secs;
				String s;
				
			     public void onTick(long millisUntilFinished) {
			    	 if (Math.round((float)millisUntilFinished / 1000.0f) != seconds){
			    		 mins = --seconds/60;
			    		 secs = seconds%60;
			    		 if(secs < 10)
			    			 s = "0" + secs;
			    		 else
			    			 s = Integer.toString(secs);
			    		 
			             mintv.setText(Integer.toString(mins));
			             sectv.setText(s);
			    	 }
			     }

			     public void onFinish() {
			       
			     }
			  }.start();
			
			  	mintv.setText(Integer.toString(time/60));
			  	sectv.setText(Integer.toString(time%60));
		}
	};
	
	View.OnClickListener resetTimer = new OnClickListener(){
		public void onClick(View v){
			//doesn't stop the timer...
			mintv.setText(Integer.toString(time/60));
			sectv.setText(Integer.toString(time%60));
		}
	};
}
