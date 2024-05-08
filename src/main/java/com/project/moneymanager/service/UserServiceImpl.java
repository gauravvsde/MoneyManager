package com.project.moneymanager.service;/*
 * @author gauravverma
 */

import com.project.moneymanager.dto.UserDto;
import com.project.moneymanager.exceptions.ResourceNotFoundException;
import com.project.moneymanager.model.User;
import com.project.moneymanager.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDto createUser(User user) throws Exception {
        user.setCreateAt(new Timestamp(System.currentTimeMillis()));
        try {
            User savedUser = userRepository.save(user);
            return modelMapper.map(savedUser, UserDto.class);
        }catch (Exception e){
            throw new Exception("Failed to create user", e);
        }
    }

    @Override
    public UserDto readUser(Long id) throws Exception {
        try {
            User savedUser = userRepository.findById(id).get();
            return modelMapper.map(savedUser, UserDto.class);
        }catch (Exception e){
            throw new ResourceNotFoundException("user with id : " + id + " doesn't exist.");
        }
    }

    @Override
    public UserDto updateUser(User user, Long id) {
        User existingUser = userRepository.findById(id).get();
        existingUser.setName(user.getName() != null ? user.getName() : existingUser.getName());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
//        existingUser.setPassword(user.getPassword() != null ? bcryptEncoder.encode(user.getPassword()) : existingUser.getPassword());
        existingUser.setAge(user.getAge() != null ? user.getAge() : existingUser.getAge());
        return modelMapper.map(userRepository.save(existingUser), UserDto.class);
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        User existingUser = userRepository.findById(id).get();
        userRepository.delete(existingUser);
    }
}