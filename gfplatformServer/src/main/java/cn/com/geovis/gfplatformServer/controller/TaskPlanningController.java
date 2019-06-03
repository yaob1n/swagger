package cn.com.geovis.gfplatformServer.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.geovis.gfplatformServer.bean.Demand;
import cn.com.geovis.gfplatformServer.bean.DemandEntity;
import cn.com.geovis.gfplatformServer.common.PageRestResponse;
import cn.com.geovis.gfplatformServer.common.RestError;
import cn.com.geovis.gfplatformServer.service.TaskPlanningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "任务规划需求")
@RestController
@RequestMapping("api/task")
public class TaskPlanningController {

    @Autowired
    private TaskPlanningService TaskPlanningServiceImpl;

    @SuppressWarnings({ "rawtypes", "unchecked" })
//    @ApiResponses({@ApiResponse(code = 500, response = RestError.class, message = "错误")})
    @ApiOperation(value = "提交任务规划需求", notes = "提交任务规划需求")
    @PostMapping(value = "demand")
    public ResponseEntity<Demand> submitDemand(@RequestBody DemandEntity demandValue) {
		try {
			log.info("文件提交任务需求");
			Demand demand = this.TaskPlanningServiceImpl.submitData(demandValue);
	      	return ResponseEntity.ok(demand);
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
    @GetMapping(value = "demand")
    @ApiOperation(value = "分页获取数据", notes = "查询数据")
    public ResponseEntity<PageRestResponse<Demand>> getTargerList(@ApiParam(value = "当前页,从1开始") @RequestParam(defaultValue = "1") int currentPage,
                                                            @ApiParam(value = "每页条目数") @RequestParam(defaultValue = "20") int pageSize,
                                                            @ApiParam(value = "需求名称") @RequestParam(required = false) String name,
                                                            @ApiParam(value = "观察类型") @RequestParam(required = false) String observeType,
                                                            @ApiParam(value = "优先级") @RequestParam(required = false) String priority,
                                                            @ApiParam(value = "需求有效开始时间") @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date beginDate,                                                
                                                            @ApiParam(value = "需求有效结束时间") @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,      
                                                            @ApiParam(value = "观测") @RequestParam(required = false) String observeFrequency) {
        log.info("开始获取目标, 第{}页,每页{}个",currentPage,pageSize);
        return ResponseEntity.ok(PageRestResponse.formatByPageDate(this.TaskPlanningServiceImpl.getByPage(currentPage-1,pageSize,name,observeType,priority,beginDate,endDate,observeFrequency)));
    }
    public Map<String, Object> getPublicBackValue(boolean flag,String message){
      	Map<String, Object>  map= new HashMap<String, Object>();
      	if(flag){
      		map.put("code", 1);
      	}else{
	       	map.put("code", 0);
      	}
      	map.put("message", message);	      
      	return map;
    }
}
