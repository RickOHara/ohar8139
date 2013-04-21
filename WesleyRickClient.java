package ohar8139;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

import ohar8139.Chromosome;
import spacewar2.actions.DoNothingAction;
import spacewar2.actions.SpacewarAction;
import spacewar2.actions.SpacewarPurchaseEnum;
import spacewar2.clients.ExampleKnowledge;
import spacewar2.clients.ImmutableTeamInfo;
import spacewar2.clients.TeamClient;
import spacewar2.objects.Asteroid;
import spacewar2.objects.Base;
import spacewar2.objects.Beacon;
import spacewar2.objects.Ship;
import spacewar2.objects.SpacewarActionableObject;
import spacewar2.objects.SpacewarObject;
import spacewar2.powerups.SpacewarPowerup;
import spacewar2.powerups.SpacewarPowerupEnum;
import spacewar2.shadows.Shadow;
import spacewar2.simulator.Toroidal2DPhysics;
import spacewar2.utilities.Position;
import ohar8139.ActionEvaluator;
import ohar8139.GeneticAlgorithm;

/**
 * Collects nearby asteroids and brings them to the base, picks up beacons as needed for energy
 * Uses A star search to navigate through the environment.
 * However, the a star agent is kinda slow, but it works and navigates.
 * 
 * A Star is Based on the code provided by Dr. McGovern and the Heuristic Asteroid Collector Agent.
 * 
 * Using Dr. McGovern's A Star code since we have been assigned new partners and this is a neutral starting
 * point for Project 2 since we both had one-off errors in Project 1.
 * 
 * Used the new Heuristic Asteroid Team as a model due to recent API Changes.
 * 
 * @author Wesley R. Howell
 */
public class WesleyRickClient extends TeamClient {
	//private Chromosome chromosome = null;//new Chromosome(0, 0, 0, 0, 0, 0);
	//private Generation generation = new Generation();
	
	HashMap <UUID, Ship> asteroidToShipMap;
	boolean aimingForBase;
	Graph astarGraph;
	//Map <KnowledgeEnum, Boolean> knowledgeValues;
	KnowledgeValues knowledgeValues;
	GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(true);//true == learning is on

