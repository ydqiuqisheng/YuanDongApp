package net.huansi.equipment.equipmentapp.util;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2016/5/3.
 */
public class ViewHolder {

    public static <T extends View> T get(View view, int id) {
        SparseArray viewHolder = (SparseArray) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray();
            view.setTag(viewHolder);
        }
        View childView = (View) viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
