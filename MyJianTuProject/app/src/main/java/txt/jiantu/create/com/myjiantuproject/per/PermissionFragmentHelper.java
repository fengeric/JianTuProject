package txt.jiantu.create.com.myjiantuproject.per;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.annotation.NonNull;

import txt.jiantu.create.com.myjiantuproject.R;
import txt.jiantu.create.com.myjiantuproject.util.LogUtil;
import txt.jiantu.create.com.myjiantuproject.util.OnActivityPermissionCallback;
import txt.jiantu.create.com.myjiantuproject.util.OnPermissionCallback;
import txt.jiantu.create.com.myjiantuproject.util.Util;


/**
 * 权限请求中fragment类型的辅助
 */
public class PermissionFragmentHelper implements OnActivityPermissionCallback {
    private static final int REQUEST_PERMISSIONS = 1;

    @NonNull
    private final OnPermissionCallback permissionCallback;
    @NonNull
    private final Fragment context;
    private String permiIntentWord;//跳转到手机权限管理页面时对应的权限名称

    private PermissionFragmentHelper(@NonNull Fragment context) {
        this.context = context;
        if (context instanceof OnPermissionCallback) {
            this.permissionCallback = (OnPermissionCallback) context;
        } else {
            throw new IllegalArgumentException("Fragment must implement (OnPermissionCallback)");
        }
    }

    private PermissionFragmentHelper(@NonNull Fragment context, @NonNull OnPermissionCallback permissionCallback) {
        this.context = context;
        this.permissionCallback = permissionCallback;
    }

    @NonNull
    public static PermissionFragmentHelper getInstance(@NonNull Fragment context) {
        return new PermissionFragmentHelper(context);
    }

    @NonNull
    public static PermissionFragmentHelper getInstance(@NonNull Fragment context, @NonNull OnPermissionCallback permissionCallback) {
        return new PermissionFragmentHelper(context, permissionCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS && permissions.length > 0 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.MANUFACTURER.equals(JumpPermissionManagement.MANUFACTURER_XIAOMI)) {
                    //针对小米机型
                    if (!hasSelfPermissionForXiaomi(context.getContext(), permissions[0])) {
                        permiIntentWord = permissions[0];
                        PermissionDialog.intentApplicationPermi(context.getContext(), permiIntentWord);
                    } else {
                        permissionCallback.onPermissionGranted(permissions[0]);
                    }
                } else {
                    permissionCallback.onPermissionGranted(permissions[0]);
                }
            } else {
                if (context.getActivity().checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                    if (context.shouldShowRequestPermissionRationale(permissions[0])) {
                        //没有勾选不再询问
                        context.requestPermissions(new String[]{permissions[0]}, REQUEST_PERMISSIONS);
                    } else {
                        //勾选了不再询问
                        permiIntentWord = permissions[0];
                        PermissionDialog.intentApplicationPermi(context.getContext(), permiIntentWord);
                    }
                } else {
                    permissionCallback.onPermissionGranted(permissions[0]);
                }
            }
        }
    }

    /**
     * used only for {@link Manifest.permission#SYSTEM_ALERT_WINDOW}
     */
    @Override
    public void onActivityForResult(int requestCode) {
        if (Util.REQUEST_PERMISSION_CODE == requestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Util.isTextNull(permiIntentWord)) {
                    handleSingle(permiIntentWord);
                }
            } else {
                permissionCallback.onNoPermissionNeeded();
            }
        }
    }

    /**
     * @param permissionName
     *         (it can be one of these types (String), (String[])
     */
    @NonNull
    public PermissionFragmentHelper request(@NonNull Object permissionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionDialog.closeDialog();
            if (permissionName instanceof String) {
                handleSingle((String) permissionName);
            } else if (permissionName instanceof String[]) {
                handleMulti((String[]) permissionName);
            } else {
                throw new IllegalArgumentException("Permissions can only be one of these types (String) or (String[])" +
                        ". given type is " + permissionName.getClass().getSimpleName());
            }
        } else {
            permissionCallback.onNoPermissionNeeded();
        }
        return this;
    }

    /**
     * internal usage.
     */
    private void handleSingle(@NonNull String permissionName) {
        if (permissionExists(permissionName)) {
            if (Build.MANUFACTURER.equals(JumpPermissionManagement.MANUFACTURER_XIAOMI)) {
                if (hasSelfPermissionForXiaomi(context.getContext(), permissionName)) {
                    permissionCallback.onPermissionGranted(permissionName);
                } else {
                    context.requestPermissions(new String[]{permissionName}, REQUEST_PERMISSIONS);
                }
            } else {
                if (context.getActivity().checkSelfPermission(permissionName) != PackageManager.PERMISSION_GRANTED) {
                    /**
                     * shouldShowRequestPermissionRationale()
                     * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。没有勾选返回true
                     * 如果设备规范禁止应用具有该权限，此方法也会返回 false。
                     */
                    if (context.shouldShowRequestPermissionRationale(permissionName)) {
                        //之前点击了“不再询问”，无法再次弹出权限申请框。则弹出dialog
                        permiIntentWord = permissionName;
                        PermissionDialog.intentApplicationPermi(context.getContext(), permiIntentWord);
                    } else {
                        //如果没勾选“不再询问”，向用户发起权限请求
                        context.requestPermissions(new String[]{permissionName}, REQUEST_PERMISSIONS);
                    }
                } else {
                    permissionCallback.onPermissionGranted(permissionName);
                }
            }
        }
    }

    //小米机型获取权限
    private boolean hasSelfPermissionForXiaomi(Context context, String permission) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        String permissionToOp = appOpsManager.permissionToOp(permission);
        if (permissionToOp == null) {
            // in case of normal permissions(e.g. INTERNET)
            return true;
        }
        int noteOp = appOpsManager.noteOp(permissionToOp, Process.myUid(), context.getPackageName());
        return noteOp == appOpsManager.MODE_ALLOWED && context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * internal usage.
     */
    private void handleMulti(@NonNull String[] permissionNames) {
        if (permissionNames != null && permissionNames.length > 0) {
            for (String permiss:
                    permissionNames) {
                //开始动态请求被拒绝的权限
                handleSingle(permiss);
            }
        }
    }

    /**
     * @return true if permission exists in the manifest, false otherwise.
     */
    public boolean permissionExists(@NonNull String permissionName) {
        try {
            Context context = this.context.getActivity();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (packageInfo.requestedPermissions != null) {
                for (String p : packageInfo.requestedPermissions) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
            LogUtil.e(PermissionFragmentHelper.class, context.getResources().getString(R.string.add_permission_manifest));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
