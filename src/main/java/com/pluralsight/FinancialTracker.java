package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                transactions.add(new Transaction(date, time, description, vendor, amount));
            }

            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Error Saving To File!");
        }
    }
    // If the file does not exist, it should be created.
    // The transactions should be stored in the `transactions` ArrayList.
    // Each line of the file represents a single transaction in the following format:
    // <date>|<time>|<description>|<vendor>|<amount>
    // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
    // After reading all the transactions, the file should be closed.
    // If any errors occur, an appropriate error message should be displayed.

    private static void addDeposit(Scanner scanner) {

        System.out.println("\nAdd Deposit");
        LocalDate date = null;
        LocalTime time = null;
        while (date == null) {
            try {
                System.out.println("Enter Date (yyyy-MM-dd): ");
                date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
            } catch (Exception e) {
                System.out.println("Date Invalid! Please Try Again.");
            }
        }
        while (time == null) {
            try {
                System.out.println("Enter Time (HH:mm:ss): ");
                time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
            } catch (Exception e) {
                System.out.println("Time Invalid! Please Try Again.");
            }
        }
        System.out.println("Enter Description: ");
        String description = scanner.nextLine();

        System.out.println("Enter Vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Enter Amount: ");
        double amount = Math.abs(Double.parseDouble(scanner.nextLine()));

//            if (amount <= 0) {
//                amount = Double.parseDouble(scanner.nextLine()) * -1;
//            }
        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(newTransaction);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.newLine();
            writer.write(newTransaction.toString());
            System.out.println("Deposit Added");
            writer.close();

        } catch (Exception e) {
            System.out.println("Error Saving To File!" + e.getMessage());
        }
    }
    private static void addPayment(Scanner scanner) {
        System.out.println("\nAdd Payment");
        LocalDate date = null;
        LocalTime time = null;
        while (date == null) {
            try {
                System.out.println("Enter Date (yyyy-MM-dd): ");
                date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
            } catch (Exception e) {
                System.out.println("Date Invalid! Please Try Again.");
            }
        }
        while (time == null) {
            try {
                System.out.println("Enter Time (HH:mm:ss): ");
                time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);
            } catch (Exception e) {
                System.out.println("Time Invalid! Please Try Again.");
            }
        }
        System.out.println("Enter Description: ");
        String description = scanner.nextLine();

        System.out.println("Enter Vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Enter Amount As Negative Number: ");
        double amount = -Math.abs(Double.parseDouble(scanner.nextLine()));

//            if (amount <= 0) {
//                System.out.println("Deposit Amount Must Be Positive");
//                return;
//            }
//            amount = amount * -1;
        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(newTransaction);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.newLine();
            writer.write(newTransaction.toString());
            System.out.println("Payment Added");
            writer.close();

        } catch (Exception e) {
            System.out.println("Error Saving To File: " + e.getMessage());

        }
    }
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    private static void displayLedger() {
        System.out.println("\nLEDGER");
        System.out.println("DATE         | TIME       | DESCRIPTION                    | VENDOR               | AMOUNT   ");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %.2f\n",
                    transaction.getDate().format(DATE_FORMATTER),
                    transaction.getTime().format(TIME_FORMATTER),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
//            System.out.println(transaction);
    }
    private static void displayDeposits() {
        System.out.println("\nDEPOSITS");
        System.out.println("DATE         | TIME       | DESCRIPTION                    | VENDOR               | AMOUNT   ");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %.2f\n",
                        transaction.getDate().format(DATE_FORMATTER),
                        transaction.getTime().format(TIME_FORMATTER),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }
    private static void displayPayments() {
        System.out.println("\nPAYMENTS");
        System.out.println("DATE         | TIME       | DESCRIPTION                    | VENDOR               | AMOUNT   ");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %.2f\n",
                        transaction.getDate().format(DATE_FORMATTER),
                        transaction.getTime().format(TIME_FORMATTER),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
                    filterTransactionsByDate(thisMonth, LocalDate.now());

                    break;
                case "2":
                    LocalDate firstOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);

                    //lengthOfMonth() = returns the correct number of days for any month (28-31)
                    LocalDate lastOfLastMonth = firstOfLastMonth.withDayOfMonth(firstOfLastMonth.lengthOfMonth());
                    filterTransactionsByDate(firstOfLastMonth, lastOfLastMonth);
                    break;
                case "3":
                    LocalDate thisYear = LocalDate.now().withDayOfYear(1);
                    filterTransactionsByDate(thisYear, LocalDate.now());
                    break;
                case "4":
                    LocalDate firstOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(1);
                    LocalDate lastOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(firstOfLastYear.lengthOfYear()); //This checks for leap years
                    filterTransactionsByDate(firstOfLastYear, lastOfLastYear);
                    break;
                case "5":
                    System.out.println("\nEnter Vendor Name: ");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                    break;
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        boolean dateFilter = false;
        System.out.println("\nTransactions From " + startDate + " To " + endDate);
        System.out.println("DATE         | TIME       | DESCRIPTION                    | VENDOR               | AMOUNT   ");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getDate().isEqual(startDate) || transaction.getDate().isAfter(startDate) && transaction.getDate().isBefore(endDate) || transaction.getDate().isEqual(endDate)) {
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %.2f\n",
                        transaction.getDate().format(DATE_FORMATTER),
                        transaction.getTime().format(TIME_FORMATTER),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                dateFilter = true;
            }
        }

        if (!dateFilter) {
            System.out.println("No Transactions Found!");
        }
    }

    private static void filterTransactionsByVendor(String vendor) {

        boolean vendorFilter = false;
        System.out.println("\nTRANSACTIONS FOUND FROM VENDOR: " + vendor);
        System.out.println("DATE         | TIME       | DESCRIPTION                    | VENDOR               | AMOUNT   ");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)){
                /*System.out.println(transaction);*/
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %.2f\n",
                        transaction.getDate().format(DATE_FORMATTER),
                        transaction.getTime().format(TIME_FORMATTER),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
                vendorFilter = true;

            }
        }

        if (!vendorFilter) {
            System.out.println("NO TRANSACTIONS FOUND FROM VENDOR: " + vendor);
        }
    }
}
