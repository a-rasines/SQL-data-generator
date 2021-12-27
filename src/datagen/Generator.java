package datagen;

import pack.Condition.CheckType;

public interface Generator {
	public Generator conditionOf(CheckType t, String value);
	public default Generator equal(String eq) {
		return this;
	}
	public default Generator like(String like) {
		return this;
	}
	public default Generator in(String[] values) {
		return this;
	}
	public default Generator max(String max) {
		return this;
	}
	public default Generator min(String min) {
		return this;
	}
	public String build();
}
