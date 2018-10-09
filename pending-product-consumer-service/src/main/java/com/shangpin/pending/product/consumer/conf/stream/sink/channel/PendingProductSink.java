package com.shangpin.pending.product.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * <p>Title:PendingProductSource.java </p>
 * <p>Description: 待处理商品数据流通道组件配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月5日 下午7:34:44
 */
public interface PendingProductSink {
	
	public static final String fashionTamers = "fashionTamersPendingProduct";
	public static final String dolciTrame = "dolciTramePendingProduct";
	public static final String cocoroseLondon = "cocoroseLondonPendingProduct";
	public static final String angeloMinetti = "angeloMinettiPendingProduct";
	public static final String fratinardi = "fratinardiPendingProduct";
	public static final String coccolebimbi = "coccolebimbiPendingProduct";
	public static final String portofino = "portofinoPendingProduct";
	public static final String FORZIERI = "forzieriPendingProduct";
	public static final String SPINNAKER = "spinnakerPendingProduct";
	
	public static final String OSTORE = "ostorePendingProduct";

	public static final String MENGOTTISNC = "mengottiSncPendingProduct";
	
	public static final String BRUNAROSSO = "brunarossoPendingProduct";
	
	public static final String STEFANIA = "stefaniaPendingProduct";
	
	public static final String GEB = "gebPendingProduct";
	public static final String lamborghini = "lamborghiniPendingProduct";
	public static final String paloma = "palomaPendingProduct";
	
	public static final String COLTORTI = "coltortiPendingProduct";
	
	public static final String TONY = "tonyPendingProduct";
	
	public static final String BIONDIONI = "biondioniPendingProduct";

	public static final String POZZILEI = "pozzileiPendingProduct";
	
	public static final String POZZILEIARTE = "pozzileiartePendingProduct";
	
	public static final String POZZILEIFORTE = "pozzileifortePendingProduct";
	
	public static final String CAROFIGLIO = "carofiglioPendingProduct";
	
	public static final String GENTEROMA = "genteromaPendingProduct";
	
	public static final String DANIELLO = "danielloPendingProduct";
	
	public static final String ITALIANI = "italianiPendingProduct";
	
	public static final String TUFANO = "tufanoPendingProduct";
	
	public static final String MONNIERFRERES = "monnierfreresPendingProduct"; 
	
	public static final String ELEONORABONUCCI = "eleonorabonucciPendingProduct";	 
	
	public static final String RUSSOCAPRI = "russocapriPendingProduct"; 
	
	public static final String GIGLIO = "giglioPendingProduct"; 
	
	public static final String DIVO = "divoPendingProduct";	 
	
	public static final String BIONDINI = "biondiniPendingProduct";
	
	public static final String DELLOGLIOSTORE = "dellogliostorePendingProduct";
	
	public static final String FRANCESCOMASSA = "francescomassaPendingProduct";
	
	public static final String TESSABIT = "tessabitPendingProduct";
	
	public static final String ALDUCADAOSTA = "alducadaostaPendingProduct"; 
	
	public static final String SANREMO = "sanremoPendingProduct";	 
	
	public static final String PAOLOFIORILLO = "paolofiorilloPendingProduct"; 
	
	public static final String VIETTI = "viettiPendingProduct";
	public static final String VIETTI2= "vietti2PendingProduct";


    public static final String ANIELLO = "anielloPendingProduct";
	
	public static final String LINDELEPALAIS = "lindelepalaisPendingProduct"; 
	
	public static final String PRITELLI = "pritelliPendingProduct"; 
	
	public static final String PICCADILLY = "piccadillyPendingProduct"; 
	
	public static final String VELA = "velaPendingProduct";	 
	
	public static final String MONTI = "montiPendingProduct"; 
	
	public static final String CREATIVE99 = "creative99PendingProduct"; 
	
	public static final String LEAM = "leamPendingProduct"; 
	
	public static final String BAGHEERA = "bagheeraPendingProduct"; 
	
	public static final String PAPINI = "papiniPendingProduct";	 
	
	public static final String ZITAFABIANI = "zitafabianiPendingProduct"; 
	
	public static final String WISE = "wisePendingProduct"; 
	
	public static final String BASEBLU = "basebluPendingProduct"; 
	
	public static final String RAFFAELLONETWORK = "raffaellonetworkPendingProduct"; 
	
	public static final String FRMODA = "frmodaPendingProduct"; 
	
	public static final String STUDIO69 = "studio69PendingProduct"; 
	
