package com.duongpham26.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duongpham26.demo.entity.Permission;
import com.duongpham26.demo.entity.Role;
import com.duongpham26.demo.entity.User;
import com.duongpham26.demo.repository.PermissionRepository;
import com.duongpham26.demo.repository.RoleRepository;
import com.duongpham26.demo.repository.UserRepository;
import com.duongpham26.demo.util.constant.GenderEnum;

@Service
public class DataBaseInitializer implements CommandLineRunner {

        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public DataBaseInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository,
                        UserRepository userRepository, PasswordEncoder passwordEncoder) {
                this.permissionRepository = permissionRepository;
                this.roleRepository = roleRepository;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) throws Exception {
                System.out.println(">>> START INIT DATABASE");
                long countPermissions = this.permissionRepository.count();
                long countRoles = this.roleRepository.count();
                long countUsers = this.userRepository.count();

                if (countPermissions == 0) {
                        ArrayList<Permission> listPermissions = new ArrayList<>();
                        listPermissions.add(
                                        new Permission("Create a company", "/api/v1/companies", "POST", "companies"));
                        listPermissions.add(
                                        new Permission("Update a company", "/api/v1/companies", "PUT", "companies"));
                        listPermissions.add(new Permission("Delete a company", "/api/v1/companies/{id}", "DELETE",
                                        "companies"));
                        listPermissions.add(new Permission("Get a company by id", "/api/v1/companies/{id}", "GET",
                                        "companies"));
                        listPermissions
                                        .add(new Permission("Get companies with pagination", "/api/v1/companies/{id}",
                                                        "GET", "companies"));

                        listPermissions.add(new Permission("Create a job", "/api/v1/jobs", "POST", "jobs"));
                        listPermissions.add(new Permission("Update a job", "/api/v1/jobs", "PUT", "jobs"));
                        listPermissions.add(new Permission("Delete a job", "/api/v1/jobs/{id}", "DELETE", "jobs"));
                        listPermissions.add(new Permission("Get a job by id", "/api/v1/jobs/{id}", "GET", "jobs"));
                        listPermissions
                                        .add(new Permission("Get jobs with pagination", "/api/v1/jobs/{id}", "GET",
                                                        "jobs"));

                        listPermissions.add(new Permission("Create a resume", "/api/v1/resumes", "POST", "resumes"));
                        listPermissions.add(new Permission("Update a resume", "/api/v1/resumes", "PUT", "resumes"));
                        listPermissions.add(
                                        new Permission("Delete a resume", "/api/v1/resumes/{id}", "DELETE", "resumes"));
                        listPermissions.add(
                                        new Permission("Get a resume by id", "/api/v1/resumes/{id}", "GET", "resumes"));
                        listPermissions
                                        .add(new Permission("Get resumes with pagination", "/api/v1/resumes/{id}",
                                                        "GET", "resumes"));

                        listPermissions.add(new Permission("Create a role", "/api/v1/companies", "POST", "roles"));
                        listPermissions.add(new Permission("Update a role", "/api/v1/roles", "PUT", "roles"));
                        listPermissions.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "roles"));
                        listPermissions.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET", "roles"));
                        listPermissions
                                        .add(new Permission("Get roles with pagination", "/api/v1/roles/{id}", "GET",
                                                        "roles"));

                        listPermissions.add(new Permission("Create a user", "/api/v1/users", "POST", "users"));
                        listPermissions.add(new Permission("Update a user", "/api/v1/users", "PUT", "users"));
                        listPermissions.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "users"));
                        listPermissions.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "users"));
                        listPermissions
                                        .add(new Permission("Get users with pagination", "/api/v1/users/{id}", "GET",
                                                        "users"));

                        listPermissions.add(new Permission("Create a subscriber", "/api/v1/subscribers", "POST",
                                        "subscribers"));
                        listPermissions.add(new Permission("Update a subscriber", "/api/v1/subscribers", "PUT",
                                        "subscribers"));
                        listPermissions
                                        .add(new Permission("Delete a subscriber", "/api/v1/subscribers/{id}", "DELETE",
                                                        "subscribers"));
                        listPermissions
                                        .add(new Permission("Get a subscriber by id", "/api/v1/subscribers/{id}", "GET",
                                                        "subscribers"));
                        listPermissions
                                        .add(new Permission("Get subscribers with pagination",
                                                        "/api/v1/subscribers/{id}", "GET",
                                                        "subscribers"));

                        listPermissions
                                        .add(new Permission("Download a file", "/api/v1/file", "POST",
                                                        "subscribers"));
                        listPermissions
                                        .add(new Permission("Upload file",
                                                        "/api/v1/files", "GET",
                                                        "subscribers"));

                        this.permissionRepository.saveAll(listPermissions);
                }

                if (countRoles == 0) {
                        List<Permission> allPermission = this.permissionRepository.findAll();

                        Role adminRole = new Role();
                        adminRole.setName("SUPER_ADMIN");
                        adminRole.setDescription("Full permission");
                        adminRole.setActive(true);
                        adminRole.setPermissions(allPermission);

                        this.roleRepository.save(adminRole);
                }

                if (countUsers == 0) {
                        User adminUser = new User();
                        adminUser.setEmail("admin@gmail.com");
                        adminUser.setAddress("HCM");
                        adminUser.setAge(18);
                        adminUser.setGender(GenderEnum.MALE);
                        adminUser.setName("Super Admin");
                        adminUser.setPassword(this.passwordEncoder.encode("123456"));

                        Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
                        if (adminRole != null) {
                                adminUser.setRole(adminRole);
                        }

                        this.userRepository.save(adminUser);
                }

                if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
                        System.out.println(">>> SKIP INIT DATABASE");
                } else {
                        System.out.println(">>> END INIT DATABASE");
                }
        }

}
