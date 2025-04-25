public class Player {
    private final String name;
    private final String email;

    Player(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String get_name() {
        return this.name;
    }
    public String get_email() {
        return this.email;
    }
}