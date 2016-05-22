package pranesh.realtytask;

/**
 * Created by pranesh on 14-May-16.
 */
public class Config {

    // used to share GCM regId with application server - using php app server
    static final String APP_SERVER_URL = "http://lifelineapp.hol.es/reality_task_api/gcm.php?shareRegId=1";

    // GCM server using java
    // static final String APP_SERVER_URL =
    // "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

    // Google Project Number
    //static final String GOOGLE_PROJECT_ID = "496325165726";

    static final String GOOGLE_PROJECT_ID = "864831634748";
    static final String MESSAGE_KEY = "m";

}
