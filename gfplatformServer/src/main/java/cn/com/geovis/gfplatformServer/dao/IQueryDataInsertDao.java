package cn.com.geovis.gfplatformServer.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.com.geovis.gfplatformServer.bean.HighScore;
import cn.com.geovis.gfplatformServer.bean.HighScoreInsert;

public interface IQueryDataInsertDao extends JpaRepository<HighScoreInsert, Integer> {
	
	Optional<HighScoreInsert> findByClassification(String classification);
	
    @Query(value = "select * from high_score where id=(select MAX(id) from high_score where classification like CONCAT(:classification,'%'))", nativeQuery = true)
    Optional<HighScoreInsert> findSecondByLikeClassification (@Param("classification") String classification);

	
}
