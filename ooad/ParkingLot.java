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
        List<Size> sizes = Arrays.asList(Size.SMALL, Size.MED, Size.LARGE, Size.XLARGE);
        
        for(Size size: sizes) {
            if(vehicle.size.sizeId < size.sizeId) {
                Stack<Spot> availableSpots = stacks.get(vehicle.size);
                
                if(!availableSpots.isEmpty()) {
                    Spot spot = availableSpots.pop();
                    spotsInUse.put(vehicle.vehicleNumber, spot);
                    return new Ticket(spot, vehicle.vehicleNumber);
                }
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
    SMALL(1), MED(2), LARGE(3), XLARGE(4);
    int sizeId;
    
    Size(int sizeId) {
        this.sizeId = sizeId;
    }
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
