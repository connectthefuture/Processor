/*
 * Copyright 2016 Martynas Jusevičius <martynas@graphity.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.graphity.processor.provider;

import org.apache.jena.ontology.Ontology;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.PerRequestTypeInjectableProvider;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import org.graphity.processor.model.TemplateCall;
import org.graphity.processor.util.TemplateMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
public class TemplateCallProvider extends PerRequestTypeInjectableProvider<Context, TemplateCall> implements ContextResolver<TemplateCall>
{

    private static final Logger log = LoggerFactory.getLogger(TemplateCallProvider.class);

    @Context UriInfo uriInfo;    
    @Context Providers providers;
    
    public TemplateCallProvider()
    {
        super(TemplateCall.class);
    }
    
    @Override
    public Injectable<TemplateCall> getInjectable(ComponentContext ic, Context a)
    {
	return new Injectable<TemplateCall>()
	{
	    @Override
	    public TemplateCall getValue()
	    {
                return getTemplateCall();
	    }
	};
    }

    @Override
    public TemplateCall getContext(Class<?> type)
    {
        return getTemplateCall();
    }
    
    public TemplateCall getTemplateCall()
    {
        return new TemplateMatcher(getOntology()).match(getUriInfo().getAbsolutePath(), getUriInfo().getBaseUri());
    }
    
    public UriInfo getUriInfo()
    {
        return uriInfo;
    }
    
    public Ontology getOntology()
    {
	ContextResolver<Ontology> cr = getProviders().getContextResolver(Ontology.class, null);
	return cr.getContext(Ontology.class);
    }
    
    public Providers getProviders()
    {
        return providers;
    }
}