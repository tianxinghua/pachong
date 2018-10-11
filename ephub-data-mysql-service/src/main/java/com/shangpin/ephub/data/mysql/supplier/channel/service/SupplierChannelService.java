package com.shangpin.ephub.data.mysql.supplier.channel.service;

import com.shangpin.ephub.data.mysql.supplier.channel.bean.SupplierChannelPic;
import com.shangpin.ephub.data.mysql.supplier.channel.mapper.SupplierChannelPicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Autor yuanxue
 * @date 2018年10月11日 上午11:22:51
 * Description: 供应商渠道相关
 */
@Service
public class SupplierChannelService {

    @Autowired
    private SupplierChannelPicMapper supplierChannelPicMapper;

    public SupplierChannelPic getSupplierChannelPicByMap(Map<String,String> map){
        SupplierChannelPic sc = supplierChannelPicMapper.selectByMap(map);
         return sc;
    }
}
