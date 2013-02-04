/*
 * Copyright (c) 2012. Midokura Japan K.K.
 */

package org.midonet.client.resource;

import org.midonet.client.WebResource;

import javax.ws.rs.core.MultivaluedMap;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Author: Tomoe Sugihara <tomoe@midokura.com>
 * Date: 8/17/12
 * Time: 5:50 PM
 */

/**
 * Base class for resource classes
 *
 * @param <T> concrete class
 * @param <U> type of principal DTO
 */
// TODO(pino): ResourceBase<T extends ResourceBase<T>, U>?
public abstract class ResourceBase<T extends ResourceBase, U> {

    final WebResource resource;
    U principalDto;
    final String mediaType;
    final URI uriForCreation;

    public ResourceBase(WebResource resource, URI uriForCreation,
                        U principalDto, String mediaType) {
        this.resource = resource;
        this.uriForCreation = uriForCreation;
        this.principalDto = principalDto;
        this.mediaType = mediaType;

    }

    /**
     * Gets URI of the resource
     *
     * @return URI of the resource
     */
    public abstract URI getUri();

    /**
     * Returns DTO of the resource. This doesn't invold REST API call, instead
     * returns principal DTO object so the client can get raw data.
     *
     * @return DTO of the resource
     */
    public U getLocalDto() {
        return principalDto;
    }

    <T, U> ResourceCollection<T>
    getChildResources(URI uri,
                      MultivaluedMap<String, String> queryParams,
                      String collectionMediaType,
                      Class<T> resourceClazz,
                      Class<U> newPrincipalDtoClazz) {
        return getChildResources(uri, queryParams, collectionMediaType, null,
                                 resourceClazz, newPrincipalDtoClazz);
    }

    /**
     * Get resources that belong to the resource.
     *
     * @param uri                  URI for getting resource(s)
     * @param queryParams          Query parameters; null for no queries
     * @param collectionMediaType  media type for collection of the resource
     * @param elementMediaType     media type for the element itself, if
     *                             resourceClazz' constructor needs it.
     * @param resourceClazz
     * @param newPrincipalDtoClazz
     * @param <T>                  Type of newly returned resources
     * @param <U>                  Type of DTO of newly returned resources
     * @return
     */
    <T, U> ResourceCollection<T>
    getChildResources(URI uri,
                      MultivaluedMap<String, String> queryParams,
                      String collectionMediaType,
                      String elementMediaType,
                      Class<T> resourceClazz,
                      Class<U> newPrincipalDtoClazz) {
        ResourceCollection<T> container =
            new ResourceCollection<T>(new ArrayList<T>());

        Class newPrincipalDtoArrayClazz = Array
            .newInstance(newPrincipalDtoClazz, 0).getClass();

        // newPrincipalDtoArrray should be T[]
        @SuppressWarnings("unchecked")
        T[] dtoArray = resource.get(uri,
                                    queryParams,
                                    (Class<T[]>) newPrincipalDtoArrayClazz,
                                    collectionMediaType);


        try {
            if (elementMediaType == null) {
                Constructor<T> ctor = resourceClazz.getConstructor(
                    WebResource.class,
                    URI.class,
                    newPrincipalDtoClazz);
                for (T e : dtoArray) {
                    container.add(ctor.newInstance(resource, uri, e));
                }
            } else {
                Constructor<T> ctor = resourceClazz.getConstructor(
                        WebResource.class,
                        URI.class,
                        newPrincipalDtoClazz,
                        String.class);
                for (T e : dtoArray) {
                    container.add(ctor.newInstance(resource, uri, e,
                                                   elementMediaType));
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return container;
    }

    /**
     * Given the uri, returns updated this resource after getting resource data
     * from API server.
     *
     * @param uri URI of the resource
     * @return this
     */
    public T get(URI uri) {
        if (uri == null)
            throw new IllegalArgumentException("The URI can't be null.");

        // principalDto should be type U as ctor defines.
        @SuppressWarnings("unchecked")
        Class<U> clazz = (Class<U>) principalDto.getClass();

        principalDto = resource.get(uri, null, clazz, mediaType);

        // "this" should be type T which is subtype of this class.
        @SuppressWarnings("unchecked")
        T updated = (T) this;
        return updated;
    }

    /**
     * Returns this after getting resource data from API server
     *
     * @return this
     */
    public T get() {
        return get(getUri());
    }

    /**
     * Creates a resource on API server by POSTing to the URI that is stored in
     * uriForCreation on the API server.
     *
     * @return this
     */
    public T create() {
        URI location = resource.post(uriForCreation, principalDto,
                                     mediaType);
        return get(location);
    }

    /**
     * Update the resource on API server with local DTO
     *
     * @return this
     */
    public T update() {
        resource.put(getUri(), principalDto, mediaType);
        return get(getUri());
    }

    /**
     * Delete the resource on API server
     */
    public void delete() {
        resource.delete(getUri());
    }
}