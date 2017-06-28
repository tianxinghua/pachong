package com.shangpin.pending.product.consumer.supplier.common;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuHandleGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuHandleGateWay;
import com.shangpin.ephub.client.product.business.size.dto.MatchSizeDto;
import com.shangpin.ephub.client.product.business.size.gateway.MatchSizeGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.enumeration.MessageType;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.common.enumeration.SpuStatus;
import com.shangpin.pending.product.consumer.supplier.dto.PendingHeaderSku;
import com.shangpin.pending.product.consumer.supplier.dto.PendingHeaderSpu;
import com.shangpin.pending.product.consumer.supplier.dto.SpuPending;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 16/12/13. 数据从缓存中拉取
 */
@Component
@Slf4j

public class PendingHandler extends VariableInit {

	@Autowired
	HubPendingSpuHandleGateWay hubPendingSpuHandleGateWay;
	@Autowired
	HubPendingSkuHandleGateWay hubPendingSkuHandleGateWay;

	@Autowired
	DataOfPendingServiceHandler dataOfPendingServiceHandler;
	@Autowired
	MatchSizeGateWay matchSizeGateWay;

	public void receiveMsg(PendingProduct message, Map<String, Object> headers) throws Exception {

		if(null!=message){

			log.info("receive message :" + mapper.writeValueAsString(message) + " message header :" + headers.toString());
		} else{
			log.info(" message header :" + headers.toString());
		}


		Map<String, Integer> messageMap = this.getMessageStatus(headers);

		PendingSpu pendingSpu = message.getData();
		SpuPending hubSpuPending = null;
		Integer spuStatus = null;
		List<PendingSku> skus = pendingSpu.getSkus();
		
		HubSpuPendingDto hubSpuPendingDto = null;
		if (messageMap.containsKey(pendingSpu.getSupplierId())) {

			spuStatus = messageMap.get(pendingSpu.getSupplierId());
			// 防止数据传入错误，需要先查询pending表中是否存在
			HubSpuPendingDto tmp = dataServiceHandler.getHubSpuPending(message.getSupplierId(),
					message.getData().getSupplierSpuNo());
			//自动选品
			if (spuStatus == MessageType.RESTART_BRAND_MODEL.getIndex()) {
				hubSpuPendingDto = handSpuPending(message.getData());
			}else if (spuStatus == InfoState.RefreshCategory.getIndex()) {
				//刷新品类
				refreshPendingCategory(message.getData(),tmp);
			}else if (spuStatus == InfoState.RefreshCategory.getIndex()) {
				//刷新颜色
				refreshHubColor(message.getData(),tmp);
			}else if (spuStatus == InfoState.RefreshColor.getIndex()) {
				//刷新尺码
				refreshHubSize(message.getSupplierId(),message.getData(),tmp);
			}else{
				//spu pending 处理
				hubSpuPending = handleSpuPending(message, headers, spuStatus, tmp);
				if(spuStatus== MessageType.RESTART_HANDLE.getIndex()){
					//重处理的不做SKU更新
					return;
				}
			}
		}
		
		if(hubSpuPendingDto!=null){
			log.info("pendingSpu处理返回信息：{}",hubSpuPendingDto);
			if(null!=skus&&skus.size()>0){
	            for (HubSkuPendingDto sku : skus) {
	            	sku.setSpuPendingId(hubSpuPendingDto.getSpuPendingId());
	            	sku.setSupplierNo(hubSpuPendingDto.getSupplierNo());
	            	hubPendingSkuHandleGateWay.handleHubPendingSku(sku);
	            }
	            hubPendingSpuHandleGateWay.updateSpuState(hubSpuPendingDto.getSpuPendingId());
			}
		}else{
			if (null != hubSpuPending) {
				//sku pending 处理
				handleSkuPending(headers, messageMap, pendingSpu, hubSpuPending, spuStatus, skus);

			}
		}
	}
	//刷新品类
		private void refreshPendingCategory(PendingSpu spu,HubSpuPendingDto spuPendingDto) throws Exception{
			
			if(null != spuPendingDto&&checkSpuPendingIsRefresh(spuPendingDto)){
				HubSpuPendingDto updateSpuPending = new HubSpuPendingDto();
				// 获取品类
				pendingCommonHandler.getCategoryMap(spu, updateSpuPending);
				dataServiceHandler.updatePendingSpu(spuPendingDto.getSpuPendingId(), updateSpuPending);
				log.info("===供应商spuPendingId:"+spuPendingDto.getSpuPendingId()+"映射hub品类刷新:"+spuPendingDto.getHubCategoryNo()+"==>"+updateSpuPending.getHubCategoryNo());
			}else{
				saveAndRefreshPending(spu);
			}
		}

