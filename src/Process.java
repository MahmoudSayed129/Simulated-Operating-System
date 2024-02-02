import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;
import java.io.IOException;


public class Process {
		 int id;
		 State state;
		 String input = "";
		 String read = "";
		 
		 public Process(int id, String path){
			 this.id = id;
			 state = State.READY;
		 }
		 public String toString() {
			 return "Process "+this.id;
		 }
}
