class ElevatorSystem {
    List<Elevator> elevators = new ArrayList<>();
    Map<Integer, LinkedHashSet<Request>> queues = new LinkedHashMap<>();
    int capacity;
    
    public ElevatorSystem(int capacity, int floors) {
        this.capacity = capacity;
        
        for(int i = 0; i < capacity; i ++) {
            elevators.add(new Elevator());
        }
        
        for(int i = 0; i < floors; i ++) {
            queues.put(i + 1, new LinkedHashSet<>());
        }
    }
    
    public void placeRequest(Request request) {
       queues.get(request.toFloor).add(request); 
    }
}

class Request {
    int fromFloor;
    int toFloor;
    
    public Request(int fromFloor, int toFloor) {
        ...
    }
}

class Elevator {
    int floors = 0;
    int currentFloor = 0;
    State currentState = State.IDLE;
    PriorityQueue<Integer> internal = new PriorityQueue<>();
    Map<Integer, LinkedHashSet<Request>> queues = null;
    
    public Elevator(Map<Integer, LinkedHashSet<Request>> queues, int floors) {
        this.queues = queues;
        this.floors = floors;
    }
    
    public void startElevator() {
        while (true) {
            LinkedHashSet<Request> requests = queues.get(currentFloor);
                    
            for(Request request: requests) {
                if(currentState == State.UP) {
                    if(request.toFloor >= currentFloor) {
                        internal.add(request.toFloor);
                    }
                } else if (currentState == State.DOWN) {
                    if(request.toFloor <= currentFloor) {
                        internal.add(request.toFloor);
                    }
                } else {
                    if(request.toFloor <= currentFloor) {
                        currentState = State.DOWN;
                    } else {
                        currentState = State.UP;
                    }
                    internal.add(request.toFloor);
                }
            }
            
            if(internal.isEmpty()) {
                 currentState = State.IDLE;
            } else {
                move(internal.pop());
            }
        }
    }
}

enum State {
    IDLE,UP, DOWN;
}
