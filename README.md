

I ran `UnsafeCounter` three times in each of three sessions. The results were:


UnsafeCounter #1       value=3,732,291  correct=false  44 ms
UnsafeCounter #2       value=3,995,070  correct=false  2 ms
UnsafeCounter #3       value=8,000,000  correct=true   0 ms

UnsafeCounter #1       value=3,817,199  correct=false  41 ms
UnsafeCounter #2       value=4,008,067  correct=false  3 ms
UnsafeCounter #3       value=8,000,000  correct=true   0 ms

UnsafeCounter #1       value=4,628,766  correct=false  42 ms
UnsafeCounter #2       value=1,035,817  correct=false  3 ms
UnsafeCounter #3       value=8,000,000  correct=true   0 ms

The expected value is 8,000,000 because 8 threads each increment the counter 1,000,000 times. Six out of the nine runs produced incorrect results.

The results are different because thread scheduling changes between runs. Sometimes, two threads overwrite each other's updates. Sometimes, the counter reaches the expected value by chance. This does not mean that `UnsafeCounter` is thread-safe.

The measured time can also change because of JVM warm-up, CPU scheduling, and background processes. A result of 0 ms means the measured duration was less than one millisecond after converting from nanoseconds to whole milliseconds.


## 5. Bonus: LongAdderCounter

I also implemented `LongAdderCounter` using `LongAdder`.

`LongAdder` can be faster under heavy contention because it spreads updates across multiple internal cells instead of making every thread compete to update one shared value. The values are combined when `sum()` is called.

In my benchmark, `LongAdderCounter` reached 8,000,000 in 9 ms, making it the fastest correct implementation in this particular test.

## 6. Benchmark Results

UnsafeCounter #1       value=3,732,291  correct=false  44 ms
UnsafeCounter #2       value=3,995,070  correct=false  2 ms
UnsafeCounter #3       value=8,000,000  correct=true   0 ms
SynchronizedCounter    value=8,000,000  correct=true   181 ms
LockCounter            value=8,000,000  correct=true   84 ms
AtomicCounter          value=8,000,000  correct=true   120 ms
LongAdderCounter       value=8,000,000  correct=true   9 ms

These results show that different implementations have different performance on my computer. The timings may change depending on the computer, JVM, and thread scheduling. The unsafe counter's short execution time is not a fair performance advantage because it can lose updates.

## 7. Why Does Synchronizing Only `increment()` Still Leave a Bug?

Making only `increment()` synchronized still leaves a bug because `get()` is not synchronized, so another thread may read an old value before seeing the latest update. Synchronizing both methods ensures that updates and reads use the same lock and that changes are visible to other threads.

