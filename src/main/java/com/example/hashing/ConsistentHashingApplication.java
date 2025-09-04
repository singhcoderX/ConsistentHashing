package com.example.hashing;

public class ConsistentHashingApplication {

    public static void main(String[] args) {
        printWelcome();
        ConsistentHashRing consistentHash = new ConsistentHashRing();
    }

    private static void printWelcome() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║        CONSISTENT HASHING SYSTEM           ║");
        System.out.println("║            Interactive Demo                ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        System.out.println("🎯 Welcome to the Consistent Hashing Interactive Demo!");
        System.out.println("This system demonstrates distributed hash ring concepts.\n");
    }


}
