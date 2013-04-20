package ohar8139;


import spacewar2.actions.DoNothingAction;
import spacewar2.actions.MoveAction;
import spacewar2.actions.SpacewarAction;
import spacewar2.objects.Ship;
import spacewar2.simulator.Toroidal2DPhysics;
import spacewar2.utilities.Vector2D;

/**
 * This action takes a path as input and outputs the primitive commands 
 * necessary to follow the path.  Path following is accomplished using pd-control.
 * @author Dr. McGovern
 *
 */
public class FollowPathAction {
	Vertex[] path;
	int currentVertex;
	boolean finishedShortAction;
	SpacewarAction lastCommand;

	public FollowPathAction() {
		path = null;
		currentVertex = -1;
		lastCommand = null;
	}

	public FollowPathAction (Vertex[] newPath) {
		path = newPath;
		currentVertex = 0;
	}

	public void followNewPath(Vertex[] newPath) {
		path = newPath;
		currentVertex = 0;
	}

	/**
	 * 
	 * @param state
	 * @param ship
	 * @return
	 */
	public SpacewarAction followPath(Toroidal2DPhysics state, Ship ship) {
		//System.out.println("Following path at current action " + currentVertex);

		// safety case:  break if we have a null path
		if (path == null || currentVertex < 0) {
			DoNothingAction doNothing = new DoNothingAction();
			lastCommand = doNothing;
		}
		
		if (lastCommand == null || lastCommand.isMovementFinished(state)) {
			currentVertex++;
			// force a replan every time a vertex is reached

			if (currentVertex >= path.length) {
				//System.out.println("Done!");
				DoNothingAction doNothing = new DoNothingAction();
				lastCommand = doNothing;
			} else {
				MoveAction command = new MoveAction(state, ship.getPosition(), path[currentVertex].getPosition());
			
				lastCommand = command;
			}
		}

		//System.out.println("Current command " + command);
		return lastCommand;
	}



}
