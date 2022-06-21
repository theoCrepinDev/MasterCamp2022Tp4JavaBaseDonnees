package db;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import document.Document;

/**
 * connection 
 */
public class ConnectMySQL {

  public static Statement connection(String tableName, String user, String password) throws ClassNotFoundException, SQLException{
    //étape 1: charger la classe de driver*
    Class.forName("com.mysql.jdbc.Driver");

    //étape 2: créer l'objet de connexion
    Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/" + tableName + "?characterEncoding=latin1", user, password);

    //étape 3: créer l'objet statement 
    Statement stmt = conn.createStatement();
    return stmt;
     
  }

  //méthode pour récupérer l'id d'un topic
  public static int getIdTopic(String topic, java.sql.Statement db){
    String sql = "SELECT TopicID FROM topic WHERE Topic='";
    try {
      ResultSet result = db.executeQuery(sql + topic +"'");
      if(result.next()){
        return result.getInt("TopicID");
      }else{
        System.out.println("Ajout du topic : " + topic +" ... ");
        String sqlAjoutTopic = "INSERT INTO topic (topic) values ('" + topic + "')";
        db.executeUpdate(sqlAjoutTopic);
        return getIdTopic(topic, db);
      }
    } catch (SQLException e) {
      System.out.print(e);
      return -1 ;
    }
  }

  //on récupère l'ID de catégory
  public static int getIdCategory(String category, java.sql.Statement db){
    String sql = "SELECT CategoryID FROM Category WHERE Name='";
    try {
      ResultSet result = db.executeQuery(sql + category +"'");
      if(result.next()){
        //si catégorie déja dans la bdd
        return result.getInt("CategoryID");
      }else{
        System.out.println("Ajout de la catégorie : " + category +" ... ");
        String sqlAjoutCategory = "INSERT INTO category (name) values ('" + category + "')";
        db.executeUpdate(sqlAjoutCategory);
        return getIdCategory(category, db);
      }
    } catch (SQLException e) {
      System.out.print(e);
      return -1 ;
    }
  }

  //stockage des tag
  //pour cela on doit vérifier si il ne spnt pas déjà dans la bdd, si ce n'est pas le cas on les ajoutent
  public static void insertTag(ArrayList<String> tags, java.sql.Statement db, String DocumentName){
    //pour chaque tag, on vérifie si il est dans la bdd
    for(String tag : tags){
      try {
        ResultSet res = db.executeQuery("SELECT TagID FROM Tag WHERE Tag = '" + tag + "'");
        if(res.next()){
          //on insere dans la table avoir le tag id et document id
          String sql = "INSERT INTO avoir(DocumentID,TagID) (SELECT d.DocumentID,t.TagID FROM document d, tag t WHERE d.DocumentName = '" +DocumentName +"' AND t.Tag = '"+ tag +"') LIMIT 1";
          db.executeUpdate(sql);
        }else{
          //si le tag n'y est pas on l'insère
          String sql = "INSERT into tag (Tag) values ('" + tag + "')";
          db.executeUpdate(sql);
          String sqlApresInsert = "INSERT INTO avoir(DocumentID,TagID) SELECT d.DocumentID,t.TagID FROM document d, tag t WHERE d.DocumentName = '" +DocumentName +"' AND t.Tag = '"+ tag +"' LIMIT 1";
          db.executeUpdate(sqlApresInsert);
        }
      } catch (SQLException e) {
        System.out.print("Erreur vérification présence du tag " + tag);
        e.printStackTrace();
      }
      
    }
  }

  //méthode pour enregistrer sur la bdd
  public static void insert(Document document, java.sql.Statement db){
    //on récupère l'id du topic
    int topicId = getIdTopic(document.getTopic(), db);
    
    //on récupère id de la category
    int categoryID = getIdCategory(document.getCategory(), db);

    if(categoryID >= 0 && topicId >= 0){
      //insertion dans la bdd
      String sql;
      if(document.getDocumentDate() == null){
        sql = "INSERT INTO DOCUMENT (DocumentName, StorageAdresse, TopicID, CategoryID) values ('%s', '%s', %2d, %2d)"
          .formatted(
          document.getDocumentName(),
          document.getStorageAdress(), 
          topicId, 
          categoryID
        );
      }else{
        sql = "INSERT INTO DOCUMENT (DocumentName, DocumentDate, StorageAdresse, TopicID, CategoryID) values ('%s', '%s', '%s', %2d, %2d)"
          .formatted(
          document.getDocumentName(),
          document.getDocumentDate(), 
          document.getStorageAdress(), 
          topicId, 
          categoryID
        );
      }
        

        //exécuter la requete
        try {
          db.executeUpdate(sql);
          //on insère les tags
          insertTag(document.getTag(), db, document.getDocumentName());
          System.out.println("Document inséré avec succé !");
        } catch (SQLException e) {
          System.out.println("Erreure dans l'insertion ...");
          System.out.println(e);
        }

    }else{
      System.out.println("Erreur de catégorie et/ou de topic");
    }
  }

