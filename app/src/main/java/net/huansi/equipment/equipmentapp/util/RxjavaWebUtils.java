package net.huansi.equipment.equipmentapp.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle.components.RxActivity;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.AttachDtl;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PictureCommitHdr;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.entity.WsEntity;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

/**
 * Created by 单中年 on 2017/1/13.
 */

public class RxjavaWebUtils {


    /**
     * 网络操作 getJsonData
     */
    public static Subscription requestByGetJsonData(RxAppCompatActivity activity,
                                                    final String str,
                                                    final String paraStr,
                                                    final Context context,
                                                    final LoadProgressDialog dialog,
                                                    final String className,
                                                    final boolean isSearch,
                                                    final String errorBySearch,
                                                    final WebListener listener) {
//        if (!NetUtil.isNetworkAvailable(context)) {
//            OthersUtil.ToastMsg(context, context.getResources().getString(R.string.net_no_active));
//            return null;
//        }
        return Observable.just("")
                .subscribeOn(Schedulers.io())
                .compose(activity.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<HsWebInfo>>() {
                    @Override
                    public Observable<HsWebInfo> call(String s) {
                        return Observable.just(getJsonData(context, str, paraStr,
                                className, isSearch, errorBySearch));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HsWebInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        OthersUtil.dismissLoadDialog(dialog);
                        OthersUtil.ToastMsg(context,
                                context.getResources().getString(R.string.connect_server_error));
                        HsWebInfo hsWebInfo = new HsWebInfo();
                        hsWebInfo.success = false;
                        listener.error(hsWebInfo);
                    }

                    @Override
                    public void onNext(HsWebInfo webInfo) {
                        if (!webInfo.success) {
                            OthersUtil.dismissLoadDialog(dialog);
                            OthersUtil.ToastMsg(context, webInfo.error.error);
                            listener.error(webInfo);
                            return;
                        }
                        listener.success(webInfo);
                    }
                });
    }


    public static Subscription requestByNormalFunction(final RxAppCompatActivity activity,
                                                       final Map<String, String> paraMap,
                                                       final String functionName,
                                                       final LoadProgressDialog dialog,
                                                       final String className,
                                                       final boolean isSearch,
                                                       final String errorBySearch,
                                                       final WebListener listener) {
//        if (!NetUtil.isNetworkAvailable(activity)) {
//            OthersUtil.ToastMsg(activity, activity.getResources().getString(R.string.net_no_active));
//            return null;
//        }
        return Observable.just("")
                .subscribeOn(Schedulers.io())
                .compose(activity.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<HsWebInfo>>() {
                    @Override
                    public Observable<HsWebInfo> call(String s) {
                        return Observable.just(normalFunction(activity, paraMap, functionName, className, isSearch, errorBySearch));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HsWebInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        OthersUtil.dismissLoadDialog(dialog);
                        OthersUtil.ToastMsg(activity,
                                activity.getResources().getString(R.string.connect_server_error));
                    }

                    @Override
                    public void onNext(HsWebInfo webInfo) {
                        if (!webInfo.success) {
                            OthersUtil.dismissLoadDialog(dialog);
                            OthersUtil.ToastMsg(activity, webInfo.error.error);
                            listener.error(webInfo);
                            return;
                        }
                        listener.success(webInfo);
                    }
                });
    }

