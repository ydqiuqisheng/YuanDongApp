package net.huansi.equipment.equipmentapp.activity.check_quality;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.activity.BaseActivity;
import net.huansi.equipment.equipmentapp.entity.HsWebInfo;
import net.huansi.equipment.equipmentapp.entity.PackagingPicEntity;
import net.huansi.equipment.equipmentapp.listener.WebListener;
import net.huansi.equipment.equipmentapp.service.NikeBomService;
import net.huansi.equipment.equipmentapp.util.NewRxjavaWebUtils;
import net.huansi.equipment.equipmentapp.util.OthersUtil;
import net.huansi.equipment.equipmentapp.widget.LoadProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Func1;

public class PackagingActivity extends BaseActivity{
    private HsWebInfo hsWebInfo=new HsWebInfo();
    private List<String> picUrl=new ArrayList<>();
    private LoadProgressDialog dialog;
    private PicListAdapter picListAdapter=new PicListAdapter();
    @BindView(R.id.lvPackagePic)
    ListView lvPackagePic;
    @BindView(R.id.etPackagingNumber)
    EditText etPackagingNumber;

    private String json="";
    @BindView(R.id.jsData)
    TextView jsData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_packaging;
    }

    @Override
    public void init() {
    setToolBarTitle("包装:图片审查");
    dialog=new LoadProgressDialog(this);
    }

    @OnClick(R.id.getToken)
    void getToken(){

    }


    @OnClick(R.id.btnPackagingSearch)
    void picSearch(){
        picUrl.clear();
        final String FEPO = etPackagingNumber.getText().toString();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(PackagingActivity.this, hsWebInfo)
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                return NewRxjavaWebUtils.getJsonData(getApplicationContext(),
                                        "spAppGetQCPackagePic",
                                        "FEPO="+FEPO,
                                        String.class.getName(),
                                        false,
                                        "helloWorld");
                            }
                        })
                ,PackagingActivity.this, dialog, new WebListener() {
                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        String json = hsWebInfo.json;
                        //json解析
                        PackagingPicEntity packagingPicEntity = JSON.parseObject(json, PackagingPicEntity.class);
                        List<PackagingPicEntity.DATABean> data = packagingPicEntity.getDATA();
                        for (int i=0;i<data.size();i++){
                            picUrl.add(data.get(i).getPICPATH());
                            Log.e("TAG", "picPath="+data.get(i).getPICPATH());
                        }
                        lvPackagePic.setAdapter(picListAdapter);
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo) {
                        Log.e("TAG", "error1="+hsWebInfo.error.error);
                        OthersUtil.showTipsDialog(PackagingActivity.this,"无此单号或网络可能有问题！");
                    }
                });
    }

    private class PicListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return picUrl.size();
        }

        @Override
        public Object getItem(int position) {
            return picUrl.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null) convertView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.packaging_pic_item,parent,false);
            ImageView ivPackagingPicItem = (ImageView) convertView.findViewById(R.id.ivPackagingPicItem);
            Glide.with(PackagingActivity.this)
                    .load(picUrl.get(position).toString())
                    .into(ivPackagingPicItem);
            return convertView;
        }
    }
}
