package com.shangpin.supplier.product.consumer.conf.stream.source.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
/**
 * <p>Title:PendingProductSource.java </p>
 * <p>Description: 待处理商品数据流通道组件配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月5日 下午7:34:44
 */
public interface PendingProductSource {
	
	public static final String fashionTamers = "fashionTamersPendingProduct";
	public static final String cocoroseLondon = "cocoroseLondonPendingProduct";
	public static final String angeloMinetti = "angeloMinettiPendingProduct";
	public static final String dolciTrame = "dolciTramePendingProduct";
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
	
	public static final String COLTORTI = "coltortiPendingProduct";
	
	public static final String TONY = "tonyPendingProduct";
	
	public static final String BIONDIONI = "biondioniPendingProduct";
	//===============================
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

    public static final String VIETTI2 = "vietti2PendingProduct";

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
	public static final String SRL = "srlPendingProduct";
	
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
	@Output(value = PendingProductSource.fashionTamers)
	public MessageChannel fashionTamersPendingProduct();
	/**
	 * 供货商dolciTrame通道组件配置
	 * @return 供货商dolciTrame通道组件
	 */
	@Output(value = PendingProductSource.dolciTrame)
	public MessageChannel dolciTramePendingProduct();
	/**
	 * 供货商cocoroseLondon通道组件配置
	 * @return 供货商cocoroseLondon通道组件
	 */
	@Output(value = PendingProductSource.cocoroseLondon)
	public MessageChannel cocoroseLondonPendingProduct();
	/**
	 * 供货商angeloMinetti通道组件配置
	 * @return 供货商angeloMinetti通道组件
	 */
	@Output(value = PendingProductSource.angeloMinetti)
	public MessageChannel angeloMinettiPendingProduct();
	/**
	 * 供货商JULIANFASHION通道组件配置
	 * @return 供货商JULIANFASHION通道组件
	 */
	@Output(value = PendingProductSource.JULIANFASHION)
	public MessageChannel julianFashionPendingProduct();

