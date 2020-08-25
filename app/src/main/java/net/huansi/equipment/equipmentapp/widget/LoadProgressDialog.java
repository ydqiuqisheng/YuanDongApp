package net.huansi.equipment.equipmentapp.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.huansi.equipment.equipmentapp.R;
import net.huansi.equipment.equipmentapp.util.DeviceUtil;


public class LoadProgressDialog {
	private Activity mActivity;
	private LinearLayout mProgressLayout;
	TextView titleTxtv;
	Dialog dialog;

	public LoadProgressDialog(Activity act) {
		mActivity = act;
		dialog = new Dialog(mActivity, R.style.Dialog);
	}




	public void showLoadDialog(String sMessage) {
		if (dialog == null) {
			dialog = new Dialog(mActivity, R.style.Dialog);
		}
		dialog.setContentView(R.layout.load_progress);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = DeviceUtil.getScreenHeight(mActivity);
		lp.width = (int) (0.2 * screenW);
		lp.height = (int) (0.2 * screenW);
		lp.alpha = 0.5f;
		titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);
		if (sMessage.isEmpty()) {
			titleTxtv.setText("加载中...");
		} else {
			setShowMessage(sMessage);
		}
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
    public void hideDialog() {
		if(dialog!=null) {
			dialog.hide();
		}
    }


	public void dismiss(){
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
	}

	public void setShowMessage(String sMessage) {
		if((titleTxtv!=null)&&(!sMessage.isEmpty())) {
			titleTxtv.setText(sMessage);
		}
	}

	public boolean isShowing(){
		if(dialog==null){
			return false;
		}else {
			return dialog.isShowing();
		}
	}

	public void setOnDissmissListener(OnDismissListener dissmissListener){
		if(dialog!=null) {
			dialog.setOnDismissListener(dissmissListener);
		}
	}

	public void setCancelled(){
		if(dialog!=null) {
			dialog.setCancelable(false);
		}
	}

	public void setCancelable(boolean flag){
		if(dialog!=null) {
			dialog.setCancelable(flag);
		}
	}
    
}
