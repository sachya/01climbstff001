package iostudio.in.et.retrofit.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import iostudio.in.et.retrofit.response.CommonResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by nuhbye on 07/03/16.
 */
public abstract class AppRetrofitCallback<S> implements Callback {

    public AppRetrofitCallback(Activity activity) {
    }

    public AppRetrofitCallback(Context context) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(Call call, Response response) {
        common();
        try {
            onResponseMazkara(call, response);
            Object obj = response.body();
            if (obj != null) {
                S objectResponse = (S) obj;
                onResponseAppObject(call, objectResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        try {
            Log.e("onFailure"," t :"+t.getMessage());
            if (t instanceof HttpException) {
                ResponseBody body = ((HttpException) t).response().errorBody();
                Gson gson = new Gson();
                TypeAdapter<CommonResponse> adapter = gson.getAdapter
                        (CommonResponse.class);
                try {
                    assert body != null;

                    CommonResponse errorParser = adapter.fromJson(body.string());


                    S objectResponse = (S) errorParser;
                    onResponseAppObject(call, objectResponse);

                    Log.i("Exception ", "Error:" + errorParser.getMessage());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!t.getMessage().equalsIgnoreCase("Canceled"))
                common();
        } catch (Exception e) {
            common();
        }
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     */
    protected abstract void onResponseMazkara(Call call, Response response);

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     */
    protected abstract void onResponseAppObject(Call call, S response);

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    //protected abstract void onFailureVoidz(Call call, Throwable t);

    /**
     * Invoked everyTime
     */
    protected abstract void common();
}
