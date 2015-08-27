package com.example.MyNotePad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	String PASSWD = "666";
	String DELETE = "754172";
	EditText passwd = null;
	Button start = null;
	ToggleButton tb1 = null;
	ToggleButton tb2 = null;
	boolean[] setting = {false,false};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		passwd = (EditText) findViewById(R.id.password);
		passwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), start +"-"+ before+"-" + count+"-" + s, Toast.LENGTH_LONG).show();
				if(s.toString().equals(PASSWD))
				{
					Intent intent = new Intent(MainActivity.this,TakePhoto.class);
					intent.putExtra("", setting);
					startActivity(intent);
					finish();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_LONG).show();
			}
		});
		tb1 = (ToggleButton) findViewById(R.id.tb1);
		tb2 = (ToggleButton) findViewById(R.id.tb2);
		tb1.setChecked(true);
		setting[0] = true;
		
		tb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), isChecked+"自动", Toast.LENGTH_LONG).show();
				setting[0] = isChecked;
			}
		});
		
		tb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), isChecked+"前后", Toast.LENGTH_LONG).show();
				setting[1] = isChecked;
			}
		});
		passwd.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_GO)
				{
					if(passwd.getText().toString().equals(PASSWD))
					{
//						Toast.makeText(getApplicationContext(), 
//								"进入传送门", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MainActivity.this,TakePhoto.class);
						intent.putExtra("", setting);
						startActivity(intent);
						finish();
					}
					else if(passwd.getText().toString().equals(DELETE))
					{
						//删除记录
						Toast.makeText(getApplicationContext(),
								"SD卡挂载失败，即将退出", Toast.LENGTH_SHORT).show();
						finish();
					}else
					{
						Toast.makeText(getApplicationContext(),
								"网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
					}
				}
				return true;
			}
		});
		
	}
}
