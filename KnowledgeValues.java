package ohar8139;

//import java.util.ArrayList;
import java.util.Set;

import spacewar2.objects.Asteroid;
import spacewar2.objects.Base;
import spacewar2.objects.Beacon;
import spacewar2.objects.Ship;
import spacewar2.simulator.Toroidal2DPhysics;
import spacewar2.utilities.Position;

/**
 * KnowledgeValues.java
 * 
 * This class contains statically defined functions to get the knowledge values of each of the following characteristics:
 * Need to return to base
 * Nearby Minable Asteroid
 * Need to Buy Base
 * Low on Fuel
 * Need to Buy Bullets
 * Nearby Base
 * Beacon Nearby
 * Enought to buy a base
 * and enemy ship nearby.
 * 
 * Only the nearby bullets is not implemented because (A) I could not find an enumeration for the bullets in the API or (B) You cannot buy bullets yet!
 * 
 * @author Wesley R. Howell
 *
 */
public class KnowledgeValues {
	
	/*
	 * Class value literals for the distances to determine nearby, or far away,
	 * or the values for money and energy.
	 */
	private static final double LowFuelValue = 2000.0;
	private static final int ShipInRange = 400;
	private static final int NearbyValue = 150;
	//private static final int FarAwayValue = 600;
	private static final int TooMuchMoney = 500;//

	/**
	 * isLowOnFuel (space, ship)
	 * This function will determine whether or not the ship is low on fuel and 
	 * needs to refuel. The value for low fuel can be set with the LowFuelValue
	 * 
	 * @param space
	 * @param ship
	 * @return boolean
	 */
	public static boolean isLowOnFuel(Toroidal2DPhysics space, Ship ship){
		//Determine if the ship is low on energy and needs to refuel
		double energy = ship.getEnergy();
		if(energy < LowFuelValue){
			return true;
		}
		else return false;
	}
	
	/**
	 * This function will determine whether or not a beacon is nearby
	 * The value used to compare whether a beacon is nearby is the 
	 * NearBy Value static variable in the class.
	 * 
	 * To determine whether a beacon is within the acceptable radius
	 * use the distance formula. If dist < radius, it is in the circle
	 * 
	 * %% This is using Euclidian Distance, Need to look into making it consider
	 * %% Toroidal distance. 
	 * 
	 * @param space
	 * @return boolean
	 */
	public static boolean isNearbyBeacon(Toroidal2DPhysics space, Ship ship){
		Set<Beacon> beacons = space.getBeacons();
		
		//Loop through all the beacons, then use the distance formula to determine
		//whether any are nearby using NearbyValue
		for(Beacon beacon : beacons){
			Position beaconPosition = beacon.getPosition();
			//Extract points
			//double xBeacon = beaconPosition.getX();
			//double yBeacon = beaconPosition.getY();
			//double xCenter = ship.getPosition().getX();
			//double yCenter = ship.getPosition().getY();
			
			//Run calculation
			
			//Toroidal Distance, Use this one!
			double distance = space.findShortestDistance(ship.getPosition(), beaconPosition);
			
			//Euclidian distance
//			int regulardistance = (int)Math.sqrt((xCenter - xBeacon) * (xCenter - xBeacon) +
//	    			(yCenter - yBeacon) * (yCenter - yBeacon));
			
			//It it is less than NearbyValue and line is free
			if(distance <= NearbyValue && AStarSearch.isFreeLine(ship.getPosition(), beaconPosition , space)){
				return true;
			}
		}
		
		//If we make it here then we visited all the beacons and none are nearby. 
		return false;
	}
	
	
	/**
	 * Determines if a ship is nearby, uses a similar methodology as the NearbyBeacon method
	 * @param space
	 * @param ship
	 * @return boolean
	 */
	public static boolean isNearbyEnemyShip(Toroidal2DPhysics space, Ship ship){
		Set<Ship> ships = space.getShips();
		
		//Loop through all the ships, then use the distance formula to determine
		//whether any are nearby using NearbyValue
		for(Ship currentShip : ships){
			
			//if the ship is not ours, continue
			if(currentShip != ship){
				Position shipPosition = currentShip.getPosition();
				//Extract points
				//double xShip = shipPosition.getX();
				//double yShip = shipPosition.getY();
				//double xCenter = ship.getPosition().getX();
				//double yCenter = ship.getPosition().getY();
				
				//Toroidal Distance Use this one!
				double distance = space.findShortestDistance(ship.getPosition(), shipPosition);

				//Run calculation
//				int distance = (int)Math.sqrt((xCenter - xShip) * (xCenter - xShip) +
//						(yCenter - yShip) * (yCenter - yShip));

				//It it is less than NearbyValue & Line is free
				if(distance <= ShipInRange && AStarSearch.isFreeLine(ship.getPosition(),
						shipPosition, space)){
					return true;
				}
			}
		}
		
		//If we make it here then we visited all the ships and none are nearby. 
		return false;
	}
	
	
	/**
	 * Again use the same methodology (as in nearbyBeacon) to find if the asteroid is close
	 * However, add whether if the asteroid is minable.
	 * 
	 * @param space
	 * @param ship
	 * @return boolean
	 */
	public static boolean isNearbyMineableAsteroid(Toroidal2DPhysics space, Ship ship){
		Set<Asteroid> asteroids = space.getAsteroids();
		
		//Loop through all the ships, then use the distance formula to determine
		//whether any are nearby using NearbyValue
		for(Asteroid asteroid : asteroids){
			
			//if the ship is not ours, continue
			if(asteroid.isMineable()){
				Position asteroidPosition = asteroid.getPosition();
				//Extract points
				//double xAsteroid = asteroidPosition.getX();
				//double yAsteroid = asteroidPosition.getY();
				//double xCenter = ship.getPosition().getX();
				//double yCenter = ship.getPosition().getY();

				//Run calculation
				//Toroidal Distance
				double distance = space.findShortestDistance(ship.getPosition(), asteroidPosition);
				
				
//				int distance = (int)Math.sqrt((xCenter - xAsteroid) * (xCenter - xAsteroid) +
//						(yCenter - yAsteroid) * (yCenter - yAsteroid));

				//It it is less than NearbyValue & Line is free
				if(distance <= NearbyValue /*&& AStarSearch.isFreeLine(ship.getPosition(),
						asteroidPosition, space)*/){
					return true;
				}
			}
		}
		
		//If we make it here then we visited all the ships and none are nearby. 
		return false;
	}
	
