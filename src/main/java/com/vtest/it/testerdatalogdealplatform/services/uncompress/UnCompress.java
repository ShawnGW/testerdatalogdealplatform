package com.vtest.it.testerdatalogdealplatform.services.uncompress;

import com.vtest.it.testerdatalogdealplatform.services.tools.PerfectCopy;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class UnCompress {
    @Autowired
    private PerfectCopy perfectCopy;
    public void deal(File zipfile, String directory, String fileName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(zipfile);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);
        ZipEntry zipEntry = null;
        File unzipfile = new File(zipfile.getParent() + "/" + fileName);
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(unzipfile);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                int b;
                while ((b = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(b);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) {
                directoryFile.mkdirs();
            }
            perfectCopy.copy(unzipfile,new File(directory+"/"+unzipfile.getName()));
            FileUtils.forceDelete(unzipfile);
        } catch (Exception e) {
            FileUtils.forceDelete(unzipfile);
        } finally {
            bufferedInputStream.close();
        }
    }
}
