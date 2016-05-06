package com.eightycats.litterbox.io.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.eightycats.litterbox.io.StreamUtils;
import com.eightycats.litterbox.logging.Logger;

/**
 * Common file operations.
 */
public class FileUtils
{
    /**
     * Moves a file to a give directory.
     *
     * @param file
     *            the path to the file to move.
     * @param targetDirectory
     *            the path to the directory where the file will be moved. If this directory does not
     *            exist, this method will create it.
     *
     * @return true if the move was successful.
     * @throws FileNotFoundException
     *             if the file to be moved does not exist.
     */
    public static boolean move (String file, String targetDirectory) throws FileNotFoundException
    {
        return move(new File(file), new File(targetDirectory));
    }

    public static boolean move (File file, File targetDirectory) throws FileNotFoundException
    {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath() + " does not exist.");
        }

        targetDirectory.mkdirs();

        return file.renameTo(new File(targetDirectory, file.getName()));
    }

    /**
     * This method moves and renames a given file. This is basically another version of the move()
     * method, except that this behavior is more consistent with your usual move commands. The given
     * filePath will get moved and renamed to the newFilePath. If the new file path specifies an
     * existing directory, then the behavior is the same as move().
     *
     * @param filePath
     *            the path to the file to be renamed.
     * @param newFilePath
     *            the renamed file path including the new name of the file.
     *
     * @return true if the rename was successful.
     *
     * @throws FileNotFoundException
     *             if the filePath does not exist.
     */
    public static boolean rename (String filePath, String newFilePath)
        throws FileNotFoundException
    {
        return rename(new File(filePath), new File(newFilePath));
    }

    public static boolean rename (File file, File newFile) throws FileNotFoundException
    {

        boolean success;

        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath() + " does not exist.");
        }

        // if the new file already exists and is a directory,
        // then just move the file to that directory
        if (newFile.exists() && newFile.isDirectory()) {
            success = move(file, newFile);
        } else {

            // make sure the target parent directory exists
            makeParentDirs(newFile);

            // rename the file
            success = file.renameTo(newFile);

        }

        return success;
    }

    public static void copy (String inputPath, String outputPath) throws FileNotFoundException,
        IOException
    {
        copy(new File(inputPath), new File(outputPath));
    }

    /**
     * Copies a file.
     */
    public static void copy (File source, File destination) throws FileNotFoundException,
        IOException
    {

        if (!source.exists()) {
            throw new FileNotFoundException("File [" + source.getPath() + "] does not exist.");
        }

        if (source.isDirectory()) {

            // do a deep copy of the source directory to
            // the destination
            deepCopy(source, destination);

        } else if (destination.exists() && destination.isDirectory()) {

            // copy the file to a new file with the same name in the
            // destination directory
            String fileName = source.getName();

            destination = new File(destination.getPath(), fileName);

            copyFile(source, destination);

        } else {

            // do a simple file copy from the source file
            // to the destination file
            copyFile(source, destination);
        }
    }

    public static void copyFile (File source, File destination) throws FileNotFoundException,
        IOException
    {

        // make sure the parent destination directories exists
        makeParentDirs(destination);

        InputStream in = new FileInputStream(source);

        try {
            OutputStream out = new FileOutputStream(destination);
            try {
                StreamUtils.copy(in, out);
            } finally {
                StreamUtils.close(out);
            }

        } finally {
            in.close();
        }
    }

    public static void deepCopy (File source, File destination) throws IOException
    {
        if (source.isDirectory()) {

            // make sure that the destination dir is not an existing file
            if (destination.exists() && !destination.isDirectory()) {
                throw new IOException("Could not copy directory [" + source.getPath()
                    + "]. The target file [" + destination.getPath() + "] already exists.");

            }

            // make sure the destination dir exists
            destination.mkdirs();

            File[] children = source.listFiles();

            for (int i = 0; i < children.length; i++) {
                // create a destination file with the same name
                // as the source child file
                File childDestination = new File(destination.getPath(), children[i].getName());
                deepCopy(children[i], childDestination);
            }

        } else {
            copyFile(source, destination);
        }

    }

    public static boolean delete (String path)
    {
        boolean success = true;

        File file = new File(path);

        if (file.exists()) {
            success = delete(file);
        }

        return success;
    }

    public static boolean delete (File file)
    {
        boolean success = true;

        if (file.isDirectory()) {
            File[] children = file.listFiles();

            for (int i = 0; i < children.length; i++) {
                if (!delete(children[i])) {
                    success = false;
                }
            }
        }

        // The directory is a file or is now empty and can be deleted.
        return success && file.delete();

    }

    public static void makeParentDirs (File file)
    {
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
    }

    public static String getWorkingDir ()
    {
        return System.getProperty("user.dir", ".");
    }

    public static void insert (String filePath, String text, long offset)
        throws FileNotFoundException, IOException
    {

        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        File temp = File.createTempFile("insert", "txt");
        FileWriter writer = new FileWriter(temp);
        PrintWriter output = new PrintWriter(writer);

        // skip ahead to the offset or the end of the file,
        // whichever comes first
        offset = Math.min(offset, file.length());

        try {

            char[] next = new char[1];
            int read = reader.read(next);
            long total = 0;

            while (read > -1 && total < offset) {
                output.print(next);
                total += read;
                read = reader.read(next);
            }

            output.print(text);

            while (read > -1) {
                output.print(next);
                read = reader.read(next);
            }

        } finally {
            StreamUtils.close(output);
            reader.close();
        }

        // copy the temp file back over the original file
        copy(temp, file);

        temp.delete();
    }

    public static void replace (String filePath, String regexPattern, String replacement)
        throws FileNotFoundException, IOException
    {
        // Since we are reading the whole file into memory,
        // this will run out of memory if the file specified is too big.
        String content = WholeFile.readFile(filePath);

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(content);

        content = matcher.replaceAll(replacement);

        WholeFile.writeFile(filePath, content);
    }

    /**
     * This gets the path to file relative the the given parentDir.
     */
    public static String getRelativePath (File parentDir, File file)
        throws IOException
    {
        String path = file.getPath();

        if (parentDir != null) {

            String parentPath = parentDir.getCanonicalPath();
            String childPath = file.getCanonicalPath();

            if (childPath.startsWith(parentPath)) {
                path = childPath.substring(parentPath.length() + 1);
            }
        }
        return path;

    }

    /**
     * File.isAbsolute() does not recognize unix-style absolute paths on Windows systems. This
     * custom isAbsolute() method checks for all unix and Windows style roots to determine if a
     * given path is absolute.
     */
    public static boolean isAbsolute (String path)
    {
        // check for unix-style root
        boolean absolute = path.startsWith("/");

        // check for windows-style roots
        absolute = absolute || path.startsWith("\\");

        if (!absolute) {
            // check for a letter followed by :\
            // e.g. C:\
            absolute = Pattern.matches("^[a-zA-Z]:\\\\.*", path);

        }

        // if it is still not considered an path, then check
        // the default file system isAbsolute() check.
        absolute = absolute || new File(path).isAbsolute();

        return absolute;
    }

    public static boolean isAbsolute (File path)
    {
        return isAbsolute(path.getPath());
    }

    /**
     * This checks to see if a given file path is a root directory.
     */
    public static boolean isRoot (String path)
    {
        return isRoot(new File(path));
    }

    /**
     * This checks to see if a given file path is a root directory.
     */
    public static boolean isRoot (File path)
    {
        boolean match = false;

        try {
            String normalPath = path.getCanonicalPath();

            // check for the unix root
            match = normalPath.equals("/");

            // otherwise, check for windows roots
            if (!match) {
                // check for a letter followed by :\
                // e.g. C:\
                match = Pattern.matches("[a-zA-Z]:\\\\", normalPath);
            }

        } catch (IOException ex) {
            Logger.warning("Could not get canonical path for [" + path + "]: " + ex);
        }

        return match;

    }

    /**
     * Checks to see if the given file exists.
     */
    public static boolean exists (String path)
    {
        return new File(path).exists();
    }

    /**
     * File Copy method where filenames to be copied are indicated by match to a standard regular
     * expression. The regex should match to the COMPLETE filename in the source directory. .*
     * groupings can be used to accomplish this.
     *
     * @param inputDir
     *            the directory to copy file from
     * @param outputDir
     *            the destination directory for the selected files.
     * @param regex
     *            a standard regular expression that matches the complete filname
     * @return a newline separated list of the files copied.
     */
    public static String regexCopy (String inputDir, String outputDir, String regex)
    {
        File source = new File(inputDir);
        File target = new File(outputDir);
        String retVal = "";
        if (!source.isDirectory() || !target.isDirectory()) {
            // Not sure of what to do here ...
            // Should throw exception?
            return "";
        }
        File[] files = source.listFiles(getFilter(regex));

        if (files.length == 0) {
            return "The list of files is empty - bad regex probably";
        }

        for (int i = 0; i < files.length; ++i) {
            try {
                copy(files[i], target);
                retVal += ("\n" + files[i].getName());
            } catch (FileNotFoundException fnf) {
                fnf.printStackTrace(System.err);
            } catch (IOException io) {
                io.printStackTrace(System.err);
            }
        }
        return retVal;
    }

    /**
     * File delete method that accepts a standard Regular Expression that is used to match to
     * filenames to create a list of files to delete.
     *
     * @param dir
     *            the home directory of the files to be deleted.
     * @param regex
     *            a standard regular expression to match complete filename.
     * @return the list of files deleted.
     */
    public static String regexDelete (String dir, String regex)
    {
        File source = new File(dir);
        String retVal = "";
        if (!source.isDirectory()) {
            return "";
        }
        File[] files = source.listFiles(getFilter(regex));

        if (files.length == 0) {
            return "The list of files is empty - possibly bad regex expression";
        }

        for (int i = 0; i < files.length; ++i) {
            // Skip directories
            if (files[i].isDirectory()) {
                continue;
            }
            delete(files[i]);
            retVal += ("\n" + files[i].getName());
        }
        return retVal;
    }

    /**
     * Creates a filter for regexCopy method. The deafult behavior is to match to the
     * ENTIRE filename. Users should employ regex .* semantics to allow their regex to match to the
     * complete filename.
     *
     * @param regex
     *            a regular expression that for selecting filenames
     * @return a FilenameFilter that returns true for the regex
     */
    protected static FilenameFilter getFilter (final String regex)
    {
        return new FilenameFilter() {

            @Override
            public boolean accept (File dir, String name)
            {
                //
                try {
                    if (Pattern.matches(regex, name)) {
                        return true;
                    }

                    /*
                     * This code supports matching to a substring in the filename. This is alternate
                     * code in case the default behavior is unsuitable.
                     *
                     * Pattern pattern = Pattern.compile(regex); Matcher matcher =
                     * pattern.matcher(name); if ( matcher.find()) { return true; }
                     */
                } catch (PatternSyntaxException pse) {
                    // If exception is thrown return false.
                    return false;
                }
                return false;
            }
        };
    }

    /**
     * Utility method for getting the parent path of a given file path string.
     *
     * @param path
     *            String a file path
     * @return String the parent file path string
     */
    public static String getParentPath (String path)
    {
        return new File(path).getParent();
    }
}
