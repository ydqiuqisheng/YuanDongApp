package net.huansi.equipment.equipmentapp.adapter;//package net.huansi.equipment.equipmentapp.adapter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.util.ViewHolder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by shanz on 2017/3/9.
// */
//
//public class AllInfoWithoutInputAdapter extends ArrayAdapter<String> implements Filterable {
//    private ArrayFilter mFilter;
//
//    public AllInfoWithoutInputAdapter(Context context, int resource) {
//        super(context, resource);
//    }
//
//    public AllInfoWithoutInputAdapter(Context context, int resource, int textViewResourceId) {
//        super(context, resource, textViewResourceId);
//    }
//
//    public AllInfoWithoutInputAdapter(Context context, int resource, String[] objects) {
//        super(context, resource, objects);
//    }
//
//    public AllInfoWithoutInputAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
//        super(context, resource, textViewResourceId, objects);
//    }
//
//    public AllInfoWithoutInputAdapter(Context context, int resource, List<String> objects) {
//        super(context, resource, objects);
//    }
//
//    public AllInfoWithoutInputAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
//        super(context, resource, textViewResourceId, objects);
//    }
//
//
//    @Override
//    public @NonNull Filter getFilter() {
//        if (mFilter == null) {
//            mFilter = new ArrayFilter();
//        }
//        return mFilter;
//    }
//
//    private class ArrayFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence prefix) {
//            final FilterResults results = new FilterResults();
//
//            if (mOriginalValues == null) {
//                synchronized (mLock) {
//                    mOriginalValues = new ArrayList<>(mObjects);
//                }
//            }
//
//            if (prefix == null || prefix.length() == 0) {
//                final ArrayList<T> list;
//                synchronized (mLock) {
//                    list = new ArrayList<>(mOriginalValues);
//                }
//                results.values = list;
//                results.count = list.size();
//            } else {
//                final String prefixString = prefix.toString().toLowerCase();
//
//                final ArrayList<T> values;
//                synchronized (mLock) {
//                    values = new ArrayList<>(mOriginalValues);
//                }
//
//                final int count = values.size();
//                final ArrayList<T> newValues = new ArrayList<>();
//
//                for (int i = 0; i < count; i++) {
//                    final T value = values.get(i);
//                    final String valueText = value.toString().toLowerCase();
//
//                    // First match against the whole, non-splitted value
//                    if (valueText.startsWith(prefixString)) {
//                        newValues.add(value);
//                    } else {
//                        final String[] words = valueText.split(" ");
//                        for (String word : words) {
//                            if (word.startsWith(prefixString)) {
//                                newValues.add(value);
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                results.values = newValues;
//                results.count = newValues.size();
//            }
//
//            return results;
//        }
//    }
//}
