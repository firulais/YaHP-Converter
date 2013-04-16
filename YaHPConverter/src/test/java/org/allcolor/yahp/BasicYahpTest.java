/*
 * BasicYahpTest.java
 *
 * created on: 1/3/13 4:04 PM
 * Copyright(c) 2002-2013 Thetus Corporation.  All Rights Reserved.
 *                        www.thetus.com
 *
 * Use of copyright notice does not imply publication or disclosure.
 * THIS SOFTWARE CONTAINS CONFIDENTIAL AND PROPRIETARY INFORMATION CONSTITUTING VALUABLE TRADE SECRETS
 *  OF THETUS CORPORATION, AND MAY NOT BE:
 *  (a) DISCLOSED TO THIRD PARTIES;
 *  (b) COPIED IN ANY FORM;
 *  (c) USED FOR ANY PURPOSE EXCEPT AS SPECIFICALLY PERMITTED IN WRITING BY THETUS CORPORATION.
 *
 * Created using IntelliJ IDEA.
 * SVN status:
 *  $HeadURL: $
 *  $Id: $
 *  $Revision: $
 *  $Date: $
 *  $Author: $
 */
package org.allcolor.yahp;

import org.allcolor.yahp.converter.CYaHPConverter;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Properties;

/**
 * //TODO: fix these tests so they can just run out of the box
 */
public class BasicYahpTest {

    public void createPdf() throws Exception {

        CYaHPConverter converter = new CYaHPConverter();

        String input = "http://www.google.com";
        String output = "output.pdf";

        Properties props = new Properties();
        props.put(IHtmlToPdfTransformer.HTML_CLEANING_LIBRARY, "jsoup");

        FileOutputStream out = new FileOutputStream(output);
        converter.convertToPdf(new URL(input),
				IHtmlToPdfTransformer.A4P, new LinkedList(), out,
				props);


    }

    /**
     * Jtidy has a problem where if you have a script tag that contains anything inside it that looks like a closing html tag
     * (like this: </foo>)
     * then jtidy errors out and doesn't do any tidying. Yahp needs to preemptively strip out script tags in order to stop from
     * triggering this jtidy bug.
     * @throws Exception
     */
    public void testScriptWithTagInside() throws Exception {
         CYaHPConverter converter = new CYaHPConverter();

        String output = "output.pdf";
        String html = "<html><script> \"</foo>\" </script></html>";

        Properties props = new Properties();

        FileOutputStream out = new FileOutputStream(output);
        converter.convertToPdf(html,
				IHtmlToPdfTransformer.A4P, new LinkedList(), "http://localhost", out,
				props);
    }

    /**
     * jtidy makes this fail because the html is invalid
     * @throws Exception
     */
    public void testInvalidHtml() throws Exception {
         CYaHPConverter converter = new CYaHPConverter();

        String output = "output.pdf";
        String html = "<html>this is invalid html but we should get a best-effort rendering like browsers give. <asdf \"</foo>\" </script></html>";

        Properties props = new Properties();

        FileOutputStream out = new FileOutputStream(output);
        converter.convertToPdf(html,
				IHtmlToPdfTransformer.A4P, new LinkedList(), "http://localhost", out,
				props);
    }


    public void testLocalHtml() throws Exception {
        CYaHPConverter converter = new CYaHPConverter();
        File f = new File("/home/slavelle/yahptest.html");
//        String input = FileUtils.readFileToString(f, "UTF-8");
        String url = "file://" + f.getAbsolutePath();
        String output = "output.pdf";

        Properties props = new Properties();
        props.put(IHtmlToPdfTransformer.HTML_CLEANING_LIBRARY, "jsoup");

        FileOutputStream out = new FileOutputStream(output);

        converter.convertToPdf(new URL(url),
				IHtmlToPdfTransformer.A4P, new LinkedList(), out,
				props);

//        converter.convertToPdf(input,
//				IHtmlToPdfTransformer.A4P, new LinkedList(), "http://localhost", out,
//				props);
    }
}
