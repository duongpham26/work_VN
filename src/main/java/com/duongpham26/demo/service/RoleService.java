package com.duongpham26.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Permission;
import com.duongpham26.demo.entity.Role;
import com.duongpham26.demo.entity.dto.response.ResResultPaginationDTO;
import com.duongpham26.demo.repository.PermissionRepository;
import com.duongpham26.demo.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role create(Role role) {

        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPermission = role.getPermissions().stream().map(permission -> permission.getId())
                    .collect(Collectors.toList());
            List<Permission> currentPermission = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(currentPermission);
        }
        return this.roleRepository.save(role);
    }

    public Role fetchById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    public Role update(Role role) {
        Role currentRole = this.fetchById(role.getId());

        if (role.getPermissions() != null) {
            List<Long> reqPermission = role.getPermissions().stream().map(permission -> permission.getId())
                    .collect(Collectors.toList());
            List<Permission> currentPermission = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(currentPermission);
        }

        currentRole.setName(role.getName());
        currentRole.setActive(role.isActive());
        currentRole.setDescription(role.getDescription());
        currentRole.setPermissions(role.getPermissions());

        // update
        currentRole = this.roleRepository.save(currentRole);
        return currentRole;
    }

    public void delete(long id) {
        // delete
        this.roleRepository.deleteById(id);
    }

    public ResResultPaginationDTO getRoles(Specification<Role> specification, Pageable pageable) {
        Page<Role> page = this.roleRepository.findAll(specification, pageable);

        ResResultPaginationDTO resResultPaginationDTO = new ResResultPaginationDTO();
        ResResultPaginationDTO.Meta meta = new ResResultPaginationDTO.Meta();

        meta.setPage(page.getNumber() + 1); // +1 vì spring tự trừ đi 1 khi cấu hình start index = 1
        meta.setPageSize(page.getSize());
        meta.setTotal(page.getTotalElements());
        meta.setPages(page.getTotalPages());

        resResultPaginationDTO.setMeta(meta);

        List<Role> listJob = page.getContent();
        resResultPaginationDTO.setResult(listJob);

        return resResultPaginationDTO;

    }

}
