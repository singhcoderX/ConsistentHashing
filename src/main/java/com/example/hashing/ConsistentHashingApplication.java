package com.example.hashing;

import java.util.Scanner;

public class ConsistentHashingApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static ConsistentHashRing hashRing;

    public static void main(String[] args) {
        printWelcome();
        hashRing = new ConsistentHashRing();
        while (true) {
            printMenu();
            int choice = getUserChoice();

            try {
                handleMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("Error:: " + e.getMessage());
                System.out.println("Please Try Again");
            }
        }
    }

    private static void printWelcome() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║        CONSISTENT HASHING SYSTEM           ║");
        System.out.println("║            Interactive Demo                ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        System.out.println("🎯 Welcome to the Consistent Hashing Interactive Demo!");
        System.out.println("This system demonstrates distributed hash ring concepts.\n");
    }

    private static void printMenu() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│              BASIC HASH RING            │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│  1. ➕  Add Server                      │");
        System.out.println("│  2. ❌  Remove Server                   │");
        System.out.println("│  3. 🔍  Find Server for Key             │");
        System.out.println("│  4. 📊  Analyze Distribution            │");
        System.out.println("│  5. 🖥️  Show Ring Status                │");
        System.out.println("│  6. 🎨  Visualize Ring                  │");
        System.out.println("│  7. 🎬  Run Full Demo                   │");
        System.out.println("│  8. 🧪  Experiment with Problems        │");
        System.out.println("│  9. 🔄  Reset Ring                      │");
        System.out.println("│  0. 🚪  Exit                            │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("Enter your choice (0-9): ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void handleMenuChoice(int choice) {
        System.out.println();

        switch (choice) {
            case 1:
                addServer();
                break;
            case 2:
                removeServer();
                break;
            case 3:
                findServerForKey();
                break;
            case 4:
                analyzeDistribution();
                break;
            case 5:
                showRingStatus();
                break;
            case 6:
                visualizeRing();
                break;
            case 7:
                runFullDemo();
                break;
            case 8:
                experimentWithProblems();
                break;
            case 9:
                resetRing();
                break;
            case 0:
                exitApplication();
                break;
            default:
                System.out.println("❌ Invalid choice. Please enter 0-9.\n");
        }
    }

    private static void addServer() {
        if (hashRing.getAllServers().isEmpty()) {
            System.out.println("💡 Tip: Try these server names to see different hash distributions:");
            System.out.println("   - ServerA, ServerB, ServerC");
            System.out.println("   - Web-01, Web-02, Web-03");
            System.out.println("   - 192.168.1.10:8080, 192.168.1.20:8080\n");
        }
        System.out.println("Enter server name: ");
        String serverName = scanner.nextLine().trim();
        if (!serverName.isEmpty()) {
            hashRing.addServer(serverName);
            if (hashRing.getAllServers().size() >= 3) {
                System.out.println("\n💡 Great! Now try option 4 to analyze distribution.");
            }
        } else {
            System.out.println("❌ Invalid server name.");
        }

        pauseForUser();
    }

    private static void removeServer() {
        if (hashRing.isEmpty()) {
            System.out.println("❌ No servers in ring. Add some first!");
            pauseForUser();
            return;
        }

        System.out.println("Current servers: " + hashRing.getAllServers());
        System.out.print("Enter server to remove: ");
        String server = scanner.nextLine().trim();

        if (!server.isEmpty()) {
            hashRing.removeServer(server);
        } else {
            System.out.println("❌ Invalid server name.");
        }
        pauseForUser();
    }

    private static void findServerForKey() {
        if (hashRing.isEmpty()) {
            System.out.println("❌ No servers in ring. Add some first!");
            pauseForUser();
            return;
        }
        System.out.println("Enter key to look for: ");
        String key = scanner.nextLine().trim();

        if (!key.isEmpty()) {
            String server = hashRing.getServer(key);
            System.out.println("🎯 Key '" + key + "' → Server: " + server);

            System.out.println("\n🔍 Want to see more keys? Try these:");
            String[] suggestions = {key + "1", key + "2", "user_" + key, key + "_session"};
            for (String s : suggestions) {
                String suggestedServer = hashRing.getServer(s);
                System.out.println(" " + s + ": " + suggestedServer);
            }
        } else {
            System.out.println("❌ Invalid key.");
        }

        pauseForUser();
    }

    private static void analyzeDistribution() {
        if (hashRing.isEmpty()) {
            System.out.println("❌ No servers in ring. Add some first!");
            pauseForUser();
            return;
        }

        System.out.println("📊 Analyzing distribution...");
        hashRing.analyzeDistribution(10000);

        System.out.println("💡 Observations:");
        System.out.println("   → Notice how uneven the distribution can be?");
        System.out.println("   → This is the main problem with basic consistent hashing!");
        System.out.println("   → Virtual nodes solve this (coming in Version 2)");

        pauseForUser();
    }

    private static void pauseForUser() {
        System.out.print("\n⏸️  Press Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }

    private static void showRingStatus() {
        hashRing.printRingDetails();
        pauseForUser();
    }

    private static void visualizeRing() {
        hashRing.visualizeRing();
        pauseForUser();
    }

    private static void runFullDemo() {
        System.out.println("🎬 Running Full Demo...\n");
        ConsistentHashingDemo.main(new String[]{});
        pauseForUser();
    }

    private static void experimentWithProblems() {
        System.out.println("🧪 === EXPERIMENT: Problems with Basic Hashing ===");
        System.out.println("Let's create scenarios that show the issues:\n");

        System.out.println("Experiment 1: Clustered Hash Values");
        ConsistentHashRing testRing = new ConsistentHashRing();

        // These servers might hash to similar values
        testRing.addServer("ServerA");
        testRing.addServer("ServerB");
        testRing.addServer("ServerC");

        testRing.printRingDetails();
        testRing.analyzeDistribution(5000);

        System.out.println("Experiment 2: What happens with server names that hash closely?");
        ConsistentHashRing clusteredRing = new ConsistentHashRing();

        // Try servers that might hash to similar values
        clusteredRing.addServer("server1");
        clusteredRing.addServer("server2");
        clusteredRing.addServer("server3");

        clusteredRing.printRingDetails();
        clusteredRing.analyzeDistribution(5000);

        System.out.println("💡 Key Insights:");
        System.out.println("   → Hash clustering creates uneven distribution");
        System.out.println("   → Some servers get much more load than others");
        System.out.println("   → Adding/removing servers affects large key ranges");
        System.out.println("   → This is why we need virtual nodes!");

        pauseForUser();
    }

    private static void resetRing() {
        hashRing = new ConsistentHashRing();
        System.out.println("🔄 Ring reset! Start fresh by adding servers.");
        pauseForUser();
    }

    private static void exitApplication() {
        System.out.println("👋 Thanks for exploring Basic Consistent Hashing!");
        System.out.println("🚀 Ready for Version 2 with Virtual Nodes?");
        System.out.println("💡 You've learned the fundamentals - great job!");
        System.exit(0);
    }

}
