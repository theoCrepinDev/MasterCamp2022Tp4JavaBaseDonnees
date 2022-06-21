
import java.io.Console;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import document.Document;
import db.ConnectMySQL;

public class Main {
     private static Document document1;
     private static Document document2;
     private static Document document3;
     private static Document document4;
     private static Document document5;
     private static java.sql.Statement db;

     public static void creation5Document(){
          //création de 5 documents:
        ArrayList<String> tagDocument1 = new ArrayList<>();
        tagDocument1.add("Tag1");
        tagDocument1.add("Tag2");
        document1 = new Document("Document test 1", Date.valueOf("2022-10-10"), "Addresse de stockage", "Normes", "Administratif", tagDocument1);
             
        ArrayList<String> tagDocument2 = new ArrayList<>();
        tagDocument2.add("Tag2");
        tagDocument2.add("tag2bis");
        document2 = new Document("Document test Deuxième", Date.valueOf("2022-11-22"), "Addresse de stockage du document 2", "Images", "Personnel", tagDocument2);

        ArrayList<String> tagDocument3 = new ArrayList<>();
        tagDocument3.add("roman");
        tagDocument3.add("plaisir");
        tagDocument3.add("détente");
        document3 = new Document("Livre de roman", Date.valueOf("2000-5-10"), "Addresse de stockage du roman document 3", "Livres", "Divertissement", tagDocument3);

        ArrayList<String> tagDocument4 = new ArrayList<>();
        tagDocument4.add("Tag4");
        tagDocument4.add("Tag2");
        document4 = new Document("Document 4", Date.valueOf("1960-12-10"), "Addresse de stockage dun vieux document", "Archives", "Administratif", tagDocument4);

        ArrayList<String> tagDocument5 = new ArrayList<>();
        tagDocument5.add("Tag5");
        document5 = new Document("Document5", Date.valueOf("2010-10-10"), "Journal du 10/10/2010", "Articles de journaux", "Politique", tagDocument5);
     }

     public static void insertionDocument(Document doc){
          ConnectMySQL.insert(doc, db);
     }

     public static void insertion5documents(Document doc1,Document doc2,Document doc3,Document doc4,Document doc5){
           //test insertion
          insertionDocument(doc1);
          
          insertionDocument(doc2);
         
          insertionDocument(doc3);
          
          insertionDocument(doc4);
          
          insertionDocument(doc5);
     }

     public static void main(String[] args) throws ClassNotFoundException, SQLException {

          //Connection à la bdd

          db = ConnectMySQL.connection("db_tp4mastercamp", "root", "Tekxover123?");
          System.out.println("Connecté à la base de données !");
          // ArrayList<String> doctag = new ArrayList<>();
          // doctag.add("Tag1");
          // doctag.add("roman");
          // doctag.add("Tag2");
          // Document doc = new Document("Livre de la jungle", Date.valueOf("2010-10-10"), "storageAdress", "Livres", "Administratif", doctag);
          // insertionDocument(doc);

          //on récupère les documents ordonnés par cattegory
          //ArrayList<Document> res = ConnectMySQL.selectAllOrderByCategory(db);
          //on récupère les documents ordonnés par topic
          // ArrayList<Document> res = ConnectMySQL.selectAllOrderByTopic(db);
          // on récupère les document pour une certaine catégory
          // ArrayList<Document> res = ConnectMySQL.selectFromCategory("Archives", db);
          //on récupère les documents pour un certain toppic
          // ArrayList<Document> res = ConnectMySQL.selectFromTopic("Administratif", db);
          // on récupère les livres d'un tag donné
          // ArrayList<Document> res = ConnectMySQL.selectFromTag("Tag2", db);
          // System.out.println(res);
          //ConnectMySQL.mostUsedTopic(db);

          //Création dicument sans date
          // ArrayList<String> tagDocument = new ArrayList<>();
          // tagDocument.add("Tag1");
          // tagDocument.add("Tag2Bis");
          // tagDocument.add("Tag4");
          // Document documentSansDate = new Document("Document sans date", "storageAdress", "Documentaires", "Politique", tagDocument);
          // //Ajout document bdd sans date
          // ConnectMySQL.insert(documentSansDate, db);

          //mise a jour de la date du dernier document inséré
          //ConnectMySQL.putDateLastNullDateInserted(db, Date.valueOf("2010-12-10"));

          //Ajout tag au dernier document ajouté
          //ConnectMySQL.insertNewTagToLast(db, "NewTag");

          //test insertion avec nouveau topic et catégory
          // ArrayList<String> tagDocument1 = new ArrayList<>();
          // tagDocument1.add("Tag1");
          // tagDocument1.add("Tag2");
          // document1 = new Document("Document zeztest 1", Date.valueOf("2022-10-10"), "Addresse de stockage", "Normeseze", "Administratifazdad", tagDocument1);
          // insertionDocument(document1);

          // ConnectMySQL.countsOfTags(db);
          db.close();
     }
}
