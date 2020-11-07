package com.vtest.it.testerdatalogdealplatform.services.uncompress;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class UnCompress {
    public boolean unGzipFile(String sourcedir) {
        String ouputfile = "";
        try {
            FileInputStream fin = new FileInputStream(sourcedir);
            GZIPInputStream gzin = new GZIPInputStream(fin);
            ouputfile = sourcedir.substring(0, sourcedir.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(ouputfile);

            int num;
            byte[] buf = new byte[1024];

            while ((num = gzin.read(buf, 0, buf.length)) != -1) {
                fout.write(buf, 0, num);
            }

            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean unCompressFile(File source) throws IOException {
        boolean unCompressFlag = false;
        FileInputStream fileInputStream = new FileInputStream(source);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);
        ZipEntry entry = null;
        try {
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File unzipfile = new File(source.getParent() + "/" + entry.getName());
                FileOutputStream fileOutputStream = new FileOutputStream(unzipfile);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                int b = 0;
                boolean flag = false;
                try {
                    while ((b = bufferedInputStream.read()) != -1) {
                        bufferedOutputStream.write(b);
                    }
                } catch (IOException e) {
                    unCompressFlag = true;
                    flag = true;
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                if (flag) {
                    unzipfile.delete();
                }
            }
            bufferedInputStream.close();
            zipInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            unCompressFlag = true;
        } finally {
            if (null != bufferedInputStream) {
                bufferedInputStream.close();
            }
            if (null != zipInputStream) {
                zipInputStream.close();
            }
            if (null != fileInputStream) {
                fileInputStream.close();
            }
        }
        return unCompressFlag;
    }
}
