package com.pbo.sigaban.repository;

import com.pbo.sigaban.model.Inventaris;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarisRepository extends JpaRepository<Inventaris, Long> {
    Optional<Inventaris> findByNamaItem(String namaItem);
}
