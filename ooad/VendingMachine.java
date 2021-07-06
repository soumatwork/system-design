class VendingMachineSystem {

	int maxCodes;
	int maxProductForEachCode;
	Map<String, Queue<Product>> slots = new HashMap<>();
	
	public VendingMachineSystem(int maxCodes, int maxProductForEachCode) {
		this.maxCodes = maxCodes;
		this.maxProductForEachCode = maxProductForEachCode;
		
		for(int i = 1; i <= maxCodes; i++) {
			slots.put(String.valueOf(i), new LinkList<>(maxProductForEachCode));
		}
	}

	public boolean addProduct(String code, Product product) {
		// it can not be unlimited
		if(!slots.containsKey(code)) {
			return false;
		}
		
		Queue<Product> items = slots.get(code);

		if (items.size() > maxProductForEachCode) {
			items.offer(product);
			return true;
		}
		
		return false;
	}

	public double price(String code) {
		Queue<Product> selectedItems = slots.get(code);

		if (!selectedItems.isEmpty()) {
			return selectedItems.peek().price;
		}
		
		return 0.0;
	}

	public List<Product> checkout(CheckoutRequest checkoutRequest, PaymentMethod paymentMethod) {
		if (slots.get(checkoutRequest.code).size() < checkoutRequest.quantity) {
			return null;
		}

		boolean result = paymentMethod.checkout(price(checkoutRequest.code), checkoutRequest.quantity);
		
		List<Product> products = new ArrayList<>();
		
		if (result) {			
			for (int i = 0; i < checkoutRequest.quantity; i++) {
				products.add(slots.get(checkoutRequest.code).poll());
			}
		}
		
		return products;
	}
}

class CheckoutRequest {
	String code;
	int quantity;
}

// Following are the payment method
interface PaymentMethod {
	boolean checkout(double price, int quantity);
}

class CreditCard extends PaymentMethod {
	private PaymentGateway paymentGateway;
	private CardDetails cardDetails;

	public CreditCard(PaymentGateway paymentGateway, CardDetails cardDetails) {
		this.paymentGateway = paymentGateway;
		this.cardDetails = cardDetails;
	}

	public boolean checkout(double price, int quantity) {
		double surcharge = 1.03;
		double total = price * quantity * surcharge;
		paymentGateway.makePayment(cardDetails, total);
		return true;
	}
}

class Cash extends PaymentMethod {
	private double cash;

	public Cash(double cash) {
		this.cash = cash;
	}

	public boolean checkout(double price, int quantity) {
		double total = price * quantity;
		double change = cash - total;

		if (change < 0) {
			return false;
		}

		return true;
	}
}

class PaymentGateway {
	boolean makePayment(CardDetails cardDetails, double amount) ...
}

// Following are the items
class Product {
	String code;
	double price;
}

class SoftDrink extends Product {
	String title;

	public SoftDrink(String title, double price) {
		this.title = title;
		this.price = price;
	}
}

class Chocolate extends Product {
	String title;

	public Chocolate(String title, double price) {
		this.title = title;
		this.price = price;
	}
}

class Chips extends Product {
	String title;

	public Chips(String title, double price) {
		this.title = title;
		this.price = price;
	}
}
