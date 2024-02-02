# Simulated Operating System Simulation

This project involves building a simulated operating system to gain a deeper understanding of operating system concepts. The simulation includes implementing a basic interpreter, mutexes for resource management, a scheduler, and memory management.

[Click here to watch the simulation results on YouTube](https://youtu.be/B32RQS2OAmE)

## Milestone 1

### Objective
The main aim of Milestone 1 is to implement a basic interpreter, mutexes, and a scheduler.

### System Calls
System calls allow processes to request services from the operating system. Required system calls include reading and writing files, printing on the screen, and taking user input.

### Programs
Three main programs are provided:
1. Program to print numbers between two given numbers.
2. Program to write data to a file.
3. Program to print the contents of a file on the screen.

### Program Syntax
Program syntax includes commands for printing, variable assignment, file I/O, and semaphores for mutual exclusion.

### Mutual Exclusion
Mutexes are implemented to ensure mutual exclusion over critical resources, such as file access, user input, and screen output.

### Scheduler
A Round Robin scheduler is implemented to schedule processes based on fixed time slices.

## Milestone 2

### Objective
The main objective of Milestone 2 is to extend the code from Milestone 1 by implementing memory management.

### Memory Management
The operating system manages memory allocation for processes, ensuring each process has its allocated space. Memory is divided into memory words, and processes are created when their program files are read into memory.

### Process Control Block
Each process has a Process Control Block (PCB) containing information such as Process ID, Process State, Program Counter, and Memory Boundaries.

## Output
The simulation provides outputs showing the state of memory, process IDs during swapping, and the format of memory stored on disk.

## Instructions for Running the Simulation
1. Clone the repository to your local machine.
2. Compile and run the simulation code.
3. Follow the prompts to interact with the simulated operating system.
4. Monitor the outputs to observe the behavior of the simulated OS.

Feel free to explore and modify the code to experiment with different aspects of operating system simulation.

---

Please adjust the instructions and details as necessary for your specific project requirements and environment.
