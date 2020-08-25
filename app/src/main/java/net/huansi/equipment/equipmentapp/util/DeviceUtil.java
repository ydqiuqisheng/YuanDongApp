package net.huansi.equipment.equipmentapp.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.compat.BuildConfig;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Administrator on 2016/5/3.
 */
public class DeviceUtil {
    public static final String XIAOMI_BRAND_LOW="xiaomi";
    public static final String MEIZU_BRAND_LOW="meizu";
    public static final String HUAWEI_BRAND_LOW="huawei";
//    private static DeviceUtil screenUtil;
//    private DeviceUtil(){}
//    public static DeviceUtil getInstance(){
//        if(screenUtil==null){
//            screenUtil=new DeviceUtil();
//        }
//        return screenUtil;
//    }



    public static int getScreenWidth(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 获取DrivceNo
     * @return
     */
    public static String  getPhoneDrivceNo(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return  tm.getDeviceId();
    }

    /**
     * 跳转到miui的权限管理页面
     */
    private static void gotoMiuiPermission(Context context) {
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        i.setComponent(componentName);
        i.putExtra("extra_pkgname", context.getPackageName());
        try {
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            gotoMeizuPermission(context);
        }
    }

    /**
     * 华为的权限管理页面
     * 华为的系统由于不太一样，有些系统是华为自己的权限管理，而6.0的是用的原生的权限管理页面，目前手上只有一台6.0的华为手机，
     */
    private static void gotoHuaweiPermission(Context context) {
        try {
            Intent intent;
            if(Build.VERSION.SDK_INT<23) {
                intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName comp = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
                intent.setComponent(comp);
            }else {
                intent=getAppDetailSettingIntent(context);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            gotoHuaweiPermission(context);
        }
    }

    /**
     * 跳转到其他手机的权限管理系统
     * @param context
     */
    private static void gotoOtherPermission(Context context){
        Intent intent=getAppDetailSettingIntent(context);
        context.startActivity(intent);
    }

    /**
     * 获取应用详情页面intent 针对其他手机
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return localIntent;
    }

    /**
     * 获取手机品牌
     */
    public static String getPhoneBrand(){
        return Build.BRAND;
    }

    /**
     * 进入权限管理
     */
    public static void gotoPermission(Context context){
        gotoOtherPermission(context);
//        switch (getPhoneBrand().toLowerCase()){
//            //小米
//            case XIAOMI_BRAND_LOW:
//                gotoMiuiPermission(context);
//                break;
//            //魅族
//            case MEIZU_BRAND_LOW:
//                gotoMeizuPermission(context);
//                break;
//            //华为
//            case HUAWEI_BRAND_LOW:
//                gotoMiuiPermission(context);
//                break;
//            default:
//                gotoOtherPermission(context);
//                break;
//        }
    }




    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity,View[] topCancelViews,View []bottomCancelViews) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 去除view的高度
        int top=0;//顶部的高度
        int bottom=0;//底部的高度
        if(topCancelViews!=null) {
            for (int i = 0; i < topCancelViews.length; i++) {
                View v = topCancelViews[i];
                top+= v.getHeight();
            }
        }
        if(bottomCancelViews!=null) {
            for (int i = 0; i < bottomCancelViews.length; i++) {
                View v = bottomCancelViews[i];
                Rect frame = new Rect();
                v.getWindowVisibleDisplayFrame(frame);
                bottom+=v.getHeight();
            }
        }
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, top+statusBarHeights, width, height
                - top-bottom-statusBarHeights);
        view.destroyDrawingCache();
        return b;
    }
}
