package server.dao;

import jersey.repackaged.com.google.common.base.Joiner;
import server.info.User;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class UserDao implements Dao<User> {
    @Override
    public List<User> getAll(){
        return Database.selectTransactional(session -> session.createQuery("from User").list());
    }

    @Override
    public List<User> getAllWhere(String... hqlConditions){
        String totalCondition = Joiner.on(" and ").join(Arrays.asList(hqlConditions));
        return Database.selectTransactional(session ->session.createQuery("from User where "
                + totalCondition).list());

    }

    @Override
    public void insert(User user){
        Database.doTransactional(session -> session.save(user));
    }

    public void delete(@NotNull String userName){
        Database.doTransactional(session -> session.createQuery("DELETE User WHERE name = :name")
                .setParameter("name", userName)
                .executeUpdate());
    }

    public void update(@NotNull String userName, String newName) throws IllegalArgumentException{
        try {
            List<User> checkNewName = getAllWhere("name = " + newName);
        } catch (Exception e) {
            Database.doTransactional(session ->
                    session.createQuery("UPDATE User SET name = :newName WHERE name = :oldname")
                            .setParameter("oldname", userName)
                            .setParameter("newName", newName)
                            .executeUpdate());
        }
    }

    public boolean passwordIsTrue(@NotNull String userName, @NotNull String password){
        try {
            List<User> user = getAllWhere("name = '" + userName + "'");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
