package com.vtest.it.testerdatalogdealplatform.advisor;

import com.vtest.it.testerdatalogdealplatform.pojo.mes.SlotAndSequenceConfigBean;
import com.vtest.it.testerdatalogdealplatform.pojo.tester.TesterDatalogInformationBean;
import com.vtest.it.testerdatalogdealplatform.services.FileNamePerfect.impl.FileNameCheck;
import com.vtest.it.testerdatalogdealplatform.services.FileNamePerfect.impl.GetRightFileName;
import com.vtest.it.testerdatalogdealplatform.services.FileNamePerfect.impl.GetSlot;
import com.vtest.it.testerdatalogdealplatform.services.mes.MesServices;
import com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools.DatalogFileNameParser;
import com.vtest.it.testerdatalogdealplatform.services.parsedatalogtools.impl.*;
import com.vtest.it.testerdatalogdealplatform.services.tools.FileNameRemoveDoubleUnderLine;
import com.vtest.it.testerdatalogdealplatform.services.tools.FileTimeCheck;
import com.vtest.it.testerdatalogdealplatform.services.uncompress.UnCompress;
import org.apache.commons.io.FileUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class StdfDataSourceNeedDeal {
    @Value("${system.properties.datalog.m7000}")
    private String m7000;
    @Value("${system.properties.datalog.t862}")
    private String t862;
    @Value("${system.properties.datalog.chroma}")
    private String chroma;
    @Value("${system.properties.datalog.v50}")
    private String v50;
    @Value("${system.properties.datalog.v93000}")
    private String v93000;
    @Value("${system.properties.datalog.j750}")
    private String j750;
    @Value("${system.properties.datalog.error-path}")
    private String errorPath;
    @Value("${system.properties.datalog.backup-path}")
    private String backupPath;
    @Autowired
    private FileNameCheck fileNameCheck;
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
    @Autowired
    private GetRightFileName getRightFileName;
    @Autowired
    private GetSlot getSlot;
    @Autowired
    private UnCompress unCompress;
    @Autowired
    private MesServices mesDao;
    @Autowired
    private FileTimeCheck fileTimeCheck;
    @Autowired
    private FileNameRemoveDoubleUnderLine removeDoubleUnderLine;

    @Around("execution(* com.vtest.it.testerdatalogdealplatform.deal.Deal.datalogDeal(..))")
    public void backupAndGetNeedDealSources(ProceedingJoinPoint proceedingJoinPoint) {
        Map<String, String> pathNeedDealmap = new HashMap<>();
        unCompressZipFiles(m7000);
        unCompressZipFiles(chroma);
        unCompressZipFiles(t862);
        unCompressZipFiles(v50);
        unCompressZipFiles(v93000);
        unCompressZipFiles(j750);
        dataSourceDeal(m7000, m7000DatalogParser, pathNeedDealmap, true, "m7000");
        dataSourceDeal(chroma, chromaDatalogParser, pathNeedDealmap, true, "chroma");
        dataSourceDeal(t862, t862DatalogParser, pathNeedDealmap, true, "t862");
        dataSourceDeal(v50, v50DatalogParser, pathNeedDealmap, true, "v50");
        dataSourceDeal(v93000, v9300DatalogParser, pathNeedDealmap, true, "v93000");
        dataSourceDeal(j750, j750DatalogParser, pathNeedDealmap, true, "j750");
        try {
            proceedingJoinPoint.proceed(new Object[]{pathNeedDealmap});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void unCompressZipFiles(String path) {
        File source = new File(path);
        File[] files = source.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".zip") && fileTimeCheck.Check(file)) {
                try {
                    boolean flag = unCompress.unCompressFile(file);
                    if (flag) {
                        FileUtils.copyFile(file, new File(errorPath + "/" + file.getName()));
                    }
                } catch (IOException e) {
                    try {
                        FileUtils.copyFile(file, new File(errorPath + "/" + file.getName()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void dataSourceDeal(String path, DatalogFileNameParser parser, Map<String, String> pathNeedDealmap, boolean flag, String type) {
        File dataSource = new File(path);
        File[] files = dataSource.listFiles();
        for (int i = 0; i < files.length; i++) {
            File waferFile = files[i];
            if (fileTimeCheck.Check(waferFile) && fileNameCheck.check(waferFile) && !waferFile.getName().endsWith(".zip")) {
                if (waferFile.getName().endsWith(".temp")) {
                    try {
                        if (fileTimeCheck.CheckLong(waferFile)) {
                            FileUtils.copyFile(waferFile, new File(errorPath + "/" + removeDoubleUnderLine.remove(waferFile.getName())));
                            FileUtils.forceDelete(waferFile);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                try {
                    String fileNameAfterDeal = removeDoubleUnderLine.removeBrackets(removeDoubleUnderLine.remove(waferFile.getName()));
                    TesterDatalogInformationBean testerDatalogInformationBean = parser.getFileInformation(fileNameAfterDeal);
                    String customerCode = testerDatalogInformationBean.getCustomCode();
                    String device = testerDatalogInformationBean.getDevice();
                    String lot = testerDatalogInformationBean.getLot();
                    String cpProcess = testerDatalogInformationBean.getCpStep();
                    String waferId = testerDatalogInformationBean.getWaferId();
                    SlotAndSequenceConfigBean slotAndSequenceConfigBean = mesDao.getLotSlotConfig(lot);
                    if (!waferFile.getName().endsWith(".temp")) {
                        String finFileName = fileNameAfterDeal;
                        if (slotAndSequenceConfigBean.getReadType().toUpperCase().equals("SLOT")) {
                            String slot = getSlot.get(waferId);
                            if (null == slot) {
                                FileUtils.copyFile(waferFile, new File(errorPath + "/" + waferFile.getName()));
                                FileUtils.forceDelete(waferFile);
                                continue;
                            }
                            waferId = mesDao.getWaferIdBySlot(lot, slot);
                            finFileName = waferId.equals("NA") ? fileNameAfterDeal : getRightFileName.getRightFileName(fileNameAfterDeal, waferId);
                        }
                        String finBackupPath = backupPath + "/" + customerCode + "/" + device + "/" + lot + "/" + cpProcess + "/" + waferId;
                        FileUtils.copyFile(waferFile, new File(finBackupPath + "/" + finFileName));
                        FileUtils.forceDelete(waferFile);
                        if (flag && (finFileName.endsWith(".stdf") || finFileName.endsWith(".std"))) {
                            pathNeedDealmap.put(finBackupPath, type);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        FileUtils.copyFile(waferFile, new File(errorPath + "/" + waferFile.getName()));
                        FileUtils.forceDelete(waferFile);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
