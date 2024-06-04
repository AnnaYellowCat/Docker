package ru.gudkova.mongoproject.controllers;

import com.mongodb.MongoCommandException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gudkova.mongoproject.dao.DAO;
import ru.gudkova.mongoproject.enums.Role;
import ru.gudkova.mongoproject.models.Client;
import ru.gudkova.mongoproject.dto.InfoForAgregation;
import ru.gudkova.mongoproject.security.UserDetailsImpl;

import java.util.List;

import static ru.gudkova.mongoproject.enums.Role.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/clients")
public class ClientController {
    private final MongoTemplate mongoUserTemplate;
    private final MongoTemplate mongoManagerTemplate;
    private final MongoTemplate mongoDirectorTemplate;
    private final DAO dao;

    private void setDao(Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl)authentication.getPrincipal();
        Role role = principal.getClient().getRole();
        dao.setMongoTemplate(mongoUserTemplate);
        if(role.equals(USER)){
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
    public String clients(Model model, Authentication authentication) {
        setDao(authentication);
        model.addAttribute("clients", dao.findAllClients());
        return "client/clients";
    }

    @GetMapping("/create")
    public String createClient() {
        return "client/newClient";
    }

    @PostMapping()
    public String create(Authentication authentication, @ModelAttribute("client") @Valid Client client, BindingResult bindingResult) {
        setDao(authentication);
        if (bindingResult.hasErrors()) {
            return "client/newClient";
        }
        dao.saveClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/update")
    public String updateClient(Model model, @PathVariable("id") String id, Authentication authentication) {
        setDao(authentication);
        model.addAttribute("client", dao.findClientById(id));
        return "client/updateClient";
    }

    @PatchMapping("/{id}")
    public String update(Authentication authentication, @ModelAttribute("client") @Valid Client client,  BindingResult bindingResult, @PathVariable("id") String id) {
        setDao(authentication);
        if (bindingResult.hasErrors()) {
            return "client/updateClient";
        }
        dao.updateClient(id, client);
        return "redirect:/clients";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String id, Authentication authentication) {
        setDao(authentication);
        dao.deleteClientById(id);
        return "redirect:/clients";
    }

    @PostMapping("/like")
    public String like(String username, Model model, Authentication authentication){
        setDao(authentication);
        model.addAttribute("clientsLike", dao.findClientByName(username));
        model.addAttribute("username", username);
        return "client/clientsLike";
    }

    @GetMapping("/groupByRole")
    public String groupByRole(Model model, Authentication authentication){
        setDao(authentication);
        List<InfoForAgregation> list = dao.groupClientsByRole();
        model.addAttribute("numberRoles", list.stream()
                .map(x -> x.numberClients)
                .toList());
        model.addAttribute("clientRoles", list.stream()
                .map(x -> x.clientRole.stream()
                .findFirst()
                .orElse(null))
                .toList());
        return "client/groupClientsByRole";
    }

    @GetMapping("/sort")
    public String sort(Model model, Authentication authentication){
        setDao(authentication);
        model.addAttribute("sortedClients", dao.orderClientsByName());
        return "client/sorted";
    }

    @ExceptionHandler(MongoCommandException.class)
    public String handleException(MongoCommandException ex){
        return "client/error";
    }

}
