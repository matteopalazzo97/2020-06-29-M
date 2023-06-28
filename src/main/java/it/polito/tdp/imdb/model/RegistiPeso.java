package it.polito.tdp.imdb.model;

public class RegistiPeso implements Comparable<RegistiPeso>{
	
	private Director d;
	private double peso;
	
	public RegistiPeso(Director d, double peso) {
		super();
		this.d = d;
		this.peso = peso;
	}

	public Director getD() {
		return d;
	}

	public void setD(Director d) {
		this.d = d;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return d.toString() + "    ATTORI IN COMUNE: " + peso;
	}

	@Override
	public int compareTo(RegistiPeso o) {
		return (int)(this.peso-o.peso);
	}
	
	

}
