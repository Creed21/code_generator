package model;

public class Zaglavlje {
	private Integer zagl_id;
	private Integer odluka_id;
	private String ime;


	public Zaglavlje() {}

	public Zaglavlje(Integer zagl_id, Integer odluka_id, String ime  ) {
		this.zagl_id = zagl_id;
		this.odluka_id = odluka_id;
		this.ime = ime;

	}

	public void set{fieldNameCapitalized}(Integer zagl_id) {
 		this.zagl_id = zagl_id;
	}
 	public void set{fieldNameCapitalized}(Integer odluka_id) {
 		this.odluka_id = odluka_id;
	}
 	public void set{fieldNameCapitalized}(String ime) {
 		this.ime = ime;
	}
 

	public Integer get${fieldNameCapitalized}() {
		return zagl_id;	
	}
	public Integer get${fieldNameCapitalized}() {
		return odluka_id;	
	}
	public String get${fieldNameCapitalized}() {
		return ime;	
	}

}
