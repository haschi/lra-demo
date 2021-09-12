package com.example;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ParticipantStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.lang.annotation.Repeatable;
import java.net.URI;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/first")
@ApplicationScoped
public class FirstResource {

    @Inject
    Logger log;

    @PUT
    @LRA(value = LRA.Type.REQUIRES_NEW, end = false)
    public String first(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraID) {
        log.infov("first");
        return lraID.toASCIIString();
    }

    @Complete
    @Path("/complete")
    @PUT
    public Response complete(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        log.infov("first: completed");
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Compensate
    @Path("/compensate")
    @PUT
    public Response compensate(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        log.infov("first: compensated");
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }
}
