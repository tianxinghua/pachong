package com.shangpin.ice.ice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import Ice.Communicator;

/**
 * 获取商品分页列表
 * OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
 * SopProductSkuPageQuery query = new SopProductSkuPageQuery(Starttime,Endtime,i,100);
 * SopProductSkuPage products = servant.FindCommodityInfoPage(SupplierID, query);
 *
 * 更新库存
 * OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
 * Boolean result = servant.UpdateStock(SupplierID, obj.SkuNo, obj.InventoryQuantity);
 */
public class IcePrxHelper {

    private Communicator _communicator = null;

    private static volatile IcePrxHelper _instance = null;

    private static volatile Object _lock = new Object();

    private IcePrxHelper(String configFile) {
        try {
            Ice.InitializationData initData = new Ice.InitializationData();
            initData.properties = Ice.Util.createProperties();
            initData.properties.load(configFile);
            _communicator = Ice.Util.initialize(initData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static IcePrxHelper getInstance() {
        if (_instance == null) {
            synchronized (_lock) {
                if (_instance == null) // double-check
                {
                    _instance = new IcePrxHelper("config.client");
                }
            }
        }
        return _instance;
    }

    private static volatile Map<String, Object> prxmap = new HashMap<String, Object>();

    public static <T> T getPrx(Class<T> prx) throws Exception {
        String prx_name = prx.getName();
        String prxHelper_name = prx_name + "Helper";
        if (!prxmap.containsKey(prx_name)) {
            synchronized (_lock) {
                if (!prxmap.containsKey(prx_name)) {
                    Class prxImp = Class.forName(prxHelper_name);
                    Method m = prxImp.getMethod("uncheckedCast", Ice.ObjectPrx.class);
                    String[] name = prx_name.split("\\.");
                    String iceconf_name = name[name.length - 1].replaceAll("Prx", "");
                    Ice.ObjectPrx o_prx = getInstance()._communicator.propertyToProxy(iceconf_name + ".Proxy");
                    T prxIntf = (T) m.invoke(prxImp.newInstance(), o_prx);

                    prxmap.put(prx_name, prxIntf);
                }
            }
        }

        return (T) prxmap.get(prx_name);
    }

}
