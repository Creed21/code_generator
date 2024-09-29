package model;

public class Student {
	private Integer id;
	private String name;


	public Student() {}

	public Student(Integer id, String name  ) {
		this.id = id;
		this.name = name;

	}

	public void set{fieldNameCapitalized}(Integer id) {
 		this.id = id;
	}
 	public void set{fieldNameCapitalized}(String name) {
 		this.name = name;
	}
 

	public Integer get${fieldNameCapitalized}() {
		return id;	
	}
	public String get${fieldNameCapitalized}() {
		return name;	
	}

}
