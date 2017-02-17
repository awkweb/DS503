public final class Customer {

    private int id;
    private String name;
    private int age;
    private int countryCode;
    private float salary;

    public Customer(int id, String name, int age, int countryCode, float salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.countryCode = countryCode;
        this.salary = salary;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public int getCountryCode() {
        return this.countryCode;
    }

    public float getSalary() {
        return this.salary;
    }

}
