package com.remote.targetDetection.services;


import com.remote.models.User;
import com.remote.targetDetection.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository user;

    public User getById(String userId) {
        Optional<User> u=user.findById(userId);
        return u.orElse(null);
    }

    public User getByName(String name) {
        Optional<User> u= Optional.ofNullable(user.getUsersByName(name));
        return u.orElse(null);
    }

    public User getByEmail(String name) {
        Optional<User> u= Optional.ofNullable(user.getUsersByEmail(name));
        return u.orElse(null);
    }

    public void createOrUpdate(User u){
        user.save(u);
    }

    //根据数量生成6位ID
    public String generateID(){
        long c= user.count()+1;
        StringBuilder s= new StringBuilder();
        while(true){
            s=new StringBuilder(Long.toString(c));
            int k=s.length();
            for(int i=0;i<6-k;i++){
                s.insert(0, "0");
            }
            s.insert(0, "U");
            if(!user.existsById(s.toString())){
                break;
            }
            c++;
        }
        return s.toString();
    }
}
