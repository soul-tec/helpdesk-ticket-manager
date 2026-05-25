package helpdesk;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all tickets: create, update, filter, and persist to disk.
 * Demonstrates: ArrayList, file I/O, streams/lambdas, exception handling
 */
public class TicketManager {

    private static final String DATA_FILE = "tickets.dat";

    private final List<Ticket> tickets = new ArrayList<>();
    private int nextId = 1;

    public TicketManager() {
        loadFromDisk();
    }

    public Ticket createTicket(String name, Ticket.Category cat, Ticket.Priority pri, String desc) {
        Ticket t = new Ticket(nextId++, name, cat, pri, desc);
        tickets.add(t);
        saveToDisk();
        return t;
    }

    public Optional<Ticket> findById(int id) {
        return tickets.stream().filter(t -> t.getId() == id).findFirst();
    }

    public boolean updateStatus(int id, Ticket.Status newStatus, String note) {
        Optional<Ticket> opt = findById(id);
        if (opt.isEmpty()) return false;
        Ticket t = opt.get();
        t.setStatus(newStatus);
        if (!note.isBlank()) t.setResolutionNote(note);
        saveToDisk();
        return true;
    }

    public List<Ticket> filterByStatus(Ticket.Status status) {
        return tickets.stream()
            .filter(t -> t.getStatus() == status)
            .sorted(Comparator.comparing(Ticket::getPriority).reversed())
            .collect(Collectors.toList());
    }

    public List<Ticket> filterByPriority(Ticket.Priority priority) {
        return tickets.stream()
            .filter(t -> t.getPriority() == priority)
            .collect(Collectors.toList());
    }

    public List<Ticket> getAllSorted() {
        return tickets.stream()
            .sorted(Comparator.comparing(Ticket::getStatus)
                .thenComparing(Comparator.comparing(Ticket::getPriority).reversed()))
            .collect(Collectors.toList());
    }

    public void printStats() {
        long open     = tickets.stream().filter(t -> t.getStatus() == Ticket.Status.OPEN).count();
        long inProg   = tickets.stream().filter(t -> t.getStatus() == Ticket.Status.IN_PROGRESS).count();
        long resolved = tickets.stream().filter(t -> t.getStatus() == Ticket.Status.RESOLVED).count();
        long critical = tickets.stream().filter(t -> t.getPriority() == Ticket.Priority.CRITICAL).count();
        System.out.println("=== HELP DESK DASHBOARD ===");
        System.out.printf("Total: %d | Open: %d | In Progress: %d | Resolved: %d | Critical: %d%n",
            tickets.size(), open, inProg, resolved, critical);
        System.out.println("===========================");
    }

    private void saveToDisk() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            pw.println(nextId);
            tickets.forEach(t -> pw.println(t.toFileLine()));
        } catch (IOException e) {
            System.err.println("Warning: Could not save tickets: " + e.getMessage());
        }
    }

    private void loadFromDisk() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String firstLine = br.readLine();
            if (firstLine != null) nextId = Integer.parseInt(firstLine.trim());
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) tickets.add(Ticket.fromFileLine(line));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Warning: Could not load tickets: " + e.getMessage());
        }
    }

    public int getTicketCount() { return tickets.size(); }
              }
