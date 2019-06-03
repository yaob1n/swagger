package cn.com.geovis.gfplatformServer.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

import cn.com.geovis.gfplatformServer.bean.AssociatedGeojson;
import cn.com.geovis.gfplatformServer.bean.HighScore;
import cn.com.geovis.gfplatformServer.bean.HighScoreInsert;
import cn.com.geovis.gfplatformServer.bean.HighScoreParent;
import cn.com.geovis.gfplatformServer.dao.IDataInsertAssiciatedDao;
import cn.com.geovis.gfplatformServer.dao.IQueryAllDataDao;
import cn.com.geovis.gfplatformServer.dao.IQueryDataDao;
import cn.com.geovis.gfplatformServer.dao.IQueryDataInsertDao;
import cn.com.geovis.gfplatformServer.service.QueryDataService;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@EnableAutoConfiguration
@Transactional(rollbackFor = Exception.class)
public class QueryDataServiceImpl implements QueryDataService{
	
	@Autowired
	private IQueryDataDao queryDataDao;
	
	@Autowired
	private IQueryAllDataDao queryAllDataDao;
	
	@Autowired
	private IQueryDataInsertDao queryAllDataInsertDao;
	
	@Autowired
	private IDataInsertAssiciatedDao insertAssiciatedDao;
	/**
	 * 通过id、分类获取数据
	 *
	 * @param id
	 * @return
	 */
	@Override
	public HighScore getByClassification(String classification) {
//		Optional<HighScore> de = this.queryDataDao.findByIdAndClassification(id,classification);
		Optional<HighScore> de = this.queryDataDao.findByClassification(classification);
		if (de.isPresent()) {
			return de.get();
		} else {
			return null;
		}
	}
	
	@Override
	public List<HighScoreParent> findByName(String name) {
		return this.queryAllDataDao.findByNameLike("%" + name + "%");
	}
	
	@Override
	public List<HighScore> findByNameAndClassifcation(String name,String classification) {
		List<HighScore> list = new ArrayList<>();
		HighScore highScore =new HighScore();
		if(!StringUtils.isEmpty(classification)) {
			Optional<HighScore> de = this.queryDataDao.findByClassification(classification);
			if (de.isPresent()) {
				highScore = de.get();
			}
			list.add(this.analysisData(highScore,name));
		}else{
			List<HighScore> de = this.queryDataDao.findByPid();//查询有几个分类
			for (HighScore item : de) {
				String Classifi = item.getClassification();
				Optional<HighScore> deValue = this.queryDataDao.findByClassification(Classifi);
				if (deValue.isPresent()) {
					highScore = deValue.get();
				}
				list.add(this.analysisData(highScore,name));
			}
		}
		
		return list;
	}
	public HighScore analysisData(HighScore highScore,String name){
		if((highScore.getName()).indexOf(name) != -1){
			return highScore;
		}
		List<HighScore> data  = highScore.getChildren();
//		for(int i = data.size()-1; i >= 0;i--){
		for(int i = 0; i <data.size();i++){
			HighScore item = data.get(i);
			if((item.getName()).indexOf(name) != -1 ){
//				if(item.getChildren().size() != 0){
//					item = this.analysisData(item, name);
//					if(item.getChildren().size() == 0){
//						data.remove(i);
//					}
//				}			
			}else{
				if(item.getChildren().size() == 0){
					data.remove(i);
					i--;
				}else{
					item = this.analysisData(item, name);
					if(item.getChildren().size() == 0){
						data.remove(i);
						i--;
					}
				}
			}
			
		}
		return highScore;
	}
	
