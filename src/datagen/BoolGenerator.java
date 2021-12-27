package datagen;

import java.util.Random;

import pack.Condition.CheckType;

public class BoolGenerator implements Generator {
	String eq;
	@Override
	public BoolGenerator equal(String value) {
		eq = value;
		return this;
	}
	@Override
	public BoolGenerator conditionOf(CheckType t, String value) {
		if(t.equals(CheckType.EQUALS))
			eq = value;
		return this;
	}

	@Override
	public String build() {
			if(eq != null)
				return eq;
		return String.valueOf(new Random().nextBoolean());
	}

}
