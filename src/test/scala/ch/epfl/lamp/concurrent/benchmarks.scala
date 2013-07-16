package ch.epfl.lamp.concurrent



import org.scalameter.api._



class benchmarks extends PerformanceTest.Regression with Serializable {
  def persistor = new SerializationPersistor
  override def reporter: Reporter = Reporter.Composite(
    new RegressionReporter(
      RegressionReporter.Tester.OverlapIntervals(),
      RegressionReporter.Historian.ExponentialBackoff()
    ),
    HtmlReporter(true)
  )

  val sizes = Gen.range("size")(1000000, 5000000, 1000000)
  val pars = Gen.exponential("par")(1, 8, 2)
  val values = for {
    sz <- sizes
    p <- pars
  } yield (sz, p)

  performance of "Generators" config(
    exec.benchRuns -> 30,
    exec.independentSamples -> 5,
    exec.jvmflags -> "-server -Xms2048m -Xmx2048m -XX:MaxPermSize=256m -XX:+UseCondCardMark"
  ) in {
    using(values) curve("OwnedIdGen") in { case (sz, p) =>
      val ownedId = OwnedIdGenerator()
      
      val threads = for (i <- 0 until p) yield new Thread {
        override def run() {
          val gen = ownedId.newAdapter
          var i = 0
          while (i < sz / p) {
            gen.generate()
            i += 1
          }
        }
      }

      threads.foreach(_.start())
      threads.foreach(_.join())
    }

    using(values) curve("ThreadLocalIdGen") in { case (sz, p) =>
      val threadLocal = ThreadLocalIdGenerator()
      
      val threads = for (i <- 0 until p) yield new Thread {
        override def run() {
          var i = 0
          while (i < sz / p) {
            threadLocal.generate()
            i += 1
          }
        }
      }

      threads.foreach(_.start())
      threads.foreach(_.join())
    }
  }

}