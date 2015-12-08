package com.xhc.numbergame.activity;

import java.util.ArrayList;

import android.app.Activity;

/*activityջ*/ 
public class ActivityCollection {
    static ArrayList<Activity> listActivity  = new ArrayList<Activity>();
    
    public static void push(Activity activity){
    	listActivity.add(activity);
    }
 
    public static Activity pop(){
    	return listActivity.get(listActivity.size() - 1);
    }
	
    public static void exit(){
    	for(Activity a : listActivity){
    		a.finish();
    	}
    }
}
