package ohar8139;

public class Chromosome {

	private double lowFuelValue;
	private int shipInRangeValue;
	private int nearbyValue;
	private int tooMuchMoneyValue;
	private double positiveWeightValue;
	private double energyToMoneyConversionValue;
	private double moneyCollected;

	//all values are initialized on object construction and cannot be mutated
	public Chromosome (double lowFuelVal, int shipInRangeVal, int nearbyVal, int tooMuchMoneyVal,
			double positiveWeightVal, double energyMoneyConversionVal){

		this.lowFuelValue = lowFuelVal;
		this.shipInRangeValue = shipInRangeVal;
		this.nearbyValue = nearbyVal;
		this.tooMuchMoneyValue = tooMuchMoneyVal;
		this.positiveWeightValue = positiveWeightVal;
		this.energyToMoneyConversionValue = energyMoneyConversionVal;
		
	}

	public double getLowFuelValue() {
		return lowFuelValue;
	}

	public int getShipInRangeValue() {
		return shipInRangeValue;
	}

	public int getNearbyValue() {
		return nearbyValue;
	}

	public int getTooMuchMoneyValue() {
		return tooMuchMoneyValue;
	}
	
	public double getPositiveWeightValue() {
		return positiveWeightValue;
	}
	
	public double getEnergyToMoneyConversionValue() {
		return energyToMoneyConversionValue;
	}

	public double getMoneyCollected() {
		return moneyCollected;
	}
	
	//used by the genetic algorithm crossover function to implement single point crossover
	public Number[] getGenesArray(){
		
		Number[] genes = new Number[6];
		
		genes[0] = lowFuelValue;
		genes[1] = shipInRangeValue;
		genes[2] = nearbyValue;
		genes[3] = tooMuchMoneyValue;
		genes[4] = positiveWeightValue;
		genes[5] = energyToMoneyConversionValue;
		
		return genes;
	}

	public void setMoneyCollected(double moneyCollected) {
		this.moneyCollected = moneyCollected;
	}
	
	public String toString(){
		
		String str = "Chromosome: \n";
		Number[] genes = getGenesArray();
		for(int i=0; i<genes.length; i++){
			str += "    " + genes[i] + "\n";
		}
		return str;
	}
	
}
