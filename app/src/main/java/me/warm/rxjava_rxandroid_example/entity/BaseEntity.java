package me.warm.rxjava_rxandroid_example.entity;

/**
 * Created by warm on 17/6/23.
 */

public class BaseEntity<R> {
    private boolean error;

    private R results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public R getResults() {
        return results;
    }

    public void setResults(R results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
