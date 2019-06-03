package cn.com.geovis.gfplatformServer.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.com.geovis.gfplatformServer.bean.AssociatedGeojson;
import cn.com.geovis.gfplatformServer.bean.HighScore;

public interface IQueryDataDao extends JpaRepository<HighScore, Integer> {
	
	   Optional<HighScore> findByClassification(String classification);
	   
	   List<HighScore> findByNameLike (String name);
	   
	   @Query(value = "select * from high_score where pid is NULL", nativeQuery = true)
	   List<HighScore> findByPid ();
	   
	   @Modifying
	   @Query(value = "insert into high_score(classification,name,create_date,pid) values(?1,?2,?3,?4)",nativeQuery = true)
	   int addHighScoreDateSecond(String classification,String name,Date date,int pid);
	   
	   @Modifying
	   @Query(value = "insert into high_score(classification,image_id,name,coordinate,"
	   		+ "cameralon,cameralat,camera_height,image_url,is_big_image,tuli,tuli_url,"
	   		+ "flag,leftlon,leftlat,rightlon,rightlat,pid,create_date,"
	   		+ "data_type) values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,"
	   		+ "?11,?12,?13,?14,?15,?16,?17,?18,?19)",nativeQuery = true)
	   int addHighScoreDate(String classification,String imageId,String name,String coordinate,
			   Double cameralon,Double cameralat,Double cameraHeight,String imageUrl,
			   String isBigImage,String tuli,String tuliUrl,String flag,Double leftlon,
			   Double leftlat,Double rightlon,Double rightlat,int pid, String date,String dateType);

	   @Query(value = "select * from high_score where name like %?1% and classification like ?2%",nativeQuery = true)
	   List<HighScore> findByNameAndClassification(String name, String classification);
	   
//	   @Modifying
//	   @Transactional
//	   @Query(value = "update high_score hs set "
//			   		  +"hs.layers = CASE WHEN :#{highScore.layers} IS NULL THEN hs.layers ELSE :#{#highScore.layers} END ,"
//	   				  +"hs.name = CASE WHEN :#{#highScore.name} IS NULL THEN hs.name ELSE :#{#highScore.name} END ,"
//			   		  +"hs.tuli_url = CASE WHEN :#{#highScore.tuliUrl} IS NULL THEN hs.tuli_url ELSE :#{#highScore.tuliUrl} END "
//	   				  +"where hs.id = :#{#highScore.id}",nativeQuery = true)
//	   boolean updateById(@Param("highScore") HighScore highScore);

	   Optional<HighScore> findById(String id);

	   @Transactional
	   @Modifying
	   @Query(value = "update high_score set layers= ?1,name= ?2,tuli_url= ?3 where id= ?4",nativeQuery = true)
	   int updateById(String layers, String name, String tuliUrl,Integer id);


	   @Transactional
	   @Modifying
	   @Query(value = "update associated_geojson set layers= ?1,description= ?2,url= ?3 where id= ?4",nativeQuery = true)
	   int updateById1(String layers, String description, String url, Integer id);
	   
}
