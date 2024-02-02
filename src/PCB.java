public class PCB {
	int processID;
	State processState;
	int PC;
	int instructionsStart;
	int instructionsEnd;
	String input = "";
	String read = "";

	public PCB(int processID, int instructionsStart, int instructionsEnd) {
		this.processID = processID;
		this.processState = State.READY;
		this.instructionsStart = instructionsStart;
		this.instructionsEnd = instructionsEnd;
		this.PC = 0;
	}
	public String toString() {
		return "Process"+this.processID;
	}
}
