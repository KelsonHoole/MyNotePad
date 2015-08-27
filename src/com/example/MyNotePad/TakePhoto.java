package com.example.MyNotePad;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class TakePhoto extends Activity {

	private long firsttime = 0;
	private long secondtime = 0;
	private final int MSG_START = 100,VIBRATOR = 200,AUTO_TAKE = 300,TOAST = 400;
	private boolean taking = false,stop = false;
	private Button start = null;
	private Camera camera = null;
	String dirPath = null;
	String filepath = "";
	Vibrator vibrator = null;
	boolean [] setting = {false,false};
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case MSG_START:
					takePicture();
					break;
				case VIBRATOR:
					vibrator.vibrate(100);
					break;
				case TOAST:
					Toast.makeText(getApplicationContext(), filepath, Toast.LENGTH_SHORT).show();
					
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_take_photo);
		
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		
		dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/.NotePad/";
		File file = new File(dirPath);
		if(!file.exists())
			file.mkdirs();
		Intent intent = getIntent();
		setting = intent.getBooleanArrayExtra("");
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if(setting[1])
		{
			camera = Camera.open(1);
		}
		else
		{
			camera = Camera.open(0);
		}
		
		if(camera == null)
		{
			Toast.makeText(getApplicationContext(), "相机实例获取失败", Toast.LENGTH_LONG).show();
			return;
		}
		
		camera.setDisplayOrientation(0);
		Camera.Parameters parameters = camera.getParameters(); // Camera parameters to obtain  
		parameters.setPictureFormat(PixelFormat.JPEG);// Setting Picture Format  
		parameters.set("jpeg-quality", 100);
		if(setting[1])
		{
			parameters.setPreviewSize(1920, 2592);
			parameters.set("rotation", 270); 
		}else
		{
			parameters.setPreviewSize(3120, 4160); // Set Photo Size  
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			parameters.set("rotation", 90); 
		}
			
		camera.setParameters(parameters); // Setting camera parameters  
		
		start = (Button) findViewById(R.id.take);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(taking)
				{
					stop = true;
					taking = false;
				}else
				{
					stop = false;
					handler.sendEmptyMessage(MSG_START);
				}
			}
		});
	}
	
	public void takePicture()
	{
		
		camera.startPreview();
		if(setting[1])
		{
			handler.sendEmptyMessage(VIBRATOR);
			camera.takePicture(null, null, pictureCallback);
		}
		else
			camera.autoFocus(auto);
	}
	
	public AutoFocusCallback auto = new AutoFocusCallback() {
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			if(success && camera != null)
			{
				handler.sendEmptyMessage(VIBRATOR);
				camera.takePicture(null, null, pictureCallback); 
			}else
				if(!success && camera != null)
			{
				handler.sendEmptyMessage(MSG_START);
			}
		}
	};
	
	public PictureCallback pictureCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			new MyThread(data).start();
		}
	}; 
	
	public class MyThread extends Thread{
		byte[] data;
		public MyThread(byte[] data) {
			// TODO Auto-generated constructor stub
			this.data = data;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 try {
				 	Date date = new Date();
			        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");//24小时制  
			        filepath = dirPath + sdformat.format(date)+".jpg";  
			        Log.e("111111111", filepath);
			        
	                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	                File file = new File(filepath);
	                if(!file.exists())
	                	file.createNewFile();
	                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
	                bos.flush();// 刷新此缓冲区的输出流
	                bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
	                bitmap.recycle();//回收bitmap空间
//	                Message msg = handler.obtainMessage();
//	                
//	                handler.sendEmptyMessage(TOAST);
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
			 
			 if(stop)
				 return;
			 if(setting[0])
			 {
				 try {
					taking = true;
					if(setting[1])
						Thread.sleep(750);
					else
						Thread.sleep(100);
					handler.sendEmptyMessage(MSG_START);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stop = true;
		if(camera != null)
		{
			camera.release();
			camera = null;
		}
		setting[0] = false;
		super.onDestroy();
	}
}
