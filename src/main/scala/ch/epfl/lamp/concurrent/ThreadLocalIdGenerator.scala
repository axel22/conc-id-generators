package ch.epfl.lamp.concurrent






class ThreadLocalIdGenerator {
  val underlying = new OwnedIdGenerator
  val localAdapter = new ThreadLocal[underlying.Adapter] {
    override def initialValue = underlying.newAdapter
  }

  def generate(): Long = localAdapter.get.generate
}


object ThreadLocalIdGenerator {
  val CHUNK_SIZE = 2048

  def apply() = new ThreadLocalIdGenerator
}