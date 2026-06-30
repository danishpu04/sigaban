package com.pbo.sigaban.repository;

import com.pbo.sigaban.model.Posko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoskoRepository extends JpaRepository<Posko, Long> {
    Optional<Posko> findByNama(String nama);
}
