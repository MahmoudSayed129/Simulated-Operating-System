import java.io.IOException;

public class Memory {
	Object[] memory = new Object[40];
	int equipped_spaces = 15;

	public void writeToMemory(Object object, int idx) {
		memory[idx] = object;
		equipped_spaces++;
	}

	public void writedata(PCB pcb, String varname, String data) {
		int varstart = (int) this.memory[(pcb.processID - 1) * 5 + 4];
		int varend = (int) this.memory[(pcb.processID - 1) * 5 + 4] - 3; 
		for (int i = varstart; i > varend; i--) {
			if (this.memory[i] == null || this.memory[i] instanceof Instruction) {
				Pair pair = new Pair(varname, data);
				this.memory[i] = pair;
				break;
			}
		}
	}
	
	public String readFromMemory(PCB p, String varname) {
		String reString = "";
		int varstart = (int) this.memory[(p.processID - 1) * 5 + 4];
		int varend = (int) this.memory[(p.processID - 1) * 5 + 4] - 3;
		for (int i =varstart; i > varend; i--)
		{
			if(this.memory[i] != null && ! (this.memory[i]instanceof Instruction)  && varname.equals(((Pair) this.memory[i]).x))
			{
				 reString = ((Pair) this.memory[i]).y;
			}
		}
		return reString;
	}
	public String toString() {
		System.out.println("--------------------------------------------------");
		String res = "Memory  : \n";
		for (int i = 0; i < memory.length; i++) {
			if(memory[i]!= null)
				res += i+" --> "+memory[i]+"\n";
		}
		System.out.println("--------------------------------------------------");
		return res;
	}
}
