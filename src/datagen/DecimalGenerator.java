package datagen;

import pack.Condition.CheckType;

public class DecimalGenerator extends IntGenerator{
	int d;
	public DecimalGenerator(int n, int d) {
		super(n);
		this.d = d;
	}
	@Override
	public String build() {
		int val = Integer.parseInt(super.build());
		return String.valueOf(((double)val)/Math.pow(10, d));	
	}
	@Override
	public IntGenerator conditionOf(CheckType t, String value) {
		switch(t) {
		case BETWEEN:
			String[] values = value.split(";");
			double v0 = Double.parseDouble(values[0]);
			double v1 = Double.parseDouble(values[1]);
			max = (int) (Math.max(v0, v1)*Math.pow(10, d));
			min = (int) (Math.min(v0, v1)*Math.pow(10, d));
			break;
		case BIGGER:
			min = (int) (Double.parseDouble(value)*Math.pow(10, d));
			break;
		case SMALLER:
			max = (int) (Double.parseDouble(value)*Math.pow(10, d));
			break;
		case EQUALS:
			eq = (int) (Double.parseDouble(value)*Math.pow(10, d));
			break;
		default:
		}
		return this;
	}
}
