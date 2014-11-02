import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mario on 01/11/2014.
 */
public class Router {

    public static void main (String []args) throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Path to the file: ");
        String filePath = sc.nextLine();
        ArrayList<String> adyacentNodes = readFile(filePath);

    }

    public static ArrayList readFile (String path) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        ArrayList<String> nodes = new ArrayList<String>();
        if (reader != null) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                nodes.add(line);
            }
        }
        return nodes;
    }
}
