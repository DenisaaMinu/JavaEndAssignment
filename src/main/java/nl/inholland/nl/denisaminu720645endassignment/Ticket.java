package nl.inholland.nl.denisaminu720645endassignment;

public class Ticket {
    protected String startTime;
    protected String endTime;
    protected String title;
    protected int seatsLeft;

    public Ticket(String start, String end, String title, int seatsLeft) {
        this.startTime = start;
        this.endTime = end;
        this.title = title;
        this.seatsLeft = seatsLeft;
    }

    // Getter for startTime
    public String getStartTime() {
        return startTime;
    }

    // Setter for startTime
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    // Getter for endTime
    public String getEndTime() {
        return endTime;
    }

    // Setter for endTime
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for seatsLeft
    public int getSeatsLeft() {
        return seatsLeft;
    }

    // Setter for seatsLeft
    public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft = seatsLeft;
    }
}
