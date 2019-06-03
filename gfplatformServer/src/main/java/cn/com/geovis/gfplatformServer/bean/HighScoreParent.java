package cn.com.geovis.gfplatformServer.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Geometry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@ApiModel
@Table(name="high_score")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class HighScoreParent implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private String classification;
    private String imageId;
    private String name;
//    @JsonIgnore
//    @ApiModelProperty(hidden = true)
    private Geometry coordinate;
    private Double cameralon;
    private Double cameralat;
    private Double cameraHeight;
    private String imageUrl;
    private Boolean isBigImage;
    private String tuli;
    private String tuliUrl;
    private String flag;
    private Double minx;
    private Double mixy;
    private Double maxx;
    private Double maxy;
    private Integer pid;
    private String layers;
    
    @ApiModelProperty(value = "创建时间", example = "2018-05-18 02:21:50", allowEmptyValue = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate = new Date();
    private String dataType;
    
    @JoinColumn(name = "pid",insertable=false,updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private HighScoreParent parent;
    
    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "highScoreId", referencedColumnName = "id")
    private List<AssociatedGeojson> data;
}

