package org.ssu.belous.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssu.belous.model.CustomerInfo;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerInfo, UUID> {

}
