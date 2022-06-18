package document;

import java.sql.Date;
import java.util.ArrayList;

public class Document {
    //private int DocumentID;
    //private static int DocumentIDActuel;
    private String documentName;
    private Date documentDate;
    private String storageAdress;
    private String category;
    private String topic;
    private ArrayList<String> tag;

    //constructors
    public Document(String documentName, String documentDate){
        this.documentName = documentName;
        this.documentDate = Date.valueOf(documentDate);
        this.tag = new ArrayList<>();
    }


    public Document(String documentName, Date documentDate, String storageAdress, String category, String topic, ArrayList<String> tag) {
        this.documentName = documentName;
        this.documentDate = documentDate;
        this.storageAdress = storageAdress;
        this.category = category;
        this.topic = topic;
        this.tag = tag;
    }

    public Document(String documentName, Date documentDate, String storageAdress, String category, String topic) {
        this.documentName = documentName;
        this.documentDate = documentDate;
        this.storageAdress = storageAdress;
        this.category = category;
        this.topic = topic;
        this.tag = new ArrayList<>();
    }


    //constructeur sans date mais avec tag liste
    public Document(String documentName, String storageAdress, String category, String topic, ArrayList<String> tag) {
        this.documentName = documentName;
        this.storageAdress = storageAdress;
        this.category = category;
        this.topic = topic;
        this.tag = tag;
    }



    //add a tag
    public void addTag(String tag){
        this.tag.add(tag);
    }


    //guetteur and setter

    public String getDocumentName() {
        return this.documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Date getDocumentDate() {
        return this.documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getStorageAdress() {
        return this.storageAdress;
    }

    public void setStorageAdress(String storageAdress) {
        this.storageAdress = storageAdress;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<String> getTag() {
        return this.tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    @Override
    public String toString(){
        return "Nom : " + this.documentName + "\nDate : " + documentDate + "\nAdresse Stockage : " + storageAdress + "\nCatégorie : " +category + "\nTopic : " + topic + "\nTags : " + tag + "\n\n";
    }
    
}