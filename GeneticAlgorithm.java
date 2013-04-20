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
	
	private Chromosome bestChromosome;//the best overall chromosome that has been found through learning across all generations
	
	//determines whether this run should be 
	private boolean isLearningOn;
	
	Random randomGenerator;//used for mutation and crossover
	
	GeneticAlgorithm(boolean shouldLearn){
		
		this.isLearningOn = shouldLearn;
		randomGenerator = new Random();
		
		//TODO: read in xml data 
		
		
		/*
		//test for crossover and mutation
		Chromosome p1 = new Chromosome(2000.0, 400, 300, 1000, 5.0, 0.04);
		Chromosome p2 = new Chromosome(500.0, 1400, 100, 700, 5.0, 0.14);
		
		Generation newGen = generateNewGeneration(crossoverParentGenes(p1, p2));
		for(Chromosome c : newGen.getGeneration()){
			System.out.println(c);
		}
		*/
		
	}
	
	public Chromosome getChromosomeForKnowledgeRepresentation(){
		if(isLearningOn) return currentChromosome;
		return bestChromosome;
	}
	
	//TODO: implement method stub
	//called in the shutdown method of the client
	public void analyzeResults(Toroidal2DPhysics space){
		
		//if learning is not on, return... nothing else needs to be done
		if(!isLearningOn) return;
		
		//find how many points the team scored using the reference to space
		
		//compare against the bestChromosome... update bestChromosome if the current is better
		
		//if the currentChromosome is the last of the generation that needs to be tested,
			//select best from the generation, crossover using the best, then generate a new generation and update the chromosomes vector
		
		//update the currentChromosome to prepare for the next run
		
		//write the generation xml to disc
		
		//write out the performance stats to disc (performance of generations over time)
		//Wesley: are we using xml or text to save the results that we will be graphing?
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
	
}
