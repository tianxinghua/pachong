package com.shangpin.supplier.product.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * <p>Title:OriginalProductSink.java </p>
 * <p>Description:  供货商原始商品数据流汇聚点配置接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 下午4:06:53
 */
public interface OriginalProductSink {
	
	public static final String SPINNAKER = "spinnakerOriginalProduct";
	
	public static final String OSTORE = "ostoreOriginalProduct";
	
	public static final String BRUNAROSSO = "brunarossoOriginalProduct";
	
	public static final String STEFANIA = "stefaniaOriginalProduct";
	
	public static final String GEB = "gebOriginalProduct";
	
	public static final String COLTORTI = "coltortiOriginalProduct";
	
	public static final String TONY = "tonyOriginalProduct";
	
	public static final String BIONDIONI = "biondioniOriginalProduct";
	//===============================
	public static final String POZZILEI = "pozzileiOriginalProduct";
	
	public static final String POZZILEIARTE = "pozzileiarteOriginalProduct";
	
	public static final String POZZILEIFORTE = "pozzileiforteOriginalProduct";
	
	public static final String CAROFIGLIO = "carofiglioOriginalProduct";
	
	public static final String GENTEROMA = "genteromaOriginalProduct";
	
	public static final String DANIELLO = "danielloOriginalProduct";
	
	public static final String ITALIANI = "italianiOriginalProduct";
	
	public static final String TUFANO = "tufanoOriginalProduct";
	
	public static final String MONNIERFRERES = "monnierfreresOriginalProduct"; 
	
	public static final String ELEONORABONUCCI = "eleonorabonucciOriginalProduct";	 
	
	public static final String RUSSOCAPRI = "russocapriOriginalProduct"; 
	
	public static final String GIGLIO = "giglioOriginalProduct"; 
	
	public static final String DIVO = "divoOriginalProduct";	 
	
	public static final String BIONDINI = "biondiniOriginalProduct";
	
	public static final String DELLOGLIOSTORE = "dellogliostoreOriginalProduct";
	
	public static final String FRANCESCOMASSA = "francescomassaOriginalProduct";
	
	public static final String TESSABIT = "tessabitOriginalProduct";
	
	public static final String ALDUCADAOSTA = "alducadaostaOriginalProduct"; 
	
	public static final String SANREMO = "sanremoOriginalProduct";	 
	
	public static final String PAOLOFIORILLO = "paolofiorilloOriginalProduct"; 
	
	public static final String VIETTI = "viettiOriginalProduct"; 
	
	public static final String ANIELLO = "anielloOriginalProduct"; 
	
	public static final String LINDELEPALAIS = "lindelepalaisOriginalProduct"; 
	
	public static final String PRITELLI = "pritelliOriginalProduct"; 
	
	public static final String PICCADILLY = "piccadillyOriginalProduct"; 
	
	public static final String VELA = "velaOriginalProduct";	 
	
	public static final String MONTI = "montiOriginalProduct"; 
	
	public static final String CREATIVE99 = "creative99OriginalProduct"; 
	
	public static final String LEAM = "leamOriginalProduct"; 
	
	public static final String BAGHEERA = "bagheeraOriginalProduct"; 
	
	public static final String PAPINI = "papiniOriginalProduct";	 
	
	public static final String ZITAFABIANI = "zitafabianiOriginalProduct"; 
	
	public static final String WISE = "wiseOriginalProduct"; 
	
	public static final String BASEBLU = "basebluOriginalProduct"; 
	
	public static final String RAFFAELLONETWORK = "raffaellonetworkOriginalProduct"; 
	
	public static final String FRMODA = "frmodaOriginalProduct"; 
	
	public static final String STUDIO69 = "studio69OriginalProduct"; 
	
	public static final String DELIBERTI = "delibertiOriginalProduct"; 
	
	public static final String SMETS = "smetsOriginalProduct";
	
	public static final String SARENZA = "sarenzaOriginalProduct";
	
	public static final String SOLEPLACENEW = "soleplacenewOriginalProduct"; 
	
	public static final String HONESTDOUSA = "honestdousaOriginalProduct";	 
	
	public static final String OPTICAL = "opticalOriginalProduct";	 
	
