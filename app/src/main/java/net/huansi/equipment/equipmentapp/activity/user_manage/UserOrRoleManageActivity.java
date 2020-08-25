package net.huansi.equipment.equipmentapp.activity.user_manage;//package net.huansi.equipment.equipmentapp.activity.user_manage;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.view.View;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.BaseActivity;
//import net.huansi.equipment.equipmentapp.adapter.UserManageRoleAdapter;
//import net.huansi.equipment.equipmentapp.adapter.UserAdapter;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnItemLongClick;
//
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserManageActivityConstants.IS_USER_PARAM;
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserManageActivityConstants.LIST_PARAM;
//
///**
// * Created by 单中年 on 2017/2/23.
// */
//
//public class UserOrRoleManageActivity extends BaseActivity {
//    @BindView(R.id.lvUserOrRoleManage) ListView lvUserOrRoleManage;
//    private BaseAdapter adapter;
//    private List mList;
//
//    private boolean isUser;//是不是用户进行管理
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.user_or_role_manage_activity;
//    }
//
//    @Override
//    public void init() {
//        isUser=getIntent().getBooleanExtra(IS_USER_PARAM,false);
//        mList= (List) getIntent().getSerializableExtra(LIST_PARAM);
//        TextView tvSubTitle=getSubTitle();
//        tvSubTitle.setText("添加");
//        tvSubTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isUser) {
//
//                }else {
//
//                }
//            }
//        });
//        if(isUser) {
//            setToolBarTitle("用户管理");
//            adapter = new UserAdapter(mList, getApplicationContext());
//        }else {
//            setToolBarTitle("角色管理");
//            adapter = new UserManageRoleAdapter(mList, getApplicationContext());
//        }
//        lvUserOrRoleManage.setAdapter(adapter);
//    }
//
//    @Override
//    public void back() {
//
//    }
//
//    /**
//     * 修改或者更新
//     */
//    @OnItemLongClick(R.id.lvUserOrRoleManage)
//    boolean delOrUpdate(int position){
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.setItems(new String[]{"修改", "删除"}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                switch (i){
//                    case 0:
//                        break;
//                    case 1:
//                        break;
//                }
//            }
//        })
//                .show();
//        return true;
//    }
//
//}
