package ohar8139;

import java.util.Vector;
import spacewar2.simulator.Toroidal2DPhysics;

import ohar8139.Chromosome;

public class GeneticAlgorithm {

	private final int CHROM_PER_GEN = 10;
	private final int RUNS_PER_CHROM = 3;
	
	private Chromosome currentChromosome;
	private int numChromosomeRuns;
	private Vector<Chromosome> chromosomes;
	
	private Chromosome currentBestChromosome;
	
	private boolean isLearningOn;
	
	
	GeneticAlgorithm(boolean shouldLearn){
		
		this.isLearningOn = shouldLearn;
		
		//if the algorithm should be learning, run the current Chromosome
		
		//if the algorithm should not be learning (i.e. should be competitive on this run), run the best chromosome that has been found
		
		
	}
	
	//generates a new generation of chromosomes based on the parents 
		//chromosome created by the crossover function
	private Vector<Chromosome> generateNewGeneration(){
		
		return null;
	}
	
}
