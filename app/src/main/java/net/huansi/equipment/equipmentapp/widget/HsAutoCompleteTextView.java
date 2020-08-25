package net.huansi.equipment.equipmentapp.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

/**
 * Created by shanz on 2017/3/9.
 */

public class HsAutoCompleteTextView extends AutoCompleteTextView {
    public HsAutoCompleteTextView(Context context) {
        super(context);
    }
    public HsAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public HsAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean enoughToFilter() {
        return true;
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
    }
}
