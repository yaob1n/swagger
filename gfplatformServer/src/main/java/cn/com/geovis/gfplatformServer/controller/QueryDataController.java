package cn.com.geovis.gfplatformServer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.geovis.gfplatformServer.bean.HighScore;
import cn.com.geovis.gfplatformServer.bean.HighScoreParent;
import cn.com.geovis.gfplatformServer.common.RestError;
import cn.com.geovis.gfplatformServer.service.QueryDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Api(tags = "数据查询")
@RestController
@RequestMapping("api/v1/query")
public class QueryDataController {
	
    @Autowired
    private QueryDataService queryDataServiceImpl;
	@Value("${file.dir}")
	private String fileDir;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "查询数据详情", notes = "查询数据详情")
	@PostMapping(value = "showMessageByCondition")
	public ResponseEntity<HighScore> submitDemand(@ApiParam( value= "分类") @RequestParam(required = false) String classification) {
		try {
			log.info("查询数据详情");
			HighScore high = this.queryDataServiceImpl.getByClassification(classification);
			return ResponseEntity.ok(high);
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "检索数据1", notes = "检索数据1")
	@PostMapping(value = "retrievalData3")
	public ResponseEntity<List<HighScore>> retrievalData3(@ApiParam(value = "name") @RequestParam String name,
			                                               @ApiParam(value = "分类") @RequestParam(required = false) String classification) {
		try {
			log.info("检索数据1");
			List<HighScore> high = this.queryDataServiceImpl.findByNameAndClassifcation1(name,classification);
			return ResponseEntity.ok(high);
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@ApiIgnore
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "检索数据/暂时不在使用", notes = "检索数据/暂时不在使用")
	@PostMapping(value = "retrievalData2")
	public ResponseEntity<List<HighScoreParent>> retrievalData2(@ApiParam(value = "name") @RequestParam String name) {
		try {
			log.info("检索数据");
			List<HighScoreParent> high = this.queryDataServiceImpl.findByName(name);
			return ResponseEntity.ok(high);
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "检索数据", notes = "检索数据")
	@PostMapping(value = "retrievalData")
	public ResponseEntity<List<HighScore>> retrievalData(@ApiParam(value = "name") @RequestParam String name,
			                                               @ApiParam(value = "分类") @RequestParam(required = false) String classification) {
		try {
			log.info("检索数据");
			List<HighScore> high = this.queryDataServiceImpl.findByNameAndClassifcation(name,classification);
			return ResponseEntity.ok(high);
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "读取本地数据，将数据录入到数据库", notes = "读取本地数据，将数据录入到数据库")
	@PostMapping(value = "dataEntry")
	public ResponseEntity<Map> dataEntry(@ApiParam(value = "AreaLayers地址,如:d:\\AreaLayers.json") @RequestParam(required = false) String AreaLayers,
			              @ApiParam(value = "AreaPanel地址,如:d:\\AreaPanel.json") @RequestParam String AreaPanel,
			              @ApiParam(value = "分类") @RequestParam String classification) {
		try {
			 log.info("读取本地数据，将数据录入到数据库");
			 //插入数据库之前，先检测分类是否存在
			 boolean isExist = this.queryDataServiceImpl.isExist(classification);
			 if(isExist){
	      		return ResponseEntity.ok(getPublicBackValue(isExist,"分类已经存在，请重新选择分类"));
	      	 }else{
				 boolean flag = this.queryDataServiceImpl.insertFile(classification,AreaLayers,AreaPanel);
		      	 if(flag){
		      		return ResponseEntity.ok(getPublicBackValue(flag,"成功"));
		      	 }else{
			       	return ResponseEntity.ok(getPublicBackValue(flag,"失败"));
		      	 }
	      	 }
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "读取本地execl文件，将数据录入到数据库", notes = "读取本地execl文件，将数据录入到数据库")
	@PostMapping(value = "execlDataEntry")
	public ResponseEntity<Map> execlDataEntry(@ApiParam(value = "execl地址,如:d:\\数据收集.xlsx") @RequestParam String execlPath,
			                                          @ApiParam(value = "分类") @RequestParam String classification) {
		try {
			log.info("读取本地execl数据，将数据录入到数据库");
    	    if(execlPath.indexOf(".") != -1 ){
        	    String[] suffix = execlPath.split("\\.");
        	    if(!"xlsx".equals(suffix[1]) && !"xls".equals(suffix[1]) ){
        	    	return ResponseEntity.ok(getPublicBackValue(false,"文件名不符合规范，请重新录入"));
        	    }
    	     }else{
    	    	 return ResponseEntity.ok(getPublicBackValue(false,"文件名不符合规范，请重新录入"));
    	     }
			 boolean flag = this.queryDataServiceImpl.insertExeclFile(execlPath,classification);
	      	 if(flag){
	      		return ResponseEntity.ok(getPublicBackValue(flag,"成功"));
	      	 }else{
		       	return ResponseEntity.ok(getPublicBackValue(flag,"失败"));
	      	 }
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据图片url获取图片", notes = "根据图片url获取图片")
	@GetMapping(value = "getImage")
	public void getImage(@ApiParam(value = "图片地址") @RequestParam String url,HttpServletResponse response) {
		FileInputStream inputStream = null;
		OutputStream out = null;
		try {
			log.info("根据图片url获取图片base64");
	        //读取本地图片输入流  
	        inputStream = new FileInputStream(fileDir + File.separator + url);  
	        int i = inputStream.available();  
	        byte[] buff = new byte[i];  //byte数组用于存放图片字节数据  
	        inputStream.read(buff);  
	        response.setContentType("image/*"); //设置发送到客户端的响应内容类型   
	        out = response.getOutputStream();  
	        out.write(buff);  
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (out != null) {  
	            try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
	        }
			if (inputStream != null) {  
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
	        }
		}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据json的url获取json", notes = "根据json的url获取json")
	@GetMapping(value = "getJsonOrGeoJson")
	public void getJsonOrGeoJson(@ApiParam(value = "json地址") @RequestParam String url,HttpServletResponse response) {
			log.info("根据json的url获取json");
		    PrintWriter out = null;  
		    try {  
			    File panelFile = new File(fileDir,url);
			    String panelInput = FileUtils.readFileToString(panelFile, "UTF-8");
			    JSONObject responseJSONObject = JSONObject.fromObject(panelInput);  
			    response.setCharacterEncoding("UTF-8");  
			    response.setContentType("application/json; charset=utf-8");  
		        out = response.getWriter();  
		        out.append(responseJSONObject.toString());  
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    } finally {  
		        if (out != null) {  
		            out.close();  
		        }  
		    }  
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
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "通过id更改高分数据", notes = "通过id更改高分数据")
	@PostMapping(value = "updateById")
	public ResponseEntity<String> updateById(@ApiParam(value = "id") @RequestParam(required = false) Integer id,
			              @ApiParam(value = "layers") @RequestParam String layers,
			              @ApiParam(value = "name") @RequestParam String name,
			              @ApiParam(value = "tuliUrl") @RequestParam String tuliUrl) {
		try {
			 log.info("通过id更改高分数据");
			 //更改数据之前，先检测数据是否存在
			 boolean isExist = this.queryDataServiceImpl.idIsExist(id);
			 if(!isExist){
	      		return ResponseEntity.ok("数据不存在，请重新输入");
	      	 }else{
				 int flag = this.queryDataServiceImpl.updateById(layers,name,tuliUrl,id);
		      	 if(flag!=0){
		      		return ResponseEntity.ok("数据更新成功");
		      	 }else{
			       	return ResponseEntity.ok("数据更新失败");
		      	 }
	      	 }
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "通过id更改关联数据", notes = "通过id更改关联数据")
	@PostMapping(value = "updateById1")
	public ResponseEntity<String> updateById1(@ApiParam(value = "id") @RequestParam(required = false) Integer id,
			              @ApiParam(value = "layers") @RequestParam String layers,
			              @ApiParam(value = "description") @RequestParam String description,
			              @ApiParam(value = "url") @RequestParam String url) {
		try {
			 log.info("通过id更改关联数据");
			 //更改数据之前，先检测数据是否存在
			 boolean isExist = this.queryDataServiceImpl.idIsExist1(id);
			 if(!isExist){
	      		return ResponseEntity.ok("数据不存在，请重新输入");
	      	 }else{
				 int flag = this.queryDataServiceImpl.updateById1(layers,description,url,id);
		      	 if(flag!=0){
		      		return ResponseEntity.ok("成功");
		      	 }else{
			       	return ResponseEntity.ok("失败");
		      	 }
	      	 }
		} catch (Exception e) {
			return new ResponseEntity(RestError.BASEIMAGE_LAYER_DELETE_DADABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
