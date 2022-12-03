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
import java.util.List;



public class PlaceInfo {
    public final String MAINURL = "https://myatlascms.com/map/accessible.php";
    public final String SPECPLACEINFOXPATH = "//ol[@role='list']/li[@role='listitem']";
    public final String LISTXPATH = "//li[@role='listitem']/a";
    public String totlist;
    public String url;

    public PlaceInfo(String url){
        this.url = url;
    }

    /**
     * Return a place's general information from web
     * @param webUrl
     * @return Place's general info
     * @throws IOException
     */
    public String specPlace(String webUrl) throws IOException {

        //Build webclient
        WebClient client = new WebClient(BrowserVersion.CHROME);

        //Get web page and close safely
        HtmlPage page = client.getPage(MAINURL+webUrl);

        client.getCurrentWindow().getJobManager().removeAllJobs();
        client.close();
        client.close();

        //Retrieve <li> elements
        List<HtmlListItem> anchors = page.getByXPath(SPECPLACEINFOXPATH);

        //Get the third li element
        String placeInfo = anchors.get(anchors.size()-1).asNormalizedText();

        return placeInfo;
    }



    public String getList(String url) throws IOException {
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

        //Store places
        StringBuilder output = new StringBuilder();
        for(HtmlAnchor anchor : anchors){
            output.append(anchor.asNormalizedText()).append(",").append(anchor.getHrefAttribute()).append("\n");
        }
        return output.toString();
    }


    public String getList(String url, String xPathEpr) throws IOException {
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
        List<HtmlAnchor> anchors = page.getByXPath(xPathEpr);
        //Store places
        StringBuilder output = new StringBuilder();
        for(HtmlAnchor anchor : anchors){
            output.append(anchor.asNormalizedText()).append(",").append(anchor.getHrefAttribute()).append("\n");
        }
        return output.toString();
    }

    public void saveListFile(File file, String input) throws IOException{
        FileWriter fout = new FileWriter(file);
        Writer writer = new BufferedWriter(fout);
        writer.write(input);
        writer.close();
    };
}
