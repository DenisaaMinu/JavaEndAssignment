package nl.inholland.nl.denisaminu720645endassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private final Map<String, User> users;
    private final List<Ticket> tickets;
    private final ObservableList<Showing> showings;
    private final ObservableList<SalesHistory> salesHistories;
    private Logger LOGGER = Logger.getLogger(Database.class.getName());

    public Database() {
        showings = FXCollections.observableArrayList();
        tickets = new ArrayList<>();
        users = new HashMap<>();
        salesHistories = FXCollections.observableArrayList();
        initializeCollections();
        loadShowingsFromFile();
        loadSalesHistoryFromFile();
    }

    private void initializeCollections() {
        // Initialize users
        users.put("admin", new User("admin", "password", "Management"));
        users.put("user", new User("user", "password", "Sales"));
        users.put("user2", new User("user2", "password2", "User"));

        // Initialize showings
        showings.add(new Showing("Rebel Moon - Part one: The Scargiver", "04-10-2024 14:00", "04-10-2024 16:30"));
        showings.add(new Showing("Rebel Moon - Part two: The Man", "05-10-2024 14:00", "05-10-2024 16:30"));
        showings.add(new Showing("Rebel Moon - Part three: The Woman", "06-10-2024 14:00", "06-10-2024 16:30"));

        // Initialize tickets
        tickets.add(new Ticket("04-10-2024 14:00", "04-10-2024 16:30","Rebel Moon - Part Two: The Scargiver", 5));
        tickets.add(new Ticket("04-10-2024 14:00", "04-10-2024 16:30","Rebel Moon - Part Two: The Scargiver", 5));
        tickets.add(new Ticket("04-10-2024 14:00", "04-10-2024 16:30","Rebel Moon - Part Two: The Scargiver", 5));
    }

    // Method to load showings from a file
    public void loadShowingsFromFile() {
        File file = new File("showings.dat");

        if (!file.exists() || file.length() == 0) {
            LOGGER.info("No showings file found or the file is empty. Starting with an empty list.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Showing> loadedShowings = (List<Showing>) ois.readObject();
            loadedShowings.forEach(this::addShowing);
            LOGGER.info("Showings loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to load showings.dat", e);
        }
    }

    public void saveShowingsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("showings.dat"))) {
            oos.writeObject(new ArrayList<>(showings));
            LOGGER.info("Showings saved.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save showings.dat", e);
        }
    }

    public void addShowing(Showing showing) {
        if (showings.stream().anyMatch(s -> s.equals(showing))) {
            LOGGER.info("Showing already exists, skipping addition.");
            return;
        }
        showings.add(showing);
        saveShowingsToFile();
    }

    public void deleteShowing(Showing showing){
        showings.remove(showing);
        saveShowingsToFile();
    }

    public ObservableList<Showing> getShowings() {
        return showings;
    }

    public User getUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void updateShowing(Showing newShowing) {
        for(int i = 0; i < showings.size(); i++) {
            Showing showing = showings.get(i);

            if (showing.getTitle().equals(newShowing.getTitle()) && showing.getStartDate().equals(newShowing.getStartDate()) && showing.getEndDate().equals(newShowing.getEndDate())){
                // Replace the showing
                showings.set(i, newShowing);
            }
        }
    }

    public ObservableList<Showing> getAvailableShowings() {
        ObservableList<Showing> showings = FXCollections.observableArrayList(getShowings()); // Assuming getShowings() returns all showings
        LocalDateTime now = LocalDateTime.now();

        showings.removeIf(showing -> {
            LocalDateTime showingDate = LocalDateTime.parse(showing.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            boolean isInThePast = showingDate.isBefore(now);  // Exclude past showings
            boolean isSoldOut = showing.getTicketsSold() >= showing.getTotalSeats(); // Exclude sold-out showings
            return isInThePast || isSoldOut;
        });

        showings.sort(Comparator.comparing(showing -> LocalDateTime.parse(showing.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        return showings;
    }

    public void addSalesHistory(SalesHistory newSalesHistory) {
        salesHistories.add(newSalesHistory);  // Add SalesHistory, not Showing
        saveSalesHistoryToFile();
    }

    public void loadSalesHistoryFromFile() {
        File file = new File("salesHistory.dat");

        // Check if file exists and is not empty
        if (!file.exists() || file.length() == 0) {
            System.out.println("No sales history file found or the file is empty. Starting with an empty history.");
            return;  // No file to load from, so skip reading
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            List<SalesHistory> loadedSalesHistory = (List<SalesHistory>) ois.readObject();
            salesHistories.addAll(loadedSalesHistory);
            System.out.println("Sales history loaded from file.");

        } catch (EOFException e) {
            // Handle empty or corrupt file
            System.out.println("Sales history file is empty or corrupted. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
           LOGGER.log(Level.SEVERE, "Failed to load salesHistory.dat", e);
        }
    }


    // Method to save sales history to a file
    public void saveSalesHistoryToFile() {
        try (FileOutputStream fos = new FileOutputStream("salesHistory.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(new ArrayList<>(salesHistories));  // Save sales history
            System.out.println("Sales history saved.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save salesHistory.dat", e);
        }
    }

    public ObservableList<SalesHistory> getSalesHistoryFromFile() {
        return salesHistories;
    }
}