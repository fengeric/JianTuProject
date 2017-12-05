package txt.jiantu.create.com.myjiantuproject.util;

import android.text.TextUtils;

public class Util {
	public static final int REQUEST_PERMISSION_CODE = 0x135;//跳转到系统权限更改页面的标识

	/**
	 * @param text
	 *            判断字符串是否为空，true为空，false不为空
	 * @return
	 */
	public static boolean isTextNull(String text) {
		boolean flag = true;
		try {
			if ((!TextUtils.isEmpty(text)) && (!text.equals("null"))) {
				flag = false;
				return flag;
			}
		} catch (Exception e) {
			LogUtil.v(Util.class, "isTextNull---" + ":" + e.toString());
		}
		return flag;
	}
}
