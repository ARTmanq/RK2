package server.api;

import org.junit.Test;

/**
 * Created by Artem on 11/8/16.
 */

public class AllTests {

    static private Long tokenRoma;
    static private Long tokenValya;
    static private Long tokenArtem;

    @Test
    public void allTests() {
        registrationTest();
        loginTest();
        changeDataTest();
        getDataTest();
        logOutTest();
    }

    @Test
    public void registrationTest(){
        CurlRequests.register("andrei", "password");
        CurlRequests.register("semen", "password");
        CurlRequests.register("roma", "password");
        CurlRequests.register("valya", "password");
        CurlRequests.register("artem", "password");

    }

    @Test
    public void loginTest(){
        tokenRoma = CurlRequests.login("roma", "password");
        tokenValya = CurlRequests.login("valya", "password");
        tokenArtem = CurlRequests.login("artem", "password");
    }

    @Test
    public void changeDataTest(){
        tokenRoma = CurlRequests.login("roma", "password");
        tokenValya = CurlRequests.login("valya", "password");
        tokenArtem = CurlRequests.login("artem", "password");
        CurlRequests.changeName(tokenRoma, "RomaMitenkov");
        CurlRequests.changeEmail(tokenValya, "mail@mail.ru");
        CurlRequests.changePassword(tokenArtem, "1234");
    }

    @Test
    public void getDataTest(){
        System.out.println(CurlRequests.getLeaders(3));
        System.out.println(CurlRequests.getUsers());
    }

    @Test
    public void logOutTest(){
        tokenRoma = CurlRequests.login("RomaMitenkov", "password");
        tokenValya = CurlRequests.login("valya", "password");
        tokenArtem = CurlRequests.login("artem", "1234");

        CurlRequests.logOut(tokenRoma);
        CurlRequests.logOut(tokenValya);
        CurlRequests.logOut(tokenArtem);
    }
}
