package com.shangpin.supplier.product.message.original.conf.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
/**
 * <p>Title:OriginalProductSource.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月5日 下午7:34:44
 */
public interface OriginalProductSource {
	
	
	public static final String fashionTamers = "fashionTamersOriginalProduct";
	public static final String dolciTrame = "dolciTrameOriginalProduct";
	public static final String cocoroseLondon = "cocoroseLondonOriginalProduct";
	public static final String angeloMinetti = "angeloMinettiOriginalProduct";
	public static final String portofino = "portofinoOriginalProduct";
	public static final String coccolebimbi = "coccolebimbiOriginalProduct";
	public static final String fratinardi = "fratinardiOriginalProduct";
	public static final String FORZIERI = "forzieriOriginalProduct";
	public static final String SPINNAKER = "spinnakerOriginalProduct";
    public static final String SUITNEGOZI = "suitnegoziOriginalProduct";
	public static final String OSTORE = "ostoreOriginalProduct";
	
	public static final String BRUNAROSSO = "brunarossoOriginalProduct";
	
	public static final String STEFANIA = "stefaniaOriginalProduct";
	
	public static final String GEB = "gebOriginalProduct";
	public static final String PALOMA = "palomaOriginalProduct";
	public static final String LAMBORGHINI = "lamborghiniOriginalProduct";

	public static final String COLTORTI = "coltortiOriginalProduct";
	
	public static final String TONY = "tonyOriginalProduct";
	
	public static final String BIONDIONI = "biondioniOriginalProduct";
	//===================================
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

	public static final String  MENGOTTISNC= "mengottiSncOriginalProduct";
	
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

	public static final String PARISI = "parisiOriginalProduct";

	public static final String SMETS = "smetsOriginalProduct";
	
	public static final String SARENZA = "sarenzaOriginalProduct";
	
	public static final String SOLEPLACENEW = "soleplacenewOriginalProduct"; 
	
	public static final String HONESTDOUSA = "honestdousaOriginalProduct";	 
	
	public static final String OPTICAL = "opticalOriginalProduct";	 
	
	public static final String TIZIANAFAUSTI = "tizianafaustiOriginalProduct"; 	 
	
	public static final String THECLUTCHER = "theclutcherOriginalProduct"; 
	public static final String ANTONACCI = "antonacciOriginalProduct";

	public static final String LUNGOLIVIGNO = "lungolivignoOriginalProduct";

	public static final String FILIPPO = "filippoOriginalProduct";

	public static final String DELLAMARTIRA = "dellaMartiraOriginalProduct";

	public static final String ROSISERLI = "rosiSerliOriginalProduct";

	public static final String MCLABLES = "mclablesOriginalProduct";

	public static final String EMONTI = "emontiOriginalProduct";

	public static final String DLRBOUTIQUE = "dlrboutiqueOriginalProduct";

	public static final String ALL_PRODUCT_STOCK = "allProductStock";


    public static final String REEBONZ = "reebonzOriginalProduct";
    
    public static final String GAUDENZI = "gaudenziOriginalProduct";
    
    public static final String MONNALISA = "monnalisaOriginalProduct";
    
    public static final String STAR = "starOriginalProduct";
    
    public static final String THESTYLESIDE = "theStyleSideOriginalProduct";
    
    public static final String JULIANFASHION = "julianFashionOriginalProduct";

    public static final String MARINO ="marinoOriginalProduct";

	public static final String REDI ="rediOriginalProduct";

	public static final String OBLU ="obluOriginalProduct";
	
	public static final String ZHICAI ="zhicaiOriginalProduct";

	public static final String YLATI ="ylatiOriginalProduct";

	public static final String MAX1980 ="max1980OriginalProduct";

    public static final String VIETTI2 = "vietti2OriginalProduct";

    public static final String ILCUCCIOLO = "ilcuccioloOriginalProduct";


	public static final String GEBNEGOZIO = "gebnegozioOriginalProduct";

    public static final String ERALDO = "eraldoOriginalProduct";

	public static final String VIPGROUP = "vipgroupOriginalProduct";

	public static final String TRICOT = "tricotOriginalProduct";
	public static final String ACCURATIME = "accuratimeOriginalProduct";

	public static final String BINI = "biniOriginalProduct";



