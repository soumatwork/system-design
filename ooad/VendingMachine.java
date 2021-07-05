class VendingMachineSystem {

	int MAX_CODES = 50;
	int MAX_PRODUCT_FOR_EACH_CODE = 10;

	Map<String, List<Product>> products = new HashMap<>();

	public void addProduct(String code, Product product) {
		// it can not be unlimited
		if (products.containsKey(code)) {
			List<Product> items = products.getOrDefault(code, new ArrrayList<>());

			if (items != null && items.size() <= MAX_PRODUCT_FOR_EACH_CODE) {
				return;
			}
			items.add(product);
			products.put(code, items);

		} else {
			if (products.keySet().size() <= MAX_CODES) {
				return;
			}
			products.computeIfAbsent(code, val -> new ArrrayList<>()).add(product);
		}

	}

	public double displayPrice(String code) {
		List<Item> selectedItems = products.get(code);

		if (selectedItems != null && !selectedItems.isEmpty()) {
			return selectedItems.get(0).price;
		}
		return -1;

	}

	public Payment checkout(List<CheckoutRequest> checkoutRequests, PaymentMethod paymentMethod) {
		List<Product> productsToCheckout = new ArrayList<>();
		for (CheckoutRequest checkoutRequest : checkoutRequests) {
			if (products.get(checkoutRequest.code).size() >= checkoutRequest.quantity) {
				productsToCheckout.addAll(products.get(checkoutRequest.code));
			}
		}

		boolean result = paymentMethod.checkout(productsToCheckout);

		if (result) {
			for (CheckoutRequest checkoutRequest : checkoutRequests) {
				for (int i = 0; i < quantity; i++) {
					products.get(checkoutRequest.code).remove();
				}
			}
		}
	}
}

class CheckoutRequest {
	String code;
	int quantity;
}

// Following are the payment method
interface PaymentMethod {
	Payment checkout(List<Product> products);
}

class CreditCard extends PaymentMethod {
	private PaymentGateway paymentGateway;
	private CardDetails cardDetails;

	public CreditCard(PaymentGateway paymentGateway, CardDetails cardDetails) {
		this.paymentGateway = paymentGateway;
		this.cardDetails = cardDetails;
	}

	public Payment checkout(List<Product> products) {
		double total = 0;

		for (Product product : products) {
			total += product.price;
		}

		paymentGateway.makePayment(cardDetails, total);
		return new Payment(products, 0);
	}
}

class Cash extends PaymentMethod {
	private double cash;

	public Cash(double cash) {
		this.cash = cash;
	}

	public Payment checkout(List<Product> products) {
		double total = 0;

		for (Product product : products) {
			total += product.price;
		}

		double change = cash - total;

		if (change < 0) {
			return null;
		}

		return new Payment(products, change);
	}
}

class PaymentGateway {
	boolean makePayment(CardDetails cardDetails, double amount) ...
}

	class Payment {
		List<Product> products;
		double change;

		public Payment(List<Product> products, double change) {
			this.products = products;
			this.change = change;
		}
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
