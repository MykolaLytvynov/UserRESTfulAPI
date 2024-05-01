package ua.mykola.UserRESTfulAPI.dao;

import org.springframework.stereotype.Component;
import ua.mykola.UserRESTfulAPI.entity.User;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that simulates the operation of a database
 */
@Component
public class UserDefaultStorage implements UserRepository {
    private AtomicLong nextId = new AtomicLong(0);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        long newUserId = nextId.incrementAndGet();
        user.setId(newUserId);
        users.put(newUserId, user);
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean isExist(long id) {
        return users.containsKey(id);
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public User update(User updatedUser) {
        User foundUser = users.get(updatedUser.getId());
        foundUser.setEmail(foundUser.getEmail());

        if (updatedUser.getFirstName() != null) {
            foundUser.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            foundUser.setLastName(updatedUser.getLastName());
        }

        if (updatedUser.getAddress() != null) {
            foundUser.setAddress(updatedUser.getAddress());
        }

        if (updatedUser.getPhoneNumber() != null) {
            foundUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        if (updatedUser.getBirthDate() != null) {
            foundUser.setBirthDate(updatedUser.getBirthDate());
        }
        return foundUser;
    }
}
