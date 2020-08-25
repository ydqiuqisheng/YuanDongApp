package net.huansi.equipment.equipmentapp.activity.make_bills;

import android.content.Intent;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;

import butterknife.OnClick;

public class MakeBillsSecondActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.make_bills_second_activity;
    }

    @Override
    public void init() {
        String billNumber = getIntent().getStringExtra("BillNumber");
        setToolBarTitle(billNumber);
    }
    @OnClick(R.id.MFCards)
    void toMF(){

        Intent intent=new Intent(this,MeetingRecordsActivity.class);
        intent.putExtra("BillNumber",getToolbarTitle().getText().toString());
        startActivity(intent);
    }
    @OnClick(R.id.WorkGuidance)
    void toWG(){
        Intent intent=new Intent(this,WorkGuidanceActivity.class);
        intent.putExtra("BillNumber",getToolbarTitle().getText().toString());
        startActivity(intent);
    }
    @OnClick(R.id.MachineLayout)
    void toML(){
        Intent intent=new Intent(this,MachineLayoutActivity.class);
        intent.putExtra("BillNumber",getToolbarTitle().getText().toString());
        startActivity(intent);
    }
    @OnClick(R.id.RiskControl)
    void toRC(){
        Intent intent=new Intent(this,RiskControlActivity.class);
        intent.putExtra("BillNumber",getToolbarTitle().getText().toString());
        startActivity(intent);
    }
}
