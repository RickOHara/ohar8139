package ohar8139;

public class Chromosome {

	//tracks:
	private double lowFuelValue;//low fuel value
	private int shipInRangeValue;//ship in range
	private int nearbyValue;//nearby value
	private int tooMuchMoneyValue;//too much money
	private double positiveWeightValue;//positive weight factor
	private double energyToMoneyConversionValue;//energy/money conversion value

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

	public double getShipInRangeValue() {
		return shipInRangeValue;
	}

	public double getnearbyValue() {
		return nearbyValue;
	}

	public double getTooMuchMoneyValue() {
		return tooMuchMoneyValue;
	}
	
	public double getPositiveWeightValue() {
		return positiveWeightValue;
	}
	
	public double getEnergyToMoneyConversionValue() {
		return energyToMoneyConversionValue;
	}
	
}
