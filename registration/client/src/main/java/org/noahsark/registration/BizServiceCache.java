package org.noahsark.registration;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.netty.handler.codec.compression.Bzip2Decoder;
import org.noahsark.registration.domain.CandidateService;
import org.noahsark.registration.domain.ServiceQuery;
import org.noahsark.server.rpc.Result;

import java.util.concurrent.ExecutionException;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/21
 */
public class BizServiceCache {

    private RegistrationClient client;

    private LoadingCache<Integer, Optional<CandidateService>> cache;

    public BizServiceCache(RegistrationClient client) {
        this();

        this.client = client;
    }

    public BizServiceCache() {
        init();
    }

    private void init() {
        CacheLoader<Integer, Optional<CandidateService>> loader = new CacheLoader<Integer, Optional<CandidateService>>() {
            @Override
            public Optional<CandidateService> load(Integer biz) throws Exception {

                ServiceQuery query = new ServiceQuery();
                query.setBiz(biz);

                Result<CandidateService> result = client.serviceLookup(query);
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

    public CandidateService get(Integer biz) throws ExecutionException {
        Optional<CandidateService> op = cache.get(biz);

        if (op.isPresent()) {
            return op.get();
        }

        return null;
    }

    public Optional<CandidateService> getOpSerice(Integer biz) throws ExecutionException {
        return cache.get(biz);
    }

    public void set(Integer biz, CandidateService service) {
        Preconditions.checkNotNull(service);

        cache.put(biz, Optional.of(service));
    }

    public void remove(Integer biz) {
        cache.invalidate(biz);
    }

    public void setClient(RegistrationClient client) {
        this.client = client;
    }
}
