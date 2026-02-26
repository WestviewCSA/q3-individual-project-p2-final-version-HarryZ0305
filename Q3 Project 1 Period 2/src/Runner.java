import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		//mapBasedInput("Hard Map 2");

	}
	
	public static void mapBasedInput(String mapName) throws Exception {
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
			
			if (row <= 0 || column <= 0 || level <= 0) {
                throw new IncorrectMapFormatException("Map dimensions must be positive non-zero numbers.");
            }
			
			//The 3D array
			String[][][] map = new String[level][row][column];
			
			
			//This loop is for each level
			for(int j = 0; j < level; j++) {
				int rowCount = 0;
				int colCount = 0;
				
				while(map1Scan.hasNext()) {
					String result = map1Scan.next();
					
					colCount = 0;
					
					for(int i = 0; i < result.length(); i++) {
						map[j][rowCount][colCount] = result.substring(i, i + 1);
						colCount++;
					}
					
					//New row
					rowCount++;
					
					//After one level end, break to the next level
					if(rowCount == row) {
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void coordBasedInput(String mapName) {
		File map1 = new File(mapName);
		try {
			Scanner map1Scan = new Scanner(map1);
			
			//The size of the 3D array
			int row = Integer.parseInt(map1Scan.next());
			int column = Integer.parseInt(map1Scan.next());
			int level = Integer.parseInt(map1Scan.next());
			
			//The 3D array
			String[][][] map = new String[level][row][column];
			
			//Put the values into the array
			while(map1Scan.hasNextLine()) {
				String symbol = map1Scan.next();
				int rowWhere = Integer.parseInt(map1Scan.next());
				int columnWhere = Integer.parseInt(map1Scan.next());
				int levelWhere = Integer.parseInt(map1Scan.next());
				
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static class IllegalCommandLineInputsException extends Exception {
        public IllegalCommandLineInputsException(String message) { 
        		super(message); 
        	}
    }

    static class IllegalMapCharacterException extends Exception {
        public IllegalMapCharacterException(String message) { 
        		super(message); 
        	}
    }

    static class IncompleteMapException extends Exception {
        public IncompleteMapException(String message) { 
        		super(message); 
        	}
    }

    static class IncorrectMapFormatException extends Exception {
        public IncorrectMapFormatException(String message) { 
        		super(message); 
        	}
    }
		
}
