package net.huansi.equipment.equipmentapp.activity.block_material;

import android.content.Intent;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;

import butterknife.OnClick;

public class BlockMaInActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_block_in;
    }

    @Override
    public void init() {
        setToolBarTitle("拼块");
    }

    @OnClick(R.id.block_binding)
    void bind(){
        startActivity(new Intent(this,BlockBindActivity.class));
    }
    @OnClick(R.id.block_query)
    void query(){
        startActivity(new Intent(this,BlockQueryActivity.class));
    }
    @OnClick(R.id.block_unbind)
    void unBind(){
        startActivity(new Intent(this,BlockUnbindActivity.class));
    }
}
