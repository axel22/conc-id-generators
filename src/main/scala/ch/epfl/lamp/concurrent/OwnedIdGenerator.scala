package ch.epfl.lamp.concurrent






class OwnedIdGenerator {
  private[concurrent] val count = new java.util.concurrent.atomic.AtomicLong

  class Adapter {
    private var from = 0L
    private var until = 0L
  
    def generate(): Long = {
      if (from < until) {
        val id = from
        from += 1
        id
      } else {
        import OwnedIdGenerator._
        from = count.getAndAdd(CHUNK_SIZE)
        until = from + CHUNK_SIZE
        generate()
      }
    }
  }

  def newAdapter = new Adapter

}


object OwnedIdGenerator {
  val CHUNK_SIZE = 2048

  def apply() = new OwnedIdGenerator
}