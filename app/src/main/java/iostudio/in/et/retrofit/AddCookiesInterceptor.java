package iostudio.in.et.retrofit;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import iostudio.in.et.pref.IOPref;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anantshah on 04/05/18.
 */

public class AddCookiesInterceptor implements Interceptor {
    // We're storing our stuff in a database made just for cookies called PREF_COOKIES.
    // I reccomend you do this, and don't change this default value.
    private Context context;

    public AddCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        HashSet<String> preferences = IOPref
                .getInstance().getStringSet(context,"PREF_COOKIES",new HashSet<String>());


       /*  HashSet<String> preferences = (HashSet<String>) PreferenceManager
                .getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<String>());
       */

        //HashSet<String> cookies = IOPref.getInstance().getStringSet(context,"PREF_COOKIES",new HashSet<String>());




        // Use the following if you need everything in one line.
        // Some APIs die if you do it differently.
        /*String cookiestring = "";
        for (String cookie : preferences) {
            String[] parser = cookie.split(";");
            cookiestring = cookiestring + parser[0] + "; ";
        }
        builder.addHeader("Cookie", cookiestring);
        */

        for (String cookie : preferences) {
            Log.e("cookies","add:"+cookie);
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}