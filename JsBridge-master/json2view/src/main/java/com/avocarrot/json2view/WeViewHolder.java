package com.avocarrot.json2view;
import android.view.*;
import java.util.*;

 public  class WeViewHolder
{
	int point=0;
	public HashMap<Integer,String> id;
	public HashMap<String,View> view;
	//List<String> id;
	public WeViewHolder(){
	//(HashMap<Integer,String> ids) {
		//
	//	String idmap=Utils.getMapToString(ids);
	
		id=new HashMap<Integer,String>();
		
		
		//id=new ArrayList<String>();
		view=new HashMap<String,View>();
		//this.id=new Utils().getStringToMap(ids);
		
		//ids;
		//this.id=Utils.getStringToMap(idmap);
		//ids;
		
		//id.put(point,"testClic");
		/*
		for(int ad=0; ad<ids.size();ad++){
			id.add(ids.get(ad));
			
		}
		
		*/
		
	}
	public void add(HashMap<String,Integer> idsm){
		   String aa=new Utils().getMapToString(idsm);
			
	HashMap<Integer,String> ids=new Utils().getStringToMap(aa);
		for(int ad=0; ad<ids.size();ad++){
			id.put(ad,ids.get(ad+13));

		}

		}
		
	
	
}
