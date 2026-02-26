import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		try {
			mapBasedInput("Hard Map 1");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//Checking if the characters are valid characters
	public static boolean isValidChar(char c) {
		String validChars = "W@.|$";
		return validChars.indexOf(c) != -1;
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
			
			for(int z = 0; z < level; z++) {
				for(int i = 0; i < row; i++) {
					for(int j = 0; j < column; j++) {
						System.out.print(map[z][i][j]);;
					}
					System.out.println();
				}
				System.out.println();
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
