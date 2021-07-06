class Elevator {
  int currentFloor = 0;
  Direction currentDirection = Direction.UP;
  State currentState = State.IDLE;

  
  //jobs which are being processed
  private Set<Request> currentJobs = new TreeSet<>();
  /**
   * up jobs which cannot be processed now so put in pending queue
   */
  private Set<Request> upPendingJobs = new TreeSet<>();
  /**
   * down jobs which cannot be processed now so put in pending queue
   */
  private Set<Request> downPendingJobs = new TreeSet<>();

  public void startElevator() {
    while (true) {

      if (!currentJobs.isEmpty()) {

        if (currentDirection == Direction.UP) {
          Request request = currentJobs.pollFirst();
          processUpRequest(request);

          if (currentJobs.isEmpty()) {
            addPendingDownJobsToCurrentJobs();

          }

        }
        if (currentDirection == Direction.DOWN) {
          Request request = currentJobs.pollLast();
          processDownRequest(request);

          if (currentJobs.isEmpty()) {
            addPendingUpJobsToCurrentJobs();
          }

        }
      }
    }
  }


  private void processUpRequest(Request request) {
    int startFloor = currentFloor;

    if (startFloor < request.sourceFloor) {
      for (int i = startFloor; i <= request.sourceFloor; i++) {        
        currentFloor = i;
      }
    }

    // The elevator is now on the floor where the person has requested it i.e. source floor. User can enter and go to the destination floor.

    startFloor = currentFloor;

    for (int i = startFloor; i <= request.destinationFloor; i++) {
      currentFloor = i;

      if (checkIfNewJobCanBeProcessed(request)) {
        break;
      }
    }

  }

  private void processDownRequest(Request request) {

    int startFloor = currentFloor;

    if (startFloor < request.externalRequest.sourceFloor) {
      for (int i = startFloor; i <= request.sourceFloor; i++) {
        currentFloor = i;
      }
    }

    startFloor = currentFloor;

    for (int i = startFloor; i >= request.destinationFloor; i--) {
      currentFloor = i;

      if (checkIfNewJobCanBeProcessed(request)) {
        break;
      }
    }

  }

  private boolean checkIfNewJobCanBeProcessed(Request currentRequest) {
    if (!currentJobs.isEmpty()) {

      if (currentDirection == Direction.UP) {
        Request request = currentJobs.pollFirst();
        
        if (request.destinationFloor < currentRequest.destinationFloor) {
          currentJobs.add(request);
          currentJobs.add(currentRequest);
          return true;
        }

        currentJobs.add(request);
      }

      if (currentDirection == Direction.DOWN) {
        Request request = currentJobs.pollLast();

        if (request.destinationFloor > currentRequest.destinationFloor) {
          currentJobs.add(request);
          currentJobs.add(currentRequest);
          return true;
        }

        currentJobs.add(request);

      }

    }

    return false;
  }

  private void addPendingDownJobsToCurrentJobs() {
    if (!downPendingJobs.isEmpty()) {
      currentJobs = downPendingJobs;
      currentDirection = Direction.DOWN;

    } else {
      currentState = State.IDLE;
    }

  }

  private void addPendingUpJobsToCurrentJobs() {
    if (!upPendingJobs.isEmpty()) {
      currentJobs = upPendingJobs;
      currentDirection = Direction.UP;
    } else {
      currentState = State.IDLE;
    }

  }

  public void addJob(Request request) {
    if (currentState == State.IDLE) {
      currentState = State.MOVING;
      currentDirection = request.directionToGo;
      currentJobs.add(request);

    } else if (currentState == State.MOVING) {

      if (request.directionToGo != currentDirection) {
        addtoPendingJobs(request);

      } else if (request.externalRequest.directionToGo == currentDirection) {
        if (currentDirection == Direction.UP && request.destinationFloor < currentFloor) {
          addtoPendingJobs(request);

        } else if (currentDirection == Direction.DOWN && request.destinationFloor > currentFloor) {
          addtoPendingJobs(request);

        } else {
          currentJobs.add(request);
        }

      }

    }

  }

  public void addtoPendingJobs(Request request) {
    if (request.directionToGo == Direction.UP) {
      upPendingJobs.add(request);

    } else {
      downPendingJobs.add(request);
    }
  }

}

class ProcessJobWorker implements Runnable {

  private Elevator elevator;

  ProcessJobWorker(Elevator elevator) {
    this.elevator = elevator;
  }

  @Override
  public void run() {
    /**
     * start the elevator
     */
    elevator.startElevator();
  }

}

enum State {
  MOVING, STOPPED, IDLE
}

enum Direction {
  UP, DOWN
}

class Request implements Comparable<Request> {
  int destinationFloor;
  int sourceFloor;
  Direction directionToGo;
  
  public Request(int destinationFloor, int sourceFloor, Direction directionToGo) {
    this.destinationFloor = destinationFloor;
    this.sourceFloor = sourceFloor;
    this.directionToGo = directionToGo;
  }

  @Override
  public int compareTo(Request req) {
    if (this.destinationFloor == req.destinationFloor)
      return 0;
    else if (this.destinationFloor > req.destinationFloor)
      return 1;
    else
      return -1;
  }
}

class AddJobWorker implements Runnable {

  private Elevator elevator;
  private Request request;

  AddJobWorker(Elevator elevator, Request request) {
    this.elevator = elevator;
    this.request = request;
  }

  @Override
  public void run() {

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    elevator.addJob(request);
  }

}


public class TestElevator {
  public static void main(String args[]) {
    Elevator elevator = new Elevator();
    
        /**
     * Thread for starting the elevator
     */
    ProcessJobWorker processJobWorker = new ProcessJobWorker(elevator);
    
    Thread t2 = new Thread(processJobWorker);
    t2.start();

    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    //person wants to go in up direction from source floor 0
    ExternalRequest er = new ExternalRequest(Direction.UP, 0);
    
    //the destination floor is 5
    InternalRequest ir = new InternalRequest(5);
    Request request1 = new Request(ir, er);

    /**
     * Pass job to the elevator
     */
    new Thread(new AddJobWorker(elevator, request1)).start();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }    
  }
}
