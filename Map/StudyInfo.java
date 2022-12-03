package Map;

import java.io.*;

public class StudyInfo extends PlaceInfo {
    String studyList;
    public StudyInfo() throws IOException {
        super("?id=1809&cId=48697");
        this.studyList = getList(this.url);
//        saveStudyListFile();
    }

    public void saveStudyListFile() throws IOException {
        //create file
        String filename = "studySpacesList.txt";
        File file = new File("./resources/"+filename);

        saveListFile(file, this.studyList);
    }
}
