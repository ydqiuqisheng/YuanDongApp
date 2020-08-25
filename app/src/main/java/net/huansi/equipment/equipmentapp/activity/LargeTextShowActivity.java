package net.huansi.equipment.equipmentapp.activity;

import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;

import butterknife.BindView;

import static net.huansi.equipment.equipmentapp.constant.Constant.LargerTextSHowActivityConstants.TEXT_CONTENT_PARAM;

public class LargeTextShowActivity extends BaseActivity {
    @BindView(R.id.LargerText)
    TextView largerText;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_text_larger;
    }

    @Override
    public void init() {
            setToolBarTitle("文本");
        String content = getIntent().getStringExtra(TEXT_CONTENT_PARAM);
        largerText.setText(content);
    }
}
