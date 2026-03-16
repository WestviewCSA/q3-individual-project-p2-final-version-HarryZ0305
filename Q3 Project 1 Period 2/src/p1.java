import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class p1 {

	//Store the coordinate path
    public static Stack<String> path = new Stack<>();
	
	public static void main(String[] args) {
	    
		//Command Line variables
		boolean useStack = false;
	    boolean useQueue = false;
	    boolean useOpt = false;
	    boolean inCoord = false;
	    boolean outCoord = false;
	    boolean showTime = false;
	    int routingMethodsCount = 0; //Only 1 search type
		
		try {
			//Check if arguments were provided
	        if(args.length == 0) {
	            throw new IllegalCommandLineInputsException("No arguments provided.");
	        }

	        //Loop through all arguments except file name
	        for(int i = 0; i < args.length - 1; i++) {
	            String arg = args[i];
	            
	            if(arg.equals("--Help")) {
	                System.out.println("Switches:");
	                System.out.println("--Stack, --Queue, --Opt : Choose ONE routing method.");
	                System.out.println("--Incoordinate : Input is coordinate format.");
	                System.out.println("--Outcoordinate : Output is coordinate format.");
	                System.out.println("--Time : Print runtime.");
	                System.exit(0);
	            } 
	            else if(arg.equals("--Stack")) { 
	            	useStack = true; 
	            	routingMethodsCount++; 
	            }
	            else if(arg.equals("--Queue")) { 
	            	useQueue = true; 
	            	routingMethodsCount++; 
	            }
	            else if(arg.equals("--Opt")) { 
	            	useOpt = true; 
	            	routingMethodsCount++; 
	            }
	            else if(arg.equals("--Incoordinate")) { 
	            	inCoord = true; 
	            }
	            else if(arg.equals("--Outcoordinate")) { 
	            	outCoord = true; 
	            }
	            else if(arg.equals("--Time")) { 
	            	showTime = true; 
	            }
	        }
	        
	        //Check the amount of search switches
	        if (routingMethodsCount != 1) {
	            System.err.println("Error: You must specify exactly one of --Stack, --Queue, or --Opt.");
	            throw new IllegalCommandLineInputsException("Invalid number of routing modes selected.");
	        }
			
	        //File name is the last argument
	        String fileName = args[args.length - 1];

	        //Build map to either coord-based or map-based on input switch
	        String[][][] map;
	        if(inCoord) {
	        	map = coordBasedInput(fileName);
	        }
	        else {
	            map = mapBasedInput(fileName);
	        }

	        //Start timer
	        long timeStart = System.nanoTime();
	        
	        //Run the search based on the routing switch
	        if(useStack) {
	            stackBasedSearch(map);
	        }
	        else if(useQueue) {
	            queueBasedSearch(map);
	        }
	        else if(useOpt) {
	        	optimalSearch(map);
	        }
	        
	        //Stop timer
	        long timeEnd = System.nanoTime();

	        //Output
	        if(path.isEmpty()) {
	            //The search methods already printed "The Wolverine Store is closed."
	        }
	        else if(outCoord) {
	            //Pop the stack to print the coordinates
	            while (!path.isEmpty()) {
	                System.out.println(path.pop());
	            }
	        }
	        else {
	            //Map output
	            for(int z = 0; z < map.length; z++) {
	                for(int i = 0; i < map[z].length; i++) {
	                    for(int j = 0; j < map[z][i].length; j++) {
	                        System.out.print(map[z][i][j]);
	                    }
	                    System.out.println();
	                }
	            }   
	        }
	        
	        //Print the runtime
	        if(showTime) {
	            double timeDuration = (timeEnd - timeStart) / 1000000000.0; //double in seconds
	            System.out.println("Total Runtime: " + timeDuration + " seconds");
	        }

	    } catch (IllegalCommandLineInputsException e) {
	        System.exit(-1);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//Checking if the characters are valid characters
	public static boolean isValidChar(char c) {
		String validChars = "W@.|$";
		return validChars.indexOf(c) != -1;
	}
	
	public static String[][][] mapBasedInput(String mapName) throws Exception {
		File map1 = new File(mapName);
		Scanner map1Scan = null;
		try {
			map1Scan = new Scanner(map1);
			
			//Checking and getting the dimensions of the map
			if (!map1Scan.hasNextInt()) {
                throw new IncorrectMapFormatException("File does not start with a number for the amount of rows");
            }
			int row = map1Scan.nextInt();
			
			if (!map1Scan.hasNextInt()) {
                throw new IncorrectMapFormatException("File does not start with a number for the amount of columns");
            }
			int column = map1Scan.nextInt();
			
			if (!map1Scan.hasNextInt()) {
                throw new IncorrectMapFormatException("File does not start with a number for the amount of levels");
            }
			int level = map1Scan.nextInt();
			
			//Skipping everything else on the first line
			if (map1Scan.hasNextLine()) {
				map1Scan.nextLine();
			}
			
			//Check if the parameters for the map are positive non-zero numbers
			if (row <= 0 || column <= 0 || level <= 0) {
                throw new IncorrectMapFormatException("Map dimensions must be positive non-zero numbers.");
            }
			
			//The 3D array
			String[][][] map = new String[level][row][column];
			
			int levelsProcessed = 0;
			int rowsProcessed = 0;
			
			//Adding the characters to the array
			while(map1Scan.hasNextLine() && levelsProcessed < level) {
				//Take the entire line
				String line = map1Scan.nextLine();
				
				//Check if the line length is the supposed length or more
				if(line.length() < column) {
					throw new IncompleteMapException("Line " + (rowsProcessed + 1) + " on level " + (levelsProcessed + 1) + " does not have enough characters");
				}
				
				//Adding the characters to the array
				for(int i = 0; i < column; i++) {
					//Take each character of the line separately
					char c = line.charAt(i);
					
					//Check if the character is valid
					if(!isValidChar(c)) {
						throw new IllegalMapCharacterException("The character on line " + (rowsProcessed + 1) + ", column " + (i + 1) + ", level " + (levelsProcessed + 1) + " is an illegal character.");
					}
					
					//Adding to the array
					map[levelsProcessed][rowsProcessed][i] = String.valueOf(c);
				}
				
				rowsProcessed++;
				
				//After filling one 2D array for one level move to the next
				if(rowsProcessed == row) {
					levelsProcessed++;
					rowsProcessed = 0;
				}
			}
			
			//Check for number of rows
			if(levelsProcessed < level) {
				throw new IncompleteMapException("The map does not have enough lines");
			}
		
			return map;
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (map1Scan != null) {
				map1Scan.close();
			}
		}
	}
	
	public static String[][][] coordBasedInput(String mapName) throws Exception {
		File map1 = new File(mapName);
		Scanner map1Scan = null;
		try {
			map1Scan = new Scanner(map1);
			
			//Check for three positive non-zero numbers
			if (!map1Scan.hasNextInt()) {
				throw new IncorrectMapFormatException("File does not start with a number for the amount of rows");
			}
			int row = map1Scan.nextInt();
			
			if (!map1Scan.hasNextInt()) {
				throw new IncorrectMapFormatException("File does not start with a number for the amount of columns");
			}
			int column = map1Scan.nextInt();
			
			if (!map1Scan.hasNextInt()) {
				throw new IncorrectMapFormatException("File does not start with a number for the amount of levels");
			}
			int level = map1Scan.nextInt();
			
			if (row <= 0 || column <= 0 || level <= 0) {
				throw new IncorrectMapFormatException("Map dimensions must be positive non-zero numbers.");
			}
			
			//The 3D array
			String[][][] map = new String[level][row][column];
			
			//Put the values into the array
			while(map1Scan.hasNext()) {
				String symbol = map1Scan.next();
				
				//Check for illegal characters in the first column
				if (!isValidChar(symbol.charAt(0)) || symbol.length() != 1) {
					throw new IllegalMapCharacterException("The character " + symbol + " is an illegal character.");
				}
				
				int rowWhere = Integer.parseInt(map1Scan.next());
				int columnWhere = Integer.parseInt(map1Scan.next());
				int levelWhere = Integer.parseInt(map1Scan.next());
				
				//Check for coordinates that don't fit inside the maze
				if (rowWhere < 0 || rowWhere >= row || columnWhere < 0 || columnWhere >= column || levelWhere < 0 || levelWhere >= level) {
					throw new IncorrectMapFormatException("Coordinates out of bounds: (" + rowWhere + ", " + columnWhere + ", " + levelWhere + ")");
				}
				
				map[levelWhere][rowWhere][columnWhere] = symbol;
			}
			
			//Fill the rest with "."
			for(int z = 0; z < level; z++) {
				for(int i = 0; i < row; i++) {
					for(int j = 0; j < column; j++) {
						if(map[z][i][j] == null) {
							map[z][i][j] = ".";
						}
					}
				}
			}
			
			return map;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (map1Scan != null) {
				map1Scan.close();
			}
		}
	}
	
	public static void queueBasedSearch(String[][][] map) {
		//The dimensions of the map
		int levels = map.length;
		int rows = map[0].length;
		int columns = map[0][0].length;
		
		//The queue
		Queue<Location> queue = new LinkedList<>();
		
		//3D array to store visited tiles, default false
		boolean[][][] visited = new boolean[levels][rows][columns];
		
		//Array to store the row and column of the W for each level
		int[][] levelSpawns = new int[levels][2];
		Location startW = null;
		
		//Find all W locations
		for(int i = 0; i < levels; i++) {
			for(int j = 0; j < rows; j++) {
				for(int z = 0; z < columns; z++) {
					if(map[i][j][z].equals("W")) {
						//Record the spawn coordinates for this specific level
						levelSpawns[i][0] = j; //row
						levelSpawns[i][1] = z; //column
						
						//Wolverine starting tile
						if (i == 0) {
							startW = new Location(j, z, i, null); 
							visited[i][j][z] = true; 
							queue.add(startW); 
						}
					}
				}
			}
		}
		
		//Check if W exists on level 1
		if(startW == null) {
			System.out.println("The Wolverine Store is closed.");
			return;
		}
		
		while(!queue.isEmpty()) {
			Location current = queue.remove(); //Dequeue next location
			
			int currentLevel = current.level;
			int currentRow = current.row;
			int currentColumn = current.column;
			
			//Check if it contains $
			if(map[currentLevel][currentRow][currentColumn].equals("$")) {
							
				//$ does not get replaced by +
				Location trace = current.prev;
							
				//Since only the starting W has a prev of null, and we don't want the first W to be replaced by +, while loop is usable
				while(trace.prev != null) {
				    if (map[trace.level][trace.row][trace.column].equals(".")) {
				        map[trace.level][trace.row][trace.column] = "+";
				        
				        //Push the coordinate to global stack
				        path.push("+ " + trace.row + " " + trace.column + " " + trace.level);
				    }
				    trace = trace.prev;
				}

				return; 
			}
			
			//Check if it contains |
			if(map[currentLevel][currentRow][currentColumn].equals("|")) {
				int nextLevel = currentLevel + 1;
				
				//Check if next level exists
				if(nextLevel < levels) {
					//Find the position of W in the new level
					int spawnRow = levelSpawns[nextLevel][0];
					int spawnColumn = levelSpawns[nextLevel][1];
					
					//Check if the tile has been visited
					if (!visited[nextLevel][spawnRow][spawnColumn]) {
						visited[nextLevel][spawnRow][spawnColumn] = true;
						Location nextSpawn = new Location(spawnRow, spawnColumn, nextLevel, current);
						queue.add(nextSpawn);
					}
				}
				//Already finished this cycle
				continue; 
			}
			
			//Add north, check for bounds, obstacle, and visited
			if(current.row - 1 >= 0 && !map[currentLevel][currentRow - 1][currentColumn].equals("@") && !visited[currentLevel][currentRow - 1][currentColumn]) {
				visited[currentLevel][currentRow - 1][currentColumn] = true; //set to visited
				Location north = new Location(currentRow - 1, currentColumn, currentLevel, current); //create new location for the next step
				queue.add(north); //enqueue the location
			}
			
			//Add south
			if(current.row + 1 < rows && !map[currentLevel][currentRow + 1][currentColumn].equals("@") && !visited[currentLevel][currentRow + 1][currentColumn]) {
				visited[currentLevel][currentRow + 1][currentColumn] = true;
				Location south = new Location(currentRow + 1, currentColumn, currentLevel, current);
				queue.add(south);
			}
			
			//Add east
			if(current.column + 1 < columns && !map[currentLevel][currentRow][currentColumn + 1].equals("@") && !visited[currentLevel][currentRow][currentColumn + 1]) {
				visited[currentLevel][currentRow][currentColumn + 1] = true;
				Location east = new Location(currentRow, currentColumn + 1, currentLevel, current);
				queue.add(east);
			}
			
			//Add west
			if(current.column - 1 >= 0 && !map[currentLevel][currentRow][currentColumn - 1].equals("@") && !visited[currentLevel][currentRow][currentColumn - 1]) {
				visited[currentLevel][currentRow][currentColumn - 1] = true;
				Location west = new Location(currentRow, currentColumn - 1, currentLevel, current);
				queue.add(west);
			}	
		}
		
		//If queue empties, $ is unreachable
		System.out.println("The Wolverine Store is closed.");
	}
	
	public static void stackBasedSearch(String[][][] map) {
		//The dimensions of the map
		int levels = map.length;
		int rows = map[0].length;
		int columns = map[0][0].length;
		
		//The stack
		Stack<Location> stack = new Stack<>();
		
		//3D array to store visited tiles, default false
		boolean[][][] visited = new boolean[levels][rows][columns];
		
		//Array to store the row and column of the W for each level
		int[][] levelSpawns = new int[levels][2];
		Location startW = null;
		
		//Find all W locations
		for(int i = 0; i < levels; i++) {
			for(int j = 0; j < rows; j++) {
				for(int z = 0; z < columns; z++) {
					if(map[i][j][z].equals("W")) {
						//Record the spawn coordinates for this specific level
						levelSpawns[i][0] = j; //row
						levelSpawns[i][1] = z; //column
						
						//Wolverine starting tile
						if (i == 0) {
							startW = new Location(j, z, i, null); 
							visited[i][j][z] = true; 
							stack.push(startW); 
						}
					}
				}
			}
		}
		
		//Check if W exists on level 1
		if(startW == null) {
			System.out.println("The Wolverine Store is closed.");
			return;
		}
		
		while(!stack.isEmpty()) {
			Location current = stack.pop(); //Pop next location
			
			int currentLevel = current.level;
			int currentRow = current.row;
			int currentColumn = current.column;
			
			//Check if it contains $
			if(map[currentLevel][currentRow][currentColumn].equals("$")) {
							
				//$ does not get replaced by +
				Location trace = current.prev;
							
				//Since only the starting W has a prev of null, and we don't want the first W to be replaced by +, while loop is usable
				while(trace.prev != null) {
				    if (map[trace.level][trace.row][trace.column].equals(".")) {
				        map[trace.level][trace.row][trace.column] = "+";
				        
				        //Push the coordinate to global stack
				        path.push("+ " + trace.row + " " + trace.column + " " + trace.level);
				    }
				    trace = trace.prev;
				}

				return; 
			}
			
			//Check if it contains |
			if(map[currentLevel][currentRow][currentColumn].equals("|")) {
				int nextLevel = currentLevel + 1;
				
				//Check if next level exists
				if(nextLevel < levels) {
					//Find the position of W in the new level
					int spawnRow = levelSpawns[nextLevel][0];
					int spawnColumn = levelSpawns[nextLevel][1];
					
					//Check if the tile has been visited
					if (!visited[nextLevel][spawnRow][spawnColumn]) {
						visited[nextLevel][spawnRow][spawnColumn] = true;
						Location nextSpawn = new Location(spawnRow, spawnColumn, nextLevel, current);
						stack.push(nextSpawn);
					}
				}
				//Already finished this cycle
				continue; 
			}
			//Add west, check for bounds, obstacle, and visited
			if(current.column - 1 >= 0 && !map[currentLevel][currentRow][currentColumn - 1].equals("@") && !visited[currentLevel][currentRow][currentColumn - 1]) {
				visited[currentLevel][currentRow][currentColumn - 1] = true;
				Location west = new Location(currentRow, currentColumn - 1, currentLevel, current);
				stack.push(west);
			}	
			
			//Add east
			if(current.column + 1 < columns && !map[currentLevel][currentRow][currentColumn + 1].equals("@") && !visited[currentLevel][currentRow][currentColumn + 1]) {
				visited[currentLevel][currentRow][currentColumn + 1] = true;
				Location east = new Location(currentRow, currentColumn + 1, currentLevel, current);
				stack.push(east);
			}

			//Add south
			if(current.row + 1 < rows && !map[currentLevel][currentRow + 1][currentColumn].equals("@") && !visited[currentLevel][currentRow + 1][currentColumn]) {
				visited[currentLevel][currentRow + 1][currentColumn] = true;
				Location south = new Location(currentRow + 1, currentColumn, currentLevel, current);
				stack.push(south);
			}
			
			//Add north
			if(current.row - 1 >= 0 && !map[currentLevel][currentRow - 1][currentColumn].equals("@") && !visited[currentLevel][currentRow - 1][currentColumn]) {
				visited[currentLevel][currentRow - 1][currentColumn] = true; //set to visited
				Location north = new Location(currentRow - 1, currentColumn, currentLevel, current); //create new location for the next step
				stack.push(north); //enqueue the location
			}
		}
		
		//If stack empties, $ is unreachable
		System.out.println("The Wolverine Store is closed.");
	}
	
	public static void optimalSearch(String[][][] map) {
		//The dimensions of the map
		int levels = map.length;
		int rows = map[0].length;
		int columns = map[0][0].length;
		
		//The priority queue 
		PriorityQueue<Location> priorityQueue = new PriorityQueue<>();
		boolean[][][] visited = new boolean[levels][rows][columns];
		
		int[][] levelSpawns = new int[levels][2];
		Location startW = null;
		
		//Variables to store $ location
		int targetLevel = -1;
		int targetRow = -1;
		int targetCol = -1;
		
		//Find all W and the $
		for(int i = 0; i < levels; i++) {
			for(int j = 0; j < rows; j++) {
				for(int z = 0; z < columns; z++) {
					if(map[i][j][z].equals("W")) {
						levelSpawns[i][0] = j; 
						levelSpawns[i][1] = z; 
						if (i == 0) {
							startW = new Location(j, z, i, null); 
						}
					} 
					//Find $
					else if (map[i][j][z].equals("$")) {
						targetLevel = i;
						targetRow = j;
						targetCol = z;
					}
				}
			}
		}
		
		//Check if W or $ is missing
		if(startW == null || targetLevel == -1) {
			System.out.println("The Wolverine Store is closed.");
			return;
		}
		//Create starting location's heuristic and add to queue
		startW.setHeuristic(targetRow, targetCol, targetLevel);
		visited[startW.level][startW.row][startW.column] = true; 
		priorityQueue.add(startW);
				
		//Search loop
		while(!priorityQueue.isEmpty()) {
			Location current = priorityQueue.poll(); //poll() removes the tile with the LOWEST total cost
					
			int currentLevel = current.level;
			int currentRow = current.row;
			int currentColumn = current.column;
					
			// Check if it contains $
			if(map[currentLevel][currentRow][currentColumn].equals("$")) {
				Location trace = current.prev;
				while(trace.prev != null) {
					if (map[trace.level][trace.row][trace.column].equals(".")) {
						map[trace.level][trace.row][trace.column] = "+";
						path.push("+ " + trace.row + " " + trace.column + " " + trace.level);
					}
					trace = trace.prev;
				}
				return;
			}
				
			//Check for |
			if(map[currentLevel][currentRow][currentColumn].equals("|")) {
				int nextLevel = currentLevel + 1;
				if(nextLevel < levels) {
					int spawnRow = levelSpawns[nextLevel][0];
					int spawnColumn = levelSpawns[nextLevel][1];
							
					if (!visited[nextLevel][spawnRow][spawnColumn]) {
						visited[nextLevel][spawnRow][spawnColumn] = true;
						Location nextSpawn = new Location(spawnRow, spawnColumn, nextLevel, current);
								
						//Calculate heuristic for the new  spawn
						nextSpawn.setHeuristic(targetRow, targetCol, targetLevel);
						priorityQueue.add(nextSpawn);
					}
				}
				continue; 
			}
					
			//Surrounding with dictionary to improve speed
			int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
					
			for (int[] dir : directions) {
				int nRow = currentRow + dir[0];
				int nCol = currentColumn + dir[1];
						
				//Check bounds, walls, and if visited
				if (nRow >= 0 && nRow < rows && nCol >= 0 && nCol < columns) {
					if (!map[currentLevel][nRow][nCol].equals("@") && !visited[currentLevel][nRow][nCol]) {
						visited[currentLevel][nRow][nCol] = true;
								
						Location nextTile = new Location(nRow, nCol, currentLevel, current);
								
						//Calculate distance to $
						nextTile.setHeuristic(targetRow, targetCol, targetLevel); 
						priorityQueue.add(nextTile);
					}
				}
			}
		}
				
		//If there is no $
		System.out.println("The Wolverine Store is closed.");
	}
}