    public static final String NUGNES = "nugnesOriginalProduct";

    public static final String MANTOVANI = "mantovaniOriginalProduct";
    /**
     * 供货商fashionTamers通道组件配置
     * @return 供货商fashionTamers通道组件
     */
    @Output(value = OriginalProductSource.fashionTamers)
    public MessageChannel fashionTamers();
	/**
     * 供货商angeloMinetti通道组件配置
     * @return 供货商angeloMinetti通道组件
     */
    @Output(value = OriginalProductSource.angeloMinetti)
    public MessageChannel angeloMinetti();
    /**
     * 供货商cocoroseLondon通道组件配置
     * @return 供货商cocoroseLondon通道组件
     */
    @Output(value = OriginalProductSource.cocoroseLondon)
    public MessageChannel cocoroseLondon();
    /**
     * 供货商dolciTrame通道组件配置
     * @return 供货商dolciTrame通道组件
     */
    @Output(value = OriginalProductSource.dolciTrame)
    public MessageChannel dolciTrame();
    /**
	 * 供货商JULIANFASHION通道组件配置
	 * @return 供货商JULIANFASHION通道组件
	 */
	@Output(value = OriginalProductSource.JULIANFASHION)
	public MessageChannel julianFashion();

    /**
	 * 供货商portofino通道组件配置
	 * @return 供货商portofino通道组件
	 */
	@Output(value = OriginalProductSource.portofino)
	public MessageChannel portofino();
	/**
	 * 供货商coccolebimbi通道组件配置
	 * @return 供货商coccolebimbi通道组件
	 */
	@Output(value = OriginalProductSource.coccolebimbi)
	public MessageChannel coccolebimbi();
	/**
	 * 供货商fratinardi通道组件配置
	 * @return 供货商FORZIERI通道组件
	 */
	@Output(value = OriginalProductSource.fratinardi)
	public MessageChannel fratinardi();
	/**
	 * 供货商FORZIERI通道组件配置
	 * @return 供货商FORZIERI通道组件
	 */
	@Output(value = OriginalProductSource.FORZIERI)
	public MessageChannel forzieri();


