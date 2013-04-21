package ohar8139;

import java.util.ArrayList;

public class Generation {
	
	private int currentIndex;
	private int GenerationNumber;
	private ArrayList<Chromosome> generation;
	
	
	public Generation(){
		setCurrentIndex(0);
		generation = new ArrayList<Chromosome>();
		setGenerationNumber(0);
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

	public int getGenerationNumber() {
		return GenerationNumber;
	}

	public void setGenerationNumber(int generationNumber) {
		GenerationNumber = generationNumber;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public void setMoney(int money, int index){
		generation.get(index).setMoneyCollected(money);
	}
	
	public void setChromosomeAtIndex(int index, Chromosome c){
		generation.set(index, c);
	}

	
}
