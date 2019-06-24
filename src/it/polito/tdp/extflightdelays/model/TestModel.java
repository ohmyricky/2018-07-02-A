package it.polito.tdp.extflightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.creaGrafo(4100);
		
		System.out.println(model.cittaMax(model.getIdMap().get(166), 15000));
		System.out.println(model.cittaCt());
		System.out.println(model.migliaUsate());

	}

}
