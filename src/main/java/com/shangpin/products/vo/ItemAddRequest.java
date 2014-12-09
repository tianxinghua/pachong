package com.shangpin.products.vo;

import java.util.Date;


/**
 * Created by suny on 14-11-26.
 */
public class ItemAddRequest {
    /**
     * 商品数量。取值范围:0-900000000的整数。且需要等于Sku所有数量的和。 拍卖商品中增加拍只能为1，荷兰拍要在[2,500)范围内。
     支持最大值为：900000000
     支持最小值为：0
     */
    private Long num;
    /**
     *商品价格。取值范围:0-100000000;精确到2位小数;单位:元。
     * 如:200.07，表示:200元7分。需要在正确的价格区间内。 拍卖商品对应的起拍价。
     */
    private Double price;
    /**
     *发布类型。可选值:fixed(一口价),auction(拍卖)。B商家不能发布拍卖商品，而且拍卖商品是没有SKU的。
     * 拍卖商品发布时需要附加拍卖商品信息：
     * 拍卖类型(paimai_info.mode，拍卖类型包括三种：增价拍[1]，荷兰拍[2]以及降价拍[3])，商品数量(num)，起拍价(price)，价格幅度(increament)，保证金(paimai_info.deposit)。
     */
    private String type;
    /**
     *新旧程度。可选值：new(新)，second(二手)，unused(闲置)。B商家不能发布二手商品。
     *  如果是二手商品，特定类目下属性里面必填新旧成色属性
     */
    private String stuffStatus;
    /**
     *宝贝标题。不能超过30字符，受违禁词控制。天猫图书管控类目最大允许120字符；
     支持最大长度为：120
     支持的最大列表长度为：120
     */
    private String title;
    /**
     *宝贝描述。字数要大于5个字符，小于25000个字符，受违禁词控制
     支持最大长度为：200000
     支持的最大列表长度为：200000
     */
    private String desc;
    /**
     * 所在省份
     */
    private String locationstate;
    /**
     * 所在城市
     */
    private String locationcity;
    /**
     *商品上传后的状态。可选值:onsale(出售中),instock(仓库中);默认值:onsale
     */
    private String approveStatus;
    /**
     *叶子类目id
     支持最小值为：0
     */
    private Long cid;
    /**
     *商品属性列表。格式:pid:vid;pid:vid。属性的pid调用taobao.itemprops.get取得，属性值的vid用taobao.itempropvalues.get取得vid。
     *如果该类目下面没有属性，可以不用填写。如果有属性，必选属性必填，其他非必选属性可以选择不填写.属性不能超过35对.
     *所有属性加起来包括分割符不能超过549字节，单个属性没有限制.
     * 如果有属性是可输入的话，则用字段input_str填入属性的值
     */
    private String props;
    /**
     *运费承担方式。可选值:seller（卖家承担）,buyer(买家承担);默认值:seller。
     *卖家承担不用设置邮费和postage_id.买家承担的时候，必填邮费和postage_id 如果用户设置了运费模板会优先使用运费模板，否则要同步设置邮费（post_fee,express_fee,ems_fee）
     */
    private String freightPayer;
    /**
     *有效期。可选值:7,14;单位:天;默认值:14
     */
    private int validThru;
    /**
     *是否有发票。可选值:true,false (商城卖家此字段必须为true);默认值:false(无发票)
     */
    private Boolean hasInvoice;
    /**
     *是否有保修。可选值:true,false;默认值:false(不保修)
     */
    private Boolean hasWarranty;
    /**
     * 橱窗推荐。可选值:true,false;默认值:false(不推荐)
     */
    private Boolean hasShowcase;
    /**
     *商品所属的店铺类目列表。按逗号分隔。结构:",cid1,cid2,...,"，如果店铺类目存在二级类目，必须传入子类目cids。
     */
    private String[] sellerCids;
    /**
     *支持会员打折。可选值:true,false;默认值:false(不打折)
     */
    private Boolean hasDiscount;
    /**
     *平邮费用。取值范围:0.01-999.00;精确到2位小数;单位:元。
     *如:5.07，表示:5元7分. 注:post_fee,express_fee,ems_fee需要一起填写
     */
    private Double postFee;
    /**
     *快递费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:15.07，表示:15元7分
     */
    private Double expressFee;
    /**
     *ems费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:25.07，表示:25元7分
     */
    private Double emsFee;
    /**
     * 定时上架时间。(时间格式：yyyy-MM-dd HH:mm:ss)
     */
    private Date listTime;
    /**
     *加价(降价)幅度。如果为0，代表系统代理幅度。对于增价拍和荷兰拍来说是加价幅度，对于降价拍来说是降价幅度。
     */
    private Double increment;
    /**
     *商品主图片。类型:JPG,GIF;最大长度:500K
     支持的文件类型为：gif,jpg,jpeg,png
     支持的最大列表长度为：524288
     支持的文件类型：gif,jpg,jpeg,png
     */
    private Byte[]image;
    /**宝贝所属的运费模板ID。
     * 取值范围：整数且必须是该卖家的运费模板的ID
     * 可通过taobao.delivery.template.get获得当前会话用户的所有邮费模板
     */
    private Long postageId;
    /**
     *商品的积分返点比例
     */
    private Long auctionPoint;
    /**
     *属性值别名。如pid:vid:别名;pid1:vid1:别名1 ，其中：pid是属性id vid是属性值id。总长度不超过800字节
     */
    private String propertyAlias;
    /**
     *用户自行输入的类目属性ID串。结构："pid1,pid2,pid3"，如："20000"（表示品牌）
     */
    private String inputPids;
    /**
     *更新的Sku的属性串，调用taobao.itemprops.get获取类目属性，
     * 如果属性是销售属性，再用taobao.itempropvalues.get取得vid。
     */
    private String skuProperties;
    /**
     *Sku的数量串，结构如：num1,num2,num3 如：2,3
     */
    private String skuQuantities;
    /**
     *Sku的价格串，结构如：10.00,5.00,… t精确到2位小数;单位:元。
     */
    private String skuPrices;
    /**
     *Sku的外部id串，结构如：1234,1342,…
     */
    private String skuOuterIds;
    /**
     *商品文字的字符集。繁体传入"zh_HK"，简体传入"zh_CN"，不传默认为简体
     */
    private String lang;
    /**
     *商品外部编码，该字段的最大长度是64个字节
     */
    private String outerId;
    /**
     *商品所属的产品ID(B商家发布商品需要用)
     */
    private Long productId;
    /**
     *商品主图需要关联的图片空间的相对url。这个url所对应的图片必须要属于当前用户。
     */
    private String picPath;
    /**
     *代充商品类型。在代充商品的类目下，不传表示不标记商品类型（交易搜索中就不能通过标记搜到相关的交易了）。
     * 可选类型： no_mark(不做类型标记) time_card(点卡软件代充) fee_card(话费软件代充)
     */
    private String autoFill;
    /**
     *用户自行输入的子属性名和属性值，结构:"父属性值;一级子属性名;一级子属性值;二级子属性名;自定义输入值,...."
     */
    private String inputStr;
    /**
     *是否在淘宝上显示（如果传FALSE，则在淘宝主站无法显示该商品）
     */
    private Boolean isTaobao;
    /**
     *是否在外店显示
     */
    private Boolean isEx;
    /**
     *是否是3D
     */
    private Boolean is3D;
    /**
     *是否承诺退换货服务!虚拟商品无须设置此项!
     */
    private Boolean sellPromise;
    /**
     *售后说明模板id
     */
    private Long afterSaleId;
    /**
     *此为货到付款运费模板的ID
     */
    private Long codPostageId;
    /**
     * 实物闪电发货
     */
    private Boolean isLightningConsignment;
    /**
     * 商品的重量(商超卖家专用字段)
     */
    private int weight;
    /**
     *商品是否为新品。只有在当前类目开通新品,并且当前用户拥有该类目下发布新品权限时才能设置is_xinpin为true，否则设置true后会返回错误码:isv.invalid-permission:add-xinpin。
     *同时只有一口价全新的宝贝才能设置为新品，否则会返回错误码：isv.invalid-parameter:xinpin。不设置该参数值或设置为false效果一致。
     */
    private Boolean isXinpin;
    /**
     *商品是否支持拍下减库存:1支持;2取消支持(付款减库存);0(默认)不更改 集市卖家默认拍下减库存;
     * 商城卖家默认付款减库存
     */
    private int subStock;
    /**
     *生产许可证号
     */
    private String foodSecurityPrdLicenseNo;
    /**
     *产品标准号
     */
    private String foodSecurityDesignCode;
    /**
     * 厂名
     */
    private String oodSecurityFactory;
    /**
     * 厂址
     */
    private String foodSecurityFactorySite;
    /**
     *厂家联系方式
     */
    private String foodSecurityContact;
    /**
     *配料表
     */
    private String foodSecurityMix;
    /**
     *储藏方法
     */
    private String foodSecurityPlanStorage;
    /**
     *保质期
     */
    private String oodSecurityPeriod;
    /**
     *食品添加剂
     */
    private String foodSecurityFoodAdditive;
    /**
     *供货商
     */
    private String foodSecuritySupplier;
    /**
     *生产开始日期，结束日期.格式必须为yyyy-MM-dd
     */
    private String foodSecurityProductDateStart;
    private String foodSecurityProductDateEnd;
    /**
     *进货开始日期，进货结束日期,要在生产日期之后，格式必须为yyyy-MM-dd
     */
    private String foodSecurityStockDateStart;
    private String foodSecurityStockDateEnd;
    /**
     *景区门票类宝贝发布时候，当卖家签订了支付宝代扣协议时候，需要选择支付方式：全额支付和订金支付。
     * 当scenic_ticket_pay_way为1时表示全额支付，为2时表示订金支付
     */
    private int scenicTicketPayWay;
    /**
     *景区门票在选择订金支付时候，需要交的预订费。传入的值是1到20之间的数值，小数点后最多可以保留两位（多余的部分将做四舍五入的处理）。
     * 这个数值表示的是预订费的比例，最终的预订费为 scenic_ticket_book_cost乘一口价除以100
     */
    private String scenicTicketBookCost;
    /**
     *表示商品的体积，如果需要使用按体积计费的运费模板，一定要设置这个值。该值的单位为立方米（m3），如果是其他单位，请转换成成立方米。
     *该值支持两种格式的设置：格式1：bulk:3,单位为立方米(m3),表示直接设置为商品的体积。
     *格式2：length:10;breadth:10;height:10，单位为米（m）。体积和长宽高都支持小数类型。
     */
    private String itemSize;
    /**
     *商品的重量，用于按重量计费的运费模板。注意：单位为kg。 只能传入数值类型（包含小数），不能带单位，单位默认为kg。
     */
    private String itemWeight;
    /**
     *商品基础色，数据格式为：pid:vid:rvid1,rvid2,rvid3;pid:vid:rvid1;
     */
    private String changeProp;
    /**
     *商品卖点信息，最长150个字符。天猫商家和集市卖家都可用。
     */
    private String sellPoint;
    /**
     *	商品描述模块化，模块列表
     */
    private String descModules;
    /**
     *健字号，保健品/膳食营养补充剂 这个类目下特有的信息，此类目下无需填写生产许可证编号（QS），
     * 如果填写了生产许可证编号（QS）将被忽略不保存；保存宝贝时，标题前会自动加上健字号产品名称一起作为宝贝标题
     */
    private String foodSecurityHealthProductNo;
    /**
     *是否是线下商品。 1：线上商品（默认值）； 2：线上或线下商品； 3：线下商品。
     */
    private String isOffline;
    /**
     *商品条形码
     */
    private String barcode;
    /**
     *sku层面的条形码，多个SKU情况，与SKU价格库存格式类似，用逗号分隔
     */
    private String skuBarcode;
    /**
     *该宝贝是否支持【7天无理由退货】，卖家选择的值只是一个因素，最终以类目和选择的属性条件来确定是否支持7天。
     * 填入字符0，表示不支持；未填写或填人字符1，表示支持7天无理由退货；
     */
    private String newprepay;
    /**
     *家装建材类目，商品SKU的长度，正整数，单位为cm，部分类目必选。天猫商家专用。 数据和SKU一一对应，用,分隔
     */
    private String skuHdLength;
    /**
     *家装建材类目，商品SKU的高度，单位为cm，部分类目必选。天猫商家专用。
     */
    private String skuHdHeight;
    /**
     *家装建材类目，商品SKU的灯头数量，正整数，大于等于3，部分类目必选。天猫商家专用。 数据和SKU一一对应，用,分隔
     */
    private String skuHdLampQuantity;
    /**
     *商品资质信息
     */
    private String qualification;
    /**
     *汽车O2O绑定线下服务标记，如不为空，表示关联服务，否则，不关联服务。
     */
    private Boolean o2oBindService;
    /**
     *发布电子凭证宝贝时候表示是否使用邮寄
     * 0: 代表不使用邮寄； 1：代表使用邮寄；如果不设置这个值，代表不使用邮寄
     */
    private String localityLifeChooseLogis;
    /**
     *本地生活电子交易凭证业务，目前此字段只涉及到的信息为有效期;
     */
    private String localityLifeExpiryDate;
    /**
     *网店ID
     */
    private String localityLifeNetworkId;
    /**
     *码商信息，格式为 码商id:nick
     */
    private String localityLifeMerchant;
    /**
     *核销打款 1代表核销打款 0代表非核销打款
     */
    private String localityLifeVerification;
    /**
     *退款比例， 百分比%前的数字,1-100的正整数值
     */
    private int localityLifeRefundRatio;
    /**
     *电子凭证售中自动退款比例，百分比%前的数字，介于1-100之间的整数
     */
    private int localityLifeOnsaleAutoRefundRatio;
    /**
     *退款码费承担方。发布电子凭证宝贝的时候会增加“退款码费承担方”配置项，
     *可选填：(1)s（卖家承担） (2)b(买家承担)
     */
    private String localityLifeRefundmafee;
    /**
     *拍卖商品选择的拍卖类型，拍卖类型包括三种：增价拍(1)，荷兰拍(2)和降价拍(3)。
     支持最大值为：3
     支持最小值为：1
     */
    private int paimaiInfoMode;
    /**
     *拍卖宝贝的保证金
     */
    private int paimaiInfoDeposit;
    /**
     *降价拍宝贝的降价周期(分钟)。降价拍宝贝的价格每隔paimai_info.interval时间会下降一次increment。
     支持最大值为：60
     支持最小值为：1
     */
    private int paimaiInfoInterval;
    /**
     *降价拍宝贝的保留价
     */
    private int paimaiInfoReserve;
    /**
     *自定义销售周期的小时数。拍卖宝贝可以自定义销售周期，这里指定销售周期的小时数。
     */
    private double paimaiInfoValidHour;
    /**
     *自定义销售周期的分钟数。拍卖宝贝可以自定义销售周期，这里是指定销售周期的分钟数
     */
    private int paimaiInfoValidMinute;
    /**
     *全球购商品采购地（库存类型）， 有两种库存类型：现货和代购 参数值为1时代表现货，值为2时代表代购。
     *注意：使用时请与 全球购商品采购地（地区/国家）配合使用
     */
    private String globalStockType;
    /**
     *全球购商品采购地（地区/国家）,默认值只在全球购商品采购地（库存类型选择情况生效）
     */
    private String globalStockCountry;

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStuffStatus() {
        return stuffStatus;
    }

