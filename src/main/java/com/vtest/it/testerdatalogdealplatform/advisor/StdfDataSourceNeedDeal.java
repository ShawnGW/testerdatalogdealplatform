package com.vtest.it.testerdatalogdealplatform.advisor;

import com.vtest.it.testerdatalogdealplatform.pojo.mes.MesConfigBean;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author shawn.sun
 * @date 2020/05/28 13:18:10
 */


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
    @Value("${system.properties.datalog.s200}")
    private String s200;
    @Value("${system.properties.datalog.v93000}")
    private String v93000;
    @Value("${system.properties.datalog.j750}")
    private String j750;
    @Value("${system.properties.datalog.dx}")
    private String dx;
    @Value("${system.properties.datalog.error-path}")
    private String errorPath;
    @Value("${system.properties.datalog.backup-path}")
    private String backupPath;
    @Value("${system.properties.datalog.cdcBackup}")
    private String cdcBackup;
    @Autowired
    private FileNameCheck fileNameCheck;
    @Autowired
    private ChromaDatalogParser chromaDatalogParser;
    @Autowired
    private J750DatalogParser j750DatalogParser;
    @Autowired
    private M7000DatalogParser m7000DatalogParser;
    @Autowired
    private S200DatalogParser s200DatalogParser;
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

    private static final Logger logger = LoggerFactory.getLogger(StdfDataSourceNeedDeal.class);
    private static final String STDFREADER = "/scripts/STDFreader ";

    @Around("execution(* com.vtest.it.testerdatalogdealplatform.deal.Deal.datalogDeal(..))")
    public void backupAndGetNeedDealSources(ProceedingJoinPoint proceedingJoinPoint) {
        Map<String, String> pathNeedDealMap = new HashMap<>();
        unCompressDxGzFiles(dx);
        unCompressZipFiles(m7000);
        unCompressZipFiles(chroma);
        unCompressZipFiles(t862);
        unCompressZipFiles(v50);
        unCompressZipFiles(v93000);
        unCompressZipFiles(j750);
        unCompressZipFiles(s200);
        dataSourceDeal(m7000, m7000DatalogParser, pathNeedDealMap, true, "m7000");
        dataSourceDeal(chroma, chromaDatalogParser, pathNeedDealMap, true, "chroma");
        dataSourceDeal(t862, t862DatalogParser, pathNeedDealMap, true, "t862");
        dataSourceDeal(v50, v50DatalogParser, pathNeedDealMap, true, "v50");
        dataSourceDeal(v93000, v9300DatalogParser, pathNeedDealMap, true, "v93000");
        dataSourceDeal(j750, j750DatalogParser, pathNeedDealMap, true, "j750");
        dataSourceDeal(s200, s200DatalogParser, pathNeedDealMap, true, "s200");
        try {
            proceedingJoinPoint.proceed(new Object[]{pathNeedDealMap});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void unCompressDxGzFiles(String path) {
        ExecutorService service = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        List<Future<String>> list = new LinkedList<>();
        File[] files = new File(path).listFiles();
        Queue<File> queue = new LinkedList<>(Arrays.asList(Objects.requireNonNull(files)));
        while (!queue.isEmpty()) {
            File gzFile = queue.poll();
            String gzFileName = gzFile.getName();
            Future<String> future = service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (gzFileName.endsWith(".gz")) {
                        boolean unGzipFlag = unCompress.unGzipFile(gzFile.getPath());
                        if (unGzipFlag) {
                            FileUtils.copyFile(gzFile, new File(cdcBackup + "/" + gzFileName));
                            if (gzFile.length() == new File(cdcBackup + "/" + gzFileName).length()) {
                                FileUtils.forceDelete(gzFile);
                            }
                        }
                    } else if (gzFileName.endsWith(".std") || gzFileName.endsWith(".stdf")) {
                        String[] tokens = gzFileName.split("_");
                        String lot = tokens[8];
                        String slot = tokens[10];
                        String cpProcess = tokens[2];
                        String waferIdBySlot = slot;
                        if (slot.length() <= 2) {
                            waferIdBySlot = mesDao.getWaferIdBySlot(lot, slot);
                        }
                        MesConfigBean waferConfigFromMes = mesDao.getWaferConfigFromMes(waferIdBySlot, cpProcess);
                        String customerCode = waferConfigFromMes.getCustomerCode();
                        String probe = waferConfigFromMes.getProberId();
                        String probeCard = waferConfigFromMes.getProberCardId();
                        String operator = waferConfigFromMes.getOperator();
                        operator = (operator == null || operator.equals("NA")) ? "V005" : operator;
                        int rpProcess = Integer.parseInt(tokens[11].substring(1));
                        String testTime1 = tokens[12].substring(0, 8);
                        String testTime2 = tokens[12].substring(8, 14);
                        String fileName = "VTEST_CP_" +
                                customerCode + "_" +
                                tokens[3] + "_" +
                                lot + "_" +
                                waferIdBySlot + "_" +
                                tokens[1] + "_" +
                                probe + "_" +
                                probeCard + "_" +
                                cpProcess + "_" +
                                "RP" + (rpProcess - 1) + "_" +
                                operator + "_" +
                                testTime1 + "_" +
                                testTime2 + ".stdf";
                        File destFile = new File(j750 + "/" + fileName);
                        String finalWaferIdBySlot = waferIdBySlot;


                        try {
                            File targetDirectory = new File("/server212/Datalog/TempData/cpRcsStdTxt/" + lot + "_" + waferIdBySlot + "_" + cpProcess);
                            if (!targetDirectory.exists()) {
                                targetDirectory.mkdirs();
                            }
                            File writting = new File(targetDirectory.getPath() + "/writting");
                            if (!writting.exists()) {
                                writting.createNewFile();
                            }
                            File[] stds = new File("/server212/Datalog/TesterData/" + customerCode + "/" + tokens[3] + "/" + lot + "/" + cpProcess + "/" + waferIdBySlot).listFiles();
                            List<File> fileList = Arrays.asList(stds);
                            fileList.add(gzFile);
                            fileList.stream().filter(e -> {
                                String fileNameTmp = e.getName();
                                return fileNameTmp.endsWith(".std") || fileNameTmp.endsWith(".stdf");
                            }).forEach(e -> {
                                String fileNameTmp = e.getName();
                                String[] fileNameTokens = fileNameTmp.split("_");
                                String fileDate = fileNameTokens[fileNameTokens.length - 2];
                                String fileTime = fileNameTokens[fileNameTokens.length - 1];
                                String targetFileName = lot + "_" + finalWaferIdBySlot + "_" + cpProcess + "_" + fileDate + fileTime;
                                if (e != gzFile) {
                                    targetFileName = lot + "_" + finalWaferIdBySlot + "_" + cpProcess + "_" + fileTime.substring(0, 14);
                                }
                                File targetFile = new File(targetDirectory.getPath() + "/" + targetFileName);
                                String cmd = STDFREADER + e + " >" + targetFile;
                                try {
                                    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
                                } catch (Exception exception) {
                                    // TODO Auto-generated catch block
                                    exception.printStackTrace();
                                }
                            });
                            FileUtils.forceDelete(writting);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                        FileUtils.copyFile(gzFile, destFile);
                        if (gzFile.length() == destFile.length()) {
                            FileUtils.forceDelete(gzFile);
                        }
                    }
                    return "ok";
                }
            });
            list.add(future);

        }
        list.forEach(e -> {
            try {
                e.get();
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        service.shutdown();
    }

    public void unCompressZipFiles(String path) {
        logger.error("uncompress :" + path);
        ExecutorService service = new ThreadPoolExecutor(10, 20, 100, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        File source = new File(path);
        File[] files = source.listFiles();
        List<Future> list = new LinkedList<>();
        for (File file : files) {
            if (file.getName().endsWith(".zip") && fileTimeCheck.Check(file)) {
                Future<String> future = service.submit(() -> {
                    boolean flag = unCompress.unCompressFile(file);
                    return flag + ":" + file.getPath();
                });
                list.add(future);
            }
        }
        for (Future future : list) {
            try {
                String unCompressResult = (String) future.get();
                String[] tokens = unCompressResult.split(":");
                File file = new File(tokens[1]);
                try {
                    boolean flag = Boolean.parseBoolean(tokens[0]);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
    }

    public void dataSourceDeal(String path, DatalogFileNameParser parser, Map<String, String> pathNeedDealMap, boolean flag, String type) {
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
                            pathNeedDealMap.put(finBackupPath, type);
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