	/**
	 * Determines whether or not a base is nearby and that it is accessible. 
	 * 
	 * Uses the same methodology of finding the distance as the NearbyBeacon method.
	 * 
	 * @param space
	 * @param ship
	 * @return
	 */
	public static boolean isNearbyBase(Toroidal2DPhysics space, Ship ship){
		Set<Base> bases = space.getBases();
		
		//Loop through all the ships, then use the distance formula to determine
		//whether any are nearby using NearbyValue
		for(Base base : bases){
			
			//if the base is ours, continue with the function
			if(base.getTeamName() == ship.getTeamName()){
				Position basePosition = base.getPosition();
				//Extract points
				//double xBase = basePosition.getX();
				//double yBase = basePosition.getY();
				//double xCenter = ship.getPosition().getX();
				//double yCenter = ship.getPosition().getY();

				//Run calculation
				//Toroidal Distance - Use this!
				double distance = space.findShortestDistance(ship.getPosition(), basePosition);
				
				
//				int distance = (int)Math.sqrt((xCenter - xBase) * (xCenter - xBase) +
//						(yCenter - yBase) * (yCenter - yBase));

				//It it is less than NearbyValue & Line is free
				if(distance <= NearbyValue && AStarSearch.isFreeLine(ship.getPosition(),
						basePosition, space)){
					return true;
				}
			}
		}
		
		//If we make it here then we visited all the bases and none of ours are nearby. 
		return false;
	}
	
	/**
	 * Method to buy bullets or other power-ups once they exist, This is for a 
	 * specific power-up (ie bullets). However, you cannot buy bullets at this time
	 * @param space
	 * @param ship
	 * @return
	 */
	public static boolean needToBuyBullets(Toroidal2DPhysics space, Ship ship){
		return false;
	}
	
	
	/**
	 * Method to buy a new base if we have sufficient money to buy one
	 * Also, the needs to be based off the far-away from base value.
	 * 
	 * I am not sure how to get the cost for a new base in the API, so
	 * for now, like the Heuristic agent, I am requiring that the base cost is 
	 * passed into this function.
	 * 
	 * @param space
	 * @param ship
	 * @return
	 */
	public static boolean needToBuyBase(Toroidal2DPhysics space, Ship ship, int currentNewBaseCost){
		int money = ship.getMoney();
		
		if(!isNearbyBase(space, ship) && money > currentNewBaseCost){
			return true;
		}
		
		else return false;
	}
	
	/**
	 * Determines whether or not we have enough money needed to buy a base
	 * 
	 * I am not sure how to get the cost for a new base in the API, so
	 * for now, like the Heuristic agent, I am requiring that the base cost is 
	 * passed into this function.
	 * 
	 * @param space
	 * @param ship
	 * @param currentNewBaseCost
	 * @return
	 */
	public static boolean hasEnoughMoneyToBuyBase(Toroidal2DPhysics space, Ship ship, int currentNewBaseCost){
		int money = ship.getMoney();
		
		if(money > currentNewBaseCost){
			return true;
		}
		
		else return false;
	}
	
	/**
	 * Determines whether it is time to return to base based on the current amount of money
	 * held by the ship
	 * 
	 * @param space
	 * @param ship
	 * @return
	 */
	public static boolean hasLargeMoneySum(Toroidal2DPhysics space, Ship ship){
		
		if(ship.getMoney() >= TooMuchMoney){
			return true;
		}
		else return false;
	}

}
