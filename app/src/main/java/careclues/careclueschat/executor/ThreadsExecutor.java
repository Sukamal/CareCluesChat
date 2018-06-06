package careclues.careclueschat.executor;

import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadsExecutor {

    /*
     * Number of cores to decide the number of threads
     */
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    /*
     * thread pool executor for background tasks
     */
    private final ThreadPoolExecutor mForBackgroundTasks;
    /*
     * thread pool executor for light weight background tasks
     */
    private final ThreadPoolExecutor mForLightWeightBackgroundTasks;
    /*
     * thread pool executor for main thread tasks
     */
    private final Executor mMainThreadExecutor;
    /*
     * an instance of DefaultExecutorSupplier
     */
    private static ThreadsExecutor sInstance;

    /*
     * returns the instance of DefaultExecutorSupplier
     */
    public static ThreadsExecutor getInstance() {
        if (sInstance == null) {
            synchronized (ThreadsExecutor.class) {
                sInstance = new ThreadsExecutor();
            }
        }
        return sInstance;
    }

        /*
         * constructor for  DefaultExecutorSupplier
         */
    private ThreadsExecutor() {

            // setting the thread factory
            ThreadFactory backgroundPriorityThreadFactory = new
                    PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

            // setting the thread pool executor for mForBackgroundTasks;
            mForBackgroundTasks = new ThreadPoolExecutor(
                    NUMBER_OF_CORES * 2,
                    NUMBER_OF_CORES * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    backgroundPriorityThreadFactory
            );

            // setting the thread pool executor for mForLightWeightBackgroundTasks;
            mForLightWeightBackgroundTasks = new ThreadPoolExecutor(
                    NUMBER_OF_CORES * 2,
                    NUMBER_OF_CORES * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    backgroundPriorityThreadFactory
            );

            // setting the thread pool executor for mMainThreadExecutor;
            mMainThreadExecutor = new MainThreadExecutor();
        }


    /*
     * returns the thread pool executor for background task
     */
    public ThreadPoolExecutor forBackgroundTasks() {
        return mForBackgroundTasks;
    }

    /*
     * returns the thread pool executor for light weight background task
     */
    public ThreadPoolExecutor forLightWeightBackgroundTasks() {
        return mForLightWeightBackgroundTasks;
    }

    /*
     * returns the thread pool executor for main thread task
     */
    public Executor forMainThreadTasks() {
        return mMainThreadExecutor;
    }
}
