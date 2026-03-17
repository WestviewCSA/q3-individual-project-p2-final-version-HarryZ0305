public class Location implements Comparable<Location> {
	//variables for Wolverine's positions
	int row;
	int column;
	int level;
	Location prev; //the tile Wolverine came from
	
	int distance;  //g-cost: steps taken from the start
	int heuristic; //h-cost: estimated distance to $
	
	//constructor
	public Location(int rowParam, int columnParam, int levelParam, Location previous) {
		this.row = rowParam;
		this.column = columnParam;
		this.level = levelParam;
		this.prev = previous;
		
		//Calculate distance based on the previous tile
		if (prev == null) {
			this.distance = 0;
		} 
		else {
			this.distance = prev.distance + 1;
		}
	}
	
	//Calculates an "Admissible Heuristic" for A* Search using walkways
	public void setHeuristic(int targetRow, int targetCol, int targetLevel, int[][] walkways) {
		if (this.level == targetLevel) {
			//On the final floor: Manhattan distance to the $
			int rowDiff = Math.abs(this.row - targetRow);
			int colDiff = Math.abs(this.column - targetCol);
			this.heuristic = rowDiff + colDiff;
		} else {
			//On a different floor: Manhattan distance to the stairs (|) on THIS floor
			int stairRow = walkways[this.level][0];
			int stairCol = walkways[this.level][1];
			
			int distToStairs = Math.abs(this.row - stairRow) + Math.abs(this.column - stairCol);
			
			//Plus the cost of dropping down the remaining levels
			int levelDiff = Math.abs(this.level - targetLevel);
			
			this.heuristic = distToStairs + levelDiff;
		}
	}

	//Calculates the f-cost
	public int getTotalCost() {
		return this.distance + this.heuristic;
	}

	//Tells the PriorityQueue to sort by lowest cost first
	@Override
	public int compareTo(Location other) {
	    int myTotalCost = this.distance + this.heuristic;
	    int otherTotalCost = other.distance + other.heuristic;
	    return Integer.compare(myTotalCost, otherTotalCost);
	}
}