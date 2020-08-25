package net.huansi.equipment.equipmentapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPHelper {
	public static final String USER_NO_KEY="userNo";//用户名的key
    public static final String ROLE_CODE_KEY="roleCode";//角色的key
    public static final String USER_PWS="userPassword";//用户密码
	public static final String INVENTORY_POWER_LEVEL="inventory_power_level";//盘点信号最大
	public static final String IP_KEY="ip";



	public static void saveLocalData(Context context, String key, Object value,String  className) {
		SharedPreferences preferences = context.getSharedPreferences("EquipmentApp", Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();

		if (className.equalsIgnoreCase(Integer.class.getName())) {
			editor.putInt(key, (Integer) value);
		} else if (className.equalsIgnoreCase(String.class.getName())) {
			editor.putString(key, value.toString());
		} else if (className.equalsIgnoreCase(Boolean.class.getName())) {
			editor.putBoolean(key, (Boolean) value);
		} else if (className.equalsIgnoreCase(Float.class.getName())) {
			editor.putFloat(key, (Float) value);
		} else if (className.equalsIgnoreCase(Long.class.getName())) {
			editor.putLong(key, (Long) value);
		}
		editor.commit();
	}

	public synchronized static Object getLocalData(Context context, String key,String  className,Object defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences("EquipmentApp", Activity.MODE_PRIVATE);
		if(className.equalsIgnoreCase(Integer.class.getName())) return preferences.getInt(key, (Integer) defaultValue);
		if(className.equalsIgnoreCase(String.class.getName())) return preferences.getString(key, defaultValue.toString());
		if(className.equalsIgnoreCase(Boolean.class.getName())) return preferences.getBoolean(key, (Boolean) defaultValue);
		if(className.equalsIgnoreCase(Float.class.getName())) return preferences.getFloat(key, (Float) defaultValue);
		if(className.equalsIgnoreCase(Long.class.getName())) return preferences.getLong(key, (Long) defaultValue);
        return "";
	}
}
