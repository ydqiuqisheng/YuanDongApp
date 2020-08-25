package net.huansi.equipment.equipmentapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/2/27.
 */
public class BitmapCache {

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
     */
    private class HsSoftRef extends SoftReference<Bitmap>{
        private Object mKey=null;

        public HsSoftRef(Bitmap r, ReferenceQueue<Bitmap> q, Object mKey) {
            super(r, q);
            this.mKey = mKey;
        }
    }

    private static BitmapCache cache;

    /**
     * 存放Cache内容
     */
    private HashMap<Object,HsSoftRef> map;

    /** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
    private ReferenceQueue<Bitmap> reQueue;

    private BitmapCache(){
        reQueue=new ReferenceQueue<>();
        map=new HashMap<>();
    }

    public static BitmapCache getInstance(){
        if(cache==null){
            cache=new BitmapCache();
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
    private void addBitmapToCache(Bitmap bmp, int key){
        cleanCache();//清除垃圾引用
        HsSoftRef ref=new HsSoftRef(bmp,reQueue,key);
        map.put(key,ref);
    }

    /**
     * 将Bitmap对象放进软引用中
     */
    private void addBitmapToCache(Bitmap bmp, String path){
        cleanCache();//清除垃圾引用
        HsSoftRef ref=new HsSoftRef(bmp,reQueue,path);
        map.put(path,ref);
    }

    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     * @return
     */
    public Bitmap getBitmap(int resId, Context context){
        Bitmap bitmap=null;
        //缓存中是否有Bitmap的对象，有的话直接引用，没有则创建，并放进缓存中
        if(map.containsKey(resId)){
            HsSoftRef ref=map.get(resId);
            bitmap=ref.get();
        }
        if(bitmap==null){
            bitmap= BitmapFactory.decodeResource(context.getResources(),resId);
            addBitmapToCache(bitmap, resId);
        }
        return bitmap;
    }

    public Bitmap getBitmap(Uri uri, ContentResolver resolver) throws FileNotFoundException {
        Bitmap bitmap=null;
        //缓存中是否有Bitmap的对象，有的话直接引用，没有则创建，并放进缓存中
        if(map.containsKey(uri.getPath())){
            HsSoftRef ref=map.get(uri.getPath());
            bitmap=ref.get();
        }
        if(bitmap==null){
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            //3.设置位图颜色显示优化方式
            //ALPHA_8：每个像素占用1byte内存（8位）
            //ARGB_4444:每个像素占用2byte内存（16位）
            //ARGB_8888:每个像素占用4byte内存（32位）
            //RGB_565:每个像素占用2byte内存（16位）
            //Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。
            // 但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。
            // 我们来做一个简单的计算题：3200*2400*4 bytes //=30M。
            // 如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
            opts.inPurgeable = true;
            //5.设置位图缩放比例
            //width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；
            // 例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。
            // 相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
            opts.inSampleSize = 2;
            //6.设置解码位图的尺寸信息
            opts.inInputShareable = true;
            bitmap= BitmapFactory.decodeStream(resolver.openInputStream(uri),null, opts);
            addBitmapToCache(bitmap,uri.getPath());
        }
        return bitmap;
    }

    /**
     * 判断传进来的Bitmap是否存在
     * @return
     */
    public Bitmap getBitmap(Bitmap b){
        Bitmap bitmap=null;
        //缓存中是否有Bitmap的对象，有的话直接引用，没有则创建，并放进缓存中
        if(map.containsKey(b.toString())){
            HsSoftRef ref=map.get(b.toString());
            bitmap=ref.get();
        }
        if(bitmap==null){
            bitmap=b;
            addBitmapToCache(bitmap, b.toString());
        }
        return bitmap;
    }


    /**
     * 依据所指定的地址，获取Bitmap对象
     * @param path
     * @return
     */
    public Bitmap getBitmap(String path){
        Bitmap bitmap=null;
        //缓存中是否有Bitmap的对象，有的话直接引用，没有则创建，并放进缓存中
        if(map.containsKey(path)){
            HsSoftRef ref=map.get(path);
            bitmap=ref.get();
        }

        if(bitmap==null){
            InputStream is = null;
            try {

                is = new FileInputStream(path);
                //2.为位图设置100K的缓存
                BitmapFactory.Options opts=new BitmapFactory.Options();
                opts.inTempStorage = new byte[100 * 1024];
                //3.设置位图颜色显示优化方式
                //ALPHA_8：每个像素占用1byte内存（8位）
                //ARGB_4444:每个像素占用2byte内存（16位）
                //ARGB_8888:每个像素占用4byte内存（32位）
                //RGB_565:每个像素占用2byte内存（16位）
                //Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。
                // 但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。
                // 我们来做一个简单的计算题：3200*2400*4 bytes //=30M。
                // 如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
                opts.inPurgeable = true;
                //5.设置位图缩放比例
                //width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；
                // 例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。
                // 相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
                opts.inSampleSize =2;
                //6.设置解码位图的尺寸信息
                opts.inInputShareable = true;
                //7.解码位图
                bitmap =BitmapFactory.decodeStream(is,null, opts);
                addBitmapToCache(bitmap, path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        return bitmap;
    }

    private Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                //这里一定要将其设置回false，因为之前我们将其设置成了true
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
