package net.huansi.equipment.equipmentapp.activity.logging_bill;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.call_repair.CallRepairEvaluateActivity;
import net.huansi.equipment.equipmentapp.activity.call_repair.CallRepairInventoryActivity;
import net.huansi.equipment.equipmentapp.activity.check_simple.CheckSimpleMainActivity;
import net.huansi.equipment.equipmentapp.activity.cut_parts.CutPartsTakeActivity;
import net.huansi.equipment.equipmentapp.activity.merge_goods.ScannerActivity;
import net.huansi.equipment.equipmentapp.activity.user_manage.RoleActivity;
import net.huansi.equipment.equipmentapp.activity.user_manage.UserActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.LogBillGroup;
import net.huansi.equipment.equipmentapp.entity.LogDtEntity;
import net.huansi.equipment.equipmentapp.entity.LogDtInfo;
import net.huansi.equipment.equipmentapp.entity.LogSumInfo;
import net.huansi.equipment.equipmentapp.entity.ScannerEntity;
import net.huansi.equipment.equipmentapp.entity.ScannerInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.JSONEntity;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;
import net.huansi.equipment.equipmentapp.widget.CheckableLinearLayout;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoggingBillActivity extends BaseActivity{
    @BindView(R.id.log_bill_detail)
    ListView detailListView;
    @BindView(R.id.log_client_po)
    ListView cpListView;//客户po
//    @BindView(R.id.log_bill_sum)
//    ListView sumListView;
//    @BindView(R.id.log_allQuantity)
//    TextView all_Quantity;
    @BindView(R.id.log_needAddNumber)
    TextView needAddNumber;
    @BindView(R.id.log_GroupNo)
    TextView groupNo;
    @BindView(R.id.log_FrameNo)
    TextView frameNo;
    @BindView(R.id.log_EndTest)
    TextView notPass;
    @BindView(R.id.log_fepo)
    TextView logFepo;
    @BindView(R.id.log_comb)
    TextView logComb;
    @BindView(R.id.log_size)
    TextView logSize;
    @BindView(R.id.log_selectedNumber)
    TextView selectedNum;
    @BindView(R.id.log_needCount)
    EditText needCount;
    @BindView(R.id.log_Day)
    EditText logDay;
    private List<LogDtInfo> dtList;
    private CheckableLinearLayout linearLayout;
    private List<String> passStateList;
    private List<String> cpList;//客户po集合
    private List<String> poList;//fepo去重集合
    private List<String> combList;//comb去重集合
    private List<String> sizeList;//尺码去重集合
    private List<String> groupList;//可选组别集合
    private LoadProgressDialog dialog;
    private LogDtInfo dtInfo;
    //private LogSumInfo sumInfo;
    private ArrayAdapter cpAdapter;
    private DetailAdapter detailAdapter;
    //private SumAdapter sumAdapter;
    private List<Boolean> selectList = new ArrayList(); // 判断listview单选位置
    private Map<String,List> logMap=new HashMap();
    private int REQUEST_CODE=1;
    private List<String> selectedSizes=new ArrayList<>();//选中条目中有多少个不同尺码
    private List<String> selectSizeNum;//每个尺码的总码长
    //private Map<Integer,Integer> positionMap=new HashMap();//将明细list发生改变的位置和汇总list新增的位置关联，以便删除
    private int count=0;
    private boolean isoncl=true;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_logging_bill;
    }

    @Override
    public void init() {
        setToolBarTitle("App登记对点单");
        TextView subTitle = getSubTitle();
        subTitle.setText("查询");
        logDay.setText("3");
        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoggingBillActivity.this,LoggingBillSearchActivity.class);
                startActivity(intent);
            }
        });
        ZXingLibrary.initDisplayOpinion(this);
        SharedPreferences message = getSharedPreferences("LogGroup", MODE_PRIVATE);
        String group = message.getString("Group", null);
        Log.e("TAG","gruop="+group);
        groupNo.setText(group);
        cpList=new ArrayList<>();
        cpAdapter=new ArrayAdapter(this,R.layout.string_item_simple_log,R.id.text_simple_log,cpList);
        dialog=new LoadProgressDialog(this);
        detailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //获取条目
                linearLayout = (CheckableLinearLayout) view.findViewById(R.id.ll_contain);
                if (linearLayout.isChecked()) {
                    Log.e("TAG", position + "选中");
                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                    imageView.setImageResource(R.drawable.icon_gou);
                    //detailListView.getChildAt(position).setBackgroundColor(Color.GREEN);
                    TextView tv1 = (TextView) linearLayout.getChildAt(1);
                    tv1.setTextColor(Color.BLUE);
                    TextView tv2 = (TextView) linearLayout.getChildAt(2);
                    tv2.setTextColor(Color.BLUE);
                    TextView tv3 = (TextView) linearLayout.getChildAt(3);
                    tv3.setTextColor(Color.BLUE);
                    TextView tv4 = (TextView) linearLayout.getChildAt(4);
                    tv4.setTextColor(Color.BLUE);
                    TextView tv5 = (TextView) linearLayout.getChildAt(5);
                    tv5.setTextColor(Color.BLUE);
                    if (!selectedSizes.contains(dtList.get(position).SIZENAME)){
                        selectedSizes.add(dtList.get(position).SIZENAME);
                    }
                    selectList.set(position,true);
                    detailAdapter.notifyDataSetChanged();
                    figureNum();
                    //stateImage.setImageResource(R.drawable.icon_plus);
                } else {
                    Log.e("TAG", position + "取消选中");
                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                    imageView.setImageResource(R.drawable.icon_minus);
                    //detailListView.getChildAt(position).setBackgroundColor(Color.GREEN);
                    TextView tv1 = (TextView) linearLayout.getChildAt(1);
                    tv1.setTextColor(Color.RED);
                    TextView tv2 = (TextView) linearLayout.getChildAt(2);
                    tv2.setTextColor(Color.RED);
                    TextView tv3 = (TextView) linearLayout.getChildAt(3);
                    tv3.setTextColor(Color.RED);
                    TextView tv4 = (TextView) linearLayout.getChildAt(4);
                    tv4.setTextColor(Color.RED);

                    TextView tv5 = (TextView) linearLayout.getChildAt(5);
                    tv5.setTextColor(Color.RED);
                    selectList.set(position,false);
                    detailAdapter.notifyDataSetChanged();
                    if (!selectedSizes.contains(dtList.get(position).SIZENAME)){
                        selectedSizes.add(dtList.get(position).SIZENAME);
                    }
                    cpList.set(position,"选择");
                    cpAdapter.notifyDataSetChanged();
                    figureNum();

                    //stateImage.setImageResource(R.drawable.icon_minus);
                }
            }
        });

        cpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (selectList.get(position)==true){

                    ShowChoise(position);
                }else {

                    Toast.makeText(getApplicationContext(), "先选中该条目", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cpListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (cpList.get(position)=="选择"||selectList.get(position)==false){
                    OthersUtil.showTipsDialog(LoggingBillActivity.this,"请选中条目并选好客户po");
                }else {
                    copyFirstPo(position);
                }
                return true;
            }
        });

        initGroup();
    }

    private void initGroup() {
        groupList=new ArrayList<>();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(LoggingBillActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"GetAllSewingPostForSTP",
                                "",String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","groupJson111="+json);
                LogBillGroup logBillGroup = JSON.parseObject(json, LogBillGroup.class);
                List<LogBillGroup.DATABean> data = logBillGroup.getDATA();
                for (int i=0;i<data.size();i++){
                    groupList.add(data.get(i).getPOSTNAME());
                }

            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
            }
        });
    }


    @OnClick(R.id.log_screenOut)
    void screenOut(){

        if (logFepo.getText().toString().isEmpty()&&!logComb.getText().toString().isEmpty()&&!logSize.getText().toString().isEmpty()){
            int count=0;
            int index=0;
            for (int i=0;i<dtList.size();i++){
                if (dtList.get(i).COMBNAME.equalsIgnoreCase(logComb.getText().toString().trim())&&dtList.get(i).SIZENAME.equalsIgnoreCase(logSize.getText().toString().trim().toUpperCase())){
                    if (!needCount.getText().toString().isEmpty()){
                        count=count+Integer.parseInt(dtList.get(i).QUANTITY);

                        if (count<=Integer.parseInt(needCount.getText().toString())){
                            index++;
                            selectList.set(i,true);
                        }else {//默认至少选中一条
                            if (index==0){
                                index=1;
                                selectList.set(i,true);
                            }else {
                                selectList.set(i,false);
                            }
                        }
                    }else {
                        selectList.set(i,true);
                    }
                if (!selectedSizes.contains(dtList.get(i).SIZENAME)){
                    selectedSizes.add(dtList.get(i).SIZENAME);
                }

                        detailAdapter.notifyDataSetChanged();
                        detailListView.setSelection(i);
                }
            }
            figureNum();
            return;
        }
        if (!logFepo.getText().toString().isEmpty()&&logComb.getText().toString().isEmpty()&&!logSize.getText().toString().isEmpty()){
            int count=0;
            int index=0;
            for (int i=0;i<dtList.size();i++){
                if (dtList.get(i).FEPOCODE.equalsIgnoreCase(logFepo.getText().toString())&&dtList.get(i).SIZENAME.equalsIgnoreCase(logSize.getText().toString())){
                    if (!needCount.getText().toString().isEmpty()){
                        count=count+Integer.parseInt(dtList.get(i).QUANTITY);
                        if (count<=Integer.parseInt(needCount.getText().toString())){
                            index++;
                            selectList.set(i,true);
                        }else {
                            if (index==0){
                                index=1;
                                selectList.set(i,true);
                            }else {
                                selectList.set(i,false);
                            }
                        }
                    }else {
                        selectList.set(i,true);
                    }
                    if (!selectedSizes.contains(dtList.get(i).SIZENAME)){
                        selectedSizes.add(dtList.get(i).SIZENAME);
                    }
                    detailAdapter.notifyDataSetChanged();
                    detailListView.setSelection(i);
                }
            }
            figureNum();
            return;
        }
        if (!logFepo.getText().toString().isEmpty()&&!logComb.getText().toString().isEmpty()&&logSize.getText().toString().isEmpty()){
            int count=0;
            int index=0;
            for (int i=0;i<dtList.size();i++){
                if (dtList.get(i).FEPOCODE.equalsIgnoreCase(logFepo.getText().toString())&&dtList.get(i).COMBNAME.equalsIgnoreCase(logComb.getText().toString())){
                    if (!needCount.getText().toString().isEmpty()){
                        count=count+Integer.parseInt(dtList.get(i).QUANTITY);
                        if (count<=Integer.parseInt(needCount.getText().toString())){
                            index++;
                            selectList.set(i,true);
                        }else {
                            if (index==0){
                                index=1;
                                selectList.set(i,true);
                            }else {
                                selectList.set(i,false);
                            }
                        }
                    }else {
                        selectList.set(i,true);
                    }
                    if (!selectedSizes.contains(dtList.get(i).SIZENAME)){
                        selectedSizes.add(dtList.get(i).SIZENAME);
                    }
                    detailAdapter.notifyDataSetChanged();
                    detailListView.setSelection(i);
                }
            }
            figureNum();
            return;
        }
        if (logFepo.getText().toString().isEmpty()&&logComb.getText().toString().isEmpty()&&!logSize.getText().toString().isEmpty()){
            int count=0;
            int index=0;
            for (int i=0;i<dtList.size();i++){
                if (dtList.get(i).SIZENAME.equalsIgnoreCase(logSize.getText().toString())){
                    if (!needCount.getText().toString().isEmpty()){
                        count=count+Integer.parseInt(dtList.get(i).QUANTITY);
                        if (count<=Integer.parseInt(needCount.getText().toString())){
                            index++;
                            selectList.set(i,true);
                        }else {
                            if (index==0){
                                index=1;
                                selectList.set(i,true);
                            }else {
                                selectList.set(i,false);
                            }
                        }
                    }else {
                        selectList.set(i,true);
                    }

                    if (!selectedSizes.contains(dtList.get(i).SIZENAME)){
                        selectedSizes.add(dtList.get(i).SIZENAME);
                    }
                    detailAdapter.notifyDataSetChanged();
                    detailListView.setSelection(i);
                }
            }
            figureNum();
            return;
        }
        if (logFepo.getText().toString().isEmpty()&&!logComb.getText().toString().isEmpty()&&logSize.getText().toString().isEmpty()){
            int count=0;
            int index=0;
            for (int i=0;i<dtList.size();i++){
                if (dtList.get(i).COMBNAME.equalsIgnoreCase(logComb.getText().toString())){
                    if (!needCount.getText().toString().isEmpty()){
                        count=count+Integer.parseInt(dtList.get(i).QUANTITY);
                        if (count<=Integer.parseInt(needCount.getText().toString())){
                            index++;
                            selectList.set(i,true);
                        }else {
                            if (index==0){
                                index=1;
                                selectList.set(i,true);
                            }else {
                                selectList.set(i,false);
                            }
                        }
                    }else {
                        selectList.set(i,true);
                    }
                    if (!selectedSizes.contains(dtList.get(i).SIZENAME)){
                        selectedSizes.add(dtList.get(i).SIZENAME);
                    }
                    detailAdapter.notifyDataSetChanged();
                    detailListView.setSelection(i);
                }
            }
            figureNum();
            return;
        }
        if (!logFepo.getText().toString().isEmpty()&&logComb.getText().toString().isEmpty()&&logSize.getText().toString().isEmpty()){
            int count=0;
            int index=0;
            for (int i=0;i<dtList.size();i++){
                if (dtList.get(i).FEPOCODE.equalsIgnoreCase(logFepo.getText().toString())){
                    if (!needCount.getText().toString().isEmpty()){
                        count=count+Integer.parseInt(dtList.get(i).QUANTITY);
                        if (count<=Integer.parseInt(needCount.getText().toString())){
                            selectList.set(i,true);
                        }else {
                            if (index==0){
                                index=1;
                                selectList.set(i,true);
                            }else {
                                selectList.set(i,false);
                            }
                        }
                    }else {
                        selectList.set(i,true);
                    }
                    if (!selectedSizes.contains(dtList.get(i).SIZENAME)){
                        selectedSizes.add(dtList.get(i).SIZENAME);
                    }
                    detailAdapter.notifyDataSetChanged();
                    detailListView.setSelection(i);
                }
            }
            figureNum();
            return;
        }
        if (!logFepo.getText().toString().isEmpty()&&!logComb.getText().toString().isEmpty()&&!logSize.getText().toString().isEmpty()){
            int count=0;
            int index=0;
            for (int i=0;i<dtList.size();i++){
                if (dtList.get(i).FEPOCODE.equalsIgnoreCase(logFepo.getText().toString())&&dtList.get(i).COMBNAME.equalsIgnoreCase(logComb.getText().toString())&&dtList.get(i).SIZENAME.equalsIgnoreCase(logSize.getText().toString())){
                    //detailListView.smoothScrollToPosition(i);

                    if (!needCount.getText().toString().isEmpty()){
                        count=count+Integer.parseInt(dtList.get(i).QUANTITY);
                        if (count<=Integer.parseInt(needCount.getText().toString())){
                            index++;
                            selectList.set(i,true);
                        }else {
                            if (index==0){
                                index=1;
                                selectList.set(i,true);
                            }else {
                                selectList.set(i,false);
                            }
                        }
                    }else {
                        selectList.set(i,true);
                    }
                    if (!selectedSizes.contains(dtList.get(i).SIZENAME)){
                        selectedSizes.add(dtList.get(i).SIZENAME);
                    }
                    detailAdapter.notifyDataSetChanged();
                    detailListView.setSelection(i);
                }
            }
            figureNum();
            return;
        }

    }

    private void figureNum(){
        selectSizeNum=new ArrayList<>();
        String result="";
        String count = needCount.getText().toString();
        for (int j=0;j<selectedSizes.size();j++){
            int sum=0;
            for (int i=0;i<dtList.size();i++){
                if (selectList.get(i)==true&&dtList.get(i).SIZENAME.equalsIgnoreCase(selectedSizes.get(j))){
                    sum=sum+Integer.parseInt(dtList.get(i).QUANTITY);
                }
            }
            selectSizeNum.add(sum+"");
            if (!needCount.getText().toString().isEmpty()) {
                int i = Integer.parseInt(count) - sum;
                needAddNumber.setText("差"+i+"件");
            }

            result=result+selectedSizes.get(j)+sum+"件;";
        }
        selectedNum.setText(result);
        Log.e("TAG","选中尺码集合"+selectedSizes.toString());
        Log.e("TAG","每个尺码的码长"+selectSizeNum.toString());
//        for (int i=0;i<dtList.size();i++){
//            if (selectList.get(i)==true){
//                sum=sum+Integer.parseInt(dtList.get(i).QUANTITY);
//            }
//        }
//        sumNum.setText(sum+"");
    }


    private void copyFirstPo( int position){
        final String firstCp = cpList.get(position);
            OthersUtil.showDoubleChooseDialog(LoggingBillActivity.this, "将用此客户po填充所有选中条目", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int t) {

                    for (int i=0;i<cpList.size();i++){
                        if (selectList.get(i)==true){
                            cpList.set(i,firstCp);
                        }
                    }
                    cpAdapter.notifyDataSetChanged();
                }
            });


    }




    private void ShowChoise(final int position) {
        //List list = logMap.get(dtList.get(position).FEPOCODE);//单个FEPO对应的客户PO集合
        String customerpo = dtList.get(position).CUSTOMERPO;
        Log.e("TAG","DtPo="+customerpo);
        //final String[] split = (String[]) list.toArray(new String[list.size()]);
        final String[] split = customerpo.split(",");
        Log.e("TAG","split="+split);
        AlertDialog alertDialog = new AlertDialog.Builder(LoggingBillActivity.this)
                .setTitle("选择一个客户PO")
                .setIcon(R.drawable.app_icon)
                .setItems(split,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {

                        Log.e("TAG","截取的客户po="+ Arrays.asList(split[which].split("@")));
                        String po = Arrays.asList(split[which].split("@")).get(0);
                        Toast.makeText(getApplicationContext(), "选择的po为：" + po, Toast.LENGTH_SHORT).show();
                        cpList.set(position,po);
                        cpAdapter.notifyDataSetChanged();
                    }
                }).create();
        alertDialog.show();
    }

    @OnClick(R.id.log_fepo)
    void showFepo(){
        if (poList==null){
            return;
        }
        int size = poList.size();

        final String[] array = poList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(LoggingBillActivity.this)
                .setTitle("选择一个PO")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        logFepo.setText(array[which]);
                    }
                }).create();
        alertDialog.show();
    }
    @OnClick(R.id.log_comb)
    void showComb(){
        if (combList==null){
            return;
        }
        int size = combList.size();

        final String[] array = combList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(LoggingBillActivity.this)
                .setTitle("选择一个Comb")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        logComb.setText(array[which]);
                    }
                }).create();
        alertDialog.show();
    }
    @OnClick(R.id.log_size)
    void showSize(){
        if (sizeList==null){
            return;
        }
        int size = sizeList.size();

        final String[] array = sizeList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(LoggingBillActivity.this)
                .setTitle("选择一个尺码")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        logSize.setText(array[which]);
                    }
                }).create();
        alertDialog.show();
    }

    @OnClick(R.id.log_GroupNo)
    void showGroup(){

            if (groupList==null||groupList.isEmpty()){
                return;
            }
        int size = groupList.size();

        final String[] array = groupList.toArray(new String[size]);
        AlertDialog alertDialog = new AlertDialog.Builder(LoggingBillActivity.this)
                .setTitle("选择一个班组")
                .setIcon(R.drawable.app_icon)
                .setItems(array,  new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int which) {
                        groupNo.setText(array[which]);
                    }
                }).create();
        alertDialog.show();
    }




    private void initData() {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(LoggingBillActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"spGetDataSewFastDeliverNew",
                                "PostName="+groupNo.getText().toString()+
                                        ",day="+Integer.parseInt(logDay.getText().toString())
                                ,String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","logJson="+json);
                OthersUtil.dismissLoadDialog(dialog);
                LogDtEntity logDtEntity = JSON.parseObject(json, LogDtEntity.class);
                List<LogDtEntity.DATABean> data = logDtEntity.getDATA();
                dtList=new ArrayList<>();
                passStateList=new ArrayList<>();
                poList=new ArrayList<>();
                combList=new ArrayList<>();
                sizeList=new ArrayList<>();
                //customerList=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    dtInfo=new LogDtInfo();
                    dtInfo.PACKAGENO=data.get(i).getPACKAGENO();
                    dtInfo.FEPOCODE=data.get(i).getFEPOCODE();
                    dtInfo.COMBNAME=data.get(i).getCOMBNAME();
                    dtInfo.SIZENAME=data.get(i).getSIZENAME();
                    dtInfo.QUANTITY=data.get(i).getQUANTITY();
                    dtInfo.RFIDID=data.get(i).getRFIDINID();
                    dtInfo.FINALCHECK=data.get(i).getFINALCHECK();
                    dtInfo.CUSTOMERPO=data.get(i).getCUSTOMERPO();
                    if (!poList.contains(dtInfo.FEPOCODE)){
                        poList.add(dtInfo.FEPOCODE);
                    }
                    if (!combList.contains(dtInfo.COMBNAME)){
                        combList.add(dtInfo.COMBNAME);
                    }
                    if (!sizeList.contains(dtInfo.SIZENAME)){
                        sizeList.add(dtInfo.SIZENAME);
                    }
//                    if (!customerList.contains(dtInfo.CUSTOMERPO)){
//                        customerList.add(dtInfo.CUSTOMERPO);
//                    }
//
//                    logMap.put(data.get(i).getFEPOCODE().toString().trim(),customerList);
                    dtList.add(dtInfo);
                }
                OthersUtil.setListViewOnTouchAndScrollListener(detailListView,cpListView);
                initCheck(false);
                detailAdapter=new DetailAdapter();
                detailListView.setAdapter(detailAdapter);
                //填充客户po列表

                for (int i=0;i<dtList.size();i++){
                    cpList.add("选择");
                }
                cpListView.setAdapter(cpAdapter );
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
            }
        });
    }

    @Override
    protected void onDestroy() {
        SharedPreferences message = getSharedPreferences("LogGroup", MODE_PRIVATE);
        SharedPreferences.Editor editor = message.edit();
        editor.putString("Group",groupNo.getText().toString());
        editor.commit();
        super.onDestroy();
    }

    private void initCheck(Boolean flag) {
        for (int i=0;i<dtList.size();i++){
            selectList.add(i,flag);
        }
    }

    private class DetailAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dtList.size();
        }

        @Override
        public Object getItem(int position) {
            return dtList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            convertView=LayoutInflater.from(LoggingBillActivity.this).inflate(R.layout.list_logging_item,null,false);
//            if (convertView==null){
//                convertView=getLayoutInflater().inflate(R.layout.list_logging_item,viewGroup,false);
//            }
            LogDtInfo logDtInfo = dtList.get(position);
            ImageView state = (ImageView) convertView.findViewById(R.id.logging_state);
            TextView packageNo = (TextView) convertView.findViewById(R.id.log_PackageNo);
                    packageNo.setText(logDtInfo.PACKAGENO);
            TextView dtFepo = (TextView) convertView.findViewById(R.id.log_DTFepo);
                    dtFepo.setText(logDtInfo.FEPOCODE);
            TextView dtComb = (TextView) convertView.findViewById(R.id.log_DTComb);
                    dtComb.setText(logDtInfo.COMBNAME);
            TextView dtSize = (TextView) convertView.findViewById(R.id.log_DTSize);
                    dtSize.setText(logDtInfo.SIZENAME);
            TextView dtQuantity = (TextView) convertView.findViewById(R.id.log_DTQuantity);
                    dtQuantity.setText(logDtInfo.QUANTITY);
                    dtQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("TAG","触摸位置"+position);
                            android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(LoggingBillActivity.this);
                            View areaDialogView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.area_input_dialog,null);
                            final EditText editText= (EditText) areaDialogView.findViewById(R.id.etInventoryAreaDialog);
                            editText.setHint("请输入想要修改的具体数值");
                            editText.setTextColor(Color.BLACK);
                            builder.setView(areaDialogView)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            String quantity=editText.getText().toString().trim();
                                            if(quantity.isEmpty()){
                                                OthersUtil.dialogNotDismissClickOut(dialogInterface);
                                                OthersUtil.ToastMsg(getApplicationContext(),"请输入具体数值");
                                                return;
                                            }
                                            //addSimpleData(area,position);
                                            OthersUtil.dialogDismiss(dialogInterface);
                                            dialogInterface.dismiss();
                                            LogDtInfo logDtInfo = dtList.get(position);
                                            logDtInfo.QUANTITY=quantity;
                                            dtList.set(position,logDtInfo);
                                            detailAdapter.notifyDataSetChanged();
                                            figureNum();
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
                    });
            if (selectList.get(position)==true){

                state.setImageResource(R.drawable.icon_gou);
                packageNo.setTextColor(Color.BLUE);
                dtFepo.setTextColor(Color.BLUE);
                dtComb.setTextColor(Color.BLUE);
                dtSize.setTextColor(Color.BLUE);
                dtQuantity.setTextColor(Color.BLUE);
            }else {
                state.setImageResource(R.drawable.icon_minus);
                packageNo.setTextColor(Color.RED);
                dtFepo.setTextColor(Color.RED);
                dtComb.setTextColor(Color.RED);
                dtSize.setTextColor(Color.RED);
                dtQuantity.setTextColor(Color.RED);
            }
            return convertView;
        }
    }



    @OnClick(R.id.logInfoUpdate)
    void logUp(){
        for (int i=0;i<cpList.size();i++){
            if (selectList.get(i)==true&&cpList.get(i).toString()=="选择"){
                OthersUtil.showTipsDialog(LoggingBillActivity.this,"无法提交,有的客户po未选好请检查");
                return;
            }
        }
        if (isoncl==true) {
            isoncl=false;
            OthersUtil.showDoubleChooseDialog(LoggingBillActivity.this, "确认提交?请保证信息无误不要多次点击提交,等待返回主页面", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int p) {
                    final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()).toString();
                    OthersUtil.showLoadDialog(dialog);
                    final String uuid = UUID.randomUUID().toString();
                    final String frameCode = frameNo.getText().toString().trim();
                    final String postName = groupNo.getText().toString().trim().toUpperCase();
                    int size = dtList.size();
                    count = 0;
                    Log.e("TAG", "提交数量:" + size);
                    OthersUtil.showLoadDialog(dialog);
                    for (int t = 0; t < size; t++) {

                        if (selectList.get(t) == true) {
                            final LogDtInfo dtInfo = dtList.get(t);
                            final String cp = cpList.get(t).trim();
                            NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(LoggingBillActivity.this, "")
                                            .map(new Func1<String, HsWebInfo>() {
                                                @Override
                                                public HsWebInfo call(String s) {

                                                    return NewRxjavaWebUtils.getJsonData(getApplicationContext(), "InsertDataToSewFastDeliver",
                                                            "ItemID =" + uuid +
                                                                    ",FrameCode=" + frameCode +
                                                                    ",FrameState=" + "-1" +
                                                                    ",PostName=" + postName +
                                                                    ",FinalCheck=" + dtInfo.FINALCHECK +
                                                                    ",PackageNo=" + dtInfo.PACKAGENO +
                                                                    ",FEPOCode=" + dtInfo.FEPOCODE +
                                                                    ",CustomerPO=" + cp +
                                                                    ",CombName=" + dtInfo.COMBNAME +
                                                                    ",SizeName=" + dtInfo.SIZENAME +
                                                                    ",Quantity=" + Integer.parseInt(dtInfo.QUANTITY) +
                                                                    ",RFIDInID=" + Integer.parseInt(dtInfo.RFIDID) +
                                                                    ",CreateDate=" + date
                                                            , String.class.getName(), false, "对点单提交成功");
                                                }
                                            })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                    //.retryWhen(new RxRetryConnect(2))
                                    , getApplicationContext(), dialog, new WebListener() {
                                        @Override
                                        public void success(HsWebInfo hsWebInfo) {
                                            String json = hsWebInfo.json;
                                            Log.e("TAG", "logUp=" + json);
                                            OthersUtil.dismissLoadDialog(dialog);
                                            //startActivity(new Intent(LoggingBillActivity.this,));
                                        }

                                        @Override
                                        public void error(HsWebInfo hsWebInfo) {
                                            Log.e("TAG", "errorLog=" + hsWebInfo.json);
                                        }
                                    });
                        }

                    }
                    OthersUtil.dismissLoadDialog(dialog);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changeState(uuid);
                        }
                    }, 2000);
                }

            });

        }
    }

    private void changeState(final String ID) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(LoggingBillActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"CommitDataToSTP_SewFastDeliver",
                                "ItemID ="+ID
                                ,String.class.getName(),false,"对点单提交成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                isoncl=true;
                OthersUtil.dismissLoadDialog(dialog);
                OthersUtil.ToastMsg(getApplicationContext(),"提交成功");
                finish();
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog="+hsWebInfo.json);
                OthersUtil.dismissLoadDialog(dialog);
                OthersUtil.ToastMsg(getApplicationContext(),"提交成功");
                finish();
            }
        });
    }


    @OnClick(R.id.log_FrameNo)
    void openCamera(){
        Intent intent = new Intent(LoggingBillActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    //接收扫描二维码数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING).trim();
                    Log.e("TAG","扫描结果="+result);
                    frameNo.setText(result);
                    isSubmit(result);//是否该对点单已经提交过

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(LoggingBillActivity.this, "解析二维码失败请重新扫描", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void isSubmit(final String code) {
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(LoggingBillActivity.this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"VerifyFrameStateByFrameCode",
                                "FrameCode="+code,
                                String.class.getName(),false,"组别获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG","upJson="+json);
                OthersUtil.showTipsDialog(LoggingBillActivity.this,json.toString());
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog1="+hsWebInfo.json);
                initData();
            }
        });
    }
}
