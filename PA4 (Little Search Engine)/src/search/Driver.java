package search;

import java.io.*;
import java.util.*;


public class Driver {
    public static void main(String[] args) throws IOException {
        LittleSearchEngine LSE = new LittleSearchEngine();
        Scanner sc = new Scanner(System.in);
        LSE.makeIndex("docs.txt", "noisewords.txt");
 
        System.out.print("First keyword: ");
        String keyword1 = sc.nextLine();
        System.out.print("Second keyword: ");
        String keyword2 = sc.nextLine();
        sc.close();
        System.out.println();
        System.out.println("Output: " + LSE.top5search(keyword1, keyword2));
    }
}
