package com.perfectworkout588;

import java.util.Timer;
import java.util.TimerTask;

import com.perfectworkout588.TimerEditAlertToneDialogFragment.EditAlertToneDialogListener;
import com.perfectworkout588.TimerEditAlertTypeDialogFragment.EditAlertTypeDialogListener;
import com.perfectworkout588.TimerEditTimeDialogFragment.EditTimeDialogListener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
//import android.app.KeyguardManager;
//import android.app.KeyguardManager.KeyguardLock;
//import android.content.BroadcastReceiver;
import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener, EditAlertToneDialogListener, EditAlertTypeDialogListener, EditTimeDialogListener{

	private int time = 60, _increment = 10, lastTimerValue; //in seconds
	private TextView mintv, sectv;
	private Vibrator vibrator;
	private Timer waitTimer;
	Sensor accelerometer, proximitySensor;
	SensorManager sensorManager;
	public Boolean started;
	final float NOISE = (float) 3.0;
	final float NS2S = 1.0f / 1000000000.0f;
	float _previousY;
	int numberOfDirectionChanges = 0;
	float _previousChangeDirY = 0;
	long _previousTimestamp = 0;
	float _localMax = 0;
	float _localMin = 0;
	private SoundPool soundPool;
	int _alertTone; 	// 1:beep, 2:airhorn - one based to match soundPool
	int _alertType;  	// 1:melody 2:vibration 3:melody&vibration
	boolean loaded = false;
	private int beepSound, hornSound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button;
		lastTimerValue = time;
		started = false;
		mintv = (TextView)findViewById(R.id.tvMin);
		sectv = (TextView)findViewById(R.id.tvSec);
		updateDisplay();		

		button = (Button)findViewById(R.id.btnStart);
		button.setOnClickListener(startTimer);
		
		button = (Button)findViewById(R.id.btnReset);
		button.setOnClickListener(resetTimer);
		
		button = (Button)findViewById(R.id.btnDecrease);
		button.setOnClickListener(decreaseTime);
		
		button = (Button)findViewById(R.id.btnIncrease);
		button.setOnClickListener(increaseTime);
		
		Log.i("onCreate", "initializeSensors()");
		initilizeSensors();
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded = true;
			}
		});
		beepSound = soundPool.load(this, R.raw.beep, 1);
		hornSound = soundPool.load(this, R.raw.airhorn, 2);
		
		_alertTone = 1;  // beep
		_alertType = 2;  // melody & vibrate
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//		filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        mReceiver = new ScreenReceiver();
//        mReceiver.setMainActivity(this);
//        registerReceiver(mReceiver, filter);
	}

	private void initilizeSensors()
	{
		Log.i("initializeSensors", "Initializing sensors");
		if(accelerometer == null && vibrator == null)
		{
			if(initializeAccelerometer())
			{
				if(initializeProximitySensor())
				{
					Toast.makeText(this, "Initialization Complete", Toast.LENGTH_SHORT).show();	
					Log.i("initSensors", "Initialization complete");
				}
				else
				{
					Toast.makeText(this, "initialization failed", Toast.LENGTH_SHORT).show();
					Log.i("initSensors", "Initialization failed");
				}
			}
			vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
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
			Log.i("initProximity", "Proximity sensor set");
			return true;
		}
	}

	private Boolean initializeAccelerometer()
	{
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		Sensor defaultAccelerometer = ((SensorManager) 
				getSystemService(Context.SENSOR_SERVICE)).getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		
		if (defaultAccelerometer == null)
		{
			Toast.makeText(this, "Your Device Does not have an accelerometer. Buy a better Device", Toast.LENGTH_SHORT).show();
			return false;
		} else
		{
			accelerometer = defaultAccelerometer;
			Toast.makeText(this, "Accelerometer Set", Toast.LENGTH_SHORT).show();
			Log.i("initAccelerometer", "Accelerometer set");
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
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			shakeDetect(event);
		}
		else if(accelerometer!= null && event.sensor.getType() == accelerometer.getType())
		{	
			//faceDetect(event);
		}
		else if(proximitySensor!=null && event.sensor.getType() == proximitySensor.getType())
		{
			detectProximity(event);
		}
	}

	private void detectProximity(SensorEvent event)
	{
		if(!started && event.values[0] == 0)
		{
			if(loaded)
			{
				soundPool.play(beepSound, getMaxVolume() , getMaxVolume(), 1, 0, 1f);
			}	
			started=true;
			startTimerNow();
			Log.i("detectProximity", "timer started");
		}
		else if(started && event.values[0] == 0)
		{
			if(loaded)
			{
				soundPool.play(beepSound, getMaxVolume() , getMaxVolume(), 1, 0, 1f);
			}	
			started = false;
			pauseTimer();
			Log.i("detectProximity", "timer paused");
		}
	}
	
	/**
	 * @return the audioManager
	 */
	public AudioManager getAudioManager() {
		if(audioManager == null)
			audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		return audioManager;
	}

	/**
	 * @param audioManager the audioManager to set
	 */
	public void setAudioManager(AudioManager audioManager) {
		this.audioManager = audioManager;
	}

	/**
	 * @return the maxVolume
	 */
	public Float getMaxVolume() {
		if(maxVolume == null)
		{
			maxVolume = (float) getAudioManager().getStreamMaxVolume(AudioManager.STREAM_MUSIC);
 
		}
		return maxVolume;
	}

	/**
	 * @param maxVolume the maxVolume to set
	 */
	public void setMaxVolume(Float maxVolume) {
		this.maxVolume = maxVolume;
	}

	private void shakeDetect(SensorEvent event)
	{
		if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
			return;
		float y = event.values[1];

		if (IsValidDirectionChange(y, event.timestamp))
		{
			numberOfDirectionChanges += 1;
			///Toast.makeText(this, "validDirection", Toast.LENGTH_SHORT).show();
		}
		
		if (numberOfDirectionChanges == 6) // 3 taps
		{
			Log.i("shakeDetect", "3 taps triggered");
			//Toast.makeText(this, "3 TAPS TRIGGERED!", Toast.LENGTH_SHORT).show();	
			increaseTimer();
			numberOfDirectionChanges = 0;
			_previousTimestamp = 0;
		}	
	}
	
	boolean IsValidDirectionChange(float currentYValue, long currentTimestamp)
	{
		if(TooMuchTimeHasPassed(currentTimestamp))
		{
			return false;
		}
		if(IsLocalMax(currentYValue) || IsLocalMin(currentYValue))
		{
			return false;
		}
		if(!DirectionChanged(currentYValue)) return false;
		if(IsNoise(currentYValue)) return false;
		_previousY = currentYValue;
		_previousTimestamp = currentTimestamp;
		return true;
	}

	// Detect face-up and face-down orientation
	// Perform task(s) upon detection
	// Cancel previous detection when user changes mind and flips phone back over before task begins
	private void faceDetect(SensorEvent event){

		float z = event.values[2];
				
		//detect facing upon a new orientation change
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && IsValidDirectionChange(z, event.timestamp)){
			//cancel timer if new flip
			try{
				waitTimer.cancel();
				Log.i("waitTimer", "canceled");
			}catch(Exception e){
				Log.i("waitTimer", "cancel exception");
			}
			//face detection
			if(z < 0){//face-down
				waitTimer = new Timer();
				Log.i("faceDetect", ": in facedetect - flipped down");
				//perform a task after a delay
				try{
					waitTimer.schedule(new TimerTask(){
						public void run(){
							//task
							vibrator.vibrate(1000);
						}
					}, 2000);					
				}
				catch(Exception e){
					Log.i("vibro",  e.toString());					
				}
			}
			else{//face-up
				waitTimer = new Timer();				
				Log.i("faceDetect", ": in facedetect - flipped up");
				//perform a task after a delay
				try{
					waitTimer.schedule(new TimerTask(){
						public void run(){
							//task
							vibrator.vibrate(250);
						}
					}, 2000);	
				}
				catch(Exception e){
					Log.i("vibro",  e.toString());					
				}
			}
			
		}
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

	//////////// TIMER FUNCTIONS /////////////////
	@SuppressLint("NewApi")
	protected final void startTimerNow()
	{
		started = true;
		countdownTimer = new CountDownTimer((lastTimerValue * 1000), 100) 
		{
		
			int seconds = lastTimerValue;
			int mins, secs;
			String s;
			
		     public void onTick(long millisUntilFinished) {
		    	 if (Math.round((float)millisUntilFinished / 1000.0f) != seconds){
		    		 mins = --seconds/60;
		    		 secs = seconds%60;
		    		 lastTimerValue = seconds;
		    		 if(secs < 10)
		    			 s = "0" + secs;
		    		 else
		    			 s = Integer.toString(secs);
		    		 mintv.setText(mins<10?"0"+mins:Integer.toString(mins));
		     	  	 sectv.setText(secs<10?"0"+secs:Integer.toString(secs));
//		             mintv.setText(Integer.toString(mins));
//		             sectv.setText(s);
		             
		             if(seconds == 30 || seconds == 10 || (seconds <=5 && seconds > 0))
		             {
		            	 
		            	 if(loaded)
		            	 {
		            		 soundPool.play(beepSound, getMaxVolume() , getMaxVolume(), 1, 0, 1f);
		            	 }
		             }
		    	 }
		     }
	
		     public void onFinish() {
		    	 pauseTimer();
		    	 lastTimerValue = time;
	        	 
	        	 if(loaded)
	        	 {
	        		 soundPool.play(hornSound, getMaxVolume() , getMaxVolume(), 1, 0, 1f);
	        	 }
	        	 updateDisplay();
		     }

			
	  };
	  	
	  	countdownTimer.start();
	  	((Button)findViewById(R.id.btnStart)).setText(getResources().getString(R.string.pause));
	  	((Button)findViewById(R.id.btnStart)).setBackground((getResources().getDrawable(R.drawable.bluebutton)));
	}
	
	@SuppressLint("NewApi")
	protected final void pauseTimer()
	{
		started = false;
		countdownTimer.cancel();
		((Button)findViewById(R.id.btnStart)).setText(getResources().getString(R.string.start));
		((Button)findViewById(R.id.btnStart)).setBackground((getResources().getDrawable(R.drawable.greenbutton)));
		Log.i("pauseTimer", "timer paused");
	}
		
	View.OnClickListener startTimer = new OnClickListener(){
		public void onClick(View v){
			
			if(!started)
			{
				startTimerNow();
				Log.i("startTimer", "timer started");
			}
			else
			{
				pauseTimer();
				Log.i("pause", "timer paused");
			}
		}
	};
	
	//sometimes crashes if timer not running
	View.OnClickListener resetTimer = new OnClickListener(){
		public void onClick(View v){
			boolean tempStarted = started;
			if(started)
			{
				pauseTimer();
			}
			lastTimerValue = time;
			updateDisplay();
			if(tempStarted)
			{
				startTimerNow();
			}
//			mintv.setText(Integer.toString(time/60));
//			sectv.setText(Integer.toString(time%60));
			Log.i("resetTimer", "timer reset");
		}
	};
	
	View.OnClickListener decreaseTime = new OnClickListener(){
		public void onClick(View v){
			decreaseTimer();
			Log.i("decreaseTime", "time decremented");
		}
	};
	
	View.OnClickListener increaseTime = new OnClickListener(){
		public void onClick(View v){
			increaseTimer();
			Log.i("increaseTime", "time incremented");
		}
	};
	
	public void setIncrement(int secs){
		//empty stub
		//(private int increment) already declared
	}
	
	private CountDownTimer countdownTimer;
	private AudioManager audioManager;
	private Float maxVolume;
