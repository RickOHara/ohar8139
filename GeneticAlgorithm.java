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
	private Generation generateNewGeneration(Chromosome parent){
		
		double mutPer = 0.01;//percentage range of possible genetic mutations
		
		//get parent gene values
		double pFuelVal = parent.getLowFuelValue();
		int pShipVal = parent.getShipInRangeValue();
		int pNearbyVal = parent.getNearbyValue();
		int pMoneyVal = parent.getTooMuchMoneyValue();
		double pPositiveVal = parent.getPositiveWeightValue();
		double pEnergyVal = parent.getEnergyToMoneyConversionValue();
		
		Generation nextGeneration = new Generation();
		
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
	
	/**
	 * Fitness function for gauging how a client has performed.
	 * @param chromosome
	 */
	private void fitnessFunction(Chromosome chromosome){
		
	}
	
	/**
	 * Selection function two select two parents for the crossover function.
	 * The selection is based on the total money collected. It will pull the two parents that collected
	 * the most amount of money in the last generation.
	 * @param generation
	 * @return
	 */
	private Generation selectionFunction(Generation generation){
		//Search Values for Selection
		double max1 = 0;
		double max2 = 0;
		int indexOfMax1 = 0;
		int indexOfMax2 = 0;
		
		//Find the Highest performing agent in the generation. 
		for (int i=0; i < generation.getGeneration().size(); i++){
			//Test to see if the current chromosome is better than the current max
			if(generation.getGeneration().get(i).getMoneyCollected() > max1){
				max1 = generation.getGeneration().get(i).getMoneyCollected();
				indexOfMax1 = i;
			}
		}
		
		//Find the second highest performing agent in the generation
		for (int i=0; i < generation.getGeneration().size(); i++){
			//Test to see if the current chromosome is better than the current max
			if(generation.getGeneration().get(i).getMoneyCollected() > max2 && i != indexOfMax1){
				max2 = generation.getGeneration().get(i).getMoneyCollected();
				indexOfMax2 = i;
			}
		}
		
		//Create the new generation with the two highest performers as parents.
		Generation newGeneration = new Generation();
		newGeneration.add(generation.getGeneration().get(indexOfMax1));
		newGeneration.add(generation.getGeneration().get(indexOfMax2));
		
		return newGeneration;
		
	}
	
	private void crossoverFuction(Chromosome parent1, Chromosome parent2){
		
	}
	
}
