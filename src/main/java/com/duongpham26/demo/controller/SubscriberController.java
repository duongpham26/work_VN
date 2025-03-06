package com.duongpham26.demo.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.service.UserService;
import com.duongpham26.demo.entity.Subscriber;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.service.SubScriberService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    private final SubScriberService subscriberService;

    private final UserService userService;

    public SubscriberController(SubScriberService subscriberService, UserService userService) {
        this.subscriberService = subscriberService;
        this.userService = userService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> addSubscriber(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {

        // check email
        // boolean isExistEmail = this.userService.existsByEmail(subscriber.getEmail());

        // if (isExistEmail) {
        // throw new IdInvalidException("Email " + subscriber.getEmail() + " already
        // exists");
        // }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.createSubscriber(subscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {

        Optional<Subscriber> subscriberOptional = this.subscriberService.getSubscriberById(subscriber.getId());

        if (!subscriberOptional.isPresent()) {
            throw new IdInvalidException("Subscriber with id " + subscriber.getId() + "not found");
        }
        return ResponseEntity.ok().body(this.subscriberService.updateSubscriber(subscriber, subscriberOptional.get()));
    }

    // @DeleteMapping("/subscribers/{id}")
    // @ApiMessage("Delete a subscriber by id")
    // public ResponseEntity<Void> deleteSubscriber(@PathVariable long id) throws
    // IdInvalidException {
    // Optional<Subscriber> currentSubscriberOptional =
    // this.subscriberService.getSubscriberById(id);
    // if (!currentSubscriberOptional.isPresent()) {
    // throw new IdInvalidException("Subscriber with " + id + "not found");
    // }
    // this.subscriberService.deleteSubscriber(id);
    // return ResponseEntity.ok(null);
    // }

    // @GetMapping("/subscribers/{id}")
    // @ApiMessage("Get a subscriber by id")
    // public ResponseEntity<Subscriber> getASubscriber(@PathVariable long id)
    // throws IdInvalidException {
    // Optional<Subscriber> currentSubscriberOptional =
    // this.subscriberService.getSubscriberById(id);
    // if (!currentSubscriberOptional.isPresent()) {
    // throw new IdInvalidException("Subscriber with " + id + "not found");
    // }
    // return ResponseEntity.ok(currentSubscriberOptional.get());
    // }

    // @GetMapping("/subscribers")
    // @ApiMessage("Get all subscribers with pagination")
    // public ResponseEntity<ResResultPaginationDTO> getAllSubscribers(@Filter
    // Specification<Subscriber> specification,
    // Pageable pageable) {
    // return
    // ResponseEntity.ok().body(this.subscriberService.fetchAll(specification,
    // pageable));
    // }

}
