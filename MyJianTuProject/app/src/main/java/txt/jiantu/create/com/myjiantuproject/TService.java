package txt.jiantu.create.com.myjiantuproject;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import txt.jiantu.create.com.myjiantuproject.util.LogUtil;

/**
 * Created by Administrator on 2017/12/8.
 */

public class TService extends Service {
    public TService() {
        super();
        LogUtil.v(getClass(), "TService---" + ":"  );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.v(getClass(), "onCreate---" + ":"  );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.v(getClass(), "onStartCommand---" + ":" );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.v(getClass(), "onDestroy---" + ":"  );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.v(getClass(), "onBind---" + ":"  );
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.v(getClass(), "onUnbind---" + ":" );
        return super.onUnbind(intent);
    }
}
