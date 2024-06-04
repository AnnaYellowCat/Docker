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
import ru.gudkova.mongoproject.models.Product;
import ru.gudkova.mongoproject.security.UserDetailsImpl;

@RequiredArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductController {
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
    public String products(Model model, Authentication authentication) {
        setDao(authentication);
        model.addAttribute("products", dao.findAllProducts());
        return "product/products";
    }

    @GetMapping("/create")
    public String createProduct() {
        return "product/newProduct";
    }

    @PostMapping()
    public String create(Authentication authentication, @ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {
        setDao(authentication);
        if (bindingResult.hasErrors()) {
            return "product/newProduct";
        }
        dao.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/update")
    public String updateProduct(Model model, @PathVariable("id") String id, Authentication authentication) {
        setDao(authentication);
        model.addAttribute("product", dao.findProductById(id));
        return "product/updateProduct";
    }

    @PatchMapping("/{id}")
    public String update(Authentication authentication, @ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") String id) {
        setDao(authentication);
        if (bindingResult.hasErrors()) {
            return "product/updateProduct";
        }
        dao.updateProduct(id, product);
        return "redirect:/products";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String id, Authentication authentication) {
        setDao(authentication);
        dao.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/sum")
    public String sum(Model model, Authentication authentication) {
        setDao(authentication);
        model.addAttribute("productsSum", dao.sumProductQuantity());
        return "product/sumOfProducts";
    }

    @PostMapping("/count")
    public String count(int price, Model model, Authentication authentication){
        setDao(authentication);
        model.addAttribute("countProducts", dao.countProductsMoreThanPrice(price));
        model.addAttribute("price", price);
        return "product/countProducts";
    }


    @ExceptionHandler(MongoCommandException.class)
    public String handleException(MongoCommandException ex){
        return "product/error";
    }

}