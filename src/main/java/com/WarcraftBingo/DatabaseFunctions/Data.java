package com.WarcraftBingo.DatabaseFunctions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Data extends JpaRepository<Database, String> {
    @Query(value = "SELECT MoltenCore FROM WarcraftBingo.Raids", nativeQuery = true)
    List<String> getMoltenCore();

    @Query(value = "SELECT BlackwingLair FROM WarcraftBingo.Raids", nativeQuery = true)
    List<String> getBlackwingLair();

    @Query(value = "SELECT ZulGurub FROM WarcraftBingo.Raids", nativeQuery = true)
    List<String> getZulGurub();
}
