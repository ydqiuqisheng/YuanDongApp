package net.huansi.equipment.equipmentapp.adapter;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.MaterialList;
import net.huansi.equipment.equipmentapp.util.ViewHolder;

import java.util.List;

/**
 * Created by zhou.mi on 2018/1/11.
 */

public class MaterialListAdapter extends HsBaseAdapter<MaterialList> implements View.OnClickListener {
    private CallBack mCallBack;
    public MaterialListAdapter(List<MaterialList> list, Context context, CallBack mCallBack) {
        super(list, context);
        this.mCallBack = mCallBack;
    }

    public interface CallBack{
        void click(View view);

    }

    @Override
    public void onClick(View v) {
        mCallBack.click(v);
    }
    @Override
    public View getView( int position, View view, ViewGroup viewGroup) {
        if(view==null) view=mInflater.inflate(R.layout.material_list_parent_item,viewGroup,false);
        TextView material_SN= ViewHolder.get(view,R.id.material_SN);//SN

        TextView material_FEPO= ViewHolder.get(view,R.id.material_FEPO);//FEPO

        TextView material_QuantityPs=ViewHolder.get(view,R.id.material_QuantityPs);//总码长
        TextView material_QuantityPsCheck=ViewHolder.get(view,R.id.material_QuantityPsCheck);//已传码长
        //TextView material_QuantityPsUnCheck=ViewHolder.get(view,R.id.material_QuantityPsUnCheck);//未传码长
        TextView material_TotalNum= ViewHolder.get(view,R.id.material_TotalNum);//总匹数
        Button material_CheckedNum= ViewHolder.get(view,R.id.material_CheckedNum);//已传匹数
        //Button material_UnCheckedNum= ViewHolder.get(view,R.id.material_UnCheckedNum);//未传匹数
        TextView material_nowMC=ViewHolder.get(view,R.id.material_nowMC);//本次扫描码长
        Button material_nowPS=ViewHolder.get(view,R.id.material_nowPS);//本次扫描匹数
        TextView material_isMatch=ViewHolder.get(view,R.id.material_isMatch);//匹配erp
        TextView material_exemption=ViewHolder.get(view,R.id.material_exemption);//匹配erp
        Button material_NoNo=ViewHolder.get(view,R.id.material_NoNo);//未传待扫
        MaterialList list=mList.get(position);
        material_SN.setText(list.SN);
        material_FEPO.setText(list.FEPOQUANTITY);
        material_QuantityPs.setText(Float.toString(list.QUANTITYTOTAL));
        material_QuantityPsCheck.setText(Float.toString(list.QUANTITYCHECKED));
        //material_QuantityPsUnCheck.setText(Float.toString(list.QUANTITYUNCHECK));
        material_TotalNum.setText(Integer.toString(list.PNOTOTAL));
        material_CheckedNum.setText(Integer.toString(list.PNOCHECKED));
        //material_UnCheckedNum.setText(Integer.toString(list.PNOUNCHECK));
        material_nowMC.setText(Float.toString(list.QUANTITYSCAN));
        material_nowPS.setText(Integer.toString(list.PNOSCAN));
        material_isMatch.setText(list.ISMATCHERPPO);
        material_exemption.setText(list.EXEMPTION);
        material_NoNo.setText(Integer.toString(list.NONONUM));
        material_CheckedNum.setOnClickListener(this);
        material_CheckedNum.setTag(R.id.tag_checked,position);
        material_CheckedNum.setTag(1);
//        material_UnCheckedNum.setOnClickListener(this);
//        material_UnCheckedNum.setTag(position+7);
        material_nowPS.setOnClickListener(this);
        material_nowPS.setTag(R.id.tag_scan,position);
        material_nowPS.setTag(2);
        material_NoNo.setOnClickListener(this);
        material_NoNo.setTag(R.id.tag_nono,position);
        material_NoNo.setTag(3);
        return view;
    }



}
