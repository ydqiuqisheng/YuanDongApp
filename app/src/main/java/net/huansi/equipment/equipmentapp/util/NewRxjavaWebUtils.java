package net.huansi.equipment.equipmentapp.util;

import android.content.Context;
import android.util.Log;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shanz on 2017/4/24.
 */

public class NewRxjavaWebUtils {

    public static <T> Observable<T[]>  getObservable(RxAppCompatActivity activity,T...ts){
       return Observable.just(ts)
               .compose(activity.<T[]>bindToLifecycle())
                .subscribeOn(Schedulers.io());
    }

    public static <T> Observable<T[]>  getObservable(RxFragment fragment, T...ts){
        return Observable.just(ts)
                .compose(fragment.<T[]>bindToLifecycle())
                .subscribeOn(Schedulers.io());
    }

    public synchronized static <T> Observable<T>  getObservable(RxAppCompatActivity activity,T t){
        return Observable.just(t)
                .compose(activity.<T>bindToLifecycle())
                .retryWhen(new RxRetryConnect(5))
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io());
    }

    public synchronized static Subscription getUIThread(Observable<HsWebInfo> observable,
                                           final Context context,
                                           final LoadProgressDialog dialog,
                                           final WebListener webListener){
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HsWebInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        OthersUtil.dismissLoadDialog(dialog);
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        hsWebInfo.success=false;
                        hsWebInfo.error.error="其他异常:"+e.toString();
                        Log.e("TAG","异常"+e.toString());
                        webListener.error(hsWebInfo);
                    }

                    @Override
                    public void onNext(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(!hsWebInfo.success){
                            webListener.error(hsWebInfo);
                            return;
                        }
                        webListener.success(hsWebInfo);
                    }
                });
    }
//    @SuppressWarnings("unchecked")
//    public static <T>Observable<HsWebInfo> request(Observable observable,
//                                                   final Context context,
//                                                   final WebServiceType webServiceType,
//                                                   final String str,
//                                                   final String  paraStr,
//                                                   final String className,
//                                                   final boolean isSearch,
//                                                   final String errorBySearch){
//        return observable.map(new Func1() {
//            @Override
//            public HsWebInfo call(Object o) {
//                return getJsonData(context,webServiceType,str,paraStr,className,isSearch,errorBySearch);
//            }
//        });
//    }

//    /**
//     * 调取getjsonData或者getCustjsonData
//     *
//     * @param context
//     * @param strs
//     * @param paraStrs
//     * @return
//     */
//    public static HsWebInfo getJsonData(Context context,
//                                        String[] strs,
//                                        String[]  paraStrs,
//                                        String className,
//                                        boolean isSearch,
//                                        String errorBySearch) {
//        HsWebInfo hsWebInfo=null;
//        for(int i=0;i<strs.length;i++){
//            hsWebInfo =getJsonData(context,strs[i],paraStrs[i],className,isSearch,errorBySearch);
//            if(!hsWebInfo.success) return hsWebInfo;
//        }
//        if(hsWebInfo==null) hsWebInfo=new HsWebInfo();
//        return hsWebInfo;
//    }
    /**
     * 调取getjsonData或者getCustjsonData
     *
     * @param context
     * @param str
     * @param paraStr
     * @return
     */
    public synchronized static HsWebInfo getJsonData(Context context,
                                        String str,//过程名,点击事件名称
                                        String paraStr,//过程的参数
                                        String className,
                                        boolean isSearch,
                                        String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();
        String json = WsUtil.getJsonDataAsync(context, str, paraStr);
//        Log.e("TAG","json="+json);
//        Log.e("TAG","isSearch="+isSearch);
        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if (errorBySearch.isEmpty()) {
                hsWebInfo.error.error = "";
                hsWebInfo.success = false;
                return hsWebInfo;
            } else {
                json = errorBySearch;
            }
        }
        String error = WsUtil.getErrorFromWs(context, json, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        hsWebInfo.wsData = JSONEntity.GetWsData(json, className);
        error = WsUtil.getErrorFromWs(context, hsWebInfo.wsData, isSearch, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        return hsWebInfo;
    }
    //可以连指定数据库
    public synchronized static HsWebInfo getJsonDataExt(Context context, String dbName,//数据库名
                                                     String str,//过程名,点击事件名称
                                                     String paraStr,//过程的参数
                                                     String className,
                                                     boolean isSearch,
                                                     String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();
        String json = WsUtil.getJsonDataAsyncExt(context, dbName,str, paraStr);
//        Log.e("TAG","json="+json);
//        Log.e("TAG","isSearch="+isSearch);
        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if (errorBySearch.isEmpty()) {
                hsWebInfo.error.error = "";
                hsWebInfo.success = false;
                return hsWebInfo;
            } else {
                json = errorBySearch;
            }
        }
        String error = WsUtil.getErrorFromWs(context, json, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        hsWebInfo.wsData = JSONEntity.GetWsData(json, className);
        error = WsUtil.getErrorFromWs(context, hsWebInfo.wsData, isSearch, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        return hsWebInfo;
    }
    public static HsWebInfo getNormalFunction(Context context,
                                              String functionName,
                                              Map<String,String> paramMap,
                                              String className,
                                              boolean isSearch,
                                              String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();
        String json="";
        if(!NetUtil.isNetworkAvailable(context)) {
            json=context.getResources().getString(R.string.net_no_active);
        }else {
            WebServices webServices = new WebServices(context);
            json = webServices.GetData(functionName, paramMap);
        }
        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if (errorBySearch.isEmpty()) {
                hsWebInfo.error.error = "";
                hsWebInfo.success = false;
                return hsWebInfo;
            } else {
                json = errorBySearch;
            }
        }
        String error = WsUtil.getErrorFromWs(context, json, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }

        hsWebInfo.wsData = JSONEntity.GetWsData(json, className);
        error = WsUtil.getErrorFromWs(context, hsWebInfo.wsData, isSearch, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        return hsWebInfo;
    }


}
