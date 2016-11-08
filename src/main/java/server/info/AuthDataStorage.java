package server.info;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.dao.ScoreDao;
import server.dao.TokenDao;
import server.dao.UserDao;

import javax.validation.constraints.NotNull;
import java.sql.SQLClientInfoException;
import java.util.List;
import java.util.Random;

public class AuthDataStorage{
    private static final Logger log = LogManager.getLogger(AuthDataStorage.class);
    private static UserDao credentials;
    private static TokenDao tokens;
    private static ScoreDao scores;

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        credentials = new UserDao();
        scores = new ScoreDao();
        tokens = new TokenDao();
        try {
            registerNewUser("admin", "admin");
        }
        catch(Exception e){
            log.info("Can't add user");
        }
    }

    public static void registerNewUser(@NotNull String user, @NotNull String password) throws Exception{
        Random rand = new Random();
        if (!credentials.getAllWhere("name = '"+user+"'").isEmpty())
            throw new SQLClientInfoException();
        credentials.insert(new User(user, password));
        scores.insert(new Leaderboard(user, rand.nextInt(500)));
    }

    public static String writeUsersJson() throws Exception {
        List<User> users = credentials.getAll();
        JsonNode root = mapper.readTree(mapper.writeValueAsString(users));
        for (JsonNode node : root){
            ((ObjectNode)node).remove("password");
            ((ObjectNode)node).remove("id");
        }
        return mapper.writeValueAsString(root);
    }

    public static String writeLeadersJson(int N) throws Exception {
        List<Leaderboard> scoresList = scores.getAllWithCondition("order by score desc LIMIT "+ N);
        return mapper.writeValueAsString(scoresList);
    }

    public static boolean authenticate(String user, String password) throws Exception {
        return credentials.passwordIsTrue(user, password);
    }

    public static Token issueToken(String user) throws Exception{
        List<Token> token = tokens.getAllWhere("userName = '" + user + "'");

        if (token.isEmpty()) {
            Token newToken = new Token(user);
            tokens.insert(newToken);
            return newToken;
        }
        return token.get(0);
    }

    public static void validateToken(String rawToken) throws Exception {
        Token token = new Token(Long.parseLong(rawToken));
        if(!tokens.tokenExists(token.getId())) {
            throw new Exception("Token validation exception");
        }
        log.info("Correct token!");
    }

    public static String logOut(Token token) throws Exception{
        List<Token> tokenList = tokens.getAllWhere("id = '" + token.getId() + "'");
        tokens.delete(token.getId());
        return tokenList.get(0).getUserName();
    }

    public static String changeName(Token token, String newName)
                                                    throws Exception{
        List<Token> tokenList = tokens.getAllWhere("id = '" + token.getId() + "'");
        String userName = tokenList.get(0).getUserName();
        List<Leaderboard> scoreList = scores.getAllWhere("userName = '"+userName+"'");
        credentials.updateName(userName, newName);
        tokens.updateName(token.getId(), newName);
        scores.updateName(userName, newName);
        return userName;
    }

    public static String changeEmail(Token token, String new_email)
            throws Exception{
        List<Token> tokenList = tokens.getAllWhere("id = '" + token.getId() + "'");
        String userName = tokenList.get(0).getUserName();
        credentials.updateEmail(userName, new_email);
        return userName;
    }

    public static String changePassword(Token token, String new_password)
            throws Exception{
        List<Token> tokenList = tokens.getAllWhere("id = '" + token.getId() + "'");
        String userName = tokenList.get(0).getUserName();
        credentials.updatePassword(userName, new_password);
        return userName;
    }

    public static boolean tokenExists(Long id) {
        return tokens.tokenExists(id);
    }
}