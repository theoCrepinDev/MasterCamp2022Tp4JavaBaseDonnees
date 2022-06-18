package db;
import java.sql.*;
import java.util.ArrayList;

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
  public static int getIdTopic(String Topic, java.sql.Statement db){
    String sql = "SELECT TopicID FROM topic WHERE Topic='";
    try {
      ResultSet result = db.executeQuery(sql + Topic +"'");
      if(result.next()){
        return result.getInt("TopicID");
      }
    } catch (SQLException e) {
      System.out.print(e);
      return -1 ;
    }
    return -2;
  }

  //on récupère l'ID de catégory
  public static int getIdCategory(String category, java.sql.Statement db){
    String sql = "SELECT CategoryID FROM Category WHERE Name='";
    try {
      ResultSet result = db.executeQuery(sql + category +"'");
      if(result.next()){
        return result.getInt("CategoryID");
      }
    } catch (SQLException e) {
      System.out.print(e);
      return -1 ;
    }
    return -2;
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
      String sql = "INSERT INTO DOCUMENT (DocumentName, DocumentDate, StorageAdresse, TopicID, CategoryID) values ('%s', '%s', '%s', %2d, %2d)"
        .formatted(
          document.getDocumentName(),
          document.getDocumentDate(), 
          document.getStorageAdress(), 
          topicId, 
          categoryID
        );

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

    }
  }

}