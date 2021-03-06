/*
 * Copyright (c) 2011 - 2019, Apinauten GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this 
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.apiomat.nativemodule.salesmodule71;

import com.apiomat.nativemodule.IModel;
import com.apiomat.nativemodule.Level;
import com.sun.org.apache.xerces.internal.util.Status;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST class for your module
 */
@io.swagger.annotations.Api( value = "/SalesModule71", tags="SalesModule71" )
public class RestClass extends com.apiomat.nativemodule.AbstractRestResource
{
    /**
     * Constructor, leave as is
     *
     * @param uriInfo
     * @param servletRequest
     * @param securityContext
     * @param wsRequest
     */
    public RestClass( javax.ws.rs.core.UriInfo uriInfo, javax.servlet.http.HttpServletRequest servletRequest, javax.ws.rs.core.SecurityContext securityContext,
        javax.ws.rs.core.Request wsRequest )
    {
        super( uriInfo, servletRequest, securityContext, wsRequest );
    }

    /**
     * A simple ping-like GET endpoint.
     * You can pass a <PARAM> to the following URL, which is contained in the response then.
     *
     * curl <BASEURL>/yambas/rest/modules/SalesModule71/{appName}/spec/ping/<PARAM>
     *
     * The @ApiOperation and @ApiParam annotations are used to documnt the REST endpoint in the apidocs:
     * <BASEURL>/apidocs/index.html
     *
     * @return response
     */
    @io.swagger.annotations.ApiOperation( value = "A simple endpoint to get lead average score" )
    @javax.ws.rs.GET
    @javax.ws.rs.Path( "/leads/score" )
    public javax.ws.rs.core.Response getAverageScore()
    {
        final com.apiomat.nativemodule.Request request = this.getAOMRequest( );
        // extract auth information from the request object if needed
        System.out.println( request );

        if (request.getIsAccountRequest() || SalesModule71.AOM.checkUserRequestCredentials(request)) {
            IModel<?>[] leadsModel = SalesModule71.AOM.findByNames(request.getApplicationName(), Lead.MODULE_NAME, Lead.MODEL_NAME, "", request);
            final List<Lead> fields = Arrays.stream( leadsModel ).map(t -> ( Lead ) t).collect( Collectors.toList( ) );

            long average = 0;
            if (fields.size() != 0) {
                long sum = fields.stream().filter(lead -> lead.getScore() != null).mapToLong(Lead::getScore).sum();
                average = sum / fields.size();
            }

            SalesModule71.AOM.log(Level.INFO, "lead average is calculated: " + average);
            return javax.ws.rs.core.Response.ok( "average is : " + average ).type( javax.ws.rs.core.MediaType.TEXT_PLAIN ).build( );
        }

        SalesModule71.AOM.throwAuthenticationException("user is not allowed to see /leads/score endpoint");
        return javax.ws.rs.core.Response.status(Response.Status.FORBIDDEN).build( );
    }
}
