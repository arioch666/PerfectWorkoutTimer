package com.perfectworkout588;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener{

	private int time; //in seconds
	private TextView mintv, sectv;
	Sensor accelerometer, proximitySensor;
	SensorManager sensorManager;
	Boolean started;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button;
		time = 70;
		started = false;
		mintv = (TextView)findViewById(R.id.tvMin);
		mintv.setText(Integer.toString(time/60));
		
		sectv = (TextView)findViewById(R.id.tvSec);
		sectv.setText(Integer.toString(time%60));
		
		button = (Button)findViewById(R.id.btnStart);
		button.setOnClickListener(startTimer);
		
		button = (Button)findViewById(R.id.btnReset);
		button.setOnClickListener(resetTimer);
		initilizeSensors();
	}

	private void initilizeSensors()
	{
		if(initializeAccelerometer())
		{
			if(initializeProximitySensor())
			{
				Toast.makeText(this, "Initialization Complete", Toast.LENGTH_SHORT).show();		
			}
			else
			{
				Toast.makeText(this, "initialization failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private boolean initializeProximitySensor()
	{
		Sensor defaultProximitySensor = ((SensorManager) 
				getSystemService(Context.SENSOR_SERVICE)).getDefaultSensor(Sensor.TYPE_PROXIMITY);
		
		if (defaultProximitySensor == null)
		{
			Toast.makeText(this, "Your Device Does not have a proximity sensor. Buy a better Device", Toast.LENGTH_SHORT).show();
			return false;
		} else
		{
			proximitySensor = defaultProximitySensor;
			Toast.makeText(this, "Proximity Sensor set", Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	private Boolean initializeAccelerometer()
	{
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		Sensor defaultAccelerometer = ((SensorManager) 
				getSystemService(Context.SENSOR_SERVICE)).getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		if (defaultAccelerometer == null)
		{
			Toast.makeText(this, "Your Device Does not have an accelerometer. Buy a better Device", Toast.LENGTH_SHORT).show();
			return false;
		} else
		{
			accelerometer = defaultAccelerometer;
			Toast.makeText(this, "Accelerometer Set", Toast.LENGTH_SHORT).show();
			return true;
		}
//		TextView stream = (TextView) context.findViewById(R.id.stream);
//		stream.setText(displaySensorList.toString());
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{

	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if(accelerometer!= null && event.sensor.getType() == accelerometer.getType())
		{	
			shakeDetect(event);
		}
		else if(proximitySensor!=null && event.sensor.getType() == proximitySensor.getType())
		{
			detectProximity(event);
		}
	}

	private void detectProximity(SensorEvent event)
	{
		// TODO Auto-generated method stub
		if(!started && event.values[0] == 0)
		{
			started=true;
			startTimerNow();
		}
//		((TextView) findViewById(R.id.stream)).setText("x:"+event.values[0]+" y:"+event.values[1]+" z:"+event.values[2]);
	}

	final float NOISE = (float) 2.0;
	final float NS2S = 1.0f / 1000000000.0f;
	float _previousY;
	int numberOfDirectionChanges = 0;
	float _previousChangeDirY = 0;
	long _previousTimestamp = 0;
	float _localMax = 0;
	float _localMin = 0;
	private void shakeDetect(SensorEvent event)
	{
		if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
			return;
		float y = event.values[1];

		if (IsValidDirectionChange(y, event.timestamp))
			numberOfDirectionChanges += 1;
		
		if (numberOfDirectionChanges == 6) // 3 taps
		{
			Toast.makeText(this, "3 TAPS TRIGGERED!", Toast.LENGTH_SHORT).show();		
			numberOfDirectionChanges = 0;
			_previousTimestamp = 0;
		}	
	}
	
	boolean IsValidDirectionChange(float currentYValue, long currentTimestamp)
	{
		if(TooMuchTimeHasPassed(currentTimestamp)) return false;
		if(IsLocalMax(currentYValue) || IsLocalMin(currentYValue)) return false;
		if(!DirectionChanged(currentYValue)) return false;
		if(IsNoise(currentYValue)) return false;
		_previousY = currentYValue;
		_previousTimestamp = currentTimestamp;
		return true;
	}

	boolean TooMuchTimeHasPassed(long currentTimestamp) {
		if ((currentTimestamp - _previousTimestamp) * NS2S < 1)
			return false;
		else {
			numberOfDirectionChanges = 0;
			_previousTimestamp = currentTimestamp;
			return true;
		}
	}

	boolean IsLocalMax(float currentYValue) {
		if (currentYValue < _localMax)
			return false;
		_localMax = currentYValue;
		return true;
	}

	boolean IsLocalMin(float currentYValue) {
		if (currentYValue > _localMin)
			return false;
		_localMin = currentYValue;
		return true;
	}

	boolean DirectionChanged(float currentYValue) {
		if (_previousY < 0 && currentYValue >= 0) // moving down (negative acceleration)
		{
			_previousChangeDirY = currentYValue;
			return true;
		} else if (_previousY >= 0 && currentYValue < 0) {
			_previousChangeDirY = currentYValue;
			return true;
		}
		return false;
	}

	boolean IsNoise (float currentYValue)
	{
		if(Math.abs(_previousChangeDirY)+ Math.abs(currentYValue) <= NOISE)return true;
		return false;
	}

	protected final void startTimerNow()
	{
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	View.OnClickListener startTimer = new OnClickListener(){
		public void onClick(View v){
			
			startTimerNow();
		}
	};
	
	View.OnClickListener resetTimer = new OnClickListener(){
		public void onClick(View v){
			//doesn't stop the timer...
			mintv.setText(Integer.toString(time/60));
			sectv.setText(Integer.toString(time%60));
		}
	};
	
	@Override
	protected void onResume()
	{
			super.onResume();
			if(accelerometer!=null)
			{
				sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			}
			if(proximitySensor!=null)
			{
				sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
			}			
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		sensorManager.unregisterListener(this);
	}

}
