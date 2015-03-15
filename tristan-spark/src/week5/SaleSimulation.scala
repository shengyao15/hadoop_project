package week5

import java.io.{PrintWriter}
import java.net.ServerSocket
import scala.io.Source

object SaleSimulation {
  def index(length: Int) = {
    import java.util.Random
    val rdm = new Random

    rdm.nextInt(length)
  }

  def main(args: Array[String]) {

    val filename = "people.txt"
    val lines = Source.fromFile(filename).getLines.toList
    val filerow = lines.length

    val listener = new ServerSocket(9999)
    while (true) {
      val socket = listener.accept()
      new Thread() {
        override def run = {
          println("Got client connected from: " + socket.getInetAddress)
          val out = new PrintWriter(socket.getOutputStream(), true)
          while (true) {
            Thread.sleep(1000)
            val content = lines(index(filerow))
            println(content)
            out.write(content + '\n')
            out.flush()
          }
          socket.close()
        }
      }.start()
    }
  }
}