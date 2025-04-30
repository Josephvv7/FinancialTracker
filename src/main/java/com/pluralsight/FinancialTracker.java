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
    // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
    // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
    // The amount should be a positive number.
    // After validating the input, a new `Transaction` object should be created with the entered values.
    // The new deposit should be added to the `transactions` ArrayList.

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

        System.out.println("Enter Amount: ");
        double amount = Double.parseDouble(scanner.nextLine()) * -1;

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
            System.out.println("Error Saving To File: ");
            e.printStackTrace();
        }
    }

    // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
    // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
    // The amount received should be a positive number then transformed to a negative number.
    // After validating the input, a new `Transaction` object should be created with the entered values.
    // The new payment should be added to the `transactions` ArrayList.

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

    // This method should display a table of all transactions in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.
    private static void displayLedger() {
        System.out.println("\nLEDGER");
        System.out.println("DATE       | TIME     | DESCRIPTION          | VENDOR            | AMOUNT   ");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            System.out.printf("%-12s | %-10s | %-30s | %-20s | %10.2f\n",
                    transaction.getDate().format(DATE_FORMATTER),
                    transaction.getTime().format(TIME_FORMATTER),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
//            System.out.println(transaction);
    }

    // This method should display a table of all deposits in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.
    private static void displayDeposits() {
        System.out.println("\nDEPOSITS");
        System.out.println("DATE       | TIME     | DESCRIPTION          | VENDOR            | AMOUNT   ");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %10.2f\n",
                        transaction.getDate().format(DATE_FORMATTER),
                        transaction.getTime().format(TIME_FORMATTER),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }

    // This method should display a table of all payments in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.
    private static void displayPayments() {
        System.out.println("\nPAYMENTS");
        System.out.println("DATE       | TIME     | DESCRIPTION          | VENDOR            | AMOUNT   ");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.printf("%-12s | %-10s | %-30s | %-20s | %10.2f\n",
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
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
                    filterTransactionsByDate(thisMonth, LocalDate.now());

                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate firstOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate lastOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(31);
                    filterTransactionsByDate(firstOfLastMonth, lastOfLastMonth);
                    break;
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate thisYear = LocalDate.now().withDayOfYear(1);
                    filterTransactionsByDate(thisYear, LocalDate.now());
                    break;
                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate firstOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(1);
                    LocalDate lastOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(365);
                    filterTransactionsByDate(firstOfLastYear, lastOfLastYear);
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
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

        for (Transaction transaction : transactions)
            if (transaction.getDate().isEqual(startDate) || transaction.getDate().isAfter(startDate) && transaction.getDate().isBefore(endDate) || transaction.getDate().isEqual(endDate)) {
                dateFilter = true;
                System.out.println(transaction);
            }

        if (!dateFilter) {
            System.out.println("No Transactions Found!");
        }

        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {

        boolean vendorFilter = false;
        for (Transaction transaction : transactions){
            if (transaction.getVendor().equalsIgnoreCase(vendor)){
                vendorFilter = true;

            }
        }
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}
