import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		mapBasedInput("Medium Map 2");

	}
	
	public static void mapBasedInput(String mapName) {
		File map1 = new File(mapName);
		try {
			Scanner map1Scan = new Scanner(map1);
			
			int row = Integer.parseInt(map1Scan.next());
			int column = Integer.parseInt(map1Scan.next());
			int level = Integer.parseInt(map1Scan.next());
			
			
			String[][][] map = new String[level][row][column];
			
			System.out.print(row);
			System.out.print(column);
			System.out.println(level);
			
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

					rowCount++;
					
					if(rowCount == row) {
						break;
					}
				}
			}
			
			
			for(int z = 0; z < level; z++) {
				for(int i = 0; i < row; i++) {
					for(int j = 0; j < column; j++) {
						System.out.println(map[z][i][j]);
					}
				}
			}
			
			
			System.out.println("Total # of values: "+ (map.length * map[0].length * map[0][0].length));
			System.out.println("Check whether it is same as Level * Row * Column: " + (level * row * column));
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	

	
}
