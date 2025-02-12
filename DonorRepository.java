package com.fooddonation.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddonation.model.Donor;
@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    List<Donor> findByCityIgnoreCase(String city);
}
