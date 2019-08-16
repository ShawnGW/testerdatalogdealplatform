package com.vtest.it.testerdatalogdealplatform.deal;

import com.vtest.it.testerdatalogdealplatform.pojo.tester.TesterDatalogInformationBean;
import com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools.DatalogFileNameParser;
import com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Service
public class Deal {
    private static final Logger LOGGER = LoggerFactory.getLogger(Deal.class);
    @Autowired
    private ChromaDatalogParser chromaDatalogParser;
    @Autowired
    private J750DatalogParser j750DatalogParser;
    @Autowired
    private M7000DatalogParser m7000DatalogParser;
    @Autowired
    private T862DatalogParser t862DatalogParser;
    @Autowired
    private V50DatalogParser v50DatalogParser;
    @Autowired
    private V9300DatalogParser v9300DatalogParser;
    @Value("${system.properties.datalog.stdf-to-text-path}")
    private String stdfTextpath;
    private static final String STDFREADER = "/scripts/STDFreader ";

    public void datalogDeal(Map<String, String> pathNeedDealmap) {
        Map<String, DatalogFileNameParser> parserMap = new HashMap<>();
        parserMap.put("m7000", m7000DatalogParser);
        parserMap.put("chroma", chromaDatalogParser);
        parserMap.put("t862", t862DatalogParser);
        parserMap.put("v50", v50DatalogParser);
        parserMap.put("v93000", v9300DatalogParser);
        parserMap.put("j750", j750DatalogParser);
        for (String path : pathNeedDealmap.keySet()) {
            DatalogFileNameParser parser = parserMap.get(pathNeedDealmap.get(path));
            File[] stdfs = new File(path).listFiles();
            for (File stdf : stdfs) {
                if (stdf.getName().endsWith(".stdf") || stdf.getName().endsWith(".std")) {
                    try {
                        TesterDatalogInformationBean testerDatalogInformationBean = parser.getFileInformation(stdf.getName());
                        String customCode = testerDatalogInformationBean.getCustomCode();
                        String device = testerDatalogInformationBean.getDevice();
                        String lot = testerDatalogInformationBean.getLot();
                        String cpStep = testerDatalogInformationBean.getCpStep();
                        String waferId = testerDatalogInformationBean.getWaferId();
                        String testTime = testerDatalogInformationBean.getTestTime();
                        String tester = testerDatalogInformationBean.getTester();
                        String prober = testerDatalogInformationBean.getProber();
                        String proberCard = testerDatalogInformationBean.getProberCard();
                        String testStep = testerDatalogInformationBean.getTestStep();
                        String operator = testerDatalogInformationBean.getOperator();
                        String suffix = testerDatalogInformationBean.getSuffix();
                        String stdfTextPath = stdfTextpath + "/" + customCode + "/" + device + "/" + lot + "/" + cpStep + "/" + waferId;
                        String resultFileName = "VTEST_CP_" + customCode + "_" + device + "_" + lot + "_" + waferId + "_" + tester + "_" + prober + "_" + proberCard + "_" + cpStep + "_" + testStep + "_" + operator + "_" + testTime + "." + suffix + ".txt";
                        LOGGER.error(stdfTextPath);
                        LOGGER.error(resultFileName);
                        File directory = new File(stdfTextPath);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }
                        String CMD = STDFREADER + stdf + " >" + new File(stdfTextPath + "/" + resultFileName);
                        try {
                            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", CMD});
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
