package iostudio.in.et.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;
import iostudio.in.et.BuildConfig;
import iostudio.in.et.R;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.response.ContractorData;

import static android.content.Context.VIBRATOR_SERVICE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by vis on 17-05-2015.
 */
public class Utility {

    public static String extractYouTubeId(String ytUrl) {
        String vId = null;

        String pattern =
                "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern
                .matcher(ytUrl); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            vId = matcher.group();
        }
        return vId;
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("dummy.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static boolean isTimeBetweenTwoTimeDates(String startTime, String endTime) {
        try {
            Date openDateTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(startTime);
            Date closeDateTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(endTime);
            Date currentTime = new Date();
            if (openDateTime.before(currentTime) &&
                    closeDateTime.after(currentTime)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public static boolean isTimeBetweenTwoTime(String startTime, String endTime) {

        try {

            //start time
            Date time1 = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);


            //end time
            Date time2 = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);


            //current time


            Date date = new SimpleDateFormat("HH:mm", Locale.getDefault())
                    .parse(UtilDate.getCurrentTime());
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(date);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();


            /*Log.e("startTime", ":" + startTime + " endTime:" + endTime + " currentTime:" +
                    calendar3.getTime());*/

            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getShopName(Context context) {
        return IOPref.getInstance().getString(context, IOPref.PreferenceKey.shopName, "");
    }

    /**
     * Reads a Map from a Parcel that was stored using a String array and a Bundle.
     *
     * @param in   the Parcel to retrieve the map from
     * @param type the class used for the value objects in the map, equivalent to V.class before
     *             type erasure
     * @return a map containing the items retrieved from the parcel
     */
    public static <V extends Parcelable> Map<String, V> readMap(Parcel in,
                                                                Class<? extends V> type) {

        Map<String, V> map = new HashMap<String, V>();
        if (in != null) {
            String[] keys = in.createStringArray();
            Bundle bundle = in.readBundle(type.getClassLoader());
            for (String key : keys)
                map.put(key, type.cast(bundle.getParcelable(key)));
        }
        return map;
    }


    /**
     * Reads into an existing Map from a Parcel that was stored using a String array and a Bundle.
     *
     * @param map  the Map<String,V> that will receive the items from the parcel
     * @param in   the Parcel to retrieve the map from
     * @param type the class used for the value objects in the map, equivalent to V.class before
     *             type erasure
     */
    public static <V extends Parcelable> void readMap(Map<String, V> map, Parcel in,
                                                      Class<V> type) {

        if (map != null) {
            map.clear();
            if (in != null) {
                String[] keys = in.createStringArray();
                Bundle bundle = in.readBundle(type.getClassLoader());
                for (String key : keys)
                    map.put(key, type.cast(bundle.getParcelable(key)));
            }
        }
    }


    /**
     * Writes a Map to a Parcel using a String array and a Bundle.
     *
     * @param map the Map<String,V> to store in the parcel
     * @param out the Parcel to store the map in
     */
    public static void writeMap(Map<String, ? extends Parcelable> map, Parcel out) {

        if (map != null && map.size() > 0) {
        /*
        Set<String> keySet = map.keySet();
        Bundle b = new Bundle();
        for(String key : keySet)
            b.putParcelable(key, map.get(key));
        String[] array = keySet.toArray(new String[keySet.size()]);
        out.writeStringArray(array);
        out.writeBundle(b);
        /*/
            // alternative using an entrySet, keeping output data format the same
            // (if you don't need to preserve the data format, you might prefer to just write the key-value pairs directly to the parcel)
            Bundle bundle = new Bundle();
            for (Map.Entry<String, ? extends Parcelable> entry : map.entrySet()) {
                bundle.putParcelable(entry.getKey(), entry.getValue());
            }

            final Set<String> keySet = map.keySet();
            final String[] array = keySet.toArray(new String[keySet.size()]);
            out.writeStringArray(array);
            out.writeBundle(bundle);
            /**/
        } else {
            //String[] array = Collections.<String>emptySet().toArray(new String[0]);
            // you can use a static instance of String[0] here instead
            out.writeStringArray(new String[0]);
            out.writeBundle(Bundle.EMPTY);
        }
    }


    public static String getCSVFromArrayListNewLine(ArrayList<String> serviceIDCSVList) {
        String ids = "";
        ids = TextUtils.join("\n", serviceIDCSVList);
        return ids;
    }

    public static String getCSVFromArrayList(ArrayList<String> serviceIDCSVList) {
        String ids = "";
        ids = TextUtils.join(",", serviceIDCSVList);
        return ids;
    }

    public static String getCSVFromContractorArrayList(ArrayList<ContractorData> dataArrayList) {
        String ids = "";
        ArrayList<String> serviceIDCSVList = new ArrayList<>();
        try {
            for (ContractorData data : dataArrayList) {
                serviceIDCSVList.add(data.getContractor_id());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ids = TextUtils.join(",", serviceIDCSVList);
        return ids;
    }


    public static String getDayOfWeek() {
        String day = null;

        try {

            Date now = new Date();

            SimpleDateFormat simpleDateformat =
                    new SimpleDateFormat("EE",
                            Locale.getDefault()); // the day of the week abbreviated
            day = simpleDateformat.format(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }


    public static String getTimeStartToEnd(String startTime, String endTime) {
        return Utility.getFromTimeToTimeOnly(startTime)
                + " to "
                + Utility.getFromTimeToTimeOnly(endTime);
    }

    public static String getUTCTimeStartToEnd(String startTime, String endTime) {
        return Utility.getUTCFromTimeToTimeOnly(startTime)
                + " to "
                + Utility.getUTCFromTimeToTimeOnly(endTime);
    }

    public static String getUTCFromTimeToTimeOnly(String originalDate) {
        String currentDate = "";

        try {
            SimpleDateFormat formatter, FORMATTER;
            formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = null;
            try {
                date = formatter.parse(originalDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            FORMATTER = new SimpleDateFormat("hh:mm a", Locale.getDefault());//12 hr
            FORMATTER.setTimeZone(TimeZone.getDefault());
            currentDate = FORMATTER.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentDate;
    }

    public static String getFromTimeToTimeOnly(String originalDate) {
        String currentDate = "";

        try {
            SimpleDateFormat formatter, FORMATTER;
            formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = null;
            try {
                date = formatter.parse(originalDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            FORMATTER = new SimpleDateFormat("hh:mm a", Locale.getDefault());//12 hr
            //FORMATTER.setTimeZone(TimeZone.getDefault());
            currentDate = FORMATTER.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentDate;
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media
                .insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap =
                    Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                            Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static String getGeneralPref(Context context) {
        return IOPref.getInstance()
                .getString(context, IOPref.PreferenceKey.generalPreferences, "");
    }

    public static String getGroupLabelList(Context context) {
        return IOPref.getInstance()
                .getString(context, IOPref.PreferenceKey.groupPreferences, "");
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    public static void addImageInEditText(Drawable drawable) {
        //https://stackoverflow.com/questions/7818438/how-to-insert-image-to-a-edittext
        drawable.setBounds(0, 0, 65, 65);
        //edit_text_frequent_flyer.getText().insert(0, " ");
        //edit_text_frequent_flyer.getText().insert(1, " ");

        SpannableStringBuilder builder =
                new SpannableStringBuilder();
        builder.setSpan(new ImageSpan(drawable), 0, 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //edit_text_frequent_flyer.setText(builder);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float convertSpToPixels(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }


    public static ArrayList<String> convertToArrayListFromStringOfArray(String[] arrayOfString) {

        return new ArrayList<String>(Arrays.asList(arrayOfString));
    }

    public static String getFacebookProfileUrl(String facebookID) {
        return "https://graph.facebook.com/" + facebookID + "/picture?type=large";
    }

    public static String getUserName(Context activity) {
        return IOPref.getInstance()
                .getString(activity, IOPref.PreferenceKey.userName, "");
    }

    public static String getFacebookUser(Context activity) {
        return IOPref.getInstance()
                .getString(activity, IOPref.PreferenceKey.facebookUser, "");
    }

    public static void saveFacebookUser(Context activity, String data) {
        IOPref.getInstance()
                .saveString(activity, IOPref.PreferenceKey.facebookUser, data);
    }

    public static void saveFacebookUserLoginResult(Context activity, String data) {
        IOPref.getInstance()
                .saveString(activity, IOPref.PreferenceKey.facebookUserLogin, data);
    }

    public static String getFacebookUserLoginResult(Context activity) {
        return IOPref.getInstance()
                .getString(activity, IOPref.PreferenceKey.facebookUserLogin, "");
    }

    public static String replaceEmoji(CharSequence source) {

        String notAllowedCharactersRegex =
                "[^a-zA-Z0-9@#\\$%\\&\\-\\+\\(\\)\\*;:!\\?\\~`£\\{\\}\\[\\]=\\.,_/\\\\\\s'\\\"<>\\^\\|÷×]";
        return source.toString()
                .replaceAll(notAllowedCharactersRegex, "");
    }

    public static String getUserEmail(Context activity) {
        return IOPref.getInstance()
                .getString(activity, IOPref.PreferenceKey.userEmail, "");
    }

    public static void promptSpeechInput(Activity activity, int requestCode,
                                         String promtMsg) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, promtMsg);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException a) {
        }

    }


    public static String getUserProfilePhoto(Context activity) {
        return IOPref.getInstance()
                .getString(activity, IOPref.PreferenceKey.userPhotoURL, "");
    }

    public static String getShopPhone(Context context) {
        return IOPref.getInstance()
                .getString(context, IOPref.PreferenceKey.shopPhone, "");
    }

    public static boolean calcuateLatLng(Activity baseActivity, double lastSavedLat,
                                         double lastSavedLng, double newLat, double newLng) {
        Double travelledDistance = distance(lastSavedLat, lastSavedLng, newLat, newLng);

        //Log.e("travelledDistance ", "" + travelledDistance.intValue());

        if (travelledDistance > 100)
            return true;
        return false;
    }

    public static void shareText(final Context currActivity, final String message) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "" + message);
            sendIntent.setType("text/plain");
            currActivity.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
                    true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
        } else {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    public static int getRandomPlaceHolder(int position) {
        int selectedPlaceHolder = position % 4;
        ArrayList<Integer> list = new ArrayList<Integer>();
        /*list.add(R.drawable.offers1);
        list.add(R.drawable.offers2);
        list.add(R.drawable.offers3);
        list.add(R.drawable.offers4);
        */
        int drawableValueAsInteger = list.get(selectedPlaceHolder);
        return drawableValueAsInteger;
    }


    public static String getFormatedURL(String urlToFromate) {


        URL url = null;
        URI uri = null;
        String returnFromatedURL = "";
        try {
            if (urlToFromate != null && urlToFromate.length() > 1) {
                url = new URL(urlToFromate);
                uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
                        url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                returnFromatedURL = url.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnFromatedURL;
    }

    public static ArrayList<Object> getUniqueValuesArrayListSearch(
            List<Object> List) {
        ArrayList<Object> arrayList = new ArrayList<>();
        try {
            //need to add equals and hashCode inside the SearchDictionary class please check
            //http://stackoverflow.com/questions/6715933/remove-duplicate-objects-from-a-arraylist-in-android

            Set<Object> unique = new LinkedHashSet<Object>(List);
            arrayList = new ArrayList<Object>(unique);
            //Log.e("recentSearched set", " is it worked:" + arrayList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        try {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null)
                return;

            int desiredWidth = View.MeasureSpec
                    .makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);
                if (i == 0)
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height =
                    totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            //Log.e(" params.height", "after:" + params.height);
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure
                        .getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public static String convertToDateStringWithsdf(String unformateddate, SimpleDateFormat sdf) {
        // TODO Auto-generated method stub
        Date date = null;
        String dateStr = unformateddate;
        try {

            date = sdf.parse(unformateddate);
            dateStr = new SimpleDateFormat("dd MMM yyyy").format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return dateStr;
    }


    public static boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }


    public static Date String2Date(String datestr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(datestr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String setTimeAfter(String dateCreatedString) {

        Calendar now = Calendar.getInstance();
        Calendar day = Calendar.getInstance();
        try {
            day.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateCreatedString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String endDateFormated = "";

        Date nowDate = now.getTime();
        Date lastDate = day.getTime();

        endDateFormated = getDateDiffString(nowDate, lastDate);
        //Log.e("endDateFormated", "nowDate :" + nowDate + "lastDate :" + lastDate + "after:" + endDateFormated);
        return endDateFormated;

    }

    public static String getDateWithTh(String dateCreatedString) {
        String endDateFormated = "";

        Calendar day = Calendar.getInstance();
        try {
            day.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateCreatedString));

            String dayNumberSuffix = getDayNumberSuffix(day.get(Calendar.DAY_OF_MONTH));
            DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMM");
            endDateFormated = "on" + dateFormat.format(day.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return endDateFormated;

    }

    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getDateDiffString(Date lastDate, Date dateTwo) {
        long timeOne = lastDate.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;


       /* long year = delta / 365;
        long rest = delta % 365;
        long month = rest / 30;
        rest = rest % 30;
        long weeks = rest / 7;
        long days = rest % 7;

*/
        if (delta > 0) {
            if (delta <= 29) {
                return delta + " days left";
            } else if (delta >= 30 && delta <= 59) {
                return "1 Month left";
            } else if (delta >= 60 && delta <= 89) {
                return "2 Month left";
            } /*else if (delta >= 90 && delta <= 119) {
                return "3 Month left";
            }*/ else {
                String monthFormat = (String) android.text.format.DateFormat.format("MMM", dateTwo);
                String yearFormat =
                        (String) android.text.format.DateFormat.format("yyyyy", dateTwo);
                String dayFormat =
                        (String) android.text.format.DateFormat.format("dd", dateTwo); //20
                String time = null;

                return dayFormat + "-" + monthFormat + "-" + yearFormat;
            }
        } else {
            delta *= -1;
            if (delta == 0) {
                return "Today";
            } else if (delta == 1) {
                return "Yesterday";
            } else {
                String monthFormat = (String) android.text.format.DateFormat.format("MMM", dateTwo);
                String yearFormat =
                        (String) android.text.format.DateFormat.format("yyyyy", dateTwo);
                String dayFormat =
                        (String) android.text.format.DateFormat.format("dd", dateTwo); //20
                String time = null;

                return dayFormat + "-" + monthFormat + "-" + yearFormat;
            }
        }


    }


    public static int getRatingBackground(Activity activity, String name) {
        String ratingImage = "round_corner_rating_background5";
        try {
            if (Float.parseFloat(name.substring(0, 3)) > 0 &&
                    Float.parseFloat(name.substring(0, 3)) < 1) {
                ratingImage = "round_corner_rating_background" + "1";

            } else {
                String ratingText = name.substring(0, 1);
                ratingImage = "round_corner_rating_background" + ratingText;
            }
            //Log.e("ratingImage", ":" + ratingImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int drawableValueAsInteger = activity.getResources()
                .getIdentifier(ratingImage, "drawable", activity.getPackageName());

        return drawableValueAsInteger;
    }

    public static int getVolumeBackground(Activity activity, int percent) {
        String ratingImage = "sound4";
        try {
            if (percent == 1) {
                ratingImage = "sound" + "2";
            } else if (percent == 2) {
                ratingImage = "sound" + "3";
            } else if (percent == 3) {
                ratingImage = "sound" + "4";
            } else if (percent == 4) {
                ratingImage = "sound" + "1";
            }
            //Log.e("ratingImage", ":" + ratingImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int drawableValueAsInteger = activity.getResources()
                .getIdentifier(ratingImage, "drawable", activity.getPackageName());

        return drawableValueAsInteger;
    }


    public static ArrayList<String> getUniqueValuesArrayList(ArrayList<String> arrayList) {
        try {
            arrayList.removeAll(Collections.singleton(null));
            arrayList.removeAll(Collections.singleton(""));
            //arrayList.removeAll(Arrays.asList("", null)); // will also work
            Set<String> hs = new HashSet<>();
            hs.addAll(arrayList);
            arrayList.clear();
            arrayList.addAll(hs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    public static Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        //Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        //Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        // haversine great circle_with_shadow distance approximation, returns meters


        float[] dist = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, dist);
        //Log.d("*******dist", ":" + dist[0]);
        return (dist[0]);
        //
        //        double theta = lon1 - lon2;
        //        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
        //                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
        //                * Math.cos(deg2rad(theta));
        //        dist = Math.acos(dist);
        //        dist = rad2deg(dist);
        //        dist = dist * 60; // 60 nautical miles per degree of seperation
        //        dist = dist * 1852; // 1852 meters per nautical mile
        //        return (dist);
        //
    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    //distance end


    //start convert to string
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    //end convert to string


    public static void openURL(Context activity, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static Drawable changeRoundedCornerColorDirect(Activity activity, Drawable background,
                                                          String normalColorString, int stroke) {
        long startTime = System.currentTimeMillis();

        int normalColor = activity.getResources()
                .getIdentifier(normalColorString, "color", activity.getPackageName());


       /* if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, R.color.menu_color4));
        } else if (background instanceof StateListDrawable) {
            DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) background.getConstantState();
            Drawable[] children = drawableContainerState.getChildren();

            GradientDrawable selectedDrawable = null;
            GradientDrawable unselectedDrawable = null;

            if (children[0] instanceof LayerDrawable) {

                if (children[0] != null) {
                    LayerDrawable selectedItem = (LayerDrawable) children[0];
                    selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
                }
            } else if (children[0] instanceof GradientDrawable) {
                if (children[0] != null)
                    selectedDrawable = (GradientDrawable) children[0];
            }

            if (selectedDrawable != null) {
                Log.e("selectedDrawable", "**************");
                selectedDrawable.setStroke(stroke, ContextCompat.getColor(activity, normalColor));
                selectedDrawable.setColor(ContextCompat.getColor(activity, normalColor));
            }
        } else*/
        if (background instanceof GradientDrawable) {
            //Log.e("", "********** gradient *&***********");
            ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, normalColor));
        } /*else if (background instanceof ColorDrawable) {

            ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, normalColor));
        }*/

        /*
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        long endTime = System.currentTimeMillis() - startTime;
        // Log.e("Total Time", "for tag change left background:" + endTime + " ms");

        return background;
    }

    public static Drawable changeRoundedCornerColorPressed(Activity activity, View view,
                                                           Drawable background, int normalColor, int pressedColor, int stroke) {
        long startTime = System.currentTimeMillis();

       /* if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, normalColor));
        } else*/
        if (background instanceof StateListDrawable) {
            DrawableContainer.DrawableContainerState drawableContainerState =
                    (DrawableContainer.DrawableContainerState) background.getConstantState();
            Drawable[] children = drawableContainerState.getChildren();
            //Log.e("children", ":" + children[0]);
            //Log.e("children", ":" + children[1]);

            // ***** for layer drawable

            GradientDrawable selectedDrawable = null;
            GradientDrawable unselectedDrawable = null;

            if (children[0] instanceof LayerDrawable) {

                if (children[0] != null) {
                    LayerDrawable selectedItem = (LayerDrawable) children[0];
                    selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
                }
                if (children[1] != null) {
                    LayerDrawable unselectedItem = (LayerDrawable) children[1];
                    unselectedDrawable = (GradientDrawable) unselectedItem.getDrawable(1);
                }
            } else if (children[0] instanceof GradientDrawable) {
                if (children[0] != null)
                    selectedDrawable = (GradientDrawable) children[0];
                if (children[1] != null)
                    unselectedDrawable = (GradientDrawable) children[1];

            }
            if (pressedColor == 0)
                pressedColor = normalColor;

            if (selectedDrawable != null) {
                // Log.e("selectedDrawable", "**************");
                selectedDrawable.setStroke(stroke, ContextCompat.getColor(activity, pressedColor));
                selectedDrawable.setColor(ContextCompat.getColor(activity, pressedColor));
            }
            if (unselectedDrawable != null) {
                //Log.e("unselectedDrawable", "**************");
                unselectedDrawable.setStroke(stroke, ContextCompat.getColor(activity, normalColor));
                unselectedDrawable.setColor(ContextCompat.getColor(activity, normalColor));
            }
            // Log.e("util.normalColor", " :" + normalColor);
            //Log.e("util.pressedColor", " :" + pressedColor);

        }
        /*else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, normalColor));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, normalColor));
        }*/


        /*
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        long endTime = System.currentTimeMillis() - startTime;
        // Log.e("Total Time", "for tag background:" + endTime + " ms");

        return background;
    }

    public static void makePhoneCall(Context context, String number) {
        if (number.trim().length() > 0) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
        }
    }

    public static void switchToMapAndSetPin(Context activity, String latitude, String longitude,
                                            String locationTitle, String address) {
        try {
            String strUri = "";

            if (longitude != null &&
                    longitude.length() > 0 &&
                    !longitude.equalsIgnoreCase("0.0") &&
                    !longitude.equalsIgnoreCase("0") &&
                    latitude != null &&
                    latitude.length() > 0 &&
                    !latitude.equalsIgnoreCase("0.0") &&
                    !latitude.equalsIgnoreCase("0")) {
                strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" +
                        locationTitle + ")";
                //Log.e("strUri", ":" + strUri);

            } else {
                strUri = "http://maps.google.co.in/maps?q=" + locationTitle;
                //Log.e("strUri", ":" + strUri);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
            intent.setComponent(new ComponentName(
                    "com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity"));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {

            try {
                activity.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.google.android.apps.maps")));
            } catch (ActivityNotFoundException anfe) {
                activity.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                                "http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
            }

            e.printStackTrace();
        }//  startActivity(intent);
    }


    public static String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId)
                .toString();
    }


    public static Drawable changeRoundedCornerColor(Drawable background, String normalColorString) {

       /* if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, R.color.menu_color4));
        } else if (background instanceof StateListDrawable) {
            DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) background.getConstantState();
            Drawable[] children = drawableContainerState.getChildren();

            GradientDrawable selectedDrawable = null;
            GradientDrawable unselectedDrawable = null;

            if (children[0] instanceof LayerDrawable) {

                if (children[0] != null) {
                    LayerDrawable selectedItem = (LayerDrawable) children[0];
                    selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
                }
            } else if (children[0] instanceof GradientDrawable) {
                if (children[0] != null)
                    selectedDrawable = (GradientDrawable) children[0];
            }

            if (selectedDrawable != null) {
                Log.e("selectedDrawable", "**************");
                selectedDrawable.setStroke(stroke, ContextCompat.getColor(activity, normalColor));
                selectedDrawable.setColor(ContextCompat.getColor(activity, normalColor));
            }
        } else*/
        if (background instanceof GradientDrawable) {
            //Log.e("", "********** gradient *&***********");
            ((GradientDrawable) background)
                    .setColor(Color.parseColor(Utility.getValidColorString(normalColorString)));
        } /*else if (background instanceof ColorDrawable) {

            ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, normalColor));
        }*/


        return background;
    }

    public static Drawable changeRoundedCornerColorLocal(Activity activity, Drawable background,
                                                         String normalColorString, int stroke, int position) {
        long startTime = System.currentTimeMillis();
        //  Log.e("position", " original :"+ position);
        position = position + 1;

        if (position > 7) {
            position = (position % 7);
            if (position == 0) {
                position = 1;
                //  position = position + 1;
            }
        }

        //   Log.e("normalColorString", " name :" + normalColorString + position);
        //int normalColor = activity.getResources().getIdentifier(normalColorString + position, "color", activity.getPackageName());
        //int normalColor = ;


       /* if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, R.color.menu_color4));
        } else if (background instanceof StateListDrawable) {
            DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) background.getConstantState();
            Drawable[] children = drawableContainerState.getChildren();

            GradientDrawable selectedDrawable = null;
            GradientDrawable unselectedDrawable = null;

            if (children[0] instanceof LayerDrawable) {

                if (children[0] != null) {
                    LayerDrawable selectedItem = (LayerDrawable) children[0];
                    selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
                }
            } else if (children[0] instanceof GradientDrawable) {
                if (children[0] != null)
                    selectedDrawable = (GradientDrawable) children[0];
            }

            if (selectedDrawable != null) {
                Log.e("selectedDrawable", "**************");
                selectedDrawable.setStroke(stroke, ContextCompat.getColor(activity, normalColor));
                selectedDrawable.setColor(ContextCompat.getColor(activity, normalColor));
            }
        } else*/
        if (background instanceof GradientDrawable) {
            //Log.e("", "********** gradient *&***********");
            ((GradientDrawable) background).setColor(Color.parseColor("#" + normalColorString));
        } /*else if (background instanceof ColorDrawable) {

            ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, normalColor));
        }*/

        /*
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        long endTime = System.currentTimeMillis() - startTime;
        // Log.e("Total Time", "for tag change left background:" + endTime + " ms");

        return background;
    }

    public static Drawable changeRoundedCornerColorNoPosition(Activity activity,
                                                              Drawable background, int normalColor, int stroke) {
        long startTime = System.currentTimeMillis();

        if (background instanceof StateListDrawable) {
            DrawableContainer.DrawableContainerState drawableContainerState =
                    (DrawableContainer.DrawableContainerState) background.getConstantState();
            Drawable[] children = drawableContainerState.getChildren();

            GradientDrawable selectedDrawable = null;
            GradientDrawable unselectedDrawable = null;

            if (children[0] instanceof LayerDrawable) {

                if (children[0] != null) {
                    LayerDrawable selectedItem = (LayerDrawable) children[0];
                    selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
                }
            } else if (children[0] instanceof GradientDrawable) {
                if (children[0] != null)
                    selectedDrawable = (GradientDrawable) children[0];
            }

            if (selectedDrawable != null) {
                //Log.e("selectedDrawable", "**************");
                selectedDrawable.setStroke(stroke, ContextCompat.getColor(activity, normalColor));
                selectedDrawable.setColor(ContextCompat.getColor(activity, normalColor));
            }
            // Log.e("util.normalColor", " :" + normalColor);
        }

        /*
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        long endTime = System.currentTimeMillis() - startTime;
        // Log.e("Total Time", "for tag background:" + endTime + " ms");

        return background;
    }


    public static String removeAllString(String normalString) {
        String result = "0";
        if (normalString != null &&
                normalString.length() > 0) {
            result = normalString.replaceAll("[^\\d.]", "").trim();
        }
        return result;
    }

    public static String removeAllNKeepNumbers(String normalString) {
        String result = "0";
        if (normalString != null &&
                normalString.length() > 0) {
            result = normalString.replaceAll("[^\\d]", "").trim();
        }
        return result;
    }


    public static boolean checkRelease() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("release");
    }


    public static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) *
                    resources.getDisplayMetrics().density);
    }

    public static int getLocalImage(Context context, String imageUrl) {
        int drawableValueAsInteger = 0;
        try {
            drawableValueAsInteger = context.getResources()
                    .getIdentifier(imageUrl, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawableValueAsInteger;
    }

    public static String getRandom(List<String> collection) {
        String[] all = (String[]) collection.toArray();
        int index = (int) (Math.random() * (((all.length - 1) - 0) + 1)) + 0;
        return all[index];
    }

    public static String getValidColorString(String dishColor) {
        if (dishColor != null &&
                !dishColor.isEmpty()) {
            if (!dishColor.contains("#")) {
                if (dishColor.length() >= 6) {
                    return new StringBuilder().append("#").append(dishColor).toString();
                } else {
                    dishColor = "#24BDA9";
                    return dishColor;
                }
            } else {
                return dishColor;
            }
        }
        dishColor = "#24BDA9";
        return dishColor;
    }

    public static String retrieveValidPhoneNo(String phoneNumber) {
        String finalPhone = "";
        if (phoneNumber != null &&
                !phoneNumber.isEmpty()) {
            String phoneNumberString = phoneNumber.trim();
            String[] phoneNos = phoneNumberString.split("\\+");
            for (String ph : phoneNos) {
                if (ph.length() > 5) {
                    finalPhone = ph;
                }
            }
        }
        return finalPhone;
    }

    public static String getFirstName(Context context) {
        return IOPref.getInstance().getString(context, IOPref.PreferenceKey.first_name, "");
    }

    public static String getLastName(Context context) {
        return IOPref.getInstance().getString(context, IOPref.PreferenceKey.last_name, "");
    }

    public static String getUserID(Context context) {
        return IOPref.getInstance().getString(context, IOPref.PreferenceKey.userID, "");
    }

    public static String getLatitude(Context applicationContext) {
        return IOPref.getInstance().getString(applicationContext, IOPref.PreferenceKey.LAT, "");
    }

    public static String getLongitude(Context applicationContext) {
        return IOPref.getInstance().getString(applicationContext, IOPref.PreferenceKey.LNG, "");
    }

    public static String getTitle() {
        String result = "Hi,";
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean getOnDuty(Context context) {
        return IOPref.getInstance().getBoolean(context, IOPref.PreferenceKey.onDuty, false);
    }


    public static boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public static void showMessage(Context context, String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getModelNo() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    @Retention(SOURCE)
    @IntDef({CIRCLE, RECTANGLE})
    public @interface FocusType {
    }

    public static final int CIRCLE = 0;
    public static final int RECTANGLE = 1;


    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {

                        if (key.equalsIgnoreCase("zoneName"))
                            value = URLDecoder.decode(pair[1], "UTF-8");
                        else
                            value = Uri.decode(pair[1]);
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    public static Bitmap compressImage(Context context, String imageUri, String srcfileName) {
        String filePath = getRealPathFromURI(context, imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        //      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        //      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = srcfileName;
        try {
            out = new FileOutputStream(filename);

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return scaledBitmap;
    }

    private static String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    /**
     * To get device consuming netowkr type is 2g,3g,4g
     *
     * @param context
     * @return "2g","3g","4g" as a String based on the network type
     */
    public static String getNetworkType(Context context) {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2g";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    /**
                     From this link https://en.wikipedia.org/wiki/Evolution-Data_Optimized ..NETWORK_TYPE_EVDO_0 & NETWORK_TYPE_EVDO_A
                     EV-DO is an evolution of the CDMA2000 (IS-2000) standard that supports high data rates.

                     Where CDMA2000 https://en.wikipedia.org/wiki/CDMA2000 .CDMA2000 is a family of 3G[1] mobile technology standards for sending voice,
                     data, and signaling data between mobile phones and cell sites.
                     */
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    //Log.d("Type", "3g");
                    //For 3g HSDPA , HSPAP(HSPA+) are main  networktype which are under 3g Network
                    //But from other constants also it will 3g like HSPA,HSDPA etc which are in 3g case.
                    //Some cases are added after  testing(real) in device with 3g enable data
                    //and speed also matters to decide 3g network type
                    //https://en.wikipedia.org/wiki/4G#Data_rate_comparison
                    return "3g";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    //No specification for the 4g but from wiki
                    //I found(LTE (Long-Term Evolution, commonly marketed as 4G LTE))
                    //https://en.wikipedia.org/wiki/LTE_(telecommunication)
                    return "4g";
                default:
                    return "Unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public static String getNetworkStrength(Context context) {
        return IOPref.getInstance().getString(context,IOPref.PreferenceKey.NETWORK_STRENGTH,"1");
    }

    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }


    /**
     * Gets the device unique id called IMEI. Sometimes, this returns 00000000000000000 for the
     * rooted devices.
     **/
    @SuppressLint("HardwareIds")
    public static String getDeviceImei(Context ctx) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ctx.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    Log.e("", "" + "permissionDenied");
                    return android_id;
                }
            }

            String deviceUniqueIdentifier = null;
            if (null != telephonyManager) {
                if (Build.VERSION.SDK_INT >= 26) {
                    deviceUniqueIdentifier = telephonyManager.getImei();
                } else {
                    deviceUniqueIdentifier = telephonyManager.getDeviceId();
                }
            }
            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
                deviceUniqueIdentifier = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return deviceUniqueIdentifier;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    // Vibrate for 150 milliseconds
    public static void shakeItBaby(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(
                        150, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
