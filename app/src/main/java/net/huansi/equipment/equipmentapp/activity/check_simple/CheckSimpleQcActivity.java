package net.huansi.equipment.equipmentapp.activity.check_simple;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;

public class CheckSimpleQcActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_simple_qc;
    }

    @Override
    public void init() {
        setToolBarTitle("品管");
    }
}
