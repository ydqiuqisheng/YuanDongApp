//package net.huansi.equipment.equipmentapp.fragment;//package net.huansi.equipment.equipmentapp.fragment;
//
//import android.os.Bundle;
//import android.widget.GridView;
//
//import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.user_manage.UserRoleActivity;
//import net.huansi.equipment.equipmentapp.adapter.UserRoleUserFragmentRoleAdapter;
//import net.huansi.equipment.equipmentapp.adapter.UserRoleUserFragmentUserAdapter;
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
//public class UserRoleUserFragment extends BaseFragment {
//    @BindView(R.id.gvUserRoleUserRoleFragmentTop)
//    GridView gvUserRoleUserFragmentUser;//用户列表
//    @BindView(R.id.gvUserRoleUserRoleFragmentBottom)
//    GridView gvUserRoleUserFragmentRole;//角色列表
//
//    private UserRoleUserFragmentUserAdapter mUserAdapter;
//    private List<EPUser> mUserList;
//    private UserRoleUserFragmentRoleAdapter mRoleAdapter;
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
//        Bundle bundle=getArguments();
//        mUserList.addAll((ArrayList<EPUser>) bundle.getSerializable(FRAGMENT_USER_LIST_PARAM));
//        mRoleList.addAll((ArrayList<EPRole>) bundle.getSerializable(FRAGMENT_ROLE_LIST_PARAM));
//        allRoleMap=new HashMap<>();
//        for(int i=0;i<mRoleList.size();i++){
//            allRoleMap.put(mRoleList.get(i).IROLEID,i);
//        }
//        mRoleAdapter=new UserRoleUserFragmentRoleAdapter(mRoleList,getContext());
//        mUserAdapter = new UserRoleUserFragmentUserAdapter(mUserList, getActivity());
//        gvUserRoleUserFragmentUser.setAdapter(mUserAdapter);
//        gvUserRoleUserFragmentRole.setAdapter(mRoleAdapter);
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void fromActivity(ActivityToFragmentEvent event){
//        if(event.activityClass!= UserRoleActivity.class||
//                event.fragmentClass!=UserRoleRoleFragment.class) return;
//        mUserList.clear();
//        mRoleList.clear();
//        mUserList.addAll((ArrayList<EPUser>)event.o1);
//        mRoleList.addAll((ArrayList<EPRole>)event.o2);
//        if(mUserList.size()>0) lastUserChoosePosition=0;
//        mUserList.get(lastUserChoosePosition).isChoose=true;
//        mUserAdapter.notifyDataSetChanged();
//        getRoleDataByUser();
//        mRoleAdapter.notifyDataSetChanged();
//    }
//
//
//    @OnItemClick(R.id.gvUserRoleUserRoleFragmentTop)
//    void showRoleByUser(int position){
//        if(lastUserChoosePosition==position) return;
//        if(lastUserChoosePosition!=-1) {
//            EPUser lastUser=mUserList.get(lastUserChoosePosition);
//            lastUser.isChoose=true;
//            mUserList.set(lastUserChoosePosition,lastUser);
//        }
//        EPUser epUser=mUserList.get(position);
//        epUser.isChoose=true;
//        mUserList.set(position,epUser);
//        lastUserChoosePosition=position;
//        mUserAdapter.notifyDataSetChanged();
//        OthersUtil.showLoadDialog(dialog);
//        getRoleDataByUser();
//    }
//
//    /**
//     * 根据用户查询角色
//     */
//    private void getRoleDataByUser(){
//
//        final EPUser epUser=mUserList.get(lastUserChoosePosition);
//        //获取角色信息
//        RxjavaWebUtils.requestByGetJsonData(
//                (RxAppCompatActivity) getActivity(),
//                "spAppEPQueryRoleByUser",
//                "sUserNo="+epUser.SUSERNO,
//                getContext(),
//                dialog,
//                EPRole.class.getName(),
//                true,
//                null,
//                new SimpleHsWeb() {
//                    @Override
//                    public void success(HsWebInfo hsWebInfo) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        List<WsEntity>  entities=hsWebInfo.wsData.LISTWSDATA;
//                        for(int i=0;i<mRoleList.size();i++){
//                            EPRole epRole= mRoleList.get(i);
//                            epRole.isChoose=false;
//                            mRoleList.set(i,epRole);
//                        }
//                        for(int i=0;i<entities.size();i++){
//                           EPRole epRole= (EPRole) entities.get(i);
//                            if(allRoleMap.get(epRole.IROLEID)!=null){
//                                epRole.isChoose=true;
//                                mRoleList.set(allRoleMap.get(epRole.IROLEID),epRole);
//                            }
//                        }
//                        mRoleAdapter.notifyDataSetChanged();
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
