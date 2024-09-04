package repository.entity;

public class GuestEntity extends Entity {

    private int id;
    private String name;
    private int age;
    private String passportNumber;
    private String address;

    public GuestEntity(int id, String name, int age, String passportNumber, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.passportNumber = passportNumber;
        this.address = address;
    }

    public GuestEntity(String name, int age, String passportNumber, String address) {
        this.name = name;
        this.age = age;
        this.passportNumber = passportNumber;
        this.address = address;
    }

    public GuestEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void getInfo() {
        System.out.println("ID : " + this.id);
        System.out.println("Name : " + this.name);
        System.out.println("Age : " + this.age);
        System.out.println("Passport Number : " + this.passportNumber);
        System.out.println("Address : " + this.address + '\n');
    }

    @Override
    public String toString() {
        return "GuestEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", passportNumber='" + passportNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
