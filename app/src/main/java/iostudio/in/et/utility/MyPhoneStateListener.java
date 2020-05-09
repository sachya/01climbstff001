package iostudio.in.et.utility;

import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

public class MyPhoneStateListener extends PhoneStateListener {

    private final String TAG = MyPhoneStateListener.class.getSimpleName();

    int mSignalStrength = 0;
    SignalStrengthListener signalStrengthListener;
    TelephonyManager[] mTelephonyManager;

    public MyPhoneStateListener(TelephonyManager[] mTelephonyManager, SignalStrengthListener signalStrengthListener) {
        this.signalStrengthListener = signalStrengthListener;
        this.mTelephonyManager = mTelephonyManager;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        mSignalStrength = signalStrength.getGsmSignalStrength();
        //Log.e("mSignalStrength","before :"+mSignalStrength);
         mSignalStrength = (2 * mSignalStrength) - 113; // -> dBm
        //Log.e("mSignalStrength","after :"+mSignalStrength);
        if (signalStrengthListener != null) {
            signalStrengthListener.onSignalStrength(String.valueOf(mSignalStrength));
        }
    }

    /**
     * https://github.com/caarmen/network-monitor/blob/master/networkmonitor/src/main/java/ca/rmen/android/networkmonitor/util/NetMonSignalStrength.java
     * @return the signal strength as dBm.
     */
    public int getDbm(SignalStrength signalStrength) {
        Log.v(TAG, "getDbm " + signalStrength);
        int dBm;

        if (signalStrength.isGsm()) {
            if (getLteLevel(signalStrength) == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                dBm = getGsmDbm(signalStrength);
            } else {
                dBm = getLteDbm(signalStrength);
            }
        } else {
            int cdmaDbm = signalStrength.getCdmaDbm();
            int evdoDbm = signalStrength.getEvdoDbm();

            return evdoDbm == -120 ? cdmaDbm : cdmaDbm == -120 ? evdoDbm : cdmaDbm < evdoDbm ? cdmaDbm : evdoDbm;
        }
        Log.e(TAG," dBm:"+dBm);
        return dBm;
    }


    public interface SignalStrengthListener {
        void onSignalStrength(String mSignalStrength);
    }






    /**
     * Get the GSM signal strength as dBm
     */
    private int getGsmDbm(SignalStrength signalStrength) {
        Log.v(TAG, "getGsmDbm" + signalStrength);

        int level = signalStrength.getGsmSignalStrength();
        int asu = level == 99 ? SIGNAL_STRENGTH_NONE_OR_UNKNOWN : level;
        if (asu != SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
            return -113 + 2 * asu;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // On the Huawei P9 Lite, getGsmSignalStrength() returns 0 but getGsmDbm() returns a value.
            try {
                Method methodGetGsmDbm = SignalStrength.class.getMethod("getGsmDbm");
                return (Integer) methodGetGsmDbm.invoke(signalStrength);
            } catch (Throwable t) {
                Log.v(TAG, "Couldn't execute getGsmDbm() " + t.getMessage(), t);
                return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
            }
        } else {
            return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        }
    }

    /**
     * Get LTE as level 0..4
     */
    private int getLteLevel(SignalStrength signalStrength) {
        try{
        Log.v(TAG, "getLteLevel " + signalStrength);
        // For now there's no other way besides reflection :( The getLteLevel() method
        // in the SignalStrength class access private fields.
        // On some Samsung devices, getLteLevel() can actually return 4 (the highest signal strength) even if we're not on Lte.
        // It seems that Samsung has reimplemented getLteLevel(). So we add an extra check to make sure we only use Lte level if we're on LTE.

        if (mTelephonyManager[0].getNetworkType() != TelephonyManager.NETWORK_TYPE_LTE) {
            Log.v(TAG, "getLteLevel: returning " + SIGNAL_STRENGTH_NONE_OR_UNKNOWN + " because we're not on Lte");
            return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        }
        try {
            Method methodGetLteLevel = SignalStrength.class.getMethod("getLteLevel");
            return (Integer) methodGetLteLevel.invoke(signalStrength);
        } catch (Throwable t) {
            Log.v(TAG, "getLteLevel failed: " + t.getMessage(), t);
            return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        }}catch (Exception e){e.printStackTrace();
            return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        }
    }

    /**
     * Get LTE as dBm
     */
    private int getLteDbm(SignalStrength signalStrength) {
        // For now there's no other way besides reflection :( The getLteDbm() method
        // in the SignalStrength class returns a private field which is not
        // accessible in any public, non-hidden methods.
        try {
            Log.v(TAG, "getLteDbm " + signalStrength);
            Method methodGetLteDbm = SignalStrength.class.getMethod("getLteDbm");
            return (Integer) methodGetLteDbm.invoke(signalStrength);
        } catch (Throwable t) {
            Log.v(TAG, "getLteDbm failed: " + t.getMessage(), t);
            return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        }
    }

}