package pack;

import java.util.Arrays;
import java.util.List;

import datagen.DateGenerator;
import pack.DataColumn.DataType;

public class Condition {
	public enum CheckType{
		BETWEEN("a;b", DataType.DATE, DataType.DECIMAL, DataType.INT),
		BIGGER("Than", DataType.DATE, DataType.DECIMAL, DataType.INT),
		EQUALS("Other", DataType.values()),
		//EQUALS_COLUMN("Column nº or c1+2*c4-6/c2", DataType.values()),
		IN("a;b;c", DataType.checkableValues()),
		LIKE("Regex", DataType.VARCHAR, DataType.CHAR, DataType.DATE),
		SMALLER("Than", DataType.DATE, DataType.DECIMAL, DataType.INT);
		String label;
		List<DataType> compatible;
		CheckType(String param, DataType... compatibleWith){
			compatible = Arrays.asList(compatibleWith);
			label = param;
		}
		public String getLabel() {
			return label;
		}
	}
	private CheckType t;
	private String param;
	public Condition(CheckType type, String regex) {
		t = type;
		param = regex;
	}
	public String getParam() {
		return param;
	}
	public CheckType getType() {
		return t;
	}
	public String toString() {
		return t.toString();
	}
	
}