	@Override
	public Boolean isExist(String classification){
		Optional<HighScoreInsert> de = this.queryAllDataInsertDao.findByClassification(classification);
		if (de.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean insertFile(String classification,String AreaLayersPath, String AreaPanelPath) {
		try {
	       //AreaPanelPath文件的解析
	       File panelFile = new File(AreaPanelPath);
	       String panelInput = FileUtils.readFileToString(panelFile, "UTF-8");
	       int fun = panelInput.indexOf("{");
	       panelInput = panelInput.substring(fun);
	       JSONObject panelJsonObject = new JSONObject(panelInput.trim());
	       String title = (String) panelJsonObject.get("title");
	       //主节点
	       HighScoreInsert high = new HighScoreInsert();
	       high.setClassification(classification);
	       high.setName(title);
	       //插入一级名称
	       HighScoreInsert csore = this.queryAllDataInsertDao.save(high);
	       JSONArray contents = panelJsonObject.getJSONArray("contents");
	       
	       JSONArray singleLayers = null;
	       if(AreaLayersPath != null){
			   //AreaLayersPath文件的解析
		       File file = new File(AreaLayersPath);
		       String input = FileUtils.readFileToString(file, "UTF-8");
		       JSONObject jsonObject = new JSONObject(input);
		       if (jsonObject != null) {
	     	      singleLayers =jsonObject.getJSONArray("singleLayers");
		       }
	       }
	       
	       for(int i = 0;i < contents.length(); i++){
	    	   JSONObject job = contents.getJSONObject(i); 
	    	   String typeTitle = (String) job.get("typeTitle");
	    	   HighScoreInsert highSecond = new HighScoreInsert();
	    	   highSecond.setClassification(classification + "-" +(i+1));
	    	   highSecond.setName(typeTitle);
	    	   highSecond.setPid(csore.getId());
	    	   highSecond.setLayers(job.has("layers")?(String)job.get("layers"):"");
	    	   //插入二级名称
	    	   HighScoreInsert csoreSecond = this.queryAllDataInsertDao.save(highSecond);
	    	   //循环插入其他层级以及关联
	    	   JSONArray dataSpan =job.getJSONArray("dataSpan");//中的节点
	    	   if(AreaLayersPath != null){
	    		   this.insertDateToTable(dataSpan,singleLayers,csoreSecond);
	    	   }else{
	    		   for(int k = 0;k<dataSpan.length();k++){
	   				JSONObject data = dataSpan.getJSONObject(k);
	   				String id = (String)data.get("id");
	   				String name = (String)data.get("name");
	   			    this.insert(id,name,csoreSecond,data,k);
	   			}
	    	   }
	    	   
	       }
	       return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void insertDateToTable(JSONArray dataSpan,JSONArray singleLayers,HighScoreInsert csore){
		try {
			for(int i = 0;i<dataSpan.length();i++){
				JSONObject job = dataSpan.getJSONObject(i);
				String id = (String)job.get("id");
				String name = (String)job.get("name");
				int num = 0;
				for(int j = 0;j<singleLayers.length();j++){
					JSONObject singleLayersJob = singleLayers.getJSONObject(j);
					String singleLayersId = (String)singleLayersJob.get("id");
					if(id.equals(singleLayersId)){
						num++;
						this.insert(id,name,csore,singleLayersJob,i);
						break;
					}
				}
				if(num == 0){
					this.insert(id,name,csore,job,i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void insert(String id,String name,HighScoreInsert csore,JSONObject job,int i){
		try {
			HighScoreInsert high = new HighScoreInsert();
			high.setImageId(id);
			high.setName(name);
			high.setPid(csore.getId());
			high.setCameralat(job.has("cameraLat")?Double.parseDouble((String)job.get("cameraLat")):null);
			high.setCameralon(job.has("cameraLon")?Double.parseDouble((String)job.get("cameraLon")):null);
			high.setCameraHeight(job.has("cameraHeight")?Double.parseDouble((String)job.get("cameraHeight")):null);
			
			if(job.has("coordinate")){
				JSONArray coordinate = job.getJSONArray("coordinate");
		        Double minx = coordinate.getDouble(0);
		        Double miny = coordinate.getDouble(1);
		        Double maxx = coordinate.getDouble(2);
		        Double maxy = coordinate.getDouble(3);
				Geometry geometry = this.getBboxGeometry(minx,miny,maxx,maxy);
			    high.setCoordinate(geometry);
			    high.setMinx(coordinate.getDouble(0));
			    high.setMixy(coordinate.getDouble(1));
			    high.setMaxx(coordinate.getDouble(2));
			    high.setMaxy(coordinate.getDouble(3));
			}
			high.setLayers(job.has("layers")?(String)job.get("layers"):"");
			high.setTuli(job.has("tuli")?(String)job.get("tuli"):"");
			high.setTuliUrl(job.has("tuliUrl")?(String)job.get("tuliUrl"):"");
			high.setImageUrl(job.has("imageUrl")?(String)job.get("imageUrl"):"");
			high.setIsBigImage(job.has("isBigImage")?Boolean.parseBoolean((String)job.get("isBigImage")):null);
			high.setClassification(csore.getClassification() + "-" +(i+1));
			HighScoreInsert csoreSecond = this.queryAllDataInsertDao.save(high);
			
			if(job.has("data")){
				JSONArray gojsons = job.getJSONArray("data");
				for(int k = 0;k<gojsons.length();k++){
					JSONObject gojson = gojsons.getJSONObject(k);
					AssociatedGeojson geojson = new AssociatedGeojson();
					geojson.setHighScoreId(csoreSecond.getId());
					geojson.setUrl(gojson.has("url")?(String)gojson.get("url"):"");
					geojson.setType(gojson.has("type")?(String)gojson.get("type"):"");
					geojson.setTime(gojson.has("time")?(String)gojson.get("time"):"");
					geojson.setLayers(gojson.has("layers")?(String)gojson.get("layers"):"");
					geojson.setCreateTime(new Date());
					geojson.setTypeLabel(gojson.has("typeLabel")?(String)gojson.get("typeLabel"):"");
					geojson.setDescription(gojson.has("description")?(String)gojson.get("description"):"");
					AssociatedGeojson associatedGeojson = this.insertAssiciatedDao.save(geojson);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
    public Geometry getBboxGeometry(Double minx,Double miny,Double maxx,Double maxy) {
    	Geometry dataBoundary = null;
        try {
	        int srid = Integer.valueOf("4326");
//	        String wktString = String.format("POLYGON((%s %s,%s %s,%s %s,%s %s,%s %s))", minX, minY, maxX, minY, maxX, maxY, minX, maxY, minX, minY);
	        String polygon = "POLYGON((" + minx + " " + miny + ", " + minx + " " + maxy + ", " + maxx + " " + maxy + ", " + maxx + " " + miny + ", " + minx + " " + miny + "))";
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            WKTReader reader = new WKTReader(geometryFactory);
            dataBoundary = reader.read(polygon);
            dataBoundary.setSRID(srid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dataBoundary;
    }
    
    public Boolean insertExeclFile(String execlPath,String classification){
    	 try {
    	    execlPath = "D:/数据收集规范表格2.xlsx";
//    	    execlPath = "D:/地名服务接口统计.xls";
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(execlPath));
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
//            XSSFRow titleCell = xssfSheet.getRow(0);
            String comparison = "";
            //找到一级节点
            HighScoreInsert oneHighScore = new HighScoreInsert();
    		Optional<HighScoreInsert> deOne = this.queryAllDataInsertDao.findByClassification(classification);
    		if (deOne.isPresent()) {
    			oneHighScore = deOne.get();
    		}
    		HighScoreInsert twoHighScore = new HighScoreInsert();
    		int classific = 1;
    		Optional<HighScoreInsert> deSecond = this.queryAllDataInsertDao.findSecondByLikeClassification(classification);
    		if (deSecond.isPresent()) {
    			twoHighScore = deSecond.get();
    			String[] classifications = twoHighScore.getClassification().split("-");
    			classific = classifications.length>=2?Integer.parseInt(classifications[1])+1:1;
    		}
    		int y = 0;
    		HighScoreInsert csoreSecond = null;
            for (int i = 1; i <= xssfSheet.getLastRowNum(); i++) {
                XSSFRow xssfRow = xssfSheet.getRow(i);
//                int minCell = xssfRow.getFirstCellNum();
//                int maxCell = xssfRow.getLastCellNum();
//                XSSFCell parent = xssfRow.getCell(0);
                if(xssfRow.getCell(0) == null){
                	break;
                }
                String parent = String.valueOf((xssfRow.getCell(0)).getStringCellValue());
//                Integer parentid = Integer.valueOf((xssfRow.getCell(1)).getStringCellValue());
                if(i==1 || parent != comparison){
                	comparison = parent;
                	//插入二级节点
                	HighScoreInsert secondhigh = new HighScoreInsert();
                	secondhigh.setClassification(classification + "-" +(classific+y));
                	secondhigh.setName(parent);
                	secondhigh.setPid(oneHighScore.getId());
                	csoreSecond = this.queryAllDataInsertDao.save(secondhigh);
                	y++;
                }
    			HighScoreInsert high = new HighScoreInsert();
    			high.setPid(csoreSecond.getId());
    			high.setName(String.valueOf((xssfRow.getCell(3)).getStringCellValue()));
    			high.setImageId(String.valueOf((xssfRow.getCell(2)).getStringCellValue()));
    			if(y == 1){
    				high.setClassification(csoreSecond.getClassification()+"-"+i);
    			}else{
    				high.setClassification(csoreSecond.getClassification()+"-"+(i-(y-1)*2));
    				
    			}
    			xssfRow.getCell(5).setCellType(CellType.STRING);
    			high.setCameralat(Double.parseDouble(xssfRow.getCell(5).getStringCellValue()));
    			xssfRow.getCell(4).setCellType(CellType.STRING);
    			high.setCameralon(Double.valueOf((xssfRow.getCell(4)).getStringCellValue()));
    			xssfRow.getCell(6).setCellType(CellType.STRING);
    			high.setCameraHeight(Double.valueOf((xssfRow.getCell(6)).getStringCellValue()));
    			
    			high.setImageUrl(String.valueOf((xssfRow.getCell(8)).getStringCellValue()));
    			high.setTuli(String.valueOf((xssfRow.getCell(10)).getStringCellValue()));
    			high.setTuliUrl(String.valueOf((xssfRow.getCell(11)).getStringCellValue()));
    			xssfRow.getCell(9).setCellType(CellType.STRING);
    			if((xssfRow.getCell(9)).getStringCellValue()==""){
    				high.setIsBigImage(null);
    			}else{
    				high.setIsBigImage(Integer.valueOf((xssfRow.getCell(9)).getStringCellValue())>300?Boolean.parseBoolean("t"):null);
    			}
    			String coordinate = String.valueOf((xssfRow.getCell(7)).getStringCellValue());
    			if(!StringUtils.isEmpty(coordinate)){
    				String[] value = coordinate.split(",");
			        Double minx = Double.parseDouble(value[0]);
			        Double miny = Double.parseDouble(value[1]);
			        Double maxx = Double.parseDouble(value[2]);
			        Double maxy = Double.parseDouble(value[3]);
    				high.setMinx(minx);
    				high.setMixy(miny);
    				high.setMaxx(maxx);
    				high.setMaxy(maxy);
    		    	Geometry dataBoundary = this.getBboxGeometry(minx,miny,maxx,maxy);;
    		        high.setCoordinate(dataBoundary);
    			}
                HighScoreInsert fourHigh =  this.queryAllDataInsertDao.save(high);
                String data = (xssfRow.getCell(12)).getStringCellValue();
                if(!"".equals(data)){
                	data = "{data:"+data+"}";
                    JSONObject panelJsonObject = new JSONObject(data);
                    JSONArray contents = panelJsonObject.getJSONArray("data");
        		    for(int k = 0;k<contents.length();k++){
     	   				JSONObject datas = contents.getJSONObject(k);
    					AssociatedGeojson geojson = new AssociatedGeojson();
    					geojson.setHighScoreId(fourHigh.getId());
    					geojson.setUrl(datas.has("url")?(String)datas.get("url"):"");
    					geojson.setType(datas.has("type")?(String)datas.get("type"):"");
    					geojson.setTime(datas.has("time")?(String)datas.get("time"):"");
    					geojson.setLayers(datas.has("layers")?(String)datas.get("layers"):"");
    					geojson.setCreateTime(new Date());
    					geojson.setTypeLabel(datas.has("typeLabel")?(String)datas.get("typeLabel"):"");
    					geojson.setDescription(datas.has("description")?(String)datas.get("description"):"");
    					this.insertAssiciatedDao.save(geojson);
       			    }
                }
            }
         } catch (Exception e) {
             e.printStackTrace();
             return false;
         }
    	
    	return true;
    }

	@Override
	public List<HighScore> findByNameAndClassifcation1(String name, String classification) {
		return this.queryDataDao.findByNameAndClassification(name, classification);

	}

	@Override
	public int updateById(String layers, String name, String tuliUrl,Integer id) {	
		return this.queryDataDao.updateById(layers,name,tuliUrl,id);
	}

	@Override
	public int updateById1(String layers, String description, String url, Integer id) {
		return this.queryDataDao.updateById1(layers,description,url,id);
	}
	
	@Override
	public boolean idIsExist(Integer id) {
		Optional<HighScore> de = this.queryDataDao.findById(id);
		if (de.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean idIsExist1(Integer id) {
		Optional<AssociatedGeojson> de = this.insertAssiciatedDao.findById(id);
		if (de.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

}
