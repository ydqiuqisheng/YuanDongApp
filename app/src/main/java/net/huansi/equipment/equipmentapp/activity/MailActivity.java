package net.huansi.equipment.equipmentapp.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.WsData;
import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
import net.huansi.equipment.equipmentapp.sqlite_db.InventoryDetail;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static net.huansi.equipment.equipmentapp.constant.Constant.MailActivityConstants.MAIL_DATA_PARAM;

/**
 * Created by shanz on 2017/3/10.
 */

public class MailActivity extends BaseActivity {
    @BindView(R.id.tvMailRFID) TextView tvMailRFID;//RFID
    @BindView(R.id.tvMailInFactoryDate) TextView tvMailInFactoryDate;//入厂时间
    @BindView(R.id.tvMailDepreciationDate) TextView tvMailDepreciationDate;//折旧时间
    @BindView(R.id.tvMailAssetsCode) TextView tvMailAssetsCode;//资产编号
    @BindView(R.id.tvMailOufFactoryCode) TextView tvMailOufFactoryCode;//出厂编号
    @BindView(R.id.tvMailCostCenter) TextView tvMailCostCenter;//成本中心
    @BindView(R.id.tvMailDeclareNum) TextView tvMailDeclareNum;//报关单号
    @BindView(R.id.tvInventoryItemCompanyIn) TextView tvInventoryItemCompanyIn;//所在工厂
    @BindView(R.id.tvMailInventoryStatus) TextView tvMailInventoryStatus;//盘点状态

    @BindView(R.id.mailAddresseeLayout)LinearLayout mailAddresseeLayout;
    @BindView(R.id.etMailAddressee)EditText etMailAddressee;//收件人
    @BindView(R.id.etMailCC) EditText etMailCC;//CC
    @BindView(R.id.mailCCLayout) LinearLayout mailCCLayout;
    @BindView(R.id.etMailSender) EditText etMailSender;
    @BindView(R.id.etMailSubject) EditText etMailSubject;
    @BindView(R.id.spMailImportance) Spinner spMailImportance;

    private ArrayAdapter<String> importanceAdapter;


    private Map<String,String> addresseeMap;
    private Map<String,String> ccMap;

//    @BindView(R.id.btnMailSendMail) Button btnMailSendMail;//发送邮件

    private LoadProgressDialog dialog;

    private InventoryDetail detail;
    @Override
    protected int getLayoutId() {
        return R.layout.mail_activity;
    }

