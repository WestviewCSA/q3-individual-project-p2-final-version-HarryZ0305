import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		mapBasedInput("Easy Map 2");

	}
	
	public static void mapBasedInput(String mapName) {
		File map1 = new File(mapName);
		try {
			Scanner map1Scan = new Scanner(map1);
			
			int row = Integer.parseInt(map1Scan.next());
			int column = Integer.parseInt(map1Scan.next());
			int level = Integer.parseInt(map1Scan.next());
			
			String[][] map = new String[row][column];
			
			System.out.print(row);
			System.out.print(column);
			System.out.println(level);
			
			int rowCount = 0;
			int colCount = 0;
			
			while(map1Scan.hasNext()) {
				String result = map1Scan.next();
				
				colCount = 0;
				
				for(int i = 0; i < result.length(); i++) {
					map[rowCount][colCount] = result.substring(i, i + 1);
					colCount++;
				}

				rowCount++;
			}
			
			for(int i = 0; i < row; i++) {
				for(int j = 0; j < column; j++) {
					System.out.println(map[i][j]);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	

	
}
