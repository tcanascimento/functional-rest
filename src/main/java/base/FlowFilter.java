package base;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

class FlowFilter implements Flow.Subscriber<String> {

    private final CompletableFuture<Boolean> found;

    private final String term;
    private Flow.Subscription subscription;

    public FlowFilter(String term, CompletableFuture<Boolean> future){
        this.term = term;
        this.found = future;
    }


    public FlowFilter(String term) {
        this.term = term;
        this.found = new CompletableFuture<>();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String line) {
        if (!found.isDone() && line.contains(term))
            found.complete(true);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable ex) {
        found.completeExceptionally(ex);
    }

    @Override
    public void onComplete() {
        found.complete(false);
    }

    public CompletableFuture<Boolean> found() {
        return found;
    }
}
