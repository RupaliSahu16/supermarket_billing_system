
import java.io.*;
import java.util.*;

public class main {

    static String[] queue = new String[100];
    static int front = 0, rear = -1, size = 0;

    static String[] item = new String[100];
    static int[] qty = new int[100];
    static int[] amt = new int[100];
    static int top = -1;

    static HashMap<String, Integer> price = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    static void loadProducts() {
        try {
            File f = new File("products.txt");

            if (!f.exists()) {
                FileWriter w = new FileWriter(f);
                w.write("Rice=60\nMilk=30\nBread=40\n");
                w.close();
            }

            Scanner s = new Scanner(f);

            while (s.hasNextLine()) {
                String[] p = s.nextLine().split("=");

                if (p.length == 2) {
                    price.put(p[0].trim().toLowerCase(),
                              Integer.parseInt(p[1].trim()));
                }
            }

            s.close();

        } catch (Exception e) {
            System.out.println("Error loading products.");
        }
    }

    static void addCustomer() {
        if (size == 100) {
            System.out.println("Queue is full.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Please enter a valid name.");
            return;
        }

        rear = (rear + 1) % 100;
        queue[rear] = name;
        size++;

        System.out.println(name + " added to billing queue.");
        System.out.println("Position: " + size);
    }

    static void viewQueue() {
        if (size == 0) {
            System.out.println("Billing queue is empty.");
            return;
        }

        System.out.println("\nCustomers Waiting:");

        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + ". " + queue[(front + i) % 100]);
        }
    }

    static void undo() {
        if (top == -1) {
            System.out.println("No item to remove.");
            return;
        }

        System.out.println(item[top] + " removed from bill.");
        top--;
    }

    static void saveBill(String name, int total) {
        try {
            FileWriter w = new FileWriter("bills.txt", true);

            w.write("Customer: " + name + "\n");

            for (int i = 0; i <= top; i++) {
                w.write(item[i] + " x " + qty[i] + " = Rs. " + amt[i] + "\n");
            }

            w.write("Total: Rs. " + total + "\n");
            w.write("-----------------------------\n");

            w.close();

        } catch (Exception e) {
            System.out.println("Error saving bill.");
        }
    }

    static void billCustomer() {
        if (size == 0) {
            System.out.println("No customers in the queue.");
            return;
        }

        String name = queue[front];
        front = (front + 1) % 100;
        size--;

        top = -1;

        System.out.println("\nNow serving: " + name);
        System.out.println("Available Products: Rice, Milk, Bread");

        while (true) {
            System.out.print("\nEnter item name (Undo/Done): ");
            String p = sc.nextLine().trim().toLowerCase();

            if (p.equals("done")) {
                break;
            }

            if (p.equals("undo")) {
                undo();
                continue;
            }

            if (!price.containsKey(p)) {
                String displayName = p.isEmpty() ? p :
                        p.substring(0, 1).toUpperCase() + p.substring(1);

                System.out.println("Item " + displayName + " not found in price list.");
                continue;
            }

            if (top == 99) {
                System.out.println("Bill item limit reached.");
                continue;
            }

            System.out.print("Enter quantity: ");

            int q;

            try {
                q = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a valid quantity.");
                continue;
            }

            if (q <= 0) {
                System.out.println("Quantity must be greater than zero.");
                continue;
            }

            top++;

            item[top] = p.substring(0, 1).toUpperCase() + p.substring(1);
            qty[top] = q;
            amt[top] = price.get(p) * q;

            System.out.println("Item added successfully.");
        }

        int total = 0;

        System.out.println("\n========== BILL ==========");
        System.out.println("Customer: " + name);
        System.out.println("--------------------------");

        for (int i = 0; i <= top; i++) {
            System.out.println(item[i] + " x " + qty[i] + " = Rs. " + amt[i]);
            total += amt[i];
        }

        System.out.println("--------------------------");
        System.out.println("Total Amount: Rs. " + total);
        System.out.println("==========================");

        saveBill(name, total);

        System.out.println("Bill saved successfully.");

        if (size == 0) {
            System.out.println("Billing queue is empty.");
        } else {
            System.out.println("Next customer: " + queue[front]);
        }
    }

    public static void main(String[] args) {

        loadProducts();

        while (true) {
            System.out.println("\n===== SUPERMARKET BILLING SYSTEM =====");
            System.out.println("1. Add Customer");
            System.out.println("2. Bill Next Customer");
            System.out.println("3. View Queue");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a valid choice.");
                continue;
            }

            switch (choice) {
                case 1:
                    addCustomer();
                    break;

                case 2:
                    billCustomer();
                    break;

                case 3:
                    viewQueue();
                    break;

                case 4:
                    System.out.println("Thank you for using Supermarket Billing System.");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}