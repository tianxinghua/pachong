package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class ColorDTO {
    private List<ColorOptions> options;

    private String is_wysiwyg_enabled;
    private String is_html_allowed_on_front;
    private String used_for_sort_by;
    private String is_filterable;
    private String is_filterable_in_search;
    private String is_used_in_grid;
    private String is_visible_in_grid;
    private String is_filterable_in_grid;
    private Integer position;
    private List<String> apply_to;
    private String is_searchable;
    private String is_visible_in_advanced_search;
    private String is_comparable;
    private String is_used_for_promo_rules;
    private String is_visible_on_front;
    private String used_in_product_listing;
    private String is_visible;
    private String scope;
    private String attribute_id;
    private String attribute_code;
    private String frontend_input;
    private String entity_type_id;
    private String is_required;
    private String is_user_defined;
    private String default_frontend_label;
    private String frontend_labels;
    private String backend_type;
    private String source_model;
    private String default_value;
    private String is_unique;
    private List<String> validation_rules;


}
