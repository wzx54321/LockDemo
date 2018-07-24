package com.lib.lock.gesture.content;


import com.lib.lock.gesture.utils.ContextUtils;

/**
 * Description :SharedPreferences
 * Created by xin on 2017/5/16 0016.
 */

public class SPManager implements SharedPreferencesKeys {


    private DataKeeper mDk;
    private static SPManager spManager;

    private SPManager() {
        mDk = new DataKeeper(ContextUtils.getContext(), spFileName);
    }

    public static SPManager getInstance() {
        if (spManager == null) {
            synchronized (SPManager.class) {
                if (spManager == null) {
                    spManager = new SPManager();
                }
            }

        }

        return spManager;
    }






    /**
     * 存手势密码
     *
     * @param encryptPwd
     * @return
     */
    public void   putPatternPSW(String encryptPwd) {

         mDk.put(KEY_GESTURE_PWD, encryptPwd).commit();

    }


    /**
     * 手势密码
     */
    public String getPatternPSW() {
        return mDk.get(KEY_GESTURE_PWD, "");
    }


    /**
     * 指纹密码
     *
     * @return
     */
    public void setHasFingerPrint(boolean isSet) {
          mDk.put(KEY_HAS_FINGERPRINT, isSet).apply();
    }

    /**
     * 指纹密码
     *
     * @return
     */
    public boolean getHasFingerPrint() {
        return mDk.get(KEY_HAS_FINGERPRINT, false);
    }
}
