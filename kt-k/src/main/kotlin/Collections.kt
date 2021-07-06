object Introduction {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    fun Shop.getSetOfCustomers(): Set<Customer> =
        customers.toSet()


    fun run() {
        val shop = Shop("shop", listOf(Customer("a", City("b"), listOf())))
        println(shop.getSetOfCustomers())
    }
}

object Sort {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Return a list of customers, sorted in the descending by number of orders they have made
    fun Shop.getCustomersSortedByOrders(): List<Customer> =
        customers.sortedByDescending { it.orders.size }

    fun run() {
        println(Shop("shop", listOf()))
    }
}

object FilterMap {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Find all the different cities the customers are from
    fun Shop.getCustomerCities(): Set<City> =
        customers.map { it.city }.toSet()

    // Find the customers living in a given city
    fun Shop.getCustomersFrom(city: City): List<Customer> =
        customers.filter { it.city == city }

    fun run() {
        val shop = Shop("a", listOf())
        println(shop.getCustomerCities())
        println(shop.getCustomersFrom(City("v")))
    }
}

object AllAnyAndOtherPredicates {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Return true if all customers are from a given city
    fun Shop.checkAllCustomersAreFrom(city: City): Boolean =
        customers.all { it.city == city }

    // Return true if there is at least one customer from a given city
    fun Shop.hasCustomerFrom(city: City): Boolean =
        customers.any { it.city == city }

    // Return the number of customers from a given city
    fun Shop.countCustomersFrom(city: City): Int =
        customers.count { it.city == city }

    // Return a customer who lives in a given city, or null if there is none
    fun Shop.findCustomerFrom(city: City): Customer? =
        customers.find { it.city == city }

    fun run() {
        val shop = Shop("a", listOf(Customer("1", City("c1"), listOf())))
        println(shop.checkAllCustomersAreFrom(City("c1")))
    }
}

object MaxMin {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Return a customer who has placed the maximum amount of orders
    fun Shop.getCustomerWithMaxOrders(): Customer? =
        customers.maxBy { it.orders.size }

    // Return the most expensive product that has been ordered by the given customer
    fun getMostExpensiveProductBy(customer: Customer): Product? =
        customer.orders
            .flatMap(Order::products)
            .maxBy(Product::price)

    fun run() {
        val shop = Shop("a", listOf(Customer("1", City("c1"), listOf())))
        println(shop.getCustomerWithMaxOrders())
    }
}

object Sum {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Return the sum of prices for all the products ordered by a given customer
    fun moneySpentBy(customer: Customer): Double =
        customer.orders.flatMap { it.products }.sumByDouble { it.price }

    fun run() {
        val customer = Customer("c", City("c1"), listOf())
        println(moneySpentBy(customer))
    }
}

object GroupBy {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Build a map that stores the customers living in a given city
    fun Shop.groupCustomersByCity(): Map<City, List<Customer>> =
        customers.groupBy { it.city }

    fun run() {
        val shop = Shop("a", listOf(Customer("1", City("c1"), listOf())))
        println(shop.groupCustomersByCity())
    }
}

object Associate {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Build a map from the customer name to the customer
    fun Shop.nameToCustomerMap(): Map<String, Customer> =
        customers.associateBy(Customer::name)

    // Build a map from the customer to their city
    fun Shop.customerToCityMap(): Map<Customer, City> =
        customers.associateWith(Customer::city)

    // Build a map from the customer name to their city
    fun Shop.customerNameToCityMap(): Map<String, City> =
        customers.associate { it.name to it.city }

    fun run() {
        val shop = Shop("a", listOf(Customer("1", City("c1"), listOf())))
        println(shop.nameToCustomerMap())
        println(shop.customerToCityMap())
        println(shop.customerNameToCityMap())
    }
}

object Partition {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Return customers who have more undelivered orders than delivered
    fun Shop.getCustomersWithMoreUndeliveredOrders(): Set<Customer> = customers.filter {
        val (delivered, undelivered) = it.orders.partition { it.isDelivered }
        undelivered.size > delivered.size
    }.toSet()

