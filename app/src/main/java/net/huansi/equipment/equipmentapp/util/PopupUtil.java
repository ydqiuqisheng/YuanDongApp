package net.huansi.equipment.equipmentapp.util;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import net.huansi.equipment.equipmentapp.R;


/**
 * Created by Administrator on 2016/5/3.
 */
public class PopupUtil {
    public interface PopDismissListener{
        void dismiss();
    }

//    private static PopupUtil popupUtil;
//    private PopupUtil(){}
//    public static PopupUtil getInstance(){
//        if(popupUtil==null){
//            popupUtil=new PopupUtil();
//        }
//        return popupUtil;
//    }

    public static void showPop(PopupWindow popupWindow,
                               Activity activity,
                               boolean isLayer,
                               PopDismissListener listener){
        setDefaultPop(popupWindow,activity,isLayer,listener);
        setCancel(popupWindow,activity,true);
    }

//    public  void showPop(PopupWindow popupWindow,  Activity activity,int animStyle){
//        setDefaultPop(popupWindow,activity);
//        setCancel(popupWindow,activity,true);
//        popupWindow.setAnimationStyle(animStyle);
//    }

    /**
     * 点击外部不消失
     * @param popupWindow
     * @param activity
     * @param animStyle
     */
    public  static void showPopNoCancel(PopupWindow popupWindow,
                                        Activity activity,
                                        boolean isLayer,
                                        int animStyle,
                                        PopDismissListener listener){
        setDefaultPop(popupWindow,activity,isLayer,listener);
        setCancel(popupWindow,activity,false);
        if(animStyle>-1) popupWindow.setAnimationStyle(animStyle);
    }
//    /**
//     * 点击外部不消失
//     * @param popupWindow
//     * @param activity
//     */
//    public  static void showPopNoCancel(PopupWindow popupWindow,
//                                        Activity activity,
//                                        PopDismissListener listener){
//        showPopNoCancel(popupWindow,activity,listener);
//    }
    /**
     * 点击外部不消失
     * @param popupWindow
     * @param activity
     */
    public  static void showPopNoCancel(PopupWindow popupWindow,
                                        Activity activity){
        showPopNoCancel(popupWindow,activity,false,-1,null);
    }


    private static void setDefaultPop(PopupWindow popupWindow,
                                      final Activity activity,
                                      final boolean isLayer,
                                      final PopDismissListener listener){
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popupWindow.setFocusable(true);
        if(isLayer) {
            // 设置背景颜色变暗
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 0.5f;
            activity.getWindow().setAttributes(lp);
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if(isLayer) {
                    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                    lp.alpha = 1f;
                    activity.getWindow().setAttributes(lp);
                }
                if(listener!=null)listener.dismiss();
            }
        });
    }

    public static void setSimplePop(PopupWindow popupWindow,Context context) {
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.setFocusable(true);
//        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
//                R.drawable.bg_button));
        popupWindow.setOutsideTouchable(true);
    }

    /**
     * 设置点击外部是否消失
     * @param popupWindow
     * @param context
     * @param b
     */
    private static void setCancel(PopupWindow popupWindow, Context context,boolean b){
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.btn_blue_circle_selector));
        popupWindow.setOutsideTouchable(b);
        popupWindow.setFocusable(b);
    }

//    public void setPopDismiss(PopupWindow popupWindow,final Activity activity){
//
//    }

}
