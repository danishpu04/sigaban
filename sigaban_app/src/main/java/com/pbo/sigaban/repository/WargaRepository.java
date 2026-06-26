package com.pbo.sigaban.repository;

import com.pbo.sigaban.model.Warga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WargaRepository extends JpaRepository<Warga, Long> {
}
