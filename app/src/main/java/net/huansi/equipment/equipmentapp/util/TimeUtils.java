package net.huansi.equipment.equipmentapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.Button;
import android.widget.PopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class TimeUtils {

    public static final int START = 1;//开始
    public final static int STOP = 2;//结束


    public static final int TODAY_INDEX=3;//今天
    public static final int TOMORROW_INDEX=4;//明天
    public static final int DAY_AFTER_TOMORROW_INDEX=5;//后天
    public static final int OTHER_BY_THIS_YEAR=8;//今年的其他日期
    public static final int OTHER_INDEX=6;//其他日期
    public static final int YESTERDAY_INDEX=7;//昨天


    private static final String TIME_FORMAT_SLASH="yyyy/MM/dd HH:mm:ss";//"/"的时间格式
    private static final String TIME_FORMAT_MINUS="yyyy-MM-dd HH:mm:ss";//"-"的时间格式
//    private TimeUtil(){}
//    private static TimeUtil timeUtil;
//    public static TimeUtil getInstance(){
//        if(timeUtil==null){
//            timeUtil=new TimeUtil();
//        }
//        return timeUtil;
//    }

    /**
     * 获得本周开始结束时间
     */
    public static String getTimeThisWeek(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 0) day = 7;
        calendar.setTime(new Date());
        //今天是星期天
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -day + 2 - 7);
        } else {
            calendar.add(Calendar.DATE, -day + 2);
        }

        switch (index) {
            case START:
                //周一
                return format.format(calendar.getTime());
            case STOP:
                //周日
                return format.format(new Date());
            default:
                return null;
        }
    }

    /**
     * 获得上一周开始结束时间
     */
    public static String getTimeLaskWeek(int index) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 0) day = 7;
        //今天是星期天
        calendar.setTime(new Date());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -day + 2 - 7 - 7);
        } else {
            calendar.add(Calendar.DATE, -day + 2 - 7);
        }
        switch (index) {
            case START:
                //周一
                return format.format(calendar.getTime());
            case STOP:
                //周日
                return format.format(format.parse(format.format(calendar.getTime())).getTime() + 6 * 24 * 3600 * 1000);
            default:
                return null;

        }
    }

    /**
     * 获得下一周开始结束时间
     */
    public static String getTimeNextWeek(int index) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 0) day = 7;
        //今天是星期天
        calendar.setTime(new Date());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -day + 2 - 7 +7);
        } else {
            calendar.add(Calendar.DATE, -day + 2 + 7);
        }
        switch (index) {
            case START:
                //周一
                return format.format(calendar.getTime());
            case STOP:
                //周日
                return format.format(format.parse(format.format(calendar.getTime())).getTime() + 6 * 24 * 3600 * 1000);
            default:
                return null;

        }
    }

    /**
     * 获得本月开始结束时间
     */
    public static String getTimeThisMonth(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        switch (index) {
            case START:
                //一号
                return format.format(calendar.getTime());
            case STOP:
                //今天
                return format.format(new Date());
            default:
                return null;

        }
    }

    /**
     * 获得本月月初和月末
     */
    public static String getTimeThisMonthAll(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        switch (index) {
            case START:
                //一号
                return format.format(calendar.getTime());
            case STOP:
                //月末
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                return format.format(calendar.getTime());
            default:
                return null;

        }
    }

    /**
     * 获得上月开始结束时间
     */
    public static String getTimeLastMonth(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        switch (index) {
            case START:
                //一号
                return format.format(calendar.getTime());
            case STOP:
                //月末
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                return format.format(calendar.getTime());
            default:
                return null;

        }
    }


    


    //得到当前操作时间
    public static String getCurDate(String timeType) {
        Date now = new Date();
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_SLASH);
        }
        String sCurDate = dateFormat.format(now);
        return sCurDate;
    }

    /**
     * 得到当前操作日期
     */
    public static String getCurrentDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(now);
        return currentDate;
    }

    /**
     * 得到当前操作时间(精确到毫秒)
     */
    public static String getCurMillDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:sss");
        String sCurDate = dateFormat.format(now);
        return sCurDate;
    }

    /**
     * 获得指定时间的long类型的毫秒数
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long getTime(String time, String timeType) throws ParseException {
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_SLASH);
        }
        return dateFormat.parse(time).getTime();
    }

    /**
     * 获得Date相对应的字符串的时间
     *
     * @param date
     * @return
     */
    public static String getTime(Date date, String timeType) {
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_SLASH);
        }
        return dateFormat.format(date);
    }

    /**
     * 通过字符串的时间获得一个Date
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date getDate(String time, String timeType) throws ParseException {
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_SLASH);
        }
        return dateFormat.parse(time);
    }

    /**
     * 通过long类型的时间获得所需的时间
     * @return
     */
    public static String getDate(long time, String timeType) {
        Date now = new Date(time);
        SimpleDateFormat dateFormat;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMAT_SLASH);
        }
        return dateFormat.format(now);
    }

    /**
     * 获得当前时间的毫秒数
     *
     * @return
     */
    public static long getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }

    /**
     * 判断是不是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Date dToday = new Date();
        Calendar cToday = Calendar.getInstance();
        cToday.setTime(dToday);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return cToday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                cToday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                cToday.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间，精确到毫秒
     *
     * @return
     */
    public static long getCurrentMillTime() throws ParseException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:sss");
        return format.parse(format.format(date)).getTime();
    }





    /**
     * 格式是MM-dd HH:mm
     *
     * @return
     */
    public static String getTimeNoYear() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 将秒数转换字符串（00：00）
     *
     * @return
     */
    public static String getDurByString(int seconds) {
        String dur = "";
        long min = seconds / 60;
        if (min < 10) {
            dur += "0" + min + ":";
        } else {
            dur += min + ":";
        }
        long s = seconds % 60;
        if (s < 10) {
            dur += "0" + s;
        } else {
            dur += s;
        }
        return dur;
    }




    private static int betweenDays(Calendar beginDate, Calendar endDate) {
        if (beginDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)) {
            int i1=endDate.get(Calendar.DAY_OF_YEAR);
            int i2=beginDate.get(Calendar.DAY_OF_YEAR);
            return i1 - i2;
        } else {
            if (beginDate.getTimeInMillis() < endDate.getTimeInMillis()) {
                int days = beginDate.getActualMaximum(Calendar.DAY_OF_YEAR)
                        - beginDate.get(Calendar.DAY_OF_YEAR)
                        + endDate.get(Calendar.DAY_OF_YEAR);
                for (int i = beginDate.get(Calendar.YEAR) + 1; i < endDate
                        .get(Calendar.YEAR); i++) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, i);
                    days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return days;
            } else {
                int days = endDate.getActualMaximum(Calendar.DAY_OF_YEAR)
                        - endDate.get(Calendar.DAY_OF_YEAR)
                        + beginDate.get(Calendar.DAY_OF_YEAR);
                for (int i = endDate.get(Calendar.YEAR) + 1; i < beginDate
                        .get(Calendar.YEAR); i++) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, i);
                    days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return days;
            }
        }
    }

    /**
     * 是不是今年的
     * @param time
     * @return
     */
    public static String isThisYear(String time) throws ParseException {
        Calendar currentCalendar=Calendar.getInstance();
        currentCalendar.setTime(new Date());

        Calendar calendar=Calendar.getInstance();
        Date date= getDate(time,time.contains("/")?"/":"-");
        calendar.setTime(date);
        if(calendar.get(Calendar.YEAR)==currentCalendar.get(Calendar.YEAR)){
            return time.split(" ")[0].subSequence(5,time.split(" ")[0].length()).toString();
        }else {
            return time.split(" ")[0];
        }
    }


    /**
     * 不含年份和秒
     * @return
     */
    public static String getTimeNoSecAndYear(String time){
        String [] strsObject=time.split(" ");
        String [] dateStrs=strsObject[0].contains("/")?strsObject[0].split("/"):strsObject[0].split("-");
        String[] timeStrs=null;
        if(strsObject.length>1) {
            timeStrs = strsObject[1].split(":");
        }
        //说明本身就不含年和秒
        if(dateStrs[0].length()<3&&
                (timeStrs==null||timeStrs.length==2)) return time;
        StringBuilder sbTime=new StringBuilder();
        for(int i=0;i<dateStrs.length;i++){
            if(i==0) continue;
            sbTime.append(dateStrs[i]+"-");
        }
        if(!sbTime.toString().isEmpty()){
            sbTime=sbTime.delete(sbTime.length()-1,sbTime.length());
            sbTime.append(" ");
        }
        if(timeStrs!=null){
            for(int i=0;i<timeStrs.length;i++){
                if(i!=timeStrs.length-1){
                    sbTime.append(timeStrs[i]+":");
                }
            }
        }
        if(sbTime.toString().contains(":")) sbTime=sbTime.delete(sbTime.length()-1,sbTime.length());
        return sbTime.toString();
    }

    /**
     * 通过日期得到今天明天还是后天
     * @return
     */
    public static int getStrByDate(String time,String format) throws ParseException {
        Date date ;
        if (time.isEmpty()) return OTHER_INDEX;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        date = dateFormat.parse(time);
        Calendar calDate = Calendar.getInstance();


        Calendar yesterday = Calendar.getInstance();    //今天

        yesterday.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH)-2);
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        yesterday.set(Calendar.HOUR_OF_DAY, 23);
        yesterday.set(Calendar.MINUTE, 59);
        yesterday.set(Calendar.SECOND, 59);

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        today.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH)-1);
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);


        Calendar tomorrow = Calendar.getInstance();//明天
        tomorrow.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        tomorrow.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        tomorrow.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH));
        tomorrow.set(Calendar.HOUR_OF_DAY, 23);
        tomorrow.set(Calendar.MINUTE, 59);
        tomorrow.set(Calendar.SECOND, 59);


        Calendar tomorrowPlus = Calendar.getInstance();//后天
        tomorrowPlus.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        tomorrowPlus.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        tomorrowPlus.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH) + 1);
        tomorrowPlus.set(Calendar.HOUR_OF_DAY, 23);
        tomorrowPlus.set(Calendar.MINUTE, 59);
        tomorrowPlus.set(Calendar.SECOND, 59);


        Calendar tomorrowDoublePlus = Calendar.getInstance();//大后天
        tomorrowDoublePlus.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        tomorrowDoublePlus.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        tomorrowDoublePlus.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH) + 2);
        tomorrowDoublePlus.set(Calendar.HOUR_OF_DAY, 23);
        tomorrowDoublePlus.set(Calendar.MINUTE, 59);
        tomorrowDoublePlus.set(Calendar.SECOND, 59);
        calDate.setTime(date);

        //说明没到期
        if(calDate.after(yesterday)&&calDate.before(today)){
            return YESTERDAY_INDEX;
        } else if (calDate.after(today) && calDate.before(tomorrow)) {
            return TODAY_INDEX;
        } else if (calDate.before(tomorrowPlus) && calDate.after(tomorrow)) {
            return TOMORROW_INDEX;
        } else if (calDate.before(tomorrowDoublePlus) && calDate.after(tomorrowPlus)) {
            return DAY_AFTER_TOMORROW_INDEX;
        } else if(calDate.get(Calendar.YEAR)==today.get(Calendar.YEAR)){
            return OTHER_BY_THIS_YEAR;
        }else {
            return OTHER_INDEX;
        }
    }

    /**
     * 不含有秒
     * @return
     */
    public static String getTimeWithoutSecond(String time)  {
        try {
            Date date=getDate(time,
                    time.contains("/")?"/":"-");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
           return simpleDateFormat.format(date);
        } catch (Exception e) {
            return time;
        }
    }


}
