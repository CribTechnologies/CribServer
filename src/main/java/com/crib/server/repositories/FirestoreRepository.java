package com.crib.server.repositories;

import com.crib.server.FirebaseSetup;
import com.crib.server.common.patterns.DataTransferObject;
import com.crib.server.common.patterns.RepositoryResponse;
import com.crib.server.common.patterns.RepositoryResponseWithPayload;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class FirestoreRepository<T extends DataTransferObject> implements IRepository<T> {

    private String collectionName;
    private CollectionReference collectionRef;
    private Firestore database;

    public FirestoreRepository(String collectionName) {
        database = FirebaseSetup.getInstance().getDatabase();
        this.collectionName = collectionName;
        this.collectionRef = database.collection(collectionName);
    }

    // Getters
    protected String getCollectionName() {
        return collectionName;
    }

    protected Firestore getDatabase() {
        return database;
    }

    protected CollectionReference getCollectionRef() {
        return collectionRef;
    }

    // Abstract methods
    protected abstract Class<T> getDTOClass();

    // Reusable private methods
    private RepositoryResponse createOrUpdate(T object) {
        RepositoryResponse response = new RepositoryResponse();
        try {
            getCollectionRef()
                    .document(object.getId())
                    .set(object)
                    .get();

            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    private RepositoryResponse createOrUpdateMany(List<T> objects) {
        RepositoryResponse response = new RepositoryResponse();
        try {
            WriteBatch batch = getDatabase().batch();
            for (T object : objects) {
                batch.set(getCollectionRef().document(object.getId()), object);
            }
            batch.commit().get();
            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // Inherited base methods
    @Override
    public RepositoryResponse create(T object) {
        return createOrUpdate(object);
    }

    @Override
    public RepositoryResponse createMany(List<T> objects) {
        return createOrUpdateMany(objects);
    }

    @Override
    public RepositoryResponseWithPayload<T> getById(String id) {
        RepositoryResponseWithPayload<T> response = new RepositoryResponseWithPayload<T>();
        try {
            DocumentSnapshot document = getCollectionRef()
                    .document(id)
                    .get()
                    .get();

            if (document.exists()) {
                response.setPayload(document.toObject(getDTOClass()));
            }
            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepositoryResponseWithPayload<List<T>> getManyByIds(List<String> ids) {
        RepositoryResponseWithPayload<List<T>> response = new RepositoryResponseWithPayload<>();
        List<String> unsuccessfulIds = new ArrayList<>();
        AtomicInteger successes = new AtomicInteger();
        AtomicInteger totalTasks = new AtomicInteger();
        try {
            List<T> objects = new ArrayList<>();
            List<Runnable> tasks = new ArrayList<>();
            for (String id : ids) {
                tasks.add(() -> {
                    totalTasks.getAndIncrement();
                    RepositoryResponseWithPayload<T> partialResponse = getById(id);
                    if (partialResponse.isSuccessful()) {
                        objects.add(partialResponse.getPayload());
                        successes.getAndIncrement();
                    }
                    else {
                        unsuccessfulIds.add(id);
                    }
                });
            }
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (Runnable task : tasks) {
                executorService.execute(task);
            }
            executorService.shutdown();

            if (successes.get() == totalTasks.get()) {
                response.setSuccessful(true);
            }
            else {
                response.setSuccessful(false);
                response.setMessage(successes + " successes from " + totalTasks + " tasks;" + String.join(";", unsuccessfulIds));
            }
            response.setPayload(objects);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepositoryResponseWithPayload<Stream<T>> getAll() {
        RepositoryResponseWithPayload<Stream<T>> response = new RepositoryResponseWithPayload<>();
        try {
            List<QueryDocumentSnapshot> documents = getCollectionRef()
                    .get()
                    .get()
                    .getDocuments();

            Stream.Builder<T> streamBuilder = Stream.builder();
            for (QueryDocumentSnapshot document : documents) {
                streamBuilder.accept(document.toObject(getDTOClass()));
            }

            response.setSuccessful(true);
            response.setPayload(streamBuilder.build());
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepositoryResponse update(T object) {
        return createOrUpdate(object);
    }

    @Override
    public RepositoryResponse updateMany(List<T> objects) {
        return createOrUpdateMany(objects);
    }

    @Override
    public RepositoryResponse delete(String id) {
        RepositoryResponse response = new RepositoryResponse();
        try {
            getCollectionRef()
                    .document(id)
                    .delete()
                    .get();

            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepositoryResponse deleteMany(List<String> ids) {
        RepositoryResponse response = new RepositoryResponse();
        try {
            WriteBatch batch = getDatabase().batch();

            for (String id : ids) {
                batch.delete(getCollectionRef().document(id));
            }
            batch.commit().get();
            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
