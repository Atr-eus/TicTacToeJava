public class TicTacToePlayer extends Player {
    private int won;
    private boolean symbol;

    TicTacToePlayer(String name, String email, int won) {
      super(name, email);
      this.won = won;
    }

    public int get_won() {
        return this.won;
    }

    public void set_won(int won) {
        this.won = won;
    }

    public boolean get_symbol() {
      return this.symbol;
    }

    public void set_symbol(boolean symbol) {
      this.symbol = symbol;
    }
}