    fun run() {
        val shop = Shop("a", listOf(Customer("1", City("c1"), listOf())))
        println(shop.getCustomersWithMoreUndeliveredOrders())
    }
}

object FlatMap {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Return all products the given customer has ordered
    fun Customer.getOrderedProducts(): List<Product> =
        orders.flatMap(Order::products)

    // Return all products that were ordered by at least one customer
    /*fun Shop.getOrderedProducts(): Set<Product> =
        customers.flatMap(Customer::getOrderedProducts).toSet()*/

    fun run() {
        val customer = Customer("c", City("c1"), listOf())
        println(customer.getOrderedProducts())
    }
}

object Fold {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Return the set of products that were ordered by all customers
    fun Shop.getProductsOrderedByAll(): Set<Product> {
        val allProducts = customers.flatMap { it.getOrderedProducts() }.toSet()
        return customers.fold(allProducts, { orderedByAll, customer ->
            orderedByAll.intersect(customer.getOrderedProducts())
        })
    }

    fun Customer.getOrderedProducts(): List<Product> =
        orders.flatMap(Order::products)

    fun run() {
        val shop = Shop("a", listOf(Customer("1", City("c1"), listOf())))
        println(shop.getProductsOrderedByAll())
    }
}

object CompoundTasks {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Find the most expensive product among all the delivered products
    // ordered by the customer. Use `Order.isDelivered` flag.
    fun findMostExpensiveProductBy(customer: Customer): Product? {
        return customer
            .orders
            .filter(Order::isDelivered)
            .flatMap(Order::products)
            .maxBy(Product::price)
    }

    // Count the amount of times a product was ordered.
    // Note that a customer may order the same product several times.
    /*fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int {
        return customers
            .flatMap(Customer::getOrderedProducts)
            .count { it == product }
    }*/

    fun Customer.getOrderedProducts(): List<Product> =
        orders.flatMap(Order::products)


    fun run() {
        val customer = Customer("c", City("c1"), listOf())
        println(findMostExpensiveProductBy(customer))
    }
}

object GettingUsedToNewStyle {

    fun doSomethingWithCollection(collection: Collection<String>): Collection<String>? {

        val groupsByLength = collection.groupBy { s -> s.length }

        val maximumSizeOfGroup = groupsByLength.values.map { group -> group.size }.max()

        return groupsByLength.values.firstOrNull { group -> group.size == maximumSizeOfGroup }
    }

    fun run() {
        println(doSomethingWithCollection(listOf("a", "b")))
    }
}

object Sequences {
    data class Shop(val name: String, val customers: List<Customer>)

    data class Customer(val name: String, val city: City, val orders: List<Order>) {
        override fun toString() = "$name from ${city.name}"
    }

    data class Order(val products: List<Product>, val isDelivered: Boolean)

    data class Product(val name: String, val price: Double) {
        override fun toString() = "'$name' for $price"
    }

    data class City(val name: String) {
        override fun toString() = name
    }

    // Find the most expensive product among all the delivered products
    // ordered by the customer. Use `Order.isDelivered` flag.
    fun findMostExpensiveProductBy(customer: Customer): Product? {
        return customer
            .orders
            .asSequence()
            .filter(Order::isDelivered)
            .flatMap { it.products.asSequence() }
            .maxBy(Product::price)
    }

    // Count the amount of times a product was ordered.
    // Note that a customer may order the same product several times.
    /*fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int {
        return customers
            .asSequence()
            .flatMap(Customer::getOrderedProducts)
            .count { it == product }
    }*/

    fun Customer.getOrderedProducts(): Sequence<Product> =
        orders.flatMap(Order::products).asSequence()


    fun run() {
        val customer = Customer("c", City("c1"), listOf())
        println(findMostExpensiveProductBy(customer))
    }
}

fun main() {
    Introduction.run()
    Sort.run()
    FilterMap.run()
    AllAnyAndOtherPredicates.run()
    MaxMin.run()
    Sum.run()
    GroupBy.run()
    Associate.run()
    Partition.run()
    FlatMap.run()
    Fold.run()
    CompoundTasks.run()
    GettingUsedToNewStyle.run()
    Sequences.run()
}