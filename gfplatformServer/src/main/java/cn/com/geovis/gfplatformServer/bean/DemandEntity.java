package cn.com.geovis.gfplatformServer.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DemandEntity{
    private String name;
    private String observeType;
    private String priorit;
    
    @ApiModelProperty(value = "开始时间", example = "2018-05-18", allowEmptyValue = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginDate = new Date();
    
    @ApiModelProperty(value = "结束时间", example = "2018-05-18", allowEmptyValue = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate = new Date();

    private String beginTime;
    private String endTime;
    private String observeFrequency;
    private String coordinateType;
    private String coordinates;
    

}

