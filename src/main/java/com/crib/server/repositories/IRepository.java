package com.crib.server.repositories;

import com.crib.server.common.patterns.DataTransferObject;
import com.crib.server.common.patterns.RepositoryResponse;
import com.crib.server.common.patterns.RepositoryResponseWithPayload;

import java.util.List;
import java.util.stream.Stream;

public interface IRepository<T extends DataTransferObject> {

    RepositoryResponse create(T object);
    RepositoryResponse createMany(List<T> objects);

    RepositoryResponseWithPayload<T> getById(String id);
    RepositoryResponseWithPayload<List<T>> getManyByIds(List<String> ids);
    RepositoryResponseWithPayload<Stream<T>> getAll();

    RepositoryResponse update(T object);
    RepositoryResponse updateMany(List<T> objects);

    RepositoryResponse delete(String id);
    RepositoryResponse deleteMany(List<String> ids);
}
