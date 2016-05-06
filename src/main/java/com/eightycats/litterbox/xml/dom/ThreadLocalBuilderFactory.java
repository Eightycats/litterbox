package com.eightycats.litterbox.xml.dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Thread local/thread safe DocumentBuilderFactory.
 */
public class ThreadLocalBuilderFactory extends ThreadLocal<DocumentBuilderFactory>
{
    private static ThreadLocalBuilderFactory factory = new ThreadLocalBuilderFactory();

    public static DocumentBuilderFactory getFactory ()
    {
        return getFactory(new DocumentBuilderConfig());
    }

    public static DocumentBuilderFactory getFactory (DocumentBuilderConfig config)
    {
        DocumentBuilderFactory builderFactory = factory.get();
        config.configureFactory(builderFactory);
        return builderFactory;
    }

    public static DocumentBuilder getDocumentBuilder ()
        throws ParserConfigurationException
    {
        DocumentBuilder builder = getFactory().newDocumentBuilder();
        // builder.setEntityResolver(entityResolver);
        // builder.setErrorHandler(entityResolver);
        return builder;
    }

    public static DocumentBuilder getDocumentBuilder (DocumentBuilderConfig config)
        throws ParserConfigurationException
    {
        DocumentBuilder builder = getFactory(config).newDocumentBuilder();
        // builder.setEntityResolver(entityResolver);
        // builder.setErrorHandler(entityResolver);
        return builder;
    }

    /**
     * This method is called by ThreadLocal when there is not yet a a Thread-specific
     * DocumentBuilderFactory for the current thread.
     */
    @Override
    protected synchronized DocumentBuilderFactory initialValue ()
    {
        return DocumentBuilderFactory.newInstance();
    }
}