    @Override
    public void init() {
        OthersUtil.hideInputFirst(this);
        detail= (InventoryDetail) getIntent().getSerializableExtra(MAIL_DATA_PARAM);
        dialog=new LoadProgressDialog(this);
        tvMailRFID.setText(detail.getEPCode());
        tvMailInFactoryDate.setText(detail.getInFactoryDate());
        tvMailDepreciationDate.setText(detail.getDepreciationStartingDate());
        tvMailAssetsCode.setText(detail.getAssetsCode());
        tvMailOufFactoryCode.setText(detail.getOutFactoryCode());
        tvMailCostCenter.setText(detail.getCostCenter());
        tvMailDeclareNum.setText(detail.getDeclarationNum());
        tvInventoryItemCompanyIn.setText(detail.getFactory());
        addresseeMap=new HashMap<>();
        ccMap=new HashMap<>();
        switch (detail.getStatus()){
            case -2:
                tvMailInventoryStatus.setText("未盘");
                break;
            case -1:
                tvMailInventoryStatus.setText("盘亏");
                break;
            case 0:
                tvMailInventoryStatus.setText("已盘");
                break;
            case 1:
                tvMailInventoryStatus.setText("盘盈");
                break;
        }
        importanceAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.string_item,R.id.text,
                new String []{"不重要","一般","重要"});
        spMailImportance.setAdapter(importanceAdapter);
    }

    /**
     * 增加收件人
     */
    @OnClick(R.id.imvMailAddresseeAdd)
    void addAddressee(){
        String addressee=etMailAddressee.getText().toString().trim();
        if(addressee.isEmpty()){
            OthersUtil.showTipsDialog(this,"请输入收件人");
            return;
        }
        etMailAddressee.getText().clear();
        final View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.string_item,null);
         TextView tv= (TextView) view.findViewById(R.id.text);
        tv.setText(addressee);
        addresseeMap.put(view.toString(),addressee);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mailAddresseeLayout.removeView(view);
                addresseeMap.remove(view.toString());
                return true;
            }
        });
        mailAddresseeLayout.addView(view);
    }

    @OnClick(R.id.imvMailCCAdd)
    void addCC(){
        String cc=etMailCC.getText().toString().trim();
        if(cc.isEmpty()){
            OthersUtil.showTipsDialog(this,"请输入抄送人员");
            return;
        }
        etMailCC.getText().clear();
        final View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.string_item,null);
         TextView tv= (TextView) view.findViewById(R.id.text);
        tv.setText(cc);
        ccMap.put(view.toString(),cc);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mailCCLayout.removeView(view);
                ccMap.remove(view.toString());
                return true;
            }
        });
        mailCCLayout.addView(view);
    }

    @OnClick(R.id.btnMailSendMail)
    void send(){
        sendMail();
    }

    /**
     * 发送邮件
     */
    private void sendMail(){
        String sender=etMailSender.getText().toString();
        if(sender.isEmpty()){
            OthersUtil.showTipsDialog(this,"请输入发送人");
            return;
        }
//        String subject=etMailSubject.getText().toString();
//        if(subject.isEmpty()){
//            OthersUtil.showTipsDialog(this,"请输入主题");
//            return;
//        }
        OthersUtil.showLoadDialog(dialog);
        Map<String,String> map=new HashMap<>();
        map.put("sDisplayName","12569");
        Iterator<Map.Entry<String,String>> addresseeIt=addresseeMap.entrySet().iterator();
        StringBuffer sbAddressee=new StringBuffer();
        while (addresseeIt.hasNext()){
            sbAddressee.append(addresseeIt.next().getValue()+";");
        }
        if(!sbAddressee.toString().isEmpty()) sbAddressee=sbAddressee.delete(sbAddressee.length()-1,sbAddressee.length());
        if(sbAddressee.toString().isEmpty()){
            OthersUtil.showTipsDialog(this,"请添加收件人");
            return;
        }
        map.put("sSendToList",sbAddressee.toString());

        Iterator<Map.Entry<String,String>> ccIt=ccMap.entrySet().iterator();
        StringBuffer sbCC=new StringBuffer();
        while (ccIt.hasNext()){
            sbCC.append(ccIt.next().getValue()+";");
        }
        if(!sbCC.toString().isEmpty()) sbCC=sbCC.delete(sbCC.length()-1,sbCC.length());
        map.put("sCCList",sbCC.toString());
        map.put("sSubject",etMailSubject.getText().toString());
        map.put("sMailBody","<style>.table-d table{ background:black}  .table-d table td{ background:#FFF}</style> " +
                "<div class='table-d'> " +
                "<table  width='400' cellspacing='1' cellpadding='1' >" +
                "<tr><td>RFID</td><td>"+detail.getEPCode()+"</td></tr>" +
                "<tr><td>入厂日期</td><td>"+detail.getInFactoryDate()+"</td></tr>" +
                "<tr><td>折旧日期</td><td>"+detail.getDepreciationStartingDate()+"</td></tr>" +
                "<tr><td>资产编号</td><td>"+detail.getAssetsCode()+"</td></tr>" +
                "<tr><td>出厂编号</td><td>"+detail.getOutFactoryCode()+"</td></tr>" +
                "<tr><td>成本中心</td><td>"+detail.getCostCenter()+"</td></tr>" +
                "<tr><td>报关单号</td><td>"+detail.getDeclarationNum()+"</td></tr>" +
                "<tr><td>所在工厂</td><td>"+detail.getFactory()+"</td></tr>" +
                "<tr><td>盘点状态</td><td>"+tvMailInventoryStatus.getText()+"</td></tr>" +
                "</table></div>");
        map.put("sAttachments" ,"");
        map.put("iMailPriority",spMailImportance.getSelectedItemPosition()+"");
        RxjavaWebUtils.requestByNormalFunction(this,
                map,
                "SendMail",
                dialog,
                WsData.class.getName(),
                false,
                "发送失败",
                new SimpleHsWeb() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        OthersUtil.ToastMsg(getApplicationContext(),"发送成功！！");
                        finish();
                    }
                });
    }
}
