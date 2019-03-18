package project3;

public class Car {
    String vin;
    String make;
    String model;
    int price;
    int mileage;
    String color;

    // constructors
     Car() {
        vin = "";
        make = "";
        model = "";
        price = 0;
        mileage = 0;
        color = "";
    }

     Car(String vinAdd, String makeAdd, String modelAdd, int priceAdd, int mileageAdd, String colorAdd){
        vin = vinAdd;
        make = makeAdd;
        model = modelAdd;
        price = priceAdd;
        mileage = mileageAdd;
        color = colorAdd;
    }
}
