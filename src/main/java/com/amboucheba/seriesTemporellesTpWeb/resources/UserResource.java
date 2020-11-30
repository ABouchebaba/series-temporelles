package com.amboucheba.seriesTemporellesTpWeb.resources;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/users")
public class UserResource  {

    @Autowired
    UserRepository userRepository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAll(){

        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided User info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addUser(@Valid @RequestBody User newUser){
        User savedUser = userRepository.save(newUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned in body"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<User> getUserById(@PathVariable("userId") long userId){

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return ResponseEntity.ok(user.get());
    }

//    @PutMapping(value = "/{userId}", consumes = "application/json", produces = "application/json")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "User updated and returned in response body"),
//            @ApiResponse(code = 201, message = "User created,  check location header for uri"),
//            @ApiResponse(code = 400, message = "Provided User info not valid, check response body for more details on error")
//    })
//    public ResponseEntity<User> updateUser(@PathVariable("userId") long userId, @Valid @RequestBody User newUser){
//        Optional<User> user = userRepository.findById(userId);
//
//        if (user.isPresent()){
//            User actualUser = user.get();
//            actualUser.setUsername(newUser.getUsername());
//            actualUser.setPassword(newUser.getPassword());
//            User savedUser = userRepository.save(actualUser);
//            return ResponseEntity.ok(savedUser);
//        }
//
//        // User does not exist so create it
//        User savedUser = userRepository.save(newUser);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("users")
//                .path("/{id}")
//                .buildAndExpand(savedUser.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).build();
//    }


//    @DeleteMapping(value = "/{userId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @ApiResponses(value = {
//            @ApiResponse(code = 204, message = "User deleted"),
//            @ApiResponse(code = 404, message = "User not found")
//    })
//    public ResponseEntity<Void> deleteUser(@PathVariable("userId") long userId){
//
//        if (!userRepository.existsById(userId)){
//            throw new NotFoundException("User with id " + userId + " not found");
//        }
//        userRepository.deleteById(userId);
//
//        return ResponseEntity.noContent().build();
//    }
}