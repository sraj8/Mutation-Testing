package eclipse.ast.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;


public class CreateBackup {

    public static void backupAndCreateFile(String filePath) throws IOException {
        File target = new File("backup");
        File fileOnPath = new File(filePath);
        File backupFile = new File("_old_"+fileOnPath.getName());
        FileUtils.copyFile(fileOnPath,backupFile);
        FileUtils.copyFileToDirectory(backupFile,target);
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
                    //FileUtils
                    /*BufferedReader reader = new BufferedReader(new FileReader(fileToBackup));
                    String fileContent = new String(Files.readAllBytes(fileToBackup.toPath()), StandardCharsets.UTF_8);
                    String line;
                    while((line = reader.readLine()) != null){
                        if (line.contains(fileToBackup.getName()))
                        {
                            fileContent = fileContent.replace(fileToBackup.getName(),backupDraft.getName());
                        }


                    }
                    Files.write(fileToBackup.toPath(),fileContent.getBytes(StandardCharsets.UTF_8));*/

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
        String fileLoc= "old_java/";
        String delPath = "\\java\\net\\openhft\\hashing";
        fileLoc = fileLoc.replace(delPath,"");
        String fileName = FilenameUtils.getBaseName(filePath);
        String fileExtension = FilenameUtils.getExtension(filePath);
        return new StringBuilder().append(fileLoc).append(fileName)
                .append("_old.")
                .append(fileExtension)
                .toString();
    }
}


