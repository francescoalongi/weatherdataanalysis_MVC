package Utils;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public class MongoDBUtil {
    private static final MongoDatabase mongoDB = initMongo();

    private static MongoDatabase initMongo() {
        Configuration configuration = new Configuration("mongoDB.properties");
        MongoClient mongoClient = new MongoClient(configuration.getProperty("mongoDB.host"), Integer.parseInt(configuration.getProperty("mongoDB.port")));
        return mongoClient.getDatabase(configuration.getProperty("mongoDB.name"));

    }

    public static FindIterable<Document> executeSelect(Document filter, Collections collectionName) {
        MongoCollection<Document> collection = mongoDB.getCollection(collectionName.toString().toLowerCase());
        return collection.find(filter);
    }

    public static void executeInsert(Document doc, Collections collectionName) {
        MongoCollection<Document> collection = mongoDB.getCollection(collectionName.toString().toLowerCase());
        collection.insertOne(doc);
    }

    public static void executeInsert(List<Document> docs , Collections collectionName) {
        MongoCollection<Document> collection = mongoDB.getCollection(collectionName.toString().toLowerCase());
        collection.insertMany(docs);
    }

    public static AggregateIterable<Document> executeAggregate(List<Bson> agg, Collections collectionName) {
        MongoCollection<Document> collection = mongoDB.getCollection(collectionName.toString().toLowerCase());
        return collection.aggregate(agg);
    }
}