//	private IntentFilter filter;
	private ScreenReceiver mReceiver;
	
	@Override
	protected void onResume()
	{
			super.onResume();
//			registerReceiver(mReceiver, filter);
			enableSensors();			
	}

	void enableSensors() {
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
//	        case R.id.action_settings_alert_type:
//	            editAlertType();
//	            return true;
	        case R.id.action_settings_alert_tone:
	            editAlertTone();
	            return true;
	        case R.id.action_settings_time:
	            editTime();
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	void editTime() {
		DialogFragment dialog = new TimerEditTimeDialogFragment();
		Bundle args = new Bundle();
		args.putInt("increment", _increment);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "timerTime");
	}

	void editAlertTone() {
		DialogFragment dialog = new TimerEditAlertToneDialogFragment();
		Bundle args = new Bundle();
		args.putInt("alertTone", _alertTone);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "AlertTone");
	}

	void editAlertType() {
		TimerEditAlertTypeDialogFragment dialog = new TimerEditAlertTypeDialogFragment();
		Bundle args = new Bundle();
		args.putInt("alertType", _alertType);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "AlertType");
	}
	
	@Override
	public void OnEditAlertToneComplete(int alertTone) {
		_alertTone = alertTone;
	}

	@Override
	public void OnEditAlertTypeComplete(int alertType) {
		_alertType = alertType;
	}
	
	@Override
	public void OnEditTimeComplete(int incrementIndex) {
		String[] displayValues = new String[]{"5","10","15","20","25","30","35","40","45","50","55","60"};
		_increment = Integer.parseInt(displayValues[incrementIndex]);
	}


	public void decreaseTimer() {
		boolean tempStarted = started;
		if(started)
		{
			pauseTimer();
		}
		if(lastTimerValue-_increment>0)
		{
			lastTimerValue-=_increment;
		}
		else
		{
			lastTimerValue=1;
		}
		if(tempStarted)
		{
			startTimerNow();
		}		
		else
		{
			updateDisplay();
			
		}
	}

	public void increaseTimer() {
		boolean tempStarted = started;
		if(started)
		{
			pauseTimer();
		}
		lastTimerValue+=_increment;
		if(tempStarted)
		{
			startTimerNow();
		}
		else
		{
			updateDisplay();
		}
	}
	
	public void updateDisplay() {
		mintv.setText((lastTimerValue/60)<10?"0"+(lastTimerValue/60):Integer.toString(lastTimerValue/60));
 	  	 sectv.setText((lastTimerValue%60)<10?"0"+(lastTimerValue%60):Integer.toString(lastTimerValue%60));
	}

	
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		if(!hasFocus)
//		{
//			unregisterReceiver(mReceiver);
//		}
//	}
	
}
