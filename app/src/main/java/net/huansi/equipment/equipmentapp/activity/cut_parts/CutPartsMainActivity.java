package net.huansi.equipment.equipmentapp.activity.cut_parts;

import android.content.Intent;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;

import butterknife.OnClick;

public class CutPartsMainActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cut_parts_main;
    }

    @Override
    public void init() {
        setToolBarTitle("功能");
    }
    @OnClick(R.id.partsLoad)
    void carLoading(){
        startActivity(new Intent(this,CutPartsLoadActivity.class));
    }
    @OnClick(R.id.partsPark)
    void carParking(){
        startActivity(new Intent(this,CutPartsParkActivity.class));
    }
    @OnClick(R.id.partsTake)
    void carTaking(){
        startActivity(new Intent(this,CutPartsTakeActivity.class));
    }
    @OnClick(R.id.partsQuery)
    void carQuerying(){
        startActivity(new Intent(this,CutPartsQueryActivity.class));
    }
}
