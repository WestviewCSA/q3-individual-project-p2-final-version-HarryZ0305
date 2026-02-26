import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		mapBasedInput("Hard Map 2");

	}
	
	public static void mapBasedInput(String mapName) {
		File map1 = new File(mapName);
		try {
			Scanner map1Scan = new Scanner(map1);
			
			//The size of the 3D array
			int row = Integer.parseInt(map1Scan.next());
			int column = Integer.parseInt(map1Scan.next());
			int level = Integer.parseInt(map1Scan.next());
			
			//The 3D array
			String[][][] map = new String[level][row][column];
			
			//Testing the size
			System.out.print(row);
			System.out.print(column);
			System.out.println(level);
			
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
			
			//Testing by printing each value
			for(int z = 0; z < level; z++) {
				for(int i = 0; i < row; i++) {
					for(int j = 0; j < column; j++) {
						System.out.print(map[z][i][j] + " ");
					}
					System.out.println();
				}
				System.out.println();
			}
			
			//Testing the size
			System.out.println("Total # of values: "+ (map.length * map[0].length * map[0][0].length));
			System.out.println("Check whether it is same as Level * Row * Column: " + (level * row * column));
			
			
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
			
			//Testing the size
			System.out.print(row);
			System.out.print(column);
			System.out.println(level);
			
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
			
			//Testing by printing each value
			for(int z = 0; z < level; z++) {
				for(int i = 0; i < row; i++) {
					for(int j = 0; j < column; j++) {
						System.out.print(map[z][i][j] + " ");
					}
					System.out.println();
				}
				System.out.println();
			}
			
			//Testing the size
			System.out.println("Total # of values: "+ (map.length * map[0].length * map[0][0].length));
			System.out.println("Check whether it is same as Level * Row * Column: " + (level * row * column));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	

	
}
