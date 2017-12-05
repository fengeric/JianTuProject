package txt.jiantu.create.com.myjiantuproject.util;

import android.support.annotation.NonNull;

/**
 * 权限判断的返回接口
 */
public interface OnPermissionCallback {

    /*
    允许的
     */
    void onPermissionGranted(@NonNull String permissionName);

//    /*
//    拒绝的
//     */
//    void onPermissionDeclined(@NonNull String[] permissionName);
//
//    /*
//
//     */
//    void onPermissionPreGranted(@NonNull String permissionsName);
//
//    /*
//    需要询问的
//     */
//    void onPermissionNeedExplanation(@NonNull String permissionName);
//
//    /*
//    勾选不再提醒后拒绝的
//     */
//    void onPermissionReallyDeclined(@NonNull String permissionName);

    /*
    不需要权限判断
     */
    void onNoPermissionNeeded();
}
