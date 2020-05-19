package iostudio.in.et.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import io.github.sac.BasicListener;
import io.github.sac.Socket;
import iostudio.in.et.fragment.HomeFragment;


/**
 * Created by vis on 08-05-2015.
 */
public class IOApp extends Application {

    public static final String TAG = IOApp.class.getSimpleName();
    private static IOApp mInstance;
    private static Context mContext;


    private Typeface primary;
    private Typeface primaryThin;
    private Typeface primaryBold;

    private Typeface secondary;
    private Typeface secondaryBold;

    Socket socket;

    String url = "ws://climbstaff.com:302/socketcluster/";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this;

        try {
            //  primary = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf");
            //  primaryThin = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Light.ttf");
            //  primaryBold = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");

            //  secondary = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
            //  secondaryBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

            initSocket();

        } catch (Exception e) {
            e.printStackTrace();
        }
        printKeyHash(getApplicationContext());
        try {

            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    ex.printStackTrace();
                    /*if (Utility.checkRelease())*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initSocket() {
        try {
            socket = new Socket(url);
            socket.setListener(new BasicListener() {

                public void onConnected(Socket socket, Map<String, List<String>> headers) {
                    System.out.println("Connected to endpoint");
                    if (HomeFragment.socketConnectionListner!=null)
                    {
                        HomeFragment.socketConnectionListner.onSocketCallback(getApplicationContext(),"connect");
                    }
                }

                public void onDisconnected(Socket socket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                    System.out.println("Disconnected from end-point");
                    if (HomeFragment.socketConnectionListner!=null)
                    {
                        HomeFragment.socketConnectionListner.onSocketCallback(getApplicationContext(),"Disconnect");
                    }
                }

                public void onConnectError(Socket socket, WebSocketException exception) {
                    System.out.println("Got connect error " + exception);
                }

                public void onSetAuthToken(String token, Socket socket) {
                    System.out.println("Token is " + token);
                }

                public void onAuthentication(Socket socket, Boolean status) {
                    if (status) {
                        System.out.println("socket is authenticated");
                    } else {
                        System.out.println("Authentication is required (optional)");
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getSocketInstance(){
        if (socket == null){
           initSocket();
        }
        return socket;
    }

    public ViewGroup getParentView(View v) {
        ViewGroup vg = null;

        if (v != null) {
            vg = (ViewGroup) v.getRootView();
        }

        return vg;

    }

    public void overrideFontsRegular(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsRegular(context, child);
                }
            }
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(secondary);
            }
            if (v instanceof EditText) {
                ((EditText) (v)).setTypeface(secondary);
            }
            if (v instanceof Button) {
                ((Button) (v)).setTypeface(secondary);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }


    public void applyTypeface(View v, Typeface f) {
        if (v != null) {
            if (v instanceof TextView) {
                ((TextView) (v)).setTypeface(f);
            }
        }
    }


    public void applyTypefacePrimary(View v) {
        if (v != null) {
            if (v instanceof TextView) {
                ((TextView) (v)).setTypeface(primary);
            }
        }
    }

    public void applyTypefacePrimaryThin(View v) {
        if (v != null) {
            if (v instanceof TextView) {
                ((TextView) (v)).setTypeface(primaryThin);
            }
        }
    }

    public void applyTypefacePrimaryBold(View v) {
        if (v != null) {
            if (v instanceof TextView) {
                ((TextView) (v)).setTypeface(primaryBold);
            }
        }
    }

    public void applyTypefaceSecondary(View v) {
        if (v != null) {
            if (v instanceof TextView) {
                ((TextView) (v)).setTypeface(secondary);
            }
        }
    }

    public void applyTypefaceSecondaryBold(View v) {
        if (v != null) {
            if (v instanceof TextView) {
                ((TextView) (v)).setTypeface(secondaryBold);
            }
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

        System.gc();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    public String getSttaicMapUrl(String lat, String lng, int zoomLevel, int width, int height) {

        //        String makerUrl = "http://icons.iconarchive.com/icons/paomedia/small-n-flat/256/map-marker-icon.png";
        //String makerUrl = "https://drive.google.com/open?id=1if5HP_-zE63wOdpMAmMFJ1lQxObOIroy";
        //String makerUrl = Utility.getURLForResource(R.drawable.pin);
        //String makerUrl = "file:///android_asset/pin_red.ico";


        String makerUrl = "https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678111-map-marker-24.png";
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng +
                "&zoom=" + zoomLevel + "&size=" + width + "x" + height +
                "&maptype=roadmap&markers=icon:" + makerUrl + "|" + lat + "," + lng;

        Log.e("makerUrl", ":" + makerUrl);
        Log.e("url", ":" + url);
        /* String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng +
                "&zoom=" + zoomLevel + "&size=" + width + "x" + height +
                "&maptype=roadmap&markers=color:red%7Clabel:C%7C" + lat + "," + lng;*/

        //"http://maps.googleapis.com/maps/api/staticmap?zoom=17&size=512x512&maptype=hybrid&markers=icon:http://cdn.sstatic.net/Sites/stackoverflow/img/favicon.ico|34.052230,-118.243680"

        return url;
    }


    public void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            Log.e("MultiDex", ": removed by me");
            // MultiDex.install(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized IOApp getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mContext;
    }


    public static String printKeyHash(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            //Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return key;
    }


    public void clearApplicationData() {

        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {

            String[] fileNames = applicationDirectory.list();

            for (String fileName : fileNames) {

                if (!fileName.equals("lib")) {

                    deleteFile(new File(applicationDirectory, fileName));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + fileName +
                            " DELETED *******************");

                }

            }

        }

    }

    public static boolean deleteFile(File file) {

        boolean deletedAll = true;

        if (file != null) {

            if (file.isDirectory()) {

                String[] children = file.list();

                for (int i = 0; i < children.length; i++) {

                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;

                }

            } else {

                deletedAll = file.delete();

            }

        }

        return deletedAll;

    }

}