/**
 * Reference to:
 * https://stackoverflow.com/questions/6159118/using-java-to-pull-data-from-a-webpage
 * https://stackoverflow.com/questions/7298817/making-image-scrollable-in-jframe-contentpane/7299067
 * https://myatlascms.com/map/accessible.php?id=1809
 * https://htmlunit.sourceforge.io/apidocs/index.html
 * https://www.scrapingbee.com/blog/introduction-to-web-scraping-with-java/
 * https://www.w3schools.com/xml/xpath_syntax.asp
 * https://www.webscrapingapi.com/java-web-scraping
 */

package Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public abstract class PlaceInfo {
    static final String MAINURL = "https://myatlascms.com/map/accessible.php";
    static final String SPECPLACEINFOXPATH = "//ol[@role='list']/li[@role='listitem']";
    static final String LISTXPATH = "//li[@role='listitem']/a";
    public static HashMap<String,String> foodTypeLIST;

    /**
     * Return a list of HTMLListItem from url and xPathExp
     * @param webUrl
     * @return
     * @throws IOException
     */
    public static List<HtmlListItem> getLiforSpecPlace(String webUrl) throws IOException {
        //Build webclient
        WebClient client = new WebClient(BrowserVersion.CHROME);

        //Get web page and close safely
        HtmlPage page = client.getPage(MAINURL+webUrl);

        client.getCurrentWindow().getJobManager().removeAllJobs();
        client.close();
        client.close();

        //Retrieve <li> elements
        List<HtmlListItem> specplace = page.getByXPath(SPECPLACEINFOXPATH);

        return specplace;
    }


    /**
     * Return a list of HTMLAnchor from url and xPathExp
     * @param url
     * @return
     * @throws IOException
     */
    public static List<HtmlAnchor> getAnchorsofNamesURL(String url) throws IOException {
        //WebURL
        String webUrl = MAINURL + url;

        //Build webclient
        WebClient client = new WebClient(BrowserVersion.CHROME);

        //Get web page and close safely
        HtmlPage page = client.getPage(webUrl);

        client.getCurrentWindow().getJobManager().removeAllJobs();
        client.close();
        client.close();

        //Retrieve <li> elements
        List<HtmlAnchor> anchors = page.getByXPath(LISTXPATH);
        return anchors;
    }


    /**
     * Return exact name of place
     * @param anchor
     * @return
     */
    public static String name (HtmlAnchor anchor){
        return anchor.asNormalizedText();
    }


    /**
     * Return a list of HTMLAnchor from url and xPathExp
     * @param url
     * @param xPathExp
     * @return
     * @throws IOException
     */
    public static List<HtmlAnchor> getAnchorsofNamesURL(String url, String xPathExp) throws IOException {
        //WebURL
        String webUrl = MAINURL + url;

        //Build webclient
        WebClient client = new WebClient(BrowserVersion.CHROME);

        //Get web page and close safely
        HtmlPage page = client.getPage(webUrl);

        client.getCurrentWindow().getJobManager().removeAllJobs();
        client.close();
        client.close();

        //Retrieve <li> elements
        List<HtmlAnchor> anchors = page.getByXPath(xPathExp);
        return  anchors;
    }


    /**
     * Return a place's general information from web
     * @param webUrl
     * @return Place's general info
     * @throws IOException
     */
    public static String specPlace(String webUrl) throws IOException {
        //Retrieve <li> elements
        List<HtmlListItem> anchors = getLiforSpecPlace(webUrl);

        //Get the last li element to avoid duplicates
        String placeInfo = anchors.get(anchors.size()-1).asNormalizedText();

        return placeInfo;
    }

    /**
     * Return the list of li elements in string
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getList(String url) throws IOException {
        //Retrieve <li> elements
        List<HtmlAnchor> anchors = getAnchorsofNamesURL(url);

        //Store places
        StringBuilder output = new StringBuilder();
        for(HtmlAnchor anchor : anchors){
            output.append(anchor.asNormalizedText()).append(",").append(anchor.getHrefAttribute()).append("\n");
        }
        return output.toString();
    }


    /**
     * Return the list of li elements in string
     * @param url
     * @param xPathEpr
     * @return
     * @throws IOException
     */
    public String getList(String url, String xPathEpr) throws IOException {
        //Retrieve <li> elements
        List<HtmlAnchor> anchors = getAnchorsofNamesURL(url, xPathEpr);
        //Store places
        StringBuilder output = new StringBuilder();
        for(HtmlAnchor anchor : anchors){
            output.append(anchor.asNormalizedText()).append(",").append(anchor.getHrefAttribute()).append("\n");
        }
        return output.toString();
    }

    /**
     * Save file to resource
     * @param file
     * @param input
     * @throws IOException
     */
    public void saveListFile(File file, String input) throws IOException{
        FileWriter fout = new FileWriter(file);
        Writer writer = new BufferedWriter(fout);
        writer.write(input);
        writer.close();
    }

    public abstract ArrayList<Place> getTotlist() throws IOException;

}