	/**
	 * Assigns ships to asteroids and beacons, as described above
	 */
	@SuppressWarnings("unused")
	public Map<UUID, SpacewarAction> getMovementStart(Toroidal2DPhysics space,
			Set<SpacewarActionableObject> actionableObjects) {
		HashMap<UUID, SpacewarAction> actions = new HashMap<UUID, SpacewarAction>();

		// loop through each ship
		for (SpacewarObject actionable :  actionableObjects) {
			if (actionable instanceof Ship) {
				Ship ship = (Ship) actionable;
				SpacewarAction current = ship.getCurrentAction();
				Position currentPosition = ship.getPosition();

				//Get the Knowledge Representation Values
				//I set 1000 as a cost of the base, Need to find the API that will dynamically 
				//Get this value.
				//knowledgeValues = getKnowledgeValues(space, ship, 1000);
				
				//Print the values of the hash map for the knowledge values
				//for (Map.Entry<KnowledgeEnum, Boolean> entry : knowledgeValues.entrySet()){
				//    System.out.println(entry.getKey() + "/" + entry.getValue());
				//}
				//System.out.println("#############################################");
				
				//find the best target to move to
				SpacewarObject dest = chooseActionUsingHeuristic(space, ship);
				Position destPos = dest.getPosition();
				
				SpacewarAction newAction = null;
				
				Random rand = new Random();
				astarGraph = AStarSearch.createGraphToGoalWithBeacons(space, ship, destPos, rand);
				Vertex[] path = astarGraph.findAStarPath(space);
				FollowPathAction fpa = new FollowPathAction(path);
				
				// if there is no beacon, then just skip a turn
				if (dest == null) {
					newAction = new DoNothingAction();
				} else {
					newAction = fpa.followPath(space, ship);
				}
				actions.put(ship.getId(), newAction);
				if(dest instanceof Base){
					aimingForBase = true;
				}
				else{
					aimingForBase = false;
				}
				continue;
				/*
				 * These Actions here follow the same criterion of the Heuristic Agent, 
				 * This part of the function needs to be changed to accommodate the new KnowledgeValues functions. 
				 * Along with the determination of which actions to perform. 
				 * 
				 * This part is modified from the Heuristic Agent to take into account the A* code. 
				 * 
				 */
				/*
				// aim for a beacon if there isn't enough energy
				if (ship.getEnergy() < 2000) {
					Beacon beacon = pickNearestBeacon(space, ship);
					SpacewarAction newAction = null;
					
					Position asteroidPosition = beacon.getPosition();
					Random rand = new Random();
					astarGraph = AStarSearch.createGraphToGoalWithBeacons(space, ship, asteroidPosition, rand);
					Vertex[] path = astarGraph.findAStarPath(space);
					FollowPathAction fpa = new FollowPathAction(path);
					
					// if there is no beacon, then just skip a turn
					if (beacon == null) {
						newAction = new DoNothingAction();
					} else {
						newAction = fpa.followPath(space, ship);
					}
					actions.put(ship.getId(), newAction);
					aimingForBase = false;
					continue;
				}

				// if the ship has enough money, take it back to base
				if (ship.getMoney() > 500) {
					Base base = findNearestBase(space, ship);
					
					Position basePosition = base.getPosition();
					Random rand = new Random();
					astarGraph = AStarSearch.createGraphToGoalWithBeacons(space, ship, basePosition, rand);
					Vertex[] path = astarGraph.findAStarPath(space);
					
					FollowPathAction fpa = new FollowPathAction(path);
					
					SpacewarAction newAction = fpa.followPath(space, ship);
					
					actions.put(ship.getId(), newAction);
					aimingForBase = true;
					continue;
				}

				// did we bounce off the base?
				if (ship.getMoney() == 0 && ship.getEnergy() > 2000 && aimingForBase) {
					current = null;
					aimingForBase = false;
				}

				// otherwise aim for the asteroid
				if (current == null || current.isMovementFinished(space)) {
					aimingForBase = false;
					Asteroid asteroid = pickHighestValueFreeAsteroid(space, ship);
					
					Position asteroidPosition = asteroid.getPosition();
					Random rand = new Random();
					astarGraph = AStarSearch.createGraphToGoalWithBeacons(space, ship, asteroidPosition, rand);
					Vertex[] path = astarGraph.findAStarPath(space);
					
					FollowPathAction fpa = new FollowPathAction(path);

					SpacewarAction newAction = null;

					if (asteroid == null) {
						// there is no asteroid available so collect a beacon
						Beacon beacon = pickNearestBeacon(space, ship);
						Position posBeacon = beacon.getPosition();
						Random randB = new Random();
						astarGraph = AStarSearch.createGraphToGoalWithBeacons(space, ship, posBeacon, randB);
						Vertex[] pathB = astarGraph.findAStarPath(space);
						FollowPathAction goToBeacon = new FollowPathAction(path);
						// if there is no beacon, then just skip a turn
						if (beacon == null) {
							newAction = new DoNothingAction();
						} else {
							
							newAction = goToBeacon.followPath(space, ship);
						}
					} else {
						asteroidToShipMap.put(asteroid.getId(), ship);
						newAction = fpa.followPath(space, ship);
					}
					actions.put(ship.getId(), newAction);
				} else {
					actions.put(ship.getId(), ship.getCurrentAction());
				}
				*/
			} else {
				actions.put(actionable.getId(), new DoNothingAction());
			}
		} 
		return actions;
	}

	/**
	 * Find the base for this team nearest to this ship
	 * 
	 * @param space
	 * @param ship
	 * @return
	 */
	private Base findNearestBase(Toroidal2DPhysics space, Ship ship) {
		double minDistance = Double.MAX_VALUE;
		Base nearestBase = null;
		
		//Loop through the bases to find the nearest one.
		for (Base base : space.getBases()) {
			//Check to see if its our base
			if (base.getTeamName().equalsIgnoreCase(ship.getTeamName())) {
				//Calculate the distance
				double dist = space.findShortestDistance(ship.getPosition(), base.getPosition());
				if (dist < minDistance) {
					minDistance = dist;
					nearestBase = base;
				}
			}
		}
		return nearestBase;
	}

