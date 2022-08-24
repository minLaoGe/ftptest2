package com.example.ftptest2.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期特殊处理工具类
 * @author yellow
 *
 */
public class DateUtil {
	
	public final static SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public final static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
	public final static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat yyyyMMddHHmmss_format = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
     * 将data转String类型的显示模式
     * @param date
     * @param dataFormat
     * @return
     */
    public static String formatDate(Date date, SimpleDateFormat dataFormat) {
    	return dataFormat.format(date);
    }
    
    /**
     * 将String日期类型转String类型的显示模式
     * @param date
     * @param dataFormat
     * @return
     */
    public static String formatDate(String date, SimpleDateFormat dataFormat){
    	
    	//转换时间格式
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try{//时间格式转换一定要捕获异常
			Date d=formatter.parse(date);
			return dataFormat.format(d);
		}
		catch(ParseException e){
			e.printStackTrace();
			return null;
		}
    	
    }
	
	/**
	 * 返回目标时间 到现在的时间的long
	 * @param date
	 * @param dayDelay
	 * @return
	 */
	public static long fromNow(Date date, int dayDelay,int hourDelay, int minDelay){
		Calendar tmp = Calendar.getInstance();
		tmp.setTime(date);
		tmp.add(Calendar.DAY_OF_YEAR, dayDelay);
		tmp.add(Calendar.HOUR_OF_DAY,hourDelay);
		tmp.add(Calendar.MINUTE, minDelay);
		Date tmpDate = tmp.getTime();
		Date now = new Date();
		return tmpDate.getTime() - now.getTime();
	}
	
	/**
	 * 返回剩余的时间：格式-》距最后付款0分钟
	 * @param time
	 * @return
	 */
	public static String fromNowString(long time){
		if(time <= 0)
			return "距最后付款0分钟";
		long hour = 1000 * 60 * 60;
		long day = hour * 24;
		long min = 1000*60;
		int remainDay = (int)(time/day);
		int remainHour = (int)(time%day/hour);
		int remainMin = (int)(time%day%hour/min);
			return "距最后付款" +remainMin+"分钟";
	}

	/**
	 * 格式->距租期到还剩1天！
	 * @param time
	 * @return
     */
	public static  String fromNowString2(long time){
		if(time <= 0)
			return "距租期到还剩0天！";
		long hour = 1000 * 60 * 60;
		long day = hour * 24;
		long min = 1000*60;
		int remainDay = (int)(time/day);
		int remainHour = (int)(time%day/hour);
		int remainMin = (int)(time%day%hour/min);
			if(remainDay == 0 && remainHour > 0)
				return "距租期到还剩"+remainHour+"小时";
			else if(remainDay == 0 && remainHour == 0)
				return "距租期到还剩"+remainMin+"分钟！";
		return "距租期到还剩"+remainDay+"天！";
	}
	
	
	
	// 返回这一年这个月的天数
	public static int tianShu(int nian, int yue) {
		if (nian == 0 || yue > 12 || yue < 1) {
			System.out.println("您传入的数据非法");
			return -1;
		}

		if (yue == 2) {
			if (shiFouRunNian(nian)) {
				return 29;
			} else {
				return 28;
			}
		}

		int ts = 0;

		switch (yue) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			ts = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			ts = 30;
			break;
		default:

		}

		return ts;

	}
	
	// 判断是否为闰年
	private static boolean shiFouRunNian(int nian) {
		boolean run = false;

		if (((nian % 4 == 0) && (nian % 100 != 0)) || (nian % 400 == 0)) {
			run = true;
		}

		return run;
	}
	
	// 同年
	public static int tongNian(int nian, int ksy, int ksr, int zzy, int zzr) {
		int yueShu = 0;
		for (int j = ksy; j < zzy; j++) {
			yueShu += tianShu(nian, j);

		}
		int ts = yueShu - ksr + zzr;
		return ts;
	}

	// 不同年
	public static int buTongNian(int ksn, int ksy, int ksr, int zzn, int zzy,
			int zzr) {

		int ts0 = 0; // 开始年与终止年之间的年份的天数和，不包括终止年
		int ts1 = 0; // 开始年所在的那一年中的天数
		int ts2 = 0; // 终止年所在的这一年中的天数

		ts1 = tongNian(ksn, 1, 1, ksy, ksr);
		ts2 = tongNian(zzn, 1, 1, zzy, zzr);
		for (int i = ksn; i < zzn; i++) {
			if (shiFouRunNian(i) == true)
				ts0 += 366;
			else
				ts0 += 365;
		}
		int ts = Math.abs(ts0 - ts1 + ts2);
		return ts;

	}
	
	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	 * @param strDate
	 * @return
	 */
	 public static Date strToDate(String strDate, String format) {
	    SimpleDateFormat formatter = new SimpleDateFormat(format);
	    ParsePosition pos = new ParsePosition(0);
	    Date strtodate = formatter.parse(strDate, pos);
	    return strtodate;
	}
	
	
	public static void main(String[] args) {
//		String a = "20171010160230";
//		String b = "20171010000000";
//		System.out.println("compare:"+a.compareTo(b));
		
		//在前面的年月日
//		int ksn=2019;
//		int ksy=Integer.valueOf("09");
//	    int ksr=26;
//	    //在后面的年月日
//	    int zzn=Integer.valueOf("2019")+1;
//	    int zzy=Integer.valueOf("01");
//	    int zzr=1;
//	    int ts=0;
//	    
//	    ts = buTongNian(ksn,ksy,ksr,zzn,zzy,zzr);
//	    System.out.println("间隔天数为：" + ts);
		
		Calendar cal = Calendar.getInstance();
//		cal.setTime(strToDate("20211025", "yyyyMMdd"));
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -30);
		System.out.println(formatDate(cal.getTime(), yyyyMMdd));
		
		
//		String txtime = "20191014";
//		//在前面的年月日
//		int ksn=Integer.valueOf(txtime.substring(0,4));
//		int ksy=Integer.valueOf(txtime.substring(4,6));
//	    int ksr=Integer.valueOf(txtime.substring(6,8));
//	    //在后面的年月日
//	    int zzn=Integer.valueOf(txtime.substring(0,4));
//	    int zzy="12".equals(txtime.substring(4,6))?1:Integer.valueOf(txtime.substring(4,6))+1;
//	    int zzr=1;
//	    int restMonthDays = DateUtil.buTongNian(ksn, ksy, ksr, zzn, zzy, zzr)-1;
//	    zzn=Integer.valueOf(txtime.substring(0,4))+1;
//	    zzy=1;
//        int restYearDays = DateUtil.buTongNian(ksn, ksy, ksr, zzn, zzy, zzr)-1;
//        System.out.println("本月剩余天数："+restMonthDays+"，本年度剩余天数："+restYearDays);
		
	}
}
