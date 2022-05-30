package com.example.tobyspring.ch04.dao;

import com.example.tobyspring.ch01.dao.UserDao;
import com.example.tobyspring.ch01.domain.User;
import com.example.tobyspring.ch04.DuplicateUserIdException;
import com.example.tobyspring.ch05.Level;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao{

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            };

    public void add(final User user) throws DuplicateUserIdException{
        this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend, email) values(?, ?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());

    }


    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[] {id}, this.userMapper);

    }


    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id",
                this.userMapper);
    }

    public void deleteAll() {
//        this.jdbcTemplate.update(
//                new PreparedStatementCreator() {
//                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                        return con.prepareStatement("delete from users");
//                    }
//                }
//        );
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
//        return this.jdbcTemplate.query(new PreparedStatementCreator() {
//
//            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                return con.prepareStatement("select count(*) from users");
//            }
//        }, new ResultSetExtractor<Integer>() {
//            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//                rs.next();
//                return rs.getInt(1);
//            }
//        });
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void update(User user) {
        this.jdbcTemplate.update(
                "update users set name = ?, password = ?, level = ?, login = ?, " +
                        "recommend = ?, email = ? where id = ? ", user.getName(), user.getPassword(),
                user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }



}
