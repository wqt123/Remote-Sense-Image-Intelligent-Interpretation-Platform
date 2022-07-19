package com.remote.terrianClassification.repositories;


import com.remote.models.History;
import com.remote.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History,String> {
    List<History> findAllByUserAndIsRemoveOrderByCreateTimeDesc(User u,Boolean r);
}
