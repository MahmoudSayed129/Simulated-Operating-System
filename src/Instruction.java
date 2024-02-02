import java.util.Queue;


public class Instruction {
	String [] inst;
	
	public static SystemCalls system = new SystemCalls();
	
	public Instruction(String [] inst) {
		this.inst = inst;
	}
	public String toString() {
		String result = "";
		for (int i = 0; i < inst.length - 1; i++) {
			result += inst[i] + " ";
		}
		result += inst[inst.length - 1];
		return result; 
	}
	public static void print(PCB p, String [] arr, Memory memory)
	{
		int varstart = (int) memory.memory[(p.processID - 1) * 5 + 4];
		int varend = (int) memory.memory[(p.processID - 1) * 5 + 4] - 3;
		for (int i = varstart; i >varend;  i--)
		{
			if(memory.memory[i] != null && arr[1].equals(((Pair) memory.memory[i]).x))
			{
				system.printstring(((Pair) memory.memory[i]).y);
			}
		}
	}
	public static void assign(PCB p, String [] arr, Memory memory)
	{
		switch (arr[2]) {
		case "input" : 
			system.printstring("Please enter a value :");
			String in = system.inputstring();
			p.input = in;
			int instidx = (int) memory.memory[(p.processID - 1) * 5 + 3] + p.PC;
			((Instruction)memory.memory[instidx]).inst[2] = "inputString";
			p.PC--;
			memory.memory[(p.processID - 1) * 5 + 2] = p.PC;
			break;
		case "readFile" :
			String [] res = {arr[2], arr[3]};
			String read = system.readFile(p, res, memory);
			p.read = read;
			int instidx2 = (int) memory.memory[(p.processID - 1) * 5 + 3] + p.PC;
			((Instruction)memory.memory[instidx2]).inst[2] = "readString";
			p.PC--;
			memory.memory[(p.processID - 1) * 5 + 2] = p.PC;
			break;
		case "inputString":
			memory.writedata(p, arr[1], p.input);
			break;
		
		case "readString":
			memory.writedata(p,arr[1], p.read);
			break;
		}
		
	}
	public static void printFromTo(PCB p, String [] arr,  Memory memory)
	{
		String a = "";
		String b = "";
		int x=0,y=0;
		try {
			int varstart = (int) memory.memory[(p.processID - 1) * 5 + 4];
			int varend = (int) memory.memory[(p.processID - 1) * 5 + 4] - 3;
			for (int i = varstart; i > varend; i--)
			{
				if(memory.memory[i] != null && arr[1].equals(((Pair) memory.memory[i]).x))
					a = ((Pair) memory.memory[i]).y;
				if(memory.memory[i] != null && arr[2].equals(((Pair) memory.memory[i]).x))
					b = ((Pair) memory.memory[i]).y;
			}
			x = Integer.parseInt(a);
			y = Integer.parseInt(b);
		} catch (Exception e) {
			system.printstring("the start and end should be integers");
		}
		for (int i = x ; i < y; i++) {
			system.printint(i);
		}
	}
	
	
	public void excute(PCB p, Mutex input, Mutex output, Mutex file, Queue<PCB> ready,  Memory memory)
	{
		String [] arr = this.inst;
		switch (arr[0])
		{
		   case "print": print(p, arr, memory); break;
		   case "printFromTo": printFromTo(p, arr, memory); break;
		   case "readFile": String string = system.readFile(p, arr, memory); break;
		   case "writeFile": system.writeFile(p, arr, memory); break;
		   case "assign": assign(p, arr, memory); break;
		   case "semWait" : 
			   switch (arr[1]) {
			   		case "userInput" : input.semWait(p); break;
			   		case "userOutput" : output.semWait(p); break;
			   		case "file" : file.semWait(p); break;
			   }
			   break;
		   case "semSignal" : 
			   switch (arr[1]) {
			   		case "userInput" :  input.semSignal(p, ready); break;
			   		case "userOutput" : output.semSignal(p, ready); break;
			   		case "file" : file.semSignal(p, ready); break;
			   }
			   break;
		}
	}
}
