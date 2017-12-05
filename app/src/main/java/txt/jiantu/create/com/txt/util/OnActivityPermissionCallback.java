package txt.jiantu.create.com.txt.util;

import android.support.annotation.NonNull;

/**
 * 用于权限请求返回结果时的接口
 */
public interface OnActivityPermissionCallback {

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onActivityForResult(int requestCode);
}
