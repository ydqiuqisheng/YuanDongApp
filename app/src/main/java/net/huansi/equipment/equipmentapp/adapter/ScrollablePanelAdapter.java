package net.huansi.equipment.equipmentapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelin.scrollablepanel.library.PanelAdapter;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.check_simple.CheckSimpleMainActivity;
import net.huansi.equipment.equipmentapp.entity.NumberInfo;
import net.huansi.equipment.equipmentapp.entity.StandardInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScrollablePanelAdapter extends PanelAdapter {
    private static final int TITLE_TYPE = 4;
    private static final int ROOM_TYPE = 0;
    private static final int DATE_TYPE = 1;
    private static final int ORDER_TYPE = 2;

    private List<StandardInfo> standardInfoList=new ArrayList<>();
    private List<NumberInfo> numberInfoList = new ArrayList<>();
    private List<String> ordersList =new ArrayList<>();

    @Override
    public int getRowCount() {
        return standardInfoList.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return numberInfoList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case DATE_TYPE:
                //setNumberView(column, (NumberViewHolder) holder);
                break;
            case ROOM_TYPE:
                setStandardView(row, (StandardViewHolder) holder);
                break;
            case ORDER_TYPE:
                //setOrderView(row, (OrderViewHolder) holder);
                break;
            case TITLE_TYPE:
                break;
            default:
                //setOrderView(row, column, (OrderViewHolder) holder);
        }
    }


    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return ROOM_TYPE;
        }
        if (row == 0) {
            return DATE_TYPE;
        }
        return ORDER_TYPE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
//            case DATE_TYPE:
//                return new NumberViewHolder(LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.listitem_number_info, parent, false));
            case ROOM_TYPE:
                return new StandardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_standard_info, parent, false));
//            case ORDER_TYPE:
//                return new OrderViewHolder(LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.listitem_order_info, parent, false));
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_title, parent, false));
            default:
                break;
        }
        return new OrderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_order_info, parent, false));
    }


    private void setNumberView(int pos, NumberViewHolder viewHolder) {
        NumberInfo numberInfo = numberInfoList.get(pos - 1);
        if (numberInfo != null && pos > 0) {
            viewHolder.numberTextView.setText(numberInfo.getNumber());

        }
    }

    private void setStandardView(int pos, StandardViewHolder viewHolder) {
        StandardInfo standardInfo = standardInfoList.get(pos - 1);
        if (standardInfo != null && pos > 0) {
            viewHolder.standardTypeTextView.setText(standardInfo.getStandardName());//规格
            viewHolder.floatTypeTextView.setText(standardInfo.getStandardFloat());//允差
            viewHolder.biaoTypeTextView.setText(standardInfo.getStandardBiao());//尺码MSL
            viewHolder.sizeTypeTextView.setText(standardInfo.getStandardSize());//尺寸3.2
        }
    }

    private void setOrderView(int pos, OrderViewHolder viewHolder) {
         //String orderInfo = ordersList.get(0);
                viewHolder.nameTextView.setClickable(true);
                for (int i=0;i<ordersList.size();i++){
                    viewHolder.nameTextView.setText(ordersList.get(pos-1));
                }
                Log.e("TAG","orderlist="+ordersList+pos);


    }

    public List<String> getOrderView(final int row, final int column, OrderViewHolder viewHolder) {
        //final OrderInfo orderInfo = ordersList.get(row - 1).get(column - 1);
            List<String> orderInfoList = new ArrayList<>();
            for (int j = 1; j <= row; j++) {
//                OrderInfo orderInfo = new OrderInfo();
//                orderInfo.setGuestName("NO." + i + j);
                //orderInfoList.add("NO." + i + j);
                orderInfoList.add(viewHolder.nameTextView.getText().toString());
            }


        return orderInfoList;
    }




    private static class NumberViewHolder extends RecyclerView.ViewHolder {
        public TextView numberTextView;
        //public TextView weekTextView;

        public NumberViewHolder(View itemView) {
            super(itemView);
            this.numberTextView = (TextView) itemView.findViewById(R.id.simple_date);
            //this.weekTextView = (TextView) itemView.findViewById(R.id.week);
        }

    }

    private static class StandardViewHolder extends RecyclerView.ViewHolder {
        public TextView standardTypeTextView;
        public TextView floatTypeTextView;
        public TextView biaoTypeTextView;
        public TextView sizeTypeTextView;

        public StandardViewHolder(View view) {
            super(view);
            this.standardTypeTextView = (TextView) view.findViewById(R.id.simple_check_standard);
            this.floatTypeTextView = (TextView) view.findViewById(R.id.simple_check_float);
            this.biaoTypeTextView = (TextView) view.findViewById(R.id.simple_check_biao);
            this.sizeTypeTextView = (TextView) view.findViewById(R.id.simple_check_size);
        }
    }

    private static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        //public TextView statusTextView;
        public View view;

        public OrderViewHolder(View view) {
            super(view);
            this.view = view;
           // this.statusTextView = (TextView) view.findViewById(R.id.status);
            this.nameTextView = (TextView) view.findViewById(R.id.sp_line);
        }

    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(View view) {
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.simple_check_title);
        }
    }


    public void setStandardInfoList(List<StandardInfo> standardInfoList) {
        this.standardInfoList = standardInfoList;
    }

    public void setNumberInfoList(List<NumberInfo> numberInfoList) {
        this.numberInfoList = numberInfoList;
    }
//中间内容填什么
    public void setOrdersList(List<String> ordersList) {
        this.ordersList = ordersList;
    }
}
