package ru.gudkova.mongoproject.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.gudkova.mongoproject.models.Client;
import ru.gudkova.mongoproject.models.Product;
import ru.gudkova.mongoproject.models.Sale;
import ru.gudkova.mongoproject.dto.InfoForAgregation;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class DAO {
    @Getter
    @Setter
    private MongoTemplate mongoTemplate;

    public Client saveClient(Client client){
        return mongoTemplate.save(client, "clients");
    }

    public Client updateClient(String id, Client client){
        Query searchQuery = new Query(Criteria.where("id").is(id));
        mongoTemplate.updateFirst(searchQuery, Update.update("username", client.getUsername()), Client.class);
        mongoTemplate.updateFirst(searchQuery, Update.update("phone", client.getPhone()), Client.class);
        mongoTemplate.updateFirst(searchQuery, Update.update("password", client.getPassword()), Client.class);
        mongoTemplate.updateFirst(searchQuery, Update.update("role", client.getRole()), Client.class);
        client.setId(id);
        return client;
    }

    public void deleteClientById(String id){
        Client client = mongoTemplate.findById(id, Client.class, "clients");
        mongoTemplate.remove(client);
    }

    public Client findClientById(String id){
        return mongoTemplate.findById(id, Client.class, "clients");
    }

    public List<Client> findAllClients(){
        return mongoTemplate.findAll(Client.class, "clients");
    }

    public List<Client> findClientByName(String name){
        Criteria cr = where("username").is(name);
        List<Client> list = mongoTemplate.aggregate(newAggregation(match(cr)), Client.class, Client.class)
                .getMappedResults();
        return list;
    }

    public List<InfoForAgregation> groupClientsByRole(){
        GroupOperation op = group("role").addToSet("role").as("clientRole").count().as("numberClients");
        List<InfoForAgregation> list = mongoTemplate.aggregate(newAggregation(op), Client.class, InfoForAgregation.class)
                .getMappedResults();
        return list;
    }

    public List<Client> orderClientsByName(){
        Query query = new Query();
        query.addCriteria(where("username").exists(true)).with(Sort.by(Sort.Direction.ASC, "username"));
        return mongoTemplate.find(query, Client.class);
    }




    public Product saveProduct(Product product) {
        return mongoTemplate.save(product, "products");
    }

    public Product updateProduct(String id, Product product){
        Query searchQuery = new Query(Criteria.where("id").is(id));
        mongoTemplate.updateFirst(searchQuery, Update.update("title", product.getTitle()), Product.class);
        mongoTemplate.updateFirst(searchQuery, Update.update("description", product.getDescription()), Product.class);
        mongoTemplate.updateFirst(searchQuery, Update.update("price", product.getPrice()), Product.class);
        mongoTemplate.updateFirst(searchQuery, Update.update("quantity", product.getQuantity()), Product.class);
        product.setId(id);
        return product;
    }

    public void deleteProductById(String id) {
        Product product = mongoTemplate.findById(id, Product.class, "products");
        mongoTemplate.remove(product);
    }

    public Product findProductById(String id) {
        return mongoTemplate.findById(id, Product.class, "products");
    }

    public List<Product> findAllProducts() {
        return mongoTemplate.findAll(Product.class, "products");
    }

    public int sumProductQuantity(){
        GroupOperation op = group("*").sum("quantity").as("totalQuantity");
        List<InfoForAgregation> list = mongoTemplate.aggregate(newAggregation(op), Product.class, InfoForAgregation.class)
                .getMappedResults();
        return list.stream().findAny().orElse(new InfoForAgregation()).totalQuantity;
    }

    public int countProductsMoreThanPrice(int price){
        Criteria cr = where("price").gt(price);
        GroupOperation op = group("*").count().as("countProducts");
        List<InfoForAgregation> list = mongoTemplate.aggregate(newAggregation(match(cr), op), Product.class, InfoForAgregation.class)
                .getMappedResults();
        return list.stream().findAny().orElse(new InfoForAgregation()).countProducts;
    }





    public void deleteSaleById(String id) {
        Sale sale = mongoTemplate.findById(id, Sale.class, "sales");
        mongoTemplate.remove(sale);
    }

    public List<Sale> findAllSales() {
        return mongoTemplate.findAll(Sale.class, "sales");
    }

    public Sale saleWithMinDate(){
        GroupOperation op = group("*").min("date").as("minDate");
        List<InfoForAgregation> list = mongoTemplate.aggregate(newAggregation(op), Sale.class, InfoForAgregation.class)
                .getMappedResults();
        String date = list.stream().findAny().orElse(new InfoForAgregation()).minDate;
        Query query = new Query();
        query.addCriteria(where("date").is(date));
        return mongoTemplate.find(query, Sale.class).get(0);
    }
}
