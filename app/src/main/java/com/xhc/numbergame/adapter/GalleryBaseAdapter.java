package com.xhc.numbergame.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xhc.numbergame.R;
import com.xhc.numbergame.activity.NumAloneActivity;
import com.xhc.numbergame.activity.SaoLeiActivity;
import com.xhc.numbergame.activity.SortActivity;
import com.xhc.numbergame.entity.GameEntity;
import com.xhc.numbergame.entity.SaoLeiArray;

public class GalleryBaseAdapter extends BaseAdapter {
    private ArrayList<GameEntity> game;
    private ViewHolder holder ;
    private LayoutInflater mflater;
    private Context context;
	public GalleryBaseAdapter(ArrayList<GameEntity> game,Context context){
		this.game = game;
		this.context = context;
		mflater = LayoutInflater.from(context);;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return game.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mflater.inflate(R.layout.choiseitem, null);
			holder.tvName = (TextView)convertView.findViewById(R.id.name);
			holder.hard = (Button)convertView.findViewById(R.id.hard);
			holder.simple = (Button)convertView.findViewById(R.id.simple);
			holder.easy = (Button)convertView.findViewById(R.id.easy);
			holder.tvName.setText(game.get(position).gameName);
			convertView.setTag(holder);
		}
			holder = (ViewHolder)convertView.getTag();
			holder.tvName.setText(game.get(position).gameName);
			holder.hard.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = null;
					if(game.get(position).gameName.equals("Êý×ÖÅÅÐò")){
						intent = new Intent(context,SortActivity.class);
							
					}
					else if(game.get(position).gameName.equals("É¨À×")){
						intent = new Intent(context,SaoLeiActivity.class);
						 
					}
					else if(game.get(position).gameName.equals("Êý¶À")){
						intent = new Intent(context,NumAloneActivity.class);
					}
					if(intent != null){
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        context.startActivity(intent);
					}
					
					
				}
			});
		return convertView;
	}
	
	class ViewHolder{
		TextView tvName;
		Button hard,simple,easy; 
	}
}








