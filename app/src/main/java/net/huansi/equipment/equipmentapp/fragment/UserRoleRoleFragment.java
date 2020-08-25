//package net.huansi.equipment.equipmentapp.fragment;//package net.huansi.equipment.equipmentapp.fragment;
//
//import android.os.Bundle;
//import android.widget.GridView;
//
//import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.user_manage.UserRoleActivity;
//import net.huansi.equipment.equipmentapp.adapter.UserRoleRoleFragmentRoleAdapter;
//import net.huansi.equipment.equipmentapp.adapter.UserRoleRoleFragmentUserAdapter;
//import net.huansi.equipment.equipmentapp.entity.EPRole;
//import net.huansi.equipment.equipmentapp.entity.EPUser;
//import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
//import net.huansi.equipment.equipmentapp.entity.WsEntity;
//import net.huansi.equipment.equipmentapp.event.ActivityToFragmentEvent;
//import net.huansi.equipment.equipmentapp.imp.SimpleHsWeb;
//import net.huansi.equipment.equipmentapp.util.OthersUtil;
//import net.huansi.equipment.equipmentapp.util.RxjavaWebUtils;
//import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.OnItemClick;
//
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserRoleActivityConstants.FRAGMENT_ROLE_LIST_PARAM;
//import static net.huansi.equipment.equipmentapp.constant.Constant.UserRoleActivityConstants.FRAGMENT_USER_LIST_PARAM;
//
///**
// * Created by 单中年 on 2017/2/23.
// */
//
//public class UserRoleRoleFragment extends BaseFragment {
//
//    @BindView(R.id.gvUserRoleUserRoleFragmentTop)
//    GridView gvUserRoleRoleFragmentRole;//角色列表
//    @BindView(R.id.gvUserRoleUserRoleFragmentBottom)
//    GridView gvUserRoleRoleFragmentUser;//用户列表
//
//    private UserRoleRoleFragmentRoleAdapter mRoleAdapter;
//    private List<EPUser> mUserList;
//    private UserRoleRoleFragmentUserAdapter mUserAdapter;
//    private List<EPRole> mRoleList;
//    private Map<String,Integer> allRoleMap;//所有角色的map key是id value是mUserList的位置
//    private LoadProgressDialog dialog;
//
//    private int lastUserChoosePosition=-1;//选中的用户位置
//
//
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.user_activity;
//    }
//
//    @Override
//    public void init() {
//        OthersUtil.registerEvent(this);
//        dialog=new LoadProgressDialog(getActivity());
//        mUserList = new ArrayList<>();
//        mRoleList=new ArrayList<>();
//        allRoleMap=new HashMap<>();
//        Bundle bundle=getArguments();
//        mUserList.addAll((ArrayList<EPUser>) bundle.getSerializable(FRAGMENT_USER_LIST_PARAM));
//        mRoleList.addAll((ArrayList<EPRole>) bundle.getSerializable(FRAGMENT_ROLE_LIST_PARAM));
//        for(int i=0;i<mRoleList.size();i++){
//            allRoleMap.put(mRoleList.get(i).IROLEID,i);
//        }
//        mRoleAdapter=new UserRoleRoleFragmentRoleAdapter(mRoleList,getContext());
//        mUserAdapter = new UserRoleRoleFragmentUserAdapter(mUserList, getActivity());
//        gvUserRoleRoleFragmentUser.setAdapter(mUserAdapter);
//        gvUserRoleRoleFragmentRole.setAdapter(mRoleAdapter);
//    }
//
//
//    @OnItemClick(R.id.gvUserRoleUserRoleFragmentTop)
//    void showRoleByUser(int position){
//        if(lastUserChoosePosition==position) return;
//        if(lastUserChoosePosition!=-1) {
//            EPRole lastEPRole=mRoleList.get(lastUserChoosePosition);
//            lastEPRole.isChoose=true;
//            mRoleList.set(lastUserChoosePosition,lastEPRole);
//        }
//        EPRole epRole=mRoleList.get(position);
//        epRole.isChoose=true;
//        mRoleList.set(position,epRole);
//        lastUserChoosePosition=position;
//        mRoleAdapter.notifyDataSetChanged();
//        OthersUtil.showLoadDialog(dialog);
//        getUserDataByRole();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void fromActivity(ActivityToFragmentEvent event){
//        if(event.activityClass!= UserRoleActivity.class||
//                event.fragmentClass!=UserRoleRoleFragment.class) return;
//        mUserList.clear();
//        mRoleList.clear();
//        mUserList.addAll((ArrayList<EPUser>)event.o1);
//        mRoleList.addAll((ArrayList<EPRole>)event.o2);
//        if(mRoleList.size()>0) lastUserChoosePosition=0;
//        mRoleList.get(lastUserChoosePosition).isChoose=true;
//        mRoleAdapter.notifyDataSetChanged();
//        getUserDataByRole();
//        mUserAdapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 根据角色查询用户
//     */
//    private void getUserDataByRole(){
//        EPRole epRole =mRoleList.get(lastUserChoosePosition);
//        //获取角色信息
//        RxjavaWebUtils.requestByGetJsonData(
//                (RxAppCompatActivity) getActivity(),
//                "spAppEPQueryUserByRole",
//                "iRoleId="+epRole.IROLEID,
//                getContext(),
//                dialog,
//                EPUser.class.getName(),
//                true,
//                null,
//                new SimpleHsWeb() {
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        List<WsEntity>  entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(int i=0;i<mUserList.size();i++){
//                            EPUser epUser= mUserList.get(i);
//                            epUser.isChoose=false;
//                            mUserList.set(i,epUser);
//                        }
//                        for(int i=0;i<entities.size();i++){
//                            EPUser epUser= (EPUser) entities.get(i);
//                            if(allRoleMap.get(epUser.IUSERID)!=null){
//                                epUser.isChoose=true;
//                                mUserList.set(allRoleMap.get(epUser.IUSERID),epUser);
//                            }
//                        }
//                        mUserAdapter.notifyDataSetChanged();
//
//                    }
//                });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        OthersUtil.unregisterEvent(this);
//    }
//}
