package iostudio.in.et.utility;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by MotoBeans on 4/21/2016.
 */
public class UtilDate {

    public static String getDate(String sDate) {
        String parseDate = sDate;
        DateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();

            try {
                DateFormat originalFormatSec = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date date = originalFormatSec.parse(sDate);
                parseDate = targetFormat.format(date);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return parseDate;
    }

    public static String getDateDay(String sDate) {
        String parseDate = "";

        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }
    public static String getDateFromDate(String sDate) {
        String parseDate = "";

        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMM");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }

    public static String getDateMonth(String sDate) {
        String parseDate = "";

        try {

            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }

    public static String getDateLongMonth(String sDate) {
        String parseDate = "";

        try {

            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMMM");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }

    public static String getDateWithOutTimeDetails(String sDate) {
        String parseDate = "";

        try {
            // sDate = "2016-08-15 10:39:23";
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);

            //  Log.e("parseDate","1:"+date);
            // Log.e("parseDate","2:"+DateUtils.isToday(date.getTime()));
           /* if (DateUtils.isToday(date.getTime())) {
                parseDate = "Today";
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }

    public static String getCurrentTime() {

        String currentDate = "";

        try {
            SimpleDateFormat FORMATTER;
            Date date = null;
            try {
                date = new Date();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FORMATTER = new SimpleDateFormat("HH:mm", Locale.US);//12 hr
            FORMATTER.setTimeZone(TimeZone.getDefault());
            currentDate = FORMATTER.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    public static String getDateWithOutTimeDetailsShortMonth(String sDate) {
        String parseDate = "";

        try {
            // sDate = "2016-08-15 10:39:23";
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);

            //  Log.e("parseDate","1:"+date);
            // Log.e("parseDate","2:"+DateUtils.isToday(date.getTime()));
           /* if (DateUtils.isToday(date.getTime())) {
                parseDate = "Today";
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }

    public static String getDateWithOutTime(String sDate) {
        String parseDate = "";

        try {
            // sDate = "2016-08-15 10:39:23";
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);
            //  Log.e("parseDate","1:"+date);
            // Log.e("parseDate","2:"+DateUtils.isToday(date.getTime()));
            if (DateUtils.isToday(date.getTime())) {
                parseDate = "Today";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }

    public static String getTimeWithOutDate(String sDate) {
        String parseDate = "";

        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("hh:mm a");
            Date date = originalFormat.parse(sDate);
            parseDate = targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseDate;
    }

    public static String getStandartCurrentDate() {
        String currentDate = "";

        // DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        currentDate = originalFormat.format(date);

        return currentDate;
    }

    public static String getStandartCurrentDateFormated() {
        String parseDate = "";
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = new Date();
            parseDate = originalFormat.format(date);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date dateFormated = null;
            try {
                dateFormated = originalFormat.parse(parseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            parseDate = targetFormat.format(dateFormated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseDate;
    }

    public static String getCurrentDate() {
        String currentDate = "";

        // DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        currentDate = originalFormat.format(date);

        return currentDate;
    }
    public static String getCurrentDateOnly() {
        String currentDate = "";

        // DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        currentDate = originalFormat.format(date);

        return currentDate;
    }


    /**
     * Pass your date format and no of days for minus from current
     * If you want to get previous date then pass days with minus sign
     * else you can pass as it is for next date
     *
     * @param dateFormat
     * @param days
     * @return Calculated Date
     */
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));

        //getCalculatedDate("dd-MM-yyyy", -10); // It will gives you date before 10 days from current date

        //getCalculatedDate("dd-MM-yyyy", 10);  // It will gives you date after 10 days from current date
    }

    public static String getCalculatedDate(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        try {
            return s.format(new Date(s.parse(date).getTime()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
           // Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }

        //getCalculatedDate("01-01-2015", "dd-MM-yyyy", -10); // It will gives you date before 10 days from given date

        //getCalculatedDate("01-01-2015", "dd-MM-yyyy", 10);  // It will gives you date after 10 days from given date
        return null;
    }

}
