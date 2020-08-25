package net.huansi.equipment.equipmentapp.util;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;


import java.util.List;

/**
 * Created by SZN on 2016/7/19.
 *
 */
public class PictureAddDialogUtils {
//    private PictureAddDialogUtils(){}
//    private static PictureAddDialogUtils pictureAddDialogUtils;
//    public static PictureAddDialogUtils getInstance(){
//        if(pictureAddDialogUtils==null){
//            pictureAddDialogUtils=new PictureAddDialogUtils();
//        }
//        return pictureAddDialogUtils;
//    }
    private  PictureDialogClick dialogClick;

    public final static int ALBUM_PICTURE_REQUEST_CODE=999;//从相册请求code
    public final static int  CAMERA_PICTURE_REQUEST_CODE=998;//拍照的请求code


    /**
     * 添加图片方式的dialog
     * 显示添加图片的几种方式（相册，相机）
     */
    public  void showPictureAddDialog(FragmentActivity activity) {
        dialogClick=new PictureDialogClick(activity);
        Builder builder=new Builder(activity);
        builder.
                setItems(new String[]{"相册","拍照"},
                        dialogClick)
                .show();
    }

    /**
     * 获得图片的地址
     */
    public String getPicturePath(){
        if(dialogClick==null) return null;
        String path=dialogClick.path;
        dialogClick=null;
        return path;
    }

    /**
     * 选择相册中的图片
     */
    private  void choosePictureByAlbum(FragmentActivity activity){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity. startActivityForResult(intent, ALBUM_PICTURE_REQUEST_CODE);
    }


    /**
     * 通过相机拍照获得图片
     */
    private  String choosePictureByCamera(FragmentActivity activity){
        return OnCamera.getPathFromCamera(activity,CAMERA_PICTURE_REQUEST_CODE);
    }

    class PictureDialogClick implements OnClickListener{
        private FragmentActivity activity;
        String path=null;

        public PictureDialogClick(FragmentActivity activity){
            this.activity=activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                //相册
                case 0:
                    choosePictureByAlbum(activity);
                    path=null;
                    break;
                //相机
                case 1:
                    path=choosePictureByCamera(activity);
                    break;
            }
        }
    }

    /**
     * 选择相册中图片后填充图片容器的内容
     */
    public void initPictureAfterChooseByAlbum(Intent data, FragmentActivity activity, List<String> pathList){
        if(data==null) return;
        Uri uri=data.getData();
        String path=OnCamera.getPictureFromAlbum(activity,uri);
        //没有添加过此图片了
        if (path==null||pathList.contains(path)) {
            OthersUtil.showTipsDialog(activity,"您已添加过此图片");
            return;
        }
        pathList.add(path);
    }

    /**
     * 选择相册中图片后获得的图片地址
     */
    public String getPictureAfterChooseByAlbum(Intent data, FragmentActivity activity){
        if(data==null) return null;
        Uri uri=data.getData();
        String path=OnCamera.getPictureFromAlbum(activity,uri);
        //没有添加过此图片了
        return path;
    }

    /**
     * 拍照后填充图片地址List的数据的内容
     */
    public void initPictureAfterChooseByCamera(Intent data,String picturePath,List<String> pathList){
        if(data == null) {
            if (picturePath != null) {
                String path=picturePath;
                if(!pathList.contains(path)) {
                    pathList.add(path);
                }
            }
        }
    }

}
