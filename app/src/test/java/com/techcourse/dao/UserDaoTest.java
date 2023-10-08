package com.techcourse.dao;

import com.techcourse.config.DataSourceConfig;
import com.techcourse.domain.User;
import com.techcourse.support.jdbc.init.DatabasePopulatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    private UserDao userDao;
    private long userId;

    @BeforeEach
    void setup() {
        DatabasePopulatorUtils.execute(DataSourceConfig.getInstance());
        userDao = new UserDao(DataSourceConfig.getInstance());
        userDao.deleteAll();
        final var user = new User("gugu", "password", "hkkang@woowahan.com");
        userId = userDao.insert(user);
    }

    @Test
    void findAll() {
        final var users = userDao.findAll();

        assertThat(users).isNotEmpty();
    }

    @Test
    void findById() {
        final var user = userDao.findById(userId).get();

        assertThat(user.getAccount()).isEqualTo("gugu");
    }

    @Test
    void findByAccount() {
        final var account = "gugu";
        User user = userDao.findByAccount(account).get();

        assertThat(user.getAccount()).isEqualTo("gugu");
    }

    @Test
    void insert() {
        final var account = "insert-gugu";
        final var user = new User(account, "password", "hkkang@woowahan.com");
        long insertedId = userDao.insert(user);

        final var actual = userDao.findById(insertedId).get();

        assertThat(actual.getAccount()).isEqualTo(account);
        assertThat(actual.getEmail()).isEqualTo("hkkang@woowahan.com");
    }

    @Test
    void update() {
        final var newPassword = "password99";
        final var user = userDao.findById(userId).get();
        user.changePassword(newPassword);

        userDao.update(user);

        final var actual = userDao.findById(userId).get();

        assertThat(actual.getPassword()).isEqualTo(newPassword);
    }
}
