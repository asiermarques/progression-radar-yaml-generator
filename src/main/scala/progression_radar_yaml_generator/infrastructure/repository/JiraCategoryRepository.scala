package progression_radar_yaml_generator.infrastructure.repository

import java.net.URI

import cats.effect.Sync
import cats.implicits._
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod}
import org.springframework.web.client.RestTemplate
import progression_radar_yaml_generator.domain.{Category, CategoryRepository}
import progression_radar_yaml_generator.infrastructure.dto.JiraResponseDTO

class JiraCategoryRepository[F[_]: Sync](
  restTemplate: RestTemplate,
  project: String,
  username: String,
  token: String
) extends CategoryRepository[F] {

  private val headers: HttpHeaders = new HttpHeaders()

  override def findAllCategories(): F[Seq[Category]] =
    for {
      request       <- createRequest
      categoriesDto <- doRequest(request)
      categories    <- mapToDomainEntity(categoriesDto)
    } yield categories

  private def createRequest: F[HttpEntity[String]] = Sync[F].delay {
    headers.setBasicAuth(username, token)
    new HttpEntity[String](headers)
  }

  private def doRequest(request: HttpEntity[String]): F[JiraResponseDTO] = Sync[F].delay(
    //restTemplate.exchange(new URI("https://google.com"), HttpMethod.GET, request, classOf[JiraResponseDTO]).getBody
    JiraResponseDTO(name = "test", "desciption")
  )

  private def mapToDomainEntity(responseDTO: JiraResponseDTO): F[Seq[Category]] = Sync[F].delay(Seq.empty[Category])
}
