package ohar8139;

import java.util.ArrayList;

public class Generation {
	
	private ArrayList<Chromosome> generation;
	
	public Generation(){
		generation = new ArrayList<Chromosome>();
	}
	
	public Generation(ArrayList<Chromosome> genes){
		generation=genes;
	}

	public ArrayList<Chromosome> getGeneration() {
		return generation;
	}

	public void setGeneration(ArrayList<Chromosome> generation) {
		this.generation = generation;
	}

	public void add(Chromosome c){
		generation.add(c);
	}
	
}