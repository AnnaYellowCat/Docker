package ru.gudkova.mongoproject.enums;

public enum Role {
    USER, MANAGER, DIRECTOR;

    @Override
    public String toString() {
        return ""+name().toUpperCase();
    }
}
