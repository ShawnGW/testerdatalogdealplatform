package com.vtest.it.testerdatalogdealplatform.test;

import com.vtest.it.testerdatalogdealplatform.pojo.tester.TesterDatalogInformationBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shawn.sun
 * @date 2020/11/23  16:14
 */
public class Test {
    public static final String J750REGEX = "VTEST_CP_(\\w{3})_([\\.\\w-]{1,}|[\\.\\w-_\\(\\)]{1,})_([A-Z0-9-\\.]{1,}|\\([A-Z0-9-\\._]{1,}\\))_([\\w-\\.]{1,})_(TT[A-Z]{1}-[\\dA-Z]{2,4})_([0-9A-Z-]{1,})_([\\w-_]{1,})_(CP[0-9]{1,})_(RP[0-9]{1})_([Vv]{1}[0-9]{3,}|[0-9]{4,})_([0-9]{8}_[0-9]{6})\\.([A-Za-z0-9]{1,})";

    public static void main(String[] args) {
        String fileName = "VTEST_CP_SLA_EF2-4500_(EEM893_B01)_EEM893-05-E2_TTJ-01_POS-52_AL004-4K-PC-01_CP1_RP1_1073_20201116_185428.zip";
        Pattern pattern = Pattern.compile(Test.J750REGEX);
        Matcher matcher = pattern.matcher(fileName);
        System.out.println(matcher.find());
        TesterDatalogInformationBean bean = new TesterDatalogInformationBean();
        String customerCode = matcher.group(1);
        String device = matcher.group(2);
        String lot = matcher.group(3);
        String waferId = matcher.group(4);
        String tester = matcher.group(5);
        String prober = matcher.group(6);
        String proberCard = matcher.group(7);
        String cpProcess = matcher.group(8);
        String reTestTime = matcher.group(9);
        String operator = matcher.group(10);
        String testTime = matcher.group(11);
        String suffix = matcher.group(12);

        bean.setCustomCode(customerCode);
        bean.setDevice(device);
        bean.setLot(lot);
        bean.setWaferId(waferId);
        bean.setTester(tester);
        bean.setProber(prober);
        bean.setProberCard(proberCard);
        bean.setCpStep(cpProcess);
        bean.setTestStep(reTestTime);
        bean.setOperator(operator);
        bean.setTestTime(testTime);
        bean.setSuffix(suffix);
        System.out.println(bean);

    }
}
