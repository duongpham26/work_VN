package com.duongpham26.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.Role;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.service.RoleService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a new role")
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) throws IdInvalidException {
        // check existing role
        if (this.roleService.existByName(role.getName())) {
            throw new IdInvalidException("Role already exists");
        }

        // create new role
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) throws IdInvalidException {

        // check existing role by id
        if (this.roleService.fetchById(role.getId()) == null) {
            throw new IdInvalidException("Role not found with id: " + role.getId());
        }

        // check exist by nam
        // if (!this.roleService.existByName(role.getName())) {
        // throw new IdInvalidException("Role not found with name " + role.getName());
        // }

        // update role
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.update(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("delete a role")
    public ResponseEntity<Void> update(@PathVariable long id) throws IdInvalidException {

        // check existing role by id
        if (this.roleService.fetchById(id) == null) {
            throw new IdInvalidException("Role not found with id: " + id);
        }
        this.roleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Get role with id")
    public ResponseEntity<Role> getById(@PathVariable long id) throws IdInvalidException {

        Role role = this.roleService.fetchById(id);
        // check existing role by id
        if (role == null) {
            throw new IdInvalidException("Role not found with id: " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping("/roles")
    @ApiMessage("Get all role with pagination")
    public ResponseEntity<ResResultPaginationDTO> getRoles(@Filter Specification<Role> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.getRoles(specification, pageable));
    }
}
