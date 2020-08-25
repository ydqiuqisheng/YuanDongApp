package net.huansi.equipment.equipmentapp.activity.call_repair;

import android.content.Intent;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;

import butterknife.OnClick;

public class CallRepairMainActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_repair_main;
    }

    @Override
    public void init() {
        setToolBarTitle("叫修");
    }
    @OnClick(R.id.btnCallRepair)
    void toInventory(){
        startActivity(new Intent(this,CallRepairInventoryActivity.class));
    }
    @OnClick(R.id.btnRepairEvaluate)
    void toEvaluate(){
        startActivity(new Intent(this,CallRepairEvaluateActivity.class));
    }
    @OnClick(R.id.CallRepairRecords)
    void toRecords(){
        startActivity(new Intent(this,CallRepairRecordsActivity.class));
    }
}
