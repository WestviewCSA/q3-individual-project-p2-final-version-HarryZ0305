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
		if (previous == null) {
			this.distance = 0;
		} 
		else {
			this.distance = previous.distance + 1;
		}
	}
	
	//Calculates "Manhattan Distance" to the Buck
	public void setHeuristic(int targetRow, int targetCol, int targetLevel) {
		int rowDiff = Math.abs(this.row - targetRow);
		int colDiff = Math.abs(this.column - targetCol);
		int levelDiff = Math.abs(this.level - targetLevel) * 100; //More important if on a different level
		
		this.heuristic = rowDiff + colDiff + levelDiff;
	}

	//Calculates the f-cost
	public int getTotalCost() {
		return this.distance + this.heuristic;
	}

	//Tells the PriorityQueue how to sort the locations (lowest cost first)
	@Override
	public int compareTo(Location other) {
		return Integer.compare(this.getTotalCost(), other.getTotalCost());
	}
}