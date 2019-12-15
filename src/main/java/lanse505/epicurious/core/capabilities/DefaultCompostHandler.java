package lanse505.epicurious.core.capabilities;

public class DefaultCompostHandler implements ICompost {

    private int compost;

    @Override
    public int getCompost() {
        return compost;
    }

    @Override
    public void setCompost(int value) {
        compost = value;
    }
}
