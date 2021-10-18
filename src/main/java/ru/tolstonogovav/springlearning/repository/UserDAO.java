package ru.tolstonogovav.springlearning.repository;

import org.springframework.stereotype.Repository;
import ru.tolstonogovav.springlearning.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUser(List<SearchCriteria> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root r = query.from(User.class);

        Predicate predicate = builder.conjunction();

        UserSearchQueryCriteriaConsumer searchConsumer =
                new UserSearchQueryCriteriaConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        List<User> result = entityManager.createQuery(query).getResultList();
        return result;
    }
    @Override
    public void save(User entity) {
        entityManager.persist(entity);
    }
}