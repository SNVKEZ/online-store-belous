package org.ssu.belous.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ssu.belous.model.CustomerInfo;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerInfo, UUID> {
    @Query("SELECT c FROM gen_customer_info c WHERE c.uuid = :uuid")
    CustomerInfo findByUuid(@Param("uuid") UUID uuid);
}
