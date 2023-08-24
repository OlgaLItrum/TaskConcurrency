package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncArrayCalculation {

  private final CompletableFuture<int[]> futureReadFile;

  public AsyncArrayCalculation(CompletableFuture<int[]> futureReadFile) {
    this.futureReadFile = futureReadFile;
  }

  public CompletableFuture<List<FutureValue>> getCombinedFutures() {
    return getAllFuture().thenApply(aVoid ->
      getListFuture().stream().map(CompletableFuture::join).collect(Collectors.toList())
    );
  }

  private CompletableFuture<Void> getAllFuture() {
    List<CompletableFuture<FutureValue>> futureList = getListFuture();
    return CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
  }

  private List<CompletableFuture<FutureValue>> getListFuture() {
    return Arrays.asList(
      calculateMaxValue(),
      calculateMinValue(),
      calculateCountEven(),
      calculateCountUneven(),
      calculateCount()
    );
  }

  private CompletableFuture<FutureValue> calculateMaxValue() {
    return futureReadFile
      .thenApply(array -> {
        Thread.currentThread().setName("Max Value");
        return new FutureValue(
          Arrays.stream(array).max().orElseThrow(() -> new RuntimeException("Value not present")),
          Thread.currentThread().getName()
        );
      });
  }

  private CompletableFuture<FutureValue> calculateMinValue() {
    return futureReadFile
      .thenApply(array -> {
        Thread.currentThread().setName("Min Value");
        return new FutureValue(
          Arrays.stream(array).min().orElseThrow(() -> new RuntimeException("Value not present")),
          Thread.currentThread().getName()
        );
      });
  }

  private CompletableFuture<FutureValue> calculateCountEven() {
    return futureReadFile
      .thenApply(array -> {
        Thread.currentThread().setName("Count Even");
        return new FutureValue((int) Arrays.stream(array).filter(value -> value % 2 == 0).count(),
          Thread.currentThread().getName());
      });
  }

  private CompletableFuture<FutureValue> calculateCountUneven() {
    return futureReadFile
      .thenApply(array -> {
        Thread.currentThread().setName("Count Uneven");
        return new FutureValue((int) Arrays.stream(array).filter(value -> value % 2 != 0).count(),
          Thread.currentThread().getName());
      });
  }

  private CompletableFuture<FutureValue> calculateCount() {
    return futureReadFile
      .thenApply(array -> {
        Thread.currentThread().setName("Get Count");
        return new FutureValue((int) Arrays.stream(array).count(), Thread.currentThread().getName());
      });
  }
}