    /**
     * 调取getjsonData或者getCustjsonData
     *
     * @param context
     * @param str
     * @param paraStr
     * @return
     */
    public static HsWebInfo getJsonData(Context context,
                                        String str,
                                        String paraStr,
                                        String className,
                                        boolean isSearch,
                                        String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();
        String json = WsUtil.getJsonDataAsync(context, str, paraStr);
        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if (errorBySearch == null || errorBySearch.isEmpty()) {
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

    /**
     * 调取正常的webservice方法
     *
     * @param context
     * @return
     */
    private static HsWebInfo normalFunction(Context context,
                                            Map<String, String> paraMap,
                                            String functionName,
                                            String className,
                                            boolean isSearch,
                                            String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();

        WebServices webServices = new WebServices(context);
        String json;
        if(NetUtil.isNetworkAvailable(context)) {
            json= webServices.GetData(functionName, paraMap);
        }else {
            json=context.getResources().getString(R.string.net_no_active);
        }
        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if ( errorBySearch.isEmpty()) {
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
        hsWebInfo.json = json;
        hsWebInfo.wsData = JSONEntity.GetWsData(json, className);
        error = WsUtil.getErrorFromWs(context, hsWebInfo.wsData, isSearch, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        return hsWebInfo;
    }


    /**
     * 上传附件
     *
     * @param dataKey
     * @param title
     * @param content
     * @param dialog
     * @param pathList
     * @param dataTpeList
     * @param lastListener 最后一个上传成功后执行的
     */
    public static void uploadAttachments(final RxAppCompatActivity activity,
                                         String dataKey,
                                         String title, String content,
                                         final LoadProgressDialog dialog,
                                         final List<String> pathList,
                                         final List<String> dataTpeList,
                                         final int pictureMaxSize,
                                         final WebListener lastListener) {
        Map<String, String> hdrMap = new HashMap<>();
        hdrMap.put("sAppUserNo", SPHelper.getLocalData(activity, USER_NO_KEY, String.class.getName(), "").toString());
        hdrMap.put("sDataKey", dataKey);
        hdrMap.put("sTitle", title);
        hdrMap.put("sContents", content);
        requestByNormalFunction(activity, hdrMap, "AttachHdrSubmit", dialog, PictureCommitHdr.class.getName(), false, "上传失败！！",
                new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        PictureCommitHdr pictureCommitHdr = (PictureCommitHdr) hsWebInfo.wsData.LISTWSDATA.get(0);
                        commitPicture(pictureCommitHdr, pathList, dataTpeList,activity, dialog,pictureMaxSize, lastListener);
                    }
                });
    }

    private synchronized static void commitPicture(final PictureCommitHdr pictureCommitHdr,
                                                   final List<String> pathList,
                                                   final List<String> dateTypeList,
                                                   final RxAppCompatActivity activity,
                                                   final LoadProgressDialog dialog,
                                                   final int pictureMaxSize,
                                                   final WebListener lastListener) {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        for (int i = 0; i < pathList.size(); i++) {
                            Map<String, String> map = new HashMap();
                            map.put("iSeq", i + "");
                            map.put("uAttachHdrGUID", pictureCommitHdr.IIDEN);
                            map.put("sFolder","file");
                            map.put("sDataType", dateTypeList.get(i));
                            map.put("sLocalPicPath", pathList.get(i));
                            map.put("iSaveToDB", "0");
                            map.put("byteArray", PictureCompressUtils.compressImage(pathList.get(i),pictureMaxSize));
                            HsWebInfo hsWebInfo = normalFunction(activity, map, "AttachDtlSubmit", WsData.class.getName(), false, "上传失败");
                            if (!hsWebInfo.success) return hsWebInfo;
//                            sbPath.append(hsWebInfo.wsData.LISTWSDATA)
                        }
                        StringBuffer sbPath=new StringBuffer();
                        HsWebInfo info=getJsonData(activity,
                                "spAppEPQueryServerPicturePath",
                                "uAttachHdrGUID="+pictureCommitHdr.IIDEN,
                                AttachDtl.class.getName(),
                                true,
                                "上传出错！！");
                        if(!info.success) {
                            info.error.error= "上传失败！！";
                            return info;
                        }
                        List<WsEntity> entities=info.wsData.LISTWSDATA;
                        for(int i=0;i<entities.size();i++){
                            AttachDtl attachDtl= (AttachDtl) entities.get(i);
                            sbPath.append(attachDtl.SFOLDER+"/"+attachDtl.SFILENAME);
                            if(i!=entities.size()-1){
                                sbPath.append(";");
                            }
                        }
                        info.object=sbPath.toString();
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HsWebInfo>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        OthersUtil.dismissLoadDialog(dialog);
                        OthersUtil.showTipsDialog(activity,"上传失败！！");
                    }

                    @Override
                    public void onNext(HsWebInfo hsWebInfo) {
                        if(!hsWebInfo.success){
                            OthersUtil.showTipsDialog(activity,"上传失败！！");
                            return;
                        }
                        lastListener.success(hsWebInfo);
                    }
                });
    }

}
