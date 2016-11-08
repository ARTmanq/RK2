package server.api;

import org.junit.Test;
import server.info.AuthDataStorage;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Artem on 11/8/16.
 */

public class AllTests {

    static private Long tokenRoma;
    static private Long tokenValya;
    static private Long tokenArtem;

    @Test
    public void registrationTest(){
        CurlRequests.register("andrei", "password");
        CurlRequests.register("semen", "password");
        CurlRequests.register("roma", "password");
        CurlRequests.register("valya", "password");
        CurlRequests.register("artem", "password");
        assertEquals(6, AuthDataStorage.getUsers().size());

        CurlRequests.register("andrei", "pass");
        assertEquals(6, AuthDataStorage.getUsers().size());
        assertEquals("password", AuthDataStorage.getUsersWhere("name = 'andrei'").get(0).getPassword());


    }

    @Test
    public void loginTest(){
        tokenRoma = CurlRequests.login("roma", "password");
        tokenValya = CurlRequests.login("valya", "password");
        tokenArtem = CurlRequests.login("artem", "password");
        assertEquals(3, AuthDataStorage.getTokens().size());

        CurlRequests.login("dmitriy", "p");
        CurlRequests.login("valya", "p");
        assertEquals(3, AuthDataStorage.getTokens().size());


    }

    @Test
    public void changeDataTest(){
        tokenRoma = CurlRequests.login("roma", "password");
        tokenValya = CurlRequests.login("valya", "password");
        tokenArtem = CurlRequests.login("artem", "password");
        CurlRequests.changeName(tokenRoma, "RomaMitenkov");
        CurlRequests.changeEmail(tokenValya, "mail@mail.ru");
        CurlRequests.changePassword(tokenArtem, "1234");

        assertEquals(1, AuthDataStorage.getUsersWhere("name = 'RomaMitenkov'").size());
        assertEquals(1, AuthDataStorage.getTokensWhere("userName = 'RomaMitenkov'").size());
        assertEquals(1, AuthDataStorage.getScoresWhere("userName = 'RomaMitenkov'").size());

        assertEquals(1, AuthDataStorage.getUsersWhere("email = 'mail@mail.ru'").size());
        assertEquals(1, AuthDataStorage.getUsersWhere("password = '1234'").size());

        assertEquals(6, AuthDataStorage.getUsers().size());
        assertEquals(6, AuthDataStorage.getScores().size());
        assertEquals(3, AuthDataStorage.getTokens().size());

        CurlRequests.changeName(new Long(1234567890), "abcdef");
        assertEquals(0, AuthDataStorage.getUsersWhere("name = 'abcdef'").size());

        CurlRequests.changeName(tokenArtem, "RomaMitenkov");
        assertEquals(1, AuthDataStorage.getUsersWhere("name = 'artem'").size());
    }

    @Test
    public void getDataTest(){
        System.out.println(CurlRequests.getLeaders(3));
        System.out.println(CurlRequests.getUsers());
    }

    @Test
    public void logOutTest(){
        CurlRequests.logOut(new Long(1234567890));
        assertEquals(3, AuthDataStorage.getTokens().size());

        tokenRoma = CurlRequests.login("RomaMitenkov", "password");
        tokenValya = CurlRequests.login("valya", "password");
        tokenArtem = CurlRequests.login("artem", "1234");
        CurlRequests.logOut(tokenRoma);
        CurlRequests.logOut(tokenValya);
        CurlRequests.logOut(tokenArtem);
        assertEquals(0, AuthDataStorage.getTokens().size());
    }
}