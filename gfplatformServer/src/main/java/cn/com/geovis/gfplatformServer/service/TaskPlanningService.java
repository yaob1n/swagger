package cn.com.geovis.gfplatformServer.service;



import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.com.geovis.gfplatformServer.bean.Demand;
import cn.com.geovis.gfplatformServer.bean.DemandEntity;

public interface TaskPlanningService {

//	Demand submitData(String name, String observe_type, String priority, Date begin_date, Date end_date,
//			Timestamp begin_time, Timestamp end_time, String observe_frequency, String coordinate_type,
//			String coordinates);
	
	Demand submitData (DemandEntity demand);
	
    Page<Demand> getByPage(int currentPage, int pageSize,String name,String observeType,String priority,Date beginDate,Date endDate,String observeFrequency);
    
	public Page<Demand> list(Pageable pageable,String name,String observeType,String priority,Date beginDate,Date endDate,String observeFrequency);

}
