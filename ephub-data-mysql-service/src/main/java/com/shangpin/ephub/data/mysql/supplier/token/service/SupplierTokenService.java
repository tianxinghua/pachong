package com.shangpin.ephub.data.mysql.supplier.token.service;

import com.shangpin.ephub.data.mysql.supplier.channel.bean.SupplierChannelPic;
import com.shangpin.ephub.data.mysql.supplier.channel.mapper.SupplierChannelPicMapper;
import com.shangpin.ephub.data.mysql.supplier.token.bean.SupplierToken;
import com.shangpin.ephub.data.mysql.supplier.token.mapper.SupplierTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Autor yuanxue
 * @date 2018年10月11日 上午11:22:51
 * Description: 供应商渠道相关
 */
@Service
public class SupplierTokenService {

    @Autowired
    private SupplierTokenMapper supplierTokenMapper;

    public SupplierToken getSupplierTokenBySupplierId(String supplierId){
        Map<String ,String> map = new HashMap<String,String>();
        map.put("supplierId",supplierId);
         return supplierTokenMapper.selectBySupplierId(map);
    }

    public int addSupplierTokenBySupplierId(SupplierToken supplierToken){

        return supplierTokenMapper.insertSelective(supplierToken);
    }

    public int updateSupplierTokenBySupplierId(SupplierToken supplierToken){

        return supplierTokenMapper.updateBySupplierToken(supplierToken);
    }

    public int delSupplierTokenBySupplierId(String supplierId){
        Map<String ,String> map = new HashMap<String,String>();
        map.put("supplierId",supplierId);
        return supplierTokenMapper.deleteByPrimaryKey(map);
    }
}
