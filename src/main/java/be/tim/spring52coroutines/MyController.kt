package be.tim.spring52coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.future
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
class MyController {

    private val logger = LoggerFactory.getLogger(MyController::class.java)

    @RequestMapping(path = ["/normal"], method = [RequestMethod.GET])
    fun normal(): ResponseEntity<ControllerResult> {
        logger.info(Thread.currentThread().name)
        return ResponseEntity.ok(ControllerResult("normal"))
    }

    //@RequestMapping(path = ["/executeCo"], method = [RequestMethod.GET])
    @GetMapping(path = ["/executeCo"])
    suspend fun executeCo(): ResponseEntity<ControllerResult> {
        logger.info(Thread.currentThread().name)
        delay(10)
        logger.info(Thread.currentThread().name)
        return ResponseEntity.ok(ControllerResult("test"))
    }

    @RequestMapping(path = ["/execute"], method = [RequestMethod.GET])
    fun execute(): CompletableFuture<ResponseEntity<ControllerResult>> {
        return GlobalScope.future {
            logger.info(Thread.currentThread().name)
            delay(10)
            logger.info(Thread.currentThread().name)
            ResponseEntity.ok(ControllerResult("test"))
        }
    }

    data class ControllerResult(val value: String)
}