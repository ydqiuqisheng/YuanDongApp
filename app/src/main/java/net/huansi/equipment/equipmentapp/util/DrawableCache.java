package net.huansi.equipment.equipmentapp.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/2/27.
 */
public class DrawableCache {

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
     */
    private class HsSoftRef extends SoftReference<Drawable>{
        private Object mKey=null;

        public HsSoftRef(Drawable r, ReferenceQueue<Drawable> q, Object mKey) {
            super(r, q);
            this.mKey = mKey;
        }
    }

    private static DrawableCache cache;

    /**
     * 存放Cache内容
     */
    private HashMap<Object,HsSoftRef> map;

    /** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
    private ReferenceQueue<Drawable> reQueue;

    private DrawableCache(){
        reQueue=new ReferenceQueue<>();
        map=new HashMap<>();
    }

    public static DrawableCache getInstance(){
        if(cache==null){
            cache=new DrawableCache();
        }
        return cache;
    }


    /**
     *清除软引用中的Bitmap对象
     */
    private void cleanCache(){
        HsSoftRef ref=null;
        while((ref= (HsSoftRef) reQueue.poll())!=null){
            map.remove(ref.mKey);
        }
    }

    /**
     * 手动清除图片的缓存
     */
    public void clearCache(){
        cleanCache();
        map.clear();
        System.gc();
        //运行已被丢弃，垃圾回车器会执行
        System.runFinalization();
    }

    /**
     * 将Bitmap对象放进软引用中
     */
    private void addDrawableToCache(Drawable bmp, int key){
        cleanCache();//清除垃圾引用
        HsSoftRef ref=new HsSoftRef(bmp,reQueue,key);
        map.put(key, ref);
    }


    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     * @return
     */
    public Drawable getDrawable(int resId, Context context){
        Drawable bitmap=null;
        //缓存中是否有Bitmap的对象，有的话直接引用，没有则创建，并放进缓存中
        if(map.containsKey(resId)){
            HsSoftRef ref=map.get(resId);
            bitmap=ref.get();
        }
        if(bitmap==null){
            bitmap= context.getResources().getDrawable(resId);
            addDrawableToCache(bitmap, resId);
        }
        return bitmap;
    }
}
