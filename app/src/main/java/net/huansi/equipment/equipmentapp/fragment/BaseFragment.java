//package net.huansi.equipment.equipmentapp.fragment;//package net.huansi.equipment.equipmentapp.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.trello.rxlifecycle.components.support.RxFragment;
//
//import net.huansi.equipment.equipmentapp.listener.InitListener;
//
//import butterknife.ButterKnife;
//
///**
// * Created by 单中年 on 2017/2/23.
// */
//
//public abstract class BaseFragment extends RxFragment implements InitListener{
//
//    public abstract int getLayoutId();
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view=inflater.inflate(getLayoutId(),container,false);
//        ButterKnife.bind(this,view);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        init();
//    }
//}
