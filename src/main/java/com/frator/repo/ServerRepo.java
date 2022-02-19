package com.frator.repo;

import com.frator.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepo extends JpaRepository<Server, Long>{
    Server findByIpAddress(String ipAddress);

    void deleteById(Long id);
}
