package txt.jiantu.create.com.myjiantuproject.per;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import txt.jiantu.create.com.myjiantuproject.R;
import txt.jiantu.create.com.myjiantuproject.util.LogUtil;
import txt.jiantu.create.com.myjiantuproject.util.ToastManager;
import txt.jiantu.create.com.myjiantuproject.util.Util;


/**
 * 展示权限请求的弹出框
 * Created by okkuaixiu on 2017/9/22.
 */

public class PermissionDialog {
    private static AlertDialog alertDialog;
    /*
    判断是哪一种权限，返回不同的提示语
     */
    public static String getDialogWord(Context context, String permissionStr){
        try {
            if (Util.isTextNull(permissionStr)) {
                return null;
            }
            if (permissionStr.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    || permissionStr.equals(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return context.getResources().getString(R.string.allow_location);
            } else if (permissionStr.equals(android.Manifest.permission.CAMERA)) {
                return context.getResources().getString(R.string.allow_take_photo);
            } else if (permissionStr.equals(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    || permissionStr.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return context.getResources().getString(R.string.allow_take_write);
            }
        } catch (Exception e) {
            LogUtil.e(PermissionDialog.class, "getDialogWord()", e);
        }
        return null;
    }

    /**
     * 跳转到权限管理页面
     * String permission 请求的权限名称
     */
    public static void intentApplicationPermi(Context context, String permission){
        intentPermission(context, getDialogWord(context, permission));
    }

    /*
	跳转到应用权限管理的界面
	String textContent 提示语
	 */
    public static void intentPermission(final Context context, String textContent){
        try {
            alertDialog = new AlertDialog.Builder(context,
                    AlertDialog.THEME_HOLO_LIGHT).create();
            View pp = LayoutInflater.from(context).inflate(
                    R.layout.checkupdate, null);
            TextView texttile = (TextView) pp.findViewById(R.id.text_title);
            TextView text_content = (TextView) pp
                    .findViewById(R.id.checkupdate_text);
            texttile.setText(R.string.allow_permission);
            text_content.setText(textContent);
            Button confirm_exit = (Button) pp.findViewById(R.id.checkupdate_yes);
            Button cancel_exit = (Button) pp.findViewById(R.id.checkupdate_no);
            confirm_exit.setText(R.string.back_order_ok);
            cancel_exit.setText(R.string.appoinment_time_dialog_cancel);
            cancel_exit.setVisibility(View.GONE);
            alertDialog.setView(pp);
            alertDialog.setCancelable(false);//点击外部区域Dialog不消失
            alertDialog.show();
            confirm_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    //跳转到该应用的设置界面,让用户手动授权
                    JumpPermissionManagement.GoToSetting((Activity) context);
                }
            });
            cancel_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    ToastManager.showToast(context, context.getResources().getString(R.string.allow_permission_next_step));
                }
            });
        } catch (Exception e) {
            LogUtil.e(PermissionDialog.class, "intentPermission()", e);
        }
    }

    /*
    返回权限判断弹出框是否正在显示
     */
    public static boolean isDialogShow(){
        if (alertDialog != null && alertDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public static void closeDialog(){
        if (isDialogShow()) {
            alertDialog.dismiss();
        }
    }
}
