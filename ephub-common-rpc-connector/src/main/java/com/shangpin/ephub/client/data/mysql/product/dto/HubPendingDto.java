package com.shangpin.ephub.client.data.mysql.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * Created by lizhongren on 2017/1/6.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubPendingDto implements Serializable {

    private Long hubSpuId;

    private Long hubSpuPendingId;



}
