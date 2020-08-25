package net.huansi.equipment.equipmentapp.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.check_goods.CheckMainActivity;
import net.huansi.equipment.equipmentapp.activity.logging_bill.LoggingBillActivity;
import net.huansi.equipment.equipmentapp.entity.EPInventory;
import net.huansi.equipment.equipmentapp.gen.DaoMaster;
import net.huansi.equipment.equipmentapp.gen.DaoSession;
import net.huansi.equipment.equipmentapp.sqlite_db.InventoryDetail;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import static net.huansi.equipment.equipmentapp.constant.Constant.DB_NAME;
import static net.huansi.equipment.equipmentapp.constant.Constant.RFD8500;

/**
 * Created by Administrator on 2016/5/3.
 */
public class OthersUtil {






    /**
     * 设置子listview的高度
     * @param listView
     */
    public  static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    /**
     * listview同步滑动效果
     * */
    public static void setListViewOnTouchAndScrollListener(final ListView listView1, final ListView listView2){


        //设置listview2列表的scroll监听，用于滑动过程中左右不同步时校正
        listView2.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //如果停止滑动
                if(scrollState == SCROLL_STATE_IDLE /*|| scrollState == 1*/){
                    //获得第一个子view
                    View subView = view.getChildAt(0);

                    if(subView !=null){
                        final int top = subView.getTop();
                        final int top1 = listView1.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        //如果两个首个显示的子view高度不等
                        if(top != top1){
                            listView1.setSelectionFromTop(position, top);
                        }
                    }
                }

            }

            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if(subView != null){
                    final int top = subView.getTop();
                    //如果两个首个显示的子view高度不等
                    int top1 = listView1.getChildAt(0).getTop();
                    if(!(top1 - 7 < top &&top < top1 + 7)){
                        listView1.setSelectionFromTop(firstVisibleItem, top);
                        listView2.setSelectionFromTop(firstVisibleItem, top);
                    }

                }
            }
        });

        //设置listview1列表的scroll监听，用于滑动过程中左右不同步时校正
        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE /*|| scrollState == 1*/){
                    //获得第一个子view
                    View subView = view.getChildAt(0);

                    if(subView !=null){
                        final int top = subView.getTop();
                        final int top1 = listView2.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        //如果两个首个显示的子view高度不等
                        if(top != top1){
                            listView1.setSelectionFromTop(position, top);
                            listView2.setSelectionFromTop(position, top);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if(subView != null){
                    final int top = subView.getTop();
                    listView1.setSelectionFromTop(firstVisibleItem, top);
                    listView2.setSelectionFromTop(firstVisibleItem, top);

                }
            }
        });
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     *
     * @return true <br/>false
     */
    public static boolean isDateBigger(String startTime, String endTime) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        long start = 0;
        long end = 0;
        try {
            start = sdf.parse(startTime).getTime();
            end = sdf.parse(endTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (start > end) {
            isBigger = true;
        } else {
            isBigger = false;
        }
        return isBigger;
    }




    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.e("TAG", "本软件的版本名。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime();
    }





    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            Log.e("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    public static Bitmap createVideoThumbnail(String filePath, int kind)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://"))
            {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            }
            else
            {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC); //retriever.getFrameAtTime(-1);
        }
        catch (IllegalArgumentException ex)
        {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        }
        catch (RuntimeException ex)
        {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                retriever.release();
            }
            catch (RuntimeException ex)
            {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null)
        {
            return null;
        }

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND)
        {//压缩图片 开始处
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512)
            {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }//压缩图片 结束处
        }
        else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND)
        {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 判断服务是否正在运行
     * @param strServiceName
     * @return
     */
    public  static boolean isServiceRunning(Context context, String strServiceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            String allServiceName = service.service.getClassName();
            if (allServiceName.equals(strServiceName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Dilaog点击外部不消失
     * @param dialog
     */
    public  static void dialogNotDismissClickOut(DialogInterface dialog){
        /**
         * 不关闭dailog
         */
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dilaog点击外部消失
     * @param dialog
     */
    public  static void dialogDismiss(DialogInterface dialog){
        /**
         * 不关闭dailog
         */
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //url中文编码转换utf_8
    public static String encodeUrl(String url) {
        return Uri.encode(url, "-![.:/,%?&=]");
    }

    // java语言md5加密
    public  static String getMD5AddCttsoft(String info) {
        MessageDigest md = null;
        info=info+"cttsoft";
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(info.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }

    }

    /**
     * UTF-8 一个汉字占三个字节
     * @param str 源字符串
     * 转换成字节数组的字符串
     * @return
     */
    public  static byte[] StringToByte(String str,String charEncode) {
        byte[] destObj = null;
        try {
            if(null == str || str.trim().equals("")){
                destObj = new byte[0];
                return destObj;
            }else{
                destObj = str.getBytes(charEncode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return destObj;
    }





    public  static String GetMatchString(String sOrgString) {
        String sNewString = "";
        char[] array = sOrgString.toCharArray();
        int iStdLength = 32;        //36个字符或18个汉字的长度；
        int iCurLength = 0;
        for(int i=0;i<array.length;i++)
        {
            if((char)(byte)array[i]!=array[i]){
                iCurLength=iCurLength+2;
            }else{
                iCurLength++;
            }
            sNewString = sNewString + array[i];
            if(iCurLength>=iStdLength)
            {
                sNewString = sNewString + "...";
                break;
            }
        }
        return sNewString;
    }

    public  static String GetMatchStringFit(String sOrgString,int ScreenWidth) {
        String sNewString = "";
        char[] array = sOrgString.toCharArray();
        int iStdLength = 32;        //36个字符或18个汉字的长度；
        int iCurLength = 0;
        for(int i=0;i<array.length;i++)
        {
            if((char)(byte)array[i]!=array[i]){
                iCurLength=iCurLength+2;
            }else{
                iCurLength++;
            }
            sNewString = sNewString + array[i];
            if(iCurLength>=iStdLength)
            {
                sNewString = sNewString + "...";
                break;
            }
        }
        return sNewString;
    }

    public static  void ToastMsg(Context context,String sMsg) {
        if(sMsg==null||sMsg.trim().isEmpty()) return;
        Toast.makeText(context,sMsg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 把逗号转为大写的
     */
    public static String replaceComma(String sMsg){
        sMsg=sMsg.replace(",","，");
        return sMsg;
    }


    /**
     * 0或者1转成boolean
     * @return
     */
    public static boolean parseNumberToBoolean(String number){
        return Integer.valueOf(number)==1?true:false;
    }

    /**
     * 跳转activity
     * @param context
     * @param aClass
     */
    public static void intentToActivity(Context context,Class aClass){
        Intent intent=new Intent();
        intent.setClass(context,aClass);
        context.startActivity(intent);
    }

    /**
     * 跳转activity并传递bundle
     * @param context
     * @param aClass
     */
    public static void intentToActivity(Context context,Class aClass,Bundle bundle){
        Intent intent=new Intent();
        intent.setClass(context,aClass);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }



//    public static void log(String s){
//        Log.i(HsApplication.TAG,s);
//    }

    /**
     * dialog消失
     * @param dialog
     */
    public static void dismissLoadDialog(LoadProgressDialog dialog){
        if(dialog==null) return;
        if(dialog.isShowing()) dialog.dismiss();
    }

    /**
     * dialog显示
     * @param dialog
     */
    public static void showLoadDialog(LoadProgressDialog dialog){
        if(dialog==null) return;
        if(!dialog.isShowing()) dialog.showLoadDialog("加载中...");
    }


    /**
     * 根据地址判断是不是图片类型的
     * @return
     */
    public static boolean isPictureFromPath(String path){
        String nameTail=path.substring(path.length()-4,path.length()).toLowerCase();
        if(nameTail.equalsIgnoreCase(".png")||
                nameTail.equalsIgnoreCase(".bmp")||nameTail.equalsIgnoreCase("gif")||
                nameTail.equalsIgnoreCase(".jpg")){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 清除StringBuffer的数据
     */
    public static void clearSb(StringBuffer ...sb){
        for(int i=0;i<sb.length;i++) {
            sb[i].delete(0, sb[i].length());
        }
    }


    /**
     * 弹出访问网络的情况下报错的信息
     * @return
     */
    public static StringBuffer toastErrorFromWs(Context context,StringBuffer sbError){
        if(sbError.toString().isEmpty())return sbError;
        ToastMsg(context,sbError.toString());
        clearSb(sbError);
        return sbError;
    }


    /*
    * 图片压缩的方法01：质量压缩方法
    */
    public static String compressImage(Bitmap beforBitmap,int maxSize) {

        // 可以捕获内存缓冲区的数据，转换成字节数组。
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (beforBitmap != null) {
            // 第一个参数：图片压缩的格式；第二个参数：压缩的比率；第三个参数：压缩的数据存放到bos中
            beforBitmap.compress(CompressFormat.JPEG, 100, bos);
            int options = 100;
            int ii=bos.toByteArray().length;
            // 循环判断压缩后的图片是否是大于100kb,如果大于，就继续压缩，否则就不压缩
            while (bos.toByteArray().length / 1024 > maxSize) {
                bos.reset();// 置为空
                // 压缩options%
                beforBitmap.compress(CompressFormat.JPEG, options, bos);
                // 每次都减少10
                options -= 10;
            }
            return Base64.encodeToString(bos.toByteArray(),Base64.DEFAULT);
        }
        return null;
    }






    /**
     * 第一次进去不弹出输入框
     * @param activity
     */
    public static void hideInputFirst(Activity activity){
        //不弹出输入法
        activity. getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     *  注册Event
     * @param o
     */
    public static void registerEvent(Object o){
        Log.i("TAG","registerEvent====>"+EventBus.getDefault().isRegistered(o)+"===>"+o.toString());
        if(!EventBus.getDefault().isRegistered(o)){
            EventBus.getDefault().register(o);
        }
    }


    /**
     *  取消注册Event
     * @param o
     */
    public static void unregisterEvent(Object o){
        Log.i("TAG","unregisterEvent====>"+EventBus.getDefault().isRegistered(o)+"===>"+o.toString());
        if(EventBus.getDefault().isRegistered(o)){
            EventBus.getDefault().unregister(o);
        }
    }

    /**
     * pix转换dp
     */
    public static float pixToDp(int size,Context context){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources()
                .getDisplayMetrics());
    }




    public static int dp2px(int dp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    /**\
     * 删除字符串的user_
     * @return
     */
    public static String delUser_(String str){
        if(str.contains("user_")){
            str=str.replace("user_","");
        }
        return str;
    }











    /**
     * 获得 greenDao的session
     * @param context
     * @return
     */
    public static DaoSession getGreenDaoSession(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    /**
     * 保留两位小数
     */
    public static double save2Dot(int digit,double number){
        StringBuffer sbZeros=new StringBuffer();
        sbZeros.append("#.");
        for(int i=0;i<digit;i++){
            sbZeros.append("0");
        }
        DecimalFormat df = new DecimalFormat(sbZeros.toString());
        return Double.parseDouble(df.format(number));
    }

    /**
     * 显示提示信息
     * @param activity
     * @param content
     */
    public static void showTipsDialog(Activity activity,String content){
        if(content==null||content.isEmpty()) return;
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setMessage(content)
                .setPositiveButton("确认", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }


    /**
     * 显示提示信息
     * @param activity
     * @param content
     */
    public static void showTipsDialog(Activity activity,String content,OnClickListener onClickListener){
        if(content==null||content.isEmpty()) return;
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setMessage(content)
                .setPositiveButton("确认", onClickListener)
                .setCancelable(false)
                .show();
    }

    /**
     * 显示选择框
     * @param activity
     * @param content
     */
    public static void showChooseDialog(Activity activity, String content, OnClickListener listener){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setMessage(content)
                .setPositiveButton("确认", listener)
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 显示选择框
     * @param activity
     * @param content
     */
    public static void showDoubleChooseDialog(Activity activity, String content,
                                        OnClickListener cancelListener,
                                        OnClickListener okListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setMessage(content)
                .setPositiveButton("确认", okListener)
                .setNegativeButton("取消", cancelListener)
                .setCancelable(false)
                .show();
    }

    /**
     * method to check whether BT device is RFID reader
     *
     * @param device device to check
     * @return true if {@link BluetoothDevice} is RFID Reader, other wise it will be false
     */
    public static boolean isRFIDReader(BluetoothDevice device) {
        if (device.getName().startsWith(RFD8500)) return true;
        return false;
    }

    /**
     * 将EPInventory（服务器） 转成InventoryDetail（本地SQLite）
     * @param inventoryId 本地SQLite的盘点主表的id
     * @param  status -1 盘亏 0 正常 1盘盈
     * @param epInventory
     * @return
     */
    public static InventoryDetail exchangeByDownInventory(EPInventory epInventory,long inventoryId,int status ){
        InventoryDetail inventoryDetail =new InventoryDetail();//本地保存的数据
        inventoryDetail.setEquipmentName(epInventory.EQUIPMENTNAME);

        inventoryDetail.setInventoryHdrIdInSQLite(inventoryId);
        inventoryDetail.setEquipmentChildId(epInventory.IEQUIPMENTDETAILID);
        inventoryDetail.setEquipmentParentId(epInventory.IEQUIPMENTID);
        inventoryDetail.setInventoryParentId(epInventory.SINVENTORYID);

        inventoryDetail.setAssetsCode(epInventory.ASSETSCODE);
        inventoryDetail.setCostCenter(epInventory.COSTCENTER);
        inventoryDetail.setOutFactoryCode(epInventory.OUTFACTORYCODE);
        inventoryDetail.setInFactoryDate(epInventory.INFACTORYDATE);
        inventoryDetail.setDepreciationStartingDate(epInventory.DEPRECIATIONSTARTINGDATE);
        inventoryDetail.setDeclarationNum(epInventory.DECLARATIONNUM);
        inventoryDetail.setEPCode(epInventory.EPCCODE);
        inventoryDetail.setFactory(epInventory.FACTORY);

        inventoryDetail.setScanTime(TimeUtils.getTime(new Date(),"-"));
        inventoryDetail.setStatus(status);
        return inventoryDetail;
    }

    /**
     * 播放音效
     * @param resid
     */
    public static void broadVoice(Context context, int resid){
        BroadVoice voice=null;
        if(voice==null){
            voice=new BroadVoice(context,resid);
            voice.start();
            voice.stop();
            voice=null;
        }
    }


    /**
     * 设置文本的内容（红色+灰色）
     * @param color
     * @param view
     * @param redStr"取消扫描后弹框(扫描成功才起效果,否则设置无效)"
     * @param isAfterByColor true=特殊颜色放在后面
     * @param normalStr
     */
    public static void setTextWithColor(int color,TextView view,String redStr,String normalStr,boolean isAfterByColor){
        TextAppearanceSpan span=new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(color), null);
        SpannableStringBuilder  spanBuilder = new SpannableStringBuilder(isAfterByColor?normalStr+redStr:redStr+normalStr);
        if(isAfterByColor) {
            spanBuilder.setSpan(span, normalStr.length(),normalStr.length()+ redStr.length(),
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }else {
            spanBuilder.setSpan(span, 0, redStr.length(),
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        view.setText(spanBuilder);
    }

}
