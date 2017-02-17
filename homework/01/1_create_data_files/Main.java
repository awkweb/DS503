import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        RandomGenerator randomGenerator = new RandomGenerator();

        String customerFile = "/Users/tom/Desktop/customers.csv";
        FileWriter customerWriter = new FileWriter(customerFile);

        int CUSTOMER_COUNT = 50000;
        List<Customer> customers = new ArrayList<Customer>(CUSTOMER_COUNT);

        for (int i=1; i<=CUSTOMER_COUNT; i++) {
            System.out.println(String.format("Customer id: %d", i));

            String name = randomGenerator.getRandomString(10, 20);
            int age = randomGenerator.getRandomIntegerBetween(10, 70);
            int countryCode = randomGenerator.getRandomIntegerBetween(1, 10);
            float salary = randomGenerator.getRandomFloatBetween(100f, 10000f);

            Customer customer = new Customer(i, name, age, countryCode, salary);
            customers.add(customer);
        }

        System.out.println("Writing customers to file...");
        for (Customer customer : customers) {
            List<String> list = new ArrayList<String>();
            list.add(Integer.toString(customer.getId()));
            list.add(customer.getName());
            list.add(Integer.toString(customer.getAge()));
            list.add(Integer.toString(customer.getCountryCode()));
            list.add(Float.toString(customer.getSalary()));

            CsvUtils.writeLine(customerWriter, list);
        }

        customerWriter.flush();
        customerWriter.close();


        // Generate transactions
        String transactionFile = "/Users/tom/Desktop/transactions.csv";
        FileWriter transactionWriter = new FileWriter(transactionFile);

        int TRANSACTION_COUNT = 5000000;
        List<Transaction> transactions = new ArrayList<Transaction>(TRANSACTION_COUNT);

        int sum = 0;
        int counter = 0;
        int currentTotal = randomGenerator.getRandomGaussian(100, 0);
        int currentId = 1;
        for (int i=1; i<=TRANSACTION_COUNT; i++) {
            int customerId;
            if (counter <= currentTotal) {
                customerId = currentId;
                counter++;
            } else {
                sum += currentTotal;
                System.out.println(String.format("Transaction id: %d, customer id: %d, transactions count: %d, mean: %d", i, currentId, currentTotal, sum/currentId));

                customerId = currentId;
                counter = 0;
                currentTotal = randomGenerator.getRandomGaussian(100, 0);
                currentId++;
            }

            float total = randomGenerator.getRandomFloatBetween(100f, 1000f);
            int itemCount = randomGenerator.getRandomIntegerBetween(1, 10);
            String description = randomGenerator.getRandomString(20, 50);

            Transaction transaction = new Transaction(i, customerId, total, itemCount, description);
            transactions.add(transaction);
        }

        System.out.println("Writing transactions to file...");
        for (Transaction transaction : transactions) {
            List<String> list = new ArrayList<String>();
            list.add(Integer.toString(transaction.getId()));
            list.add(Integer.toString(transaction.getCustomerId()));
            list.add(Float.toString(transaction.getTotal()));
            list.add(Integer.toString(transaction.getItemCount()));
            list.add(transaction.getDescription());

            CsvUtils.writeLine(transactionWriter, list);
        }

        transactionWriter.flush();
        transactionWriter.close();
    }

}
