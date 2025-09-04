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
}
