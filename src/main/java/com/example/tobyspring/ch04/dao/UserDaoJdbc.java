package com.example.tobyspring.ch04.dao;

import com.example.tobyspring.ch01.dao.UserDao;
import com.example.tobyspring.ch01.domain.User;
import com.example.tobyspring.ch04.DuplicateUserIdException;
import com.example.tobyspring.ch05.Level;
import com.example.tobyspring.ch07.SqlService;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao{

    private SqlService sqlService;

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

    public void add(User user){
        this.jdbcTemplate.update(sqlService.getSql("userAdd"),
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());

    }


    public User get(String id) {
        return this.jdbcTemplate.queryForObject(sqlService.getSql("userGet"),
                new Object[] {id}, this.userMapper);

    }


    public List<User> getAll() {
        return this.jdbcTemplate.query(sqlService.getSql("userGetAll"),
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
        this.jdbcTemplate.update(sqlService.getSql("userDeleteAll"));
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
        return this.jdbcTemplate.queryForObject(sqlService.getSql("userGetCount"), Integer.class);
    }

    public void update(User user) {
        this.jdbcTemplate.update(sqlService.getSql("userUpdate"),
                user.getName(), user.getPassword(),
                user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }
}
