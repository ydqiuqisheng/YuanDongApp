package net.huansi.equipment.equipmentapp.activity.block_material;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.adapter.HsBaseAdapter;
import net.huansi.equipment.equipmentapp.entity.BlockBindEntity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.blockBindInfo;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.ViewHolder;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BlockQueryActivity extends BaseActivity{
    @BindView(R.id.et_query)
    EditText billInfo;
    @BindView(R.id.block_billNo)
    TextView billNo;
    @BindView(R.id.Block_queryInfo_lv)
    ListView queryInfo_lv;
    private LoadProgressDialog dialog;
    private BlockBindEntity bindInfo;
    private List<BlockBindEntity> bindQueryEntities;
    private BlockQueryAdapter blockQueryAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_block_query;
    }

    @Override
    public void init() {
        setToolBarTitle("查询");
        dialog=new LoadProgressDialog(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (billNo.getText().toString().isEmpty()){
            String s = billInfo.getText().toString().trim();
            billNo.setText(s);
            billInfo.getText().clear();
            getShelfInfo(s);
        }
        return super.onKeyUp(keyCode, event);
    }

    private void getShelfInfo(final String billNo) {
        OthersUtil.showLoadDialog(dialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this,"")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(),"sp_CPT_ReportSplitsStock",
                                "PullOrderCode="+billNo
                                ,String.class.getName(),false,"明细获取成功");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()), getApplicationContext(), dialog, new WebListener() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                String json = hsWebInfo.json;
                Log.e("TAG888",json);
                OthersUtil.dismissLoadDialog(dialog);
                blockBindInfo blockBindInfo = JSON.parseObject(json, blockBindInfo.class);
                List<blockBindInfo.DATABean> data = blockBindInfo.getDATA();
                bindQueryEntities=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    bindInfo=new BlockBindEntity();

                    bindInfo.SSUBFEPOCODE=data.get(i).getSSUBFEPOCODE();
                    bindInfo.PACKAGECOUNT=data.get(i).getPACKAGECOUNT();
                    bindInfo.SMODIFYSIZE=data.get(i).getSMODIFYSIZE();
                    bindInfo.QTY=data.get(i).getQTY();
                    bindInfo.SHELFNO=data.get(i).getFRAMECODE();
                    bindQueryEntities.add(bindInfo);
                }

                blockQueryAdapter=new BlockQueryAdapter(bindQueryEntities,getContext());
                queryInfo_lv.setAdapter(blockQueryAdapter);
            }
            @Override
            public void error(HsWebInfo hsWebInfo) {
                Log.e("TAG","errorLog2="+hsWebInfo.json);
            }
        });
    }

    private class BlockQueryAdapter extends HsBaseAdapter<BlockBindEntity>{


        public BlockQueryAdapter(List<BlockBindEntity> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)  convertView=mInflater.inflate(R.layout.activity_block_bind_item,parent,false);
            TextView fepo = ViewHolder.get(convertView, R.id.block_fepo);//PO
            TextView pacCo = ViewHolder.get(convertView, R.id.block_pacCo);//包数
            TextView size = ViewHolder.get(convertView, R.id.block_size);//尺码
            TextView qty = ViewHolder.get(convertView, R.id.block_qty);//件数
            TextView add = ViewHolder.get(convertView, R.id.block_shelfNo);//架号


            pacCo.setText(mList.get(position).PACKAGECOUNT);
            fepo.setText(mList.get(position).SSUBFEPOCODE);
            size.setText(mList.get(position).SMODIFYSIZE);
            add.setText(mList.get(position).SHELFNO);
            qty.setText(mList.get(position).QTY);
            return convertView;
        }
    }
}
