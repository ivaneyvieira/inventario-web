package br.com.pintos.framework.view.vaadin10
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object VaadinOnKotlin {
  fun init() = synchronized(this) {
    // TomEE also has by default 5 threads, so I guess this is okay :-D
    executor = Executors.newScheduledThreadPool(5, threadFactory)
    val plugins = pluginsLoader.toList()
    plugins.forEach { it.init() }
    log.info("Vaadin On Kotlin initialized with plugins ${plugins.map { it.javaClass.simpleName }}")
  }

  fun destroy() = synchronized(this) {
    if (isStarted) {
      pluginsLoader.forEach { it.destroy() }
      executor!!.shutdown()
      executor!!.awaitTermination(1, TimeUnit.DAYS)
      executor = null
    }
  }

  val isStarted: Boolean
    get() = synchronized(this) { executor != null }

  private var executor: ScheduledExecutorService? = null

  private fun checkStarted() {
    if (!isStarted) throw IllegalStateException("init() has not been called, or VaadinOnKotlin is already destroyed")
  }

  val asyncExecutor: ScheduledExecutorService
    get() = synchronized(this) { checkStarted(); executor!! }

  @Volatile
  var threadFactory: ThreadFactory = object : ThreadFactory {
    private val id = AtomicInteger()
    override fun newThread(r: Runnable): Thread? {
      val thread = Thread(r)
      thread.name = "async-${id.incrementAndGet()}"
      return thread
    }
  }

  internal val log = LoggerFactory.getLogger(javaClass)

  private val pluginsLoader = ServiceLoader.load(VOKPlugin::class.java)
}


fun <R> async(block: () -> R): Future<R> = VaadinOnKotlin.asyncExecutor.submit(Callable<R> {
  try {
    block.invoke()
  } catch (t: Throwable) {
    // log the exception - if nobody is waiting on the Future, the exception would have been lost.
    LoggerFactory.getLogger(block::class.java).error("Async failed: $t", t)
    throw t
  }
})

fun scheduleAtFixedRate(initialDelay: Duration, period: Duration, command: ()->Unit): ScheduledFuture<*> = VaadinOnKotlin.asyncExecutor.scheduleAtFixedRate(
  {
    try {
      command.invoke()
    } catch (t: Throwable) {
      // if nobody is using Future to wait for the result of this op, the exception is lost. better log it here.
      LoggerFactory.getLogger(command::class.java).error("Async failed: $t", t)
      throw t
    }
  }, initialDelay.toMillis(), period.toMillis(), TimeUnit.MILLISECONDS)