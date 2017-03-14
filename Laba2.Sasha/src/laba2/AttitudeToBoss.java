package laba2;

public enum AttitudeToBoss {
    HATE((byte) -128),
    LOW((byte) -64),
    DEFAULT((byte) 0),
    NORMAL((byte) 63),
    HIGH((byte) 127);

    /**
     * Created by admin on 10.03.2017.
     */
    protected final byte attitude;

    AttitudeToBoss(byte attitude){
        this.attitude = attitude;
    }
}