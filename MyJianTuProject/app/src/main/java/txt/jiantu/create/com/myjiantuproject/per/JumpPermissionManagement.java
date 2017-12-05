package txt.jiantu.create.com.myjiantuproject.per;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import txt.jiantu.create.com.myjiantuproject.util.CommonUtils;
import txt.jiantu.create.com.myjiantuproject.util.LogUtil;
import txt.jiantu.create.com.myjiantuproject.util.Util;


/**
 * 不同手机的权限设置界面
 * 这里所写的政对小米手机测试过，其它型号的手机待测
 * Created by okkuaixiu on 2017/9/21.
 */

public class JumpPermissionManagement {
    /**
     * Build.MANUFACTURER
     */
    private static final String MANUFACTURER_HUAWEI = "huawei";//华为
    private static final String MANUFACTURER_MEIZU = "meizu";//魅族
    public static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    private static final String MANUFACTURER_SONY = "sony";//索尼
    private static final String MANUFACTURER_OPPO = "oppo";
    private static final String MANUFACTURER_LG = "lg";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_LETV = "letv";//乐视
    private static final String MANUFACTURER_ZTE = "zte";//中兴
    private static final String MANUFACTURER_YULONG = "yulong";//酷派
    private static final String MANUFACTURER_LENOVO = "lenovo";//联想
    
    public static final String APPLICATION_ID= "com.feng.dynamicperimission.check";// 包名

    /**
     * 此函数可以自己定义
     * @param activity
     */
    public static void GoToSetting(Activity activity){
        switch (Build.MANUFACTURER.toLowerCase()){
            case MANUFACTURER_HUAWEI:
                Huawei(activity);
                break;
            case MANUFACTURER_MEIZU:
                Meizu(activity);
                break;
            case MANUFACTURER_XIAOMI:
                Xiaomi(activity);
                break;
            case MANUFACTURER_SONY:
                Sony(activity);
                break;
            case MANUFACTURER_OPPO:
                OPPO(activity);
                break;
            case MANUFACTURER_LG:
                LG(activity);
                break;
            case MANUFACTURER_LETV:
                Letv(activity);
                break;
            default:
                ApplicationInfo(activity);
                LogUtil.e(JumpPermissionManagement.class, "目前暂不支持此系统");
                break;
        }
    }

    public static void Huawei(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", APPLICATION_ID);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        intent.setComponent(comp);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
    }

    public static void Meizu(Activity activity) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", APPLICATION_ID);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
    }

    public static void Xiaomi(Activity activity) {
        String rom = MIUIRomVersion();
        if (!Util.isTextNull(rom)) {
            switch (rom){
                case "V5":
                    Uri packageURI = Uri.parse("package:" + activity.getApplicationInfo().packageName);
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
                    break;
                case "V6":
                case "V7":
                    Intent intentlocal = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intentlocal.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    intentlocal.putExtra("extra_pkgname", activity.getPackageName());
                    CommonUtils.launchActivityForResult(activity, intentlocal, Util.REQUEST_PERMISSION_CODE);
                    break;
                case "V8":
                    Intent intentSett = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intentSett.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intentSett.putExtra("extra_pkgname", activity.getPackageName());
//                    activity.startActivity(intentSett);
                    CommonUtils.launchActivityForResult(activity, intentSett, Util.REQUEST_PERMISSION_CODE);
                    break;
                default:
                    ApplicationInfo(activity);
                    break;
            }
        } else {
            ApplicationInfo(activity);
        }
    }
    public static String MIUIRomVersion(){
        String property = getSystemProperty("ro.miui.ui.version.name");
        return property;
    }
    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    public static void Sony(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", APPLICATION_ID);
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
    }

    public static void OPPO(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", APPLICATION_ID);
        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        intent.setComponent(comp);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);

        //OPPO R9 colosOs v3.0.0 android5.1
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("pkg_name", context.getPackageName());
//        intent.putExtra("app_name", context.getString(R.string.app_name));
//        intent.putExtra("class_name", "com.welab.notificationdemo.MainActivity");
//        ComponentName comp = new ComponentName("com.coloros.notificationmanager", "com.coloros" +
//                ".notificationmanager.AppDetailPreferenceActivity");
//        intent.setComponent(comp);
    }

    public static void LG(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", APPLICATION_ID);
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
    }

    public static void Letv(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", APPLICATION_ID);
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
    }

    /**
     * 只能打开到自带安全软件
     * @param activity
     */
    public static void _360(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", APPLICATION_ID);
        ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        intent.setComponent(comp);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
    }

    /**
     * 应用信息界面
     * @param activity
     */
    public static void ApplicationInfo(Activity activity){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        CommonUtils.launchActivityForResult(activity, localIntent, Util.REQUEST_PERMISSION_CODE);
    }

    /**
     * 系统设置界面
     * @param activity
     */
    public static void SystemConfig(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        CommonUtils.launchActivityForResult(activity, intent, Util.REQUEST_PERMISSION_CODE);
    }
}