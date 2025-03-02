package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Permission;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod());
    }

    public Permission create(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    public Permission fetchById(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return permissionOptional.get();
        }
        return null;
    }

    public Permission update(Permission permission) {
        Permission currentPermission = this.fetchById(permission.getId());
        if (currentPermission != null) {
            currentPermission.setName(permission.getName());
            currentPermission.setApiPath(permission.getApiPath());
            currentPermission.setMethod(permission.getMethod());
            currentPermission.setModule(permission.getModule());

            // update
            currentPermission = this.permissionRepository.save(currentPermission);
            return currentPermission;
        }
        return null;
    }

    public void delete(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete
        this.permissionRepository.delete(currentPermission);
    }

    public ResResultPaginationDTO getPermissions(Specification<Permission> specification, Pageable pageable) {
        Page<Permission> page = this.permissionRepository.findAll(specification, pageable);

        ResResultPaginationDTO resResultPaginationDTO = new ResResultPaginationDTO();
        ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();

        meta.setPage(page.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình start index = 1
        meta.setPageSize(page.getSize());
        meta.setTotal(page.getTotalElements());
        meta.setPages(page.getTotalPages());

        resResultPaginationDTO.setMeta(meta);

        List<Permission> listJob = page.getContent();
        resResultPaginationDTO.setResult(listJob);

        return resResultPaginationDTO;

    }

}