  //méthode affichage tous les livres order by Category Name
  public static ArrayList<Document> selectAllOrderByCategory(java.sql.Statement db){
    ArrayList<Document> result = new ArrayList<>();
    String sql = "SELECT d.DocumentID, d.DocumentName, d.DocumentDate, d.StorageAdresse, c.Name, t.topic FROM document d NATURAL JOIN category c NATURAL JOIN topic t order by c.Name";

    try{
      ResultSet res = db.executeQuery(sql);
      ArrayList<Integer> documentID= new ArrayList<>();
      while(res.next()){
        int docID = res.getInt("DocumentID");
        Document doc = new Document(res.getString("DocumentName"), Date.valueOf(res.getString("DocumentDate")), res.getString("StorageAdresse"), res.getString("Name"), res.getString("topic"));
        documentID.add(docID);
        result.add(doc);      
      }
      for(int i =0; i < result.size(); i++){
        String sqlTag = "SELECT Tag FROM tag Natural JOIN avoir WHERE DocumentID =" + documentID.get(i);
        ResultSet resTag = db.executeQuery(sqlTag);
        while(resTag.next()){
          result.get(i).addTag(resTag.getString("Tag"));
        }
      }
       return result;     
    }
    catch(SQLException e){
      System.out.println(e);
      System.out.println("Erreur dans le Select");
      return result;
    }
  }

  //méthode affichage des livres de la catégorie donnée
  public static ArrayList<Document> selectFromCategory(String category, java.sql.Statement db){
    ArrayList<Document> result = new ArrayList<>();
    String sql = "SELECT d.DocumentID, d.DocumentName, d.DocumentDate, d.StorageAdresse, c.Name, t.topic FROM document d NATURAL JOIN category c NATURAL JOIN topic t WHERE c.name ='" + category +"'";

    try{
      ResultSet res = db.executeQuery(sql);
      ArrayList<Integer> documentID= new ArrayList<>();
      while(res.next()){
        int docID = res.getInt("DocumentID");
        Document doc = new Document(res.getString("DocumentName"), Date.valueOf(res.getString("DocumentDate")), res.getString("StorageAdresse"), res.getString("Name"), res.getString("topic"));
        documentID.add(docID);
        result.add(doc);      
      }
      for(int i =0; i < result.size(); i++){
        String sqlTag = "SELECT Tag FROM tag Natural JOIN avoir WHERE DocumentID =" + documentID.get(i);
        ResultSet resTag = db.executeQuery(sqlTag);
        while(resTag.next()){
          result.get(i).addTag(resTag.getString("Tag"));
        }
      }
       return result;     
    }
    catch(SQLException e){
      System.out.println(e);
      System.out.println("Erreur dans le Select");
      return result;
    }
  }

  //méthode affichage livres order by Topic Name
  public static ArrayList<Document> selectAllOrderByTopic(java.sql.Statement db){
    ArrayList<Document> result = new ArrayList<>();
    String sql = "SELECT d.DocumentID, d.DocumentName, d.DocumentDate, d.StorageAdresse, c.Name, t.topic FROM document d NATURAL JOIN category c NATURAL JOIN topic t order by t.Topic";

    try{
      ResultSet res = db.executeQuery(sql);
      ArrayList<Integer> documentID= new ArrayList<>();
      while(res.next()){
        int docID = res.getInt("DocumentID");
        Document doc = new Document(res.getString("DocumentName"), Date.valueOf(res.getString("DocumentDate")), res.getString("StorageAdresse"), res.getString("Name"), res.getString("topic"));
        documentID.add(docID);
        result.add(doc);      
      }
      for(int i =0; i < result.size(); i++){
        String sqlTag = "SELECT Tag FROM tag Natural JOIN avoir WHERE DocumentID =" + documentID.get(i);
        ResultSet resTag = db.executeQuery(sqlTag);
        while(resTag.next()){
          result.get(i).addTag(resTag.getString("Tag"));
        }
      }
       return result;     
    }
    catch(SQLException e){
      System.out.println(e);
      System.out.println("Erreur dans le Select");
      return result;
    }
  }

