package ru.gudkova.mongoproject.dto;


import ru.gudkova.mongoproject.enums.Role;

import java.util.List;

public class InfoForAgregation {
    public int totalQuantity;

    public String minDate;

    public int countProducts;

    public int numberClients;
    public List<Role> clientRole;
}
