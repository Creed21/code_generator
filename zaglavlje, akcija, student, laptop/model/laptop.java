package model;

public class Laptop {
	private Integer id;
	private String name;
	private Integer student_id;


	public Laptop() {}

	public Laptop(Integer id, String name, Integer student_id  ) {
		this.id = id;
		this.name = name;
		this.student_id = student_id;

	}

	public void set{fieldNameCapitalized}(Integer id) {
 		this.id = id;
	}
 	public void set{fieldNameCapitalized}(String name) {
 		this.name = name;
	}
 	public void set{fieldNameCapitalized}(Integer student_id) {
 		this.student_id = student_id;
	}
 

	public Integer get${fieldNameCapitalized}() {
		return id;	
	}
	public String get${fieldNameCapitalized}() {
		return name;	
	}
	public Integer get${fieldNameCapitalized}() {
		return student_id;	
	}

}
