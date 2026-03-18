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
    //Whether there is a solution
    public static boolean solutionFound = false;

    public static void main(String[] args) {
        //Command Line variables
        boolean useStack = false;
        boolean useQueue = false;
        boolean useOpt = false;
        boolean inCoord = false;
        boolean outCoord = false;
        boolean showTime = false;
        int routingMethodsCount = 0;

        try {
            if (args.length == 0) {
                throw new IllegalCommandLineInputsException("No arguments provided.");
            }
            
            for (String arg : args) {
                if (arg.equalsIgnoreCase("--Help")) {
                	System.out.println("Switches:");
                    System.out.println("--Stack, --Queue, --Opt : Choose ONE routing method.");
                    System.out.println("--Incoordinate : Input is coordinate format.");
                    System.out.println("--Outcoordinate : Output is coordinate format.");
                    System.out.println("--Time : Print runtime.");
                    System.exit(0);
                }
            }
            
            for (int i = 0; i < args.length - 1; i++) {
                String arg = args[i];

                if (arg.equalsIgnoreCase("--Stack")) {
                    useStack = true;
                    routingMethodsCount++;
                } else if (arg.equalsIgnoreCase("--Queue")) {
                    useQueue = true;
                    routingMethodsCount++;
                } else if (arg.equalsIgnoreCase("--Opt")) {
                    useOpt = true;
                    routingMethodsCount++;
                } else if (arg.equalsIgnoreCase("--Incoordinate")) {
                    inCoord = true;
                } else if (arg.equalsIgnoreCase("--Outcoordinate")) {
                    outCoord = true;
                } else if (arg.equalsIgnoreCase("--Time")) {
                    showTime = true;
                }
            }

            if (routingMethodsCount != 1) {
                System.err.println("Error: You must specify exactly one of --Stack, --Queue, or --Opt.");
                throw new IllegalCommandLineInputsException("Invalid number of routing modes selected.");
            }

            String fileName = args[args.length - 1];

            //Map changed to char array for speed
            char[][][] map;
            if (inCoord) {
                map = coordBasedInput(fileName);
            } else {
                map = mapBasedInput(fileName);
            }

            long timeStart = System.nanoTime();

            if (useStack) {
                stackBasedSearch(map);
            } else if (useQueue) {
                queueBasedSearch(map);
            } else if (useOpt) {
                optimalSearch(map);
            }

            long timeEnd = System.nanoTime();

            if (!solutionFound) {
                //The search methods already printed "The Wolverine Store is closed."
            } else if (outCoord) {
                while (!path.isEmpty()) {
                    System.out.println(path.pop());
                }
            } else {
                for (int z = 0; z < map.length; z++) {
                    for (int i = 0; i < map[z].length; i++) {
                        for (int j = 0; j < map[z][i].length; j++) {
                            System.out.print(map[z][i][j]);
                        }
                        System.out.println();
                    }
                }
            }

            if (showTime) {
                double timeDuration = (timeEnd - timeStart) / 1000000000.0;
                System.out.println("Total Runtime: " + timeDuration + " seconds");
            }

        } catch (IllegalCommandLineInputsException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        } catch (IllegalMapCharacterException | IncompleteMapException | IncorrectMapFormatException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidChar(char c) {
        String validChars = "W@.|$";
        return validChars.indexOf(c) != -1;
    }

    public static char[][][] mapBasedInput(String mapName) throws Exception {
        File map1 = new File(mapName);
        Scanner map1Scan = null;
        try {
            map1Scan = new Scanner(map1);

            if (!map1Scan.hasNextInt()) throw new IncorrectMapFormatException("Missing rows");
            int row = map1Scan.nextInt();

            if (!map1Scan.hasNextInt()) throw new IncorrectMapFormatException("Missing cols");
            int column = map1Scan.nextInt();

            if (!map1Scan.hasNextInt()) throw new IncorrectMapFormatException("Missing levels");
            int level = map1Scan.nextInt();

            if (map1Scan.hasNextLine()) map1Scan.nextLine();

            if (row <= 0 || column <= 0 || level <= 0) {
                throw new IncorrectMapFormatException("Map dimensions must be positive non-zero numbers.");
            }

            char[][][] map = new char[level][row][column];
            int levelsProcessed = 0;
            int rowsProcessed = 0;

            while (map1Scan.hasNextLine() && levelsProcessed < level) {
                String line = map1Scan.nextLine();

                if (line.length() < column) {
                    throw new IncompleteMapException("Line " + (rowsProcessed + 1) + " on level " + (levelsProcessed + 1) + " does not have enough characters");
                }

                for (int i = 0; i < column; i++) {
                    char c = line.charAt(i);
                    if (!isValidChar(c)) {
                        throw new IllegalMapCharacterException("The character on line " + (rowsProcessed + 1) + ", column " + (i + 1) + ", level " + (levelsProcessed + 1) + " is an illegal character.");
                    }
                    map[levelsProcessed][rowsProcessed][i] = c;
                }

                rowsProcessed++;
                if (rowsProcessed == row) {
                    levelsProcessed++;
                    rowsProcessed = 0;
                }
            }

            if (levelsProcessed < level) {
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

    public static char[][][] coordBasedInput(String mapName) throws Exception {
        File map1 = new File(mapName);
        Scanner map1Scan = null;
        try {
            map1Scan = new Scanner(map1);

            if (!map1Scan.hasNextInt()) throw new IncorrectMapFormatException("Missing rows");
            int row = map1Scan.nextInt();

            if (!map1Scan.hasNextInt()) throw new IncorrectMapFormatException("Missing cols");
            int column = map1Scan.nextInt();

            if (!map1Scan.hasNextInt()) throw new IncorrectMapFormatException("Missing levels");
            int level = map1Scan.nextInt();

            if (row <= 0 || column <= 0 || level <= 0) {
                throw new IncorrectMapFormatException("Map dimensions must be positive non-zero numbers.");
            }

            char[][][] map = new char[level][row][column];

            while (map1Scan.hasNext()) {
            	String symbol = map1Scan.next();
                if (!isValidChar(symbol.charAt(0)) || symbol.length() != 1) {
                    throw new IllegalMapCharacterException("The character " + symbol + " is an illegal character.");
                }

                //Check for unexpected end of file before reading each coordinate
                if (!map1Scan.hasNext()) {
                    throw new IncompleteMapException("Coordinate map file ended prematurely: missing row.");
                }
                int rowWhere = Integer.parseInt(map1Scan.next());

                if (!map1Scan.hasNext()) {
                    throw new IncompleteMapException("Coordinate map file ended prematurely: missing column.");
                }
                int columnWhere = Integer.parseInt(map1Scan.next());

                if (!map1Scan.hasNext()) {
                    throw new IncompleteMapException("Coordinate map file ended prematurely: missing level.");
                }
                int levelWhere = Integer.parseInt(map1Scan.next());

                if (rowWhere < 0 || rowWhere >= row || columnWhere < 0 || columnWhere >= column || levelWhere < 0 || levelWhere >= level) {
                    throw new IncorrectMapFormatException("Coordinates out of bounds: (" + rowWhere + ", " + columnWhere + ", " + levelWhere + ")");
                }

                map[levelWhere][rowWhere][columnWhere] = symbol.charAt(0);
            }

            //Fill empty spaces with '.'
            for (int z = 0; z < level; z++) {
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < column; j++) {
                        //Default value for an empty char  element
                        if (map[z][i][j] == '\u0000') {
                            map[z][i][j] = '.';
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

    //Helper method to extract the traceback logic
    private static void buildPath(Location current, char[][][] map) {
        Location trace = current;
        while (trace.prev != null) {
            path.push("+ " + trace.row + " " + trace.column + " " + trace.level);
            if (map[trace.level][trace.row][trace.column] == '.') {
                map[trace.level][trace.row][trace.column] = '+';
            }
            trace = trace.prev;
        }
        solutionFound = true;
    }

    //Helper method to find the W on a specific level 
    private static Location findWOnLevel(char[][][] map, int level) {
        for (int j = 0; j < map[0].length; j++) {
            for (int z = 0; z < map[0][0].length; z++) {
                if (map[level][j][z] == 'W') {
                    return new Location(j, z, level, null);
                }
            }
        }
        return null;
    }

    public static void queueBasedSearch(char[][][] map) {
        int levels = map.length;
        int rows = map[0].length;
        int columns = map[0][0].length;

        Queue<Location> queue = new LinkedList<>();
        boolean[][][] visited = new boolean[levels][rows][columns];

        //Only scan level 0
        Location startW = findWOnLevel(map, 0);

        if (startW == null) {
            System.out.println("The Wolverine Store is closed.");
            return;
        }

        visited[startW.level][startW.row][startW.column] = true;
        queue.add(startW);

        while (!queue.isEmpty()) {
            Location current = queue.remove();

            int currentLevel = current.level;
            int currentRow = current.row;
            int currentColumn = current.column;

            //Check if stepped on a walkway
            if (map[currentLevel][currentRow][currentColumn] == '|') {
                int nextLevel = currentLevel + 1;
                if (nextLevel < levels) {
                    Location nextSpawn = findWOnLevel(map, nextLevel);
                    if (nextSpawn != null && !visited[nextLevel][nextSpawn.row][nextSpawn.column]) {
                        visited[nextLevel][nextSpawn.row][nextSpawn.column] = true;
                        
                        //Create a new location so the constructor calculates the distance
                        Location actualNextSpawn = new Location(nextSpawn.row, nextSpawn.column, nextLevel, current);
                        queue.add(actualNextSpawn);
                    }
                }
                continue;
            }

            //North
            if (currentRow - 1 >= 0 && map[currentLevel][currentRow - 1][currentColumn] != '@' && !visited[currentLevel][currentRow - 1][currentColumn]) {
                if (map[currentLevel][currentRow - 1][currentColumn] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow - 1][currentColumn] = true;
                queue.add(new Location(currentRow - 1, currentColumn, currentLevel, current));
            }

            //South
            if (currentRow + 1 < rows && map[currentLevel][currentRow + 1][currentColumn] != '@' && !visited[currentLevel][currentRow + 1][currentColumn]) {
                if (map[currentLevel][currentRow + 1][currentColumn] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow + 1][currentColumn] = true;
                queue.add(new Location(currentRow + 1, currentColumn, currentLevel, current));
            }

            //East
            if (currentColumn + 1 < columns && map[currentLevel][currentRow][currentColumn + 1] != '@' && !visited[currentLevel][currentRow][currentColumn + 1]) {
                if (map[currentLevel][currentRow][currentColumn + 1] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow][currentColumn + 1] = true;
                queue.add(new Location(currentRow, currentColumn + 1, currentLevel, current));
            }

            //West
            if (currentColumn - 1 >= 0 && map[currentLevel][currentRow][currentColumn - 1] != '@' && !visited[currentLevel][currentRow][currentColumn - 1]) {
                if (map[currentLevel][currentRow][currentColumn - 1] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow][currentColumn - 1] = true;
                queue.add(new Location(currentRow, currentColumn - 1, currentLevel, current));
            }
        }

        System.out.println("The Wolverine Store is closed.");
    }

    public static void stackBasedSearch(char[][][] map) {
        int levels = map.length;
        int rows = map[0].length;
        int columns = map[0][0].length;

        Stack<Location> stack = new Stack<>();
        boolean[][][] visited = new boolean[levels][rows][columns];

        Location startW = findWOnLevel(map, 0);

        if (startW == null) {
            System.out.println("The Wolverine Store is closed.");
            return;
        }

        visited[startW.level][startW.row][startW.column] = true;
        stack.push(startW);

        while (!stack.isEmpty()) {
            Location current = stack.pop();

            int currentLevel = current.level;
            int currentRow = current.row;
            int currentColumn = current.column;

            if (map[currentLevel][currentRow][currentColumn] == '|') {
                int nextLevel = currentLevel + 1;
                if (nextLevel < levels) {
                    Location nextSpawn = findWOnLevel(map, nextLevel);
                    if (nextSpawn != null && !visited[nextLevel][nextSpawn.row][nextSpawn.column]) {
                        visited[nextLevel][nextSpawn.row][nextSpawn.column] = true;
                        
                        // Create a new location so the constructor calculates the distance
                        Location actualNextSpawn = new Location(nextSpawn.row, nextSpawn.column, nextLevel, current);
                        stack.push(actualNextSpawn);
                    }
                }
                continue;
            }

            //West
            if (currentColumn - 1 >= 0 && map[currentLevel][currentRow][currentColumn - 1] != '@' && !visited[currentLevel][currentRow][currentColumn - 1]) {
                if (map[currentLevel][currentRow][currentColumn - 1] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow][currentColumn - 1] = true;
                stack.push(new Location(currentRow, currentColumn - 1, currentLevel, current));
            }

            //East
            if (currentColumn + 1 < columns && map[currentLevel][currentRow][currentColumn + 1] != '@' && !visited[currentLevel][currentRow][currentColumn + 1]) {
                if (map[currentLevel][currentRow][currentColumn + 1] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow][currentColumn + 1] = true;
                stack.push(new Location(currentRow, currentColumn + 1, currentLevel, current));
            }

            //South
            if (currentRow + 1 < rows && map[currentLevel][currentRow + 1][currentColumn] != '@' && !visited[currentLevel][currentRow + 1][currentColumn]) {
                if (map[currentLevel][currentRow + 1][currentColumn] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow + 1][currentColumn] = true;
                stack.push(new Location(currentRow + 1, currentColumn, currentLevel, current));
            }

            //North
            if (currentRow - 1 >= 0 && map[currentLevel][currentRow - 1][currentColumn] != '@' && !visited[currentLevel][currentRow - 1][currentColumn]) {
                if (map[currentLevel][currentRow - 1][currentColumn] == '$') {
                    buildPath(current, map); 
                    return;
                }
                visited[currentLevel][currentRow - 1][currentColumn] = true;
                stack.push(new Location(currentRow - 1, currentColumn, currentLevel, current));
            }
        }

        System.out.println("The Wolverine Store is closed.");
    }

    public static void optimalSearch(char[][][] map) {
        int levels = map.length;
        int rows = map[0].length;
        int columns = map[0][0].length;

        PriorityQueue<Location> priorityQueue = new PriorityQueue<>();

        int[][] walkways = new int[levels][2];
        Location startW = null;

        int targetLevel = -1;
        int targetRow = -1;
        int targetCol = -1;

        //Full scan
        for (int i = 0; i < levels; i++) {
            for (int j = 0; j < rows; j++) {
                for (int z = 0; z < columns; z++) {
                    if (map[i][j][z] == 'W' && i == 0) {
                        startW = new Location(j, z, i, null);
                    } 
                    else if (map[i][j][z] == '$') {
                        targetLevel = i;
                        targetRow = j;
                        targetCol = z;
                    } 
                    else if (map[i][j][z] == '|') {
                        walkways[i][0] = j;
                        walkways[i][1] = z;
                    }
                }
            }
        }

        if (startW == null || targetLevel == -1) {
            System.out.println("The Wolverine Store is closed.");
            return;
        }
        
        int[][][] costToReach = new int[levels][rows][columns];
        for(int i=0; i<levels; i++)
            for(int j=0; j<rows; j++)
                for(int k=0; k<columns; k++)
                    costToReach[i][j][k] = Integer.MAX_VALUE;

        costToReach[startW.level][startW.row][startW.column] = 0;

        //Initialize heuristics for starting position
        startW.setHeuristic(targetRow, targetCol, targetLevel, walkways);
        priorityQueue.add(startW);

        while (!priorityQueue.isEmpty()) {
            Location current = priorityQueue.poll();

            if (current.distance > costToReach[current.level][current.row][current.column]) {
                continue; //Skip stale node
            }
            
            int currentLevel = current.level;
            int currentRow = current.row;
            int currentColumn = current.column;

            //Wait to check for $ until after popped
          //Wait to check for $ until after popped
            if (map[currentLevel][currentRow][currentColumn] == '$') {
                buildPath(current.prev, map); 
                return;
            }

            if (map[currentLevel][currentRow][currentColumn] == '|') {
                int nextLevel = currentLevel + 1;
                if (nextLevel < levels) {
                    Location nextSpawn = findWOnLevel(map, nextLevel);
                    
                    int tentativeDistance = current.distance + 1;

                    if (nextSpawn != null && tentativeDistance < costToReach[nextLevel][nextSpawn.row][nextSpawn.column]) {
                        
                        costToReach[nextLevel][nextSpawn.row][nextSpawn.column] = tentativeDistance;
                        
                        //Create a new Location so the constructor calculates the distance
                        Location actualNextSpawn = new Location(nextSpawn.row, nextSpawn.column, nextLevel, current);
                        
                        actualNextSpawn.setHeuristic(targetRow, targetCol, targetLevel, walkways);
                        priorityQueue.add(actualNextSpawn);
                    }
                }
                continue;
            }

            int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

            for (int[] dir : directions) {
                int nRow = currentRow + dir[0];
                int nCol = currentColumn + dir[1];

                if (nRow >= 0 && nRow < rows && nCol >= 0 && nCol < columns) {
                	int tentativeDistance = current.distance + 1;

                	//Check if it's not a wall and if this new path is faster than any previous path
                	if (map[currentLevel][nRow][nCol] != '@' && tentativeDistance < costToReach[currentLevel][nRow][nCol]) {
                	    
                	    //Update the record for the fastest path
                	    costToReach[currentLevel][nRow][nCol] = tentativeDistance;
                	    
                	    Location nextTile = new Location(nRow, nCol, currentLevel, current);
                	    nextTile.setHeuristic(targetRow, targetCol, targetLevel, walkways);
                	    priorityQueue.add(nextTile);
                	}
                }
            }
        }

        System.out.println("The Wolverine Store is closed.");
    }
}