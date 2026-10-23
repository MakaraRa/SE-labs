package edu.itc.se.lab01.task1;



public class Main {

    public static void main(String[] args) throws InterruptedException {

        boolean sequential = args.length > 0
                && args[0].equals("--sequential");

        Thread apollo = new Countdown("apollo");

        Thread gemini = new Thread(
                new CountdownTask(), "gemini");

        Thread mercury = new Thread(() -> {
            new CountdownTask().run();
        }, "mercury");

        Thread artemis = Thread.ofVirtual()
                .name("artemis")
                .unstarted(new CountdownTask());

        // Store all four threads in an array.
        Thread[] threads = { apollo, gemini, mercury, artemis };

        // Start or run all four threads.
        for (Thread thread : threads) {
            if (sequential) {
                thread.run();
            } else {
                thread.start();
            }
        }

        System.out.println("Mission control: all launched");

        // Wait for all threads to finish in concurrent mode.
        if (!sequential) {
            for (Thread thread : threads) {
                thread.join();
            }
        }

        System.out.println("Mission control: all landed");

        // Print each thread's name, virtual status, and state.
        for (Thread thread : threads) {
            System.out.printf(
                    "%-8s virtual=%-5s state=%s%n",
                    thread.getName(),
                    thread.isVirtual(),
                    thread.getState());
        }
    }
}