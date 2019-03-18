package project3;

import java.util.ArrayList;
import java.util.HashMap;

public class PQ {
    
    PricePQ pricePQ;
    MilePQ milePQ;
    HashMap mmPrice;
    HashMap mmMile;
    
    // constructor
    public PQ() {
        pricePQ = new PricePQ();    // Price Priority Queue
        milePQ = new MilePQ();      // Mileage Priority Queue
        mmPrice = new HashMap();     // keep track of make & model PQ
        mmMile = new HashMap();
    }
    
    // add a car
    public void add(String vin, String make, String model, int price, int mile, String color) {
        Car c = new Car(vin, make, model, price, mile, color);
        pricePQ.add(c);
        milePQ.add(c);
        if(mmPrice.containsKey(make+model) && mmMile.containsKey(make+model)) {
            ((PricePQ)mmPrice.get(make+model)).add(c);
            ((MilePQ)mmMile.get(make+model)).add(c);
        } else {
            PricePQ makePricePQ = new PricePQ();
            makePricePQ.add(c);
            mmPrice.put(make+model, makePricePQ);
            
            MilePQ makeMilePQ = new MilePQ();
            makeMilePQ.add(c);
            mmMile.put(make+model, makeMilePQ);
        }
    }   
    
    // check if the VIN is in the system.
    public boolean check (String v){
        return pricePQ.check(v);
    }
    
    // update a car
    public void updatePrice (String v, int p){
        pricePQ.updatePrice(v, p);
        milePQ.updatePrice(v, p);
        ((PricePQ)mmPrice.get(pricePQ.getMM(v))).updatePrice(v, p);
        ((MilePQ)mmMile.get(pricePQ.getMM(v))).updatePrice(v, p);
    }
    
    public void updateMile (String v, int m){
        pricePQ.updateMile(v, m);
        milePQ.updateMile(v, m);
        ((PricePQ)mmPrice.get(pricePQ.getMM(v))).updateMile(v, m);
        ((MilePQ)mmMile.get(pricePQ.getMM(v))).updateMile(v, m);
    }
    
    public void updateColor (String v, String c){
        pricePQ.updateColor(v, c);
        milePQ.updateColor(v, c);
        ((PricePQ)mmPrice.get(pricePQ.getMM(v))).updateColor(v, c);
        ((MilePQ)mmMile.get(pricePQ.getMM(v))).updateColor(v, c);
    }
    
    // remove a specific car
    public void remove (String v) {
        ((PricePQ)mmPrice.get(pricePQ.getMM(v))).remove(v);
        ((MilePQ)mmMile.get(pricePQ.getMM(v))).remove(v); 
        pricePQ.remove(v);
        milePQ.remove(v);
    }
    
    // retrieve the lowest price car
    public Car rPrice () {
        if (pricePQ.isEmpty())
            return null;
        return pricePQ.retrieve();
    }
    
    // retrieve the lowest mileage car
    public Car rMile () {
        if (milePQ.isEmpty())
            return null;
        return milePQ.retrieve();
    }
 
    // retrieve the lowest price car by make & model
    public Car rPriceMM (String mm) {
        if (pricePQ.isEmpty())
            return null;
        return ((PricePQ)(mmPrice.get(mm))).retrieve();
    }
    
    // retrieve the lowest mileage car by make & model
    public Car rMileMM (String mm) {
        if (pricePQ.isEmpty())
            return null;
        return ((MilePQ)(mmMile.get(mm))).retrieve();
    }
}