	public static final String DELIBERTI = "delibertiPendingProduct"; 
	
	public static final String PARISI = "parisiPendingProduct";
	
	public static final String SMETS = "smetsPendingProduct";
	
	public static final String SARENZA = "sarenzaPendingProduct";
	
	public static final String SOLEPLACENEW = "soleplacenewPendingProduct"; 
	
	public static final String HONESTDOUSA = "honestdousaPendingProduct";	 
	
	public static final String OPTICAL = "opticalPendingProduct";	 
	
	public static final String TIZIANAFAUSTI = "tizianafaustiPendingProduct"; 	 
	
	public static final String THECLUTCHER = "theclutcherPendingProduct";
	
	public static final String ANTONACCI = "antonacciPendingProduct";
	
	public static final String LUNGOLIVIGNO = "lungolivignoPendingProduct";
	
	public static final String FILIPPO = "filippoPendingProduct";
	
	public static final String DELLAMARTIRA = "dellaMartiraPendingProduct";
	
	public static final String ROSISERLI = "rosiSerliPendingProduct";
	
	public static final String MCLABLES = "mclablesPendingProduct";
	
	public static final String EMONTI = "emontiPendingProduct";
	
	public static final String DLRBOUTIQUE = "dlrboutiquePendingProduct";


	public static final String REEBONZ = "reebonzPendingProduct";
	
	public static final String GAUDENZI = "gaudenziPendingProduct";
	
	public static final String MONNALISA = "monnalisaPendingProduct";

	public static final String STAR = "starPendingProduct";

	
	public static final String THESTYLESIDE = "theStyleSidePendingProduct";

	public static final String JULIANFASHION = "julianFashionPendingProduct";

	public static final String MARINO = "marinoPendingProduct";

	public static final String REDI = "rediPendingProduct";

	public static final String OBLU = "obluPendingProduct";
	
	public static final String ZHICAI = "zhicaiPendingProduct";
	public static final String YLATI = "ylatiPendingProduct";

	public static final String MAX1980 = "max1980PendingProduct";
	
	public static final String GEBNEGOZIO = "gebnegozioPendingProduct";

	public static final String ILCUCCIOLO = "ilcuccioloPendingProduct";

	/**
	 * 供货商fashionTamers通道组件配置
	 * @return 供货商fashionTamers通道组件
	 */
	@Input(value = PendingProductSink.fashionTamers)
	public SubscribableChannel fashionTamersPendingProduct();
	/**
	 * 供货商angeloMinetti通道组件配置
	 * @return 供货商angeloMinetti通道组件
	 */
	@Input(value = PendingProductSink.angeloMinetti)
	public SubscribableChannel angeloMinettiPendingProduct();
	/**
	 * 供货商cocoroseLondon通道组件配置
	 * @return 供货商cocoroseLondon通道组件
	 */
	@Input(value = PendingProductSink.cocoroseLondon)
	public SubscribableChannel cocoroseLondonPendingProduct();
	/**
	 * 供货商dolciTrame通道组件配置
	 * @return 供货商dolciTrame通道组件
	 */
	@Input(value = PendingProductSink.dolciTrame)
	public SubscribableChannel dolciTramePendingProduct();
	/**
	 * 供货商JULIANFASHION通道组件配置
	 * @return 供货商JULIANFASHION通道组件
	 */
	@Input(value = PendingProductSink.JULIANFASHION)
	public SubscribableChannel julianFashionPendingProduct();

