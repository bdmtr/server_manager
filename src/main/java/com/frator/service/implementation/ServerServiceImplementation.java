package com.frator.service.implementation;

import com.frator.exception.ServerNotFoundException;
import com.frator.model.Server;
import com.frator.repo.ServerRepo;
import com.frator.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static com.frator.enumaration.Status.SERVER_DOWN;
import static com.frator.enumaration.Status.SERVER_UP;
import static java.lang.Boolean.TRUE;

@Service
@Transactional
@Slf4j
public class ServerServiceImplementation implements ServerService {
    private final ServerRepo serverRepo;

    @Autowired
    public ServerServiceImplementation(ServerRepo serverRepo) {
        this.serverRepo = serverRepo;
    }

    @Override
    public Server create(Server server) {
        log.info("Create server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepo.save(server);
    }

    @Override
    public Server ping(String ipAddress) {
        log.info("Ping server: {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            server.setStatus(inetAddress.isReachable(3000) ? SERVER_UP : SERVER_DOWN);
        } catch (IOException exception) {
            log.info("Cant ping server:" + server.getIpAddress());
        }
        serverRepo.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Find all servers");
        return serverRepo.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Find server: {}", id);
        return serverRepo.findById(id)
                .orElseThrow(() -> new ServerNotFoundException("Server not found"));
    }

    @Override
    public Server update(Server server) {
        log.info("Update server: {}", server.getName());
        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Delete server: {}", id);
        serverRepo.deleteById(id);
        return TRUE;
    }

    private String setServerImageUrl() {
        String[] imageNames = {"1.png", "2.png", "3.png", "4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("server/image" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
