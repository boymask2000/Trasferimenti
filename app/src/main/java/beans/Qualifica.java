package beans;

public class Qualifica {
	private int id;
	private int codice;
	private int lingua;
	private String descrizione;
	
	public int getCodice() {
		return codice;
	}
	public void setCodice(int codice) {
		this.codice = codice;
	}
	public int getLingua() {
		return lingua;
	}
	public void setLingua(int lingua) {
		this.lingua = lingua;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
