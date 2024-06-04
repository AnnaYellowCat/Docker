package ru.gudkova.mongoproject.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.gudkova.mongoproject.enums.Role;
import ru.gudkova.mongoproject.models.Client;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MongoTemplate mongoLoginTemplate;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        List<Client> clients = mongoLoginTemplate.find(query, Client.class, "clients");
        if(clients.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        if(clients.size() > 1){
            throw new UsernameNotFoundException("Quantity of users is greater than 1");
        }
        return new UserDetailsImpl(clients.get(0));
    }
}
