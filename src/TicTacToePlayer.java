public class TicTacToePlayer extends Player {
    private boolean symbol = true;

    public TicTacToePlayer() {}

    public boolean get_symbol() {
        return this.symbol;
    }

    public void set_symbol(boolean symbol) {
        this.symbol = symbol;
    }

    public void display() {
        super.display();

        System.out.print("\nRandomly assigned symbol: ");
        if (this.get_symbol()) {
            System.out.print("O");
        } else {
            System.out.print("X");
        }

        System.out.println();
    }
}