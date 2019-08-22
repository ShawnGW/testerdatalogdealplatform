package com.vtest.it.testerdatalogdealplatform.services.tools;

public class DatalogRegexSamples {
    public static final String J750REGEX = "VTEST_CP_(\\w{3})_([\\.\\w-]{1,}|[\\.\\w-_\\(\\)]{1,})_([A-Z0-9-\\.]{1,})_([A-Z0-9-\\.]{1,})_(TT[A-Z]{1}-[\\dA-Z]{2,4})_([0-9A-Z-]{1,})_([0-9A-Z-_]{1,})_(CP[0-9]{1,})_(RP[0-9]{1})_([Vv]{1}[0-9]{3,}|[0-9]{4,})_([0-9]{8}_[0-9]{6})\\.([A-Za-z0-9]{1,})";
    public static final String V93000REGEX = "VTEST_CP_(\\w{3})_([\\.\\w-]{1,}|[\\.\\w-_\\(\\)]{1,})_([A-Z0-9-\\.]{1,})_([A-Z0-9-\\.]{1,})_(TT[A-Z]{1}-[\\dA-Z]{2,4})_([0-9A-Z-]{1,})_([0-9A-Z-_]{1,})_(CP[0-9]{1,})_(RP[0-9]{1})_([Vv]{1}[0-9]{3,}|[0-9]{4,})_([0-9]{8}_[0-9]{6})\\.([A-Za-z0-9]{1,})";
    public static final String M7000REGEX = "VTEST_CP_(\\w{3})_([\\.\\w-]{1,}|[\\.\\w-_\\(\\)]{1,})_([\\w\\d-\\.]{1,})_([\\w-\\.]{1,})_(TT[\\w]{1}-[\\dA-Z]{2,4})_([\\w-]{1,})_([\\w-_]{1,})_(CP[\\d]{1,})_(RP[\\d]{1})_([Vv]{1}[\\d]{3,}|[\\d]{4,})_([\\d]{14})(_((datalog_[\\d]{1})|Summary))?\\.([\\w]{1,})";
    public static final String T862REGEX = "VTEST_CP_(\\w{3})_([\\.\\w-]{1,}|[\\.\\w-_\\(\\)]{1,})_([\\w\\d-\\.]{1,})_([\\w-\\.]{1,})_(TT[\\w]{1}-[\\dA-Z]{2,4})_([\\w-]{1,})_([\\w-_]{1,})_(CP[\\d]{1,})_(RP[\\d]{1})_([Vv]{1}[\\d]{3,}|[\\d]{4,})_([\\d]{14})\\.([\\w]{1,})";
    public static final String CHROMAREGEX = "VTEST_CP_(\\w{3})_([\\.\\w-]{1,}|[\\.\\w-_\\(\\)]{1,})_([\\w\\d-\\.]{1,})_([\\w-\\.]{1,})_(TT[\\w]{1}-[\\dA-Z]{2,4})_([\\w-]{1,})_([\\w-_]{1,})_(CP[\\d]{1,})_(RP[\\d]{1})_([Vv]{1}[\\d]{3,}|[\\d]{4,})_([\\w]{15})(_(dlogTDO|sumryTDO|wmpTDO|hsTDO))?\\.([\\w]{1,})";
    public static final String V50REGEX = "VTEST_CP_(\\w{3})_([\\.\\w-]{1,}|[\\.\\w-_\\(\\)]{1,})_([A-Z0-9-\\.]{1,})_([A-Z0-9-\\.]{1,})_(TT[A-Z]{1}-[\\dA-Z]{2,4})_([0-9A-Z-]{1,})_([0-9A-Z-_]{1,})_([Vv]{1}[0-9]{3,}|[0-9]{4,})_(CP[0-9]{1,})_(RP[0-9]{1})_([0-9]{8}_[0-9]{6})\\.([A-Za-z0-9]{1,})";
    public static final String MODIFYREGEX="(.*{1,})_(.*{1,})_(TT.*{1,})";
}
