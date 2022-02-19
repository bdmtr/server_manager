package com.frator.resource;

import com.frator.enumaration.Status;
import com.frator.model.Response;
import com.frator.model.Server;
import com.frator.service.implementation.ServerServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {

    private final ServerServiceImplementation serverServiceImplementation;

    @GetMapping("/list")
    public ResponseEntity<Response> getServers() {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now()).data(Map.of("servers", serverServiceImplementation.list(30)))
                .message("server extracted")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) {
        Server server = serverServiceImplementation.ping(ipAddress);
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now()).data(Map.of("servers", server))
                .message(server.getStatus() == Status.SERVER_UP ? "Ping ok" : "Cant ping server")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now()).data(Map.of("server", serverServiceImplementation.create(server)))
                .message("Server created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now()).data(Map.of("server", serverServiceImplementation.get(id)))
                .message("Get server")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(now()).data(Map.of("deleted", serverServiceImplementation.delete(id)))
                .message("Server deleted")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) {
        try {
            return Files.readAllBytes(Paths.get(System.getProperty("H:\\SOURCE\\server manager\\server_manager\\src\\main\\resources\\images\\" + fileName)));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return new byte[0];
    }


}
