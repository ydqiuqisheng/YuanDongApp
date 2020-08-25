//package net.huansi.equipment.equipmentapp.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.entity.CutPiecesDetail;
//import net.huansi.equipment.equipmentapp.util.ViewHolder;
//
//import java.util.List;
//
//public class CutPiecesDetailAdapter extends HsBaseAdapter<CutPiecesDetail>{
//
//
//    public CutPiecesDetailAdapter(List<CutPiecesDetail> list, Context context) {
//        super(list, context);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup viewGroup) {
//        if (convertView == null) mInflater.inflate(R.layout.cut_pieces_detail_item, viewGroup, false);
//
//            TextView receiveDate = (TextView) convertView.findViewById(R.id.receiveDate);
//            TextView vatNo = (TextView) convertView.findViewById(R.id.vatNo);
//            TextView result = (TextView) convertView.findViewById(R.id.result);
//            TextView level = (TextView) convertView.findViewById(R.id.level);
//            TextView abNormalType = (TextView) convertView.findViewById(R.id.abNormalType);
//            TextView remark = (TextView) convertView.findViewById(R.id.remark);
////            TextView receiveDate = ViewHolder.get(convertView, R.id.receiveDate);
////            TextView vatNo = ViewHolder.get(convertView, R.id.vatNo);
////            TextView result = ViewHolder.get(convertView, R.id.result);
////            TextView level = ViewHolder.get(convertView, R.id.level);
////            TextView abNormalType = ViewHolder.get(convertView, R.id.abNormalType);
////            TextView remark = ViewHolder.get(convertView, R.id.remark);
//            CutPiecesDetail cutPiecesDetail = mList.get(position);
//            receiveDate.setText(cutPiecesDetail.RECEIVEDATE);
//            vatNo.setText(cutPiecesDetail.VATNO);
//            result.setText(cutPiecesDetail.RESULT);
//            level.setText(cutPiecesDetail.LEVEL);
//            abNormalType.setText(cutPiecesDetail.ABNORMALTYPE);
//            remark.setText(cutPiecesDetail.REMARK);
//
//        return convertView;
//    }
//}
