class ElevatorSystem {
    List<Elevator> elevators = new ArrayList<>();
    Map<Integer, List<Request>> queues = Collections.synchronizedMap();
    int capacity;
    int floors;
    
    public ElevatorSystem(int capacity, int floors) {
        this.capacity = capacity;
        this.floors = floors;
        
        for(int i = 0; i < capacity; i ++) {
            elevators.add(new Elevator(queues));
        }
        
        for(int i = 0; i < floors; i ++) {
            queues.put(i + 1, new LinkedList<>());
        }
    }
    
    public void placeRequest(Request request) {
       queues.get(request.from).add(request); 
    }
}

class Request {
    int from;
    int to;
    
    public Request(int from, int to) {
        ...
    }
}

class Elevator implements Runnable {
    int currentFloor = 0;
    State currentState = State.IDLE;
    PriorityQueue<Integer> internal = new PriorityQueue<>();
    Map<Integer, List<Request>> queues = null;
    
    public Elevator(Map<Integer, List<Request>> queues) {
        this.queues = queues;
    }
    
    public void run() {
        while (true) {
            if(currentState == State.UP || currentState == State.DOWN) {
                List<Request> requests = queues.get(currentFloor);

                for(Iterator i = requests.iterator(); i.hasNext();) {
                    Request request = i.next();

                    if(currentState == State.UP) {
                        if(request.to > currentFloor) {
                            internal.add(request.to);
                            i.remove();
                        }
                    } else if (currentState == State.DOWN) {
                        if(request.to < currentFloor) {
                            internal.add(-1 * request.to);
                            i.remove();
                        }
                    }
                }                
            } else {
                Set<Integer> keys = queues.keySet();
                
                for(int floor: keys) {
                    List<Request> requests = queues.get(floor);
                    
                    for(Iterator i = requests.iterator(); i.hasNext();) {
                        Request request = i.remove();
                        move(request.from);
                        
                        if(request.to > currentFloor) {
                            internal.add(request.to);
                            currentState = State.UP;
                        } else {
                            internal.add(-1 * request.to);
                            currentState = State.DOWN;                            
                        }                       
                    }
                }
            }

            
            if(internal.isEmpty()) {
                currentState = State.IDLE;
            } else {
                move(Math.abs(internal.pop()));
            }
        }
    }
    
    public void move(int to) {
        currentFloor = to;
    }
}

enum State {
    IDLE,UP, DOWN;
}
