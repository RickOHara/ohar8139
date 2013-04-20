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

	public void setMoneyCollected(double moneyCollected) {
		this.moneyCollected = moneyCollected;
	}
	
}
