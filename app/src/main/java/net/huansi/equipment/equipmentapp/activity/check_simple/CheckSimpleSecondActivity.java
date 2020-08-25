package net.huansi.equipment.equipmentapp.activity.check_simple;

import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kelin.scrollablepanel.library.ScrollablePanel;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.ScrollablePanelAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.NumberInfo;
import net.huansi.equipment.equipmentapp.entity.SimpleStandardEntity;
import net.huansi.equipment.equipmentapp.entity.StandardInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//班长
public class CheckSimpleSecondActivity extends BaseActivity {
    private List<String> simpleData;
    private List<String> guige=new ArrayList<>();
    private List<String> yuncha=new ArrayList<>();
    private List<String> chima=new ArrayList<>();
    private List<String> chicun=new ArrayList<>();
    private ScrollablePanelAdapter scrollablePanelAdapter;
    private ScrollablePanel scrollablePanel;
    private ArrayAdapter inputAdapter;
    private LoadProgressDialog dialog;
    private Thread mThread;
    @BindView(R.id.lv_simple_input)
    ListView lv_simple_input;
    @BindView(R.id.designer_advice)
    TextView designerAdvice;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_simple_second;
    }

    @Override
    public void init() {
        setToolBarTitle("班长");
        dialog=new LoadProgressDialog(this);
        scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_panel);

        simpleData=new ArrayList<>();
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                initInfo();
            }
        });
        mThread.start();

    }

    private void initInfo() {
        final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
        final String sizeid = getIntent().getStringExtra("SIZEID");
        final String simpleSize = getIntent().getStringExtra("SUBMITSIZE");
        Log.e("TAG",simpleSize);
        final String simpleColor = getIntent().getStringExtra("SUBMITCOLOR");
        Log.e("TAG",simpleColor);
        final String simpleRank = getIntent().getStringExtra("SUBMITRANK");
        Log.e("TAG",simpleRank);
        final String simpleAdvice = getIntent().getStringExtra("SUBMITADVICE");
        designerAdvice.setText(simpleAdvice);
        Log.e("TAG",produceorderid+"   "+sizeid);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetSampleCheckSizeMeasureResult",
                                "PDMProduceOrderID="+produceorderid+
                                        ",ResultListType="+"DETAIL"+
                                ",Size="+simpleSize+
                                ",NodeCategory="+"Sample"+
                                ",HowManyTimes="+simpleRank+
                                ",Color="+simpleColor,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","orderggJson="+json);
                SimpleStandardEntity simpleStandardEntity = JSON.parseObject(json, SimpleStandardEntity.class);
                List<SimpleStandardEntity.DATABean> data = simpleStandardEntity.getDATA();

                for (int i=0;i<data.size();i++){
                    guige.add(data.get(i).getSPECIFICATION());
                    yuncha.add(data.get(i).getFRANCHISE());
                    chima.add(data.get(i).get进单参考尺码());
                    chicun.add(data.get(i).getGUESTSTANDARD());
                    simpleData.add(data.get(i).getMEASURERESULT());
                }
                inputAdapter=new ArrayAdapter(getApplicationContext(),R.layout.string_item_simple,R.id.text_simple,simpleData);
                lv_simple_input.setAdapter(inputAdapter);
                setListViewHeightBasedOnChildren(lv_simple_input);
                scrollablePanelAdapter = new ScrollablePanelAdapter();
                generateTestData(scrollablePanelAdapter);
                //submitMeasureData(scrollablePanelAdapter);
                scrollablePanel.setPanelAdapter(scrollablePanelAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","error99="+hsWebInfo.json);
                String json = hsWebInfo.json;
            }
        });
    }

    /*
     * 当ScrollView 与 LiseView 同时滚动计算高度的方法
     * 设置listview 的高度
     * 参数：listivew的findviewbyid
     * */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        try{
            // 获取ListView对应的Adapter
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
                // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                // 计算子项View 的宽高
                listItem.measure(0, 0);
                // 统计所有子项的总高度
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            // listView.getDividerHeight()获取子项间分隔符占用的高度
            // params.height最后得到整个ListView完整显示需要的高度
            listView.setLayoutParams(params);
        }catch (Exception e){

        }
    }

    private void generateTestData(ScrollablePanelAdapter scrollablePanelAdapter) {
        List<StandardInfo> standardInfoList = new ArrayList<>();
        for (int i = 0; i < guige.size(); i++) {
            StandardInfo standardInfo = new StandardInfo();
            standardInfo.setStandardId(i);
            standardInfo.setStandardName(guige.get(i));
            standardInfo.setStandardFloat(yuncha.get(i));
            standardInfo.setStandardBiao(chima.get(i));
            standardInfo.setStandardSize(chicun.get(i));
            standardInfoList.add(standardInfo);
        }
        scrollablePanelAdapter.setStandardInfoList(standardInfoList);


        List<NumberInfo> numberInfoList = new ArrayList<>();
        for (int k=1;k<=2;k++){
            NumberInfo numberInfo=new NumberInfo();
            numberInfo.setNumber("第"+k+"件");
            numberInfoList.add(numberInfo);
        }
        scrollablePanelAdapter.setNumberInfoList(numberInfoList);
    }


    @Override
    protected void onDestroy() {
        mThread.interrupt();
        super.onDestroy();
    }
}
