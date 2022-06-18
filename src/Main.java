
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import document.Document;
import db.ConnectMySQL;

public class Main {
     public static void main(String[] args) throws ClassNotFoundException, SQLException {

        //Connection à la bdd

        java.sql.Statement db = ConnectMySQL.connection("db_tp4", "root", "Tekxover123?");
        System.out.println("Connecté à la base de données !");

        //création de 5 documents:
        ArrayList<String> tagDocument1 = new ArrayList<>();
        tagDocument1.add("Tag1");
        tagDocument1.add("Tag2");
        Document document1 = new Document("Document test 1", Date.valueOf("2022-10-10"), "Addresse de stockage", "Normes", "Administratif", tagDocument1);
             
        ArrayList<String> tagDocument2 = new ArrayList<>();
        tagDocument2.add("Tag2");
        tagDocument2.add("tag2bis");
        Document document2 = new Document("Document test Deuxième", Date.valueOf("2022-11-22"), "Addresse de stockage du document 2", "Images", "Personnel", tagDocument2);

        ArrayList<String> tagDocument3 = new ArrayList<>();
        tagDocument3.add("roman");
        tagDocument3.add("plaisir");
        tagDocument3.add("détente");
        Document document3 = new Document("Livre de roman", Date.valueOf("2000-5-10"), "Addresse de stockage du roman document 3", "Livres", "Divertissement", tagDocument3);

        ArrayList<String> tagDocument4 = new ArrayList<>();
        tagDocument4.add("Tag4");
        tagDocument4.add("Tag2");
        Document document4 = new Document("Document 4", Date.valueOf("1960-12-10"), "Addresse de stockage dun vieux document", "Archives", "Administratif", tagDocument4);

        ArrayList<String> tagDocument5 = new ArrayList<>();
        tagDocument5.add("Tag5");
        Document document5 = new Document("Document5", Date.valueOf("2010-10-10"), "Journal du 10/10/2010", "Articles de journaux", "Politique", tagDocument5);

    
         //test insertion
        ConnectMySQL.insert(document1, db);
         
        ConnectMySQL.insert(document2, db);
         
        ConnectMySQL.insert(document3, db);
         
        ConnectMySQL.insert(document4, db);
         
        ConnectMySQL.insert(document5, db);

         //}
         db.close();
     }
}