	/**
	 * 供货商coccolebimbi通道组件配置
	 * @return 供货商SPINNAKER通道组件
	 */
	@Input(value = PendingProductSink.coccolebimbi)
	public SubscribableChannel coccolebimbiPendingProduct();
	/**
	 * 供货商fratinardi通道组件配置
	 * @return 供货商fratinardi通道组件
	 */
	@Input(value = PendingProductSink.fratinardi)
	public SubscribableChannel fratinardiPendingProduct();
	/**
	 * 供货商portofino通道组件配置
	 * @return 供货商portofino通道组件
	 */
	@Input(value = PendingProductSink.portofino)
	public SubscribableChannel portofinoPendingProduct();
	/**
	 * 供货商FORZIERI通道组件配置
	 * @return 供货商SPINNAKER通道组件
	 */
	@Input(value = PendingProductSink.FORZIERI)
	public SubscribableChannel forzieriPendingProduct();
	/**
	 * 供货商SPINNAKER通道组件配置
	 * @return 供货商SPINNAKER通道组件
	 */
	@Input(value = PendingProductSink.SPINNAKER)
    public SubscribableChannel spinnakerPendingProduct();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return 供货商OSTORE通道组件
	 */
	@Input(value = PendingProductSink.OSTORE)
    public SubscribableChannel ostorePendingProduct();
	/**
	 * 供货商MENGOTTISNC通道组件配置
	 * @return 供货商MENGOTTTSNC通道组件
	 */
	@Input(value = PendingProductSink.MENGOTTISNC)
	public SubscribableChannel mengottiSncPendingProduct();
	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return 供货商BRUNAROSSO通道组件
	 */
	@Input(value = PendingProductSink.BRUNAROSSO)
    public SubscribableChannel brunarossoPendingProduct();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return 供货商STEFANIA通道组件
	 */
	@Input(value = PendingProductSink.STEFANIA)
    public SubscribableChannel stefaniaPendingProduct();
	/**
	 * 供货商paloma通道组件配置
	 * @return 供货商paloma通道组件
	 */
	@Input(value = PendingProductSink.paloma)
    public SubscribableChannel palomaPendingProduct();
	/**
	 * 供货商GEB通道组件配置
	 * @return 供货商GEB通道组件
	 */
	@Input(value = PendingProductSink.GEB)
	public SubscribableChannel gebPendingProduct();
	/**
	 * 供货商lamborghini通道组件配置
	 * @return 供货商lamborghini通道组件
	 */
	@Input(value = PendingProductSink.lamborghini)
	public SubscribableChannel lamborghiniPendingProduct();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return 供货商COLTORTI通道组件
	 */
	@Input(value = PendingProductSink.COLTORTI)
    public SubscribableChannel coltortiPendingProduct();
	/**
	 * 供货商TONY通道组件配置
	 * @return 供货商TONY通道组件
	 */
	@Input(value = PendingProductSink.TONY)
    public SubscribableChannel tonyPendingProduct();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return 供货商BIONDIONI通道组件
	 */
	@Input(value = PendingProductSink.BIONDIONI)
    public SubscribableChannel biondioniPendingProduct();
	/**
	 * 供货商POZZILEI通道组件配置
	 * @return 供货商POZZILEI通道组件
	 */
	@Input(value = PendingProductSink.POZZILEI)
    public SubscribableChannel pozzileiPendingProduct();
	/**
	 * 供货商POZZILEIARTE通道组件配置
	 * @return 供货商POZZILEIARTE通道组件
	 */
	@Input(value = PendingProductSink.POZZILEIARTE)
    public SubscribableChannel pozzileiartePendingProduct();
	/**
	 * 供货商POZZILEIFORTE通道组件配置
	 * @return 供货商POZZILEIFORTE通道组件
	 */
	@Input(value = PendingProductSink.POZZILEIFORTE)
    public SubscribableChannel pozzileifortePendingProduct();
	/**
	 * 供货商CAROFIGLIO通道组件配置
	 * @return 供货商CAROFIGLIO通道组件
	 */
	@Input(value = PendingProductSink.CAROFIGLIO)
    public SubscribableChannel carofiglioPendingProduct();
	/**
	 * 供货商GENTEROMA通道组件配置
	 * @return 供货商GENTEROMA通道组件
	 */
	@Input(value = PendingProductSink.GENTEROMA)
    public SubscribableChannel genteromaPendingProduct();
	/**
	 * 供货商DANIELLO通道组件配置
	 * @return 供货商DANIELLO通道组件
	 */
	@Input(value = PendingProductSink.DANIELLO)
    public SubscribableChannel danielloPendingProduct();
	/**
	 * 供货商ITALIANI通道组件配置
	 * @return 供货商ITALIANI通道组件
	 */
	@Input(value = PendingProductSink.ITALIANI)
    public SubscribableChannel italianiPendingProduct();
	/**
	 * 供货商TUFANO通道组件配置
	 * @return 供货商TUFANO通道组件
	 */
	@Input(value = PendingProductSink.TUFANO)
    public SubscribableChannel tufanoPendingProduct();
	/**
	 * 供货商MONNIERFRERES通道组件配置
	 * @return 供货商MONNIERFRERES通道组件
	 */
	@Input(value = PendingProductSink.MONNIERFRERES)
    public SubscribableChannel monnierfreresPendingProduct();
	/**
	 * 供货商ELEONORABONUCCI通道组件配置
	 * @return 供货商ELEONORABONUCCI通道组件
	 */
	@Input(value = PendingProductSink.ELEONORABONUCCI)
    public SubscribableChannel eleonorabonucciPendingProduct();
	/**
	 * 供货商RUSSOCAPRI通道组件配置
	 * @return 供货商RUSSOCAPRI通道组件
	 */
	@Input(value = PendingProductSink.RUSSOCAPRI)
    public SubscribableChannel russocapriPendingProduct();
	/**
	 * 供货商GIGLIO通道组件配置
	 * @return 供货商GIGLIO通道组件
	 */
	@Input(value = PendingProductSink.GIGLIO)
    public SubscribableChannel giglioPendingProduct();
	/**
	 * 供货商DIVO通道组件配置
	 * @return 供货商DIVO通道组件
	 */
	@Input(value = PendingProductSink.DIVO)
    public SubscribableChannel divoPendingProduct();
	/**
	 * 供货商BIONDINI通道组件配置
	 * @return 供货商BIONDINI通道组件
	 */
	@Input(value = PendingProductSink.BIONDINI)
    public SubscribableChannel biondiniPendingProduct();
	/**
	 * 供货商DELLOGLIOSTORE通道组件配置
	 * @return 供货商DELLOGLIOSTORE通道组件
	 */
	@Input(value = PendingProductSink.DELLOGLIOSTORE)
    public SubscribableChannel dellogliostorePendingProduct();
	/**
	 * 供货商FRANCESCOMASSA通道组件配置
	 * @return 供货商FRANCESCOMASSA通道组件
	 */
	@Input(value = PendingProductSink.FRANCESCOMASSA)
    public SubscribableChannel francescomassaPendingProduct();
	/**
	 * 供货商TESSABIT通道组件配置
	 * @return 供货商TESSABIT通道组件
	 */
	@Input(value = PendingProductSink.TESSABIT)
    public SubscribableChannel tessabitPendingProduct();
	/**
	 * 供货商ALDUCADAOSTA通道组件配置
	 * @return 供货商ALDUCADAOSTA通道组件
	 */
	@Input(value = PendingProductSink.ALDUCADAOSTA)
    public SubscribableChannel alducadaostaPendingProduct();
	/**
	 * 供货商SANREMO通道组件配置
	 * @return 供货商SANREMO通道组件
	 */
	@Input(value = PendingProductSink.SANREMO)
    public SubscribableChannel sanremoPendingProduct();
	/**
	 * 供货商PAOLOFIORILLO通道组件配置
	 * @return 供货商PAOLOFIORILLO通道组件
	 */
	@Input(value = PendingProductSink.PAOLOFIORILLO)
    public SubscribableChannel paolofiorilloPendingProduct();
	/**
	 * 供货商VIETTI通道组件配置
	 * @return 供货商VIETTI通道组件
	 */
	@Input(value = PendingProductSink.VIETTI)
    public SubscribableChannel viettiPendingProduct();

