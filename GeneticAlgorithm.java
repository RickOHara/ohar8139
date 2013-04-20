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
	
	Random randomGenerator;//used for mutation and crossover
	
	GeneticAlgorithm(boolean shouldLearn){
		
		this.isLearningOn = shouldLearn;
		
		randomGenerator = new Random();
		
		//if the algorithm should be learning, run the current Chromosome
		
		//if the algorithm should not be learning (i.e. should be competitive on this run), run the best chromosome that has been found
		
		//test for crossover and mutation
		Chromosome p1 = new Chromosome(2000.0, 400, 300, 1000, 5.0, 0.04);
		Chromosome p2 = new Chromosome(500.0, 1400, 100, 700, 5.0, 0.14);
		
		Generation newGen = generateNewGeneration(crossoverParentGenes(p1, p2));
		for(Chromosome c : newGen.getGeneration()){
			System.out.println(c);
		}
		
	}
	
	/**
	 * Takes two parents and crosses over their traits into a child chromosome
	 * Uses the single point crossover method
	 * @param parent1
	 * @param parent2
	 * @return Chromosome that the next generation should be based off of
	 */
	private Chromosome crossoverParentGenes(Chromosome parent1, Chromosome parent2){
		
		Number[] p1Genes = parent1.getGenesArray();
		Number[] p2Genes = parent2.getGenesArray();
		
		int crossoverPoint = randomGenerator.nextInt(6);
		System.out.println(crossoverPoint);
		
		Number[] cGenes = new Number[6];//child genes
		
		//intialize with parent1 genes
		for(int i=0; i<cGenes.length; i++){
			cGenes[i] = p1Genes[i];
		}
		
		//take parent2 genes after the crossover point
		for(int i = crossoverPoint; i<cGenes.length ; i++){
			cGenes[i] = p2Genes[i];
		}
		
		//create a new chromosome from the inherited genes
		Chromosome childChromosome = new Chromosome(cGenes[0].doubleValue(), cGenes[1].intValue(), cGenes[2].intValue(), 
				cGenes[3].intValue(), cGenes[4].doubleValue(), cGenes[5].doubleValue());
		
		return childChromosome;
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
		return randomGenerator.nextInt(max - min) + min;
	}
	
	private double getDoubleInRange(double min, double max){
		return min + (randomGenerator.nextDouble() * (max - min));
	}
	
	/**
	 * Fitness function for gauging how a client has performed.
	 * @param chromosome
	 */
	private void fitnessFunction(Chromosome chromosome){
		
	}
	
	private void selectionFunction(Generation generation){
		
	}
	
}
