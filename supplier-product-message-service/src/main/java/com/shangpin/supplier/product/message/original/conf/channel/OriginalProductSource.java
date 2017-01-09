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
	
	public static final String SPINNAKER = "spinnakerOriginalProduct";
	
	public static final String OSTORE = "ostoreOriginalProduct";
	
	public static final String BRUNAROSSO = "brunarossoOriginalProduct";
	
	public static final String STEFANIA = "stefaniaOriginalProduct";
	
	public static final String GEB = "gebOriginalProduct";
	
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
}
