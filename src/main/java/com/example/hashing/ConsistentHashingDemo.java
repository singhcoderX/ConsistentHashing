package com.example.hashing;

public class ConsistentHashingDemo {
    public static void main(String[] args) {
        System.out.println("🎯 ============================================");
        System.out.println("🎯    BASIC CONSISTENT HASHING DEMO V1.0    ");
        System.out.println("🎯        (Without Virtual Nodes)           ");
        System.out.println("🎯 ============================================\n");

        // Create basic hash ring
        ConsistentHashRing hashRing = new ConsistentHashRing();

        // Demo 1: Adding Servers
        System.out.println("📝 DEMO 1: Adding Servers to Ring");
        System.out.println("----------------------------------");

        hashRing.addServer("Server-A:8080");
        hashRing.addServer("Server-B:8081");
        hashRing.addServer("Server-C:8082");

        hashRing.printRingDetails();
        hashRing.visualizeRing();

        // Demo 2: Key-to-Server Mapping
        System.out.println("📝 DEMO 2: Key-to-Server Mapping");
        System.out.println("---------------------------------");

        String[] testKeys = {
                "user_123", "session_456", "cache_789",
                "data_abc", "file_xyz", "token_999",
                "image_001", "video_002", "document_003"
        };

        System.out.println("Initial key distribution:");
        for (String key : testKeys) {
            String server = hashRing.getServer(key);
            System.out.printf("%-15s → %s\n", key, server);
        }

        // Demo 3: Distribution Analysis (Show Problems without Virtual Nodes)
        System.out.println("\n📝 DEMO 3: Distribution Analysis");
        System.out.println("--------------------------------");

        hashRing.analyzeDistribution(10000);

        // Demo 4: Server Failure Simulation
        System.out.println("📝 DEMO 4: Server Failure Impact");
        System.out.println("---------------------------------");

        System.out.println("💥 Simulating Server-B failure...\n");
        hashRing.removeServer("Server-B:8081");

        System.out.println("Key redistribution after Server-B failure:");
        int affectedKeys = 0;
        for (String key : testKeys) {
            String newServer = hashRing.getServer(key);
            System.out.printf("%-15s → %s", key, newServer);

            // In a real scenario, we'd compare with previous mapping
            System.out.println(" (may have moved)");
            affectedKeys++;
        }

        System.out.println("\n⚠️  Impact Analysis:");
        System.out.println("In basic consistent hashing, when a server fails,");
        System.out.println("ALL keys handled by that server need redistribution.");
        System.out.println("This can cause significant data movement.\n");

        // Demo 5: Adding New Server
        System.out.println("📝 DEMO 5: Adding New Server");
        System.out.println("----------------------------");

        System.out.println("➕ Adding Server-D to handle load...\n");
        hashRing.addServer("Server-D:8083");

        System.out.println("Key redistribution after adding Server-D:");
        for (String key : testKeys) {
            String server = hashRing.getServer(key);
            System.out.printf("%-15s → %s\n", key, server);
        }

        hashRing.analyzeDistribution(10000);

        // Demo 6: Problems with Basic Hashing
        System.out.println("📝 DEMO 6: Problems with Basic Hashing");
        System.out.println("--------------------------------------");

        demonstrateUnbalancedDistribution();

        // Demo 7: Hash Collisions and Distribution
        System.out.println("📝 DEMO 7: Understanding Hash Distribution");
        System.out.println("------------------------------------------");

        demonstrateHashDistribution(hashRing);

        System.out.println("\n🎯 === KEY LEARNINGS ===");
        System.out.println("✅ Basic consistent hashing provides server selection");
        System.out.println("✅ Keys map consistently to the same servers");
        System.out.println("✅ Ring structure allows for server addition/removal");
        System.out.println("⚠️  Distribution can be highly uneven");
        System.out.println("⚠️  Server failures affect large key ranges");
        System.out.println("⚠️  Hash clustering can cause hotspots");
        System.out.println("\n💡 SOLUTION: Virtual Nodes (Coming in Version 2!)");

        System.out.println("\n🎉 ============================================");
        System.out.println("🎉     BASIC CONSISTENT HASHING COMPLETE     ");
        System.out.println("🎉 ============================================");
    }

    /**
     * Demonstrate how basic hashing can lead to unbalanced distribution
     */
    private static void demonstrateUnbalancedDistribution() {
        System.out.println("Creating rings with different server names to show distribution variance:\n");

        // Test different server naming patterns
        String[][] serverSets = {
                {"ServerA", "ServerB", "ServerC"},
                {"Web-01", "Web-02", "Web-03"},
                {"192.168.1.10", "192.168.1.20", "192.168.1.30"}
        };

        for (int i = 0; i < serverSets.length; i++) {
            System.out.println("Test Set " + (i + 1) + ": " + java.util.Arrays.toString(serverSets[i]));

            ConsistentHashRing testRing = new ConsistentHashRing();
            for (String server : serverSets[i]) {
                testRing.addServer(server);
            }

            testRing.analyzeDistribution(5000);
        }
    }

    /**
     * Show hash values and distribution patterns
     */
    private static void demonstrateHashDistribution(ConsistentHashRing ring) {
        System.out.println("Hash values for current servers:");

        for (String server : ring.getAllServers()) {
            long hash = computeTestHash(server);
            System.out.printf("%-20s → Hash: %20d\n", server, hash);
        }

        System.out.println("\nHash values for sample keys:");
        String[] keys = {"key1", "key2", "key3", "user123", "session456"};

        for (String key : keys) {
            long hash = computeTestHash(key);
            String assignedServer = ring.getServer(key);
            System.out.printf("%-15s → Hash: %20d → Server: %s\n",
                    key, hash, assignedServer);
        }
    }

    /**
     * Compute hash for demonstration (mirrors the internal method)
     */
    private static long computeTestHash(String input) {
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(input.getBytes());

            long hash = 0;
            for (int i = 0; i < 8 && i < digest.length; i++) {
                hash = (hash << 8) | (digest[i] & 0xFF);
            }

            return Math.abs(hash);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
}
