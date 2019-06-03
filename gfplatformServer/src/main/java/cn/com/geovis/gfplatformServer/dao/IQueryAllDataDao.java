package cn.com.geovis.gfplatformServer.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.com.geovis.gfplatformServer.bean.HighScoreParent;

public interface IQueryAllDataDao extends JpaRepository<HighScoreParent, Integer> {

	List<HighScoreParent> findByNameLike(String name);
	 
}
