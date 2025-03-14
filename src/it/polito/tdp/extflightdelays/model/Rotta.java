package it.polito.tdp.extflightdelays.model;

public class Rotta implements Comparable<Rotta>{
	
	private Airport a1;
	private Airport a2;
	private Double distanza;
	
	public Rotta(Airport a1, Airport a2, Double distanza) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.distanza = distanza;
	}

	public Airport getA1() {
		return a1;
	}

	public void setA1(Airport a1) {
		this.a1 = a1;
	}

	public Airport getA2() {
		return a2;
	}

	public void setA2(Airport a2) {
		this.a2 = a2;
	}

	public Double getDistanza() {
		return distanza;
	}

	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}

	@Override
	public String toString() {
		return "Arrivo a " + a2 + " tra " + distanza + " miglia\n";
	}

	@Override
	public int compareTo(Rotta o) {
		return o.getDistanza().compareTo(this.getDistanza());
	}

}
