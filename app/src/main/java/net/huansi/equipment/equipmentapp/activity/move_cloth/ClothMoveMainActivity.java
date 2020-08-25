package net.huansi.equipment.equipmentapp.activity.move_cloth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tscdll.TSCActivity;
import com.tools.io.BluetoothPort;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.activity.call_repair.CallRepairInventoryActivity;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.util.SPHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

import static net.huansi.equipment.equipmentapp.util.SPHelper.USER_NO_KEY;

public class ClothMoveMainActivity extends BaseActivity {
    @BindView(R.id.btnRegisterSample)
    Button btnRegisterSample;
    @BindView(R.id.btnStokeIn)
    Button btnStokeIn;
    private List<String> regiaster;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cloth_move_main;
    }

    @Override
    public void init() {
        setToolBarTitle("样衣管理主页面");
        regiaster=new ArrayList<>();
        regiaster.add("Admin");//密码adminxx
        regiaster.add("B68818");
        regiaster.add("B78014");
        regiaster.add("B77653");
        regiaster.add("B76527");
        regiaster.add("B69298");
        regiaster.add("A57558");
        regiaster.add("A52957");
        regiaster.add("B82691");
        regiaster.add("B82254");
        regiaster.add("B79323");
        regiaster.add("B78584");
        regiaster.add("A55450");
        regiaster.add("A51514");
        regiaster.add("B52886");
        regiaster.add("A51124");
        regiaster.add("B62756");
        regiaster.add("B83210");
        regiaster.add("B82924");
        regiaster.add("B81020");
        regiaster.add("B75066");
        regiaster.add("B74534");
        regiaster.add("B58927");
        regiaster.add("B58680");
        regiaster.add("A57318");
        regiaster.add("A54022");
        String user = SPHelper.getLocalData(getApplicationContext(), USER_NO_KEY, String.class.getName(), "").toString();
        Log.e("TAG",user);
        if (regiaster.contains(user)){
            btnRegisterSample.setVisibility(View.VISIBLE);
            btnStokeIn.setVisibility(View.VISIBLE);
        }
    }
    @OnClick(R.id.btnRegisterSample)
    void toRegister(){
//        String[] methodArry={"Bluetooth(蓝牙)","WiFi(网络)"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
//        builder.setIcon(R.drawable.icon_bluetooth);
//        builder.setTitle("选择打印模式");
//        builder.setCancelable(true);
//        builder.setSingleChoiceItems(methodArry, 0, new DialogInterface.OnClickListener() {// 默认的选中
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
//                switch (which){
//                    case 0:
                        startActivity(new Intent(ClothMoveMainActivity.this,ClothPoActivity.class));
//                        break;
//                    case 1:
//                        startActivity(new Intent(ClothMoveMainActivity.this,ClothWiFiRegisterActivity.class));
//                        break;
//                }
//
//                dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
//            }
//        });
//        builder.show();// 让弹出框显示
//        OthersUtil.showDoubleChooseDialog(this, "请确认已经打开pad蓝牙和打印机开关",null, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(ClothMoveMainActivity.this,ClothRegisterActivity.class));
//            }
//        });
    }
    @OnClick(R.id.btnQueryRecord)
    void toQuery(){
        startActivity(new Intent(ClothMoveMainActivity.this,ClothQueryRecordActivity.class));
    }
    @OnClick(R.id.btnTakeSample)
    void toTake(){
        startActivity(new Intent(ClothMoveMainActivity.this,ClothTakeActivity.class));
    }
    @OnClick(R.id.btnStokeIn)
    void stokeIn(){
        startActivity(new Intent(ClothMoveMainActivity.this,ClothStokeActivity.class));
    }


}
