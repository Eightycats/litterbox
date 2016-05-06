package com.eightycats.litterbox.io.file;

import java.io.File;
import java.io.FileFilter;

/**
 * Visits every file in a directory.
 */
public class DirectoryIterator
{
    protected String _parent;

    protected FileFilter _filter;

    protected boolean _recurse;

    public DirectoryIterator (String parentPath)
    {
        this(parentPath, true);
    }

    public DirectoryIterator (String parentPath, boolean recurse)
    {
        _parent = parentPath;
        _recurse = recurse;
    }

    /**
     * Filters out files that do not end with the given suffix.
     */
    public void setNameFilter (final String suffix)
    {
        _filter = new FileFilter() {
            @Override public boolean accept (File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(suffix);
            }
        };
    }

    /**
     * Starts processing the parent directory. Applies the given reader to each file we find.
     */
    public void run (LineReader fileReader)
    {
        run(fileReader, new File(_parent));
    }

    protected void run (LineReader fileReader, File parentDir)
    {
        File[] files;
        if (_filter != null) {
            files = parentDir.listFiles(_filter);
        } else {
            files = parentDir.listFiles();
        }

        for (File file : files) {
            if (file.isDirectory()) {
                if (_recurse) {
                    run(fileReader, file);
                }
            } else {
                System.out.println("Processing file: " + file.getAbsolutePath());
                fileReader.read(file.getAbsolutePath());
            }
        }
    }
}