	private void refreshHubColor(PendingSpu spuPendingDto, HubSpuPendingDto existSpuPendingDto) throws Exception{
		if(null != existSpuPendingDto&&checkSpuPendingIsRefresh(existSpuPendingDto)){
			HubSpuPendingDto updateSpuPending = new HubSpuPendingDto();
			// 获取品类
			pendingCommonHandler.getColorMap(spuPendingDto, updateSpuPending);
			dataServiceHandler.updatePendingSpu(existSpuPendingDto.getSpuPendingId(), updateSpuPending);
			log.info("===供应商spuPendingId:"+spuPendingDto.getSpuPendingId()+"映射hub颜色刷新:"+spuPendingDto.getHubColor()+"==>"+updateSpuPending.getHubColor());
		}else{
//			saveAndRefreshPending(spuPendingDto);
		}
	}

	private boolean commonSize(String supplierId,PendingSku supplierSku,HubSkuPendingDto updateSkuPending){
		boolean flag = false;
		Map<String, String> sizeMap = dataSverviceUtil.getSupplierSizeMapping(supplierId);
		if (sizeMap.containsKey(supplierSku.getHubSkuSize())) {
			flag = true;
			String spSize = sizeMap.get(supplierSku.getHubSkuSize());
			String spSizeTypeAndSize =   spSize.substring(0,spSize.indexOf(","));
			if(spSizeTypeAndSize.indexOf(":")>=0){
				updateSkuPending.setHubSkuSizeType(spSizeTypeAndSize.substring(0,spSizeTypeAndSize.indexOf(":")));
				updateSkuPending.setHubSkuSize(spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":"),spSizeTypeAndSize.length()));
			} else{
				updateSkuPending.setHubSkuSize(spSizeTypeAndSize);
			}
			updateSkuPending.setScreenSize(spSize.substring(spSize.indexOf(",")+1,spSize.length()));
		}
		return flag;
	}
	
	// 刷新尺码
	private void refreshHubSize(String supplierId,PendingSpu spu, HubSpuPendingDto spuPendingDto) throws Exception {

		if (null != spuPendingDto) {
			List<PendingSku> skuList = spu.getSkus();
			if (skuList != null && skuList.size() > 0) {
				for (PendingSku supplierSku : skuList) {
					HubSkuPendingDto hubSkuPending = dataServiceHandler.getHubSkuPending(supplierSku.getSupplierId(),
							supplierSku.getSupplierSkuNo());
					if (null == hubSkuPending) {
						byte filterFlag = FilterFlag.EFFECTIVE.getIndex();
						SpuPending hubSpuPending = new SpuPending();
						BeanUtils.copyProperties(spuPendingDto, hubSpuPending);
						this.addNewSku(hubSpuPending, spu, supplierSku, null, filterFlag);
					} else {
						if(hubSkuPending.getSkuState()!=null&&(hubSkuPending.getSkuState()==2||hubSkuPending.getSkuState()==5)){
							continue ;
						}
//						boolean flag = false;
						HubSkuPendingDto updateSkuPending = new HubSkuPendingDto();
						supplierSku.setHubSkuSize(StringUtils.deleteWhitespace(supplierSku.getHubSkuSize()));
						if(!commonSize(supplierId, supplierSku, updateSkuPending)){
							if(!commonSize("quanju", supplierSku, updateSkuPending)){
								updateSkuPending.setHubSkuSize(dataSverviceUtil.sizeCommonReplace(supplierSku.getHubSkuSize()));
//								flag = true;
							}
						}
						
//						Map<String, String> sizeMap = dataSverviceUtil.getSupplierSizeMapping(supplierId);
//						if (sizeMap.containsKey(supplierSku.getHubSkuSize())) {
//							String spSize = sizeMap.get(supplierSku.getHubSkuSize());
//							String spSizeTypeAndSize =   spSize.substring(0,spSize.indexOf(","));
//							if(spSizeTypeAndSize.indexOf(":")>=0){
//								updateSkuPending.setHubSkuSizeType(spSizeTypeAndSize.substring(0,spSizeTypeAndSize.indexOf(":")));
//								updateSkuPending.setHubSkuSize(spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":"),spSizeTypeAndSize.length()));
//							} else{
//								updateSkuPending.setHubSkuSize(spSizeTypeAndSize);
//								flag = true;
//							}
//							updateSkuPending.setScreenSize(spSize.substring(spSize.indexOf(",")+1,spSize.length()));
//						} else {
//							
//							updateSkuPending.setHubSkuSize(dataSverviceUtil.sizeCommonReplace(supplierSku.getHubSkuSize()));
//							flag = true;
//						}
						if(true){
							MatchSizeResult matchSizeResult = null;
							if(spuPendingDto.getCatgoryState()!=null&&spuPendingDto.getSpuBrandState()!=null&&spuPendingDto.getCatgoryState()==CatgoryState.PERFECT_MATCHED.getIndex()&&spuPendingDto.getSpuBrandState()==SpuBrandState.HANDLED.getIndex()){
								MatchSizeDto match = new MatchSizeDto();
								match.setHubBrandNo(spuPendingDto.getHubBrandNo());
								match.setHubCategoryNo(spuPendingDto.getHubCategoryNo());
								match.setSize(updateSkuPending.getHubSkuSize());
								matchSizeResult = matchSizeGateWay.matchSize(match);
								if(matchSizeResult!=null&&matchSizeResult.isPassing()){
									updateSkuPending.setHubSkuSizeType(matchSizeResult.getSizeType());
									updateSkuPending.setFilterFlag((byte)1);
									updateSkuPending.setSpSkuSizeState((byte)1);
									updateSkuPending.setSkuState((byte)1);
									updateSkuPending.setScreenSize(matchSizeResult.getSizeId());
								}else{
									updateSkuPending.setFilterFlag((byte)1);
									updateSkuPending.setSpSkuSizeState((byte)0);
									updateSkuPending.setSkuState((byte)0);
									updateSkuPending.setHubSkuSizeType("");
								}
							}
						}
						// 品牌和品类都已匹配上 尺码未匹配上
						updateSkuPending.setSkuPendingId(hubSkuPending.getSkuPendingId());
						dataServiceHandler.updateSkuPengding(updateSkuPending);
						log.info("===供应商spuPendingId:" + updateSkuPending.getSkuPendingId() + "映射hub尺码刷新:"
								+ supplierSku.getHubSkuSize() + "==>" + updateSkuPending.getHubSkuSize());
					}
				}
			}
		} else {
//			saveAndRefreshPendingSku(spu);
		}
	}
	private void saveAndRefreshPending(PendingSpu spu) throws Exception{
			HubSupplierSpuDto supplierSpuDto = dataServiceHandler
					.getHubSupplierSpuBySupplierIdAndSupplierSpuNo(spu.getSupplierId(), spu.getSupplierSpuNo());
			PendingSpu tmp = new PendingSpu();
			this.setValueFromHubSuppierSpuToPendingSpu(supplierSpuDto, tmp);
			tmp.setSupplierNo(spu.getSupplierNo());
			SpuPending newSpuPending = null;
			try {
				newSpuPending = addNewSpu(tmp);
			} catch (Exception e) {
				newSpuPending = new SpuPending();
				setSpuPendingValueWhenDuplicateKeyException(newSpuPending, e, spu.getSupplierId(),
						spu.getSupplierSpuNo());
			}
	}
	private boolean checkSpuPendingIsRefresh(HubSpuPendingDto spuPendingDto){
		
		if (null != spuPendingDto&&(spuPendingDto.getSpuState() != null
				&& (spuPendingDto.getSpuState().intValue() == SpuStatus.SPU_WAIT_HANDLE.getIndex()))) {
			return true;
		} 
		return false;
	}
	private void handleSkuPending(Map<String, Object> headers, Map<String, Integer> messageMap, PendingSpu pendingSpu, SpuPending hubSpuPending, Integer spuStatus, List<PendingSku> skus) throws Exception {
		Integer skuStatus=0;
		if(null!=skus&&skus.size()>0){
            byte filterFlag = FilterFlag.EFFECTIVE.getIndex();
            for (PendingSku sku : skus) {
                if (messageMap.containsKey(sku.getSupplierSkuNo())) {
                    HubSkuPendingDto hubSkuPending = dataServiceHandler.getHubSkuPending(sku.getSupplierId(),
                            sku.getSupplierSkuNo());
                    skuStatus = messageMap.get(sku.getSupplierSkuNo());
                    if (skuStatus == MessageType.NEW.getIndex()) {
                        if (null == hubSkuPending) {
                            this.addNewSku(hubSpuPending, pendingSpu, sku, headers, filterFlag);
                        }
                    } else if (skuStatus == MessageType.UPDATE.getIndex()) {
						if (null == hubSkuPending) {
							//获取hub_supplier_sku
							this.createNewSkuFromSupplier(hubSpuPending, pendingSpu, sku, headers, filterFlag);

						} else{

							this.updateSku(hubSpuPending, sku, headers, filterFlag);
						}

                    } else if (skuStatus == MessageType.MODIFY_PRICE.getIndex()) {

						if (null == hubSkuPending) {
							//获取hub_supplier_sku
							this.createNewSkuFromSupplier(hubSpuPending, pendingSpu, sku, headers, filterFlag);
						} else {
							// TODO 处理自动调整价格    现先处理库存
							dataSverviceUtil.updatePriceOrStock(hubSpuPending, sku);
						}

                    } else{
						if (null == hubSkuPending) {
							//获取hub_supplier_sku
							this.createNewSkuFromSupplier(hubSpuPending, pendingSpu, sku, headers, filterFlag);

						}
					}
                }
            }
        }else{
            if(null!=spuStatus&&(spuStatus == MessageType.NEW.getIndex())){
                //新增的SPU 但没有sku

            }
        }
	}

	private SpuPending handleSpuPending(PendingProduct message, Map<String, Object> headers,Integer spuStatus, HubSpuPendingDto tmp) throws Exception {
		SpuPending hubSpuPending = null;
		if (spuStatus == MessageType.NEW.getIndex()) {
            if (null == tmp) {
                hubSpuPending = createNewSpu(message);
            } else {
                hubSpuPending = new SpuPending();
                BeanUtils.copyProperties(tmp, hubSpuPending);
            }

        } else if (spuStatus == MessageType.UPDATE.getIndex()) {
            hubSpuPending = this.updateSpu(message.getData(), headers);

        }else if (spuStatus == MessageType.RESTART_HANDLE.getIndex()) {
			hubSpuPending = this.updateSpu(message.getData(), headers);

		} else {
            // 不需要处理 已存在
            if (null != tmp) {
                hubSpuPending = new SpuPending();
                BeanUtils.copyProperties(tmp, hubSpuPending);

				//判断HUB_SPU是否存   （现无需再判断，在insert hub_sku_pending里处理）
//				if (hubSpuPending.getSpuBrandState().intValue()==PropertyStatus.MESSAGE_HANDLED.getIndex()
//						&& null != hubSpuPending.getSpuModel()&&PropertyStatus.MESSAGE_HANDLED.getIndex()==hubSpuPending.getSpuModelState().intValue()) {
//					HubSpuDto hubSpuDto = dataServiceHandler.getHubSpuByHubBrandNoAndProductModel(hubSpuPending.getHubBrandNo(),
//							hubSpuPending.getSpuModel());
//					if(null!=hubSpuDto){
//						if(hubSpuPending.getSpuState().intValue()==SpuStatus.SPU_WAIT_HANDLE.getIndex()){
//						   spuPendingHandler.updateSpuStateToHandle(hubSpuPending.getSpuPendingId());
//							hubSpuPending.setSpuState(SpuStatus.SPU_HANDLED.getIndex().byteValue());
//						}
//					}
//				}


            } else {// 如果不存在 说明是消息队列混乱了
				hubSpuPending = createNewSpuFromSupplier(message);
            }



        }
		return hubSpuPending;
	}

	private HubSpuPendingDto handSpuPending(HubSpuPendingDto hubSpuPendingDto) {
		return 	hubPendingSpuHandleGateWay.handleHubPendingSpu(hubSpuPendingDto);
	}

	private SpuPending createNewSpu(PendingProduct message)
			throws Exception {
		SpuPending hubSpuPending = null;
		try {
			message.getData().setSupplierNo(message.getSupplierNo());
			hubSpuPending = this.addNewSpu(message.getData());
		} catch (Exception e) {

			hubSpuPending = new SpuPending();
			setSpuPendingValueWhenDuplicateKeyException(hubSpuPending, e, message.getSupplierId(), message.getData().getSupplierSpuNo());


		}
		return hubSpuPending;
	}


	private SpuPending createNewSpuFromSupplier(PendingProduct message)
			throws Exception {
		HubSupplierSpuDto supplierSpuDto = dataServiceHandler.getHubSupplierSpuBySupplierIdAndSupplierSpuNo(message.getSupplierId(), message.getData().getSupplierSpuNo());
		PendingSpu tmp = new PendingSpu();
		this.setValueFromHubSuppierSpuToPendingSpu(supplierSpuDto,tmp);
		tmp.setSupplierNo(message.getSupplierNo());
		SpuPending hubSpuPending = null;
		try {
			hubSpuPending = addNewSpu(tmp);
		} catch (Exception e) {
			hubSpuPending = new SpuPending();
			setSpuPendingValueWhenDuplicateKeyException(hubSpuPending, e, tmp.getSupplierId(), tmp.getSupplierSpuNo());
		}
		return hubSpuPending;
	}

	private void createNewSkuFromSupplier(SpuPending hubSpuPending, PendingSpu supplierSpu, PendingSku supplierSku,
												Map<String, Object> headers, byte filterFlag)
			throws Exception {
		HubSupplierSkuDto supplierSkuDto = dataServiceHandler.getHubSupplierSkuBySupplierIdAndSupplierSkuNo(supplierSku.getSupplierId(), supplierSku.getSupplierSkuNo());
		objectConvertCommon.convertHubSkuToPendingSku(supplierSkuDto,supplierSku);
		this.addNewSku(hubSpuPending, supplierSpu, supplierSku, headers, filterFlag);

	}



	/**
	 * 获取spu或者sku对应的状态
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	private Map<String, Integer> getMessageStatus(Map<String, Object> headers) throws Exception {

		Map<String, Integer> result = new HashMap<>();
		String key = MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY;
		String value = null;
		if (headers.containsKey(key)) {
			value =  headers.get(key).toString();
			ObjectMapper om = new ObjectMapper();
			PendingHeaderSpu spu = om.readValue(value, PendingHeaderSpu.class);
			if (null != spu) {
				result.put(spu.getSupplierId(), spu.getStatus());
				List<PendingHeaderSku> skus = spu.getSkus();
				if(null!=skus){

					for (PendingHeaderSku sku : skus) {
						result.put(sku.getSkuNo(), sku.getStatus());
					}
				}
			}

		}

		return result;

	}

	public SpuPending addNewSpu(PendingSpu spu) throws Exception {

		// judage in hub_spu by product_code ,if exist ,set value from hub_spu
		// and set spu status value is 1
		String productCode = spu.getSpuModel();
		SpuPending hubSpuPending = new SpuPending();
		HubSpuDto hubSpuDto = null;

		BeanUtils.copyProperties(spu, hubSpuPending);
		boolean brandmapping = false;
		// 首先映射品牌 ，否则无法查询SPU
		brandmapping = setBrandMapping(spu, hubSpuPending);

		// 验证货号
		boolean spuModelJudge = false;
		if (brandmapping) {
			spuModelJudge = setBrandModel(spu, hubSpuPending);
		}

		if (brandmapping && null != spu.getSpuModel()) {
			hubSpuDto = dataServiceHandler.getHubSpuByHubBrandNoAndProductModel(hubSpuPending.getHubBrandNo(),
					hubSpuPending.getSpuModel());
		}

		if (null != hubSpuDto) {
			// 直接复制HUB-SPU里的信息 ，SPU状态 直接为审核通过
			objectConvertCommon.setSpuPropertyFromHubSpu(hubSpuPending, hubSpuDto);
			dataServiceHandler.savePendingSpu(hubSpuPending);
			hubSpuPending.setHubSpuNo(hubSpuDto.getSpuNo());

		} else {

			setSpuProperty(spu, hubSpuPending, brandmapping, spuModelJudge);
			Date date = new Date();
			hubSpuPending.setCreateTime(date);
			hubSpuPending.setUpdateTime(date);
			//过滤设置
			byte filterFlag = screenSupplierBrandAndSeasonEffectiveOrNot(hubSpuPending.getSupplierId(),
					hubSpuPending.getHubBrandNo(), hubSpuPending.getHubSeason());
			hubSpuPending.setFilterFlag(filterFlag);

			dataServiceHandler.savePendingSpu(hubSpuPending);

		}
		return hubSpuPending;

	}



	//各个属性赋值
	private void setSpuProperty(PendingSpu spu, SpuPending hubSpuPending, boolean brandmapping, boolean spuModelJudge) throws Exception {
		boolean allStatus = true;
		if (!brandmapping)
            allStatus = false;
		if (!spuModelJudge)
            allStatus = false;
		// 设置性别
		if (!setGenderMapping(spu, hubSpuPending))
            allStatus = false;

		// 获取品类
		if (!setCategoryMapping(spu, hubSpuPending))
            allStatus = false;

		// 获取颜色
		if (!setColorMapping(spu, hubSpuPending))
            allStatus = false;

		// 获取季节
		if (!setSeasonMapping(spu, hubSpuPending))
            allStatus = false;

		// 获取材质
		if (!replaceMaterial(spu, hubSpuPending))
            allStatus = false;

		// 产地映射
		if (!setOriginMapping(spu, hubSpuPending))
            allStatus = false;

		// 查询是否有图片
		if(!handlePicLink(spu, hubSpuPending)){
            allStatus = false;
        }

		if (allStatus) {
            hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        } else {
            hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
        }
	}











	private SpuPending updateSpu(PendingSpu spu, Map<String, Object> headers) throws Exception {

		HubSpuPendingDto spuPendingDto = null;

		spuPendingDto = dataServiceHandler.getHubSpuPending(spu.getSupplierId(), spu.getSupplierSpuNo());
		HubSpuDto hubSpuDto = null;
		if (null != spuPendingDto) {
			if (spuPendingDto.getSpuState().intValue() == SpuStatus.SPU_WAIT_AUDIT.getIndex()
					|| spuPendingDto.getSpuState().intValue() == SpuStatus.SPU_HANDLING.getIndex()
					|| spuPendingDto.getSpuState().intValue() == SpuStatus.SPU_HANDLED.getIndex()) {
				// 审核中或者已处理,不能做修改
				boolean brandmapping = true;
				// 首先映射品牌 ，否则无法查询SPU
				brandmapping = setBrandMapping(spu, spuPendingDto);

				// 验证货号
				boolean spuModelJudge = true;
				if (brandmapping) {
					spuModelJudge = setBrandModel(spu, spuPendingDto);
				}

				if (brandmapping && null != spu.getSpuModel()) {
					hubSpuDto = dataServiceHandler.getHubSpuByHubBrandNoAndProductModel(spuPendingDto.getHubBrandNo(),
							spuPendingDto.getSpuModel());
				}

			} else {
				HubSpuPendingDto updateSpuPending = new HubSpuPendingDto();

				BeanUtils.copyProperties(spu, updateSpuPending);
				//只有未被人工修改过的才做处理
				if(StringUtils.isBlank(spuPendingDto.getUpdateUser())) {
					setSpuPendingValueForUpdate(spu, spuPendingDto, updateSpuPending);
				}

				dataServiceHandler.updatePendingSpu(spuPendingDto.getSpuPendingId(), updateSpuPending);
				//更新后重新赋值
				spuPendingDto = dataServiceHandler.getSpuPendingById(spuPendingDto.getSpuPendingId());
			}

			SpuPending spuPending = new SpuPending();
			BeanUtils.copyProperties(spuPendingDto, spuPending);
			if (null != hubSpuDto) {
				spuPending.setHubSpuNo(hubSpuDto.getSpuNo());
			}
			return spuPending;
		} else{
			//  if can't find spupending ,  search  supplier and insert spupending
			HubSupplierSpuDto supplierSpuDto = dataServiceHandler.getHubSupplierSpuBySupplierIdAndSupplierSpuNo(spu.getSupplierId(), spu.getSupplierSpuNo());
			PendingSpu tmp = new PendingSpu();
			this.setValueFromHubSuppierSpuToPendingSpu(supplierSpuDto,tmp);
			tmp.setSupplierNo(spu.getSupplierNo());
			SpuPending newSpuPending = null;
			try {
				newSpuPending = addNewSpu(tmp);
			} catch (Exception e) {
				 newSpuPending = new SpuPending();
				 setSpuPendingValueWhenDuplicateKeyException(newSpuPending, e, spu.getSupplierId(), spu.getSupplierSpuNo());
			}
			return newSpuPending;
		}

	}

	/**
	 * 因异常被封装 无法判断 DoubleKey异常 先查询 没有不处理
	 * @param spuPending
	 * @param e
	 * @param supplierId
	 * @param supplierSpuNo
	 * @throws Exception
	 */
	private void setSpuPendingValueWhenDuplicateKeyException(SpuPending spuPending, Exception e, String supplierId, String supplierSpuNo) throws Exception {
		HubSpuPendingDto spuDto = dataServiceHandler.getHubSpuPending(supplierId,
				supplierSpuNo);
		if (null != spuDto) {

			BeanUtils.copyProperties(spuDto, spuPending);

		}else{
			e.printStackTrace();
			throw e;
		}




	}





	private void addNewSku(SpuPending hubSpuPending, PendingSpu supplierSpu, PendingSku supplierSku,
			Map<String, Object> headers, byte filterFlag) throws Exception {

		// 公共属性
		HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
		// 单独属性赋值
		Date  date  = new Date();
		setSkuPendingValue(hubSpuPending, supplierSku, filterFlag, hubSkuPending,date);
		// 尺码映射

		handleSizeReplace(hubSpuPending, supplierSku, hubSkuPending);

		// 品牌和品类都已匹配上 获取尺码

		String hubSize = "";

		if(null!=hubSpuPending.getSpuBrandState()&&null!=hubSpuPending.getCatgoryState()){
			if (hubSpuPending.getSpuBrandState().intValue() == PropertyStatus.MESSAGE_HANDLED.getIndex()
					&& hubSpuPending.getCatgoryState().intValue() == PropertyStatus.MESSAGE_HANDLED.getIndex()) {
				hubSize = this.getHubSize(hubSpuPending.getHubCategoryNo(), hubSpuPending.getHubBrandNo(),
						 hubSkuPending.getHubSkuSize());

			}
		}

	    //判断HUBSPU  是否存在 其它状态 比如过滤的 不在处理的 认为不存在HUB_SPU 即使已经存在HUB_SPU
		if (SpuStatus.SPU_HANDLED.getIndex() == hubSpuPending.getSpuState().intValue()
				||(SpuStatus.SPU_WAIT_HANDLE.getIndex()==hubSpuPending.getSpuState().intValue()
				&&StringUtils.isNotBlank(hubSpuPending.getHubSpuNo()))
				||(SpuStatus.SPU_WAIT_AUDIT.getIndex()==hubSpuPending.getSpuState().intValue()
				&&StringUtils.isNotBlank(hubSpuPending.getHubSpuNo()))
				||(SpuStatus.SPU_HANDLING.getIndex()==hubSpuPending.getSpuState().intValue()
				&&StringUtils.isNotBlank(hubSpuPending.getHubSpuNo()))
				) {
			// 查询HUBSKU
			log.info("hubSpu 存在："+(null==hubSpuPending.getHubSpuNo()?"":hubSpuPending.getHubSpuNo()) + ""+
					" hubSize = " +hubSize + " query parameter: category=" + hubSpuPending.getHubCategoryNo() + "  brandno="+hubSpuPending.getHubBrandNo()
					+ " supplierId=" + supplierSku.getSupplierId() + " hubSize="+hubSkuPending.getHubSkuSize());
			setSkuPendingIfHubSkuExist(hubSpuPending, supplierSpu, supplierSku, hubSkuPending, date, hubSize);

		} else {
			// hubspu 不存在
			if ("".equals(hubSize)) {

				hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
				hubSkuPending.setSkuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
				// 如果是待审核的 因为尺码问题 不能通过(现可部分审核，不修改状态）
//					if (hubSpuPending.getSpuState().intValue() == SpuStatus.SPU_WAIT_AUDIT.getIndex()) {
//						spuPendingHandler.updateSpuStateFromWaitAuditToWaitHandle(hubSpuPending.getSpuPendingId());
//					}

			} else {
				if(hubSize.indexOf(",")>=0){

					setSkuPendingSizePropertyValue(hubSkuPending, hubSize);
				}
			}

			hubSkuPending.setFilterFlag(filterFlag);
			//spu pending stock state handle
			updateSpuStockStateForInsertSku(hubSpuPending, hubSkuPending);
			dataServiceHandler.savePendingSku(hubSkuPending);

		}
        //整体处理SPU的状态
		if(hubSpuPending.getSpuState().intValue() == SpuStatus.SPU_HANDLED.getIndex()){
			spuPendingHandler.updateSpuStateToWaitHandleIfSkuStateHaveWaitHandle(hubSpuPending.getSpuPendingId());
		}

	}

	private void updateSpuStockStateForInsertSku(SpuPending hubSpuPending, HubSkuPendingDto hubSkuPending) {
		if(null!=hubSpuPending.getStockState()){
            if(hubSpuPending.getStockState().toString().equals(String.valueOf(StockState.NOSKU.getIndex()))){
                spuPendingHandler.updateStotckState(hubSpuPending.getSpuPendingId(),hubSkuPending.getStock());
            }else if(hubSpuPending.getStockState().toString().equals(String.valueOf(StockState.NOSTOCK.getIndex()))){
                if(hubSkuPending.getStock()>0){
                    spuPendingHandler.updateStotckState(hubSpuPending.getSpuPendingId(),hubSkuPending.getStock());
                }
            }else{
                //原库存标记为有库存
                if(0==hubSkuPending.getStock()){
                    int totalStock = 0;
                    try {
                        totalStock = dataOfPendingServiceHandler.getStockTotalBySpuPendingId(hubSpuPending.getSpuPendingId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(0==totalStock){
                        spuPendingHandler.updateStotckState(hubSpuPending.getSpuPendingId(),totalStock);
                    }

                }
            }
        }else{//遗漏  第一次插入SKU  应该默认赋值为NOSKU
            spuPendingHandler.updateStotckState(hubSpuPending.getSpuPendingId(),hubSkuPending.getStock());

        }
	}

	private void handleSizeReplace(SpuPending hubSpuPending, PendingSku supplierSku, HubSkuPendingDto hubSkuPending) {
		Map<String, String> sizeMap = dataSverviceUtil.getSupplierSizeMapping(hubSpuPending.getSupplierId());
		if (sizeMap.containsKey(supplierSku.getHubSkuSize())) {
			replaceSize(supplierSku, hubSkuPending, sizeMap);
//			mappingSize = true;
		} else {
			Map<String, String> commonSizeMap = dataSverviceUtil.getSupplierSizeMapping(ConstantProperty.REDIS_EPHUB_SUPPLIER_ALL_SIZE_MAPPING_KEY);
			if (commonSizeMap.containsKey(supplierSku.getHubSkuSize())) {
				replaceSize(supplierSku, hubSkuPending, commonSizeMap);

			}else{
				hubSkuPending.setHubSkuSize(dataSverviceUtil.sizeCommonReplace(supplierSku.getHubSkuSize()).trim());
			}

		}
	}

	private void replaceSize(PendingSku supplierSku, HubSkuPendingDto hubSkuPending, Map<String, String> sizeMap) {
		String spSize = sizeMap.get(supplierSku.getHubSkuSize());
		String spSizeTypeAndSize =   spSize.substring(0,spSize.indexOf(","));
		if(spSizeTypeAndSize.indexOf(":")>=0){

            hubSkuPending.setHubSkuSizeType(spSizeTypeAndSize.substring(0,spSizeTypeAndSize.indexOf(":")));
            hubSkuPending.setHubSkuSize(spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":"),spSizeTypeAndSize.length()));
        } else{
            hubSkuPending.setHubSkuSize(spSizeTypeAndSize);
        }
		hubSkuPending.setScreenSize(spSize.substring(spSize.indexOf(",")+1,spSize.length()));
	}

	private void setSkuPendingIfHubSkuExist(SpuPending hubSpuPending, PendingSpu supplierSpu, PendingSku supplierSku, HubSkuPendingDto hubSkuPending, Date date,  String hubSize) throws Exception {
		if ( StringUtils.isNotBlank(hubSize)) {

			String[] sizeAndIdArray = hubSize.split(",");
			hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

			String spSizeTypeAndSize =   sizeAndIdArray[1];
			hubSkuPending.setHubSkuSizeType(spSizeTypeAndSize.substring(0,spSizeTypeAndSize.indexOf(":")));
			hubSkuPending.setHubSkuSize(spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":")+1,spSizeTypeAndSize.length()));

			hubSkuPending.setScreenSize(sizeAndIdArray[0]);


            hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            hubSkuPending.setSkuState(SpuStatus.SPU_HANDLED.getIndex().byteValue());

            HubSkuDto hubSku = dataServiceHandler.getSkuBySpuNoAndSizeAndSizeType(hubSpuPending.getHubSpuNo(),
                    null==hubSkuPending.getHubSkuSize()?"":hubSkuPending.getHubSkuSize(),
					null==hubSkuPending.getHubSkuSizeType()?"":hubSkuPending.getHubSkuSizeType());
            if (null != hubSku) {// 存在 录入skusupplier对应关系
                // 首先存储skuPending
				hubSkuPending.setSkuState(SpuStatus.SPU_HANDLED.getIndex().byteValue());//SKU状态与SPU 相同
				hubSkuPending.setHubSkuNo(hubSku.getSkuNo());
                dataServiceHandler.savePendingSku(hubSkuPending);
                // 保存对应关系
                dataServiceHandler.saveSkuSupplierMapping(hubSku.getSkuNo(), hubSkuPending, supplierSpu, supplierSku);
            } else { // 不存在 创建hubsku 并创建 对应关系
				//先创建hub_sku 然后反写到SKUPENDING 中

				HubSkuDto hubSkuNo = dataServiceHandler.insertHubSku(hubSpuPending.getHubSpuNo(), hubSpuPending.getHubColor(), date,
                        hubSkuPending);

				hubSkuPending.setHubSkuNo(hubSkuNo.getSkuNo());
				dataServiceHandler.savePendingSku(hubSkuPending);


                dataServiceHandler.saveSkuSupplierMapping(hubSkuNo.getSkuNo(), hubSkuPending, supplierSpu, supplierSku);


            }
        } else {// 无尺码映射
            hubSkuPending.setSkuState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.savePendingSku(hubSkuPending);


        }
	}



	private void updateSku(HubSpuPendingDto hubSpuPending, PendingSku supplierSku, Map<String, Object> headers, byte filterFlag)
			throws Exception {
		// 公共属性
		HubSkuPendingDto originSkuPending =  dataServiceHandler.getHubSkuPending(supplierSku.getSupplierId(),supplierSku.getSupplierSkuNo());
		if(null!=originSkuPending){

			HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
			BeanUtils.copyProperties(supplierSku, hubSkuPending);
			hubSkuPending.setSkuPendingId(originSkuPending.getSkuPendingId());
			Date date = new Date();
			hubSkuPending.setUpdateTime(date);
			hubSkuPending.setFilterFlag(filterFlag);
			if(StringUtils.isNotBlank(supplierSku.getHubSkuSize())){

				if(null!=originSkuPending.getSpSkuSizeState()&&originSkuPending.getSpSkuSizeState().intValue()!=PropertyStatus.MESSAGE_HANDLED.getIndex()){ //已匹配上的尺码不做处理

					setSkuPendingSizeForUpdate(hubSpuPending, supplierSku, hubSkuPending);
				}
			}

			hubSkuPending.setFilterFlag(filterFlag);
			dataServiceHandler.updateSkuPengding(hubSkuPending);
		}

	}

	private void setSkuPendingSizeForUpdate(HubSpuPendingDto hubSpuPending, PendingSku supplierSku, HubSkuPendingDto hubSkuPending) throws IOException {
		Map<String, String> sizeMap = dataSverviceUtil.getSupplierSizeMapping(hubSpuPending.getSupplierId());
		if (sizeMap.containsKey(supplierSku.getHubSkuSize())) {
            // 尺码映射   举例 ：  国际码:32,123
            String spSize = sizeMap.get(supplierSku.getHubSkuSize());

            if(spSize.indexOf(",")>=0){

                String spSizeTypeAndSize =   spSize.substring(0,spSize.indexOf(","));
                if(spSizeTypeAndSize.indexOf(":")>=0){
					hubSkuPending.setHubSkuSizeType(spSizeTypeAndSize.substring(0,spSizeTypeAndSize.indexOf(":")));
					hubSkuPending.setHubSkuSize(spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":")+1,spSizeTypeAndSize.length()));
				}else{
					hubSkuPending.setHubSkuSize(spSizeTypeAndSize);
				}

                hubSkuPending.setScreenSize(spSize.substring(spSize.indexOf(",")+1,spSize.length()));
            }

        } else {
            hubSkuPending.setHubSkuSize(dataSverviceUtil.sizeCommonReplace(supplierSku.getHubSkuSize()));
            // 品牌和品类都已匹配上
            String hubSize = "";
            if (hubSpuPending.getSpuBrandState().intValue() == PropertyStatus.MESSAGE_HANDLED.getIndex()
                    && hubSpuPending.getCatgoryState().intValue() == PropertyStatus.MESSAGE_HANDLED.getIndex()) {
                hubSize = this.getHubSize(hubSpuPending.getHubCategoryNo(), hubSpuPending.getHubBrandNo(),
                        supplierSku.getSupplierId(), hubSkuPending.getHubSkuSize());
            }

            if ("".equals(hubSize)) {
                hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                // 如果是待审核的 因为尺码问题 不能通过
                if (hubSpuPending.getSpuState().intValue() == SpuStatus.SPU_WAIT_AUDIT.getIndex()) {
                    spuPendingHandler.updateSpuStateFromWaitAuditToWaitHandle(hubSpuPending.getSpuPendingId());
                }
            } else {
                if(hubSize.indexOf(",")>=0) {
					setSkuPendingSizePropertyValue(hubSkuPending, hubSize);
                }
            }
        }
	}



}
