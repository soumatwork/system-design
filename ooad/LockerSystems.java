class LockerSystems {
	List<LockerSystem> systems;
}

class LockerSystem {
	List<Locker> lockers = new ArrayList<>();
	
	public Locker findLocker(Package pkg) {
	}
	
	public Package openLocker(String code) {
	}
}

class Locker {
	Size size;
	Location location;
	Package pkg;
	
	public void setPackage(Package pkg) {
		
	}
}

class Location {
	int x;
	int y;
}

class Size {
	int width;
	int height;
	int length;
}

class Shipment {
	Order order;
	List<Package> packages;
}

class Order {
	User user;
}

class Package {
	Location location;
	Size size;
}

class User {
	String name;
	String address;
}