	/**
	 * Returns the asteroid of highest value that isn't already being chased by this team
	 * 
	 * @return
	 */
	private Asteroid pickHighestValueFreeAsteroid(Toroidal2DPhysics space, Ship ship) {
		Set<Asteroid> asteroids = space.getAsteroids();
		int bestMoney = Integer.MIN_VALUE;
		Asteroid bestAsteroid = null;

		//Loop through each asteroids in Space
		for (Asteroid asteroid : asteroids) {
			//Is the asteroid a goal
			if (!asteroidToShipMap.containsKey(asteroid)) {
				//Is the asteroid mineable, if yes, see if it has more money
				if (asteroid.isMineable() && asteroid.getMoney() > bestMoney) {
					bestMoney = asteroid.getMoney();
					bestAsteroid = asteroid;
				}
			}
		}
		//System.out.println("Best asteroid has " + bestMoney);
		return bestAsteroid;
	}

	/**
	 * Find the nearest beacon to this ship
	 * @param space
	 * @param ship
	 * @return
	 */
	private Beacon pickNearestBeacon(Toroidal2DPhysics space, Ship ship) {
		// get the current beacons
		Set<Beacon> beacons = space.getBeacons();

		Beacon closestBeacon = null;
		double bestDistance = Double.POSITIVE_INFINITY;

		//Loop though the beacons in space
		for (Beacon beacon : beacons) {
			//If the distance to the new beacon is closer than the previous, set closest beacon
			double dist = space.findShortestDistance(ship.getPosition(), beacon.getPosition());
			if (dist < bestDistance) {
				bestDistance = dist;
				closestBeacon = beacon;
			}
		}

		return closestBeacon;
	}

	/**
	 * Find nearest mineable asteroid to this ship
	 * @param space
	 * @param ship
	 * @return
	 */
	private Asteroid pickNearestMineableAsteroid(Toroidal2DPhysics space, Ship ship){
		// get the current asteroids
		Set<Asteroid> asteroids = space.getAsteroids();

		Asteroid closestAsteroid = null;
		double bestDistance = Double.POSITIVE_INFINITY;

		//Loop though the beacons in space
		for (Asteroid asteroid : asteroids) {
			if(!asteroid.isMineable()) continue;//don't consider the asteroid if it is not mineable
			//If the distance to the new beacon is closer than the previous, set closest beacon
			double dist = space.findShortestDistance(ship.getPosition(), asteroid.getPosition());
			if (dist < bestDistance) {
				bestDistance = dist;
				closestAsteroid = asteroid;
			}
		}

		return closestAsteroid;

	}

	/**
	 * Uses knowledge representation to choose an optimal action considering the current state
	 * @param space
	 * @param ship
	 * @return the optimal spacewar object to move to 
	 */
	private SpacewarObject chooseActionUsingHeuristic(Toroidal2DPhysics space, Ship ship){
		
		ActionEvaluator eval = new ActionEvaluator(space, ship, knowledgeValues);
		
		Asteroid nearestA = pickNearestMineableAsteroid(space, ship);
		double asteroidValue = eval.evaluateMoveToAsteroid(nearestA);
		//System.out.println("Asteroid val: " + asteroidValue);
		
		Beacon nearestB = pickNearestBeacon(space, ship);
		double beaconValue = eval.evaluateBeaconMove(nearestB);
		//System.out.println("Beacon val: " + beaconValue);
		
		//not purchasing bases yet
		//double basePurValue = eval.evaluateBasePurchase();
		//System.out.println("base purchase val: " + basePurValue);
		
		Base nearestBase = findNearestBase(space, ship);
		double baseRetVal = eval.evaluateReturnToBase(nearestBase);
		//System.out.println("base return val: " + baseRetVal);
		
		double maxVal = Math.max(asteroidValue, Math.max(beaconValue, baseRetVal));
		
		if(maxVal == asteroidValue){
			//System.out.println("Moving to nearest asteroid");
			return nearestA;
		}
		else if(maxVal == beaconValue){
			//System.out.println("Moving to nearest beacon");
			return nearestB;
		}
		else if(maxVal == baseRetVal){
			//System.out.println("Moving to nearest base");
			return nearestBase;
		}
		
		return null;
		
	}