    public void setStuffStatus(String stuffStatus) {
        this.stuffStatus = stuffStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocationstate() {
        return locationstate;
    }

    public void setLocationstate(String locationstate) {
        this.locationstate = locationstate;
    }

    public String getLocationcity() {
        return locationcity;
    }

    public void setLocationcity(String locationcity) {
        this.locationcity = locationcity;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getFreightPayer() {
        return freightPayer;
    }

    public void setFreightPayer(String freightPayer) {
        this.freightPayer = freightPayer;
    }

    public int getValidThru() {
        return validThru;
    }

    public void setValidThru(int validThru) {
        this.validThru = validThru;
    }

    public Boolean getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(Boolean hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public Boolean getHasWarranty() {
        return hasWarranty;
    }

    public void setHasWarranty(Boolean hasWarranty) {
        this.hasWarranty = hasWarranty;
    }

    public Boolean getHasShowcase() {
        return hasShowcase;
    }

    public void setHasShowcase(Boolean hasShowcase) {
        this.hasShowcase = hasShowcase;
    }

    public String[] getSellerCids() {
        return sellerCids;
    }

    public void setSellerCids(String[] sellerCids) {
        this.sellerCids = sellerCids;
    }

    public Boolean getHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(Boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public Double getPostFee() {
        return postFee;
    }

    public void setPostFee(Double postFee) {
        this.postFee = postFee;
    }

    public Double getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(Double expressFee) {
        this.expressFee = expressFee;
    }

    public Double getEmsFee() {
        return emsFee;
    }

    public void setEmsFee(Double emsFee) {
        this.emsFee = emsFee;
    }

    public Date getListTime() {
        return listTime;
    }

    public void setListTime(Date listTime) {
        this.listTime = listTime;
    }

    public Double getIncrement() {
        return increment;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public Long getPostageId() {
        return postageId;
    }

    public void setPostageId(Long postageId) {
        this.postageId = postageId;
    }

    public Long getAuctionPoint() {
        return auctionPoint;
    }

    public void setAuctionPoint(Long auctionPoint) {
        this.auctionPoint = auctionPoint;
    }

    public String getPropertyAlias() {
        return propertyAlias;
    }

    public void setPropertyAlias(String propertyAlias) {
        this.propertyAlias = propertyAlias;
    }

    public String getInputPids() {
        return inputPids;
    }

    public void setInputPids(String inputPids) {
        this.inputPids = inputPids;
    }

    public String getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(String skuProperties) {
        this.skuProperties = skuProperties;
    }

    public String getSkuQuantities() {
        return skuQuantities;
    }

    public void setSkuQuantities(String skuQuantities) {
        this.skuQuantities = skuQuantities;
    }

    public String getSkuPrices() {
        return skuPrices;
    }

    public void setSkuPrices(String skuPrices) {
        this.skuPrices = skuPrices;
    }

    public String getSkuOuterIds() {
        return skuOuterIds;
    }

    public void setSkuOuterIds(String skuOuterIds) {
        this.skuOuterIds = skuOuterIds;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getAutoFill() {
        return autoFill;
    }

    public void setAutoFill(String autoFill) {
        this.autoFill = autoFill;
    }

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public Boolean getIsTaobao() {
        return isTaobao;
    }

    public void setIsTaobao(Boolean isTaobao) {
        this.isTaobao = isTaobao;
    }

    public Boolean getIsEx() {
        return isEx;
    }

    public void setIsEx(Boolean isEx) {
        this.isEx = isEx;
    }

    public Boolean getIs3D() {
        return is3D;
    }

    public void setIs3D(Boolean is3D) {
        this.is3D = is3D;
    }

    public Boolean getSellPromise() {
        return sellPromise;
    }

    public void setSellPromise(Boolean sellPromise) {
        this.sellPromise = sellPromise;
    }

    public Long getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(Long afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public Long getCodPostageId() {
        return codPostageId;
    }

    public void setCodPostageId(Long codPostageId) {
        this.codPostageId = codPostageId;
    }

    public Boolean getIsLightningConsignment() {
        return isLightningConsignment;
    }

    public void setIsLightningConsignment(Boolean isLightningConsignment) {
        this.isLightningConsignment = isLightningConsignment;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Boolean getIsXinpin() {
        return isXinpin;
    }

    public void setIsXinpin(Boolean isXinpin) {
        this.isXinpin = isXinpin;
    }

    public int getSubStock() {
        return subStock;
    }

    public void setSubStock(int subStock) {
        this.subStock = subStock;
    }

    public String getFoodSecurityPrdLicenseNo() {
        return foodSecurityPrdLicenseNo;
    }

    public void setFoodSecurityPrdLicenseNo(String foodSecurityPrdLicenseNo) {
        this.foodSecurityPrdLicenseNo = foodSecurityPrdLicenseNo;
    }

    public String getFoodSecurityDesignCode() {
        return foodSecurityDesignCode;
    }

    public void setFoodSecurityDesignCode(String foodSecurityDesignCode) {
        this.foodSecurityDesignCode = foodSecurityDesignCode;
    }

    public String getOodSecurityFactory() {
        return oodSecurityFactory;
    }

    public void setOodSecurityFactory(String oodSecurityFactory) {
        this.oodSecurityFactory = oodSecurityFactory;
    }

    public String getFoodSecurityFactorySite() {
        return foodSecurityFactorySite;
    }

    public void setFoodSecurityFactorySite(String foodSecurityFactorySite) {
        this.foodSecurityFactorySite = foodSecurityFactorySite;
    }

    public String getFoodSecurityContact() {
        return foodSecurityContact;
    }

    public void setFoodSecurityContact(String foodSecurityContact) {
        this.foodSecurityContact = foodSecurityContact;
    }

    public String getFoodSecurityMix() {
        return foodSecurityMix;
    }

    public void setFoodSecurityMix(String foodSecurityMix) {
        this.foodSecurityMix = foodSecurityMix;
    }

    public String getFoodSecurityPlanStorage() {
        return foodSecurityPlanStorage;
    }

    public void setFoodSecurityPlanStorage(String foodSecurityPlanStorage) {
        this.foodSecurityPlanStorage = foodSecurityPlanStorage;
    }

    public String getOodSecurityPeriod() {
        return oodSecurityPeriod;
    }

    public void setOodSecurityPeriod(String oodSecurityPeriod) {
        this.oodSecurityPeriod = oodSecurityPeriod;
    }

    public String getFoodSecurityFoodAdditive() {
        return foodSecurityFoodAdditive;
    }

    public void setFoodSecurityFoodAdditive(String foodSecurityFoodAdditive) {
        this.foodSecurityFoodAdditive = foodSecurityFoodAdditive;
    }

    public String getFoodSecuritySupplier() {
        return foodSecuritySupplier;
    }

    public void setFoodSecuritySupplier(String foodSecuritySupplier) {
        this.foodSecuritySupplier = foodSecuritySupplier;
    }

    public String getFoodSecurityProductDateStart() {
        return foodSecurityProductDateStart;
    }

    public void setFoodSecurityProductDateStart(String foodSecurityProductDateStart) {
        this.foodSecurityProductDateStart = foodSecurityProductDateStart;
    }

    public String getFoodSecurityProductDateEnd() {
        return foodSecurityProductDateEnd;
    }

    public void setFoodSecurityProductDateEnd(String foodSecurityProductDateEnd) {
        this.foodSecurityProductDateEnd = foodSecurityProductDateEnd;
    }

    public String getFoodSecurityStockDateStart() {
        return foodSecurityStockDateStart;
    }

    public void setFoodSecurityStockDateStart(String foodSecurityStockDateStart) {
        this.foodSecurityStockDateStart = foodSecurityStockDateStart;
    }

    public String getFoodSecurityStockDateEnd() {
        return foodSecurityStockDateEnd;
    }

    public void setFoodSecurityStockDateEnd(String foodSecurityStockDateEnd) {
        this.foodSecurityStockDateEnd = foodSecurityStockDateEnd;
    }

    public int getScenicTicketPayWay() {
        return scenicTicketPayWay;
    }

    public void setScenicTicketPayWay(int scenicTicketPayWay) {
        this.scenicTicketPayWay = scenicTicketPayWay;
    }

    public String getScenicTicketBookCost() {
        return scenicTicketBookCost;
    }

    public void setScenicTicketBookCost(String scenicTicketBookCost) {
        this.scenicTicketBookCost = scenicTicketBookCost;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(String itemWeight) {
        this.itemWeight = itemWeight;
    }

    public String getChangeProp() {
        return changeProp;
    }

    public void setChangeProp(String changeProp) {
        this.changeProp = changeProp;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public String getDescModules() {
        return descModules;
    }

    public void setDescModules(String descModules) {
        this.descModules = descModules;
    }

    public String getFoodSecurityHealthProductNo() {
        return foodSecurityHealthProductNo;
    }

    public void setFoodSecurityHealthProductNo(String foodSecurityHealthProductNo) {
        this.foodSecurityHealthProductNo = foodSecurityHealthProductNo;
    }

    public String getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(String isOffline) {
        this.isOffline = isOffline;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public void setSkuBarcode(String skuBarcode) {
        this.skuBarcode = skuBarcode;
    }

    public String getNewprepay() {
        return newprepay;
    }

    public void setNewprepay(String newprepay) {
        this.newprepay = newprepay;
    }

    public String getSkuHdLength() {
        return skuHdLength;
    }

    public void setSkuHdLength(String skuHdLength) {
        this.skuHdLength = skuHdLength;
    }

    public String getSkuHdHeight() {
        return skuHdHeight;
    }

    public void setSkuHdHeight(String skuHdHeight) {
        this.skuHdHeight = skuHdHeight;
    }

    public String getSkuHdLampQuantity() {
        return skuHdLampQuantity;
    }

    public void setSkuHdLampQuantity(String skuHdLampQuantity) {
        this.skuHdLampQuantity = skuHdLampQuantity;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Boolean getO2oBindService() {
        return o2oBindService;
    }

    public void setO2oBindService(Boolean o2oBindService) {
        this.o2oBindService = o2oBindService;
    }

    public String getLocalityLifeChooseLogis() {
        return localityLifeChooseLogis;
    }

    public void setLocalityLifeChooseLogis(String localityLifeChooseLogis) {
        this.localityLifeChooseLogis = localityLifeChooseLogis;
    }

    public String getLocalityLifeExpiryDate() {
        return localityLifeExpiryDate;
    }

    public void setLocalityLifeExpiryDate(String localityLifeExpiryDate) {
        this.localityLifeExpiryDate = localityLifeExpiryDate;
    }

    public String getLocalityLifeNetworkId() {
        return localityLifeNetworkId;
    }

    public void setLocalityLifeNetworkId(String localityLifeNetworkId) {
        this.localityLifeNetworkId = localityLifeNetworkId;
    }

    public String getLocalityLifeMerchant() {
        return localityLifeMerchant;
    }

    public void setLocalityLifeMerchant(String localityLifeMerchant) {
        this.localityLifeMerchant = localityLifeMerchant;
    }

    public String getLocalityLifeVerification() {
        return localityLifeVerification;
    }

    public void setLocalityLifeVerification(String localityLifeVerification) {
        this.localityLifeVerification = localityLifeVerification;
    }

    public int getLocalityLifeRefundRatio() {
        return localityLifeRefundRatio;
    }

    public void setLocalityLifeRefundRatio(int localityLifeRefundRatio) {
        this.localityLifeRefundRatio = localityLifeRefundRatio;
    }

    public int getLocalityLifeOnsaleAutoRefundRatio() {
        return localityLifeOnsaleAutoRefundRatio;
    }

    public void setLocalityLifeOnsaleAutoRefundRatio(int localityLifeOnsaleAutoRefundRatio) {
        this.localityLifeOnsaleAutoRefundRatio = localityLifeOnsaleAutoRefundRatio;
    }

    public String getLocalityLifeRefundmafee() {
        return localityLifeRefundmafee;
    }

    public void setLocalityLifeRefundmafee(String localityLifeRefundmafee) {
        this.localityLifeRefundmafee = localityLifeRefundmafee;
    }

    public int getPaimaiInfoMode() { return paimaiInfoMode; }

    public void setPaimaiInfoMode(int paimaiInfoMode) {
        this.paimaiInfoMode = paimaiInfoMode;
    }

    public int getPaimaiInfoDeposit() {
        return paimaiInfoDeposit;
    }

    public void setPaimaiInfoDeposit(int paimaiInfoDeposit) {
        this.paimaiInfoDeposit = paimaiInfoDeposit;
    }

    public int getPaimaiInfoInterval() {
        return paimaiInfoInterval;
    }

    public void setPaimaiInfoInterval(int paimaiInfoInterval) {
        this.paimaiInfoInterval = paimaiInfoInterval;
    }

    public int getPaimaiInfoReserve() {
        return paimaiInfoReserve;
    }

    public void setPaimaiInfoReserve(int paimaiInfoReserve) {
        this.paimaiInfoReserve = paimaiInfoReserve;
    }

    public double getPaimaiInfoValidHour() {
        return paimaiInfoValidHour;
    }

    public void setPaimaiInfoValidHour(double paimaiInfoValidHour) {
        this.paimaiInfoValidHour = paimaiInfoValidHour;
    }

    public int getPaimaiInfoValidMinute() {
        return paimaiInfoValidMinute;
    }

    public void setPaimaiInfoValidMinute(int paimaiInfoValidMinute) {
        this.paimaiInfoValidMinute = paimaiInfoValidMinute;
    }

    public String getGlobalStockType() {
        return globalStockType;
    }

    public void setGlobalStockType(String globalStockType) {
        this.globalStockType = globalStockType;
    }

    public String getGlobalStockCountry() {
        return globalStockCountry;
    }

    public void setGlobalStockCountry(String globalStockCountry) {
        this.globalStockCountry = globalStockCountry;
    }
}

