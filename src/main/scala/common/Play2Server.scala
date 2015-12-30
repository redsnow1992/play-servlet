package play.core.server.servlet

import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import javax.servlet.ServletContext

import play.api._
import play.core.ApplicationProvider
import play.core.server.{Server, ServerWithStop}

import scala.util.control.NonFatal
import scala.util.{Success, Try}

object Play2WarServer {

  var playServer: Option[Play2WarServer] = None

  def apply(contextPath: Option[String] = None, mode: Mode.Mode = Mode.Dev) = {

    playServer = Option(new Play2WarServer(new WarApplication(mode, contextPath)))

  }

  val context = ApplicationLoader.createContext(
    new Environment(new File("."), ApplicationLoader.getClass.getClassLoader, playServer.map(_.mode).getOrElse(Mode.Dev)))
  Logger.configure(context.environment)

  lazy val configuration = Play.current.configuration

  private val started = new AtomicBoolean(true)

  def stop(sc: ServletContext) = {
    synchronized {
      if (started.getAndSet(false)) {
        playServer.foreach(_.stop())
        sc.log("Play server stopped")
      }
    }
  }

  def handleRequest(requestHandler: RequestHandler) = {

    playServer.fold {
      Logger("play").error("Play server as not been initialized. Due to a previous error ?")
    }(requestHandler(_))

  }
}

private[servlet] class Play2WarServer(appProvider: WarApplication) extends Server with ServerWithStop {

  private val requestIDs = new java.util.concurrent.atomic.AtomicLong(0)

  def mode = appProvider.mode

  def applicationProvider = appProvider

  // This isn't currently used for anything except local dev mode, so just stub this out for now
  lazy val mainAddress = ???
  override def httpPort: Option[Int] = ???
  override def httpsPort: Option[Int] = ???

  def newRequestId = requestIDs.incrementAndGet

  override def stop() = {
    Logger("play").info("Stopping play server...")

    try {
      Play.stop(appProvider.application)
    } catch {
      case NonFatal(e) => Logger("play").error("Error while stopping the application", e)
    }

    try {
      super.stop()
    } catch {
      case NonFatal(e) => Logger("play").error("Error while stopping akka", e)
    }
  }
}

private[servlet] class WarApplication(val mode: Mode.Mode, contextPath: Option[String]) extends ApplicationProvider {

  val applicationPath = Option(System.getProperty("user.home")).fold(new File("")){ new File(_) }

  val application: Application = {
    val environment = Environment(new File("."), Thread.currentThread.getContextClassLoader, mode)
    val initialSettings = contextPath
      .filterNot(_.isEmpty)
      .map(cp => cp + (if (cp.endsWith("/")) "" else "/"))
      .fold(Map.empty[String, AnyRef]) { cp ⇒
        Logger("play").info(s"Force Play 'application.context' to '$cp'")
        Map("application.context" -> cp)
      }
    val context = ApplicationLoader.createContext(environment, initialSettings = initialSettings)
    // Because of https://play.lighthouseapp.com/projects/82401-play-20/tickets/275, reconfigure Logger
    // without substitutions
    Logger.configure(context.environment)
    val loader = ApplicationLoader(context)
    loader.load(context)
  }

  Play.start(application)

  def get: Try[Application] = Success(application)
  def path = applicationPath
}
