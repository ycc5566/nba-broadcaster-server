import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author eason
 */
public class GetDB {

    MongoCollection mc;
    
    public GetDB() {
        MongoClient mongoClient = MongoClients.create(
//                "mongodb://Eason:momo0937262916@ds143573.mlab.com:43573/ds95702project4"); // testing
                "mongodb://Eason:mo1234@ds149495.mlab.com:49495/ds95702project4production"); //production
        MongoDatabase db = 
//                mongoClient.getDatabase("ds95702project4"); // testing
                mongoClient.getDatabase("ds95702project4production"); // production db
        
        // get my collections
        mc = db.getCollection("log");
    }
}
