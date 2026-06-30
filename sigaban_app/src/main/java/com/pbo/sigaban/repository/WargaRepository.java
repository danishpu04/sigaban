package com.pbo.sigaban.repository;

import com.pbo.sigaban.model.Warga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WargaRepository extends JpaRepository<Warga, Long> {

    @Query("SELECT COALESCE(SUM(w.family), 0) FROM Warga w")
    Long sumFamily();

    @Query("SELECT COUNT(DISTINCT w.evacPoint) FROM Warga w")
    Long countDistinctEvacPoint();

    List<Warga> findTop3ByOrderByIdDesc();

    long countByEvacPoint(String evacPoint);

    @Query("SELECT COALESCE(SUM(w.family + 1), 0) FROM Warga w WHERE w.evacPoint = :evacPoint")
    long sumOrangByEvacPoint(@Param("evacPoint") String evacPoint);
}
