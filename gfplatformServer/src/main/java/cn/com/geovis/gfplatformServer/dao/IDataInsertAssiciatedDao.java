package cn.com.geovis.gfplatformServer.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.com.geovis.gfplatformServer.bean.AssociatedGeojson;

public interface IDataInsertAssiciatedDao extends JpaRepository<AssociatedGeojson, Integer> {

	Optional<AssociatedGeojson> findById(Integer id);

}
