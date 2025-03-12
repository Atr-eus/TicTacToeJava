import java.util.Scanner;

public class Player {
    private String name;
    private int age;
    private Gender gender;
    private String country;

    public Player() {}

    public String get_name() {
        return this.name;
    }
    public int get_age() {
        return this.age;
    }
    public Gender get_gender() {
        return this.gender;
    }
    public String get_country() {
        return this.country;
    }

    public void take_input() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        this.name = sc.nextLine();
        System.out.print("Enter your age: ");
        this.age = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter your gender: ");
        this.gender = sc.nextLine().equalsIgnoreCase("male") ? Gender.MALE : Gender.FEMALE;
        System.out.print("Enter your country: ");
        this.country = sc.nextLine();
    }

    public void display() {
        System.out.println("Name: " + this.get_name());
        System.out.println("Age: " + this.get_age());
        System.out.println("Gender: " + this.get_gender());
        System.out.println("Country: " + this.get_country());
    }
}