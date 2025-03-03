package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duongpham26.demo.entity.Permission;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.service.PermissionService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

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

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a new permission")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission permission) throws IdInvalidException {
        // check existing permission
        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission already exists");
        }

        // create new permission
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission permission) throws IdInvalidException {

        // check existing permission by id
        if (this.permissionService.fetchById(permission.getId()) == null) {
            throw new IdInvalidException("Permission not found with id: " + permission.getId());
        }

        // check existing permission
        if (this.permissionService.isPermissionExist(permission)) {
            // check name
            // if (this.permissionService.isSameName(permission))
            throw new IdInvalidException("Permission not found");
        }

        // update permission
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.update(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> update(@PathVariable long id) throws IdInvalidException {

        // check existing permission by id
        if (this.permissionService.fetchById(id) == null) {
            throw new IdInvalidException("Permission not found with id: " + id);
        }
        this.permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("Get all permission with pagination")
    public ResponseEntity<ResResultPaginationDTO> getPermissions(@Filter Specification<Permission> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.permissionService.getPermissions(specification, pageable));
    }
}
