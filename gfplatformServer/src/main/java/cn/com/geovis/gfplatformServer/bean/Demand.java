package cn.com.geovis.gfplatformServer.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@ApiModel
@Table(name="demand")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Demand implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	@Column
    private String name;
	@Column
    private String observeType;
	@Column
    private String priorit;
    
	@Column
//    @ApiModelProperty(value = "开始时间", example = "2018-05-18", allowEmptyValue = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginDate;
    
	@Column
//    @ApiModelProperty(value = "结束时间", example = "2018-05-18", allowEmptyValue = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    
	@Column
    private String beginTime;
	@Column
    private String endTime;
	@Column
    private String observeFrequency;
	@Column
    private String coordinateType;
	@Column
    private String coordinates;
    

}
