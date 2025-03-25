package org.ssu.belous.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.belous.dto.request.CustomerInfoRequestDto;
import org.ssu.belous.model.CustomerInfo;
import org.ssu.belous.model.User;
import org.ssu.belous.repository.CustomerRepository;
import org.ssu.belous.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void addCustomerInfo(String username, CustomerInfoRequestDto requestDto){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("Пользователь не найден");
        UUID id = user.get().getUuid();

        CustomerInfo customerInfo = CustomerInfo.builder()
                .uuid(id)
                .name(requestDto.name())
                .secondName(requestDto.second_name())
                .lastName(requestDto.last_name())
                .city(requestDto.city())
                .street(requestDto.street())
                .number_home(requestDto.number_home())
                .number_apartment(requestDto.number_apartment())
                .build();

        customerRepository.save(customerInfo);
    }
}
