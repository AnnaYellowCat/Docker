package ru.gudkova.mongoproject.controllers;

import com.mongodb.MongoCommandException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gudkova.mongoproject.dao.DAO;
import ru.gudkova.mongoproject.enums.Role;
import ru.gudkova.mongoproject.security.UserDetailsImpl;

@RequiredArgsConstructor
@Controller
@RequestMapping("/sales")
public class SaleController {
    private final MongoTemplate mongoUserTemplate;
    private final MongoTemplate mongoManagerTemplate;
    private final MongoTemplate mongoDirectorTemplate;
    private final DAO dao;

    private void setDao(Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl)authentication.getPrincipal();
        Role role = principal.getClient().getRole();
        dao.setMongoTemplate(mongoUserTemplate);
        if(role.equals(Role.USER)){
            dao.setMongoTemplate(mongoUserTemplate);
        }
        if(role.equals(Role.MANAGER)){
            dao.setMongoTemplate(mongoManagerTemplate);
        }
        if(role.equals(Role.DIRECTOR)){
            dao.setMongoTemplate(mongoDirectorTemplate);
        }
    }

    @GetMapping()
    public String sales(Model model, Authentication authentication) {
        setDao(authentication);
        model.addAttribute("sales", dao.findAllSales());
        return "sale/sales";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String id, Authentication authentication) {
        setDao(authentication);
        dao.deleteSaleById(id);
        return "redirect:/sales";
    }

    @GetMapping("/min")
    public String min(Model model, Authentication authentication){
        setDao(authentication);
        model.addAttribute("minDateSale", dao.saleWithMinDate());
        return "sale/minDate";
    }

    @ExceptionHandler(MongoCommandException.class)
    public String handleException(MongoCommandException ex){
        return "sale/error";
    }

}