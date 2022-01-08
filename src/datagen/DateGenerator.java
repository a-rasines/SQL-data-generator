package datagen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import pack.Condition.CheckType;

public class DateGenerator implements Generator{
	/**
	 * BETWEEN("a;b", DataType.DATE, DataType.DECIMAL, DataType.INT), //DONE
		BIGGER("Than", DataType.DATE, DataType.DECIMAL, DataType.INT), //DONE
		EQUALS("Other", DataType.values()), //DONE
		LIKE("Regex", DataType.VARCHAR, DataType.CHAR), //DONE
		SMALLER("Than", DataType.DATE, DataType.DECIMAL, DataType.INT), //DONE
	 */
	private Date max = new Date(System.currentTimeMillis());
	private Date min = new Date(0);
	private String[] in;
	private DateLikeParser like;
	Calendar date = Calendar.getInstance(); 
	private Date eq;
	
	public DateGenerator conditionOf(CheckType t, String value) {
		switch(t) {
		case BETWEEN:
			String[] values = value.split(";");
			Date v0 = dateParser(values[0]);
			Date v1 = dateParser(values[1]);
			max = v0.compareTo(v1)==1?v0:v1;
			min = v0.compareTo(v1)==1?v1:v0;
			break;
		case BIGGER:
			min = dateParser(value);
			break;
		case SMALLER:
			max = dateParser(value);
			break;
		case EQUALS:
			eq = dateParser(value);
			break;
		case LIKE:
			like = new DateLikeParser(value);
			break;
		case IN:
			in = value.split(";");
		default:
		}
		return this;
	}
	@Override
	public DateGenerator equal(String eq) {
		this.eq = dateParser(eq);
		return this;
	}
	@Override
	public DateGenerator max(String max) {
		this.max = dateParser(max);
		return this;
	}
	@Override
	public DateGenerator min(String min) {
		this.min = dateParser(min);
		return this;
	}
	@Override
	public DateGenerator in(String[]values) {
		return this;
	}
	@Override
	public DateGenerator like(String like) {
		this.like = new DateLikeParser(like);
		return this;
	}
	SimpleDateFormat ret = new SimpleDateFormat("yyyy-MM-dd");
	public String build() {
		if(eq != null) {
			return ret.format(eq);
		}
		if(in != null)
			return in[new Random().nextInt(in.length)];
		date.setTime(min);
		date.add(Calendar.DATE, new Random().nextInt((int)((max.getTime()-min.getTime())/1000/60/60/24)));
		if(like != null) {
			date.set(Calendar.YEAR, like.getValueFrom(Calendar.YEAR, date.get(Calendar.YEAR)));
			date.set(Calendar.MONTH, like.getValueFrom(Calendar.MONTH, date.get(Calendar.MONTH)));
			date.set(Calendar.DATE, like.getValueFrom(Calendar.DATE, date.get(Calendar.DATE)));
			date.set(Calendar.HOUR_OF_DAY, like.getValueFrom(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY)));
			date.set(Calendar.MINUTE, like.getValueFrom(Calendar.MINUTE, date.get(Calendar.MINUTE)));
			date.set(Calendar.SECOND, like.getValueFrom(Calendar.SECOND, date.get(Calendar.SECOND)));
		}
		return "'"+ret.format(date.getTime())+"'";
	}
	
	public static boolean isDate(String s) {
		return dateParser(s) != null;
	}
	public static Date dateParser(String date) {
		String[] formats = {
				"yyyy-MM-dd",
				"yyyy-MM",
				"MM-dd",
				"yyyy" 
				};
		for(String s : formats) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(date);
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}

}
class DateLikeParser{
	final String year;
	final String month;
	final String day;
	final String hour;
	final String min;
	final String sec;
	public DateLikeParser(String date) {
		String[] f = date.split("-");
		String h0 = f[f.length-1];
		String[] h;
		if (h0.contains(" ")){
			h = h0.split(" ")[1].split(":");
			h0 = h0.split(" ")[0];
		}else if(h0.contains("%")) {
			if(h0.startsWith("%")) {
				h = h0.split(":");
				h0 = "%";
			}else {
				h = h0.split("%")[1].split(":");
				h0 = h0.split("%")[0];
			}
		}else {
			h = new String[0];
		}
		if(f.length==3) {
			year = f[0];
			month = f[1];
			day = h0;
		}else if(f.length == 2) {
			if(f[0].startsWith("%")) {
				year = "%";
				month = f[0];
				day = h0;
			}else {
				year = f[0];
				month = h0;
				day = "%";
			}
		}else if(f.length == 1) {
			if(date.startsWith("%")) {
				day = "%";
				month = "%";
				year = h0;
			}else {
				day = h0;
				month = "%";
				year = "%";
			}
		}else {
			year = "%";
			month = "%";
			day = "%";
		}
		if(h.length==3) {
			hour = h[0];
			min = h[1];
			sec = h[2];
		}else if(h.length == 2) {
			if(h[0].startsWith("%")) {
				hour = "%";
				min = h[0];
				sec = h[1];
			}else {
				hour = h[0];
				min = h[1];
				sec = "%";
			}
		}else if(h.length == 1) {
			if(date.startsWith("%")) {
				hour = "%";
				min = "%";
				sec = h[0];
			}else {
				hour = h[0];
				min = "%";
				sec = "%";
			}
		}else {
			hour = "%";
			min = "%";
			sec = "%";
		}
	}
	public int getValueFrom(int df, int value) {
		int n;
		String val;
		switch(df) {
		case Calendar.YEAR:
			val = year;
			n = 4;
		break;
		case Calendar.MONTH:
			val = month;
			n = 2;
		break;
		case Calendar.DATE:
			val = day;
			n = 2;
		break;
		case Calendar.HOUR_OF_DAY:
			val = hour;
			n = 2;
		break;
		case Calendar.MINUTE:
			val = min;
			n = 2;
			break;
		case Calendar.SECOND:
			val = sec;
			n = 2;
			break;
		default:
			val = "";
			n = 0;
		}
		return fillField(val,String.valueOf(value), n);
	}
	private int fillField(String year, String val, int length) {
		int n = Integer.parseInt(year.replace("%", ""));
		if(year.replace("%", "").length()!=length) {
			String post = year.substring(year.indexOf("%")+1);
			String prev = year.substring(0, year.indexOf("%"));
			String mid = "";
			for(int i = prev.length(); i<length-post.length();i++) {
				mid+=val.charAt(i);
			}
			n = Integer.parseInt(prev+mid+post);
			return n;
		}
		return Integer.parseInt(year);
	}
	
}
