package datagen;

import java.util.Random;

import pack.Condition.CheckType;

public class IntGenerator implements Generator{
	/*
	 * BETWEEN("a;b", DataType.DATE, DataType.DECIMAL, DataType.INT),
		BIGGER("Than", DataType.DATE, DataType.DECIMAL, DataType.INT),
		EQUALS("Other", DataType.values()),
		IN("a;b;c", DataType.VARCHAR, DataType.CHAR),
		LIKE("Regex", DataType.VARCHAR, DataType.CHAR, DataType.DATE),
		SMALLER("Than", DataType.DATE, DataType.DECIMAL, DataType.INT);
	 */
	long max = 0;
	long min = 0;
	Integer eq;
	String[] in;
	public IntGenerator(int n) {
		for(int i = 0; i<n;i++) {
			max*=10;
			max+=9;
			min*=10;
			min-=9;
		}
	}
	@Override
	public IntGenerator max(String value) {
		max = Integer.parseInt(value);
		return this;
	}
	@Override
	public IntGenerator min(String value) {
		min = Integer.parseInt(value);
		return this;
	}
	@Override
	public IntGenerator equal(String value) {
		eq = Integer.parseInt(value);
		return this;
	}
	@Override
	public IntGenerator in(String[] value) {
		in = value;
		return this;
	}
	
	@Override
	public IntGenerator conditionOf(CheckType t, String value) {
		switch(t) {
		case BETWEEN:
			String[] values = value.split(";");
			int v0 = Integer.parseInt(values[0]);
			int v1 = Integer.parseInt(values[1]);
			max = Math.max(v0, v1);
			min = Math.min(v0, v1);
			break;
		case BIGGER:
			min = Integer.parseInt(value);
			break;
		case SMALLER:
			max = Integer.parseInt(value);
			break;
		case EQUALS:
			eq = Integer.parseInt(value);
			break;
		case IN:
			in = value.split(";");
		default:
		}
		return this;
	}
	@Override
	public String build() {
		if(eq != null)
			return eq.toString();
		else if(in != null) {
			return in[new Random().nextInt(in.length)];
		}
		else {
			System.out.println(min);
			return String.valueOf(min+(new Random().nextLong())%(max+1));
		}
	}
}
