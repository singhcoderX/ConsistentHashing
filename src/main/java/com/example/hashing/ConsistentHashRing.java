package com.example.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * A Consistent Hashing Implementation in Java
 * This implementation uses MD5 hash function
 */
public class ConsistentHashRing {
    private final SortedMap<Long, String> ring = new TreeMap<>();
    private final Set<String> servers = new HashSet<>();

    public void addServer(String server) {
        if (servers.contains(server)) {
            System.out.println("Server " + server + " already exists in the Ring!");
            return;
        }

        servers.add(server);
        long hash = computeHash(server);
        ring.put(hash, server);

        System.out.println("Added server " + server + " to Ring!" + "[ Hash : " + hash + " ]");
    }

    /**
     * MD5 hash function - converts string to long hash value
     *
     * @param input String to hash
     * @return Long hash value
     */
    public long computeHash(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(input.getBytes());

            // Convert byte array to long (using first 8 bytes)
            long hash = 0;
            for (int i = 0; i < 8 && i < digest.length; i++) {
                hash = (hash << 8) | (digest[i] & 0xFF);
            }

            return Math.abs(hash); // Ensure positive value
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    public void removeServer(String server) {
        if (!servers.contains(server)) {
            System.out.println("❌ Server " + server + " not found in the ring");
            return;
        }
        servers.remove(server);

        long hash = computeHash(server);
        ring.remove(hash);
        System.out.println("✅ Removed server: " + server);
        printRingStats();

    }

    /**
     * Find the server responsible for a given key
     *
     * @param key The key to hash and find server for
     * @return Server identifier or null if no servers available
     */
    public String getServer(String key) {
        if (ring.isEmpty()) return null;
        long hash = computeHash(key);

        SortedMap<Long, String> tailMap = ring.tailMap(hash);
        // If no server found clockwise, wrap around to the beginning
        Long serverHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();

        return ring.get(serverHash);
    }

    /**
     * Get all active servers
     *
     * @return Set of server identifiers
     */
    public Set<String> getAllServers() {
        return new HashSet<>(servers);
    }

    /**
     * Print current ring statistics
     */
    private void printRingStats() {
        System.out.println("📊 Ring Stats: " + servers.size() + " servers, " + ring.size() + " positions on ring");
    }

    /**
     * Get total number of positions on the ring (same as server count)
     *
     * @return Number of positions
     */
    public int getRingSize() {
        return ring.size();
    }

    /**
     * Check if the ring is empty
     *
     * @return true if no servers in ring
     */
    public boolean isEmpty() {
        return ring.isEmpty();
    }

    /**
     * Analyze key distribution across servers
     *
     * @param numberOfKeys Number of random keys to test
     */
    public void analyzeDistribution(int numberOfKeys) {
        if (servers.isEmpty()) {
            System.out.println("❌ No servers in ring for distribution analysis");
            return;
        }

        Map<String, Integer> distribution = new HashMap<>();

        // Initialize counters
        for (String server : servers) {
            distribution.put(server, 0);
        }

        // Generate random keys and track distribution
        Random random = new Random();
        for (int i = 0; i < numberOfKeys; i++) {
            String key = "key_" + random.nextInt(100000);
            String server = getServer(key);
            if (server != null) {
                distribution.put(server, distribution.get(server) + 1);
            }
        }

        // Print distribution analysis
        System.out.println("\n📈 === DISTRIBUTION ANALYSIS (Basic Ring) ===");
        System.out.println("Sample size: " + numberOfKeys + " random keys");
        System.out.println("Expected per server: " + (numberOfKeys / servers.size()) + " keys");
        System.out.println("⚠️  Note: Without virtual nodes, distribution may be uneven!\n");

        double expectedPerServer = (double) numberOfKeys / servers.size();

        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            String server = entry.getKey();
            int keyCount = entry.getValue();
            double percentage = (keyCount * 100.0) / numberOfKeys;
            double deviation = Math.abs(keyCount - expectedPerServer) / expectedPerServer * 100;

            System.out.printf("%-20s: %6d keys (%5.1f%%) | Deviation: %5.1f%%\n",
                    server, keyCount, percentage, deviation);
        }
        System.out.println("===============================================\n");
    }

    /**
     * Print detailed ring information (for debugging)
     */
    public void printRingDetails() {
        System.out.println("\n🔍 === BASIC RING INFORMATION ===");
        System.out.println("Total Servers: " + servers.size());
        System.out.println("Ring Positions: " + ring.size());
        System.out.println("Active Servers: " + servers);

        if (!ring.isEmpty()) {
            System.out.println("\nServer Positions on Ring:");
            for (Map.Entry<Long, String> entry : ring.entrySet()) {
                System.out.printf("  Hash: %20d → Server: %s\n",
                        entry.getKey(), entry.getValue());
            }
        }
        System.out.println("=====================================\n");
    }

    /**
     * Show the hash ring visually (simplified representation)
     */
    public void visualizeRing() {
        System.out.println("\n🎨 === BASIC RING VISUALIZATION ===");

        if (ring.isEmpty()) {
            System.out.println("Ring is empty!");
            return;
        }

        System.out.println("Ring positions (clockwise order):");

        int position = 1;
        for (Map.Entry<Long, String> entry : ring.entrySet()) {
            System.out.printf("%d. %s (Hash: %d)\n",
                    position++, entry.getValue(), entry.getKey());
        }

        System.out.println("\nSample key mappings:");
        String[] sampleKeys = {"user1", "user2", "user3", "session123", "data456"};

        for (String key : sampleKeys) {
            String server = getServer(key);
            long keyHash = computeHash(key);
            System.out.printf("Key: %-10s (Hash: %20d) → %s\n",
                    key, keyHash, server);
        }

        System.out.println("====================================\n");
    }
}
