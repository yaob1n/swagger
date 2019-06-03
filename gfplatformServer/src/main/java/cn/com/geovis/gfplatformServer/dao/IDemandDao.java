package cn.com.geovis.gfplatformServer.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.com.geovis.gfplatformServer.bean.Demand;

public interface IDemandDao extends JpaRepository<Demand, Integer> {

	
    Page<Demand> findAll(Pageable pageable);

    
}
