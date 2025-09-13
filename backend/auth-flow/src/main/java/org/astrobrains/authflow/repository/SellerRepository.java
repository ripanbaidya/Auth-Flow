package org.astrobrains.authflow.repository;

import org.astrobrains.authflow.enums.AccountStatus;
import org.astrobrains.authflow.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByEmail(String email);

    Optional<Seller> findByPhoneNumber(String phoneNumber);

    // find all the seller based on there account status
    Page<Seller> findByAccountStatus(AccountStatus status, Pageable pageable);
}
