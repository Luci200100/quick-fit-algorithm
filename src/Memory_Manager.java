import java.util.*;

public class Memory_Manager {

    private final Map<Integer, LinkedList<String>> memoryBlocks;
    private final Map<String, Map.Entry<String, Integer>> allocatedProcesses;

    // Constructor to initialize memory block lists for different sizes
    public Memory_Manager() {
        memoryBlocks = new HashMap<>();
        memoryBlocks.put(50, new LinkedList<>(Arrays.asList("Block 1", "Block 2")));
        memoryBlocks.put(100, new LinkedList<>(Arrays.asList("Block 3", "Block 4")));
        memoryBlocks.put(200, new LinkedList<>(Collections.singletonList("Block 5")));

        allocatedProcesses = new HashMap<>();
    }

    // Method to allocate memory to a process
    public void allocate_Memory(String processName, int size) {
        // Find the smallest suitable block size
        int suitableSize = memoryBlocks.keySet().stream()
                .filter(blockSize -> blockSize >= size && !memoryBlocks.get(blockSize).isEmpty())
                .min(Integer::compareTo)
                .orElse(-1);

        if (suitableSize != -1) {
            String allocatedBlock = memoryBlocks.get(suitableSize).removeFirst();
            allocatedProcesses.put(processName.trim().toLowerCase(), Map.entry(allocatedBlock, suitableSize));
            System.out.println(processName + " (" + size + " KB): Allocated " + allocatedBlock + " (" + suitableSize + " KB Block)." );
        } else {
            System.out.println(processName + " (" + size + " KB): No suitable block available.");
        }
    }

    // Method to deallocate memory from a process
    public void deallocateMemory(String processName) {
        String normalizedProcessName = processName.trim().toLowerCase();
        if (allocatedProcesses.containsKey(normalizedProcessName)) {
            Map.Entry<String, Integer> allocation = allocatedProcesses.remove(normalizedProcessName);
            String blockName = allocation.getKey();
            int blockSize = allocation.getValue();

            memoryBlocks.get(blockSize).add(blockName);
            System.out.println(processName + " deallocated " + blockName + " (" + blockSize + " KB Block)." );
        } else {
            System.out.println("Process \"" + processName + "\" does not have any allocated memory. Currently allocated processes: " + allocatedProcesses.keySet());
        }
    }

    // Method to display the current state of memory blocks
    public void displayMemoryState() {
        System.out.println("Current Memory State:");
        for (Map.Entry<Integer, LinkedList<String>> entry : memoryBlocks.entrySet()) {
            System.out.println("  " + entry.getKey() + " KB List: " + entry.getValue());
        }
        System.out.println("Currently Allocated Processes: " + allocatedProcesses);
    }

    // Main method for user input
    public static void main(String[] args) {
        Memory_Manager memoryManager = new Memory_Manager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Quick Fit Memory Management Simulation!");
        boolean running = true;

        while (running) {
            System.out.println("\nSelect a Option:");
            System.out.println("1. Display Memory Status");
            System.out.println("2. Allocate Memory for a Process");
            System.out.println("3. Deallocate Memory from a Process");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    memoryManager.displayMemoryState();
                    break;
                case "2":
                    System.out.print("Enter process name: ");
                    String processName = scanner.nextLine();
                    System.out.print("Enter memory size: ");

                    try {
                        int size = Integer.parseInt(scanner.nextLine());
                        memoryManager.allocate_Memory(processName, size);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid memory size. Please enter a valid number again.");
                    }
                    break;
                case "3":
                    System.out.print("Enter process name to deallocate: ");
                    String processToDeallocate = scanner.nextLine();
                    memoryManager.deallocateMemory(processToDeallocate);
                    break;
                case "4":
                    running = false;
                    System.out.println("Thank you and Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
