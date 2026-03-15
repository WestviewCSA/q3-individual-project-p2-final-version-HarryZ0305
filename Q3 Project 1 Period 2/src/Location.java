public class Location {
	//variables for Wolverine's positions
	int row;
	int column;
	int level;
	Location prev; //the tile Wolverine came from
	
	//constructor
	public Location(int rowParam, int columnParam, int levelParam, Location previous) {
		this.row = rowParam;
		this.column = columnParam;
		this.level = levelParam;
		this.prev = previous;
	}
}
