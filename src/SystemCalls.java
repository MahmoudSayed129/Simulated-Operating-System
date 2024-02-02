import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SystemCalls {


	public  void printstring(String s) {
		System.out.println(s);
	}
	public  void printint(int i) {
		System.out.println(i);
	}

	public  String inputstring() {
		Scanner s = new Scanner(System.in);
		String n=s.nextLine();
		return n;
	}
	
	public void writeFile(PCB p, String [] arr, Memory memory)
	{
		BufferedWriter out = null;

		try {
		    FileWriter fstream = new FileWriter(memory.readFromMemory(p, arr[1])+".txt", true); 
		    out = new BufferedWriter(fstream);
		    out.write(memory.readFromMemory(p, arr[2]));
		}

		catch (IOException e) {
		    this.printstring("Error: " + e.getMessage());
		}

		finally {
		    if(out != null) {
		        try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
	}
		

	public  String readFile(PCB p, String [] arr, Memory memory) 
	{
		StringBuilder result=new StringBuilder();
        try {
            File file=new File(memory.readFromMemory(p, arr[1])+".txt");
            BufferedReader br=new BufferedReader(new FileReader(file));
            String st;
            while((st=br.readLine())!=null){
                if(result.length()!=0) result.append("\n");
                result.append(st);
            }
        }catch (Exception e){
            this.printstring("error at readFile");
        }
        String reString = result.toString()  != ""  ? result.toString(): "File is empty or does not exist";
        return reString;
	}
}
