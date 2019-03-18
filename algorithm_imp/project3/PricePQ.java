package project3;

// for pq of make and model

import java.util.ArrayList;
import java.util.HashMap;

public class PricePQ {
    // constructor
    int n;
    ArrayList<Car> pq;
    HashMap qp;
    
    public PricePQ() {
        n = 0;
        pq = new ArrayList<>();     // index -> car
        pq.add(n, null);
        qp = new HashMap();         // car.vin -> index of pq
    }
    
    // helper methods
    private boolean greater(int i, int j){
        return ((pq.get(i).price) > (pq.get(j).price));
    }
    
    private void exch(int i, int j){
        Car swap = pq.get(i);
        pq.set(i, pq.get(j));
        pq.set(j, swap);
        qp.put(pq.get(i).vin, i);
        qp.put(pq.get(j).vin, j);
    }
    
    void swim (int k){
        while (k > 1 && greater(k/2, k)){
            exch(k, k/2);
            k = k/2;
        }
    }
    
    void sink (int k){
        while (2*k <= n){
            int j = 2*k;
            if (j < n && greater(j, j+1))   
                j++;
            if (!greater(k, j))
                break;
            exch(k, j);
            k = j;
        }
    }
    
    // add a car
    public void add(Car c) {
        n++;
        pq.add(c);
        qp.put(c.vin, n);
        swim (n);
    }    
    
    public boolean check (String v){
        return qp.containsKey(v);
    }
    
    public String getMM (String v){
        int i = (int)qp.get(v);
        Car c = (Car) pq.get(i);
        return (c.make + c.model);
    }
    
    public boolean isEmpty() {
        return n == 0;
    }
    
    // update a car
    public void updatePrice (String v, int p) {   // update price
        int i = (int)qp.get(v);
        pq.get(i).price = p;
        swim(i);
        sink(i);
    }
    public void updateMile (String v, int m) {           // update mileage
        int i = (int)qp.get(v);
        pq.get(i).mileage = m;
        swim(i);
        sink(i);
    }
    public void updateColor (String v, String c) {           // update color
        int i = (int)qp.get(v);
        pq.get(i).color = c;
        swim(i);
        sink(i);
    }
    
    // remove a specific car
    public void remove (String v) {
        int index = (int)qp.get(v);
        exch(index, n--);
        swim(index);
        sink(index);
        pq.remove(n);
        qp.remove(v);
    }
    
    // retrieve the lowest price car
    public Car retrieve () {
        return pq.get(1);
    }
}
