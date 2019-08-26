package com.ciyuanplus.mobile.utils;

import com.ciyuanplus.mobile.statistics.StatisticsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alen on 2017/7/5.
 */

public class FileUtils {

    public static List<String> getFileList(String path, String suffix) {
        List<String> loggerList = new LinkedList<>();
        File dir = new File(path);
        if (dir.exists()) {
            try {
                for (File child : dir.listFiles()) {
                    if (!child.isDirectory()) {
                        String fileSuffix = stripFileExtension(child.getPath());
                        if (fileSuffix.toLowerCase().equals(
                                suffix.toLowerCase())) {
                            loggerList.add(child.getPath());
                        }
                    }
                }
            } catch (Exception e) {
                StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

                e.printStackTrace();
            }
        }
        return loggerList;
    }

    /**
     * Strips out the path and file name from a string. Dot is included in the
     * returned string.
     *
     * @return file extension stripped down
     */
    private static String stripFileExtension(String fileName) {
        final String normalizedFileName = normalizePath(fileName);

        int dotInd = normalizedFileName.lastIndexOf('.');
        int separatorInd = normalizedFileName.lastIndexOf(File.separatorChar);

        // if dot is in the first position,
        // we are dealing with a hidden file rather than an extension
        return (dotInd > 0 && dotInd < normalizedFileName.length() && dotInd > separatorInd) ? normalizedFileName
                .substring(dotInd) : "";
    }

    /**
     * Filter the path and replace all illegal separator charater. '/' is not
     * included in the returned string.
     *
     * @return file legal path string
     */
    private static String normalizePath(String path) {
        String normalizedFileName = path.replace('/', File.separatorChar);
        return normalizedFileName.replace('\\', File.separatorChar);
    }

    /**
     * Traverse folder to find specific suffix files returned the list of the
     * files.
     *
     * @return loggerList: fit to suffix's files list
     */
    public static String read(String fileFullName) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileFullName));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        } catch (FileNotFoundException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        } catch (IOException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

                e.printStackTrace();
            }
        }
        return sb.toString().trim();
    }

    /**
     * Deletes a file. If file is a directory, delete it and all
     * sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     * (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException  if the directory is <code>null</code>
     * @throws FileNotFoundException if the file was not found
     * @throws IOException           in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: "
                            + file);
                }
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    private static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    private static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(ioe), "");

                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

}
