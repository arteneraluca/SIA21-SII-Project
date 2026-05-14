package org.datasource.jdbc.jmh;

import org.datasource.jdbc.views.userratings.UserRatingSummaryViewBuilder;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Test_SQLJDBCView_JMH extends AbstractBenchmark {
    //

    public static UserRatingSummaryViewBuilder userRatingSummaryViewBuilder;

    @Autowired
    public static void setUserRatingSummaryViewBuilder(UserRatingSummaryViewBuilder userRatingSummaryViewBuilder) {
        Test_SQLJDBCView_JMH.userRatingSummaryViewBuilder = userRatingSummaryViewBuilder;
    }

    //
    @Benchmark
    public void test_UserRatingSummaryView(){
        System.out.println("userRatingSummaryViewBuilder is null? " + userRatingSummaryViewBuilder);
        //List<UserRatingSummaryView> viewList = userRatingSummaryViewBuilder.build().getViewList();
    }
}


/*
https://gist.github.com/msievers/ce80d343fc15c44bea6cbb741dde7e45

@Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 10)
    @Fork(1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)


 Options options = new OptionsBuilder()
                .include(this.getClass().getSimpleName()) // Include benchmarks in this class only
                .warmupIterations(3)                       // JVM warm-up iterations
                .measurementIterations(3)                  // Measurement iterations
                .forks(0)                                 // No forking to keep Spring context alive
                .threads(1)                               // Single thread for benchmarking
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .build();
 */
