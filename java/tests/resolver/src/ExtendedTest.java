// ManagerTest.java - A test of multiple Catalog Managers

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.Date;
import java.util.GregorianCalendar;
import java.net.MalformedURLException;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.*;

import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.apps.XParseError;

/**
 * <p>A simple command-line XML parsing application.</p>
 *
 * <p>This class tests the ability of the XML Catalog code to support
 * multiple catalog manager instances.
 * </p>
 *
 * <p>Usage: ManagerTest document.xml</p>
 *
 * <p>The process ends with error-level 1, if there errors.</p>
 *
 * @see org.apache.xml.resolver.tools.ResolvingXMLReader
 *
 * @author Norman Walsh
 * <a href="mailto:Norman.Walsh@Sun.COM">Norman.Walsh@Sun.COM</a>
 *
 * @version 1.0
 */
public class ExtendedTest extends TestParser
{
  /** The main entry point */
  public static void main (String[] args)
    throws FileNotFoundException, IOException {

    for (int i=0; i<args.length; i++) {
      if (args[i].equals("-c")) {
	++i;
	catalogFiles.add(args[i]);
	continue;
      }

      xmlfile = args[i];
    }

    if (xmlfile == null) {
      System.out.println("Usage: ExtendedTest [ -c catalog ] xmlfile");
      System.exit(1);
    }

    System.out.println("Creating my CatalogManager (Resolver)");
    CatalogManager myManager = new CatalogManager();
    myManager.setUseStaticCatalog(false);
    myManager.setVerbosity(2);
    myManager.setCatalogClassName("org.apache.xml.resolver.Resolver");
    System.out.println("Parsing with my CatalogManager");
    ResolvingXMLReader reader = new ResolvingXMLReader(myManager);
    parse(reader);
  }
}
