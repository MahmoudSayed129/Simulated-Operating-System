import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class Scheduler {
	Queue<PCB> ready;
	Mutex input;
	Mutex output;
	Mutex file;
	int quantum;
	int prog_1_t;
	int prog_2_t;
	int prog_3_t;
	Memory memory = new Memory();

	public static SystemCalls systemCalls = new SystemCalls();
	public static int t = 0;
	public static int currProcId = 1;
	public static int numOfcompProc = 0;

	public Scheduler(int quantum, int prog_1_t, int prog_2_t, int prog_3_t) {
		this.prog_1_t = prog_1_t;
		this.prog_2_t = prog_2_t;
		this.prog_3_t = prog_3_t;
		this.quantum = quantum;
		ready = new LinkedList<>();
		input = new Mutex();
		output = new Mutex();
		file = new Mutex();
	}

	public static void printdesk() throws IOException {
		systemCalls.printstring("---------------------------------------------------------------");
		systemCalls.printstring("Desk : ");
		BufferedReader b = new BufferedReader(new FileReader("desk.txt"));
		String currentLine = b.readLine();
		// take process from the desk
		while (currentLine != null) {
			systemCalls.printstring(currentLine);
			currentLine = b.readLine();
		}
		b.close();
		systemCalls.printstring("---------------------------------------------------------------");
	}
	
	public static int writeTodesk(PCB pcb, Memory memory) {
		int id = 0;
		int max = 0;
		for (int i = 4; i < 15; i += 5) {
			if ((int) memory.memory[i] > max && pcb.instructionsEnd != (int) memory.memory[i]) {
				max = (int) memory.memory[i];
				id = (int) memory.memory[i - 4];
			}
		}
		BufferedWriter out = null;

		try {
			FileWriter fstream = new FileWriter("desk.txt", true);
			out = new BufferedWriter(fstream);
			for (int i = (int) memory.memory[(id - 1) * 5 + 3]; i <= max; i++) {
				if (memory.memory[i] != null) out.write(memory.memory[i].toString() +"\n");
				memory.equipped_spaces--;
			}
		}

		catch (IOException e) {
			systemCalls.printstring("Error: " + e.getMessage());
		}

		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return id;
	}
	
	public static void swapArrival(PCB pcb, String path, Memory memory, int num) throws IOException {
		int id = writeTodesk(pcb, memory); 
		BufferedReader b = new BufferedReader(new FileReader(path));
		String currentLine = b.readLine();
		int currinst = (int) memory.memory[(id - 1) * 5 + 3];
		pcb.instructionsStart = (int) memory.memory[(id - 1) * 5 + 3];
		pcb.instructionsEnd = (int) memory.memory[(id - 1) * 5 + 3] + num + 2;
		int num2 = (int) memory.memory[(id - 1) * 5 + 4] - (int) memory.memory[(id - 1) * 5 + 3];
		memory.memory[(id - 1) * 5 + 3] = num2 + 1;
		memory.memory[(id - 1) * 5 + 4] = -1;
		memory.memory[(pcb.processID - 1) * 5 + 3] = pcb.instructionsStart;
		memory.memory[(pcb.processID - 1) * 5 + 4] = pcb.instructionsEnd;
		while (currentLine != null) {
			Instruction instruction = new Instruction(currentLine.split(" "));
			memory.writeToMemory(instruction, currinst);
			currentLine = b.readLine();
			currinst++;
		}
		b.close();
		systemCalls.printstring("Process "+id+" went to desk and Process "+pcb.processID+" is now in RAM");
	}

	public static void swap(int id, Memory memory) throws IOException {
		ArrayList<Object> deskprocess = new ArrayList<>();
		BufferedReader b = new BufferedReader(new FileReader("desk.txt"));
		String currentLine = b.readLine();
		// take process from the desk
		while (currentLine != null) {
			if (currentLine.charAt(0) == '(') {
				String x = "";
				String y = "";
				int i = 1;
				for (; currentLine.charAt(i) != ','; i++)
					x += currentLine.charAt(i);
				i++;
				for (; currentLine.charAt(i) != ')'; i++)
					y += currentLine.charAt(i);
				Pair pair = new Pair(x, y);
				deskprocess.add(pair);
			} else {
				Instruction instruction = new Instruction(currentLine.split(" "));
				deskprocess.add(instruction);
			}
			currentLine = b.readLine();
		}
		b.close();
		File file = new File("desk.txt");
		PrintWriter writer = new PrintWriter(file);
		writer.print("");
		writer.close();
		BufferedWriter out = null;

		try {
			FileWriter fstream = new FileWriter("desk.txt", true);
			out = new BufferedWriter(fstream);
			for (int i = (int) memory.memory[(id - 1) * 5 + 3]; i <= (int) memory.memory[(id - 1) * 5 + 4]; i++) {
				if (memory.memory[i] != null) out.write(memory.memory[i].toString() +"\n");
				memory.equipped_spaces--;
			}
		}

		catch (IOException e) {
			systemCalls.printstring("Error: " + e.getMessage());
		}

		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		int i = 0, num = 0;
		for (; i < 15; i = i + 5) {
			if ((int) memory.memory[i + 4] == -1) {
				num = (int) memory.memory[i + 3];
				break;
			}
		}
		for (int j = (int)memory.memory[(id - 1) * 5 + 3]; j<=(int)memory.memory[(id - 1) * 5 + 4]; j++)
		{
			memory.memory[j] = null;
		}
		// add process to memory to run
		memory.memory[i  + 3] = (int) memory.memory[(id - 1) * 5 + 3];
		memory.memory[i  + 4] = (int) memory.memory[(id - 1) * 5 + 3] + num - 1;
		memory.memory[(id - 1) * 5 + 3] = (int) memory.memory[(id - 1) * 5 + 4] - (int) memory.memory[(id - 1) * 5 + 3] + 1;
		memory.memory[(id - 1) * 5 + 4] = -1;
		int curr = (int) memory.memory[i  + 3];
		for (Object object : deskprocess) {
			memory.writeToMemory(object, curr);
			curr++;
		}
		systemCalls.printstring("Process "+id+" went to desk and Process "+memory.memory[i]+" is now in RAM");
	}

	public static void addtoRAM(PCB pcb, Memory memory) {
		int id = 0;
		int max = 0;
		for (int i = 4; i < 15; i += 5) {
			if (memory.memory[i] != null && (int) memory.memory[i] > max && pcb.instructionsEnd != (int) memory.memory[i]) {
				max = (int) memory.memory[i];
				id = (int) memory.memory[i - 4];
			}
		}
		if (memory.memory[(pcb.processID - 1) * 5 + 4] != null && (int) memory.memory[(pcb.processID - 1) * 5 + 4] == -1)
		{ // swap as process is not in RAM
			try {
				swap(id, memory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void createProcess(int id, String path, Queue<PCB> ready, Memory memory) throws IOException {
		BufferedReader b = new BufferedReader(new FileReader(path));
		String currentLine = b.readLine();
		int numOfInst = 0;
		while (currentLine != null) {
			numOfInst++;
			currentLine = b.readLine();
		}
		b.close();
		PCB pcb = new PCB(id, memory.equipped_spaces, memory.equipped_spaces + numOfInst + 2);
		for (int i = 0; i < 15; i++) {
			if (memory.memory[i] == null) {
				memory.memory[i] = pcb.processID;
				memory.memory[i + 1] = pcb.processState;
				memory.memory[i + 2] = pcb.PC;
				memory.memory[i + 3] = pcb.instructionsStart;
				memory.memory[i + 4] = pcb.instructionsEnd;
				break;
			}
		}
		if (40 - memory.equipped_spaces >= numOfInst + 3) // there is free space
		{
			int currinst = pcb.instructionsStart;
			b = new BufferedReader(new FileReader(path));
			currentLine = b.readLine();
			while (currentLine != null) {
				Instruction instruction = new Instruction(currentLine.split(" "));
				memory.writeToMemory(instruction, currinst);
				currentLine = b.readLine();
				currinst++;
			}
			memory.equipped_spaces+=3;
			b.close();
		} else { // there is no space
			swapArrival(pcb, path, memory, numOfInst);
		}
		ready.add(pcb);
	}

	// responsible for checking if the process should start or not
	public void checkProcessTime() {
		if (this.prog_1_t == t) {
			systemCalls.printstring("Process " + currProcId + " arrived at T = " + t + " and was added to the Ready Queue");
			try {
				createProcess(currProcId, "Program_1.txt", this.ready, memory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currProcId++;
		} else if (this.prog_2_t == t) {
			systemCalls.printstring("Process " + currProcId + " arrived at T = " + t + " and was added to the Ready Queue");
			try {
				createProcess(currProcId, "Program_2.txt", this.ready, memory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currProcId++;
		} else if (this.prog_3_t == t) {
			systemCalls.printstring("Process " + currProcId + " arrived at T = " + t + " and was added to the Ready Queue");
			try {
				createProcess(currProcId, "Program_3.txt", this.ready, memory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currProcId++;
		}
	}

	// responsible for dispatching processes
	public PCB dispatch(boolean started, PCB running) {
		if (started)
			systemCalls.printstring("-----------------------------------------------------------------------------------------------");
		systemCalls.printstring("Dispatching.....");
		if (running != null) {
			running.processState = State.READY;
			ready.add(running);
			running = null;
		}
		if (!ready.isEmpty()) {
			running = ready.poll();
			running.processState = State.RUNNING;
			started = true;
		}
		return running;
	}

	// responsible for executing the chosen Process
	public PCB executeProcess(PCB running, Memory memory) {
		int instidx = running.PC + (int) memory.memory[(running.processID - 1) * 5 + 3];
		Instruction i = (Instruction) memory.memory[instidx];
		systemCalls.printstring("the current instruction " + i + " at T = " + t);
		i.excute(running, input, output, file, ready, memory);
		running.PC++;
		memory.memory[(running.processID - 1) * 5 + 2] = running.PC;
		return running;
	}
	
	public static void reorder(PCB pcb, Memory memory) {
		int id = 0;
		boolean last = true;
		for (int i = 4; i < 15; i+=5)
		{
			if((int) memory.memory[(pcb.processID - 1) * 5 + 4] < (int)memory.memory[i])
			{
				last = false;
				id = i-4;
			}
		}
		if(!last)
		{
			int start = (int) memory.memory[(pcb.processID - 1) * 5 + 3];
			int start2 = (int) memory.memory[id + 3]; 
			int end = (int) memory.memory[id + 4];
			for (int i = start2; i <= end; i++)
			{
				memory.memory[start] = memory.memory[i];
				start++;
			}
			int shift = start2 - start;
			memory.memory[id + 3] = start;
			memory.memory[id + 4] = end - shift;
		}
	}
	
	public static void getProcessfromdesk(int id, Memory memory)  throws IOException {
		ArrayList<Object> deskprocess = new ArrayList<>();
		BufferedReader b = new BufferedReader(new FileReader("desk.txt"));
		String currentLine = b.readLine();
		while (currentLine != null) {
			if (currentLine.charAt(0) == '(') {
				String x = "";
				String y = "";
				int i = currentLine.length();
				for (; currentLine.charAt(i) != ','; i++)
					x += currentLine.charAt(i);
				for (; currentLine.charAt(i) != ')'; i++)
					y += currentLine.charAt(i);
				Pair pair = new Pair(x, y);
				deskprocess.add(pair);
			} else {
				Instruction instruction = new Instruction(currentLine.split(" "));
				deskprocess.add(instruction);
			}
			currentLine = b.readLine();
		}
		b.close();
		File file = new File("desk.txt");
		PrintWriter writer = new PrintWriter(file);
		writer.print("");
		writer.close();
		int start = memory.equipped_spaces;
		for (Object object : deskprocess) {
			memory.writeToMemory(object, start);
			start++;
		}
		memory.equipped_spaces+=3;
		memory.memory[id+3] = start;
		memory.memory[id+4] =memory.equipped_spaces;
	}
	
	public static void endprocess(PCB pcb, Memory memory) {
		memory.memory[(pcb.processID - 1) * 5 + 1] = State.FINISHED;
		pcb.processState = State.FINISHED;
		for (int i = (int) memory.memory[(pcb.processID - 1) * 5 + 3] ; i <= (int)memory.memory[(pcb.processID - 1) * 5 + 4] ; i++)
		{
			memory.memory[i] = null;
			memory.equipped_spaces--;
		}
		reorder(pcb, memory);
		for(int i = (int) memory.memory[(pcb.processID - 1) * 5 + 4]; i<15 ; i+=5) {
			if((int)memory.memory[i]== -1)
			{
				try {
					getProcessfromdesk(i-4, memory);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// the main method which start the execution
	public void run() {
		int currQuan = quantum;
		boolean started = false;
		PCB running = null;
		while (numOfcompProc < 3) {
			this.checkProcessTime();

			// dispatch
			if (currQuan == 0 || !started) {
				running = this.dispatch(started, running);
				if (running != null) {
					started = true;
					currQuan = this.quantum;
				}
			}
			addtoRAM(running, memory);
			try {
				printdesk();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("at t = "+t +"\n"+memory);
			if (running != null) {
				currQuan--;
				running = executeProcess(running, memory);
				// checking if process is blocked or when process finished
				if (running.PC + (int)memory.memory[(running.processID - 1) * 5 +3] == (int)memory.memory[(running.processID - 1) * 5 +4] - 2 
						|| running.processState == State.BLOCKED) {
					currQuan = 0;
					if (running.PC + (int)memory.memory[(running.processID - 1) * 5 +3] == (int)memory.memory[(running.processID - 1) * 5 +4]  - 2 )
					{
						numOfcompProc++;
						endprocess(running, memory);
					}
					if (running.processState == State.BLOCKED)
						systemCalls.printstring("Process of id " + running.processID + " is blocked");
					else
						systemCalls.printstring("Process of id " + running.processID
								+ " is finshed and number of process completed is : " + numOfcompProc);
//					systemCalls.printstring("Ready Queue  :  " + ready);
//					systemCalls.printstring("Input Blocked Queue :  " + input.blocked);
//					systemCalls.printstring("Output Blocked Queue :  " + output.blocked);
//					systemCalls.printstring("File Blocked Queue :  " + file.blocked);
					running = null;
				}

			}
			t++;
		}
		systemCalls.printstring("-----------------------------------------------------------");
		systemCalls.printstring("The three Processes have finished");
	}
}
