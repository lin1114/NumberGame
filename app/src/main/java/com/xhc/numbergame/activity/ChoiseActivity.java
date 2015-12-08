package com.xhc.numbergame.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xhc.numbergame.R;

public class ChoiseActivity extends Activity {

	 TextView level1,level2,level3,level4,level5,help,saoleilevel1,saoleilevel2,saoleilevel3,
	 saoleihelp,sortlevel1,sortlevel2,sortlevel3,sorthelp,aboutme;
	        

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ActivityCollection.push(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.goodchoisegame);
		init();
	}
	
	void init(){
		level1 = (TextView)findViewById(R.id.level1);
		level2 = (TextView)findViewById(R.id.level2);
		level3 = (TextView)findViewById(R.id.level3);
		level4 = (TextView)findViewById(R.id.level4);
		level5 = (TextView)findViewById(R.id.level5);
		help = (TextView)findViewById(R.id.describesudoku);
		saoleilevel1 = (TextView)findViewById(R.id.saoleilevel1);
		saoleilevel2 = (TextView)findViewById(R.id.saoleilevel2);
		saoleilevel3 = (TextView)findViewById(R.id.saoleilevel3);
		saoleihelp = (TextView)findViewById(R.id.describesaolei);
		sortlevel1 = (TextView)findViewById(R.id.sortlevel1);
		sortlevel2 = (TextView)findViewById(R.id.sortlevel2);
		sortlevel3 = (TextView)findViewById(R.id.sortlevel3);
		sorthelp = (TextView)findViewById(R.id.describesort);
		aboutme = (TextView)findViewById(R.id.aboutme);
		
		Click click = new Click();
		level1.setOnClickListener(click);
		level2.setOnClickListener(click);
		level3.setOnClickListener(click);
		level4.setOnClickListener(click);
		level5.setOnClickListener(click);
		help.setOnClickListener(click);
		saoleilevel1.setOnClickListener(click);
		saoleilevel2.setOnClickListener(click);
		saoleilevel3.setOnClickListener(click);
		saoleihelp.setOnClickListener(click);
		sortlevel1.setOnClickListener(click);
		sortlevel2.setOnClickListener(click);
		sortlevel3.setOnClickListener(click);
		sorthelp.setOnClickListener(click);
		aboutme.setOnClickListener(click);
		
		
	}
	
	void jump(int flag ,int level){
		Intent intent ;
		if(flag == 1){
			intent = new Intent(ChoiseActivity.this,NumAloneActivity.class);
			intent.putExtra("level", ""+level);
			
			startActivity(intent);
		}
		else if(flag == 2){
			intent = new Intent(ChoiseActivity.this,SaoLeiActivity.class);
			intent.putExtra("level", ""+level);
			startActivity(intent);
		}
		else if(flag == 3){
			intent = new Intent(ChoiseActivity.this,SortActivity.class);
			intent.putExtra("level", ""+level);
			startActivity(intent);
		}
	}
	
	class Click implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == level1){
				jump(1,1);
			}
			else if(v == level2){
				jump(1,2);
			}
			else if(v == level3){
				jump(1,3);
			}
			else if(v == level4){
				jump(1,4);
			}
			else if(v == level5){
				jump(1,5);
			}
			else if(v == help){
				Intent intent = new Intent(ChoiseActivity.this,HelpActivity.class);
				intent.putExtra("who", 1+"");
				startActivity(intent);
				
			}
			else if(v == saoleilevel1){
				jump(2,1);
			}
			else if(v == saoleilevel2){
				jump(2,2);
			}
			else if(v == saoleilevel3){
				jump(2,3);
			}
			else if(v == saoleihelp){
				Intent intent = new Intent(ChoiseActivity.this,HelpActivity.class);
				intent.putExtra("who", 2+"");
				startActivity(intent);
			}
			else if(v == sortlevel1){
				jump(3,1);
			}
			else if(v == sortlevel2){
				jump(3,2);
			}
			else if(v == sortlevel3){
				jump(3,3);
			}
			else if(v == sorthelp){
				Intent intent = new Intent(ChoiseActivity.this,HelpActivity.class);
				intent.putExtra("who", 3+"");
				startActivity(intent);
			}
			else if(v == aboutme){
				Intent intent = new Intent(ChoiseActivity.this,AboutMe.class);
				startActivity(intent);
			}
		}
		
	}
}






















