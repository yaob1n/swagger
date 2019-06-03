package cn.com.geovis.gfplatformServer.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 巩志远
 * @create 2018-04-24 09:03:45.
 */
@Getter
@Setter
@ApiModel
public class PageRestResponse<T> {
    private long total;
    private int pageCount;
    private int currentPage;

	@ApiModelProperty(value = "响应集合")
    private List<T> items;

    public static PageRestResponse formatByPageDate(Page page){
        PageRestResponse result = new PageRestResponse<>();
        result.setPageCount(page.getTotalPages());
        result.setTotal(page.getTotalElements());
        result.setCurrentPage(page.getNumber());
        result.setItems(page.getContent());
        return result;
    }


}
