public class TicTacToePlayer extends Player {
    private boolean symbol;

    TicTacToePlayer(String name, String email) {
      super(name, email);
    }

    public boolean get_symbol() {
      return this.symbol;
    }

    public void set_symbol(boolean symbol) {
      this.symbol = symbol;
    }
}