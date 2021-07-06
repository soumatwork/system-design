class LockerSystems {
	List<LockerSystem> systems;
	
	public LockerSystem findLocker(Shipment shipment) {
		//find locker system based on location;
		// you could use QuadTree to store 
	}
}

class UsersRepo {
	Map<String, User> users = new HashMap<>();
	
	public User findUser(String registrationId) {
		return users.get(registrationId);
	}
}

class LockerSystem {
	List<Locker> lockers = new ArrayList<>();
	Map<String, Locker> usedLocker = new HashMap<>();
	UsersRepo userRepo;
	
	public LockerSystem(UsersRepo userRepo, int numberOfLockers) {
		this.userRepo = userRepo;
		
		for(int i=0; i < numberOfLockers; i++) {
			lockers.add(new Locker());
		}
	}
	
	public boolean findLocker(Package pkg) {
		if(!userRepo.containsKey(pkg.registrationId)) {
			return false;
		}
		
		Locker selectedLocker = null;
		
		for(Locker locker: lockers) {
			if(!locker.inUse() && sizeMatched(pkg.size, locker.size)) {
				selectedLocker = locker;
			}
		}
		
		if(selectedLocker == null) {
			return false;
		}
		
		String code = generateCode();
		usedLocker.put(code, selectedLocker);
		userRepo.get(pkg.registrationId).notify(code);
	}
	
	public Package openLocker(String code) {
		if(usedLocker.containsKey(code)) {
			Locker locker = usedLocker.get(code);
			return locker.returnPackage();
		}
		return null;
	}
	
	private String generateCode() {
		UUID uuid = UUID.randomUUID();
		String code = uuid.toString();
		
		while(usedLocker.containsKey(code)) {
			code = UUID.randomUUID().toString();
		}
		
		return code;
	}
	
	private boolean sizeMatched(Size size, Size container) {
		return container.height > size.height && container.width > size.width && container.length > size.length;
	}
}

class Locker {
	Size size;
	Location location;
	Package pkg;
	
	public boolean inUse() {
		return pkg != null;
	}
	
	public void setPackage(Package pkg) {
		this.pkg = pkg;
	}
	
	public Package returnPackage() {
		Package toReturn = pkg;
		pkg = null;
		return toReturn;
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
	String name;
	String registrationId;
	Location location;
	Size size;
}

class User {
	String name;
	String address;
	NotificationMethod notificationMethod;
	
	public void notify(String code) {
		notificationMethod.notify(code);
	}
}
