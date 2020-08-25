package net.huansi.equipment.equipmentapp.activity.check_quality;

import android.content.Intent;
import android.widget.Button;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class QualityMainActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_quality_main;
    }

    @Override
    public void init() {
    setToolBarTitle("品管信息流");
    }
    @OnClick(R.id.btnCheckPieces)
    void intentCutPieces(){
        Intent intent=new Intent();
        intent.setClass(this,CutPiecesActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnPackaging)
    void intentPackagingActivity(){
        Intent intent=new Intent(this,PackagingActivity.class);
        startActivity(intent);
    }
}
