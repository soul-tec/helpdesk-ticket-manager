package helpdesk;

import java.util.*;

/**
 * IT Help Desk Ticket Manager - CLI Application
 * Demonstrates: Java OOP, file persistence, enums, ArrayList, streams.
 * @author Mark Egbe-Osibe
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TicketManager manager = new TicketManager();

    public static void main(String[] args) {
        printBanner();
        manager.printStats();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = prompt("Choose option").trim();
            System.out.println();
            switch (choice) {
                case "1" -> createTicket();
                case "2" -> listTickets();
                case "3" -> viewTicket();
                case "4" -> updateTicket();
                case "5" -> filterMenu();
                case "6" -> manager.printStats();
                case "7" -> seedDemoData();
                case "0" -> { System.out.println("Goodbye!"); running = false; }
                default  -> System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    private static void createTicket() {
        String name = prompt("Requester name");
        if (name.isBlank()) return;
        Ticket.Category cat = pickEnum("Category", Ticket.Category.values());
        if (cat == null) return;
        Ticket.Priority pri = pickEnum("Priority", Ticket.Priority.values());
        if (pri == null) return;
        String desc = prompt("Issue description");
        if (desc.isBlank()) return;
        System.out.println("Created: " + manager.createTicket(name, cat, pri, desc));
    }

    private static void listTickets() {
        var all = manager.getAllSorted();
        if (all.isEmpty()) { System.out.println("No tickets. Use option 7 for demo data."); return; }
        all.forEach(t -> System.out.println(t.toSummaryLine()));
    }

    private static void viewTicket() {
        try {
            int id = Integer.parseInt(prompt("Ticket ID").trim());
            manager.findById(id).ifPresentOrElse(System.out::println, () -> System.out.println("Not found."));
        } catch (NumberFormatException e) { System.out.println("Invalid number."); }
    }

    private static void updateTicket() {
        try {
            int id = Integer.parseInt(prompt("Ticket ID to update").trim());
            if (manager.findById(id).isEmpty()) { System.out.println("Not found."); return; }
            Ticket.Status s = pickEnum("New status", Ticket.Status.values());
            if (s == null) return;
            String note = prompt("Note (Enter to skip)");
            System.out.println(manager.updateStatus(id, s, note) ? "Updated!" : "Failed.");
        } catch (NumberFormatException e) { System.out.println("Invalid number."); }
    }

    private static void filterMenu() {
        System.out.println("[1] By Status  [2] By Priority");
        String c = prompt("Choose").trim();
        if (c.equals("1")) {
            Ticket.Status s = pickEnum("Status", Ticket.Status.values());
            if (s != null) manager.filterByStatus(s).forEach(t -> System.out.println(t.toSummaryLine()));
        } else if (c.equals("2")) {
            Ticket.Priority p = pickEnum("Priority", Ticket.Priority.values());
            if (p != null) manager.filterByPriority(p).forEach(t -> System.out.println(t.toSummaryLine()));
        }
    }

    private static void seedDemoData() {
        manager.createTicket("Sarah Johnson",  Ticket.Category.HARDWARE, Ticket.Priority.HIGH,     "Laptop won't power on");
        manager.createTicket("James Miller",   Ticket.Category.NETWORK,  Ticket.Priority.CRITICAL, "VPN down - team blocked");
        manager.createTicket("Priya Sharma",   Ticket.Category.ACCOUNT,  Ticket.Priority.MEDIUM,   "Locked out of Office 365");
        manager.createTicket("Derek Thompson", Ticket.Category.SOFTWARE,  Ticket.Priority.LOW,      "Excel crashing on large files");
        manager.createTicket("Maria Lopez",    Ticket.Category.HARDWARE, Ticket.Priority.MEDIUM,   "Monitor flickering");
        manager.updateStatus(1, Ticket.Status.IN_PROGRESS, "Checking power adapter");
        manager.updateStatus(2, Ticket.Status.IN_PROGRESS, "Escalated to network team");
        manager.updateStatus(3, Ticket.Status.RESOLVED,    "Account unlocked via admin");
        System.out.println("5 demo tickets loaded!");
        listTickets();
    }

    private static <T> T pickEnum(String label, T[] values) {
        System.out.println(label + ":");
        for (int i = 0; i < values.length; i++) System.out.printf("  [%d] %s%n", i+1, values[i]);
        try {
            int idx = Integer.parseInt(prompt("Enter number").trim()) - 1;
            if (idx >= 0 && idx < values.length) return values[idx];
        } catch (NumberFormatException ignored) {}
        return null;
    }

    private static String prompt(String label) {
        System.out.print("> " + label + ": ");
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }

    private static void printMenu() {
        System.out.println("[1]New [2]List [3]View [4]Update [5]Filter [6]Dashboard [7]Demo [0]Exit");
    }

    private static void printBanner() {
        System.out.println("=== IT HELP DESK TICKET MANAGER v1.0 | by Mark Egbe-Osibe ===");
    }
            }
