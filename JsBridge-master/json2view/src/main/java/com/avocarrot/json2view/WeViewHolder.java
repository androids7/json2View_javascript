package com.avocarrot.json2view;
import android.view.*;
import java.util.*;

 public  class WeViewHolder
{
	public HashMap<Integer,String> id;
	public HashMap<String,View> view;
	
	public WeViewHolder() {
		id=new HashMap<Integer,String>();
		
		
		view=new HashMap<String,View>();
		
		
		id.put(0,"testClick");
		
	}
}
