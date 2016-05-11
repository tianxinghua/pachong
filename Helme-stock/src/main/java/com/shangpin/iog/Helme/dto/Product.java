package com.shangpin.iog.Helme.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

	String Tipologia ; //品类
	String GruppoMarchio ;//品牌
	String Modello ;//货号
	String Genere ;//性别
	String Barcode ;//条形码
	String Colore ;//颜色
	String Taglia ;//尺寸
	String Materiale ;//材料
	String HttpFoto1 ;//照片链接
	String HttpFoto2 ;
	String HttpFoto3 ;
	String HttpFoto4 ;
	String HttpFoto5 ;
	String Giacenza ;//库存
	String Prezzoven ;//价格
	String Stagione ;//季节
	String ProductOrigin ;

}
