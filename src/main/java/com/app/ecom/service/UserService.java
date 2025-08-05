package com.app.ecom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers()
    {
        return userRepository.findAll()
                .stream().map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void createUser(UserRequest userRequest)
    {
        User user = new User();
        updateUserFromRequest(user,userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> findUser(Long id)
    {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }
    public boolean updateUser(Long id,User updatedUser)
    {
        return userRepository.findById(id)
                .map(existingUser->{
                    existingUser.setFirstname(updatedUser.getFirstname());
                    existingUser.setLastname(updatedUser.getLastname());
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }
    private UserResponse mapToUserResponse(User user)
    {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstname(user.getFirstname());
        userResponse.setLastname(user.getLastname());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        if(userResponse.getAddressDTO() != null)
        {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddressDTO(addressDTO);
        }
        return userResponse;
    }
    private void updateUserFromRequest(User user,UserRequest userRequest)
    {
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setPhone(userRequest.getPhone());
        user.setEmail(userRequest.getEmail());
        if(userRequest.getAddress()!=null)
        {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
    }
}
