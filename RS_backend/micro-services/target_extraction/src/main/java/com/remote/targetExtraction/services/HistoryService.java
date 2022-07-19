package com.remote.targetExtraction.services;


import com.remote.models.History;
import com.remote.models.User;
import com.remote.targetExtraction.repositories.HistoryRepository;
import com.remote.targetExtraction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoryService {

    @Autowired
    HistoryRepository history;

    @Autowired
    UserRepository user;

    public History getById(String hisId) {
        Optional<History> h=history.findById(hisId);
        return h.orElse(null);
    }

    public List<History> getAllByUserId(String userId){
        Optional<User> u=user.findById(userId);
        return history.findAllByUserAndIsRemoveOrderByCreateTimeDesc(u.orElse(null),false);
    }

    public void createOrUpdate(History h){
        history.save(h);
    }

    //根据数量生成6位ID
    public String generateID(){
        long c= history.count()+1;
        StringBuilder s= new StringBuilder();
        while(true){
            s=new StringBuilder(Long.toString(c));
            int k=s.length();
            for(int i=0;i<6-k;i++){
                s.insert(0, "0");
            }
            s.insert(0, "H");
            if(!history.existsById(s.toString())){
                break;
            }
            c++;
        }

        return s.toString();
    }
}
