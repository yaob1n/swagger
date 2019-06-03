package cn.com.geovis.gfplatformServer.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.geovis.gfplatformServer.bean.Demand;
import cn.com.geovis.gfplatformServer.bean.DemandEntity;
import cn.com.geovis.gfplatformServer.dao.IDemandDao;
import cn.com.geovis.gfplatformServer.service.TaskPlanningService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@EnableAutoConfiguration
@Transactional(rollbackFor = Exception.class)
public class TaskPlanningServiceImpl implements TaskPlanningService{

	@Autowired
	private IDemandDao  demandDao;

	@Override
	public Demand submitData(DemandEntity demand) {
		Demand data = new Demand();
		data.setName(demand.getName());
		data.setObserveType(demand.getCoordinateType());
		data.setPriorit(demand.getPriorit());
		data.setBeginDate(demand.getBeginDate());
		data.setEndDate(demand.getEndDate());
		data.setBeginTime(demand.getBeginTime());
		data.setEndTime(demand.getEndTime());
		data.setObserveFrequency(demand.getCoordinateType());
		data.setCoordinateType(demand.getCoordinateType());
		data.setCoordinates(demand.getCoordinates());
		
		Demand demandEntity = this.demandDao.save(data);
		return demandEntity;
	}
	
	
	@Override
	public Page<Demand> getByPage(int currentPage, int pageSize,String name,String observeType,String priority,Date beginDate,Date endDate,String observeFrequency) {
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.Direction.ASC,"id");
        return list(pageable,name,observeType,priority,beginDate,endDate,observeFrequency);

	}
	
	@Override
	public Page<Demand> list(Pageable pageable, String name,String observeType,
			String priority,Date beginDate,Date endDate,String observeFrequency) {
		
		Demand example = new Demand();
		if(name != null) {
			example.setName(name);
		}
		if(observeType != null) {
			example.setObserveType(observeType);
		}
		if(!StringUtils.isEmpty(priority)) {
			example.setPriorit(priority);
		}
		if(beginDate != null) {
			example.setBeginDate(beginDate);
		}
		if(endDate != null) {
			example.setEndDate(endDate);
		}
		if(observeFrequency != null) {
			example.setObserveFrequency(observeFrequency);
		}
		
//		ExampleMatcher matcher = ExampleMatcher.matching()
//	            .withMatcher("name" ,ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		
	    //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("name", GenericPropertyMatchers.startsWith()) //姓名采用“开始匹配”的方式查询
                .withIgnorePaths("focus");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
        
        
		return this.demandDao.findAll(Example.of(example,matcher),pageable);
	
	}

}
