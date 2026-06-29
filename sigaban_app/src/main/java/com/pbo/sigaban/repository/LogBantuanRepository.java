package com.pbo.sigaban.repository;

import com.pbo.sigaban.model.LogBantuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogBantuanRepository extends JpaRepository<LogBantuan, Long> {
    List<LogBantuan> findByTipeOrderByWaktuDesc(String tipe);
}
