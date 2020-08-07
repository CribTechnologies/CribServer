package com.crib.server.repositories;

import com.crib.server.common.patterns.DTO;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;

import java.util.List;
import java.util.stream.Stream;

public interface IRepository<T extends DTO> {

    RepoResponse create(T object);
    RepoResponse createMany(List<T> objects);

    RepoResponseWP<T> getById(String id);
    RepoResponseWP<List<T>> getByIds(List<String> ids);
    RepoResponseWP<List<T>> getManyByIds(List<String> ids);
    RepoResponseWP<Stream<T>> getAll();

    RepoResponse update(T object);
    RepoResponse updateMany(List<T> objects);

    RepoResponse delete(String id);
    RepoResponse deleteMany(List<String> ids);
}
