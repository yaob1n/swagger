package cn.com.geovis.gfplatformServer.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@ApiModel
@Table(name="associated_geojson")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class AssociatedGeojson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    @ApiModelProperty(value = "创建时间", example = "2018-05-18 02:21:50", allowEmptyValue = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
    @Column
	private String url;
    @Column
    @JsonIgnore
	private Integer highScoreId;
    @Column
    private String type;
    @Column
    private String layers;
    @Column
    private String time;
    @Column
    private String typeLabel;
    @Column
    private String description;

}
