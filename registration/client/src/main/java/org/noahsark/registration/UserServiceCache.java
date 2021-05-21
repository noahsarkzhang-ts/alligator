package org.noahsark.registration;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.noahsark.registration.domain.CandidateService;
import org.noahsark.registration.domain.UserQuery;
import org.noahsark.server.rpc.Result;

import java.util.concurrent.ExecutionException;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/21
 */
public class UserServiceCache {

    private RegistrationClient client;

    private LoadingCache<String, Optional<CandidateService>> cache;

    public UserServiceCache(RegistrationClient client) {
        this();

        this.client = client;
    }

    public UserServiceCache() {
        init();
    }

    private void init() {
        CacheLoader<String, Optional<CandidateService>> loader = new CacheLoader<String, Optional<CandidateService>>() {
            @Override
            public Optional<CandidateService> load(String userId) throws Exception {

                UserQuery query = new UserQuery();
                query.setUserId(userId);

                Result<CandidateService> result = client.userLookup(query);
                CandidateService service = result.getData();

                if (service != null) {
                    return Optional.of(service);
                } else {
                    return Optional.fromNullable(null);
                }

            }
        };

        this.cache = CacheBuilder.newBuilder().maximumSize(1000).build(loader);
    }

    public CandidateService get(String userId) throws ExecutionException {
        Optional<CandidateService> op = cache.get(userId);

        if (op.isPresent()) {
            return op.get();
        }

        return null;
    }

    public Optional<CandidateService> getOpSerice(String userId) throws ExecutionException {
        return cache.get(userId);
    }

    public void set(String userId, CandidateService service) {
        Preconditions.checkNotNull(service);

        cache.put(userId, Optional.of(service));
    }

    public void remove(String userId) {
        cache.invalidate(userId);
    }

}
