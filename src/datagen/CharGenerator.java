package datagen;

import java.util.Random;

import pack.Condition.CheckType;

public class CharGenerator implements Generator{
	private String eq;
	private String like;
	private String[] in;
	private boolean nums;
	private int l;
	public CharGenerator(int length, boolean numbers) {
		l = length;
		nums = numbers;
	}
	
	public CharGenerator equal(String eq) {
		this.eq = eq;
		return this;
	}
	public CharGenerator like(String like) {
		this.like = like;
		return this;
	}
	public CharGenerator in(String[] values) {
		in = values;
		return this;
	}
	@Override
	public Generator conditionOf(CheckType t, String value) {
		switch(t) {
		case EQUALS:
			eq = value;
		case LIKE:
			like = value;
		case IN:
			in = value.split(";");
		}
		return null;
	}
	private static String[] chars = new String[62];
	private static String[] noNumChars = new String[52];
	static {
		for(int i=65;i<91;i++)//Mayusculas
			chars[i-65] = String.valueOf((char)i);
		for(int i=97;i<123;i++)//Minusculas
			chars[i-71] = String.valueOf((char)i);
		noNumChars = chars.clone();
		for(int i = 0;i<10;i++)
			chars[52+i] = String.valueOf(i);
	}
	@Override
	public String build() {
		if(eq != null)
			return '"'+eq+'"';
		if(in != null)
			return '"'+in[new Random().nextInt(in.length)]+'"';
		String val0 = "";
		String[] bchars = nums?chars:noNumChars;
		for(int i = 0; i<l;i++)
			val0 += bchars[new Random().nextInt(bchars.length)];
		if(like != null)
			return "'"+new CharLikeParser(val0, like).build()+"'";
		return "'"+val0+"'";
	}

}
class CharLikeParser{
	String val;
	String cond;
	String conds[];
	public CharLikeParser(String v, String c) {
		val = v;
		cond = c;
		conds = c.split("%");
	}
	public String build() {
		int ss = 0; //Primer char no tocado del string
		int fs = val.length(); //Ultimo char no tocado del string
		int len = cond.replace("%", "").length(); //La longitud de las condiciones restantes
		int s = 0; //Posicion de la primera condicion restante
		int f = conds.length; //Posicion de la última condicion restante excluida
		if(!cond.startsWith("%")) {//Inicio
			s++;
			len -= conds[0].length();
			ss += conds[0].length();
			val = conds[0]+val.substring(conds[0].length());
		}
		if(!cond.endsWith("%")) {//Final
			f--;
			len-= conds[conds.length-1].length();
			fs -= conds[conds.length-1].length();
			val = val.substring(0, val.length()-conds[conds.length-1].length())+conds[conds.length-1];
		}
		boolean valid = true;
		for (int i = s; i<f; i++) {
			while (!valid) {
				int pos = ss+new Random().nextInt(fs-ss);
				if(len > fs-pos-conds[i].length())
					valid = false;
				else {
					valid = true;
					char[] v = val.toCharArray();
					char[] c = conds[i].toCharArray();
					for (int j = pos;j<pos+conds[j].length();j++) {
						v[j] = c[j-pos];
					}
					val = String.valueOf(v);
				}
			}
		}
		return val;
	}
	
}
