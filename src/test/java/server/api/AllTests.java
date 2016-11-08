package server.api;

import org.junit.Test;

/**
 * Created by Artem on 11/8/16.
 */
public class AllTests {

    @Test
    public void allTests() {
        //CurlRequests.register("super", "password");
        Long token = CurlRequests.login("super", "password");
        CurlRequests.changeName(token, "superman");
        /*CurlRequests.logOut(token);*/
    }
}
