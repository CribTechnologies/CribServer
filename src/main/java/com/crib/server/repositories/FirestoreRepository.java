package com.crib.server.repositories;

import com.crib.server.FirebaseSetup;
import com.crib.server.common.patterns.DTO;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class FirestoreRepository<T extends DTO> implements IRepository<T> {

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
    private RepoResponse createOrUpdate(T object) {
        RepoResponse response = new RepoResponse();
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

    private RepoResponse createOrUpdateMany(List<T> objects) {
        RepoResponse response = new RepoResponse();
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

    // Reusable protected methods
    protected <T1> RepoResponse updateField(String id, String fieldName, T1 fieldValue) {
        RepoResponse response = new RepoResponse();
        try {
            getCollectionRef()
                    .document(id)
                    .update(fieldName, fieldValue)
                    .get();

            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    protected RepoResponse updateFields(String id, Map<String, Object> fields) {
        RepoResponse response = new RepoResponse();
        try {
            getCollectionRef()
                    .document(id)
                    .update(fields)
                    .get();

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
    public RepoResponse create(T object) {
        return createOrUpdate(object);
    }

    @Override
    public RepoResponse createMany(List<T> objects) {
        return createOrUpdateMany(objects);
    }

    @Override
    public RepoResponseWP<T> getById(String id) {
        RepoResponseWP<T> response = new RepoResponseWP<T>();
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
    public RepoResponseWP<List<T>> getManyByIds(List<String> ids) {
        RepoResponseWP<List<T>> response = new RepoResponseWP<>();
        List<String> unsuccessfulIds = new ArrayList<>();
        AtomicInteger successes = new AtomicInteger();
        AtomicInteger totalTasks = new AtomicInteger();
        try {
            List<T> objects = new ArrayList<>();
            List<Runnable> tasks = new ArrayList<>();
            for (String id : ids) {
                tasks.add(() -> {
                    totalTasks.getAndIncrement();
                    RepoResponseWP<T> partialResponse = getById(id);
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
    public RepoResponseWP<Stream<T>> getAll() {
        RepoResponseWP<Stream<T>> response = new RepoResponseWP<>();
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
    public RepoResponse update(T object) {
        return createOrUpdate(object);
    }

    @Override
    public RepoResponse updateMany(List<T> objects) {
        return createOrUpdateMany(objects);
    }

    @Override
    public RepoResponse delete(String id) {
        RepoResponse response = new RepoResponse();
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
    public RepoResponse deleteMany(List<String> ids) {
        RepoResponse response = new RepoResponse();
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
