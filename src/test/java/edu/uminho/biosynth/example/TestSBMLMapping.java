package edu.uminho.biosynth.example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uminho.biosynth.core.components.biodb.kegg.KeggCompoundMetaboliteEntity;
import edu.uminho.biosynth.core.components.biodb.kegg.KeggReactionEntity;
import edu.uminho.biosynth.core.components.biodb.mnx.MnxMetaboliteEntity;
import edu.uminho.biosynth.core.components.biodb.mnx.components.MnxMetaboliteCrossReferenceEntity;
import edu.uminho.biosynth.core.components.biodb.mnx.components.MnxReactionCrossReferenceEntity;
import edu.uminho.biosynth.core.data.io.dao.HelperHbmConfigInitializer;
import edu.uminho.biosynth.core.data.io.dao.IGenericDao;
import edu.uminho.biosynth.core.data.io.dao.hibernate.GenericEntityDaoImpl;
//import edu.uminho.biosynth.optflux.ContainerLoader;
//import edu.uminho.biosynth.optflux.parser.DefaultSbmlTransformerImpl;
import edu.uminho.biosynth.util.BioSynthUtilsIO;

public class TestSBMLMapping {

	private static String[] cpdMap = { "14glucan", "1-4-alpha-D-Glucan",
		"appl", "1-AMINO-PROPAN-2-OL",
		"23dhmp", "1-KETO-2-METHYLVALERATE",
		"10fthf", "10-FORMYL-THF",
		"23dhdp", "2-3-DIHYDRODIPICOLINATE",
		"23dhb", "2-3-DIHYDROXYBENZOATE",
		"dhpppn", "2-3-DIHYDROXYPHENYL-PROPIONATE",
		"2tpr3dpcoa", "2-5-TRIPHOSPHORIBOSYL-3-DEPHOSPHO-",
		"alac-S", "2-ACETO-LACTATE",
		"2amsa", "2-AMINOMALONATE-SEMIALDEHYDE",
		"2me4p", "2-C-METHYL-D-ERYTHRITOL-4-PHOSPHATE",
		"3c2hmp", "2-D-THREO-HYDROXY-3-CARBOXY-ISOCAPROATE",
		"2dh3dgal", "2-DEHYDRO-3-DEOXY-D-GALACTONATE",
		"2ddglcn", "2-DEHYDRO-3-DEOXY-D-GLUCONATE",
		"2dhp", "2-DEHYDROPANTOATE",
		"tpalm2eACP", "2-Hexadecenoyl-ACPs",
		"2ddg6p", "2-KETO-3-DEOXY-6-P-GLUCONATE",
		"3mop", "2-KETO-3-METHYL-VALERATE",
		"3mob", "2-KETO-ISOVALERATE",
		"akg", "2-KETOGLUTARATE",
		"manglyc", "2-O-ALPHA-MANNOSYL-D-GLYCERATE",
		"2ohph", "2-OCTAPRENYL-6-HYDROXYPHENOL",
		"2omph", "2-OCTAPRENYL-6-METHOXYPHENOL",
		"2oph", "2-OCTAPRENYLPHENOL",
		"toct2eACP", "2-Octenoyl-ACPs",
		"2obut", "2-OXOBUTANOATE",
		"2pg", "2-PG",
		"2p4c2me", "2-PHOSPHO-4-CYTIDINE-5-DIPHOSPHO-2-C-MET",
		"orn", "25-DIAMINOPENTANOATE",
		"25dkglcn", "25-DIDEHYDRO-D-GLUCONATE",
		"2mecdp", "2C-METH-D-ERYTHRITOL-CYCLODIPHOSPHATE",
		"4mop", "2K-4CH3-PENTANOATE",
		"pap", "3-5-ADP",
		"3c3hmp", "3-CARBOXY-3-HYDROXY-ISOCAPROATE",
		"3dhsk", "3-DEHYDRO-SHIKIMATE",
		"2dda7p", "3-DEOXY-D-ARABINO-HEPTULOSONATE-7-P",
		"3psme", "3-ENOLPYRUVYL-SHIKIMATE-5P",
		"3hoctACP", "3-Hydroxy-octanoyl-ACPs",
		"3hpp", "3-HYDROXY-PROPIONATE",
		"3hadpcoa", "3-HYDROXYADIPYL-COA",
		"3hpppn", "3-HYDROXYPHENYL-PROPIONATE",
		"oxadpcoa", "3-KETO-ADIPYL-COA",
		"3dhguln", "3-KETO-L-GULONATE",
		"acac", "3-KETOBUTYRATE",
		"mercppyr", "3-MERCAPTO-PYRUVATE",
		"3ophb", "3-OCTAPRENYL-4-HYDROXYBENZOATE",
		"3ocvac11eACP", "3-oxo-cis-vaccenoyl-ACPs",
		"3odecACP", "3-oxo-decanoyl-ACPs",
		"3oddecACP", "3-oxo-dodecanoyl-ACPs",
		"3ohexACP", "3-oxo-hexanoyl-ACPs",
		"3omrsACP", "3-oxo-myristoyl-ACPs",
		"3ooctACP", "3-Oxo-octanoyl-ACPs",
		"3opalmACP", "3-oxo-palmitoyl-ACPs",
		"3ooctdACP", "3-oxo-stearoyl-ACPs",
		"3ohdcoa", "3-OXOPALMITOYL-COA",
		"3php", "3-P-HYDROXYPYRUVATE",
		"pser-L", "3-P-SERINE",
		"pppn", "3-PHENYLPROPIONATE",
		"3sala", "3-SULFINOALANINE",
		"34dhpac", "34-DIHYDROXYPHENYLACETALDEHYDE",
		"ohpb", "3OH-4P-OH-ALPHA-KETOBUTYRATE",
		"4adcho", "4-AMINO-4-DEOXYCHORISMATE",
		"4abutn", "4-AMINO-BUTYRALDEHYDE",
		"4abut", "4-AMINO-BUTYRATE",
		"4c2me", "4-CYTIDINE-5-DIPHOSPHO-2-C",
		"4h2opntn", "4-HYDROXY-2-KETOVALERATE",
		"4hbz", "4-hydroxybenzoate",
		"4ppan", "4-P-PANTOTHENATE",
		"phthr", "4-PHOSPHONOOXY-THREONINE",
		"methf", "5-10-METHENYL-THF",
		"5aop", "5-AMINO-LEVULINATE",
		"5dglcn", "5-DEHYDROGLUCONATE",
		"5fthf", "5-FORMYL-THF",
		"5dh4dglc", "5-KETO-4-DEOXY-D-GLUCARATE",
		"5mthf", "5-METHYL-THF",
		"5mta", "5-METHYLTHIOADENOSINE",
		"pram", "5-P-BETA-D-RIBOSYL-AMINE",
		"fgam", "5-P-RIBOSYL-N-FORMYLGLYCINEAMIDE",
		"gar", "5-PHOSPHO-RIBOSYL-GLYCINEAMIDE",
		"air", "5-PHOSPHORIBOSYL-5-AMINOIMIDAZOLE",
		"fpram", "5-PHOSPHORIBOSYL-N-FORMYLGLYCINEAMIDINE",
		"dhpt", "7-8-DIHYDROPTEROATE",
		"8aonn", "8-AMINO-7-OXONONANOATE",
		"ac", "ACET",
		"acald", "ACETALD",
		"actACP", "Acetoacetyl-ACPs",
		"aacoa", "ACETOACETYL-COA",
		"acetol", "ACETOL",
		"acACP", "ACETYL-ACP",
		"accoa", "ACETYL-COA",
		"unaga", "ACETYL-D-GLUCOSAMINYLDIPHOSPHO-UNDECAPRE",
		"acglu", "ACETYL-GLU",
		"actp", "ACETYL-P",
		"acmalt", "ACETYLMALTOSE",
		"acser", "ACETYLSERINE",
		"ACP", "ACP",
		"ade", "ADENINE",
		"adn", "ADENOSINE",
		"ahcys", "ADENOSYL-HOMO-CYS",
		"ap4a", "ADENOSYL-P4",
		"adocbl", "ADENOSYLCOBALAMIN",
		"adocbi", "ADENOSYLCOBINAMIDE",
		"agdpcbi", "ADENOSYLCOBINAMIDE-GDP",
		"adocbip", "ADENOSYLCOBINAMIDE-P",
		"dcamp", "ADENYLOSUCC",
		"adp", "ADP",
		"arbtn-fe3", "AEROBACTIN",
		"agm", "AGMATHINE",
		"aicar", "AICAR",
		"trnaala", "ALA-tRNAs",
		"alltt", "ALLANTOATE",
		"alltn", "ALLANTOIN",
		"all-D", "ALLOSE",
		"rdmbzi", "ALPHA-RIBAZOLE",
		"5prdmbz", "ALPHA-RIBAZOLE-5-P",
		"aact", "AMINO-ACETONE",
		"4ampm", "AMINO-HYDROXYMETHYL-METHYL-PYR-P",
		"2mahmp", "AMINO-HYDROXYMETHYL-METHYLPYRIMIDINE-PP",
		"2aobut", "AMINO-OXOBUT",
		"4r5au", "AMINO-RIBOSYLAMINO-1H-3H-PYR-DIONE",
		"nh4", "AMMONIUM",
		"amp", "AMP",
		"anth", "ANTHRANILATE",
		"aps", "APS",
		"ara5p", "ARABINOSE-5P",
		"arg-L", "ARG",
		"trnaarg", "ARG-tRNAs",
		"aso4", "ARSENATE",
		"ascb-L", "ASCORBATE",
		"asn-L", "ASN",
		"trnaasn", "ASN-tRNAs",
		"trnaasp", "ASP-tRNAs",
		"atp", "ATP",
		"ala-B", "B-ALANINE",
		"3hdecACP", "Beta-hydroxydecanoyl-ACPs",
		"glyb", "BETAINE",
		"betald", "BETAINE_ALDEHYDE",
		"btn", "BIOTIN",
		"lipidAds", "BISOHMYR-GLC",
		"lipidX", "BISOHMYR-GLUCOSAMINYL-1P",
		"btal", "BUTANAL",
		"butACP", "Butanoyl-ACPs",
		"but", "BUTYRIC_ACID",
		"btcoa", "BUTYRYL-COA",
		"ugmda", "C1",
		"uagmda", "C5",
		"unagamu", "C55-PP-GLCNAC-MANNACA",
		"unagamuf", "C55-PP-GLCNAC-MANNACA-FUC4NAC",
		"uaagmda", "C6",
		"ca2", "CA+2",
		"15dap", "CADAVERINE",
		"camp", "CAMP",
		"cbp", "CARBAMOYL-P",
		"cbasp", "CARBAMYUL-L-ASPARTATE",
		"co2", "CARBON-DIOXIDE",
		"cechddd", "CARBOXYETHYL-3-5-CYCLOHEXADIENE-1-2-DIOL",
		"2cpr5p", "CARBOXYPHENYLAMINO-DEOXYRIBULOSE-P",
		"crn", "CARNITINE",
		"cd2", "CD+2",
		"cdp", "CDP",
		"35cgmp", "CGMP",
		"dad-5", "CH33ADO",
		"alatrna", "Charged-ALA-tRNAs",
		"argtrna", "Charged-ARG-tRNAs",
		"asntrna", "Charged-ASN-tRNAs",
		"asptrna", "Charged-ASP-tRNAs",
		"cystrna", "Charged-CYS-tRNAs",
		"glntrna", "Charged-GLN-tRNAs",
		"glutrna", "Charged-GLT-tRNAs",
		"glytrna", "Charged-GLY-tRNAs",
		"histrna", "Charged-HIS-tRNAs",
		"iletrna", "Charged-ILE-tRNAs",
		"leutrna", "Charged-LEU-tRNAs",
		"lystrna", "Charged-LYS-tRNAs",
		"mettrna", "Charged-MET-tRNAs",
		"phetrna", "Charged-PHE-tRNAs",
		"protrna", "Charged-PRO-tRNAs",
		"sectrna", "Charged-SEC-tRNAs",
		"sertrna", "Charged-SER-tRNAs",
		"thrtrna", "Charged-THR-tRNAs",
		"trptrna", "Charged-TRP-tRNAs",
		"tyrtrna", "Charged-TYR-tRNAs",
		"valtrna", "Charged-VAL-tRNAs",
		"chol", "CHOLINE",
		"chor", "CHORISMATE",
		"acon-C", "CIS-ACONITATE",
		"cit", "CIT",
		"cl", "CL-",
		"cmp", "CMP",
		"ckdo", "CMP-KDO",
		"coa", "CO-A",
		"cobalt2", "CO+2",
		"cbl1", "COB-I-ALAMIN",
		"cbi", "COBINAMIDE",
		"cpppg3", "COPROPORPHYRINOGEN_III",
		"sulfac", "CPD-10246",
		"3oodcoa", "CPD-10260",
		"od2coa", "CPD-10262",
		"dcacoa", "CPD-10267",
		"3otdcoa", "CPD-10284",
		"ethso3", "CPD-10434",
		"dhcinnm", "CPD-10796",
		"3hcinnm", "CPD-10797",
		"5aprbu", "CPD-1086",
		"urdglyc", "CPD-1091",
		"dxyl", "CPD-1093",
		"2mcacn", "CPD-1136",
		"arbt6p", "CPD-1162",
		"enlipa", "CPD-11653",
		"2dmmql8", "CPD-12115",
		"malt6p", "CPD-1244",
		"udpg", "CPD-12575",
		"cdpdodecg", "CPD-12814",
		"ps180", "CPD-12816",
		"ps160", "CPD-12817",
		"pe180", "CPD-12818",
		"pe160", "CPD-12819",
		"pgp180", "CPD-12820",
		"pgp160", "CPD-12821",
		"pg180", "CPD-12822",
		"clpn160", "CPD-12824",
		"cenchddd", "CPD-13034",
		"23dhmb", "CPD-13357",
		"dtdp4addg", "CPD-14020",
		"udpgal", "CPD-14553",
		"2ahbut", "CPD-15103",
		"aacald", "CPD-1772",
		"23dappa", "CPD-1782",
		"octa", "CPD-195",
		"occoa", "CPD-196",
		"phaccoa", "CPD-207",
		"met-D", "CPD-218",
		"acon-T", "CPD-225",
		"3dhgulnp", "CPD-2343",
		"6pgc", "CPD-2961",
		"mobd", "CPD-3",
		"rbflvrd", "CPD-316",
		"23doguln", "CPD-334",
		"n8aspmd", "CPD-3462",
		"lald-D", "CPD-358",
		"dca", "CPD-3617",
		"3amp", "CPD-3706",
		"23camp", "CPD-3707",
		"3gmp", "CPD-3708",
		"23cgmp", "CPD-3709",
		"3cmp", "CPD-3711",
		"23ccmp", "CPD-3713",
		"3ump", "CPD-3724",
		"23cump", "CPD-3725",
		"tyrp", "CPD-3728",
		"butso3", "CPD-3744",
		"isetac", "CPD-3745",
		"mso3", "CPD-3746",
		"oxur", "CPD-389",
		"mmet", "CPD-397",
		"sucarg", "CPD-421",
		"dmpp", "CPD-4211",
		"acg5sa", "CPD-469",
		"1pyr5c", "CPD-478",
		"acglc-D", "CPD-522",
		"glyc2p", "CPD-536",
		"g3pi", "CPD-541",
		"Sfglutth", "CPD-548",
		"rhcys", "CPD-564",
		"N1aspmd", "CPD-568",
		"5apru", "CPD-602",
		"gp4g", "CPD-609",
		"micit", "CPD-618",
		"23dhba", "CPD-62",
		"mal-D", "CPD-660",
		"ppal", "CPD-665",
		"2pglyc", "CPD-67",
		"cinnm", "CPD-674",
		"cynt", "CPD-69",
		"3c4mop", "CPD-7100",
		"btnso", "CPD-722",
		"dd2coa", "CPD-7222",
		"aso3", "CPD-763",
		"dms", "CPD-7670",
		"ttdca", "CPD-7836",
		"sucgsa", "CPD-822",
		"pg160", "CPD-8260",
		"1agpe160", "CPD-8353",
		"2agpg160", "CPD-8363",
		"12ppd-R", "CPD-8891",
		"gg4abut", "CPD-9000",
		"5caiz", "CPD-9005",
		"hdcea", "CPD-9245",
		"2ippm", "CPD-9451",
		"udcpp", "CPD-9646",
		"udcpdp", "CPD-9649",
		"mqn8", "CPD-9728",
		"q8h2", "CPD-9956",
		"man6pglyc", "CPD0-1063",
		"galctn-L", "CPD0-1083",
		"malthp", "CPD0-1133",
		"ap5a", "CPD0-1137",
		"LalaLglu", "CPD0-1445",
		"uama", "CPD0-1456",
		"g3ps", "CPD0-2030",
		"3oddcoa", "CPD0-2105",
		"3oocoa", "CPD0-2106",
		"3hddcoa", "CPD0-2107",
		"oc2coa", "CPD0-2108",
		"1odecg3p", "CPD0-2113",
		"fe3hox", "CPD0-2114",
		"hdd2coa", "CPD0-2117",
		"td2coa", "CPD0-2120",
		"hx2coa", "CPD0-2121",
		"3odcoa", "CPD0-2123",
		"feoxam", "CPD0-2124",
		"gdpofuc", "CPD0-2128",
		"2agpg180", "CPD0-2133",
		"eca2und", "CPD0-2135",
		"eca4und", "CPD0-2138",
		"eca3und", "CPD0-2139",
		"1agpg180", "CPD0-2140",
		"1agpg140", "CPD0-2143",
		"1agpg120", "CPD0-2144",
		"1agpe140", "CPD0-2147",
		"1agpe120", "CPD0-2148",
		"1agpe180", "CPD0-2152",
		"uLa4fn", "CPD0-2153",
		"anhgm3p", "CPD0-2155",
		"1agpg160", "CPD0-2162",
		"2agpe140", "CPD0-2165",
		"3htdcoa", "CPD0-2171",
		"2agpg140", "CPD0-2173",
		"fe3hox-un", "CPD0-2175",
		"2agpe160", "CPD0-2177",
		"2agpe120", "CPD0-2178",
		"progly", "CPD0-2182",
		"4hthr", "CPD0-2189",
		"LalaDglu", "CPD0-2190",
		"apg140", "CPD0-2192",
		"apg120", "CPD0-2194",
		"apg180", "CPD0-2195",
		"apg160", "CPD0-2197",
		"1ddecg3p", "CPD0-2200",
		"cpg160", "CPD0-2202",
		"fecrm-un", "CPD0-2205",
		"murein5p5p5p", "CPD0-2213",
		"o16a4und", "CPD0-2214",
		"garagund", "CPD0-2215",
		"cpe160", "CPD0-2216",
		"cpg180", "CPD0-2217",
		"cpe180", "CPD0-2218",
		"gfgaragund", "CPD0-2219",
		"2agpe180", "CPD0-2223",
		"3hocoa", "CPD0-2224",
		"pgp161", "CPD0-2230",
		"didp", "CPD0-2231",
		"3hhdcoa", "CPD0-2232",
		"hphhlipa", "CPD0-2238",
		"phphhlipa", "CPD0-2239",
		"phhlipa", "CPD0-2240",
		"fecrm", "CPD0-2241",
		"3hdcoa", "CPD0-2244",
		"murein5p4p", "CPD0-2245",
		"gicolipa", "CPD0-2247",
		"o16a4colipa", "CPD0-2249",
		"3hodcoa", "CPD0-2253",
		"1tdecg3p", "CPD0-2254",
		"LalaDgluMdapDala", "CPD0-2256",
		"kphphhlipa", "CPD0-2257",
		"gagicolipa", "CPD0-2258",
		"gggagicolipa", "CPD0-2259",
		"ggagicolipa", "CPD0-2261",
		"cpgn-un", "CPD0-2262",
		"hacolipa", "CPD0-2264",
		"kdo2lipid4p", "CPD0-2265",
		"murein4px4p4p", "CPD0-2266",
		"o16a2und", "CPD0-2268",
		"murein4p4p", "CPD0-2269",
		"colipa", "CPD0-2271",
		"murein3p3p", "CPD0-2272",
		"murein3px4p", "CPD0-2273",
		"murein4p3p", "CPD0-2274",
		"murein3px3p", "CPD0-2277",
		"murein5px4p", "CPD0-2278",
		"o16aund", "CPD0-2279",
		"o16a3und", "CPD0-2280",
		"murein5p5p", "CPD0-2283",
		"anhm3p", "CPD0-2284",
		"anhm4p", "CPD0-2285",
		"murein5p3p", "CPD0-2287",
		"ragund", "CPD0-2289",
		"aragund", "CPD0-2290",
		"anhgm4p", "CPD0-2292",
		"acolipa", "CPD0-2293",
		"eca4colipa", "CPD0-2294",
		"icolipa", "CPD0-2295",
		"murein5px4px4p", "CPD0-2296",
		"pg161", "CPD0-2330",
		"fe3dhbzs", "CPD0-2482",
		"cpgn", "CPD0-621",
		"hlipa", "CPD0-929",
		"hhlipa", "CPD0-930",
		"ctbt", "CROTONO-BETAINE",
		"ctbtcoa", "CROTONOBETAINYL-COA",
		"b2coa", "CROTONYL-COA",
		"ctp", "CTP",
		"cu", "CU+",
		"cu2", "CU+2",
		"malt", "CXXXXX",
		"man6p", "CXXXXX",
		"adprib", "CXXXXX",
		"icit", "CXXXXX",
		"fdp", "CXXXXX",
		"adpglc", "CXXXXX",
		"2dr1p", "CXXXXX",
		"f1p", "CXXXXX",
		"octeACP", "CXXXXX",
		"seln", "CXXXXX",
		"2mcit", "CXXXXX",
		"5mtr", "CXXXXX",
		"acg5p", "CXXXXX",
		"but2eACP", "CXXXXX",
		"hkndd", "CXXXXX",
		"3hpalmACP", "CXXXXX",
		"glcur1p", "CXXXXX",
		"tdec2eACP", "CXXXXX",
		"myrsACP", "CXXXXX",
		"hkntd", "CXXXXX",
		"3hoctaACP", "CXXXXX",
		"acmum6p", "CXXXXX",
		"gbbtn", "CXXXXX",
		"metsox-R-L", "CXXXXX",
		"2dhguln", "CXXXXX",
		"fe3dcit", "CXXXXX",
		"6hmhpt", "CXXXXX",
		"hmgth", "CXXXXX",
		"udpLa4o", "CXXXXX",
		"5drib", "CXXXXX",
		"frulysp", "CXXXXX",
		"dsbgrd", "CXXXXX",
		"dsbard", "CXXXXX",
		"dsbcrd", "CXXXXX",
		"ag", "CXXXXX",
		"tdeACP", "CXXXXX",
		"pg140", "CXXXXX",
		"r5p", "CXXXXX",
		"pg120", "CXXXXX",
		"pg181", "CXXXXX",
		"pe161", "CXXXXX",
		"palmACP", "CXXXXX",
		"pg141", "CXXXXX",
		"pe181", "CXXXXX",
		"g3p", "CXXXXX",
		"pe120", "CXXXXX",
		"pe140", "CXXXXX",
		"12dgr140", "CXXXXX",
		"anhgm", "CXXXXX",
		"12dgr160", "CXXXXX",
		"cdpdhdecg", "CXXXXX",
		"2agpe141", "CXXXXX",
		"2agpe161", "CXXXXX",
		"2agpe181", "CXXXXX",
		"2agpg120", "CXXXXX",
		"2agpg141", "CXXXXX",
		"2agpg161", "CXXXXX",
		"2agpg181", "CXXXXX",
		"2ommbl", "CXXXXX",
		"pe141", "CXXXXX",
		"12dgr161", "CXXXXX",
		"12dgr181", "CXXXXX",
		"12dgr180", "CXXXXX",
		"clpn161", "CXXXXX",
		"clpn181", "CXXXXX",
		"cdpdddecg", "CXXXXX",
		"cdpdhdec9eg", "CXXXXX",
		"cdpdodec11eg", "CXXXXX",
		"cdpdtdec7eg", "CXXXXX",
		"cdpdtdecg", "CXXXXX",
		"12dgr120", "CXXXXX",
		"12dgr141", "CXXXXX",
		"clpn120", "CXXXXX",
		"clpn180", "CXXXXX",
		"murein4px4p", "CXXXXX",
		"3hcddec5eACP", "CXXXXX",
		"3hcmrs7eACP", "CXXXXX",
		"ggptrc", "CXXXXX",
		"2ddecg3p", "CXXXXX",
		"2hdec9eg3p", "CXXXXX",
		"2hdecg3p", "CXXXXX",
		"2odec11eg3p", "CXXXXX",
		"2odecg3p", "CXXXXX",
		"2tdec7eg3p", "CXXXXX",
		"2tdecg3p", "CXXXXX",
		"hdcoa", "CXXXXX",
		"cddec5eACP", "CXXXXX",
		"t3c11vaceACP", "CXXXXX",
		"t3c5ddeceACP", "CXXXXX",
		"t3c7mrseACP", "CXXXXX",
		"t3c9palmeACP", "CXXXXX",
		"murein5px3p", "CXXXXX",
		"acgal1p", "CXXXXX",
		"murein4px4px4p", "CXXXXX",
		"ocdcea", "CXXXXX",
		"3hcpalm9eACP", "CXXXXX",
		"3hcvac11eACP", "CXXXXX",
		"1agpe141", "CXXXXX",
		"1agpe161", "CXXXXX",
		"1agpg141", "CXXXXX",
		"1agpg161", "CXXXXX",
		"1agpg181", "CXXXXX",
		"3ocddec5eACP", "CXXXXX",
		"3ocmrs7eACP", "CXXXXX",
		"3ocpalm9eACP", "CXXXXX",
		"apg141", "CXXXXX",
		"apg161", "CXXXXX",
		"apg181", "CXXXXX",
		"adphep-DD", "CXXXXX",
		"adphep-LD", "CXXXXX",
		"clpn141", "CXXXXX",
		"clpn140", "CXXXXX",
		"cdec3eACP", "CXXXXX",
		"feenter", "CXXXXX",
		"bglycogen", "CXXXXX",
		"ttdcea", "CXXXXX",
		"tdecoa", "CXXXXX",
		"dsbdrd", "CXXXXX",
		"zn2", "CXXXXX",
		"sbzcoa", "CXXXXX",
		"hdeACP", "CXXXXX",
		"LalaDgluMdap", "CXXXXX",
		"pa160", "CXXXXX",
		"pa180", "CXXXXX",
		"pa140", "CXXXXX",
		"pa120", "CXXXXX",
		"pa161", "CXXXXX",
		"pa181", "CXXXXX",
		"pa141", "CXXXXX",
		"anhm", "CXXXXX",
		"1hdecg3p", "CXXXXX",
		"23dhbzs", "CXXXXX",
		"grxrd", "CXXXXX",
		"feoxam-un", "CXXXXX",
		"grxox", "CXXXXX",
		"uLa4n", "CXXXXX",
		"tagdp-D", "CXXXXX",
		"1hdec9eg3p", "CXXXXX",
		"1odec11eg3p", "CXXXXX",
		"1tdec7eg3p", "CXXXXX",
		"6hmhptpp", "CXXXXX",
		"pgp120", "CXXXXX",
		"pgp181", "CXXXXX",
		"pgp141", "CXXXXX",
		"pgp140", "CXXXXX",
		"alpp", "CXXXXX",
		"ps120", "CXXXXX",
		"ps161", "CXXXXX",
		"ps181", "CXXXXX",
		"ps141", "CXXXXX",
		"ps140", "CXXXXX",
		"trnasecys", "CXXXXX",
		"1agpe181", "CXXXXX",
		"arbtn", "CXXXXX",
		"dsbdox", "CXXXXX",
		"dsbgox", "CXXXXX",
		"dsbaox", "CXXXXX",
		"dsbcox", "CXXXXX",
		"pheme", "CXXXXX",
		"14dhncoa", "CXXXXX",
		"23dhacoa", "CXXXXX",
		"2fe1s", "CXXXXX",
		"2fe2s", "CXXXXX",
		"2oxpaccoa", "CXXXXX",
		"2sephchc", "CXXXXX",
		"3amac", "CXXXXX",
		"3fe4s", "CXXXXX",
		"3oxdhscoa", "CXXXXX",
		"4crsol", "CXXXXX",
		"4fe4s", "CXXXXX",
		"arbt", "CXXXXX",
		"athtp", "CXXXXX",
		"bmoco", "CXXXXX",
		"bmoco1gdp", "CXXXXX",
		"bmocogdp", "CXXXXX",
		"bwco", "CXXXXX",
		"bwco1gdp", "CXXXXX",
		"bwcogdp", "CXXXXX",
		"cbm", "CXXXXX",
		"cdg", "CXXXXX",
		"chtbs", "CXXXXX",
		"chtbs6p", "CXXXXX",
		"cm", "CXXXXX",
		"colipap", "CXXXXX",
		"cph4", "CXXXXX",
		"cpmp", "CXXXXX",
		"ddcap", "CXXXXX",
		"dgslnt", "CXXXXX",
		"dhgly", "CXXXXX",
		"dhmpt", "CXXXXX",
		"dhptdn", "CXXXXX",
		"doxrbcn", "CXXXXX",
		"egmeACP", "CXXXXX",
		"epmeACP", "CXXXXX",
		"flxr", "CXXXXX",
		"flxso", "CXXXXX",
		"fusa", "CXXXXX",
		"ghb", "CXXXXX",
		"gmeACP", "CXXXXX",
		"gslnt", "CXXXXX",
		"hdcap", "CXXXXX",
		"hdceap", "CXXXXX",
		"hgmeACP", "CXXXXX",
		"hpmeACP", "CXXXXX",
		"iscs", "CXXXXX",
		"iscssh", "CXXXXX",
		"iscu", "CXXXXX",
		"iscu-2fe2s", "CXXXXX",
		"iscu-2fe2s2", "CXXXXX",
		"iscu-4fe4s", "CXXXXX",
		"lipoamp", "CXXXXX",
		"lipopb", "CXXXXX",
		"malcoame", "CXXXXX",
		"mdhdhf", "CXXXXX",
		"mincyc", "CXXXXX",
		"moadamp", "CXXXXX",
		"moadcoo", "CXXXXX",
		"moadcosh", "CXXXXX",
		"moco", "CXXXXX",
		"mococdp", "CXXXXX",
		"mocogdp", "CXXXXX",
		"mpt", "CXXXXX",
		"mptamp", "CXXXXX",
		"mththf", "CXXXXX",
		"na15dap", "CXXXXX",
		"novbcn", "CXXXXX",
		"ocdcap", "CXXXXX",
		"ocdceap", "CXXXXX",
		"octapb", "CXXXXX",
		"ogmeACP", "CXXXXX",
		"opmeACP", "CXXXXX",
		"pimACP", "CXXXXX",
		"pmeACP", "CXXXXX",
		"poaac", "CXXXXX",
		"preq0", "CXXXXX",
		"preq1", "CXXXXX",
		"quin", "CXXXXX",
		"rephaccoa", "CXXXXX",
		"rfamp", "CXXXXX",
		"sertrna_LSQBKT_sec_RSQBKT_", "CXXXXX",
		"slnt", "CXXXXX",
		"sufbcd", "CXXXXX",
		"sufbcd-2fe2s", "CXXXXX",
		"sufbcd-2fe2s2", "CXXXXX",
		"sufbcd-4fe4s", "CXXXXX",
		"sufse", "CXXXXX",
		"sufsesh", "CXXXXX",
		"tartr-D", "CXXXXX",
		"thmnp", "CXXXXX",
		"thptdn", "CXXXXX",
		"ttdcap", "CXXXXX",
		"ttdceap", "CXXXXX",
		"ttrcyc", "CXXXXX",
		"uracp", "CXXXXX",
		"wco", "CXXXXX",
		"cys-L", "CYS",
		"cgly", "CYS-GLY",
		"trnacys", "CYS-tRNAs",
		"cytd", "CYTIDINE",
		"csn", "CYTOSINE",
		"6pgl", "D-6-P-GLUCONO-DELTA-LACTONE",
		"alaala", "D-ALA-D-ALA",
		"ala-D", "D-ALANINE",
		"all6p", "D-ALLOSE-6-PHOSPHATE",
		"allul6p", "D-ALLULOSE-6-PHOSPHATE",
		"gmhep7p", "D-ALPHABETA-D-HEPTOSE-7-PHOSPHATE",
		"altrn", "D-ALTRONATE",
		"gmhep1p", "D-BETA-D-HEPTOSE-1-P",
		"gmhep17bp", "D-BETA-D-HEPTOSE-17-DIPHOSPHATE",
		"crn-D", "D-CARNITINE",
		"crnDcoa", "D-CARNITINYL-COA",
		"cys-D", "D-CYSTEINE",
		"eig3p", "D-ERYTHRO-IMIDAZOLE-GLYCEROL-P",
		"galct-D", "D-GALACTARATE",
		"galctn-D", "D-GALACTONATE",
		"gal", "D-Galactose",
		"galur", "D-GALACTURONATE",
		"glu-D", "D-GLT",
		"glcr", "D-GLUCARATE",
		"gam6p", "D-GLUCOSAMINE-6-P",
		"glc-D", "D-Glucose",
		"g6p", "D-glucose-6-phosphate",
		"lac-D", "D-LACTATE",
		"mana", "D-MANNONATE",
		"mmcoa-S", "D-METHYL-MALONYL-COA",
		"mi1p-D", "D-MYO-INOSITOL-1-MONOPHOSPHATE",
		"s17bp", "D-SEDOHEPTULOSE-1-7-P2",
		"s7p", "D-SEDOHEPTULOSE-7-P",
		"ser-D", "D-SERINE",
		"sbt6p", "D-SORBITOL-6-P",
		"tagur", "D-TAGATURONATE",
		"xyl-D", "D-Xylose",
		"xylu-D", "D-XYLULOSE",
		"dadp", "DADP",
		"damp", "DAMP",
		"datp", "DATP",
		"dcdp", "DCDP",
		"dcmp", "DCMP",
		"dctp", "DCTP",
		"dnad", "DEAMIDO-NAD",
		"dcaACP", "Decanoyl-ACPs",
		"2dh3dgal6p", "DEHYDRO-DEOXY-GALACTONATE-PHOSPHATE",
		"3dhq", "DEHYDROQUINATE",
		"thdp", "DELTA1-PIPERIDEINE-2-6-DICARBOXYLATE",
		"ipdp", "DELTA3-ISOPENTENYL-PP",
		"2dmmq8", "DEMETHYLMENAQUINONE",
		"2dr5p", "DEOXY-RIBOSE-5P",
		"dad-2", "DEOXYADENOSINE",
		"dcyt", "DEOXYCYTIDINE",
		"dgsn", "DEOXYGUANOSINE",
		"din", "DEOXYINOSINE",
		"duri", "DEOXYURIDINE",
		"dxyl5p", "DEOXYXYLULOSE-5P",
		"dpcoa", "DEPHOSPHO-COA",
		"dtbt", "DETHIOBIOTIN",
		"dgdp", "DGDP",
		"dgmp", "DGMP",
		"dgtp", "DGTP",
		"dhor-S", "DI-H-OROTATE",
		"56dura", "DI-H-URACIL",
		"25drapp", "DIAMINO-OH-PHOSPHORIBOSYLAMINO-PYR",
		"dann", "DIAMINONONANOATE",
		"23ddhb", "DIHYDRO-DIOH-BENZOATE",
		"dhnpt", "DIHYDRO-NEO-PTERIN",
		"dhf", "DIHYDROFOLATE",
		"dhmptp", "DIHYDROMONAPTERIN-TRIPHOSPHATE",
		"dhpmp", "DIHYDRONEOPTERIN-P",
		"ahdt", "DIHYDRONEOPTERIN-P3",
		"dscl", "DIHYDROSIROHYDROCHLORIN",
		"dhap", "DIHYDROXY-ACETONE-PHOSPHATE",
		"db4p", "DIHYDROXY-BUTANONE-P",
		"dha", "DIHYDROXYACETONE",
		"dhna", "DIHYDROXYNAPHTHOATE",
		"dhptd", "DIHYDROXYPENTANEDIONE",
		"dmlz", "DIMETHYL-D-RIBITYL-LUMAZINE",
		"dmbzid", "DIMETHYLBENZIMIDAZOLE",
		"dimp", "DIMP",
		"ditp", "DITP",
		"dmso", "DMSO",
		"tddec2eACP", "Dodec-2-enoyl-ACPs",
		"ddca", "DODECANOATE",
		"ddcaACP", "Dodecanoyl-ACPs",
		"dopa", "DOPAMINE",
		"13dpg", "DPG",
		"dtdp4d6dg", "DTDP-4-DEHYDRO-6-DEOXY-D-GALACTOSE",
		"dtdpglu", "DTDP-D-GLUCOSE",
		"dtdp4d6dm", "DTDP-DEOH-DEOXY-MANNOSE",
		"dtdprmn", "DTDP-RHAMNOSE",
		"dudp", "DUDP",
		"dump", "DUMP",
		"dutp", "DUTP",
		"enter", "ENTEROBACTIN",
		"4per", "ERYTHRONATE-4P",
		"e4p", "ERYTHROSE-4P",
		"etha", "ETHANOL-AMINE",
		"etoh", "ETOH",
		"fad", "FAD",
		"fadh2", "FADH2",
		"frdp", "FARNESYL-PP",
		"fe2", "FE+2",
		"fe3", "FE+3",
		"fmn", "FMN",
		"fmnh2", "FMNH2",
		"fald", "FORMALDEHYDE",
		"for", "FORMATE",
		"forcoa", "FORMYL-COA",
		"fru", "FRU",
		"f6p", "FRUCTOSE-6P",
		"frulys", "FRUCTOSELYSINE",
		"fruur", "FRUCTURONATE",
		"fc1p", "FUCULOSE-1P",
		"fum", "FUM",
		"3pg", "G3P",
		"galt", "GALACTITOL",
		"galt1p", "GALACTITOL-1-PHOSPHATE",
		"gal-bD", "GALACTOSE",
		"gal1p", "GALACTOSE-1P",
		"bbtcoa", "GAMMA-BUTYROBETAINYL-COA",
		"ggbutal", "GAMMA-GLUTAMYL-GAMMA-AMINOBUTYRALDEH",
		"gdp", "GDP",
		"gdpddman", "GDP-4-DEHYDRO-6-DEOXY-D-MANNOSE",
		"gdpmann", "GDP-MANNOSE",
		"gdptp", "GDP-TP",
		"grdp", "GERANYL-PP",
		"g1p", "GLC-1-P",
		"gln-L", "GLN",
		"trnagln", "GLN-tRNAs",
		"glu-L", "GLT",
		"trnaglu", "GLT-tRNAs",
		"glcn", "GLUCONATE",
		"gam", "GLUCOSAMINE",
		"gam1p", "GLUCOSAMINE-1P",
		"glcur", "GLUCURONATE",
		"glu1sa", "GLUTAMATE-1-SEMIALDEHYDE",
		"gthrd", "GLUTATHIONE",
		"gtspmd", "GLUTATHIONYLSPERMIDINE",
		"gly", "GLY",
		"trnagly", "GLY-tRNAs",
		"glyald", "GLYCERALD",
		"glyc-R", "GLYCERATE",
		"glyc", "GLYCEROL",
		"glyc3p", "GLYCEROL-3P",
		"g3pg", "GLYCEROPHOSPHOGLYCEROL",
		"glycogen", "Glycogens",
		"gcald", "GLYCOLALDEHYDE",
		"glyclt", "GLYCOLLATE",
		"glx", "GLYOX",
		"gmp", "GMP",
		"gtp", "GTP",
		"gua", "GUANINE",
		"gsn", "GUANOSINE",
		"gdpfuc", "GUANOSINE_DIPHOSPHATE_FUCOSE",
		"ppgpp", "GUANOSINE-5DP-3DP",
		"cyan", "HCN",
		"hco3", "HCO3",
		"hemeO", "HEME_O",
		"halipa", "HEPTA-ACYLATED-LIPID-A",
		"thex2eACP", "Hex-2-enoyl-ACPs",
		"hxa", "HEXANOATE",
		"hexACP", "Hexanoyl-ACPs",
		"hxcoa", "HEXANOYL-COA",
		"hg2", "HG+2",
		"his-L", "HIS",
		"trnahis", "HIS-tRNAs",
		"histd", "HISTIDINOL",
		"4ahmmp", "HMP",
		"hcys-L", "HOMO-CYS",
		"hom-L", "HOMO-SER",
		"h2s", "HS",
		"tcynt", "HSCN",
		"h2", "HYDROGEN-MOLECULE",
		"h2o2", "HYDROGEN-PEROXIDE",
		"hqn", "HYDROQUINONE",
		"h2mb4p", "HYDROXY-METHYL-BUTENYL-DIP",
		"hmbil", "HYDROXYMETHYLBILANE",
		"4hoxpacd", "HYDRPHENYLAC-CPD",
		"hxan", "HYPOXANTHINE",
		"idp", "IDP",
		"ile-L", "ILE",
		"trnaile", "ILE-tRNAs",
		"imacp", "IMIDAZOLE-ACETOL-P",
		"iasp", "IMINOASPARTATE",
		"imp", "IMP",
		"indole", "INDOLE",
		"3ig3p", "INDOLE-3-GLYCEROL-P",
		"ins", "INOSINE",
		"ichor", "ISOCHORISMATE",
		"itp", "ITP",
		"3ohcoa", "K-HEXANOYL-COA",
		"k", "K+",
		"kdo", "KDO",
		"kdo8p", "KDO-8P",
		"kdolipid4", "KDO-LIPID-IVA",
		"kdo2lipid4L", "KDO2-LAUROYL-LIPID-IVA",
		"lipa", "KDO2-LIPID-A",
		"kdo2lipid4", "KDO2-LIPID-IVA",
		"lipa_cold", "KDO2-LIPID-IVA-COLD",
		"g3pc", "L-1-GLYCERO-PHOSPHORYLCHOLINE",
		"g3pe", "L-1-GLYCEROPHOSPHORYLETHANOL-AMINE",
		"athr-L", "L-ALLO-THREONINE",
		"ala-L", "L-ALPHA-ALANINE",
		"arab-L", "L-ARABINOSE",
		"argsuc", "L-ARGININO-SUCCINATE",
		"ascb6p", "L-ASCORBATE-6-PHOSPHATE",
		"asp-L", "L-ASPARTATE",
		"aspsa", "L-ASPARTATE-SEMIALDEHYDE",
		"4pasp", "L-BETA-ASPARTYL-P",
		"crncoa", "L-CARNITINYL-COA",
		"citr-L", "L-CITRULLINE",
		"cyst-L", "L-CYSTATHIONINE",
		"fuc-L", "L-FUCOSE",
		"fcl-L", "L-FUCULOSE",
		"glucys", "L-GAMMA-GLUTAMYLCYSTEINE",
		"glu5sa", "L-GLUTAMATE_GAMMA-SEMIALDEHYDE",
		"glu5p", "L-GLUTAMATE-5-P",
		"hisp", "L-HISTIDINOL-P",
		"idon-L", "L-IDONATE",
		"lac-L", "L-LACTATE",
		"lyx-L", "L-LYXOSE",
		"metsox-S-L", "L-Methionine-sulfoxides",
		"pant-R", "L-PANTOATE",
		"rmn", "L-rhamnose",
		"rbl-L", "L-RIBULOSE",
		"ru5p-L", "L-RIBULOSE-5-P",
		"thrp", "L-THREONINE-O-3-PHOSPHATE",
		"xylu-L", "L-XYLULOSE",
		"xu5p-L", "L-XYLULOSE-5-P",
		"lald-L", "LACTALD",
		"lcts", "LACTOSE",
		"ddcacoa", "LAUROYLCOA-CPD",
		"leu-L", "LEU",
		"trnaleu", "LEU-tRNAs",
		"lipidA", "LIPID-IV-A",
		"lipoate", "LIPOIC-ACID",
		"lpp", "Lipoproteins",
		"26dap-LL", "LL-DIAMINOPIMELATE",
		"lys-L", "LYS",
		"trnalys", "LYS-tRNAs",
		"mal-L", "MAL",
		"msa", "MALONATE-S-ALD",
		"malACP", "MALONYL-ACP",
		"malcoa", "MALONYL-COA",
		"malthx", "MALTOHEXAOSE",
		"maltpt", "MALTOPENTAOSE",
		"maltttr", "MALTOTETRAOSE",
		"malttr", "MALTOTRIOSE",
		"mnl", "MANNITOL",
		"mnl1p", "MANNITOL-1P",
		"man", "MANNOSE",
		"man1p", "MANNOSE-1P",
		"melib", "MELIBIOSE",
		"26dap-M", "MESO-DIAMINOPIMELATE",
		"met-L", "MET",
		"trnamet", "MET-tRNAs",
		"mthgxl", "METHYL-GLYOXAL",
		"mlthf", "METHYLENE-THF",
		"meoh", "METOH",
		"mg2", "MG+2",
		"minohp", "MI-HEXAKISPHOSPHATE",
		"mn2", "MN+2",
		"aconm", "MONOMETHYL-ESTER-OF-TRANS-ACONITATE",
		"apoACP", "Myelin-proteolipids",
		"inost", "MYO-INOSITOL",
		"pran", "N-5-PHOSPHORIBOSYL-ANTHRANILATE",
		"acgal", "N-acetyl-D-galactosamine",
		"acgam", "N-acetyl-D-glucosamine",
		"acgam1p", "N-ACETYL-D-GLUCOSAMINE-1-P",
		"acgam6p", "N-ACETYL-D-GLUCOSAMINE-6-P",
		"acmana", "N-acetyl-D-mannosamine",
		"acmanap", "N-ACETYL-D-MANNOSAMINE-6P",
		"acanth", "N-ACETYLANTHRANILATE",
		"acnam", "N-ACETYLNEURAMINATE",
		"acorn", "N-ALPHA-ACETYLORNITHINE",
		"fmettrna", "N-formyl-L-methionyl-tRNAfmet",
		"Nmtrp", "N-METHYLTRYPTOPHAN",
		"sl2a6o", "N-SUCCINYL-2-AMINO-6-KETOPIMELATE",
		"sl26da", "N-SUCCINYLLL-2-6-DIAMINOPIMELATE",
		"sucglu", "N2-SUCCINYLGLUTAMATE",
		"sucorn", "N2-SUCCINYLORNITHINE",
		"na1", "NA+",
		"acmum", "NACMUR",
		"nad", "NAD",
		"nadh", "NADH",
		"nadp", "NADP",
		"nadph", "NADPH",
		"ni2", "NI+2",
		"ncam", "NIACINAMIDE",
		"nac", "NIACINE",
		"nmn", "NICOTINAMIDE_NUCLEOTIDE",
		"nicrnt", "NICOTINATE_NUCLEOTIDE",
		"no3", "NITRATE",
		"no", "NITRIC-OXIDE",
		"no2", "NITRITE",
		"n2o", "NITROUS-OXIDE",
		"phom", "O-PHOSPHO-L-HOMOSERINE",
		"suchms", "O-SUCCINYL-L-HOMOSERINE",
		"sucbz", "O-SUCCINYLBENZOATE",
		"toctd2eACP", "Octadec-2-enoyl-ACPs",
		"ocACP", "Octanoyl-ACPs",
		"octdp", "OCTAPRENYL-DIPHOSPHATE",
		"2ombzl", "OCTAPRENYL-METHOXY-BENZOQUINONE",
		"2omhmbl", "OCTAPRENYL-METHYL-OH-METHOXY-BENZQ",
		"3haACP", "OH-ACYL-ACP",
		"3hhcoa", "OH-HEXANOYL-COA",
		"u23ga", "OH-MYRISTOYL",
		"hpyr", "OH-PYR",
		"odecoa", "OLEOYL-COA",
		"orot", "OROTATE",
		"orot5p", "OROTIDINE-5-PHOSPHATE",
		"trdox", "Ox-Thioredoxin",
		"oaa", "OXALACETIC_ACID",
		"oxa", "OXALATE",
		"oxalcoa", "OXALYL-COA",
		"oxam", "OXAMATE",
		"gthox", "OXIDIZED-GLUTATHIONE",
		"op4en", "OXOPENTENOATE",
		"o2", "OXYGEN-MOLECULE",
		"4abz", "P-AMINO-BENZOATE",
		"34hpp", "P-HYDROXY-PHENYLPYRUVATE",
		"25aics", "P-RIBOSYL-4-SUCCCARB-AMINOIMIDAZOLE",
		"pppi", "P3I",
		"hdca", "PALMITATE",
		"pmtcoa", "PALMITYL-COA",
		"pan4p", "PANTETHEINE-P",
		"pnto-R", "PANTOTHENATE",
		"paps", "PAPS",
		"phe-L", "PHE",
		"trnaphe", "PHE-tRNAs",
		"phpyr", "PHENYL-PYRUVATE",
		"pacald", "PHENYLACETALDEHYDE",
		"pac", "PHENYLACETATE",
		"peamn", "PHENYLETHYLAMINE",
		"pep", "PHOSPHO-ENOL-PYRUVATE",
		"ppt", "PHOSPHONATE",
		"prbamp", "PHOSPHORIBOSYL-AMP",
		"prbatp", "PHOSPHORIBOSYL-ATP",
		"5aizc", "PHOSPHORIBOSYL-CARBOXY-AMINOIMIDAZOLE",
		"fprica", "PHOSPHORIBOSYL-FORMAMIDO-CARBOXAMIDE",
		"prfp", "PHOSPHORIBOSYL-FORMIMINO-AICAR-P",
		"prlp", "PHOSPHORIBULOSYL-FORMIMINO-AICAR-P",
		"pi", "Pi",
		"ppbng", "PORPHOBILINOGEN",
		"ppi", "PPI",
		"pphn", "PREPHENATE",
		"pro-L", "PRO",
		"trnapro", "PRO-tRNAs",
		"12ppd-S", "PROPANE-1-2-DIOL",
		"ppa", "PROPIONATE",
		"ppcoa", "PROPIONYL-COA",
		"ppap", "PROPIONYL-P",
		"h", "PROTON",
		"ppp9", "PROTOPORPHYRIN_IX",
		"pppg9", "PROTOPORPHYRINOGEN",
		"prpp", "PRPP",
		"psclys", "PSICOSELYSINE",
		"ptrc", "PUTRESCINE",
		"pydx", "PYRIDOXAL",
		"pydx5p", "PYRIDOXAL_PHOSPHATE",
		"pydam", "PYRIDOXAMINE",
		"pyam5p", "PYRIDOXAMINE-5P",
		"pydxn", "PYRIDOXINE",
		"pdx5p", "PYRIDOXINE-5P",
		"pyr", "PYRUVATE",
		"quln", "QUINOLINATE",
		"3hddecACP", "R-3-hydroxydodecanoyl-ACPs",
		"3hhexACP", "R-3-hydroxyhexanoyl-ACPs",
		"3hmrsACP", "R-3-hydroxymyristoyl-ACPs",
		"4ppcys", "R-4-PHOSPHOPANTOTHENOYL-L-CYSTEINE",
		"trdrd", "Red-Thioredoxin",
		"mql8", "REDUCED-MENAQUINONE",
		"rml", "RHAMNULOSE",
		"rml1p", "RHAMNULOSE-1P",
		"ribflv", "RIBOFLAVIN",
		"rib-D", "RIBOSE",
		"r15bp", "RIBOSE-15-BISPHOSPHATE",
		"r1p", "RIBOSE-1P",
		"ru5p-D", "RIBULOSE-5P",
		"3hbcoa", "S-3-HYDROXYBUTANOYL-COA",
		"amob", "S-ADENOSYL-4-METHYLTHIO-2-OXOBUTANOATE",
		"ametam", "S-ADENOSYLMETHIONINAMINE",
		"amet", "S-ADENOSYLMETHIONINE",
		"lgt-S", "S-LACTOYL-GLUTATHIONE",
		"tsul", "S2O3",
		"sarcs", "SARCOSINE",
		"sel", "SELENATE",
		"selnp", "SEPO3",
		"ser-L", "SER",
		"trnaser", "SER-tRNAs",
		"seramp", "SERYL-AMP",
		"skm", "SHIKIMATE",
		"skm5p", "SHIKIMATE-5P",
		"sheme", "SIROHEME",
		"scl", "SIROHYDROCHLORIN",
		"so3", "SO3",
		"sbt-D", "SORBITOL",
		"spmd", "SPERMIDINE",
		"ocdca", "STEARIC_ACID",
		"ocdcaACP", "Stearoyl-ACPs",
		"stcoa", "STEAROYL-COA",
		"succ", "SUC",
		"succoa", "SUC-COA",
		"sucsal", "SUCC-S-ALD",
		"2shchc", "SUCCINYL-OH-CYCLOHEXADIENE-COOH",
		"sucr", "SUCROSE",
		"suc6p", "SUCROSE-6P",
		"so4", "SULFATE",
		"so2", "SULFUR-DIOXIDE",
		"o2s", "SUPER-OXIDE",
		"dc2coa", "T2-DECENOYL-COA",
		"tag6p-D", "TAGATOSE-6-PHOSPHATE",
		"tartr-L", "TARTRATE",
		"2h3oppan", "TARTRONATE-S-ALD",
		"taur", "TAURINE",
		"dtdp", "TDP",
		"dtdp4aaddg", "TDP-FUC4NAC",
		"tmrs2eACP", "Tetradec-2-enoyl-ACPs",
		"tdcoa", "TETRADECANOYL-COA",
		"thf", "THF",
		"thm", "THIAMINE",
		"thmmp", "THIAMINE-P",
		"thmpp", "THIAMINE-PYROPHOSPHATE",
		"thr-L", "THR",
		"trnathr", "THR-tRNAs",
		"thymd", "THYMIDINE",
		"thym", "THYMINE",
		"4mhetz", "THZ",
		"4mpetz", "THZ-P",
		"dtmp", "TMP",
		"tre", "TREHALOSE",
		"tre6p", "TREHALOSE-6P",
		"tmao", "TRIMENTHLAMINE-N-O",
		"tma", "TRIMETHYLAMINE",
		"trp-L", "TRP",
		"trnatrp", "TRP-tRNAs",
		"dttp", "TTP",
		"tungs", "TUNGSTATE",
		"tyr-L", "TYR",
		"trnatyr", "TYR-tRNAs",
		"tym", "TYRAMINE",
		"q8", "UBIQUINONE-8",
		"udp", "UDP",
		"udpLa4n", "UDP-4-AMINO-4-DEOXY-L-ARABINOSE",
		"uamag", "UDP-AA-GLUTAMATE",
		"ugmd", "UDP-AAGM-DIAMINOHEPTANEDIOATE",
		"uaccg", "UDP-ACETYL-CARBOXYVINYL-GLUCOSAMINE",
		"udpgalfur", "UDP-D-GALACTO-14-FURANOSE",
		"udpglcur", "UDP-GLUCURONATE",
		"udpLa4fn", "UDP-L-ARA4-FORMYL-N",
		"uacmam", "UDP-MANNAC",
		"uacmamu", "UDP-MANNACA",
		"um4p", "UDP-MURNAC-TETRAPEPTIDE",
		"uacgam", "UDP-N-ACETYL-D-GLUCOSAMINE",
		"udpacgal", "UDP-N-ACETYL-GALACTOSAMINE",
		"uamr", "UDP-N-ACETYLMURAMATE",
		"u3aga", "UDP-OHMYR-ACETYLGLUCOSAMINE",
		"u3hga", "UDP-OHMYR-GLUCOSAMINE",
		"ump", "UMP",
		"ura", "URACIL",
		"urate", "URATE",
		"urea", "UREA",
		"uri", "URIDINE",
		"uppg3", "UROPORPHYRINOGEN-III",
		"utp", "UTP",
		"val-L", "VAL",
		"trnaval", "VAL-tRNAs",
		"h2o", "WATER",
		"xan", "XANTHINE",
		"xtsn", "XANTHOSINE",
		"xmp", "XANTHOSINE-5-PHOSPHATE",
		"xdp", "XDP",
		"xtp", "XTP",
		"xu5p-D", "XYLULOSE-5-PHOSPHATE", };
	
	
	
