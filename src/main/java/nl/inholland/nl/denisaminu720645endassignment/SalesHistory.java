package nl.inholland.nl.denisaminu720645endassignment;

import java.io.Serial;
import java.io.Serializable;

public class SalesHistory implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    protected String dateTime;
    protected int numberOfTickets;
    protected String customerName;
    protected Showing showing;

    public SalesHistory(String dateTime, int numberOfTickets, String customerName, Showing showing) {
        this.dateTime = dateTime;
        this.numberOfTickets = numberOfTickets;
        this.customerName = customerName;
        this.showing = showing;
    }

    // Getter for dateTime
    public String getDateTime() {
        return dateTime;
    }

    // Setter for dateTime
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    // Getter for numberOfTickets
    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    // Setter for numberOfTickets with validation
    public void setNumberOfTickets(int numberOfTickets) {
        if (numberOfTickets < 0) {
            throw new IllegalArgumentException("Number of tickets cannot be negative");
        }
        this.numberOfTickets = numberOfTickets;
    }

    // Getter for customerName
    public String getCustomerName() {
        return customerName;
    }

    // Setter for customerName with validation
    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        this.customerName = customerName;
    }

    // Getter for showing
    public Showing getShowing() {
        return showing;
    }

    // Setter for showing
    public void setShowing(Showing showing) {
        this.showing = showing;
    }
}
