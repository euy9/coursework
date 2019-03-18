package project3;

import java.util.Scanner;

public class CarTracker {
static PQ pq = new PQ();
    
    public static void main(String[] args) {
        Scanner s = new Scanner (System.in);
        
        /* initialize car data */
        
        while (true){
            // options 1-8
            System.out.println("\nChoose an option: ");
            System.out.println("\t1.\tAdd a car");
            System.out.println("\t2.\tUpdate a car");
            System.out.println("\t3.\tRemove a specific car");
            System.out.println("\t4.\tRetrieve the lowest price car");
            System.out.println("\t5.\tRetrieve the lowest mileage car");
            System.out.println("\t6.\tRetrieve the lowest price car by make and model");
            System.out.println("\t7.\tRetrieve the lowest mileage car by make and model");
            System.out.println("\t8.\tExit");

            int option = s.nextInt();

            switch (option){
                case 1:     add();                  break;
                case 2:     update();               break;
                case 3:     remove();               break;
                case 4:     retrievePrice();        break;
                case 5:     retrieveMile();         break;
                case 6:     retrievePriceMake();    break;
                case 7:     retrieveMileMake();     break;
                case 8:     System.exit(0);         break;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        }
    }
    
    public static void add(){
        Scanner s = new Scanner(System.in);
        System.out.println("You have chosen:\t1. Add a car");
        System.out.print("Enter VIN number: ");
        String vin = s.nextLine();
        System.out.print("Enter the car's make: ");
        String make = s.nextLine();
        System.out.print("Enter the car's model: ");
        String model = s.nextLine();
        System.out.print("Enter the price to purchase: ");
        int price = s.nextInt();
        System.out.print("Enter the mileage of the car: ");
        int mile = s.nextInt();
        s.nextLine();
        System.out.print("Enter the color of the car: ");
        String color = s.nextLine();
        
        pq.add(vin.toUpperCase(), make, model, price, mile, color);
    }
    
    public static void update(){
        Scanner s = new Scanner(System.in);
        System.out.println("You have chosen:\t2. Update a car");
        System.out.println("Enter the VIN number of a car to update: ");
        String v = s.nextLine();
        if (!pq.check(v.toUpperCase())){
            System.out.println("Not a valid VIN number.");
            return;
        }
        System.out.println("What would you like to update?");
        System.out.println("\t1.\tThe price of the car");
        System.out.println("\t2.\tThe mileage of the car");
        System.out.println("\t3.\tThe color of the car");
        int option = s.nextInt();
        switch (option){
            case 1:
                System.out.print("Enter the new price: ");
                int p = s.nextInt();
                pq.updatePrice(v.toUpperCase(), p);
                break;
            case 2:
                System.out.print("Enter the new mileage: ");
                int m = s.nextInt();
                pq.updateMile(v.toUpperCase(), m);
                break;
            case 3:
                System.out.print("Enter the new color: ");
                s.nextLine();
                String color = s.nextLine();
                pq.updateColor(v.toUpperCase(), color);
                break;
            default:
                System.out.println("You did not enter a valid option");
                break;
        }
    }
    
    public static void remove(){
        System.out.println("You have chosen:\t3. Remove a specific car");
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the VIN number of a car to remove: ");
        String v = s.nextLine();
        if (!pq.check(v)){
            System.out.println("Unsuccessful removal: Not an existing VIN number.");
            return;
        }
        pq.remove(v.toUpperCase());
        System.out.println("Successfully removed.");
    }
    
    public static void retrievePrice(){
        System.out.println("You have chosen:\t4.Retrieve the lowest price car");
        Car c = pq.rPrice();
        if (c == null){
            System.out.println("No car exists in the system.");
            return;
        }
        System.out.println("VIN Number:\t" + c.vin.toUpperCase());
        System.out.println("Make:\t\t" + c.make);
        System.out.println("Model:\t\t" + c.model);
        System.out.println("Price:\t\t$" + c.price);
        System.out.println("Mileage:\t" + c.mileage);
        System.out.println("Color:\t\t" + c.color);
    }
    
    public static void retrieveMile(){
        System.out.println("You have chosen:\t5. Retrieve the lowest mileage car");
        Car c = pq.rMile();
        if (c == null){
            System.out.println("No car exists in the system.");
            return;
        }
        System.out.println("VIN Number:\t" + c.vin.toUpperCase());
        System.out.println("Make:\t\t" + c.make);
        System.out.println("Model:\t\t" + c.model);
        System.out.println("Price:\t\t$" + c.price);
        System.out.println("Mileage:\t" + c.mileage);
        System.out.println("Color:\t\t" + c.color);
    }
    
    public static void retrievePriceMake(){
        System.out.println("You have chosen:\t6. Retrieve the lowest price car by make and model");
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the make: ");
        String make = s.nextLine();
        System.out.println("Enter the model: ");
        String model = s.nextLine();
        Car c = pq.rPriceMM(make+model);
        if (c == null){
            System.out.println("No car exists in the system.");
            return;
        }
        System.out.println("VIN Number:\t" + c.vin.toUpperCase());
        System.out.println("Make:\t\t" + c.make);
        System.out.println("Model:\t\t" + c.model);
        System.out.println("Price:\t\t$" + c.price);
        System.out.println("Mileage:\t" + c.mileage);
        System.out.println("Color:\t\t" + c.color);
    }
    
    public static void retrieveMileMake(){
        System.out.println("You have chosen:\t7. Retrieve the lowest mileage car by make and model");
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the make: ");
        String make = s.nextLine();
        System.out.println("Enter the model: ");
        String model = s.nextLine();
        Car c = pq.rMileMM(make+model);
        if (c == null){
            System.out.println("No car exists in the system.");
            return;
        }
        System.out.println("VIN Number:\t" + c.vin.toUpperCase());
        System.out.println("Make:\t\t" + c.make);
        System.out.println("Model:\t\t" + c.model);
        System.out.println("Price:\t\t$" + c.price);
        System.out.println("Mileage:\t" + c.mileage);
        System.out.println("Color:\t\t" + c.color);
    }
    
}
