package sosgame.product;

public abstract class Player {
    protected char id;

    public Player(char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }

    public abstract Move getMove(BaseGame game);
}
