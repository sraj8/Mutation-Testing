package net.openhft.hashing;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;


public class CreateBackup {

    public static File backupAndCreateFile(String filePath) throws IOException {
        return failedBackupOfExistingFile(filePath) ? null : new File(filePath);
    }

    private static boolean failedBackupOfExistingFile(String filePath) {
        File fileOnPath = new File(filePath);
       return  fileOnPath.exists() && !successBackupFile(fileOnPath);
    }

    private static boolean successBackupFile(File fileToBackup) {

        while (true) {

            File backupDraft = new File(generateBackupName(fileToBackup.getAbsolutePath()));
            if (!backupDraft.exists()) {
                try {
                    FileUtils.copyFile(fileToBackup, backupDraft);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fileToBackup.renameTo(backupDraft);
            }
            fileWithGeneratedNameExistsNowSleepForSomeTime();
        }
    }

    private static void fileWithGeneratedNameExistsNowSleepForSomeTime(){
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String generateBackupName (String filePath) {
        String fileLoc=FilenameUtils.getFullPath(filePath);
        String fileName = FilenameUtils.getBaseName(filePath);
        String fileExtension = FilenameUtils.getExtension(filePath);
        return new StringBuilder().append(fileLoc).append(fileName)
                .append("_old.")
                .append(fileExtension)
                .toString();
    }
}