  //méthode affichage livres order by Topic Name
  public static ArrayList<Document> selectFromTopic(String topic, java.sql.Statement db){
    ArrayList<Document> result = new ArrayList<>();
    String sql = "SELECT d.DocumentID, d.DocumentName, d.DocumentDate, d.StorageAdresse, c.Name, t.topic FROM document d NATURAL JOIN category c NATURAL JOIN topic t WHERE topic='" + topic +"'";

    try{
      ResultSet res = db.executeQuery(sql);
      ArrayList<Integer> documentID= new ArrayList<>();
      while(res.next()){
        int docID = res.getInt("DocumentID");
        Document doc = new Document(res.getString("DocumentName"), Date.valueOf(res.getString("DocumentDate")), res.getString("StorageAdresse"), res.getString("Name"), res.getString("topic"));
        documentID.add(docID);
        result.add(doc);      
      }
      for(int i =0; i < result.size(); i++){
        String sqlTag = "SELECT Tag FROM tag Natural JOIN avoir WHERE DocumentID =" + documentID.get(i);
        ResultSet resTag = db.executeQuery(sqlTag);
        while(resTag.next()){
          result.get(i).addTag(resTag.getString("Tag"));
        }
      }
       return result;     
    }
    catch(SQLException e){
      System.out.println(e);
      System.out.println("Erreur dans le Select");
      return result;
    }
  }

  //méthode pour récupérer le dujet le plus férquent
  public static String mostUsedTopic(java.sql.Statement db){
    String sql = "SELECT count(*), Topic from topic natural join document group by Topic order by count(*) desc limit 1";
    try {
      ResultSet res =db.executeQuery(sql);
      if(res.next()){
        String topicName = res.getString("Topic");
        int count = res.getInt("count(*)");
        System.out.println("Sujet le plus utilisé : " + topicName + " avec " + count +" apparitions ...");
        return topicName;
      }
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("Erreur récupération topic le plus utilisé ...");
    }
    return "0";
  }

  //méthode pour récupérer les occurence des tags
  public static HashMap<String, Integer> countsOfTags(java.sql.Statement db){
    String sql = "SELECT count(*), Tag from Tag natural join avoir group by Tag order by count(*) desc";
    HashMap<String, Integer> result = new HashMap<>();

    try {
      ResultSet res = db.executeQuery(sql);
      while(res.next()){
        String tag = res.getString("Tag");
        int count = res.getInt("count(*)");
        System.out.println("Le tag : " + tag + " apparait " + count + " fois ...");
        result.put(tag, count);
      }
    } catch (Exception e) {
      System.out.print(e);
    }
    return result;
  }

  public static void putDateLastNullDateInserted(java.sql.Statement db, Date date){
    //requête pour récupérer l'id du dernier doc inséré
    String sqlGetLastID = "SELECT DocumentID from document order by DocumentID desc limit 1";
    String sql = "UPDATE document set DocumentDate ='" + date +"' WHERE DocumentID = (SELECT * FROM(%s)tblTemp)"
      .formatted(sqlGetLastID);
    try {
      db.executeUpdate(sql);
      System.out.println("Mis a jour faite ! ");
    } catch (Exception e) {
      System.out.println("Erreur dans la mise a jour ....");
      System.out.println(e);
    }
  }

  //fonction pour inséré un nouveau tag au dernier document
  public static void insertNewTagToLast(java.sql.Statement db, String tag){
    //on a besoin du nom du dernier document pour utiliser la fonction insertTag déjà codée
    String sql = "SELECT DocumentName FROM document ORDER BY DocumentID desc limit 1";
    try{
      ResultSet res = db.executeQuery(sql);
      String documentName = "";
      if(res.next()){
        documentName = res.getString("DocumentName");
      }
      ArrayList<String> tagList = new ArrayList<>();
      tagList.add(tag);
      insertTag(tagList, db, documentName);
      System.out.println("Tag ajouté !");
    }catch(SQLException e){
      System.out.println("PRobleme fonction insertNewTagToLast");
      System.out.print(e);
    }
  }

}