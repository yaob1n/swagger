package cn.com.geovis.gfplatformServer.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonParseException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@ApiModel
@Table(name="high_score")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class HighScoreInsert implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private String classification;
    private String imageId;
    private String name;
	@JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDerializer.class)
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
    
    @ApiModelProperty(value = "创建时间", example = "2018-05-18 02:21:50", allowEmptyValue = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate = new Date();
    private String dataType;
    private String layers;

    
	public static class GeometrySerializer extends JsonSerializer<Geometry>{

		@Override
		public void serialize(Geometry geometry, JsonGenerator gen, SerializerProvider serializers)
				throws IOException{
			
			String wkt =  null;
			if(geometry != null){
				wkt = geometry.toText();
				gen.writeString(wkt);
			}
			
		}

	}
	public static class GeometryDerializer extends JsonDeserializer<Geometry> {

		@Override
		public Geometry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

			String wkt = p.getText();
			Geometry geometry = null;
			try {
				if(wkt.startsWith("SRID")){
					String[] strarray = wkt.split(";");
					int srid = Integer.parseInt(strarray[0].substring(5));
					wkt = strarray[1];
					geometry  = new WKTReader().read(wkt);
					geometry.setSRID(srid);
				}else{
					geometry  = new WKTReader().read(wkt);
					geometry.setSRID(4326);
				}
			}catch (Exception e) {
				throw new JsonParseException("Geometry反序列化失败");
			}
			
			return geometry;
		}

	}
}

