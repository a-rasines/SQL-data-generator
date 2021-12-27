package pack;

public class SavedColumn {
	private DataColumn dc;
	private String name;
	public SavedColumn(DataColumn dc) {
		this.dc = dc;
		name = dc.toString();
	}
	public void addToUse() {
		Window.columns.addElement(dc);
	}
	public void removeFromSave() {
		
	}
	public void changeName(String n) {
		name = n;
	}
	@Override
	public String toString() {
		return name;
	}
}
