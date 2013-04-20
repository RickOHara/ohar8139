package ohar8139;

import java.util.Random;
import java.util.Vector;
import spacewar2.simulator.Toroidal2DPhysics;

import ohar8139.Chromosome;

public class GeneticAlgorithm {

	
	private final int CHROM_PER_GEN = 10;//number of child chromosomes to be created per generations
	private final int RUNS_PER_CHROM = 3;//number of runs of the ladder to test each child chromosome
	
	private Chromosome currentChromosome;//current chromosome being tested
	private int numChromosomeRuns;//number of testing rounds that the current chromosome has been through
	
	private Vector<Chromosome> chromosomes;//all chromosomes of the current generation
	
	//the best overall chromosome that has been found through learning
		//this is the chromosome used by the knowledge representation if the 
		//learning is turned off
	private Chromosome currentBestChromosome;
	
	//determines whether this run should be 
	private boolean isLearningOn;
	
	Random randomGenerator;//used for mutation method
	
	GeneticAlgorithm(boolean shouldLearn){
		
		this.isLearningOn = shouldLearn;
		
		//if the algorithm should be learning, run the current Chromosome
		
		//if the algorithm should not be learning (i.e. should be competitive on this run), run the best chromosome that has been found
		
	}
	
	//generates a new generation of chromosomes based on the parents 
		//chromosome created by the crossover function
	//this fulfills the mutation part of the project
	private Vector<Chromosome> generateNewGeneration(Chromosome parent){
		
		double mutPer = 0.01;//percentage range of possible genetic mutations
		
		//get parent gene values
		double pFuelVal = parent.getLowFuelValue();
		int pShipVal = parent.getShipInRangeValue();
		int pNearbyVal = parent.getNearbyValue();
		int pMoneyVal = parent.getTooMuchMoneyValue();
		double pPositiveVal = parent.getPositiveWeightValue();
		double pEnergyVal = parent.getEnergyToMoneyConversionValue();
		
		Vector<Chromosome> nextGeneration = new Vector<Chromosome> (CHROM_PER_GEN);
		
		for(int i=0; i<CHROM_PER_GEN; i++){
			
			//randomly generate child genes based on parent and mutation percentage
			double cFuelVal = getDoubleInRange(pFuelVal - pFuelVal*mutPer, pFuelVal + pFuelVal*mutPer);
			int cShipVal = getIntInRange(pShipVal - (int)(pShipVal*mutPer),pShipVal + (int)(pShipVal*mutPer));
			int cNearbyVal = getIntInRange(pNearbyVal - (int)(pNearbyVal*mutPer),pNearbyVal + (int)(pNearbyVal*mutPer));
			int cMoneyVal = getIntInRange(pMoneyVal - (int)(pMoneyVal*mutPer),pMoneyVal + (int)(pMoneyVal*mutPer));
			double cPositiveVal = getDoubleInRange(pPositiveVal - pPositiveVal*mutPer, pPositiveVal + pPositiveVal*mutPer);
			double cEnergyVal = getDoubleInRange(pEnergyVal - pEnergyVal*mutPer, pEnergyVal + pEnergyVal*mutPer);
			
			nextGeneration.add(new Chromosome(cFuelVal, cShipVal, cNearbyVal, cMoneyVal, 
					cPositiveVal, cEnergyVal));
		}
		
		return nextGeneration;
	}
	
	private int getIntInRange(int min, int max){
		if(randomGenerator == null) randomGenerator = new Random();
		return randomGenerator.nextInt(max - min) + min;
	}
	
	private double getDoubleInRange(double min, double max){
		if(randomGenerator == null) randomGenerator = new Random();
		return min + (randomGenerator.nextDouble() * (max - min));
	}
	
	
}
