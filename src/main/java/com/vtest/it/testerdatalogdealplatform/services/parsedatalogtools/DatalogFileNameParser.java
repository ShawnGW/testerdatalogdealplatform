package com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools;

import com.vtest.it.testerdatalogdealplatform.pojo.tester.TesterDatalogInformationBean;

import java.text.ParseException;

public interface DatalogFileNameParser {
    public abstract TesterDatalogInformationBean getFileInformation(String fileName) throws ParseException;
}
