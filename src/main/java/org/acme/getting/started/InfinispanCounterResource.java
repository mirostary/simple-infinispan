package org.acme.getting.started;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;

@Path("/counter")
public class InfinispanCounterResource {

    protected AtomicInteger counter = new AtomicInteger(0);

    @Inject
    @Remote("mycache")
    RemoteCache<String, Integer> cache;

    @Path("/get-cache")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getCacheCounter() {
        return cache.get("counter");
    }

    @Path("/get-client")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getClientCounter() {
        return counter.get();
    }

    @Path("/increment-counters")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String incCounters() {
        int invocationClientNumber = counter.incrementAndGet();
        int invocationCacheNumber = cache.get("counter") + 1;
        cache.put("counter", invocationCacheNumber);
        return "Cache=" + invocationCacheNumber + " Client=" + invocationClientNumber;
    }
}