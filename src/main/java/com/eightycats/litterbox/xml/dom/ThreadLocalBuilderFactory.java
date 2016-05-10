/**
 * Copyright 2016 Matthew A Jensen <eightycats@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
