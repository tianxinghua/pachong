package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/10.
 */
@Getter
@Setter
public class CategorieDTO {
    private String id;
    private String parent_id;
    private String name;
    private String is_active;
    private String position;
    private String level;
    private String children;
    private String created_at;
    private String updated_at;
    private String path;
    private List<String> available_sort_by;
    private String include_in_menu;
}
