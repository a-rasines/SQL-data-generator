package pack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;

import datagen.BoolGenerator;
import datagen.CharGenerator;
import datagen.DateGenerator;
import datagen.DecimalGenerator;
import datagen.Generator;
import datagen.IntGenerator;
import pack.Condition.CheckType;


public class DataColumn {
	public enum DataType{
		CHAR(1), 
		VARCHAR(1), 
		DATE(0), 
		INT(1), 
		DECIMAL(2), 
		BOOL(0);
		int q;
		DataType(int q) {
			this.q = q;
		}
		public int getParams() {
			return q;
		}
		public static DataType[] checkableValues() {
			return new DataType[]{CHAR, VARCHAR, DATE, INT, DECIMAL};
		}
	};
	Generator gen;
	
	char[] alphabet = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm".toCharArray();
	DataType t;
	Map<CheckType, Condition> conds = new HashMap<>();
	Random rand = new Random();
	int[] vars;
	public DataColumn(DataType type, int[] var){
		t = type;
		switch(t) {
		case BOOL:
			gen = new BoolGenerator();
			break;
		case DATE:
			gen = new DateGenerator();
			break;
		case CHAR:
			gen = new CharGenerator(var[0], false);
			break;
		case VARCHAR:
			gen = new CharGenerator(var[0], true);
			break;
		case DECIMAL:
			gen = new DecimalGenerator(var[0], var[1]);
			break;
		case INT:
			gen = new IntGenerator(var[0]);
			break;
		}
		vars = var;
	}
	public void addCondition(CheckType t, Condition c) {
		
		if(t.compatible.contains(this.t)) {
			conds.put(t, c);
			gen.conditionOf(t, c.getParam());
		}else
			JOptionPane.showMessageDialog(null, "La condición "+t.toString()+" no es compatible con "+this.t.toString());
	}
	public void removeCondition(CheckType t) {
		if(conds.containsKey(t)) {
			conds.remove(t);
		}else {
			JOptionPane.showMessageDialog(null, "No existe ninguna condición de ese tipo");
		}
	}
	public String randomValue() {
		return gen.build();
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(t.toString()+(vars.length==0?"":("("+String.valueOf(vars[0])+(vars.length == 1?"":" , "+String.valueOf(vars[1]))+") (")));
		conds.keySet().forEach(v->sb.append(v.toString()+" , "));
		return sb.append(")").toString();
	}
}
