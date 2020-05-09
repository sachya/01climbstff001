package iostudio.in.et.retrofit.api;


import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import iostudio.in.et.retrofit.AddCookiesInterceptor;
import iostudio.in.et.retrofit.ReceivedCookiesInterceptor;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.Utility;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient uniqInstance;
    private final String URL_SANDBOX = Constant.API.BASEURL_API;
    private final String URL_SANDBOXTest = Constant.API.BASEURL_API;
    private KApp apiKApp, apiKAppTest;

    public static synchronized ApiClient getInstance() {
        if (uniqInstance == null) {
            uniqInstance = new ApiClient();
        }
        return uniqInstance;
    }

    private void ApiClient() {
        try {


            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


                /**
                 * retrofit 2.0 Code
                 */
                {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    // set your desired log level
                    if (!Utility.checkRelease()) {
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    }
                    //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient httpClient = new OkHttpClient.Builder()
                            .readTimeout(180, TimeUnit.SECONDS)
                            .connectTimeout(180, TimeUnit.SECONDS)
                            //.addInterceptor(new AddCookiesInterceptor(IOApp.getInstance()))
                            //.addInterceptor(new ReceivedCookiesInterceptor(IOApp.getInstance()))
                            .addInterceptor(logging)
                            .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return true;
                                }
                            })
                            .build();

                    // <-- this is the important line!
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL_SANDBOX)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient)
                            .build();

                    apiKApp = retrofit.create(KApp.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KApp getApi() {
        if (uniqInstance == null) {
            getInstance();
        }
        uniqInstance.ApiClient();
        return apiKApp;
    }


    //for test
    public KApp getApiTest() {
        if (uniqInstance == null) {
            getInstance();
        }
        uniqInstance.ApiClientTest();

        return apiKAppTest;
    }

    private void ApiClientTest() {
        try {

            /**
             * retrofit 2.0 Code
             */
            {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                //logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build();

                // <-- this is the important line!
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL_SANDBOXTest)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient)
                        .build();

                apiKAppTest = retrofit.create(KApp.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
