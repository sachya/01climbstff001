package iostudio.in.et.retrofit;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import iostudio.in.et.pref.IOPref;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by anantshah on 04/05/18.
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    } // AddCookiesInterceptor()

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
           /* HashSet<String> cookies = (HashSet<String>) PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getStringSet("PREF_COOKIES", new HashSet<String>());
*/
            HashSet<String> cookies = IOPref
                    .getInstance().getStringSet(context,"PREF_COOKIES",new HashSet<String>());

            for (String header : originalResponse.headers("Set-Cookie")) {
                Log.e("header", ":" + header);
                cookies.add(header);
            }

           /* SharedPreferences.Editor memes =
                    PreferenceManager.getDefaultSharedPreferences(context).edit();
            Log.e("cookies", "received:" + cookies.toString());
            memes.putStringSet("PREF_COOKIES", cookies).apply();
            memes.commit();*/
            IOPref.getInstance().saveStringSet(context,"PREF_COOKIES",cookies);
        }

        return originalResponse;
    }
}