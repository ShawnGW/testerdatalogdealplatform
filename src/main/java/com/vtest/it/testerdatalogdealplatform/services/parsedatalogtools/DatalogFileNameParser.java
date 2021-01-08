package com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools;

import com.vtest.it.testerdatalogdealplatform.pojo.tester.TesterDatalogInformationBean;

import java.text.ParseException;

/**
 * @author shawn.sun
 * @date 2020-12-26 10:40:13
 */
public interface DatalogFileNameParser {
    /**
     * fetch file information
     *
     * @param fileName file name
     * @return tester information bean
     * @throws ParseException exception
     */
    TesterDatalogInformationBean getFileInformation(String fileName) throws ParseException;
}
