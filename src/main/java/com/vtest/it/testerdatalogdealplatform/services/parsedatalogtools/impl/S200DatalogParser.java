package com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools.impl;

import com.vtest.it.testerdatalogdealplatform.pojo.tester.TesterDatalogInformationBean;
import com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools.DatalogFileNameParser;
import com.vtest.it.testerdatalogdealplatform.services.tools.DatalogRegexSamples;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shawn.sun
 * @date 2020/05/28  13:11
 */
@Service
public class S200DatalogParser implements DatalogFileNameParser {
    private Pattern pattern = Pattern.compile(DatalogRegexSamples.S200REGEX);

    @Override
    public TesterDatalogInformationBean getFileInformation(String fileName) throws ParseException {
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
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
            return bean;
        }
        return null;
    }
}
