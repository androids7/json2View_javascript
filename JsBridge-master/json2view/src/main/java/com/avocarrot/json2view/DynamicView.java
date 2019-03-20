package com.avocarrot.json2view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.*;

/**
 * Created by avocarrot on 11/12/2014.
 * parse the json as a tree and create View with its dynamicProperties
 */
public class DynamicView {

    static int mCurrentId = 13;
    static int INTERNAL_TAG_ID = 0x7f020000;

	String id="";
	//不要修改
	static HashMap<Integer,String> idmap;
	
	WeViewHolder holder;
	
	//static WeViewHolder wv;
    /**
     * @param jsonObject : json object
     * @param holderClass : class that will be created as an holder and attached as a tag in the View
     * @return the view that created
     */
	 
	static int wid=0;
	 public DynamicView(){
		 
		 
		 idmap=new HashMap<Integer,String>();
		 
		 
	 }
    public View createView (Context context, JSONObject jsonObject, Class holderClass) {
        return createView(context, jsonObject, null, holderClass);
    }

    /**
     * @param jsonObject : json object
     * @param parent : parent viewGroup
     * @param holderClass : class that will be created as an holder and attached as a tag in the View, If contains HashMap ids will replaced with idsMap
     * @return the view that created
     */
	 //这里只会运行一次
	 //int cc=0;
    public  View createView (Context context, JSONObject jsonObject, ViewGroup parent, Class holderClass) {

		//String id="";
        if (jsonObject==null)
            return null;

        HashMap<String, Integer> ids = new HashMap<>();
		
		
        View container = createViewInternal(context, jsonObject, parent, ids);

        if (container==null)
            return null;

        if (container.getTag(INTERNAL_TAG_ID) != null)
            DynamicHelper.applyLayoutProperties(container, (List<DynamicProperty>) container.getTag(INTERNAL_TAG_ID), parent, ids);

        /* clear tag from properties */
        container.setTag(INTERNAL_TAG_ID, null);

        if (holderClass!= null) {

            try {
				//wv=new WeViewHolder();
				//String idmapstr=new Utils().getMapToString(idmap);
				//System.out.println(idmapstr);
               
			  //idmapstr);
			//  cc++;
			//  System.out.println("Run count:"+cc);
			
	//	Object b=  (WeViewHolder)holderClass.getConstructor().newInstance();
	
	
				holder = new WeViewHolder();
				holder.add(ids);
	
			//这里没有被执行
            holder=  new  DynamicHelper().parseDynamicView(holder, container, ids);
               
			container.setTag(holder);
         
		  
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("error ==="+e.toString());
		}
        }

        return container;

    }

    /**
     * @param jsonObject : json object
     * @param parent : parent viewGroup
     * @return the view that created
     */
    public View createView (Context context, JSONObject jsonObject, ViewGroup parent) {
        return createView(context, jsonObject, parent, null);
    }

    /**
     * @param jsonObject : json object
     * @return the view that created
     */
    public  View createView (Context context, JSONObject jsonObject) {
        return createView(context, jsonObject, null, null);
    }

    /**
     * use internal to parse the json as a tree to create View
     * @param jsonObject : json object
     * @param ids : the hashMap where we keep ids as string from json to ids as int in the layout
     * @return the view that created
     */
	 
	 //view id的
	 
    public  View createViewInternal (Context context, JSONObject jsonObject, ViewGroup parent, HashMap<String, Integer> ids) {

        View view = null;

		
        ArrayList<DynamicProperty> properties;

        try {
            /* Create the View Object. If not full package is available try to create a view from android.widget */
            String widget = jsonObject.getString("widget");
            if (!widget.contains(".")) {
                widget = "android.widget." + widget;
            }
            Class viewClass = Class.forName(widget);
            /* create the actual view object */
            view = (View) viewClass.getConstructor(Context.class).newInstance(new Object[] { context });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (view==null) return null;

        try {

            /* default Layout in case the user not set it */
            ViewGroup.LayoutParams params = DynamicHelper.createLayoutParams(parent);
            view.setLayoutParams(params);

            /* iterrate json and get all properties in array */
            properties = new ArrayList<>();
			//这里能获取到id值
            JSONArray jArray = jsonObject.getJSONArray("properties");
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
				//	System.out.println(jArray.getJSONObject(i));
                    DynamicProperty p = new DynamicProperty(jArray.getJSONObject(i));
                    if (p.isValid())
                        properties.add(p);
                }
            }

            /* keep properties obj as a tag */
            view.setTag(INTERNAL_TAG_ID, properties);

            /* add and integer as a universal id  and keep it in a hashmap */
			//检查是否返回id
             id = DynamicHelper.applyStyleProperties(view, properties);
			 
			 //能获取到id
			 
            if (!TextUtils.isEmpty(id)) {
                /* to target older versions we cannot use View.generateViewId();  */
                ids.put(id, mCurrentId);
                view.setId( mCurrentId );
				idmap.put(wid,id);
				
				
				wid++;
				
                mCurrentId++;
            }
else{
	System.out.println("View check id:"+id);
}
            /* if view is type of ViewGroup check for its children view in json */
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;

                /* parse the aray to get the children views */
                List<View> views = new ArrayList<>();
                JSONArray jViews = jsonObject.optJSONArray("views");
                if (jViews != null) {
                    int count=jViews.length();
                    for (int i=0;i<count;i++) {
                        /* create every child add it in viewGroup and set its tag with its properties */
                        View dynamicChildView =new DynamicView().createViewInternal(context, jViews.getJSONObject(i), parent, ids);
                        if (dynamicChildView!=null) {
                            views.add(dynamicChildView);
                            viewGroup.addView(dynamicChildView);
                        }
                    }
                }
                /* after create all the children apply layout properties
                * we need to do this after al children creation to have create all possible ids */
                for(View v : views) {
                    DynamicHelper.applyLayoutProperties(v, (List<DynamicProperty>) v.getTag(INTERNAL_TAG_ID), viewGroup, ids);
                    /* clear tag from properties */
                    v.setTag(INTERNAL_TAG_ID, null);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

		//holder.add(idmap);
		System.out.println("createView"+idmap);
        return view;

    }

}
