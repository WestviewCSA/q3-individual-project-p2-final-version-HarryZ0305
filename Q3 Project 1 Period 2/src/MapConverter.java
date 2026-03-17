import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class MapConverter {
    public static void main(String[] args) throws Exception {
        //Change to the file to convert
        Scanner scan = new Scanner(new File("test09"));
        
        //Change to  desired output file name
        PrintWriter out = new PrintWriter("test10"); 

        int rows = scan.nextInt();
        int cols = scan.nextInt();
        int levels = scan.nextInt();
        scan.nextLine(); //consume the rest of the line

        out.println(rows + " " + cols + " " + levels);

        for (int l = 0; l < levels; l++) {
            for (int r = 0; r < rows; r++) {
                String line = scan.nextLine();
                for (int c = 0; c < cols; c++) {
                    char ch = line.charAt(c);
                    //If it is NOT an empty space, write it to the coordinate map
                    if (ch != '.') {
                        //Using spaces to prevent double-digit collision
                        out.println(ch + " " + r + " " + c + " " + l);
                    }
                }
            }
        }
        
        out.close();
        scan.close();
        System.out.println("Conversion Complete!");
    }
}