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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.net.URI;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/second")
@ApplicationScoped
public class SecondResource {

    @Inject
    Logger log;

    @PUT
    @LRA(value = LRA.Type.REQUIRED, end = true)
    public Response second(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, @QueryParam("error") boolean error) {
        log.infov("second");
        if (error) {
            return Response.status(400).build();
        }

        return Response.ok().build();
    }

    @Complete
    @Path("/complete")
    @PUT
    public Response complete(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        log.infov("second: completed");
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }

    @Compensate
    @Path("/compensate")
    @PUT
    public Response compensate(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        log.infov("second: compensated");
        return Response.ok(ParticipantStatus.Compensated.name()).build();
    }
}
