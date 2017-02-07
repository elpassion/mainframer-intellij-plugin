package tools;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

final class FakeCall<T> implements Call<T> {
    private final Response<T> response;
    private final IOException error;
    private final AtomicBoolean canceled = new AtomicBoolean();
    private final AtomicBoolean executed = new AtomicBoolean();

    FakeCall(Response<T> response, IOException error) {
        if ((response == null) == (error == null)) {
            throw new AssertionError("Only one of response or error can be set.");
        }
        this.response = response;
        this.error = error;
    }

    @Override
    public Response<T> execute() throws IOException {
        if (!executed.compareAndSet(false, true)) {
            throw new IllegalStateException("Already executed");
        }
        if (canceled.get()) {
            throw new IOException("canceled");
        }
        if (response != null) {
            return response;
        }
        throw error;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        if (callback == null) {
            throw new NullPointerException("callback == null");
        }
        if (!executed.compareAndSet(false, true)) {
            throw new IllegalStateException("Already executed");
        }
        if (canceled.get()) {
            callback.onFailure(this, new IOException("canceled"));
        } else if (response != null) {
            callback.onResponse(this, response);
        } else {
            callback.onFailure(this, error);
        }
    }

    @Override
    public boolean isExecuted() {
        return executed.get();
    }

    @Override
    public void cancel() {
        canceled.set(true);
    }

    @Override
    public boolean isCanceled() {
        return canceled.get();
    }

    @Override
    public Call<T> clone() {
        return new FakeCall<>(response, error);
    }

    @Override
    public Request request() {
        if (response != null) {
            return response.raw().request();
        }
        return new Request.Builder().url("http://localhost").build();
    }
}