    /**
     * 供货商VIETTI通道组件配置
     * @return 供货商VIETTI通道组件
     */
    @Input(value = PendingProductSink.VIETTI2)
    public SubscribableChannel vietti2PendingProduct();


	/**
	 * 供货商ANIELLO通道组件配置
	 * @return 供货商ANIELLO通道组件
	 */
	@Input(value = PendingProductSink.ANIELLO)
    public SubscribableChannel anielloPendingProduct();
	/**
	 * 供货商LINDELEPALAIS通道组件配置
	 * @return 供货商LINDELEPALAIS通道组件
	 */
	@Input(value = PendingProductSink.LINDELEPALAIS)
    public SubscribableChannel lindelepalaisPendingProduct();
	/**
	 * 供货商PRITELLI通道组件配置
	 * @return 供货商PRITELLI通道组件
	 */
	@Input(value = PendingProductSink.PRITELLI)
    public SubscribableChannel pritelliPendingProduct();
	/**
	 * 供货商PICCADILLY通道组件配置
	 * @return 供货商PICCADILLY通道组件
	 */
	@Input(value = PendingProductSink.PICCADILLY)
    public SubscribableChannel piccadillyPendingProduct();
	/**
	 * 供货商VELA通道组件配置
	 * @return 供货商VELA通道组件
	 */
	@Input(value = PendingProductSink.VELA)
    public SubscribableChannel velaPendingProduct();
	/**
	 * 供货商MONTI通道组件配置
	 * @return 供货商MONTI通道组件
	 */
	@Input(value = PendingProductSink.MONTI)
    public SubscribableChannel montiPendingProduct();
	/**
	 * 供货商CREATIVE99通道组件配置
	 * @return 供货商CREATIVE99通道组件
	 */
	@Input(value = PendingProductSink.CREATIVE99)
    public SubscribableChannel creative99PendingProduct();
	/**
	 * 供货商LEAM通道组件配置
	 * @return 供货商LEAM通道组件
	 */
	@Input(value = PendingProductSink.LEAM)
    public SubscribableChannel leamPendingProduct();
	/**
	 * 供货商BAGHEERA通道组件配置
	 * @return 供货商BAGHEERA通道组件
	 */
	@Input(value = PendingProductSink.BAGHEERA)
    public SubscribableChannel bagheeraPendingProduct();
	/**
	 * 供货商PAPINI通道组件配置
	 * @return 供货商PAPINI通道组件
	 */
	@Input(value = PendingProductSink.PAPINI)
    public SubscribableChannel papiniPendingProduct();
	/**
	 * 供货商ZITAFABIANI通道组件配置
	 * @return 供货商ZITAFABIANI通道组件
	 */
	@Input(value = PendingProductSink.ZITAFABIANI)
    public SubscribableChannel zitafabianiPendingProduct();
	/**
	 * 供货商WISE通道组件配置
	 * @return 供货商WISE通道组件
	 */
	@Input(value = PendingProductSink.WISE)
    public SubscribableChannel wisePendingProduct();
	/**
	 * 供货商BASEBLU通道组件配置
	 * @return 供货商BASEBLU通道组件
	 */
	@Input(value = PendingProductSink.BASEBLU)
    public SubscribableChannel basebluPendingProduct();
	/**
	 * 供货商RAFFAELLONETWORK通道组件配置
	 * @return 供货商RAFFAELLONETWORK通道组件
	 */
	@Input(value = PendingProductSink.RAFFAELLONETWORK)
    public SubscribableChannel raffaellonetworkPendingProduct();
	/**
	 * 供货商FRMODA通道组件配置
	 * @return 供货商FRMODA通道组件
	 */
	@Input(value = PendingProductSink.FRMODA)
    public SubscribableChannel frmodaPendingProduct();
	/**
	 * 供货商STUDIO69通道组件配置
	 * @return 供货商STUDIO69通道组件
	 */
	@Input(value = PendingProductSink.STUDIO69)
    public SubscribableChannel studio69PendingProduct();
	/**
	 * 供货商DELIBERTI通道组件配置
	 * @return 供货商DELIBERTI通道组件
	 */
	@Input(value = PendingProductSink.DELIBERTI)
    public SubscribableChannel delibertiPendingProduct();
	/**
	 * 供货商PARISI通道组件配置
	 * @return 供货商PARISI通道组件
	 */
	@Input(value = PendingProductSink.PARISI)
    public SubscribableChannel parisiPendingProduct();
	/**
	 * 供货商SMETS通道组件配置
	 * @return 供货商SMETS通道组件
	 */
	@Input(value = PendingProductSink.SMETS)
    public SubscribableChannel smetsPendingProduct();
	/**
	 * 供货商SARENZA通道组件配置
	 * @return 供货商SARENZA通道组件
	 */
	@Input(value = PendingProductSink.SARENZA)
    public SubscribableChannel sarenzaPendingProduct();
	/**
	 * 供货商SOLEPLACENEW通道组件配置
	 * @return 供货商SOLEPLACENEW通道组件
	 */
	@Input(value = PendingProductSink.SOLEPLACENEW)
    public SubscribableChannel soleplacenewPendingProduct();
	/**
	 * 供货商HONESTDOUSA通道组件配置
	 * @return 供货商HONESTDOUSA通道组件
	 */
	@Input(value = PendingProductSink.HONESTDOUSA)
    public SubscribableChannel honestdousaPendingProduct();
	/**
	 * 供货商OPTICAL通道组件配置
	 * @return 供货商OPTICAL通道组件
	 */
	@Input(value = PendingProductSink.OPTICAL)
    public SubscribableChannel opticalPendingProduct();
	/**
	 * 供货商TIZIANAFAUSTI通道组件配置
	 * @return 供货商TIZIANAFAUSTI通道组件
	 */
	@Input(value = PendingProductSink.TIZIANAFAUSTI)
    public SubscribableChannel tizianafaustiPendingProduct();
	/**
	 * 供货商THECLUTCHER通道组件配置
	 * @return 供货商THECLUTCHER通道组件
	 */
	@Input(value = PendingProductSink.THECLUTCHER)
    public SubscribableChannel theclutcherPendingProduct();
	/**
	 * 供货商antonacci通道组件配置
	 * @return 供货商antonacci通道组件
	 */
	@Input(value = PendingProductSink.ANTONACCI)
	public SubscribableChannel antonacciPendingProduct();
	/**
	 * 供应商LUNGOLIVIGNO通道组件配置
	 * @return 供货商LUNGOLIVIGNO通道组件
	 */
	@Input(value = PendingProductSink.LUNGOLIVIGNO)
	public SubscribableChannel lungolivignoPendingProduct();
	/**
	 * 供应商FILIPPO通道组件配置
	 * @return 供货商FILIPPO通道组件
	 */
	@Input(value = PendingProductSink.FILIPPO)
	public SubscribableChannel filippoPendingProduct();
	/**
	 * 供应商DELLAMARTIRA通道组件配置
	 * @return 供货商DELLAMARTIRA通道组件
	 */
	@Input(value = PendingProductSink.DELLAMARTIRA)
	public SubscribableChannel dellaMartiraPendingProduct();
	/**
	 * 供应商ROSISERLI通道组件配置
	 * @return 供货商ROSISERLI通道组件
	 */
	@Input(value = PendingProductSink.ROSISERLI)
	public SubscribableChannel rosiSerliPendingProduct();
	/**
	 * 供应商MCLABLES通道组件配置
	 * @return 供货商MCLABLES通道组件
	 */
	@Input(value = PendingProductSink.MCLABLES)
	public SubscribableChannel mclablesPendingProduct();
	/**
	 * 供应商EMONTI通道组件配置
	 * @return 供货商EMONTI通道组件
	 */
	@Input(value = PendingProductSink.EMONTI)
	public SubscribableChannel emontiPendingProduct();
	/**
	 * 供应商DLRBOUTIQUE通道组件配置
	 * @return 供货商DLRBOUTIQUE通道组件
	 */
	@Input(value = PendingProductSink.DLRBOUTIQUE)
	public SubscribableChannel dlrboutiquePendingProduct();