	private static SessionFactory sessionFactory;
	private static Map<String, MnxMetaboliteEntity> biggToMnxMap;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sessionFactory = HelperHbmConfigInitializer.initializeHibernateSession("");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sessionFactory.close();
	}

	@Before
	public void setUp() throws Exception {
		sessionFactory.openSession();
	}

	@After
	public void tearDown() throws Exception {
		sessionFactory.getCurrentSession().close();
	}
	
	private void mapToMeta(String file, IGenericDao dao) throws Exception {
//		File sbml = new File(file);
//		DefaultSbmlTransformerImpl transformer = new DefaultSbmlTransformerImpl();
////		System.out.println(transformer.normalizeMetaboliteId("M_lald_L_c"));
//		
//		ContainerLoader loader = new ContainerLoader(sbml, transformer);
//		
////		System.out.println(loader.getMetaboliteSpecies());
//
//		for (String cpdId : loader.getMetaboliteIdSet()) {
//			StringBuilder sb = new StringBuilder();
//			sb.append(cpdId).append("\t");
//			if (biggToMnxMap.containsKey(cpdId)) {
//				sb.append(biggToMnxMap.get(cpdId).getEntry()).append("\t");
//				for (MnxMetaboliteCrossReferenceEntity xref : biggToMnxMap.get(cpdId).getCrossReferences()) {
//					if (xref.getRef().toLowerCase().equals("metacyc") /*&& xref.getValue().toLowerCase().charAt(0) == 'c' */) {
//						sb.append(xref).append("\t");
////						List<KeggMetaboliteEntity> entry = dao.criteria(KeggMetaboliteEntity.class, Restrictions.eq("entry", xref.getValue()));
////						if (entry.size() == 1) {
////							if (entry.get(0).getReactions().size() > 0) sb.append(xref).append("\t");
////						} else {
////							sb.append("##############OMG###############");
////						}
//					}
//				}
//			} else {
//				sb.append("NOTFOUND");
//			}
//			System.out.println(sb.toString());
//		}
//		
////		for (String cpdId : loader.getMetaboliteIdSet()) {
////			for (String cpdSpecieId : loader.getMetaboliteSpecieMap().get(cpdId)) {
//////				System.out.println(cpdSpecieId + "\t" + );
////			}
////		}
	}
	
	private void mapToKegg(String file, IGenericDao dao) throws Exception {
//		File sbml = new File(file);
//		DefaultSbmlTransformerImpl transformer = new DefaultSbmlTransformerImpl();
////		System.out.println(transformer.normalizeMetaboliteId("M_lald_L_c"));
//		
//		ContainerLoader loader = new ContainerLoader(sbml, transformer);
//		
////		System.out.println(loader.getMetaboliteSpecies());
//
//		for (String cpdId : loader.getMetaboliteIdSet()) {
//			StringBuilder sb = new StringBuilder();
//			sb.append(cpdId).append("\t");
//			if (biggToMnxMap.containsKey(cpdId)) {
//				sb.append(biggToMnxMap.get(cpdId).getEntry()).append("\t");
//				for (MnxMetaboliteCrossReferenceEntity xref : biggToMnxMap.get(cpdId).getCrossReferences()) {
//					if (xref.getRef().toLowerCase().equals("kegg") && xref.getValue().toLowerCase().charAt(0) == 'c' ) {
////						sb.append(xref).append("\t");
//						List<KeggMetaboliteEntity> entry = dao.criteria(KeggMetaboliteEntity.class, Restrictions.eq("entry", xref.getValue()));
//						if (entry.size() == 1) {
//							if (entry.get(0).getReactions().size() > 0) sb.append(xref).append("\t");
//						} else {
//							sb.append("##############OMG+" +  entry.toString() +  "###############");
//						}
//					}
//				}
//			} else {
//				sb.append("NOTFOUND");
//			}
//			System.out.println(sb.toString());
//		}
//		
////		for (String cpdId : loader.getMetaboliteIdSet()) {
////			for (String cpdSpecieId : loader.getMetaboliteSpecieMap().get(cpdId)) {
//////				System.out.println(cpdSpecieId + "\t" + );
////			}
////		}
	}
	
	@Test
	public void test2() throws Exception {
//		Map<String, String> toKeggMap = new HashMap<> ();
//		for (int i = 0; i < cpdMap.length; i+=2) {
//			toKeggMap.put(cpdMap[i], cpdMap[i + 1]);
//		}
//		
//		File sbml = new File("./src/main/resources/sbml/iJO1366.xml");
//		DefaultSbmlTransformerImpl transformer = new DefaultSbmlTransformerImpl();
//		ContainerLoader loader = new ContainerLoader(sbml, transformer);
//		
////		for (String rxnId : loader.getReactionSpecies()) {
////			System.out.println(rxnId + "\t" + "RXXXXX" + "\tKEGG_REACTION");
////		}
////		
////		for (String cpdSiD : loader.getMetaboliteSpecies()) {
////			System.out.println(cpdSiD + "\t" + toKeggMap.get(transformer.normalizeMetaboliteId(cpdSiD)) + "\tKEGG_CPD");
////		}
	}
	
	
	@Test
	public void keggToBigg() throws Exception {
		IGenericDao dao = new GenericEntityDaoImpl(sessionFactory);
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		String file = BioSynthUtilsIO.readFromFile("E:/kegg_rxn_id.txt");
		String[] rxn = file.split("\n");
		for (String rxnId : rxn) {
			Criterion criterion1 = Restrictions.and(
					Restrictions.eq("ref", "kegg"),
					Restrictions.eq("value", rxnId));
			List<MnxReactionCrossReferenceEntity> res = dao.criteria(MnxReactionCrossReferenceEntity.class, criterion1);
			for (MnxReactionCrossReferenceEntity xref : res) {
				StringBuilder sb = new StringBuilder();
				sb.append(xref.getValue()).append("\t");
				for (MnxReactionCrossReferenceEntity rxnXref : xref.getMnxReactionEntity().getCrossReferences()) {
					if (rxnXref.getRef().equals("bigg")) {
						sb.append(rxnXref.getValue()).append("\t");
					}
				}
				System.out.println(sb.toString());
//				System.out.println();
//				xref.getMnxReactionEntity().getId();
//				Criterion criterion2 = Restrictions.and(
//						Restrictions.eq("id_reaction", "kegg"),
//						Restrictions.eq("ref", "bigg"));
//				List<MnxReactionCrossReferenceEntity> bigg = dao.criteria(MnxReactionCrossReferenceEntity.class, criterion2);
				
			}
//			dao.getReference(MnxReactionCrossReferenceEntity.class, id)
//			System.out.println(rxnId);
		}
		System.out.println(rxn.length);
		tx.commit();
	}
	
	
	@Test
	public void readModel() throws IOException {
//		File sbml = new File("./src/main/resources/sbml/iJO1366.xml");
//		DefaultSbmlTransformerImpl transformer = new DefaultSbmlTransformerImpl();
//		ContainerLoader loader = new ContainerLoader(sbml, transformer);
////		System.out.println(loader.getMetaboliteSpecies());
	}
	
	@Test
	public void testMapSbmlMetabolites() throws Exception {
		biggToMnxMap = new HashMap<> ();
		
		IGenericDao dao = new GenericEntityDaoImpl(sessionFactory);
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		List<MnxMetaboliteEntity> metabolites = dao.findAll(MnxMetaboliteEntity.class);
		
		for (MnxMetaboliteEntity mnxCpd : metabolites) {
			for (MnxMetaboliteCrossReferenceEntity xref : mnxCpd.getCrossreferences()) {
				if (xref.getRef().toLowerCase().equals("bigg")) {
					if (biggToMnxMap.put(xref.getValue(), mnxCpd) != null) {
						System.out.println("COLLISION !!! " + mnxCpd);
					}
				}
			}
			
//			System.out.println(mnxCpd.getEntry());
		}
		

		mapToKegg("./src/main/resources/sbml/iJO1366.xml", dao);
		
		tx.commit();
//		System.out.println("####################################################################");
//		System.out.println("####################################################################");
//		System.out.println("####################################################################");
//		map("./src/main/resources/sbml/recon1.xml");
//		System.out.println("####################################################################");
//		System.out.println("####################################################################");
//		System.out.println("####################################################################");
//		map("./src/main/resources/sbml/iSB619.xml");
		
//		ContainerLoader loader = new ContainerLoader();
		
		
		assertEquals(true, true);
	}

}
