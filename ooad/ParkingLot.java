class ParkingLot {
    Map<Size, Stack<Spot>> stacks = new HashMap<>();
    Map<String, Spot> spotsInUse = new HashMap<>();
    Lock lock = new ReentrantLock();
    
    public ParkingLot() {
        initialiseStack(stack);
    }
    
    public Ticket enter(Vehicle vehicle) {
        lock.lock();
        try {
            return enterVehicle(vehicle);
        } finally {
            lock.unlock();
        }
    }
    
    private Ticket enterVehicle(Vehicle vehicle) {
        if(vehicle.size == SMALL) {
            Stack<Spot> availableSpots = stacks.get(SMALL);
            
            if(availableSpots.isEmpty()) {
              availableSpots = stacks.get(MED);  
            }
            if(availableSpots.isEmpty()) {
              availableSpots = stacks.get(LARGE);  
            }
            if(availableSpots.isEmpty()) {
              availableSpots = stacks.get(XLARGE);  
            }
            
            if(availableSpots.isEmpty()) {
                return null;    
            } else {
                Spot spot = availableSpots.pop();
                spotsInUse.put(vehicle.vehicleNumber, spot);
                return new Ticket(spot, vehicle.vehicleNumber);
            }
            
        } else if (vehicle.size == MED) {
            Stack<Spot> availableSpots = stacks.get(MED);
            
            if(availableSpots.isEmpty()) {
              availableSpots = stacks.get(LARGE);  
            }
            if(availableSpots.isEmpty()) {
              availableSpots = stacks.get(XLARGE);  
            }
            
            if(availableSpots.isEmpty()) {
                return null;    
            } else {
                Spot spot = availableSpots.pop();
                spotsInUse.put(vehicle.vehicleNumber, spot);
                return new Ticket(spot, vehicle.vehicleNumber);
            }
        } else if (vehicle.size == LARGE) {
            Stack<Spot> availableSpots = stacks.get(LARGE);
            
            if(availableSpots.isEmpty()) {
              availableSpots = stacks.get(XLARGE);  
            }
            
            if(availableSpots.isEmpty()) {
                return null;    
            } else {
                Spot spot = availableSpots.pop();
                spotsInUse.put(vehicle.vehicleNumber, spot);
                return new Ticket(spot, vehicle.vehicleNumber);
            }
        } else {
            Stack<Spot> availableSpots = stacks.get(XLARGE);
            
            if(availableSpots.isEmpty()) {
                return null;    
            } else {
                Spot spot = availableSpots.pop();
                spotsInUse.put(vehicle.vehicleNumber, spot);
                return new Ticket(spot, vehicle.vehicleNumber);
            }            
        }
        
        return null;
    }
    
    public boolean exitVehicle(Vehicle vehicle, PaymentMethod method, Ticket ticket) {
        boolean success = method.makePayment(calculatePrice(ticket));
        
        if(success) {
            Spot spot = spotsInUse.remove(vehicle.vehicleNumber);
            stacks.get(vehicle.size).push(spot);
        }
    }    
}

class Vehicle {
    String vehicleNumber;
    Size size;
}

enum Size {
    SMALL, MED, LARGE, XLARGE
}

class Spot {
    Long id;
    Size size;
}

class Ticket {
    Long spotId;
    String vehicleNumber;
    double price;
    Date createdAt;
    
    public Ticket(Spot spot, String vehicleNumber) {
        ....
    }
}
