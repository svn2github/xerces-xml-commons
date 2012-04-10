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
public abstract class TestParser
{
  protected static String  xmlfile    = null;
  protected static int     maxErrs    = 10;
  protected static boolean nsAware    = true;
  protected static boolean validating = true;
  protected static boolean showErrors = true;
  protected static boolean showWarnings = true;
  protected static Vector catalogFiles = new Vector();

  protected static void parse(ResolvingXMLReader reader)
    throws MalformedURLException, FileNotFoundException, IOException {
    try {
      reader.setFeature("http://xml.org/sax/features/namespaces", nsAware);
      reader.setFeature("http://xml.org/sax/features/validation", validating);
    } catch (SAXException e) {
      // nop;
    }

    Catalog catalog = reader.getCatalog();

    System.out.println("Parsing with " + catalog);

    for (int count = 0; count < catalogFiles.size(); count++) {
      String file = (String) catalogFiles.elementAt(count);
      catalog.parseCatalog(file);
    }

    XParseError xpe = new XParseError(showErrors, showWarnings);
    xpe.setMaxMessages(maxErrs);
    reader.setErrorHandler(xpe);

    String parseType = validating ? "validating" : "well-formed";
    String nsType = nsAware ? "namespace-aware" : "namespace-ignorant";
    if (maxErrs > 0) {
      System.out.println("Attempting "
			 + parseType
			 + ", "
			 + nsType
			 + " parse");
    }

    Date startTime = new Date();

    try {
      reader.parse(xmlfile);
    } catch (SAXException sx) {
      System.out.println("SAX Exception: " + sx);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Date endTime = new Date();

    long millisec = endTime.getTime() - startTime.getTime();
    long secs = 0;
    long mins = 0;
    long hours = 0;

    if (millisec > 1000) {
      secs = millisec / 1000;
      millisec = millisec % 1000;
    }

    if (secs > 60) {
      mins = secs / 60;
      secs = secs % 60;
    }

    if (mins > 60) {
      hours = mins / 60;
      mins = mins % 60;
    }

    if (maxErrs > 0) {
      System.out.print("Parse ");
      if (xpe.getFatalCount() > 0) {
	System.out.print("failed ");
      } else {
	System.out.print("succeeded ");
	System.out.print("(");
	if (hours > 0) {
	  System.out.print(hours + ":");
	}
	if (hours > 0 || mins > 0) {
	  System.out.print(mins + ":");
	}
	System.out.print(secs + "." + millisec);
	System.out.print(") ");
      }
      System.out.print("with ");

      int errCount = xpe.getErrorCount();
      int warnCount = xpe.getWarningCount();

      if (errCount > 0) {
	System.out.print(errCount + " error");
	System.out.print(errCount > 1 ? "s" : "");
	System.out.print(" and ");
      } else {
	System.out.print("no errors and ");
      }

      if (warnCount > 0) {
	System.out.print(warnCount + " warning");
	System.out.print(warnCount > 1 ? "s" : "");
	System.out.print(".");
      } else {
	System.out.print("no warnings.");
      }

      System.out.println("");
    }
  }
}
