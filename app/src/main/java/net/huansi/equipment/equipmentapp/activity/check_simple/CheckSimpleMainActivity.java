package net.huansi.equipment.equipmentapp.activity.check_simple;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kelin.scrollablepanel.library.ScrollablePanel;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.inventory.InventoryDetailActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.adapter.ScrollablePanelAdapter;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.NumberInfo;
import net.huansi.equipment.equipmentapp.entity.OrderInfo;
import net.huansi.equipment.equipmentapp.entity.SimplePendMeasureEntity;
import net.huansi.equipment.equipmentapp.entity.SimpleStandardEntity;
import net.huansi.equipment.equipmentapp.entity.SimpleUnMeasureUtils;
import net.huansi.equipment.equipmentapp.entity.StandardInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OnDoubleClickListener;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;
//样品组
public class CheckSimpleMainActivity extends BaseActivity {
    public String area="";
    private int DelPosition;//删除后重新添加，到原来位置
    //private Boolean isChange=false;//是否录错需要修改
    private List<String> simpleData;
    private List<String> simpleDataCopy;
    private List<String> simpleColor;
    private List<String> simpleSize;
    private List<List<String>> standardData;
    private List<String> guige=new ArrayList<>();
    private List<String> yuncha=new ArrayList<>();
    private List<String> chima=new ArrayList<>();
    private List<String> chicun=new ArrayList<>();
    private ScrollablePanelAdapter scrollablePanelAdapter;
    private ScrollablePanel scrollablePanel;
    private ArrayAdapter inputAdapter;
    private StandardAdapter standardAdapter;
    private LoadProgressDialog dialog;
    private Thread mThread;
    @BindView(R.id.lv_simple_input)
    ListView lv_simple_input;
    @BindView(R.id.lv_input_standard)
    ListView lv_input_standard;
    @BindView(R.id.simple_color)
    EditText simple_color;
    @BindView(R.id.simple_size)
    TextView simple_size;
    @BindView(R.id.simpleStandard_submit)
    Button simpleStandard_submit;
    @BindView(R.id.designer_advice)
    TextView designerAdvice;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_simple_main;
    }

    @Override
    public void init() {
        setToolBarTitle("样品");
        dialog=new LoadProgressDialog(this);
        scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_panel);
        simpleData=new ArrayList<>();
        standardData=new ArrayList<>();
        simpleColor=new ArrayList<>();
        simpleSize=new ArrayList<>();
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                initInfo();
            }
        });
        mThread.start();
        standardAdapter=new StandardAdapter();

        lv_input_standard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                OthersUtil.showDoubleChooseDialog(CheckSimpleMainActivity.this, "确认删除该行吗?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("TAG","quxiao"+position);
                        return;
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DelPosition=position;
                        standardData.remove(position);
                        simpleColor.remove(position);
                        simpleSize.remove(position);
                        standardAdapter.notifyDataSetChanged();
                        Log.e("TAG","确认三"+position);
                    }
                });
                return false;
            }
        });

        lv_input_standard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
                    @Override
                    public void onDoubleClick() {
                        lv_input_standard.setAdapter(standardAdapter);
                        lv_input_standard.setItemChecked(position,true);//被选中说明对应位置的数据需要更改
                        Log.e("TAG","双击"+lv_input_standard.getCheckedItemPosition());
                        //isChange=true;
                        DelPosition=position;//更改位置
                        List<String> list = standardData.get(position);
                        for (int i=0;i<list.size();i++){
                            simpleData.set(i,list.get(i));
                        }
                        //standardData.remove(position);
                        standardAdapter.notifyDataSetChanged();
                        inputAdapter.notifyDataSetChanged();
                    }
                }));
            }
        });

    }


    @OnClick(R.id.move_down)
    void moveDown(){
        //添加到下面
        simpleDataCopy=new ArrayList<>();
        simpleColor.add(simple_color.getText().toString());
        simpleSize.add(simple_size.getText().toString());
        for (int i=0;i<simpleData.size();i++){
            simpleDataCopy.add(simpleData.get(i).toString());

        }
        //standardData页面下方数据容器
        Log.e("TAG","DelPosition="+DelPosition);
        if (lv_input_standard.getCheckedItemPosition()==DelPosition){
            standardData.add(DelPosition,simpleDataCopy);
            lv_input_standard.setItemChecked(DelPosition,false);
        }else {
            standardData.add(simpleDataCopy);
        }

        lv_input_standard.setAdapter(standardAdapter);
        standardAdapter.notifyDataSetChanged();
        lv_input_standard.setSelected(false);
        for (int i=0;i<simpleData.size();i++){
            simpleData.set(i,"");
        }
        inputAdapter.notifyDataSetChanged();
    }
    private void initInfo() {
        final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
        final String sizeid = getIntent().getStringExtra("SIZEID");
        final String sizeadvice = getIntent().getStringExtra("SIZEADVICE");
        Log.e("TAG",produceorderid+"   "+sizeid);
        simple_size.setText(sizeid);
        designerAdvice.setText("版师意见***"+sizeadvice);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_GetSampleCheckSizeInfo",
                                "PDMProduceOrderID="+produceorderid+
                                ",SizeID="+sizeid,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","orderJson="+json);
                SimpleStandardEntity simpleStandardEntity = JSON.parseObject(json, SimpleStandardEntity.class);
                List<SimpleStandardEntity.DATABean> data = simpleStandardEntity.getDATA();
                for (int i=0;i<data.size();i++){
                    guige.add(data.get(i).getSPECIFICATION());
                    yuncha.add(data.get(i).getFRANCHISE());
                    chima.add(data.get(i).get进单参考尺码());
                    chicun.add(data.get(i).getGUESTSTANDARD());
                    simpleData.add("");
                }
                inputAdapter=new ArrayAdapter(getApplicationContext(),R.layout.string_item_simple,R.id.text_simple,simpleData);
                lv_simple_input.setAdapter(inputAdapter);
                OthersUtil.setListViewHeightBasedOnChildren(lv_simple_input);

                scrollablePanelAdapter = new ScrollablePanelAdapter();
                generateTestData(scrollablePanelAdapter);
                //submitMeasureData(scrollablePanelAdapter);
                scrollablePanel.setPanelAdapter(scrollablePanelAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                OthersUtil.ToastMsg(getApplicationContext(),"获取款式表error");
                Log.e("TAG","error="+hsWebInfo.json);
            }
        });
    }


    @OnItemClick(R.id.lv_simple_input)
        void setLv_simple_input(final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View areaDialogView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.area_input_dialog,null);
        final EditText editText= (EditText) areaDialogView.findViewById(R.id.etInventoryAreaDialog);
        editText.setHint("请输入具体数值");
        editText.setTextColor(Color.BLACK);
        builder.setView(areaDialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String area=editText.getText().toString().trim();
                        if(area.isEmpty()){
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                            OthersUtil.ToastMsg(getApplicationContext(),"请输入具体数值");
                            return;
                        }
                        addSimpleData(area,position);
                        OthersUtil.dialogDismiss(dialogInterface);
                        dialogInterface.dismiss();
                        CheckSimpleMainActivity.this.area=area;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        OthersUtil.dialogDismiss(dialogInterface);
                        dialogInterface.dismiss();

                    }
                })
                .setCancelable(false)
                .show();
    }



    private void addSimpleData(String area,int position) {

        simpleData.set(position,area);
        Log.e("TAG","simpleData="+simpleData);
        inputAdapter.notifyDataSetChanged();
    }

    private void generateTestData(ScrollablePanelAdapter scrollablePanelAdapter) {
        List<StandardInfo> standardInfoList = new ArrayList<>();
        for (int i = 0; i < guige.size(); i++) {
            StandardInfo standardInfo = new StandardInfo();
            standardInfo.setStandardId(i);
            //roomInfo.setRoomType("SUPK");
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


    private class StandardAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return standardData.size();
        }

        @Override
        public Object getItem(int position) {
            return standardData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            convertView=View.inflate(CheckSimpleMainActivity.this,R.layout.listview_simple_standard,null);
            TextView tvSimpleCount= (TextView) convertView.findViewById(R.id.tvSimple_count);
            TextView tvSimpleColor= (TextView) convertView.findViewById(R.id.tvSimple_color);
            TextView tvSimpleSize= (TextView) convertView.findViewById(R.id.tvSimple_size);
            TextView tvSimpleStandard= (TextView) convertView.findViewById(R.id.tvSimple_standard);
            tvSimpleCount.setText("第"+Integer.toString(position+1)+"件");
            tvSimpleColor.setText(simpleColor.get(position).toString());
            tvSimpleSize.setText(simpleSize.get(position).toString());
            tvSimpleStandard.setText(standardData.get(position).toString());
            return convertView;
        }
    }


    @OnClick(R.id.simpleStandard_submit)
    void simpleStandard_submit(){
        if (standardData.size()==0){
            OthersUtil.ToastMsg(this,"没有可提交数据！");
        }else {
            final String produceorderid = getIntent().getStringExtra("PRODUCEORDERID");
            final String UserID = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString().toUpperCase();
            Log.e("TAG",produceorderid);
            OthersUtil.showDoubleChooseDialog(CheckSimpleMainActivity.this, "提交后只能电脑修改,请仔细核对填写无误", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface mdialog, int which) {

                    for (int i=0;i<standardData.size();i++){
                        final int finalI = i;
                        String split="";
                        for (int n=0;n<guige.size();n++){
                            split=split+guige.get(n).toString()+":"+standardData.get(i).get(n).toString()+"!";
                        }
                        Log.e("TAG","PDMProduceOrderID="+produceorderid);
                        Log.e("TAG","Color="+simple_color.getText().toString());

                        Log.e("TAG","SIZE="+simple_size.getText().toString());

                        Log.e("TAG","Range="+Integer.toString(finalI+1));
                        Log.e("TAG","split="+split);
                        final String finalSplit = split.replace(",",";").trim();
                        Log.e("TAG","finalSplit="+finalSplit);
                        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(CheckSimpleMainActivity.this,"")
                                .map(new Func1<String, HsWebInfo>() {
                                    @Override
                                    public HsWebInfo call(String s) {
                                        return NewRxjavaWebUtils.getJsonDataExt(getApplicationContext(),"SqlConnStrAGP","spAPP_SubmitSampleCheckSizeInfo",
                                                "PDMProduceOrderID="+produceorderid+
                                                        ",Color="+simpleColor.get(finalI)+
                                                        ",SIZE="+simple_size.getText().toString()+
                                                        ",Range="+Integer.toString(finalI+1)+
                                                        ",OPType="+"Sample"+
                                                        ",Content="+ finalSplit+
                                                        ",Advice="+""+
                                                        ",UserID="+UserID
                                                ,String.class.getName(),false,"成功");
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
                            @Override
                            public void success(HsWebInfo hsWebInfo) {
                                String json = hsWebInfo.json;
                                OthersUtil.ToastMsg(CheckSimpleMainActivity.this,"提交成功");
                                startActivity(new Intent(CheckSimpleMainActivity.this,CheckSimpleSizesActivity.class));
                                Log.e("TAG","submitJson="+json);
                            }
                            @Override
                            public void error(HsWebInfo hsWebInfo) {
                                Log.e("TAG","errorSubmit="+hsWebInfo.json);
                                OthersUtil.ToastMsg(getApplicationContext(),"上传出错log:"+hsWebInfo.json);
                            }
                        });
                    }

                }
            });
        }

    }
    @Override
    protected void onDestroy() {
        mThread.interrupt();
        super.onDestroy();
    }
}
