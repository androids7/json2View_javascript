package com.avocarrot.json2view;
import java.util.*;

public class Utils
{
	
	
	
	public String getMapToString(HashMap<String,Integer> map){
		Set<String> keySet = map.keySet();
		//将set集合转换为数组
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		//给数组排序(升序)
		Arrays.sort(keyArray);
		//因为String拼接效率会很低的，所以转用StringBuilder。博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyArray.length; i++) {
			// 参数值为空，则不参与签名 这个方法trim()是去空格
			//
			
	if (map.get(keyArray[i])> 0) {
				sb.append(keyArray[i]).append(":").append(map.get(keyArray[i]));
			}
			if(i != keyArray.length-1){
				sb.append(",");
			}
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	/*
	public  HashMap<Integer,String> getStringToMap(String str){
		//感谢bojueyou指出的问题
		//判断str是否有值
		if(null == str || "".equals(str)){
			return null;
		}
		//根据&截取
		String[] strings = str.split("&");
		//设置HashMap长度
		int mapLength = strings.length;
		//判断hashMap的长度是否是2的幂。
		if((strings.length % 2) != 0){
			mapLength = mapLength+1;
		}

		HashMap<Integer,String> map = new HashMap<>(mapLength);
		//循环加入map集合
		for (int i = 0; i < strings.length; i++) {
			//截取一组字符串
			String[] strArray = strings[i].split("=");
			//strArray[0]为KEY  strArray[1]为值
			map.put(Integer.parseInt( strArray[0]),strArray[1]);
		}
		return map;
	}
	
	*/
	/*
	
	public  String getMapToString(HashMap<Integer,String> map){
		Set<Integer> keySet = map.keySet();
		//将set集合转换为数组
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		//给数组排序(升序)
		Arrays.sort(keyArray);
		//因为String拼接效率会很低的，所以转用StringBuilder
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyArray.length; i++) {
			// 参数值为空，则不参与签名 这个方法trim()是去空格
			if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
				sb.append(keyArray[i]).append("@").append(String.valueOf(map.get(keyArray[i])).trim());
			}
			if(i != keyArray.length-1){
				sb.append(",");
			}
		}
		System.out.println("MapToString "+sb.toString());
		return sb.toString();
	}
*/
	/**
	 * 
	 * String转map
	 * @param str
	 * @return
	 */
	public HashMap<Integer,String> getStringToMap(String str){
		//根据逗号截取字符串数组
		String[] str1 = str.split(",");
		//创建Map对象
		HashMap<Integer,String> map = new HashMap<>();
		//循环加入map集合
		for (int i = 0; i < str1.length; i++) {
			//根据":"截取字符串数组
			String[] str2 = str1[i].split(":");
			//str2[0]为KEY,str2[1]为值
			
			
			
			
			String stt=str2[0];
			
			if(!stt.equals("")){
				map.put(  Integer.parseInt(str2[1])  ,str2[0]);
			
			}
			
		}
		System.out.println("getStringToMap "+map);
		return map;
	}
	
}
