public class OS {
	Scheduler RoundRobin ;
	
	
	public OS (int quantum, int prog_1_t, int prog_2_t, int prog_3_t) {
		
		RoundRobin = new Scheduler(quantum, prog_1_t, prog_2_t, prog_3_t);
	}
	
	
	public void run() {
		this.RoundRobin.run();
	}
	public static void main(String[] args) {
		int prog_1_t = 0;
		int prog_2_t = 1;
		int prog_3_t = 4;
		int quantum = 2;
		OS os = new OS(quantum, prog_1_t, prog_2_t, prog_3_t);
		os.run();
	}
}