	/**
	 * 供货商fratinardi通道组件配置
	 * @return 供货商fratinardi通道组件
	 */
	@Output(value = PendingProductSource.fratinardi)
	public MessageChannel fratinardiPendingProduct();
	/**
	 * 供货商coccolebimbi通道组件配置
	 * @return 供货商coccolebimbi通道组件
	 */
	@Output(value = PendingProductSource.coccolebimbi)
	public MessageChannel coccolebimbiPendingProduct();
	/**
	 * 供货商portofino通道组件配置
	 * @return 供货商portofino通道组件
	 */
	@Output(value = PendingProductSource.portofino)
	public MessageChannel portofinoPendingProduct();
	/**
	 * 供货商forzieri通道组件配置
	 * @return 供货商FORZIERI通道组件
	 */
	@Output(value = PendingProductSource.FORZIERI)
	public MessageChannel forzieriPendingProduct();
	/**
	 * 供货商SPINNAKER通道组件配置
	 * @return 供货商SPINNAKER通道组件
	 */
	@Output(value = PendingProductSource.SPINNAKER)
    public MessageChannel spinnakerPendingProduct();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return 供货商OSTORE通道组件
	 */
	@Output(value = PendingProductSource.OSTORE)
    public MessageChannel ostorePendingProduct();
	/**
	 * 供货商MENGOTTISNC通道组件配置
	 * @return 供货商OSTORE通道组件
	 */
	@Output(value = PendingProductSource.MENGOTTISNC)
	public MessageChannel mengottiSncPendingProduct();

	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return 供货商BRUNAROSSO通道组件
	 */
	@Output(value = PendingProductSource.BRUNAROSSO)
    public MessageChannel brunarossoPendingProduct();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return 供货商STEFANIA通道组件
	 */
	@Output(value = PendingProductSource.STEFANIA)
    public MessageChannel stefaniaPendingProduct();
	/**
	 * 供货商GEB通道组件配置
	 * @return 供货商GEB通道组件
	 */
	@Output(value = PendingProductSource.GEB)
    public MessageChannel gebPendingProduct();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return 供货商COLTORTI通道组件
	 */
	@Output(value = PendingProductSource.COLTORTI)
    public MessageChannel coltortiPendingProduct();
	/**
	 * 供货商TONY通道组件配置
	 * @return 供货商TONY通道组件
	 */
	@Output(value = PendingProductSource.TONY)
    public MessageChannel tonyPendingProduct();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return 供货商BIONDIONI通道组件
	 */
	@Output(value = PendingProductSource.BIONDIONI)
    public MessageChannel biondioniPendingProduct();
	/**
	 * 供货商POZZILEI 通道组件配置
	 * @return 供货商POZZILEI 通道组件
	 */
	@Output(value = PendingProductSource.POZZILEI)
    public MessageChannel pozzileiPendingProduct();
	/**
	 * 供货商POZZILEIARTE 通道组件配置
	 * @return 供货商POZZILEIARTE 通道组件
	 */
	@Output(value = PendingProductSource.POZZILEIARTE)
    public MessageChannel pozzileiartePendingProduct();
	/**
	 * 供货商POZZILEIFORTE 通道组件配置
	 * @return 供货商 POZZILEIFORTE通道组件
	 */
	@Output(value = PendingProductSource.POZZILEIFORTE)
    public MessageChannel pozzileifortePendingProduct();
	/**
	 * 供货商CAROFIGLIO 通道组件配置
	 * @return 供货商 CAROFIGLIO通道组件
	 */
	@Output(value = PendingProductSource.CAROFIGLIO)
    public MessageChannel carofiglioPendingProduct();
	/**
	 * 供货商GENTEROMA 通道组件配置
	 * @return 供货商GENTEROMA 通道组件
	 */
	@Output(value = PendingProductSource.GENTEROMA)
    public MessageChannel genteromaPendingProduct();
	/**
	 * 供货商DANIELLO 通道组件配置
	 * @return 供货商 DANIELLO通道组件
	 */
	@Output(value = PendingProductSource.DANIELLO)
    public MessageChannel danielloPendingProduct();
	/**
	 * 供货商ITALIANI 通道组件配置
	 * @return 供货商ITALIANI 通道组件
	 */
	@Output(value = PendingProductSource.ITALIANI)
    public MessageChannel italianiPendingProduct();
	/**
	 * 供货商TUFANO 通道组件配置
	 * @return 供货商TUFANO 通道组件
	 */
	@Output(value = PendingProductSource.TUFANO)
    public MessageChannel tufanoPendingProduct();
	/**
	 * 供货商MONNIERFRERES 通道组件配置
	 * @return 供货商MONNIERFRERES 通道组件
	 */
	@Output(value = PendingProductSource.MONNIERFRERES)
    public MessageChannel monnierfreresPendingProduct();
	/**
	 * 供货商ELEONORABONUCCI 通道组件配置
	 * @return 供货商ELEONORABONUCCI 通道组件
	 */
	@Output(value = PendingProductSource.ELEONORABONUCCI)
    public MessageChannel eleonorabonucciPendingProduct();
	/**
	 * 供货商RUSSOCAPRI 通道组件配置
	 * @return 供货商RUSSOCAPRI 通道组件
	 */
	@Output(value = PendingProductSource.RUSSOCAPRI)
    public MessageChannel russocapriPendingProduct();
	/**
	 * 供货商GIGLIO 通道组件配置
	 * @return 供货商 GIGLIO通道组件
	 */
	@Output(value = PendingProductSource.GIGLIO)
    public MessageChannel giglioPendingProduct();
	/**
	 * 供货商 DIVO通道组件配置
	 * @return 供货商DIVO 通道组件
	 */
	@Output(value = PendingProductSource.DIVO)
    public MessageChannel divoPendingProduct();
	/**
	 * 供货商BIONDINI 通道组件配置
	 * @return 供货商BIONDINI 通道组件
	 */
	@Output(value = PendingProductSource.BIONDINI)
    public MessageChannel biondiniPendingProduct();
	/**
	 * 供货商DELLOGLIOSTORE 通道组件配置
	 * @return 供货商DELLOGLIOSTORE 通道组件
	 */
	@Output(value = PendingProductSource.DELLOGLIOSTORE)
    public MessageChannel dellogliostorePendingProduct();
	/**
	 * 供货商FRANCESCOMASSA 通道组件配置
	 * @return 供货商 FRANCESCOMASSA通道组件
	 */
	@Output(value = PendingProductSource.FRANCESCOMASSA)
    public MessageChannel francescomassaPendingProduct();
	/**
	 * 供货商TESSABIT 通道组件配置
	 * @return 供货商TESSABIT 通道组件
	 */
	@Output(value = PendingProductSource.TESSABIT)
    public MessageChannel tessabitPendingProduct();
	/**
	 * 供货商 ALDUCADAOSTA通道组件配置
	 * @return 供货商ALDUCADAOSTA 通道组件
	 */
	@Output(value = PendingProductSource.ALDUCADAOSTA)
    public MessageChannel alducadaostaPendingProduct();
	/**
	 * 供货商SANREMO 通道组件配置
	 * @return 供货商SANREMO 通道组件
	 */
	@Output(value = PendingProductSource.SANREMO)
    public MessageChannel sanremoPendingProduct();
	/**
	 * 供货商PAOLOFIORILLO 通道组件配置
	 * @return 供货商PAOLOFIORILLO 通道组件
	 */
	@Output(value = PendingProductSource.PAOLOFIORILLO)
    public MessageChannel paolofiorilloPendingProduct();
	/**
	 * 供货商VIETTI 通道组件配置
	 * @return 供货商VIETTI 通道组件
	 */
	@Output(value = PendingProductSource.VIETTI)
    public MessageChannel viettiPendingProduct();


