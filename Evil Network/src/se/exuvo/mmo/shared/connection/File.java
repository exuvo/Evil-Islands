package se.exuvo.mmo.shared.connection;

public class File {
	public String filename;
	public int size;
	

	@SuppressWarnings("unused")
	private File(){
	}
	
	public File(String name, int size){
		filename = name;
		this.size = size;
	}
}