	@Override
	public void getMovementEnd(Toroidal2DPhysics space, Set<SpacewarActionableObject> actionableObjects) {
		ArrayList<Asteroid> finishedAsteroids = new ArrayList<Asteroid>();

		//Loop through the asteroid to ship map and get the money from the ones we mined
		for (UUID asteroidId : asteroidToShipMap.keySet()) {
			Asteroid asteroid = (Asteroid) space.getObjectById(asteroidId);
			if (!asteroid.isAlive()) {
				finishedAsteroids.add(asteroid);
			}
		}

		//Mark mined asteroids as finished and remove from space
		for (Asteroid asteroid : finishedAsteroids) {
			asteroidToShipMap.remove(asteroid);
		}


	}

	/**
	 * Initializes the ships turn on the first step
	 */
	@Override
	public void initialize(Toroidal2DPhysics space) {
		asteroidToShipMap = new HashMap<UUID, Ship>();
		astarGraph = new Graph();
		
		//set the Chromosome based on the state of the genetic algorithm
		Chromosome thisChrom = geneticAlgorithm.getChromosomeForKnowledgeRepresentation();
		knowledgeValues = new KnowledgeValues(thisChrom);
		
	}

	/**
	 * Shuts down any objests on game closure
	 */
	@Override
	public void shutDown(Toroidal2DPhysics space) {
		
		//perform final tasks of the genetic algorithm before shutting down
		geneticAlgorithm.analyzeResults(space);
		
	}

	@Override
	public Set<Shadow> getShadows() {
		return null;
	}

	@Override
	/**
	 * If there is enough money, buy a base.  Place it by finding a ship that is sufficiently
	 * far away from the existing bases
	 */
	public Map<UUID, SpacewarPurchaseEnum> getTeamPurchases(Toroidal2DPhysics space,
			Set<SpacewarActionableObject> actionableObjects, int availableMoney, Map<SpacewarPurchaseEnum, Integer> purchaseCosts) {

		HashMap<UUID, SpacewarPurchaseEnum> purchases = new HashMap<UUID, SpacewarPurchaseEnum>();
		
		return purchases;
	}

	/**
	 * The asteroid collector doesn't use power ups but the weapons one does (at random)
	 * @param space
	 * @param actionableObjects
	 * @return
	 */
	@Override
	public Map<UUID, SpacewarPowerupEnum> getPowerups(Toroidal2DPhysics space,
			Set<SpacewarActionableObject> actionableObjects) {
		return null;
	}
	
	/**
	 * Get the Knowledge Representation values (Boolean values) and store them with the key
	 * that represents each characteristic of the environment. 
	 * 
	 * @param space
	 * @param ship
	 * @param costOfBase
	 * @return Map<KnowledgeEnum, Boolean>
	 */
	/*
	public Map<KnowledgeEnum, Boolean> getKnowledgeValues(Toroidal2DPhysics space, Ship ship, int costOfBase){
		HashMap<KnowledgeEnum, Boolean> values = new HashMap<KnowledgeEnum, Boolean>();
		
		//Call the values from the KnowledgeValues class, Each of these functions is a static function
		values.put(KnowledgeEnum.Low_On_Fuel, KnowledgeValues.isLowOnFuel(space, ship));
		values.put(KnowledgeEnum.Beacon_Nearby, KnowledgeValues.isNearbyBeacon(space, ship));
		values.put(KnowledgeEnum.Ship_Nearby, KnowledgeValues.isNearbyEnemyShip(space, ship));
		values.put(KnowledgeEnum.Nearby_Minable_Asteroid, KnowledgeValues.isNearbyMineableAsteroid(space, ship));
		values.put(KnowledgeEnum.Nearby_Base, KnowledgeValues.isNearbyBase(space, ship));
		values.put(KnowledgeEnum.Need_Buy_Base, KnowledgeValues.needToBuyBase(space, ship, costOfBase));
		values.put(KnowledgeEnum.Need_Buy_Bullets, KnowledgeValues.needToBuyBullets(space, ship));
		values.put(KnowledgeEnum.Enough_Buy_Base, KnowledgeValues.hasEnoughMoneyToBuyBase(space, ship, costOfBase));
		values.put(KnowledgeEnum.Need_To_Return_To_Base, KnowledgeValues.hasLargeMoneySum(space, ship));
		
		//Return the map for use
		return values;
	}
	*/

}