    /**
     * 供货商VIETTI2 通道组件配置
     * @return 供货商VIETTI2 通道组件
     */
    @Output(value = PendingProductSource.VIETTI2)
    public MessageChannel vietti2PendingProduct();
	/**
	 * 供货商 ANIELLO通道组件配置
	 * @return 供货商ANIELLO 通道组件
	 */
	@Output(value = PendingProductSource.ANIELLO)
    public MessageChannel anielloPendingProduct();
	/**
	 * 供货商LINDELEPALAIS 通道组件配置
	 * @return 供货商LINDELEPALAIS 通道组件
	 */
	@Output(value = PendingProductSource.LINDELEPALAIS)
    public MessageChannel lindelepalaisPendingProduct();
	/**
	 * 供货商PRITELLI 通道组件配置
	 * @return 供货商PRITELLI 通道组件
	 */
	@Output(value = PendingProductSource.PRITELLI)
    public MessageChannel pritelliPendingProduct();
	/**
	 * 供货商PICCADILLY 通道组件配置
	 * @return 供货商PICCADILLY 通道组件
	 */
	@Output(value = PendingProductSource.PICCADILLY)
    public MessageChannel piccadillyPendingProduct();
	/**
	 * 供货商 VELA通道组件配置
	 * @return 供货商VELA 通道组件
	 */
	@Output(value = PendingProductSource.VELA)
    public MessageChannel velaPendingProduct();
	/**
	 * 供货商 MONTI通道组件配置
	 * @return 供货商MONTI 通道组件
	 */
	@Output(value = PendingProductSource.MONTI)
    public MessageChannel montiPendingProduct();
	/**
	 * 供货商 CREATIVE99通道组件配置
	 * @return 供货商CREATIVE99 通道组件
	 */
	@Output(value = PendingProductSource.CREATIVE99)
    public MessageChannel creative99PendingProduct();
	/**
	 * 供货商LEAM 通道组件配置
	 * @return 供货商LEAM 通道组件
	 */
	@Output(value = PendingProductSource.LEAM)
    public MessageChannel leamPendingProduct();
	/**
	 * 供货商BAGHEERA 通道组件配置
	 * @return 供货商BAGHEERA 通道组件
	 */
	@Output(value = PendingProductSource.BAGHEERA)
    public MessageChannel bagheeraPendingProduct();
	/**
	 * 供货商PAPINI 通道组件配置
	 * @return 供货商PAPINI 通道组件
	 */
	@Output(value = PendingProductSource.PAPINI)
    public MessageChannel papiniPendingProduct();
	/**
	 * 供货商ZITAFABIANI 通道组件配置
	 * @return 供货商ZITAFABIANI 通道组件
	 */
	@Output(value = PendingProductSource.ZITAFABIANI)
    public MessageChannel zitafabianiPendingProduct();
	/**
	 * 供货商WISE 通道组件配置
	 * @return 供货商WISE 通道组件
	 */
	@Output(value = PendingProductSource.WISE)
    public MessageChannel wisePendingProduct();
	/**
	 * 供货商BASEBLU 通道组件配置
	 * @return 供货商BASEBLU 通道组件
	 */
	@Output(value = PendingProductSource.BASEBLU)
    public MessageChannel basebluPendingProduct();
	/**
	 * 供货商RAFFAELLONETWORK 通道组件配置
	 * @return 供货商RAFFAELLONETWORK 通道组件
	 */
	@Output(value = PendingProductSource.RAFFAELLONETWORK)
    public MessageChannel raffaellonetworkPendingProduct();
	/**
	 * 供货商FRMODA 通道组件配置
	 * @return 供货商FRMODA 通道组件
	 */
	@Output(value = PendingProductSource.FRMODA)
    public MessageChannel frmodaPendingProduct();
	/**
	 * 供货商STUDIO69 通道组件配置
	 * @return 供货商STUDIO69 通道组件
	 */
	@Output(value = PendingProductSource.STUDIO69)
    public MessageChannel studio69PendingProduct();
	/**
	 * 供货商DELIBERTI 通道组件配置
	 * @return 供货商DELIBERTI 通道组件
	 */
	@Output(value = PendingProductSource.DELIBERTI)
    public MessageChannel delibertiPendingProduct();
	/**
	 * 供货商PARISI 通道组件配置
	 * @return 供货商PARISI 通道组件
	 */
	@Output(value = PendingProductSource.PARISI)
    public MessageChannel parisiPendingProduct();
	/**
	 * 供货商SMETS 通道组件配置
	 * @return 供货商SMETS 通道组件
	 */
	@Output(value = PendingProductSource.SMETS)
    public MessageChannel smetsPendingProduct();
	/**
	 * 供货商SARENZA 通道组件配置
	 * @return 供货商SARENZA 通道组件
	 */
	@Output(value = PendingProductSource.SARENZA)
    public MessageChannel sarenzaPendingProduct();
	/**
	 * 供货商SOLEPLACENEW 通道组件配置
	 * @return 供货商SOLEPLACENEW 通道组件
	 */
	@Output(value = PendingProductSource.SOLEPLACENEW)
    public MessageChannel soleplacenewPendingProduct();
	/**
	 * 供货商HONESTDOUSA 通道组件配置
	 * @return 供货商HONESTDOUSA 通道组件
	 */
	@Output(value = PendingProductSource.HONESTDOUSA)
    public MessageChannel honestdousaPendingProduct();
	/**
	 * 供货商OPTICAL 通道组件配置
	 * @return 供货商OPTICAL 通道组件
	 */
	@Output(value = PendingProductSource.OPTICAL)
    public MessageChannel opticalPendingProduct();
	/**
	 * 供货商TIZIANAFAUSTI 通道组件配置
	 * @return 供货商TIZIANAFAUSTI 通道组件
	 */
	@Output(value = PendingProductSource.TIZIANAFAUSTI)
    public MessageChannel tizianafaustiPendingProduct();
	/**
	 * 供货商THECLUTCHER 通道组件配置
	 * @return 供货商THECLUTCHER 通道组件
	 */
	@Output(value = PendingProductSource.THECLUTCHER)
    public MessageChannel theclutcherPendingProduct();
	/**
	 * 供货商ANTONACCI 通道组件配置
	 * @return 供货商ANTONACCI 通道组件
	 */
	@Output(value = PendingProductSource.ANTONACCI)
	public MessageChannel antonacciPendingProduct();
	/**
	 * 供货商LUNGOLIVIGNO 通道组件配置
	 * @return 供货商LUNGOLIVIGNO 通道组件
	 */
	@Output(value = PendingProductSource.LUNGOLIVIGNO)
	public MessageChannel lungolivignoPendingProduct();
	/**
	 * 供货商FILIPPO 通道组件配置
	 * @return 供货商FILIPPO 通道组件
	 */
	@Output(value = PendingProductSource.FILIPPO)
	public MessageChannel filippoPendingProduct();
	/**
	 * 供货商DELLAMARTIRA 通道组件配置
	 * @return 供货商DELLAMARTIRA 通道组件
	 */
	@Output(value = PendingProductSource.DELLAMARTIRA)
	public MessageChannel dellaMartiraPendingProduct();
	/**
	 * 供货商ROSISERLI 通道组件配置
	 * @return 供货商ROSISERLI 通道组件
	 */
	@Output(value = PendingProductSource.ROSISERLI)
	public MessageChannel rosiSerliPendingProduct();
	/**
	 * 供货商MCLABLES 通道组件配置
	 * @return 供货商MCLABLES 通道组件
	 */
	@Output(value = PendingProductSource.MCLABLES)
	public MessageChannel mclablesPendingProduct();
	/**
	 * 供货商EMONTI 通道组件配置
	 * @return 供货商EMONTI 通道组件
	 */
	@Output(value = PendingProductSource.EMONTI)
	public MessageChannel emontiPendingProduct();
	/**
	 * 供货商DLRBOUTIQUE 通道组件配置
	 * @return 供货商DLRBOUTIQUE 通道组件
	 */
	@Output(value = PendingProductSource.DLRBOUTIQUE)
	public MessageChannel dlrboutiquePendingProduct();
	/**
	 * 供货商REEBONZ 通道组件配置
	 * @return 供货商REEBONZ 通道组件
	 */
	@Output(value = PendingProductSource.REEBONZ)
	public MessageChannel reebonzPendingProduct();
	/**
	 * 供货商GAUDENZI 通道组件配置
	 * @return 供货商GAUDENZI 通道组件
	 */
	@Output(value = PendingProductSource.REEBONZ)
	public MessageChannel gaudenziPendingProduct();
	/**
	 * 供货商MONNALISA 通道组件配置
	 * @return 供货商MONNALISA 通道组件
	 */
	@Output(value = PendingProductSource.MONNALISA)
	public MessageChannel monnalisaPendingProduct();

