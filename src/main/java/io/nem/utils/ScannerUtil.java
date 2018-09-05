package io.nem.utils;

import java.util.Scanner;

/**
 * The Class ScannerUtil.
 */
public class ScannerUtil {

    public static boolean bShutdown = false;

    /**
     * exit when enter string "exit".
     */
    public static void monitorExit() {
        bShutdown = false;
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                String line = scanner.nextLine();
                if (ScannerUtil.isShutdown() || "exit".equals(line)) {
                    break;
                }

            }
        } catch (Exception ex) {

        } finally {
            scanner.close();
        }
    }

    public static boolean isShutdown() {
        return bShutdown;
    }

    public static void destroy() {
        bShutdown = true;
    }
}
