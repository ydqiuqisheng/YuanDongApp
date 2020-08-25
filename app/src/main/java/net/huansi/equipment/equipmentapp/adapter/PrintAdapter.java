package net.huansi.equipment.equipmentapp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import net.huansi.equipment.equipmentapp.R;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhou.mi on 2018/3/5.
 */

public class PrintAdapter {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static class MyPrintAdapter extends PrintDocumentAdapter {

        Context mContext;
        private int pageHeight;
        private int pageWidth;
        public PdfDocument myPdfDocument;
        public int totalpages = 1;//设置一共打印一张纸

        public MyPrintAdapter(Context context) {//这里传各种需要的参数就行
            this.mContext = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle metadata) {

            Log.e("TAG", "--------run onLayout");
            myPdfDocument = new PrintedPdfDocument(mContext, newAttributes); //创建可打印PDF文档对象

            pageHeight =
                    newAttributes.getMediaSize().getHeightMils() / 1000 * 72; //设置尺寸,为什么是1000 * 72, 72dpi
            pageWidth =
                    newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

            if (totalpages > 0) {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder("whiteRadish")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalpages);  //构建文档配置信息

                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }

        @Override
        public void onWrite(final PageRange[] pageRanges, final ParcelFileDescriptor destination, final CancellationSignal cancellationSignal,
                            final WriteResultCallback callback) {

            Log.e("TAG", "--------run onWrite");

            for (int i = 0; i < totalpages; i++) {
                if (pageInRange(pageRanges, i)) //保证页码正确
                {
                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                            pageHeight, i).create();//创建对应的Page

                    PdfDocument.Page page =
                            myPdfDocument.startPage(newPage);  //创建新页面

                    if (cancellationSignal.isCanceled()) {  //取消信号
                        callback.onWriteCancelled();
                        myPdfDocument.close();
                        myPdfDocument = null;
                        return;
                    }
                    drawPage(page, i);  //将内容绘制到页面Canvas上
                    myPdfDocument.finishPage(page);
                }
            }

            try {
                myPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                myPdfDocument.close();
                myPdfDocument = null;
            }

            callback.onWriteFinished(pageRanges);
        }

        private boolean pageInRange(PageRange[] pageRanges, int page) {
            for (int i = 0; i < pageRanges.length; i++) {
                if ((page >= pageRanges[i].getStart()) &&
                        (page <= pageRanges[i].getEnd()))
                    return true;
            }
            return false;
        }

        //页面绘制
        private void drawPage(PdfDocument.Page page,
                              int pagenumber) {
            Canvas canvas = page.getCanvas();

            //这里是页码。页码不能从0开始
            pagenumber++;

            Paint paint = new Paint();
            PdfDocument.PageInfo pageInfo = page.getInfo();

            //draw heart rate line
            //if (pageWidth > pageHeight){
                drawViewsOnPaper(canvas, paint);
                Log.e("TAG","onPaint");
            //}
        }

        //draw views
        private void drawViewsOnPaper(Canvas canvas, Paint paint){
            paint.setStrokeWidth(3);
            paint.setTextSize(60);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.STROKE);
//            float [] pts = {
//                    300,300,300,400,
//                    300,400,400,400,
//                    400,400,400,500
//            };
//            canvas.drawLines(pts,paint);
//            canvas.drawCircle(600,600,100,paint);

            canvas.drawText("账务卡",300,100,paint);
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_fly);
            canvas.drawBitmap(bitmap,100,200,paint);
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(50.0F);
            textPaint.setAntiAlias(true);
            StaticLayout layout = new StaticLayout("东风夜放花千树更吹落星如雨，宝马雕车香满路凤箫声动玉壶光转，一夜鱼龙舞", textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
            canvas.save();
            canvas.translate(100,100);//从100，100开始画
            layout.draw(canvas);
            canvas.restore();//别忘了restore

            //这里绘制要在打印的纸上的内容，如果精确一点的话，根据pageHeight，pageWidth计算就行，这里的内容和自定义View的内容一样,
            //把自定义View绘制的东西拉过来直接就可以用
        }
    }
}