	/**
	 * 供货商SPINNAKER通道组件配置
	 * @return 供货商SPINNAKER通道组件
	 */
	@Output(value = OriginalProductSource.SPINNAKER)
    public MessageChannel spinnaker();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return 供货商OSTORE通道组件
	 */
	@Output(value = OriginalProductSource.OSTORE)
    public MessageChannel ostore();
	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return 供货商BRUNAROSSO通道组件
	 */
	@Output(value = OriginalProductSource.BRUNAROSSO)
    public MessageChannel brunarosso();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return 供货商STEFANIA通道组件
	 */
	@Output(value = OriginalProductSource.STEFANIA)
    public MessageChannel stefania();
	/**
	 * 供货商LAMBORGHINI通道组件配置
	 * @return 供货商LAMBORGHINI通道组件
	 */
	@Output(value = OriginalProductSource.LAMBORGHINI)
    public MessageChannel lamborghini();
	/**
	 * 供货商PALOMA通道组件配置
	 * @return 供货商PALOMA通道组件
	 */
	@Output(value = OriginalProductSource.PALOMA)
	public MessageChannel paloma();
	/**
	 * 供货商GEB通道组件配置
	 * @return 供货商GEB通道组件
	 */
	@Output(value = OriginalProductSource.GEB)
	public MessageChannel geb();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return 供货商COLTORTI通道组件
	 */
	@Output(value = OriginalProductSource.COLTORTI)
    public MessageChannel coltorti();
	/**
	 * 供货商TONY通道组件配置
	 * @return 供货商TONY通道组件
	 */
	@Output(value = OriginalProductSource.TONY)
    public MessageChannel tony();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return 供货商BIONDIONI通道组件
	 */
	@Output(value = OriginalProductSource.BIONDIONI)
    public MessageChannel biondioni();
	/**
	 * 供货商POZZILEI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.POZZILEI)
    public MessageChannel pozzilei();
	/**
	 * 供货商POZZILEIARTE通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.POZZILEIARTE)
    public MessageChannel pozzileiarte();
	/**
	 * 供货商POZZILEIFORTE通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.POZZILEIFORTE)
    public MessageChannel pozzileiforte();
	/**
	 * 供货商CAROFIGLIO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.CAROFIGLIO)
    public MessageChannel carofiglio();
	/**
	 * 供货商GENTEROMA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.GENTEROMA)
    public MessageChannel genteroma();
	/**
	 * 供货商DANIELLO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.DANIELLO)
    public MessageChannel daniello();
	/**
	 * 供货商ITALIANI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ITALIANI)
    public MessageChannel italiani();
	/**
	 * 供货商TUFANO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.TUFANO)
    public MessageChannel tufano();
	/**
	 * 供货商MONNIERFRERES通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.MONNIERFRERES)
    public MessageChannel monnierfreres();
	/**
	 * 供货商ELEONORABONUCCI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ELEONORABONUCCI)
    public MessageChannel eleonorabonucci();
	/**
	 * 供货商RUSSOCAPRI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.RUSSOCAPRI)
    public MessageChannel russocapri();
	/**
	 * 供货商GIGLIO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.GIGLIO)
    public MessageChannel giglio();
	/**
	 * 供货商DIVO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.DIVO)
    public MessageChannel divo();

	/**
	 * 供货商mengottiSnc通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.MENGOTTISNC)
	public MessageChannel mengottiSnc();

	/**
	 * 供货商BIONDINI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.BIONDINI)
    public MessageChannel biondini();
	/**
	 * 供货商DELLOGLIOSTORE通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.DELLOGLIOSTORE)
    public MessageChannel dellogliostore();
	/**
	 * 供货商FRANCESCOMASSA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.FRANCESCOMASSA)
    public MessageChannel francescomassa();
	/**
	 * 供货商TESSABIT通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.TESSABIT)
    public MessageChannel tessabit();
	/**
	 * 供货商ALDUCADAOSTA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ALDUCADAOSTA)
    public MessageChannel alducadaosta();
	/**
	 * 供货商SANREMO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.SANREMO)
    public MessageChannel sanremo();
	/**
	 * 供货商PAOLOFIORILLO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.PAOLOFIORILLO)
    public MessageChannel paolofiorillo();
	/**
	 * 供货商VIETTI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.VIETTI)
    public MessageChannel vietti();
	/**
	 * 供货商ANIELLO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ANIELLO)
    public MessageChannel aniello();
	/**
	 * 供货商LINDELEPALAIS通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.LINDELEPALAIS)
    public MessageChannel lindelepalais();
	/**
	 * 供货商PRITELLI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.PRITELLI)
    public MessageChannel pritelli();
	/**
	 * 供货商PICCADILLY通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.PICCADILLY)
    public MessageChannel piccadilly();
	/**
	 * 供货商VELA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.VELA)
    public MessageChannel vela();
	/**
	 * 供货商MONTI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.MONTI)
    public MessageChannel monti();
	/**
	 * 供货商CREATIVE99通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.CREATIVE99)
    public MessageChannel creative99();
	/**
	 * 供货商LEAM通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.LEAM)
    public MessageChannel leam();
	/**
	 * 供货商BAGHEERA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.BAGHEERA)
    public MessageChannel bagheera();
	/**
	 * 供货商PAPINI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.PAPINI)
    public MessageChannel papini();
	/**
	 * 供货商ZITAFABIANI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ZITAFABIANI)
    public MessageChannel zitafabiani();
	/**
	 * 供货商WISE通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.WISE)
    public MessageChannel wise();
	/**
	 * 供货商BASEBLU通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.BASEBLU)
    public MessageChannel baseblu();
	/**
	 * 供货商RAFFAELLONETWORK通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.RAFFAELLONETWORK)
    public MessageChannel raffaellonetwork();
	/**
	 * 供货商FRMODA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.FRMODA)
    public MessageChannel frmoda();
	/**
	 * 供货商通STUDIO69道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.STUDIO69)
    public MessageChannel studio69();
	/**
	 * 供货商DELIBERTI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.DELIBERTI)
    public MessageChannel deliberti();
	/**
	 * 供货商PARISI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.PARISI)
    public MessageChannel parisi();
	/**
	 * 供货商SMETS通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.SMETS)
    public MessageChannel smets();
	/**
	 * 供货商SARENZA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.SARENZA)
    public MessageChannel sarenza();
	/**
	 * 供货商SOLEPLACENEW通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.SOLEPLACENEW)
    public MessageChannel soleplacenew();
	/**
	 * 供货商HONESTDOUSA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.HONESTDOUSA)
    public MessageChannel honestdousa();
	/**
	 * 供货商OPTICAL通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.OPTICAL)
    public MessageChannel optical();
	/**
	 * 供货商TIZIANAFAUSTI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.TIZIANAFAUSTI)
    public MessageChannel tizianafausti();
	/**
	 * 供货商THECLUTCHER通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.THECLUTCHER)
    public MessageChannel theclutcher();
	/**
	 * 供货商ANTONACCI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ANTONACCI)
	public MessageChannel antonacci();
	/**
	 * 供货商LUNGOLIVIGNO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.LUNGOLIVIGNO)
	public MessageChannel lungolivigno();
	/**
	 * 供应商FILIPPO通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.FILIPPO)
	public MessageChannel filippo();
	/**
	 * 供应商DELLAMARTIRA通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.DELLAMARTIRA)
	public MessageChannel dellaMartira();
	/**
	 * 供应商ROSISERLI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ROSISERLI)
	public MessageChannel rosiSerli();
	/**
	 * 供应商MCLABLES通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.MCLABLES)
	public MessageChannel mclables();
	/**
	 * 供应商EMONTI通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.EMONTI)
	public MessageChannel emonti();
	/**
	 * 供应商DLRBOUTIQUE通道组件配置
	 * @return
	 */
	@Output(value = OriginalProductSource.DLRBOUTIQUE)
	public MessageChannel dlrboutique();
	/**
	 * 所有产品的库存通道
	 * @return
	 */
	@Output(value = OriginalProductSource.ALL_PRODUCT_STOCK)
	public MessageChannel allProductStock();


