package ohar8139;

import spacewar2.objects.Asteroid;
import spacewar2.objects.Base;
import spacewar2.objects.Beacon;
import spacewar2.objects.Ship;
import spacewar2.simulator.Toroidal2DPhysics;
import ohar8139.KnowledgeValues;

/**
 * This class is responsible for evaluating the values of potential actions that a ship can take. It uses the knowledge representation 
 * defined in the KnowledgeValues class to make an optimal action based on the current state of the environment. 
 * All returned values are an estimate of the potential currency value of an action. 
 * @author rickohara
 *
 */

public class ActionEvaluator {

	//reference to the current state of space
	Toroidal2DPhysics space;
	
	//reference to the ship that is to take an action
	Ship ship;
	
	//reference to the knowledge values populated via the chromosome
	KnowledgeValues values;
	
	//converts energy to money values, needed since all returned evaluations are in units of currency
	//final double ENERGY_TO_MONEY_CONVERSION_FACTOR = 0.04;//25 energy = 1 currency
	
	//amount to increase the evaluation value by for each positive factor
	//final double POSITIVE_WEIGHT_FACTOR = 5.0;//1.33;
	
	//cost of purchasing a new base
	//TODO: this needs to be synched with the API's cost for bases when it becomes available
	final int NEW_BASE_COST = 500;
	
	public ActionEvaluator(Toroidal2DPhysics space, Ship ship, KnowledgeValues vals){
		this.space = space;
		this.ship = ship;
		this.values = vals;
	}
	
	/**
	 * Provides an estimate of the value of the ship purchasing a new base (assuming the next action the ship takes will
	 * be moving to the base).
	 * value = buyBaseWeight() * (ship's money - cost of new base)
	 * @return double estimating the currency value of buying a new base
	 */
	public double evaluateBasePurchase(){
		
		int shipMoney = this.ship.getMoney();
		
		double actionValue = this.buyBaseWeight() * (shipMoney - NEW_BASE_COST);
		
		return actionValue;
		
	}
	
	/**
	 *Estimates value of moving to a nearby beacon
	 *value = moveToBeaconWeight() * (value of energy / sqrt(distanmce to beacon))
	 * @param beacon the beacon that is potentially being moved to
	 * @return double estimating the currency value of getting energy from the beacon
	 */
	public double evaluateBeaconMove(Beacon beacon){
		
		int beaconEnergy = Beacon.BEACON_ENERGY_BOOST;
		double energyValue = beaconEnergy * values.getEnergyToMoneyConversionFactor();
		
		double distanceToBeacon = this.space.findShortestDistance(this.ship.getPosition(), beacon.getPosition());
		
		double actionValue = moveToBeaconWeight() * (energyValue / Math.sqrt(distanceToBeacon));
		
		return actionValue;
	}
	
	/**
	 *Estimates value of returning to the ship's nearest base
	 *value = returnToBaseWeight() * (current money * sqrt(distance to base))
	 * @param base the base that is potentially being moved to
	 * @return double estimating the currency value of returning to the base
	 */
	public double evaluateReturnToBase(Base base){
		
		int currentMoney = this.ship.getMoney();
		
		double distanceToBase = this.space.findShortestDistance(this.ship.getPosition(), base.getPosition());
		
		double actionValue = returnToBaseWeight() * (currentMoney / Math.sqrt(distanceToBase));
		
		return actionValue;
	}
	
	/**
	 * TODO: implement this once bullets can be purchased
	 * @param enemyShip
	 * @return
	 */
	public double evaluateFiringBulletAtEnemyShip(Ship enemyShip){
		
		return 0;
	}
	
	/**
	 *Estimates value of mining the closest asteroid
	 *value = moveToAsteroidWeight() * (asteroid value / sqrt(distance to asteroid))
	 * @param asteroid the asteroid that is potentially being moved to
	 * @return double estimating the currency value of mining the asteroid
	 */
	public double evaluateMoveToAsteroid(Asteroid asteroid){
		
		int asteroidValue = asteroid.getMoney();
		
		double distanceToBase = this.space.findShortestDistance(this.ship.getPosition(), asteroid.getPosition());
		
		double actionValue = moveToAsteroidWeight() * (asteroidValue / Math.sqrt(distanceToBase));
		
		return actionValue;
	} 
	
	/**
	 * Calculates the weight to use in calculating the value for moving to an asteroid.
	 * This is how our client applies our knowledge representation to choosing actions.
	 * Positive factors are : isNearbyMineableAsteroid
	 * @return double value corresponding to weight
	 */
	private double moveToAsteroidWeight(){

		double weight = 1.0;
		
		//positive factors are isNearbyMineableAsteroid
		if(values.isNearbyMineableAsteroid(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		
		return weight;
	}
	
	/**
	 * Calculates the weight to use in calculating the value for firing a bullet.
	 * This is how our client applies our knowledge representation to choosing actions.
	 * Positive factors are : isNearbyEnemyShip
	 * @return double value corresponding to weight
	 */
	private double fireBulletWeight(){

		double weight = 1.0;
		
		//positive factors are isNearbyEnemyShip
		if(values.isNearbyEnemyShip(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		
		return weight;
	}
	
	/**
	 * Calculates the weight to use in calculating the value for returning to base.
	 * This is how our client applies our knowledge representation to choosing actions.
	 * Positive factors are : islowOnFuel, isNearbyBase, hasLargeMoneySum
	 * @return double value corresponding to weight
	 */
	private double returnToBaseWeight(){

		double weight = 1.0;
		
		//positive factors are islowOnFuel and isNearbyBase and hasLargeMoneySum
		//if(KnowledgeValues.isLowOnFuel(this.space, this.ship)) weight *= POSITIVE_WEIGHT_FACTOR;//took out, favored base too much
		if(values.isNearbyBase(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		if(values.hasLargeMoneySum(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		
		return weight;
	}
	
	/**
	 * Calculates the weight to use in calculating the value for moving to a beacon.
	 * This is how our client applies our knowledge representation to choosing actions.
	 * Positive factors are : islowOnFuel, isNearbyBeacon
	 * @return double value corresponding to weight
	 */
	private double moveToBeaconWeight(){
		
		double weight = 1.0;
		
		//positive factors are islowOnFuel and isNearbyBeacon
		if(values.isLowOnFuel(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		if(values.isNearbyBeacon(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		
		return weight;
		
	}
	
	/**
	 * Calculates the weight to use in calculating the value for purchasing a new base.
	 * This is how our client applies our knowledge representation to choosing actions.
	 * Positive factors are : islowOnFuel, hasLargeMoneySum
	 * @return double value corresponding to weight
	 */
	private double buyBaseWeight(){
		
		double weight = 1.0;
		
		//positive factors are islowOnFuel and hasLargeMoneySum
		if(values.isLowOnFuel(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		if(values.hasLargeMoneySum(this.space, this.ship)) weight *= values.getPositiveWeightFactor();
		
		return weight;
	}
	
}
