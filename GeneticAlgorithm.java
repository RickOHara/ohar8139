package ohar8139;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

import spacewar2.clients.ImmutableTeamInfo;
import spacewar2.simulator.Toroidal2DPhysics;

import ohar8139.Chromosome;

public class GeneticAlgorithm {

	private Generation generation = new Generation(); //all chromosomes in the current generation
	
	private final int CHROM_PER_GEN = 10;//number of child chromosomes to be created per generations
	private final int RUNS_PER_CHROM = 3;//number of runs of the ladder to test each child chromosome
	
	private Chromosome currentChromosome = null;//current chromosome being tested
	private int numChromosomeRuns;//number of testing rounds that the current chromosome has been through
	
	//private Vector<Chromosome> chromosomes;//all chromosomes of the current generation
	
	private Chromosome bestChromosome = null;//the best overall chromosome that has been found through learning across all generations
	
	//determines whether this run should be 
	private boolean isLearningOn;
	
	Random randomGenerator;//used for mutation and crossover
	
	GeneticAlgorithm(boolean shouldLearn){
		
		this.isLearningOn = shouldLearn;
		randomGenerator = new Random();
		
		Chromosome chromosome = null;

		//Read XML Data
		XStream xstream = new XStream();
		xstream.alias("Generation", Generation.class);

		try { 
			generation = (Generation) xstream.fromXML(new File("ohar8139/generation.xml"));
			System.out.println("Generation size" + generation.getGeneration().size());
		} catch (XStreamException e) {
			// if you get an error, handle it other than a null pointer because
			// the error will happen the first time you run
			chromosome = new Chromosome(2000.0, 400, 300, 1000, 5.0, 0.04);
			generation.add(chromosome);
		}
		
		
		//Set Best Chromosome for tests
		//Set the first one just to have a comparison
		bestChromosome = generation.getGeneration().get(0);
		for (Chromosome c : generation.getGeneration()){
			if(c.getMoneyCollected() > bestChromosome.getMoneyCollected()){
				bestChromosome = c;
			}
		}
		
		//Set the current chromosome to test against.
		currentChromosome = generation.getGeneration().get(generation.getCurrentIndex());
		
		
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
		//Add current client to the generation
				//But first add its money to the gene, this will be used to base performance
		double MoneyCollected = -1;
		Set<ImmutableTeamInfo> allTeams = space.getTeamInfo();
		for (ImmutableTeamInfo team : allTeams){
				if(team.getTeamName().equalsIgnoreCase("WesandRick")){
					MoneyCollected = team.getScore();		
				}
		}
		currentChromosome.setMoneyCollected(MoneyCollected);
		generation.setChromosomeAtIndex(generation.getCurrentIndex(), currentChromosome);
		
		//compare against the bestChromosome... update bestChromosome if the current is better
		if(currentChromosome.getMoneyCollected() > bestChromosome.getMoneyCollected()){
			bestChromosome = currentChromosome;
		}
		
		//if the currentChromosome is the last of the generation that needs to be tested,
			//select best from the generation, crossover using the best, then generate a new generation and update the chromosomes vector
		if(generation.getGeneration().size() >= CHROM_PER_GEN && generation.getCurrentIndex()>=generation.getGeneration().size()-1 ){
			//Run the selection and crossover methods, Generate the new generation and output the new gen
			Generation parents = selectionFunction(generation);
			Chromosome newChrom = crossoverParentGenes(parents.getGeneration().get(0), parents.getGeneration().get(1));
			
			//Create the new generation to run.
			Generation newGen = generateNewGeneration(newChrom);
			newGen.setGenerationNumber(parents.getGenerationNumber()+1);
			
			//To XML, rename the old gen as GenX.xml and the new one, CurrentGen.xml
			//Add +1 to generation number for new generation number.
			XStream xstream = new XStream();
			xstream.alias("Generation", Generation.class);

			try { 
				// if you want to compress the file, change FileOuputStream to a GZIPOutputStream
				xstream.toXML(generation, new FileOutputStream(new File("ohar8139/"+generation.getGenerationNumber()+".xml")));
			} catch (XStreamException e) {
				// if you get an error, handle it somehow as it means your knowledge didn't save
				// the error will happen the first time you run
				
			} catch (FileNotFoundException e) {
				
			}
			
			generation = newGen;
			generation.setGenerationNumber(generation.getGenerationNumber() + 1);
			
			//update the currentChromosome to prepare for the next run
			currentChromosome = newChrom;
			
		}else{
			//Update the currentChromosome index to prepare for the next run
			generation.setCurrentIndex(generation.getCurrentIndex()+1);
		}
		
		//write the generation xml to disc
		XStream xstream = new XStream();
		xstream.alias("Generation", Generation.class);

		try { 
			// if you want to compress the file, change FileOuputStream to a GZIPOutputStream
			xstream.toXML(generation, new FileOutputStream(new File("ohar8139/generation.xml")));
		} catch (XStreamException e) {
			// if you get an error, handle it somehow as it means your knowledge didn't save
			// the error will happen the first time you run
			
		} catch (FileNotFoundException e) {
			
			
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
		newGeneration.setGenerationNumber(newGeneration.getGenerationNumber()+1);
		
		return newGeneration;
		
	}
	
}
