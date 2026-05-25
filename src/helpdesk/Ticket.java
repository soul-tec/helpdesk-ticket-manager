package helpdesk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single IT support ticket.
 * Demonstrates: OOP, encapsulation, enums, constructors
 */
public class Ticket {

    public enum Status   { OPEN, IN_PROGRESS, RESOLVED, CLOSED }
    public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
    public enum Category { HARDWARE, SOFTWARE, NETWORK, ACCOUNT, OTHER }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int id;
    private final String requesterName;
    private final Category category;
    private Priority priority;
    private Status status;
    private String description;
    private String resolutionNote;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Ticket(int id, String requesterName, Category category, Priority priority, String description) {
        this.id            = id;
        this.requesterName = requesterName;
        this.category      = category;
        this.priority      = priority;
        this.description   = description;
        this.status        = Status.OPEN;
        this.resolutionNote = "";
        this.createdAt     = LocalDateTime.now();
        this.updatedAt     = LocalDateTime.now();
    }

    public static Ticket fromFileLine(String line) {
        String[] p = line.split("\\|", 8);
        Ticket t = new Ticket(Integer.parseInt(p[0]), p[1], Category.valueOf(p[2]), Priority.valueOf(p[3]), p[5]);
        t.status         = Status.valueOf(p[4]);
        t.resolutionNote = p[6];
        t.updatedAt      = LocalDateTime.parse(p[7], FMT);
        return t;
    }

    public String toFileLine() {
        return id + "|" + requesterName + "|" + category + "|" + priority + "|"
             + status + "|" + description + "|" + resolutionNote + "|" + updatedAt.format(FMT);
    }

    @Override
    public String toString() {
        return String.format("Ticket #%04d | %s | %s | %s | %s | %s",
            id, requesterName, category, priority, status, description);
    }

    public String toSummaryLine() {
        return String.format("#%04d [%-8s] %-10s %-12s %s: %s",
            id, status, category, priority, requesterName, description.substring(0, Math.min(40, description.length())));
    }

    public int      getId()            { return id; }
    public Status   getStatus()        { return status; }
    public Priority getPriority()      { return priority; }
    public Category getCategory()      { return category; }
    public String   getRequesterName() { return requesterName; }
    public String   getDescription()   { return description; }

    public void setStatus(Status s)          { this.status = s;         this.updatedAt = LocalDateTime.now(); }
    public void setPriority(Priority p)      { this.priority = p;       this.updatedAt = LocalDateTime.now(); }
    public void setResolutionNote(String n)  { this.resolutionNote = n; this.updatedAt = LocalDateTime.now(); }
                                                                             }
