package org.nerdcoding.example.http4k.utils.filter.exception

import org.http4k.core.Body
import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.KotlinxSerialization.auto
import org.slf4j.LoggerFactory

object ExceptionFilter {

    private val logger = LoggerFactory.getLogger(ExceptionFilter::class.java)

    operator fun invoke() = Filter { next -> {
        try {
            next(it)
        } catch (e: HttpStatusCodeException) {
            logger.error("HttpStatusCodeException: ", e)

            Body.auto<Problem>().toLens()(
                Problem(
                    type = it.header("host") ?: "no HTTP host header",
                    title = e.title,
                    status = e.status.code,
                    detail = e.detail,
                    instance = it.uri.path,
                ),
                Response(e.status)
            )
        } catch (t: Throwable) {
            logger.error("Unexpected Exception: ", t)

            Body.auto<Problem>().toLens()(
                Problem(
                    type = it.header("host") ?: "no HTTP host header",
                    title = "Unexpected server error",
                    status = Status.INTERNAL_SERVER_ERROR.code,
                    detail = t.message,
                    instance = it.uri.path,
                ),
                Response(Status.INTERNAL_SERVER_ERROR)
            )
        }
    }}

}