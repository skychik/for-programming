package ru.ifmo.cs.programming.lab5.utils;

import com.google.gson.stream.JsonReader;
import ru.ifmo.cs.programming.lab5.core.OutOfAttitudeToBossException;

import java.io.IOException;

public enum AttitudeToBoss {

    HATE((byte) -128),
    LOW((byte) -64),
    DEFAULT((byte) 0),
    NORMAL((byte) 63),
    HIGH((byte) 127);

    /**
     * Created by admin on 10.03.2017.
     */
    private byte attitude;

    AttitudeToBoss(byte attitude){
        this.attitude = attitude;
    }

    public byte getAttitude() {
        return this.attitude;
    }

    public static AttitudeToBoss readAttitudeToBoss(JsonReader reader) throws IOException {

        String attitude = reader.nextString();

        switch (attitude){
            case "HATE":
            case "-128":
                return AttitudeToBoss.HATE;
            case "LOW":
            case "-64":
                return AttitudeToBoss.LOW;
            case "DEFAULT":
            case "0":
                return AttitudeToBoss.DEFAULT;
            case "NORMAL":
            case "63":
                return AttitudeToBoss.NORMAL;
            case "HIGH":
            case "127":
                return AttitudeToBoss.HIGH;
            default:
                throw  new OutOfAttitudeToBossException("Value of attitudeToBoss isn't written correctly");
        }
    }
}