	/**
	 * 供货商reebonz通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.REEBONZ)
	public MessageChannel reebonz();
	
	/**
	 * 供货商gaudenzi通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.GAUDENZI)
	public MessageChannel gaudenzi();
	
	/**
	 * 供货商monnalisa通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.MONNALISA)
	public MessageChannel monnalisa();
	
	/**
	 * 供货商star通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.STAR)
	public MessageChannel star();
	
	/**
	 * 供货商theStyleSide通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.THESTYLESIDE)
	public MessageChannel theStyleSide();

	/* OUTPUT channel */
	@Output(value = OriginalProductSource.MARINO)
	public MessageChannel marino();

	@Output(value = OriginalProductSource.REDI)
	public MessageChannel redi();


	@Output(value = OriginalProductSource.OBLU)
	public MessageChannel oblu();
	
	@Output(value = OriginalProductSource.ZHICAI)
	public MessageChannel zhicai();

	@Output(value = OriginalProductSource.YLATI)
	public MessageChannel ylati();

	@Output(value = OriginalProductSource.MAX1980)
	public MessageChannel max1980();

    /**
     * 供货商VIETTI通道组件配置
     * @return 供货商通道组件
     */
    @Output(value = OriginalProductSource.VIETTI2)
    public MessageChannel vietti2();

    /**
     * 供货商ILCUCCIOLO通道组件配置
     * @return 供货商通道组件
     */
    @Output(value = OriginalProductSource.ILCUCCIOLO)
    public MessageChannel ilcucciolo();



	/**
	 * 供货商gebnegozio通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.GEBNEGOZIO)
	public MessageChannel gebnegozio();

	/**
	 * 供货商eraldo通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ERALDO)
	public MessageChannel eraldo();

	/**
	 * 供货商vipgroup通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.VIPGROUP)
	public MessageChannel vipgroup();


    @Output(value = OriginalProductSource.SUITNEGOZI)
    public MessageChannel suitnegozi();


	/**
	 * 供货商tricot通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.TRICOT)
	public MessageChannel tricot();
	/**
	 * 供货商tricot通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.ACCURATIME)
	public MessageChannel accuratime();


	/**
	 * 供货商bini通道组件配置
	 * @return 供货商通道组件
	 */
	@Output(value = OriginalProductSource.BINI)
	public MessageChannel bini();
    @Output(value = OriginalProductSource.NUGNES)
    public MessageChannel nugnes();

    @Output(value = OriginalProductSource.MANTOVANI)
    public MessageChannel mantovani();

}
