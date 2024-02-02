import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
	int id;
	boolean available;
	Queue<PCB> blocked;
	public Mutex() {
		id = -1;
		available = true;
		blocked = new LinkedList<>();
	}
	public void semWait(PCB p) {
		if (this.available) {
			this.id = p.processID;
			this.available = false;
		}
		else {
			p.processState = State.BLOCKED;
			blocked.add(p);
		}
	}
	
	public void semSignal(PCB p, Queue<PCB> ready) {
		if (p.processID == this.id)
		{
			if (blocked.isEmpty())
				available = true;
			else {
				PCB p1 = this.blocked.poll();
				this.id = p1.processID;
				p1.processState = State.READY;
				ready.add(p1);
			}
		}
	}
	
}
