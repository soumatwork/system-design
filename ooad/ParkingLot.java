class ParkingLot {
    
    Map<VehicleType, Stack<Spot>> availableSpots = new HashMap<>();
    Map<VehicleType, Stack<Spot>> notAvailableSpots = new HashMap<>();
    
    Map<String, Integer> availabilityPerFloor = new ConcurrentHashMap<>();
    
    public ParkingLot() {
        initialiseSpots(spots);
        initialiseAvailableSpots(availableSlots);
    }
    
    public Map<String, Integer> avilableSlots() {
        return availabilityPerFloor;
    }
    
    public Ticket enterVehicle(Vehicle vehicle) {
        Stack<Spot> stack = availableSpots.get(vehicle.type);
        
        if(!stack.isEmpty()) {
            Spot spot = stack.pop();
            notAvailableSpots.get(vehicle.type).push(spot);
            return new Ticket(spot, vehicle.vehicleNumber);
        }
        
        return null;
    }
    
    public boolean exitVehicle(Ticket ticket, Vehicle vehicle) {
        Stack<Spot> stack = availableSpots.get(vehicle.type);
    }
    
//     private boolean sizeMatched(Size vehicle, Size spot) {
//         return (vehicle.width + 2) < spot.width && (vehicle.length + 2) < spot.length;
//     }
    
}

class Vehicle {
    String vehicleNumber;
    VehicleType type;
}

enum VehicleType {
    CAR, TRUCK, BUS
}

class Spot {
    int code;
    int floor;
    Set<VehicleType> types; 
    
    public Spot(int code, int floor, Size size, Set<VehicleType> types) {
        this.code = code;
        this.floor = floor;
        this.size = size;
        this.types = types; 
    }
}

class Ticket {
    Spot spot;
    String vehicleNumber;
    
    public Ticket(Spot spot, String vehicleNumber) {
        ....
    }
}
