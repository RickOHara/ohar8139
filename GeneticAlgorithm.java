package ohar8139;

import java.util.Vector;

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
		
		//test addition to repo
		
	}
	
}
