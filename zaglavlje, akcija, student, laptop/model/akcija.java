package model;

public class Akcija {
	private Integer zagl_id;
	private Integer odluka_id;
	private Integer tip_lica;
	private Double vrednost;


	public Akcija() {}

	public Akcija(Integer zagl_id, Integer odluka_id, Integer tip_lica, Double vrednost  ) {
		this.zagl_id = zagl_id;
		this.odluka_id = odluka_id;
		this.tip_lica = tip_lica;
		this.vrednost = vrednost;

	}

	public void set{fieldNameCapitalized}(Integer zagl_id) {
 		this.zagl_id = zagl_id;
	}
 	public void set{fieldNameCapitalized}(Integer odluka_id) {
 		this.odluka_id = odluka_id;
	}
 	public void set{fieldNameCapitalized}(Integer tip_lica) {
 		this.tip_lica = tip_lica;
	}
 	public void set{fieldNameCapitalized}(Double vrednost) {
 		this.vrednost = vrednost;
	}
 

	public Integer get${fieldNameCapitalized}() {
		return zagl_id;	
	}
	public Integer get${fieldNameCapitalized}() {
		return odluka_id;	
	}
	public Integer get${fieldNameCapitalized}() {
		return tip_lica;	
	}
	public Double get${fieldNameCapitalized}() {
		return vrednost;	
	}

}