	/**
	 * 供应商REEBONZ通道组件配置
	 * @return 供货商REEBONZ通道组件
	 */
	@Input(value = PendingProductSink.REEBONZ)
	public SubscribableChannel reebonzPendingProduct();
	
	/**
	 * 供应商GAUDENZI通道组件配置
	 * @return 供货商GAUDENZI通道组件
	 */
	@Input(value = PendingProductSink.GAUDENZI)
	public SubscribableChannel gaudenziPendingProduct();
	
	/**
	 * 供应商MONNALISA通道组件配置
	 * @return 供货商MONNALISA通道组件
	 */
	@Input(value = PendingProductSink.MONNALISA)
	public SubscribableChannel monnalisaPendingProduct();

	/**

	 * 供应商STAR通道组件配置
	 * @return 供货商STAR通道组件
	 */
	@Input(value = PendingProductSink.STAR)
	public SubscribableChannel starPendingProduct();


	/**
	 * 供应商MARINO通道组件配置
	 * @return 供货商MARINO通道组件
	 */
	@Input(value = PendingProductSink.MARINO)
	public SubscribableChannel marinoPendingProduct();

	
	/**
	 * 供应商THESTYLESIDE通道组件配置
	 * @return 供货商THESTYLESIDE通道组件
	 */
	@Input(value = PendingProductSink.THESTYLESIDE)
	public SubscribableChannel theStyleSidePendingProduct();

	@Input(value = PendingProductSink.REDI)
	public SubscribableChannel rediPendingProduct();


	@Input(value = PendingProductSink.OBLU)
	public SubscribableChannel obluPendingProduct();
	
	@Input(value = PendingProductSink.ZHICAI)
	public SubscribableChannel zhicaiPendingProduct();

	@Input(value = PendingProductSink.YLATI)
	public SubscribableChannel ylatiPendingProduct();

	@Input(value = PendingProductSink.MAX1980)
	public SubscribableChannel max1980PendingProduct();

	/**
	 * 供应商gebnegozio通道组件配置
	 * @return 供货商gebnegozio通道组件
	 */
	@Input(value = PendingProductSink.GEBNEGOZIO)
	public SubscribableChannel gebnegozioPendingProduct();

	/**
	 * 供应商ilcucciolo通道组件配置
	 * @return 供货商ilcucciolo通道组件
	 */
	@Input(value = PendingProductSink.ILCUCCIOLO)
	public SubscribableChannel ilcuccioloPendingProduct();
}
