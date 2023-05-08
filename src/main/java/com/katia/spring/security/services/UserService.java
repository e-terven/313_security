package com.katia.spring.security.services;

import com.katia.spring.security.model.CreateOrEditUserModel;
import com.katia.spring.security.entities.Role;
import com.katia.spring.security.entities.User;
import com.katia.spring.security.repositories.RoleRepository;
import com.katia.spring.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository,RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public List<User> findAll() {return userRepository.findAll();}
    public User findById(Long id) {
        return userRepository.getOne(id);
    }
    public User findByEmail(String email) {return userRepository.findByEmail(email);
    }

    // --------------------------CREATE----------------------------------------

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(CreateOrEditUserModel userCreateDto) {

        User newUser = new User();
        newUser.setEmail(userCreateDto.getEmail());
        newUser.setFirstName(userCreateDto.getFirstName());
        newUser.setLastName(userCreateDto.getLastName());
        newUser.setAge(userCreateDto.getAge());
        newUser.setPassword(userCreateDto.getPassword());

        var roles = roleService.findByRoleNames(userCreateDto.getRoleNames());
        newUser.setRoles(new HashSet<>(roles));
        userRepository.save(newUser);

        return userRepository.save(newUser);
    }

    // --------------------------UPDATE----------------------------------------


    public boolean isEmailTakenByOtherUser(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }
    public void updateUser(Long id, CreateOrEditUserModel userUpdateDto) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(userUpdateDto.getEmail());
            user.setFirstName(userUpdateDto.getFirstName());
            user.setLastName(userUpdateDto.getLastName());
            user.setAge(userUpdateDto.getAge());
            user.setPassword(userUpdateDto.getPassword());

            var roles = roleService.findByRoleNames(userUpdateDto.getRoleNames());
            user.setRoles(new HashSet<>(roles));
            userRepository.save(user);
        }
    }

    // --------------------------DELETE----------------------------------------

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // --------------------------SuccessUserHandler----------------------------------------

    public Long getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return user.getId();
    }
}