	/**
	 * 供货商STAR 通道组件配置
	 * @return STAR 通道组件
	 */
	@Output(value = PendingProductSource.STAR)
	public MessageChannel starPendingProduct();

	/**
	 * MARINO 通道组件配置
	 * @return MARINO 通道组件
	 */
	@Output(value = PendingProductSource.MARINO)
	public MessageChannel marinoPendingProduct();


	@Output(value = PendingProductSource.SRL)
	public MessageChannel srlPendingProduct();
	
	/**
	 * 供货商THESTYLESIDE 通道组件配置
	 * @return 供货商THESTYLESIDE 通道组件
	 */
	@Output(value = PendingProductSource.THESTYLESIDE)
	public MessageChannel theStyleSidePendingProduct();


	/**
	 * redi 通道组件配置
	 * @return 供货商THESTYLESIDE 通道组件
	 */
	@Output(value = PendingProductSource.REDI)
	public MessageChannel rediPendingProduct();

	/**
	 * OBLU 通道组件配置
	 * @return 供货商OBLU 通道组件
	 */
	@Output(value = PendingProductSource.OBLU)
	public MessageChannel obluPendingProduct();

	/**
	 * zhicai 通道组件配置
	 * @return 供货商zhicai 通道组件
	 */
	@Output(value = PendingProductSource.ZHICAI)
	public MessageChannel zhicaiPendingProduct();

	@Output(value = PendingProductSource.YLATI)
	public MessageChannel ylatiPendingProduct();
	/**
	 *MAX1980 通道组件配置
	 * @return 供货商MAX1980通道组件
	 */
	@Output(value = PendingProductSource.MAX1980)
	public MessageChannel max1980PendingProduct();

	/**
	 *gebnegozio 通道组件配置
	 * @return 供货商gebnegozio通道组件
	 */
	@Output(value = PendingProductSource.GEBNEGOZIO)
	public MessageChannel gebnegozioPendingProduct();

	/**
	 *ilcucciolo 通道组件配置
	 * @return 供货商ilcucciolo通道组件
	 */
	@Output(value = PendingProductSource.GEBNEGOZIO)
	public MessageChannel ilcuccioloPendingProduct();
}
