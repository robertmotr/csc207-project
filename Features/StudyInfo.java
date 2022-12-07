package Features;

import Features.Factory;
import Features.Place;
import Features.PlaceInfo;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudyInfo extends PlaceInfo {

    String STUDYURL = "?id=1809&cId=48697";

    ArrayList<Place> studyLIST = new ArrayList<>();

    /**
     * Update studyLIST
     * @param l
     */
    private void updateList(List<HtmlAnchor> l){
        for(HtmlAnchor build : l){
            String name = build.asNormalizedText();
            String url = build.getHrefAttribute();
            this.studyLIST.add(Factory.createPlace(name, url));
        }
    }

    /**
     *
     * @return Study place list
     * @throws IOException
     */
    @Override
    public ArrayList<Place> getTotlist() throws IOException {
        List<HtmlAnchor> stu = getAnchorsofNamesURL(STUDYURL);
        updateList(stu);
        return this.studyLIST;
    }

    /**
     * Save study list to file, used to generate back up
     * @throws IOException
     */
    public void saveStudyListFile() throws IOException {
        //create file
        String filename = "studySpacesList.txt";
        File file = new File("./resources/"+filename);

        saveListFile(file, getList(STUDYURL));
    }
}