	public static final String TIZIANAFAUSTI = "tizianafaustiOriginalProduct"; 	 
	
	public static final String THECLUTCHER = "theclutcherOriginalProduct"; 
	//===============================
	/**
	 * 供货商SPINNAKER通道组件配置
	 * @return SPINNAKER通道组件
	 */
	@Input(value = OriginalProductSink.SPINNAKER)
    public SubscribableChannel spinnakerOriginalProduct();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return OSTORE通道组件
	 */
	@Input(value = OriginalProductSink.OSTORE)
    public SubscribableChannel ostoreOriginalProduct();
	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return BRUNAROSSO通道组件
	 */
	@Input(value = OriginalProductSink.BRUNAROSSO)
    public SubscribableChannel brunarossoOriginalProduct();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return STEFANIA通道组件
	 */
	@Input(value = OriginalProductSink.STEFANIA)
    public SubscribableChannel stefaniaOriginalProduct();
	/**
	 * 供货商GEB通道组件配置
	 * @return GEB通道组件
	 */
	@Input(value = OriginalProductSink.GEB)
    public SubscribableChannel gebOriginalProduct();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return COLTORTI通道组件
	 */
	@Input(value = OriginalProductSink.COLTORTI)
    public SubscribableChannel coltortiOriginalProduct();
	/**
	 * 供货商TONY通道组件配置
	 * @return TONY通道组件
	 */
	@Input(value = OriginalProductSink.TONY)
    public SubscribableChannel tonyOriginalProduct();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return BIONDIONI通道组件
	 */
	@Input(value = OriginalProductSink.BIONDIONI)
    public SubscribableChannel biondioniOriginalProduct();
	/**
	 * 供货商 通道组件配置
	 * @return  通道组件
	 */
	@Input(value = OriginalProductSink.POZZILEI)
    public SubscribableChannel pozzileiOriginalProduct();
	/**
	 * 供货商 通道组件配置
	 * @return  通道组件
	 */
	@Input(value = OriginalProductSink.POZZILEIARTE)
    public SubscribableChannel pozzileiarteOriginalProduct();
	/**
	 * 供货商pozzileiforte 通道组件配置
	 * @return  pozzileiforte通道组件
	 */
	@Input(value = OriginalProductSink.POZZILEIFORTE)
    public SubscribableChannel pozzileiforteOriginalProduct();
	/**
	 * 供货商 CAROFIGLIO通道组件配置
	 * @return  CAROFIGLIO通道组件
	 */
	@Input(value = OriginalProductSink.CAROFIGLIO)
    public SubscribableChannel carofiglioOriginalProduct();
	/**
	 * 供货商GENTEROMA 通道组件配置
	 * @return  GENTEROMA通道组件
	 */
	@Input(value = OriginalProductSink.GENTEROMA)
    public SubscribableChannel genteromaOriginalProduct();
	/**
	 * 供货商 DANIELLO通道组件配置
	 * @return  DANIELLO通道组件
	 */
	@Input(value = OriginalProductSink.DANIELLO)
    public SubscribableChannel danielloOriginalProduct();
	/**
	 * 供货商ITALIANI 通道组件配置
	 * @return  ITALIANI通道组件
	 */
	@Input(value = OriginalProductSink.ITALIANI)
    public SubscribableChannel italianiOriginalProduct();
	/**
	 * 供货商TUFANO 通道组件配置
	 * @return  TUFANO通道组件
	 */
	@Input(value = OriginalProductSink.TUFANO)
    public SubscribableChannel tufanoOriginalProduct();
	/**
	 * 供货商MONNIERFRERES 通道组件配置
	 * @return  MONNIERFRERES通道组件
	 */
	@Input(value = OriginalProductSink.MONNIERFRERES)
    public SubscribableChannel monnierfreresOriginalProduct();
	/**
	 * 供货商ELEONORABONUCCI 通道组件配置
	 * @return  ELEONORABONUCCI通道组件
	 */
	@Input(value = OriginalProductSink.ELEONORABONUCCI)
    public SubscribableChannel eleonorabonucciOriginalProduct();
	/**
	 * 供货商RUSSOCAPRI 通道组件配置
	 * @return  RUSSOCAPRI通道组件
	 */
	@Input(value = OriginalProductSink.RUSSOCAPRI)
    public SubscribableChannel russocapriOriginalProduct();
	/**
	 * 供货商 GIGLIO通道组件配置
	 * @return  GIGLIO通道组件
	 */
	@Input(value = OriginalProductSink.GIGLIO)
    public SubscribableChannel giglioOriginalProduct();
	/**
	 * 供货商DIVO 通道组件配置
	 * @return  DIVO通道组件
	 */
	@Input(value = OriginalProductSink.DIVO)
    public SubscribableChannel divoOriginalProduct();
	/**
	 * 供货商 BIONDINI通道组件配置
	 * @return  BIONDINI通道组件
	 */
	@Input(value = OriginalProductSink.BIONDINI)
    public SubscribableChannel biondiniOriginalProduct();
	/**
	 * 供货商DELLOGLIOSTORE 通道组件配置
	 * @return  DELLOGLIOSTORE通道组件
	 */
	@Input(value = OriginalProductSink.DELLOGLIOSTORE)
    public SubscribableChannel dellogliostoreOriginalProduct();
	/**
	 * 供货商 FRANCESCOMASSA通道组件配置
	 * @return  FRANCESCOMASSA通道组件
	 */
	@Input(value = OriginalProductSink.FRANCESCOMASSA)
    public SubscribableChannel francescomassaOriginalProduct();
	/**
	 * 供货商TESSABIT 通道组件配置
	 * @return  TESSABIT通道组件
	 */
	@Input(value = OriginalProductSink.TESSABIT)
    public SubscribableChannel tessabitOriginalProduct();
	/**
	 * 供货商ALDUCADAOSTA 通道组件配置
	 * @return  ALDUCADAOSTA通道组件
	 */
	@Input(value = OriginalProductSink.ALDUCADAOSTA)
    public SubscribableChannel alducadaostaOriginalProduct();
	/**
	 * 供货商SANREMO 通道组件配置
	 * @return  SANREMO通道组件
	 */
	@Input(value = OriginalProductSink.SANREMO)
    public SubscribableChannel sanremoOriginalProduct();
	/**
	 * 供货商 PAOLOFIORILLO通道组件配置
	 * @return  PAOLOFIORILLO通道组件
	 */
	@Input(value = OriginalProductSink.PAOLOFIORILLO)
    public SubscribableChannel paolofiorilloOriginalProduct();
	/**
	 * 供货商VIETTI 通道组件配置
	 * @return  VIETTI通道组件
	 */
	@Input(value = OriginalProductSink.VIETTI)
    public SubscribableChannel viettiOriginalProduct();
	/**
	 * 供货商ANIELLO 通道组件配置
	 * @return  ANIELLO通道组件
	 */
	@Input(value = OriginalProductSink.ANIELLO)
    public SubscribableChannel anielloOriginalProduct();
	/**
	 * 供货商LINDELEPALAIS 通道组件配置
	 * @return  LINDELEPALAIS通道组件
	 */
	@Input(value = OriginalProductSink.LINDELEPALAIS)
    public SubscribableChannel lindelepalaisOriginalProduct();
	/**
	 * 供货商PRITELLI 通道组件配置
	 * @return  PRITELLI通道组件
	 */
	@Input(value = OriginalProductSink.PRITELLI)
    public SubscribableChannel pritelliOriginalProduct();
	/**
	 * 供货商PICCADILLY 通道组件配置
	 * @return  PICCADILLY通道组件
	 */
	@Input(value = OriginalProductSink.PICCADILLY)
    public SubscribableChannel piccadillyOriginalProduct();
	/**
	 * 供货商VELA 通道组件配置
	 * @return  VELA通道组件
	 */
	@Input(value = OriginalProductSink.VELA)
    public SubscribableChannel velaOriginalProduct();
	/**
	 * 供货商MONTI 通道组件配置
	 * @return  MONTI通道组件
	 */
	@Input(value = OriginalProductSink.MONTI)
    public SubscribableChannel montiOriginalProduct();
	/**
	 * 供货商CREATIVE99 通道组件配置
	 * @return  CREATIVE99通道组件
	 */
	@Input(value = OriginalProductSink.CREATIVE99)
    public SubscribableChannel creative99OriginalProduct();
	/**
	 * 供货商LEAM 通道组件配置
	 * @return  LEAM通道组件
	 */
	@Input(value = OriginalProductSink.LEAM)
    public SubscribableChannel leamOriginalProduct();
	/**
	 * 供货商BAGHEERA 通道组件配置
	 * @return  BAGHEERA通道组件
	 */
	@Input(value = OriginalProductSink.BAGHEERA)
    public SubscribableChannel bagheeraOriginalProduct();
	/**
	 * 供货商PAPINI 通道组件配置
	 * @return  PAPINI通道组件
	 */
	@Input(value = OriginalProductSink.PAPINI)
    public SubscribableChannel papiniOriginalProduct();
	/**
	 * 供货商 ZITAFABIANI通道组件配置
	 * @return  ZITAFABIANI通道组件
	 */
	@Input(value = OriginalProductSink.ZITAFABIANI)
    public SubscribableChannel zitafabianiOriginalProduct();
	/**
	 * 供货商 WISE通道组件配置
	 * @return  WISE通道组件
	 */
	@Input(value = OriginalProductSink.WISE)
    public SubscribableChannel wiseOriginalProduct();
	/**
	 * 供货商BASEBLU 通道组件配置
	 * @return  BASEBLU通道组件
	 */
	@Input(value = OriginalProductSink.BASEBLU)
    public SubscribableChannel basebluOriginalProduct();
	/**
	 * 供货商 RAFFAELLONETWORK通道组件配置
	 * @return  RAFFAELLONETWORK通道组件
	 */
	@Input(value = OriginalProductSink.RAFFAELLONETWORK)
    public SubscribableChannel raffaellonetworkOriginalProduct();
	/**
	 * 供货商FRMODA 通道组件配置
	 * @return  FRMODA通道组件
	 */
	@Input(value = OriginalProductSink.FRMODA)
    public SubscribableChannel frmodaOriginalProduct();
	/**
	 * 供货商 STUDIO69通道组件配置
	 * @return  STUDIO69通道组件
	 */
	@Input(value = OriginalProductSink.STUDIO69)
    public SubscribableChannel studio69OriginalProduct();
	/**
	 * 供货商 DELIBERTI通道组件配置
	 * @return  DELIBERTI通道组件
	 */
	@Input(value = OriginalProductSink.DELIBERTI)
    public SubscribableChannel delibertiOriginalProduct();
	/**
	 * 供货商SMETS 通道组件配置
	 * @return  SMETS通道组件
	 */
	@Input(value = OriginalProductSink.SMETS)
    public SubscribableChannel smetsOriginalProduct();
	/**
	 * 供货商 SARENZA通道组件配置
	 * @return  SARENZA通道组件
	 */
	@Input(value = OriginalProductSink.SARENZA)
    public SubscribableChannel sarenzaOriginalProduct();
	/**
	 * 供货商SOLEPLACENEW 通道组件配置
	 * @return  SOLEPLACENEW通道组件
	 */
	@Input(value = OriginalProductSink.SOLEPLACENEW)
    public SubscribableChannel soleplacenewOriginalProduct();
	/**
	 * 供货商 HONESTDOUSA通道组件配置
	 * @return HONESTDOUSA 通道组件
	 */
	@Input(value = OriginalProductSink.HONESTDOUSA)
    public SubscribableChannel honestdousaOriginalProduct();
	/**
	 * 供货商 OPTICAL通道组件配置
	 * @return  OPTICAL通道组件
	 */
	@Input(value = OriginalProductSink.OPTICAL)
    public SubscribableChannel opticalOriginalProduct();
	/**
	 * 供货商TIZIANAFAUSTI 通道组件配置
	 * @return  TIZIANAFAUSTI通道组件
	 */
	@Input(value = OriginalProductSink.TIZIANAFAUSTI)
    public SubscribableChannel tizianafaustiOriginalProduct();
	/**
	 * 供货商 THECLUTCHER通道组件配置
	 * @return  THECLUTCHER通道组件
	 */
	@Input(value = OriginalProductSink.THECLUTCHER)
    public SubscribableChannel theclutcherOriginalProduct();
}
