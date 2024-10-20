package nl.inholland.nl.denisaminu720645endassignment;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Showing implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

   protected String title;
   protected String startDate;
   protected String endDate;
   protected final int TOTAL_SEATS = 72;
   protected String seatsLeft;
   protected int ticketsSold;
   protected Map<String, Boolean> seatAvailability = new HashMap<>();  // Track sold seats for this showing

   public Showing(String title, String startDate, String endDate) {
       this.title = title;
       this.startDate = startDate;
       this.endDate = endDate;
       this.ticketsSold = 0;
   }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for start date
    public String getStartDate() {
        return startDate;
    }

    // Setter for start date
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    // Getter for end date
    public String getEndDate() {
        return endDate;
    }

    // Setter for end date
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // Getter for seats left    maybe delete later
   public String getSeatsLeft(){
       return (TOTAL_SEATS - ticketsSold) + "/" + TOTAL_SEATS;
   }

    // Getter for total seats
    public int getTotalSeats() {
        return TOTAL_SEATS;
    }

    // Getter for tickets sold
    public int getTicketsSold() {
        return ticketsSold;
    }

    // Setter for tickets sold
    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    // Getter for seat availability
    public Map<String, Boolean> getSeatAvailability() {
        return seatAvailability;
    }

    // Setter for seat availability
    public void setSeatAvailability(Map<String, Boolean> seatAvailability) {
        this.seatAvailability = seatAvailability;
    }
}