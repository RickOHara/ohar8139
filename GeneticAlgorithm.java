package ohar8139;

import java.util.Vector;
import spacewar2.simulator.Toroidal2DPhysics;

import ohar8139.Chromosome;

public class GeneticAlgorithm {

	//number of child chromosomes to be created per generations
	private final int CHROM_PER_GEN = 10;
	//number of runs of the ladder to test each child chromosome
	private final int RUNS_PER_CHROM = 3;
	
	//current chromosome being tested
		//this is the chromosome that should be loaded for the knowledge representation values
	private Chromosome currentChromosome;
	//testing rounds that the current chromosome has been through
	private int numChromosomeRuns;
	//all chromosomes of the current generation
	private Vector<Chromosome> chromosomes;
	
	//the best overall chromosome that has been found through learning
		//this is the chromosome used by the knowledge representation if the 
		//learning is turned off
	private Chromosome currentBestChromosome;
	
	//determines whether this run should be 
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
